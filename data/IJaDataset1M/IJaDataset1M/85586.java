package org.hip.vif.web.layout;

import java.util.Iterator;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hip.kernel.exc.VException;
import org.hip.vif.core.ApplicationConstants;
import org.hip.vif.core.bom.Member;
import org.hip.vif.core.bom.impl.BOMHelper;
import org.hip.vif.core.service.ApplicationData;
import org.hip.vif.core.service.PreferencesHandler;
import org.hip.vif.web.controller.PermissionRegistry;
import org.hip.vif.web.dash.VIFDashBoard;
import org.hip.vif.web.interfaces.IDashBoard;
import org.hip.vif.web.interfaces.IDisposable;
import org.hip.vif.web.internal.handler.ApplicationDash;
import org.hip.vif.web.util.RequestHandler;
import org.hip.vif.web.util.SkinRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.vaadin.Application;
import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Event;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;

/**
 * Base class for applications developed with VIF.
 * 
 * @author Luthiger
 * Created: 11.05.2011
 * @see Application
 */
@SuppressWarnings("serial")
public abstract class VIFApplication extends Application implements Component.Listener, HttpServletRequestListener {

    private static final Logger LOG = LoggerFactory.getLogger(VIFApplication.class);

    private VerticalLayout contentPane;

    private VIFLogin loginView;

    private VIFFooter footer;

    private RequestHandler requestHandler;

    private String requestURL;

    /**
	 * Subclasses must call this method in their <code>init()</code> method.
	 * 
	 * @param inMain {@link Window}
	 */
    protected void afterInit(Window inMain) {
        initialzeLayout(inMain);
        ApplicationData.create(this);
        ApplicationData.initLocale(PreferencesHandler.INSTANCE.getLocale(true, getLocale()), "VIFMessages");
        ApplicationData.setRequestURL(requestURL);
        ApplicationData.setWindow(inMain);
        ApplicationDash.create(this);
        PermissionRegistry.INSTANCE.createPermissions();
        setMainComponent(getLoginView());
        requestHandler = setRequestHandler(inMain);
        LOG.trace("Initialized VIF application.");
    }

    /**
	 * @return String the actor's language according to his language settings
	 */
    public Locale getActorLanguage() {
        try {
            Member lMember = BOMHelper.getMemberCacheHome().getMember(ApplicationData.getActor().getActorID());
            String outLanguage = lMember.getUserSettings(ApplicationConstants.USER_SETTINGS_LANGUAGE);
            return outLanguage == null ? ApplicationData.getLocale() : new Locale(outLanguage);
        } catch (Exception exc) {
            LOG.error("Error encountered while retrieving the actor's language user setting!", exc);
        }
        return ApplicationData.getLocale();
    }

    /**
	 * @param inMain
	 */
    private void initialzeLayout(Window inMain) {
        inMain.setStyleName("vif-window");
        inMain.addListener(new Window.CloseListener() {

            public void windowClose(CloseEvent inEvent) {
                close();
            }
        });
        VerticalLayout lLayout = new VerticalLayout();
        lLayout.setSizeFull();
        addHeader(lLayout);
        createContentPane(lLayout);
        addFooter(lLayout);
        inMain.getContent().setSizeFull();
        getMainWindow().setContent(lLayout);
        try {
            setTheme(SkinRegistry.INSTANCE.getActiveSkin().getSkinID());
        } catch (VException exc) {
            LOG.error("Run application with default skin.", exc);
            setTheme(ApplicationConstants.DFLT_SKIN);
        }
    }

    private RequestHandler setRequestHandler(Window inMain) {
        RequestHandler out = new RequestHandler();
        inMain.addParameterHandler(out);
        return out;
    }

    private void addHeader(VerticalLayout inLayout) {
        Layout lHeader = new HorizontalLayout();
        lHeader.setWidth("100%");
        lHeader.setHeight(80, Sizeable.UNITS_PIXELS);
        try {
            lHeader.addComponent(SkinRegistry.INSTANCE.getActiveSkin().getHeader());
        } catch (VException exc) {
            lHeader.addComponent(new Label("VIF Forum"));
            LOG.error("Could not display the skin's header!", exc);
        }
        inLayout.addComponent(lHeader);
        inLayout.setExpandRatio(lHeader, 0);
    }

    private void addFooter(VerticalLayout inLayout) {
        footer = new VIFFooter();
        inLayout.addComponent(footer);
        inLayout.setExpandRatio(footer, 0);
    }

    private void createContentPane(VerticalLayout inLayout) {
        contentPane = new VerticalLayout();
        contentPane.setStyleName("vif-content");
        contentPane.setSizeFull();
        inLayout.addComponent(contentPane);
        inLayout.setExpandRatio(contentPane, 1);
    }

    private void setMainComponent(Component inComponent) {
        contentPane.removeAllComponents();
        contentPane.addComponent(inComponent);
    }

    private Component getLoginView() {
        if (loginView == null) {
            loginView = new VIFLogin(this);
        }
        return loginView;
    }

    protected Component getContentPane() {
        return null;
    }

    public void componentEvent(Event inEvent) {
        if (loginView.checkSource(inEvent.getComponent())) {
            if (!isLoggedIn()) return;
            footer.setLoggedIn();
            showAfterLogin();
        }
    }

    /**
	 * @return boolean <code>True</code> if the user logged in
	 */
    public boolean isLoggedIn() {
        return ApplicationData.getActor() != null;
    }

    private void showAfterLogin() {
        ApplicationData.initLocale(getActorLanguage(), "VIFMessages");
        Component lDashboard = getAfterLoginView();
        ApplicationDash.setDash((VIFDashBoard) lDashboard);
        setMainComponent(lDashboard);
        if (lDashboard instanceof IDashBoard) {
            if (!requestHandler.process((IDashBoard) lDashboard)) {
                ((IDashBoard) lDashboard).showDefault();
            }
        }
    }

    protected abstract Component getAfterLoginView();

    /**
	 * Refresh the application's view e.g. after the user changed the language.
	 */
    public void refreshDash() {
        showAfterLogin();
    }

    private void disposeComponents() {
        Iterator<Component> lComponents = contentPane.getComponentIterator();
        while (lComponents.hasNext()) {
            Component lComponent = lComponents.next();
            if (lComponent instanceof IDisposable) {
                ((IDisposable) lComponent).dispose();
            }
        }
    }

    public void onRequestStart(HttpServletRequest inRequest, HttpServletResponse inResponse) {
        requestURL = new String(inRequest.getRequestURL());
    }

    public void onRequestEnd(HttpServletRequest inRequest, HttpServletResponse inResponse) {
    }

    @Override
    public void close() {
        disposeComponents();
        super.close();
    }

    public abstract boolean getIsAdmin();
}
