package org.jext.gui;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.util.ArrayList;
import com.jgoodies.clearlook.ClearLookManager;
import com.jgoodies.plaf.*;
import com.jgoodies.plaf.plastic.PlasticLookAndFeel;

class BundledSkinFactory implements SkinFactory {

    public Skin[] getSkins() {
        ArrayList skins = new ArrayList(8);
        skins.add(new PlasticSkin());
        skins.add(new MetalSkin());
        skins.add(new JextSkin());
        skins.add(new GenericSkin("Unix Motif Skin", "motif", "com.sun.java.swing.plaf.motif.MotifLookAndFeel"));
        if (!UIManager.getSystemLookAndFeelClassName().equals(UIManager.getCrossPlatformLookAndFeelClassName())) skins.add(new GenericSkin("Native Skin", "native", UIManager.getSystemLookAndFeelClassName()));
        addSkinIfPresent(skins, "MacOs Native Skin", "_macos", "javax.swing.plaf.mac.MacLookAndFeel");
        addSkinIfPresent(skins, "MacOs Native Skin", "macos", "com.sun.java.swing.plaf.mac.MacLookAndFeel");
        addSkinIfPresent(skins, "GTK Skin", "gtk", "com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        skins.add(new GenericSkin("Windows Native Skin", "windows", "com.sun.java.swing.plaf.windows.WindowsLookAndFeel") {

            public boolean isAvailable() {
                return (new com.sun.java.swing.plaf.windows.WindowsLookAndFeel()).isSupportedLookAndFeel();
            }
        });
        return (Skin[]) skins.toArray(new Skin[0]);
    }

    /**
   * Adds the descripted skin to the array list if the skins exists. The test is made against the
   * presence of the Look And Feel class on the system.
   * @return If the operation succeed, i.e. if the skin was added to the list
   */
    private boolean addSkinIfPresent(ArrayList skins, String description, String name, String lnfClass) {
        try {
            Class bytecode = Class.forName(lnfClass);
            if (bytecode != null) {
                skins.add(new GenericSkin(description, name, lnfClass));
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    private class MetalSkin extends Skin {

        public String getSkinName() {
            return "Standard Metal Skin";
        }

        public String getSkinInternName() {
            return "metal";
        }

        public void apply() throws Throwable {
        }
    }

    private class JextSkin extends Skin {

        public String getSkinName() {
            return "Jext Metal Skin";
        }

        public String getSkinInternName() {
            return "jext";
        }

        public void apply() throws Throwable {
        }
    }

    private class PlasticSkin extends Skin {

        private PlasticSettings settings = PlasticSettings.createDefault();

        private Object oldUIClassLoader;

        public String getSkinName() {
            return "Plastic Skin";
        }

        public String getSkinInternName() {
            return "plastic";
        }

        public void unapply() throws Throwable {
            UIManager.put("ClassLoader", oldUIClassLoader);
        }

        public void apply() throws Throwable {
        }
    }
}
