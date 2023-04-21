package org.gaea.ui.component;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JComponent;

/**
 * JLabel moving with a gradient color horizontally.
 * 
 * The parent of this JLabel must deal with the start and stop of the thread.
 * 
 * @author jsgoupil
 */
public class LineSeparatorGradient extends JComponent implements Runnable {

    /**
	 * Serial generated
	 */
    private static final long serialVersionUID = -2360665373093499838L;

    /**
	 * Colors of the gradient.
	 */
    private Color _color1, _color2;

    /**
	 * Moving speed. It's time in milliseconds waited in each move.
	 */
    private int _speed;

    /**
	 * Current position
	 */
    private float _x = 0;

    /**
	 * Holds the orientation
	 */
    private Orientation _orientation;

    /**
	 * Stop moving the bar if true
	 */
    public boolean _stop = false;

    /**
	 * Possible Orientations
	 */
    enum Orientation {

        HORIZONTAL, VERTICAL
    }

    /**
	 * Constructor with 2 colors and the speed. Horizontal orientation is used.
	 * 
	 * @param color1
	 *            Color 1
	 * @param color2
	 *            Color 2
	 * @param speed
	 *            Waiting time in milliseconds
	 * @param orientation
	 *            The Orientation of the gradient
	 */
    public LineSeparatorGradient(Color color1, Color color2, int speed, Orientation orientation) {
        _color1 = color1;
        _color2 = color2;
        _speed = speed;
        _orientation = orientation;
    }

    /**
	 * Constructor with 2 colors and the speed. Horizontal orientation is used.
	 * 
	 * @param color1
	 *            Color 1
	 * @param color2
	 *            Color 2
	 * @param speed
	 *            Waiting time in milliseconds
	 */
    public LineSeparatorGradient(Color color1, Color color2, int speed) {
        this(color1, color2, speed, Orientation.HORIZONTAL);
    }

    @Override
    public void paint(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (_orientation == Orientation.HORIZONTAL) {
            g2.setPaint(new GradientPaint(_x, 0, _color1, w / 2 + _x, 0, _color2, true));
        } else {
            g2.setPaint(new GradientPaint(0, _x, _color1, 0, w / 2 + _x, _color2, true));
        }
        g2.fillRect(0, 0, w, h);
    }

    public void run() {
        do {
            _x += 5;
            int cx = this.getWidth();
            if (cx != 0 && _x > cx) {
                _x = 0;
            }
            try {
                Thread.sleep(_speed);
            } catch (InterruptedException e) {
            }
            repaint();
        } while (_stop == false);
    }
}
