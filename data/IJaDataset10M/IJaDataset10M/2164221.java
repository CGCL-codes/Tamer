package org.magnesia.client.gui.data;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.prefs.Preferences;
import org.magnesia.client.gui.ClientConnection;

public class Properties {

    private static Preferences p = Preferences.userNodeForPackage(Properties.class);

    public static int getKeyModifiers(SHORTCUT kb) {
        return p.getInt(kb.name() + "_modifier", kb.getDefaultKeyModifier());
    }

    public static int getKeyCode(SHORTCUT kb) {
        return p.getInt(kb.name() + "_code", kb.getDefaultKeyCode());
    }

    public static void setKeyCode(SHORTCUT kb, int keycode) {
        p.putInt(kb.name() + "_code", keycode);
    }

    public static void setKeyModifier(SHORTCUT kb, int modifier) {
        p.putInt(kb.name() + "_modifier", modifier);
    }

    public static SHORTCUT getShortcut(int modifier, int keycode) {
        try {
            for (String s : p.keys()) {
                if (s.endsWith("_code") && p.getInt(s, -1) == keycode) {
                    String key = s.replaceAll("_code", "");
                    if (p.getInt(key + "_modifier", -1) == modifier) {
                        return SHORTCUT.valueOf(key);
                    }
                }
            }
        } catch (Exception e) {
        }
        for (SHORTCUT sc : SHORTCUT.values()) {
            if (sc.getDefaultKeyModifier() == modifier && sc.getDefaultKeyCode() == keycode) {
                return sc;
            }
        }
        return null;
    }

    public static int getThumbnailWidth() {
        return p.getInt("TB_WIDTH", org.magnesia.Constants.THUMBNAIL_WIDTH);
    }

    public static void setThumbnailWidth(int width) {
        if (ClientConnection.getConnection().isAuthenticated()) if (ClientConnection.getConnection().setThumbnailWidth(width)) p.putInt("TB_WIDTH", width);
    }

    public static Dimension getWindowSize() {
        Dimension d = null;
        try {
            String[] dim = p.get("WINDOW_SIZE", "800,600").split(",");
            d = new Dimension(Integer.parseInt(dim[0]), Integer.parseInt(dim[1]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (d == null) {
            d = new Dimension(800, 600);
        }
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        if (d.width > screen.width) d.width = screen.width;
        if (d.height > screen.height) d.height = screen.height;
        if (d.width < 150) d.width = 150;
        if (d.height < 150) d.height = 150;
        return d;
    }

    public static void setWindowSize(Dimension d) {
        p.put("WINDOW_SIZE", d.width + "," + d.height);
    }

    public static Point getWindowLocation() {
        Point point = null;
        try {
            String[] dim = p.get("WINDOW_LOCATION", "-1,-1").split(",");
            point = new Point(Integer.parseInt(dim[0]), Integer.parseInt(dim[1]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (point == null || point.x == -1 || point.y == -1) {
            return null;
        }
        return point;
    }

    public static void setWindowLocation(Point point) {
        p.put("WINDOW_LOCATION", point.x + "," + point.y);
    }

    public static boolean isAutofetchComments() {
        return p.getBoolean("AUTOFETCH_COMMENTS", false);
    }

    public static void setAutofetchComments(boolean b) {
        p.putBoolean("AUTOFETCH_COMMENTS", b);
    }

    public static boolean isAutoLogin() {
        return p.getBoolean("AUTO_LOGIN", false) && keepPassword();
    }

    public static boolean keepPassword() {
        return p.getBoolean("KEEP_PWD", false);
    }

    public static void setAutoLogin(boolean b) {
        p.putBoolean("AUTO_LOGIN", b);
        if (b) {
            setKeepPassword(b);
        }
    }

    public static void setKeepPassword(boolean b) {
        p.putBoolean("KEEP_PWD", b);
    }
}
