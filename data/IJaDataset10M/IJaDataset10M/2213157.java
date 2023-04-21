package com.shimaging;

import java.util.HashMap;
import javax.swing.ImageIcon;

class IconCache {

    static HashMap<String, ImageIcon> cache = new HashMap<String, ImageIcon>();

    public static ImageIcon getIcon(String name) {
        ImageIcon icon = cache.get(name);
        if (icon == null) {
            try {
                icon = new ImageIcon(Thread.currentThread().getContextClassLoader().getResource(name));
                cache.put(name, icon);
            } catch (Exception ex) {
                System.out.println(ex.toString());
            }
        }
        return icon;
    }
}
