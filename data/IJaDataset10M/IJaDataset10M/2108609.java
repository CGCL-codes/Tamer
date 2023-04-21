package org.taHjaj.wo.swingk.util;

import java.awt.Dimension;
import java.awt.Toolkit;

public class GUIUtil {

    private GUIUtil() {
        super();
    }

    public static final Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }
}
