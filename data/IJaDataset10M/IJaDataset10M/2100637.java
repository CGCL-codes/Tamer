package org.dlib.gui.print;

import java.awt.Graphics;

public interface PrintRow {

    public void print(Graphics g, PrintInfo pi);

    public int getHeight();
}
