package com.jlect.swebing.ui.laf.basic.client;

import com.jlect.swebing.ui.client.GComponent;

/**
 * Class defines methods to know the size of the string
 *
 * @author Sergey Kozmin
 * @since 04.12.2007 23:09:38
 */
public class FontMetrics {

    private GComponent c;

    private FontMetrics(GComponent c) {
        this.c = c;
    }

    public static FontMetrics createFontMetric(GComponent c) {
        return new FontMetrics(c);
    }

    public int getWidth(String string) {
        return string.length() * 10;
    }

    public int getHeight(String string) {
        return 15;
    }
}
