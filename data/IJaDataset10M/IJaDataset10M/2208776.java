package de.jaret.util.swt;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Simple class to collect allocated colors and to relese them.
 * 
 * @author Peter Kliem
 * @version $Id: ColorManager.java 242 2007-02-11 21:05:07Z olk $
 */
public class ColorManager {

    protected static Map<Device, ColorManager> _instances = new HashMap<Device, ColorManager>();

    protected Map<RGB, Color> _colorTable = new HashMap<RGB, Color>(10);

    protected Device _device;

    public ColorManager(Device device) {
        if (device != null) {
            _device = device;
        } else {
            _device = Display.getCurrent();
        }
    }

    public static ColorManager getColorManager(Device device) {
        ColorManager cm = _instances.get(device);
        if (cm == null) {
            cm = new ColorManager(device);
            _instances.put(device, cm);
        }
        return cm;
    }

    public static void disposeAll() {
        for (Device device : _instances.keySet()) {
            ColorManager cm = getColorManager(device);
            cm.dispose();
        }
        _instances.clear();
    }

    public void dispose() {
        for (Color color : _colorTable.values()) {
            color.dispose();
        }
    }

    public Color getColor(RGB rgb) {
        Color color = _colorTable.get(rgb);
        if (color == null) {
            color = new Color(_device, rgb);
            _colorTable.put(rgb, color);
        }
        return color;
    }
}
