package net.sourceforge.sqlexplorer.sqleditor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * SQL color manager.
 */
public class SQLColorManager implements IColorManager, IColorManagerExtension {

    protected Map fKeyTable = new HashMap(10);

    protected Map fDisplayTable = new HashMap(2);

    public SQLColorManager() {
    }

    void dispose(Display display) {
        Map colorTable = (Map) fDisplayTable.get(display);
        if (colorTable != null) {
            Iterator e = colorTable.values().iterator();
            while (e.hasNext()) ((Color) e.next()).dispose();
        }
    }

    public Color getColor(RGB rgb) {
        if (rgb == null) return null;
        final Display display = Display.getCurrent();
        Map colorTable = (Map) fDisplayTable.get(display);
        if (colorTable == null) {
            colorTable = new HashMap(10);
            fDisplayTable.put(display, colorTable);
            display.disposeExec(new Runnable() {

                public void run() {
                    dispose(display);
                }
            });
        }
        Color color = (Color) colorTable.get(rgb);
        if (color == null) {
            color = new Color(Display.getCurrent(), rgb);
            colorTable.put(rgb, color);
        }
        return color;
    }

    public void dispose() {
    }

    public Color getColor(String key) {
        if (key == null) return null;
        RGB rgb = (RGB) fKeyTable.get(key);
        return getColor(rgb);
    }

    public void bindColor(String key, RGB rgb) {
        Object value = fKeyTable.get(key);
        if (value != null) throw new UnsupportedOperationException();
        fKeyTable.put(key, rgb);
    }

    public void unbindColor(String key) {
        fKeyTable.remove(key);
    }
}
