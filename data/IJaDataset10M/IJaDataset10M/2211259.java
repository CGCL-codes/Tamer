package jugglinglab.core;

import java.awt.*;
import java.net.*;

public class VersionSpecific {

    protected static VersionSpecific vs = null;

    protected static URL[] images = null;

    public static VersionSpecific getVersionSpecific() {
        if (vs == null) {
            if (getJavaVersion() >= 2) {
                try {
                    Object vso = Class.forName("jugglinglab.core.VersionSpecific2").newInstance();
                    vs = (VersionSpecific) vso;
                } catch (ClassNotFoundException e) {
                    vs = null;
                } catch (SecurityException e) {
                    vs = null;
                } catch (IllegalAccessException e) {
                    vs = null;
                } catch (InstantiationException e) {
                    vs = null;
                }
            }
        }
        if (vs == null) vs = new VersionSpecific();
        return vs;
    }

    public static int getJavaVersion() {
        String version = System.getProperty("java.version");
        char ch = version.charAt(2);
        return Character.digit(ch, 10);
    }

    public static void setDefaultPropImages(URL[] is) {
        images = is;
    }

    public void setAntialias(Graphics g) {
    }

    public void setColorTransparent(Graphics g) {
        g.setColor(Color.white);
    }

    public Image makeImage(Component comp, int width, int height) {
        Image image = comp.createImage(width, height);
        Graphics tempg = image.getGraphics();
        tempg.setColor(comp.getBackground());
        tempg.fillRect(0, 0, width, height);
        return image;
    }

    public URL getDefaultPropImage() {
        return images[0];
    }
}
