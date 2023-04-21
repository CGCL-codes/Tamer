package org.formaria.laf;

import javax.swing.UIManager;

/**
 * Attempt to install the Windows LAF
 * <p> Copyright (c) Formaria Ltd., 2008</p>
 * <p> $Revision: 1.1 $</p>
 * <p> License: see License.txt</p>
 */
public class WindowsLafInstaller {

    /**
   * Install the look and feel
   */
    public static void installLaf() {
        try {
            String lafName = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
            UIManager.setLookAndFeel(lafName);
        } catch (Exception e) {
            System.err.println("Can't set look & feel:" + e);
        }
    }
}
