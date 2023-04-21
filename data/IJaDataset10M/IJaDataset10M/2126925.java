package org.middleheaven.ui.models;

import org.middleheaven.process.AttributeContext;
import org.middleheaven.ui.UIClient;
import org.middleheaven.ui.UIComponent;
import org.middleheaven.ui.desktop.swing.SwingRenderKit;
import org.middleheaven.ui.events.UIFocusEvent;

/**
 * Desktop based UIClient. Allows to build window based applications.
 * The specific graphic environment (Swing, SWT, ...) is not defined
 * by this class. 
 * 
 * @author Sergio Taborda
 */
public abstract class DesktopClientModel extends AbstractUIClientModel {

    public DesktopClientModel() {
        this.setRenderKit(new SwingRenderKit());
    }

    @Override
    public final void onFocusGained(UIFocusEvent event) {
    }

    @Override
    public final void onFocusLost(UIFocusEvent event) {
    }

    /**
	 * 
	 * @return Unrendered UIWindow
	 */
    public abstract UIComponent defineMainWindow(UIClient client, AttributeContext context);

    /**
	 * Splash window. If unrendered will be rendered 
	 * @param client 
	 * @return splash window.
	 */
    public abstract UIComponent defineSplashWindow(UIClient client, AttributeContext context);
}
