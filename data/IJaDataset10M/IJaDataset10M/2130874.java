package org.vastenhouw.skin;

import java.io.File;
import java.awt.Frame;
import java.net.URL;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import org.vastenhouw.util.StringUtil;
import org.vastenhouw.util.FileUtil;
import org.vastenhouw.util.Debug;
import com.l2fprod.gui.plaf.skin.Skin;
import com.l2fprod.gui.plaf.skin.CompoundSkin;
import com.l2fprod.gui.plaf.skin.SkinLookAndFeel;

public class SkinUtil {

    private SkinUtil() {
    }

    public static void setSkinLookAndFeel(String lookAndFeel, String skinResource) {
        String desiredSkin = skinResource;
        try {
            File skinFile = new File(skinResource);
            boolean found = false;
            if (!skinFile.isAbsolute()) {
                String thisClassName = SkinUtil.class.getName();
                int lastDotIndex = thisClassName.lastIndexOf('.');
                thisClassName = thisClassName.substring(lastDotIndex + 1) + ".class";
                URL thisClassURL = SkinUtil.class.getResource(thisClassName);
                if (thisClassURL.getProtocol().equals("jar")) {
                    String jarLocation = FileUtil.getJarLocation(thisClassURL, false);
                    if (jarLocation != null) {
                        URL skinURL = new URL(jarLocation + '/' + skinResource);
                        desiredSkin = skinURL.getFile();
                        found = (new File(desiredSkin)).exists();
                        if (found) {
                            if (Debug.INFO) Debug.out.println("using separate skin, at same location as jar: " + desiredSkin);
                        }
                    }
                }
                if (!found) {
                    String classpath = System.getProperty("java.class.path");
                    classpath = classpath.replace('\\', '/');
                    StringTokenizer tk = new StringTokenizer(classpath, "/");
                    while (tk.hasMoreTokens()) {
                        String path = tk.nextToken();
                        if (path.endsWith(skinResource)) {
                            found = (new File(path)).exists();
                            if (found) {
                                desiredSkin = path;
                                if (Debug.INFO) Debug.out.println("using skin from classpath: " + desiredSkin);
                                break;
                            }
                        }
                    }
                }
            }
            if (!found) {
                desiredSkin = SkinUtil.class.getResource(skinResource).toString();
                if (Debug.INFO) Debug.out.println("using defaultSkin from resources: " + desiredSkin);
            }
            Skin skin = new CompoundSkin(SkinLookAndFeel.loadThemePack(new URL(desiredSkin)), SkinLookAndFeel.loadThemePack(new URL(desiredSkin)));
            SkinLookAndFeel.setSkin(skin);
            UIManager.setLookAndFeel(new SkinLookAndFeel());
        } catch (Exception ex) {
            ex.printStackTrace(Debug.out);
        }
    }

    public static void setLookAndFeel(String lafClass) {
        try {
            if (!StringUtil.isEmpty(lafClass)) {
                if (Debug.INFO) Debug.out.println("LAF set to: " + lafClass);
                if (SkinLookAndFeel.class.getName().equals(lafClass)) {
                    SkinUtil.setSkinLookAndFeel(null, "JPhotarSkin.zip");
                } else {
                    UIManager.setLookAndFeel(lafClass);
                }
                Frame rootFrame = JOptionPane.getRootFrame();
                if (rootFrame != null) {
                    SwingUtilities.updateComponentTreeUI(rootFrame);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace(Debug.out);
        }
    }

    public static LookAndFeel getSkinLAF() {
        return new SkinLookAndFeel();
    }
}
