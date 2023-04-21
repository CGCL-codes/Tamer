package org.zaval.awt;

import java.awt.*;

public interface ScrollObject {

    public Point getSOLocation();

    public void setSOLocation(int x, int y);

    public Dimension getSOSize();

    public Component getScrollComponent();
}
