package org.mati.geotech.gui.method;

import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JPanel;
import org.mati.geotech.model.Rect;
import org.mati.geotech.model.RectViewModel;

public abstract class DrawPanel extends JPanel implements ComponentListener {

    private static final long serialVersionUID = 2707240743186447753L;

    private RectViewModel _model;

    protected double _xsc = 1;

    protected double _ysc = 1;

    private double _zoom = 0.9;

    private double _mouseX, _mouseY;

    private boolean drug = false;

    protected double _xsh = -200.0;

    protected double _ysh = -100.0;

    private double _screenW = 128;

    private double _screenH = 128;

    public DrawPanel() {
        super();
        init();
    }

    public void changeZoom(double dZ) {
        _zoom += _zoom * dZ;
        updateScale();
        repaint();
    }

    public double scrToMapX(double x) {
        return (x / (_xsc) + _xsh);
    }

    public double scrToMapY(double y) {
        return (y / (_ysc) + _ysh);
    }

    public double mapToScrX(double x) {
        return (x - _xsh) * (_xsc);
    }

    public double mapToScrY(double y) {
        return (y - _ysh) * (_ysc);
    }

    private void init() {
        addComponentListener(this);
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) drug = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) drug = false;
            }
        });
        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if (drug) {
                    _xsh = _xsh + (_mouseX - scrToMapX(e.getX()));
                    _ysh = _ysh + (_mouseY - scrToMapY(e.getY()));
                    _mouseX = scrToMapX(e.getX());
                    _mouseY = scrToMapY(e.getY());
                } else {
                    _mouseX = scrToMapX(e.getX());
                    _mouseY = scrToMapY(e.getY());
                }
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (getModel() != null) {
                    getModel().getDisplayRect().setX(scrToMapX(e.getPoint().x) - getModel().getDisplayRect().getWidth() / 2);
                    getModel().getDisplayRect().setY(scrToMapY(e.getPoint().y) - getModel().getDisplayRect().getHeight() / 2);
                    repaint();
                }
                if (!drug) {
                    _mouseX = scrToMapX(e.getX());
                    _mouseY = scrToMapY(e.getY());
                }
            }
        });
        addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.isControlDown()) changeZoom(((double) e.getWheelRotation()) * 0.01);
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintRects(g);
    }

    public DrawPanel(LayoutManager arg0) {
        super(arg0);
    }

    public DrawPanel(boolean arg0) {
        super(arg0);
    }

    public abstract void paintRects(Graphics g);

    public void paintRect(Graphics g, Rect r) {
        if (r != null) g.drawRect((int) Math.round(mapToScrX(r.getX())), (int) Math.round(mapToScrY(r.getY())), (int) Math.round(r.getWidth() * _xsc), (int) Math.round(r.getHeight() * _ysc));
    }

    protected void updateScale() {
        _xsc = this.getWidth() / _model.getWidth() * _zoom;
        _ysc = this.getHeight() / _model.getHeight() * _zoom;
    }

    public DrawPanel(LayoutManager arg0, boolean arg1) {
        super(arg0, arg1);
    }

    public void setModel(RectViewModel model) {
        _model = model;
    }

    public RectViewModel getModel() {
        return _model;
    }

    public void componentResized(ComponentEvent e) {
        updateScale();
    }

    @Override
    public void componentHidden(ComponentEvent arg0) {
    }

    @Override
    public void componentMoved(ComponentEvent arg0) {
    }

    @Override
    public void componentShown(ComponentEvent arg0) {
    }

    public double getScreenWidth() {
        return _screenW;
    }

    public void setScreenWidth(double screenW) {
        this._screenW = screenW;
    }

    public double getScreenHeight() {
        return _screenH;
    }

    public void setScreenHeight(double screenH) {
        this._screenH = screenH;
    }
}
