package org.chmuk.styles;

import java.awt.Color;

/**
 * @author julien gaffuri
 *
 */
public interface WithColorIntervalStyle {

    public Color getStartColor();

    public void setStartColor(Color startColor);

    public Color getEndColor();

    public void setEndColor(Color endColor);

    public Color getColor(int i);
}
