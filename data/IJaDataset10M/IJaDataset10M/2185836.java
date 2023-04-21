package de.mindcrimeilab.swing.util;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import de.mindcrimeilab.xsanalyzer.util.LogHelper;

/**
 * @author Michael Engelhardt<me@mindcrime-ilab.de>
 * @author $Author:me $
 * @version $Revision:62 $
 * 
 */
public class IconProvider {

    private static final IconProvider instance = new IconProvider();

    private final ClassLoader loader;

    private final Map<String, Icon> iconCash = Collections.synchronizedMap(new WeakHashMap<String, Icon>());

    private IconProvider() {
        loader = this.getClass().getClassLoader();
    }

    public static Icon getIcon(String name) {
        return IconProvider.instance.getCachedIcon(name);
    }

    private Icon getCachedIcon(String name) {
        Icon icon = iconCash.get(name);
        if (null == icon) {
            LogHelper.finer("Loading icon...");
            icon = new ImageIcon(loader.getResource(name));
            iconCash.put(name, icon);
        }
        return icon;
    }
}
