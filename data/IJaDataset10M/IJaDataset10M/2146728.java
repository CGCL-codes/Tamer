package org.gwt.mosaic.desktop.client;

import org.gwt.mosaic.actions.client.ActionMap;
import org.gwt.mosaic.actions.client.MenuItemBindings;
import org.gwt.mosaic.actions.client.ToolButtonBindings;
import org.gwt.mosaic.application.client.Application;
import org.gwt.mosaic.application.client.ApplicationConstants;
import org.gwt.mosaic.application.client.ApplicationImageBundle;
import org.gwt.mosaic.application.client.CmdAction;
import org.gwt.mosaic.core.client.DOM;
import org.gwt.mosaic.core.client.util.DelayedRunnable;
import org.gwt.mosaic.pagebus.client.PageBus;
import org.gwt.mosaic.pagebus.client.SubscriptionCallback;
import org.gwt.mosaic.ui.client.DesktopPanel;
import org.gwt.mosaic.ui.client.InfoPanel;
import org.gwt.mosaic.ui.client.MessageBox;
import org.gwt.mosaic.ui.client.PopupMenu;
import org.gwt.mosaic.ui.client.ToolBar;
import org.gwt.mosaic.ui.client.ToolButton;
import org.gwt.mosaic.ui.client.Viewport;
import org.gwt.mosaic.ui.client.ToolButton.ToolButtonStyle;
import org.gwt.mosaic.ui.client.layout.BoxLayout;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData;
import org.gwt.mosaic.ui.client.layout.LayoutPanel;
import org.gwt.mosaic.ui.client.layout.BoxLayout.Orientation;
import org.gwt.mosaic.ui.client.layout.BoxLayoutData.FillStyle;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtDE extends Application implements EntryPoint {

    public static final GwtDEImageBundle IMAGES = GWT.create(GwtDEImageBundle.class);

    private DesktopPanel desktopPanel = new DesktopPanel();

    private ToolBar toolBar = new ToolBar();

    public DesktopPanel getDesktopPanel() {
        return desktopPanel;
    }

    /**
   * This is the entry point method.
   */
    public void onModuleLoad() {
        Application.launch(this, (ApplicationConstants) GWT.create(GwtDEConstants.class), (ApplicationImageBundle) GWT.create(GwtDEImageBundle.class));
    }

    @Override
    protected void initialize() {
        super.initialize();
        PageBus.subscribe("**", new SubscriptionCallback() {

            public void execute(String subject, Object message) {
                InfoPanel.show(subject, "Message: " + message);
            }
        });
    }

    @Override
    protected void startup() {
        final Viewport viewport = new Viewport(new BoxLayout(Orientation.VERTICAL));
        final LayoutPanel layoutPanel = viewport.getLayoutPanel();
        layoutPanel.setPadding(0);
        layoutPanel.setWidgetSpacing(0);
        layoutPanel.add(toolBar, new BoxLayoutData(FillStyle.HORIZONTAL));
        layoutPanel.add(desktopPanel, new BoxLayoutData(FillStyle.BOTH));
        final ActionMap actionMap = getContext().getActionMap();
        final ToolButtonBindings toolBtnActionSupport = new ToolButtonBindings(actionMap.get("hiCmd"));
        final ToolButton toolButton = toolBtnActionSupport.getTarget();
        toolButton.setStyle(ToolButtonStyle.SPLIT);
        toolBtnActionSupport.bind();
        toolButton.setMenu(createMenuBar());
        toolBar.add(toolButton);
        toolBar.addSeparator();
        viewport.attach();
    }

    @Override
    protected void ready() {
        final Widget splash = RootPanel.get("splash");
        if (splash != null) {
            new DelayedRunnable() {

                @Override
                public void run() {
                    DOM.setStyleAttribute(splash.getElement(), "display", "none");
                }
            };
        }
    }

    @CmdAction(description = "hiCmdDescription", image = "faceSmile")
    public void hiCmd() {
        ControlPanel.get().show();
    }

    @CmdAction
    public void aboutCmd() {
        MessageBox.info("About GwtDE", "GWT Desktop Environment");
    }

    public ToolBar getToolBar() {
        return toolBar;
    }

    private PopupMenu createMenuBar() {
        PopupMenu mainMenu = new PopupMenu();
        ActionMap actionMap = Application.getInstance().getContext().getActionMap();
        final MenuItemBindings aboutBinding = new MenuItemBindings(actionMap.get("aboutCmd"));
        mainMenu.addItem(aboutBinding.getTarget());
        aboutBinding.bind();
        return mainMenu;
    }
}
