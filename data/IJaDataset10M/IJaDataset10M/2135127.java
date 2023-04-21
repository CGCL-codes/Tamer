package scamsoft.squadleader.client;

import scamsoft.squadleader.classloaders.CoreMediaClassLoader;
import java.awt.*;
import java.io.IOException;
import java.net.URL;

/**
 * Utility class providing custom mouse cursors
 *
 * @author Andreas Mross
 *         created    September 24, 2001
 */
public class SLCursor {

    public static final Cursor NORMAL = new Cursor(Cursor.DEFAULT_CURSOR);

    public static final Cursor MOVE = createCursor("icon_move.gif");

    public static final Cursor NA = createCursor("icon_no.gif");

    public static final Cursor TARGET = createCursor("icon_targetb.gif");

    public static final Cursor ACTION = new Cursor(Cursor.HAND_CURSOR);

    private static Cursor createCursor(String filename) {
        ClassLoader classLoader = CoreMediaClassLoader.class.getClassLoader();
        URL imageURL = classLoader.getResource(filename);
        Image image = null;
        try {
            image = scamsoft.util.Toolkit.getImageFromURL(imageURL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Toolkit.getDefaultToolkit().createCustomCursor(image, new Point(16, 16), filename);
    }

    private SLCursor() {
    }
}
