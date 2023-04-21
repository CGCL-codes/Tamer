package org.xito.launcher.web;

import java.beans.*;
import javax.swing.*;
import org.xito.launcher.*;

/**
 *
 * @author DRICHAN
 */
public class WebActionBeanInfo extends SimpleBeanInfo {

    protected static ImageIcon icon16 = new ImageIcon(LauncherService.class.getResource("images/web_16.png"));

    protected static ImageIcon icon32 = new ImageIcon(LauncherService.class.getResource("images/web_32.png"));

    private BeanDescriptor desc;

    /** Creates a new instance of AppletActionBeanInfo */
    public WebActionBeanInfo() {
        desc = new BeanDescriptor(WebAction.class, WebConfigDialog.class);
        desc.setDisplayName(Resources.webBundle.getString("action.display.name"));
    }

    /**
    * Get the Icon
    */
    public java.awt.Image getIcon(int iconKind) {
        if (iconKind == ICON_COLOR_16x16 || iconKind == ICON_MONO_16x16) return icon16.getImage();
        if (iconKind == ICON_COLOR_32x32 || iconKind == ICON_MONO_32x32) return icon32.getImage();
        return null;
    }

    /**
    * Get the Bean Descriptor
    */
    public BeanDescriptor getBeanDescriptor() {
        return desc;
    }
}
