package com.crumbach.jcycledata.gui;

import com.crumbach.jcycledata.base.JCDResourceManager;
import com.crumbach.jcycledata.base.ResourceKeys;
import com.crumbach.tools.ResourceManager;
import java.net.URL;
import javax.swing.*;

/**
 *  This class offers the user different sets of icons to choose
 *
 * @author     Manfred Crumbach
 * @created    10. Februar 2002
 */
public class IconLocator {

    private static final ResourceManager rm = JCDResourceManager.getTheInstance();

    private static IconSet currentSet = IconSet.ICONSET_JLF_NORMAL;

    private IconLocator() {
    }

    /**
    *  Select the new set of icons
    *
    * @param  iset  The new IconSet
    */
    public static void setIconSet(IconSet iset) {
        currentSet = iset;
    }

    /**
    *  Gets the specified icon using the current Icon Set
    *
    * @param  sIcon  icon to be loaded
    * @return        The Icon value
    */
    public static ImageIcon getIcon(String sIcon) {
        URL url = rm.getResource(rm.getString(ResourceKeys.ICON_PATH) + currentSet.getPath() + rm.getString(sIcon));
        if (url == null) {
            url = rm.getResource(rm.getString(ResourceKeys.ICON_PATH) + rm.getString(sIcon));
        }
        return new ImageIcon(url);
    }

    /**
    *  Gets the specified icon using the specified Icon Set
    *
    * @param  sIcon        icon to be loaded
    * @param  iconSetSize  Description of Parameter
    * @return              The Icon value
    */
    public static ImageIcon getIcon(String sIcon, String iconSetSize) {
        URL url = null;
        url = rm.getResource(rm.getString(ResourceKeys.ICON_PATH) + currentSet.getPath(iconSetSize) + rm.getString(sIcon));
        if (url == null) {
            url = rm.getResource(rm.getString(ResourceKeys.ICON_PATH) + rm.getString(sIcon));
        }
        return new ImageIcon(url);
    }
}
