package edu.ucsd.ncmir.jinx.gui.cursors;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.net.URL;

/**
 *
 * @author spl
 */
public class JxCursorFactory {

    protected JxCursorFactory() {
    }

    private static Cursor zoom_in_cursor = null;

    private static final String ZOOM_IN = "/edu/ucsd/ncmir/jinx/gui/cursors/zoom_in.png";

    public static Cursor createZoomInCursor() {
        if (zoom_in_cursor == null) zoom_in_cursor = buildCursor(ZOOM_IN, "zoom_in");
        return zoom_in_cursor;
    }

    private static Cursor zoom_out_cursor = null;

    private static final String ZOOM_OUT = "/edu/ucsd/ncmir/jinx/gui/cursors/zoom_out.png";

    public static Cursor createZoomOutCursor() {
        if (zoom_out_cursor == null) zoom_out_cursor = buildCursor(ZOOM_OUT, "zoom_out");
        return zoom_in_cursor;
    }

    private static Cursor buildCursor(String path, String name) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        URL url = JxCursorFactory.class.getResource(path);
        Image image = toolkit.getImage(url);
        Point hotspot = new Point(7, 7);
        return toolkit.createCustomCursor(image, hotspot, name);
    }
}
