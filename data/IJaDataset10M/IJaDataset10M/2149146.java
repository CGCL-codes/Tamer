package tms.client;

import tms.client.accesscontrol.AccessController;
import tms.client.controls.WindowCloser;
import tms.client.controls.dialogs.AlertBox;
import tms.client.i18n.TMSConstants;
import tms.client.services.AppConfigService;
import tms.client.services.AppConfigServiceAsync;
import tms.client.services.result.DataCheckResult;
import tms.client.util.ErrorHandler;
import tms.client.util.FooterUtility;
import tms.client.util.HostedModeUtils;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry class for the Index.html page.
 * 
 * @author Werner Liebenberg
 * @author Wildrich Fourie
 * @author Ismail Lavangee
 *
 */
public class Welcome implements EntryPoint, AccessControlStateDependant, LoaderModule, WindowCloser {

    private static final TMSConstants constants = GWT.create(TMSConstants.class);

    private final AppConfigServiceAsync _app_config = GWT.create(AppConfigService.class);

    private boolean _is_registered = false;

    @Override
    public void onModuleLoad() {
        Loader.registerDependant(this);
    }

    private void initialize() {
        Window.setTitle(constants.title());
        Label titleLabel = new Label(constants.title());
        titleLabel.addStyleName("titleLabel");
        titleLabel.addStyleName("labelTextBold");
        Label link = new Label(constants.link(), false);
        link.setStyleName("hyperLink");
        link.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                gotoTermBrowser();
            }
        });
        HorizontalPanel h_panel = new HorizontalPanel();
        h_panel.setSpacing(3);
        h_panel.add(new HTML(constants.welcome()));
        h_panel.add(link);
        RootPanel.get("Title").add(titleLabel);
        RootPanel.get("welcome").add(h_panel);
        RootPanel.get("footer").add(FooterUtility.createIndexFooter());
        AccessController.registerDependant(this);
        setClosingHandler();
    }

    private void gotoTermBrowser() {
        WindowClosing.setWindowClosingState(false);
        if (!GWT.isScript()) Window.open(HostedModeUtils.insertPageAddress("termbrowser.jsp"), "_top", null); else Window.open("./termbrowser.jsp", "_top", null);
    }

    @Override
    public void updateAccessControlledStates() {
        if (AccessController.isSignedOn()) {
            gotoTermBrowser();
            setIsRegistered(true);
        }
    }

    @Override
    public boolean needAdminAccess() {
        return false;
    }

    @Override
    public void load() {
        _app_config.checkAppProperties(new AsyncCallback<DataCheckResult<Boolean>>() {

            @Override
            public void onFailure(Throwable caught) {
                ErrorHandler.handle(caught.getMessage());
            }

            @Override
            public void onSuccess(DataCheckResult<Boolean> result) {
                try {
                    if (!result.getResult()) AlertBox.show(constants.fault_minor(), result.getMessage(), false, true); else initialize();
                } catch (Exception e) {
                    ErrorHandler.handle(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean doneUpdatingControlledStates() {
        return _is_registered;
    }

    @Override
    public void setIsRegistered(boolean is_registered) {
        _is_registered = is_registered;
    }

    @Override
    public void setClosingHandler() {
        WindowClosing.close();
    }
}
