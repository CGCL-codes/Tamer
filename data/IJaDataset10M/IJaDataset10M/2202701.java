package net.sf.cyberrails.GUI;

import net.sf.cyberrails.Engine.Milepost;
import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;

public class MapPanel extends JPanel {

    private final AffineTransform _transform;

    private final AffineTransform _inverse;

    private final GraphicalBoard _gb;

    private Polygon _hex;

    private BufferedImage _bg = null;

    MapPanel(AffineTransform at, GraphicalBoard gb) {
        super(false);
        _transform = at;
        _inverse = new AffineTransform();
        try {
            _inverse.setTransform(_transform.createInverse());
        } catch (NoninvertibleTransformException excn) {
        }
        _gb = gb;
        _hex = null;
        MouseInputListener mil = new MapPanelMouseListener(this);
        addMouseListener(mil);
        addMouseMotionListener(mil);
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public Dimension getPreferredSize() {
        return new Dimension(_gb.getWidth() / 2, _gb.getHeight() / 2);
    }

    private final javax.swing.border.Border myBorder = javax.swing.BorderFactory.createRaisedBevelBorder();

    private boolean _inverseUpdateNeeded = false;

    public void updateInverse() {
        _inverseUpdateNeeded = true;
    }

    public void paintComponent(Graphics oldG) {
        boolean updateBG = false;
        if (_bg == null || _bg.getWidth() != getWidth() || _bg.getHeight() != getHeight()) {
            if (getWidth() <= 0 || getHeight() <= 0) return;
            updateBG = true;
            _bg = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        }
        if (_inverseUpdateNeeded || updateBG) {
            updateBGImage();
        }
        if (_inverseUpdateNeeded) {
            _inverseUpdateNeeded = false;
            try {
                _inverse.setTransform(_transform.createInverse());
            } catch (NoninvertibleTransformException excn) {
                System.err.print("Transform noninvertible\n");
            }
        }
        Graphics2D g = (Graphics2D) oldG.create();
        g.drawImage(_bg, 0, 0, this);
        g.transform(_transform);
        g.setColor(Color.BLACK);
        if (_hex != null) {
            g.fillPolygon(_hex);
        }
        _gb.drawTemporaryLayer(g);
    }

    private static final int SOLID_LINE_SIZE = 8;

    public void updateBGImage() {
        Graphics2D imG = _bg.createGraphics();
        imG.setColor(Color.BLACK);
        imG.fillRect(0, 0, getWidth(), getHeight());
        imG.setClip(0, 0, _bg.getWidth(), _bg.getHeight());
        imG.transform(_transform);
        imG.setColor(Color.WHITE);
        imG.fillRect(-SOLID_LINE_SIZE / 2, -SOLID_LINE_SIZE / 2, getWidth() + SOLID_LINE_SIZE, getHeight() + SOLID_LINE_SIZE);
        _gb.drawBoard(imG);
    }

    private BufferedImage _mpImage = null;

    public class MapPanelMouseListener extends MouseInputAdapter {

        private final MapPanel _mp;

        public MapPanelMouseListener(MapPanel mp) {
            _mp = mp;
        }

        public void mousePressed(MouseEvent e) {
            if (e.getButton() != MouseEvent.BUTTON1) {
                return;
            }
            Point location = e.getPoint();
            _inverse.transform(location, location);
            _gb.startAt(location);
        }

        public void mouseDragged(MouseEvent e) {
            if ((e.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) == 0) {
                return;
            }
            Point location = e.getPoint();
            _mp._inverse.transform(location, location);
            _gb.continueAt(location);
        }

        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                Point location = e.getPoint();
                _inverse.transform(location, location);
                _gb.endAt(location);
            }
        }
    }
}
