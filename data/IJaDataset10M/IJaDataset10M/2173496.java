package org.jfonia.connect5.gui;

import java.awt.Graphics2D;
import java.awt.Point;

/**
 * @author wijnand.schepens@hogent.be
 */
public interface IComponent {

    void paint(Graphics2D g);

    boolean contains(Point p);
}
