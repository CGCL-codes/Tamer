package net.sf.gham.core.entity.match.matchtablecolumn;

import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author  Fabio Collini
 */
public class WeatherCellRenderer extends DefaultTableCellRenderer {

    private static WeatherCellRenderer singleton;

    public static WeatherCellRenderer singleton() {
        if (singleton == null) {
            singleton = new WeatherCellRenderer();
        }
        return singleton;
    }

    private WeatherCellRenderer() {
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    protected void setValue(Object obj) {
        if (obj != null) {
            java.net.URL url = getClass().getResource("/icon/weather" + obj + ".png");
            if (url != null) setIcon(new ImageIcon(url));
        } else setIcon(null);
    }
}
