package hu.kisszoltan.boxescape.gui;

import hu.kisszoltan.boxescape.Game;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;
import java.util.Vector;
import javax.swing.JPanel;

public class GamePane extends JPanel implements ComponentListener {

    private static final long serialVersionUID = 1L;

    private List<GameObject> objects = new Vector<GameObject>();

    private Player player = new Player(this);

    private int baseBorderWidth = 20;

    public GamePane() {
        super(new BorderLayout());
        initObjects();
        addMouseMotionListener(player);
        addComponentListener(this);
    }

    private void initObjects() {
        objects.add(player);
        objects.add(new Enemy(this, 50, 10, 80, 40));
        objects.add(new Enemy(this, 30, 30, 200, 40));
        objects.add(new Enemy(this, 30, 20, 80, 140));
        objects.add(new Enemy(this, 15, 30, 200, 140));
    }

    public void paint(Graphics g) {
        super.paint(g);
        drawBorder(g);
        drawObjects(g);
    }

    private void drawObjects(Graphics g) {
        for (GameObject o : objects) {
            o.paint(g);
        }
    }

    private void drawBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int borderWidth = Math.round(baseBorderWidth * getScaleX());
        int borderHeight = Math.round(baseBorderWidth * getScaleY());
        setBackground(Color.BLACK);
        g2.setColor(Color.WHITE);
        g2.fillRect(borderWidth, borderHeight, getWidth() - 2 * borderWidth, getHeight() - 2 * borderHeight);
    }

    public float getScaleX() {
        return getWidth() / 312.0f;
    }

    public float getScaleY() {
        return getHeight() / 232.0f;
    }

    public void checkCollision() {
        Polygon p = player.getPolygon();
        for (int i = 0; i < p.npoints; i++) {
            if (!Game.isRunning()) {
                return;
            }
            if (p.xpoints[i] < baseBorderWidth || p.xpoints[i] > getWidth() - baseBorderWidth || p.ypoints[i] < baseBorderWidth || p.ypoints[i] > getHeight() - baseBorderWidth) {
                Game.getInstance().end();
                return;
            }
        }
        for (GameObject o : objects) {
            if (!Game.isRunning()) {
                return;
            }
            if (!o.isPlayer() && o.getPolygon().intersects(p.getBounds2D())) {
                Game.getInstance().end();
                return;
            }
        }
    }

    public void reset() {
        player.moveToCenter();
        for (GameObject o : objects) {
            o.reset();
        }
        repaint();
    }

    public void update() {
        for (GameObject o : objects) {
            o.update();
            o.speedUp(Game.getLevel());
        }
        repaint();
        checkCollision();
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentResized(ComponentEvent e) {
        for (GameObject o : objects) {
            o.moveBy(getScaleX(), getScaleY());
        }
        repaint();
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }
}
