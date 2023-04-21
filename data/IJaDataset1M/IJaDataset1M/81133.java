package org.web3d.x3d;

import java.awt.Image;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.SimpleBeanInfo;
import org.openide.ErrorManager;
import org.openide.loaders.UniFileLoader;
import org.openide.util.ImageUtilities;
import org.openide.util.Utilities;

public class X3DLoaderBeanInfo extends SimpleBeanInfo {

    @Override
    public BeanInfo[] getAdditionalBeanInfo() {
        try {
            return new BeanInfo[] { Introspector.getBeanInfo(UniFileLoader.class) };
        } catch (IntrospectionException ie) {
            ErrorManager.getDefault().notify(ie);
            return null;
        }
    }

    /** @param type Desired type of the icon
   * @return returns the Image loader's icon
   */
    @Override
    public Image getIcon(final int type) {
        if ((type == java.beans.BeanInfo.ICON_COLOR_16x16) || (type == java.beans.BeanInfo.ICON_MONO_16x16)) {
            return ImageUtilities.loadImage("org/web3d/x3d/resources/x3dObject.png");
        } else {
            return ImageUtilities.loadImage("org/web3d/x3d/resources/x3dObject32.png");
        }
    }
}
