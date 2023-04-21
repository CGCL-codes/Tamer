package neon.ui;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import javax.swing.*;
import javax.swing.border.*;
import neon.core.Engine;
import neon.util.ColorFactory;
import neon.util.spatial.SimpleIndex;
import neon.graphics.*;
import neon.graphics.animation.Animator;
import neon.maps.*;
import neon.objects.entities.Player;
import neon.objects.property.Condition;

/**
 * This class represents the main game screen. It contains a <code>JVectorPane</code> 
 * overlayed with player information, and a text area to show in-game messages.
 * 
 * @author mdriesen
 */
@SuppressWarnings("serial")
public class GamePanel extends JComponent {

    private JTextArea text;

    private JScrollPane scroller;

    private JPanel stats;

    private DefaultRenderable cursor;

    private TitledBorder sBorder, aBorder, cBorder;

    private JVectorPane drawing;

    private Animator animator;

    private JLabel intLabel, conLabel, dexLabel, strLabel, wisLabel, chaLabel;

    private JLabel healthLabel, magicLabel, AVLabel, DVLabel;

    /**
	 * Initializes this GamePanel.
	 */
    public GamePanel() {
        drawing = new JVectorPane();
        drawing.setFilter(new LightFilter());
        animator = new Animator(drawing);
        stats = new JPanel(new GridLayout(0, 1)) {

            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        stats.setLayout(new BoxLayout(stats, BoxLayout.PAGE_AXIS));
        stats.setOpaque(false);
        JPanel sPanel = new JPanel(new GridLayout(0, 1));
        sBorder = new TitledBorder("Stats");
        sBorder.setBorder(new LineBorder(Color.LIGHT_GRAY));
        sPanel.setBorder(sBorder);
        sPanel.setOpaque(false);
        sPanel.add(healthLabel = new JLabel());
        sPanel.add(magicLabel = new JLabel());
        stats.add(sPanel);
        JPanel cPanel = new JPanel(new GridLayout(0, 1));
        cBorder = new TitledBorder("Combat");
        cBorder.setBorder(new LineBorder(Color.LIGHT_GRAY));
        cPanel.setBorder(cBorder);
        cPanel.setOpaque(false);
        cPanel.add(AVLabel = new JLabel());
        cPanel.add(DVLabel = new JLabel());
        stats.add(cPanel);
        JPanel aPanel = new JPanel(new GridLayout(0, 1));
        aBorder = new TitledBorder("Attributes");
        aBorder.setBorder(new LineBorder(Color.LIGHT_GRAY));
        aPanel.setBorder(aBorder);
        aPanel.setOpaque(false);
        aPanel.add(intLabel = new JLabel());
        aPanel.add(conLabel = new JLabel());
        aPanel.add(dexLabel = new JLabel());
        aPanel.add(strLabel = new JLabel());
        aPanel.add(wisLabel = new JLabel());
        aPanel.add(chaLabel = new JLabel());
        stats.add(aPanel);
        text = new JTextArea();
        text.setOpaque(false);
        text.setFocusable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        scroller = new JScrollPane(text) {

            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        scroller.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "Messages"));
        scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        layout(true);
        cursor = new DefaultRenderable(0, 0, Byte.MAX_VALUE, 1, 1, "x", Color.white);
    }

    /**
     * Zooms in on the main game screen.
     */
    public void zoomIn() {
        drawing.setZoom(drawing.getZoom() + 1);
        repaint();
    }

    /**
	 * Zooms the main game screen out.
	 */
    public void zoomOut() {
        drawing.setZoom(drawing.getZoom() - 1);
        repaint();
    }

    /**
     * Prints a message in the message box of the main game screen.
     * 
     * @param message	the message to print
     */
    public void print(String message) {
        text.append(message + "\n");
    }

    public void setZone(Zone zone) {
        zone.addLayer(new Layer(3, new SimpleIndex<Renderable>()));
        Player player = Engine.getPlayer();
        Rectangle bounds = new Rectangle(player.x, player.y, player.width, player.height);
        zone.addElement(player, bounds, 3);
        drawing.setScene(zone);
        Engine.getUI().showPanel(this);
    }

    public void repaint() {
        if (Engine.getPlayer() != null) {
            drawStats();
            drawing.updateCamera(new Point(Engine.getPlayer().x, Engine.getPlayer().y));
        }
        super.repaint();
    }

    private void layout(boolean over) {
        removeAll();
        if (over) {
            add(drawing);
            add(stats);
            add(scroller);
            SpringLayout layout = new SpringLayout();
            layout.putConstraint(SpringLayout.NORTH, drawing, 0, SpringLayout.NORTH, this);
            layout.putConstraint(SpringLayout.EAST, drawing, 0, SpringLayout.EAST, this);
            layout.putConstraint(SpringLayout.SOUTH, drawing, 0, SpringLayout.SOUTH, this);
            layout.putConstraint(SpringLayout.WEST, drawing, 0, SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.NORTH, stats, 0, SpringLayout.NORTH, this);
            layout.putConstraint(SpringLayout.SOUTH, stats, 0, SpringLayout.SOUTH, this);
            layout.putConstraint(SpringLayout.WEST, stats, 0, SpringLayout.WEST, this);
            layout.putConstraint(SpringLayout.NORTH, scroller, 0, SpringLayout.NORTH, this);
            layout.putConstraint(SpringLayout.EAST, scroller, 0, SpringLayout.EAST, this);
            layout.putConstraint(SpringLayout.SOUTH, scroller, 0, SpringLayout.SOUTH, this);
            setLayout(layout);
            setComponentZOrder(drawing, 2);
        } else {
            setLayout(new BorderLayout());
            add(stats, BorderLayout.LINE_START);
            add(drawing, BorderLayout.CENTER);
            add(scroller, BorderLayout.LINE_END);
        }
    }

    private void drawStats() {
        Player player = Engine.getPlayer();
        if (player.getHealth() * 4 < player.getBaseHealth()) {
            healthLabel.setText("<html>health: <font color=red>" + player.getHealth() + "/" + player.getBaseHealth() + "</font></html>");
        } else if (player.getHealth() > player.getBaseHealth()) {
            healthLabel.setText("<html>health: <font color=green>" + player.getHealth() + "/" + player.getBaseHealth() + "</font></html>");
        } else {
            healthLabel.setText("health: " + player.getHealth() + "/" + player.getBaseHealth());
        }
        magicLabel.setText("magic: " + player.getMana() + "/" + (int) (player.species.mana * player.species.iq));
        AVLabel.setText("AV: " + player.getAVString());
        DVLabel.setText("DV: " + player.getDV());
        if (player.getStr() > (int) player.species.str) {
            strLabel.setText("<html>strength: <font color=green>" + player.getStr() + "</font></html>");
        } else if (player.getStr() < (int) player.species.str) {
            strLabel.setText("<html>strength: <font color=red>" + player.getStr() + "</font></html>");
        } else {
            strLabel.setText("strength: " + player.getStr());
        }
        if (player.getDex() > (int) player.species.dex) {
            dexLabel.setText("<html>dexterity: <font color=green>" + player.getDex() + "</font></html>");
        } else if (player.getDex() < (int) player.species.dex) {
            dexLabel.setText("<html>dexterity: <font color=red>" + player.getDex() + "</font></html>");
        } else {
            dexLabel.setText("dexterity: " + player.getDex());
        }
        if (player.getCon() > (int) player.species.con) {
            conLabel.setText("<html>constitution: <font color=green>" + player.getCon() + "</font></html>");
        } else if (player.getCon() < (int) player.species.con) {
            conLabel.setText("<html>constitution: <font color=red>" + player.getCon() + "</font></html>");
        } else {
            conLabel.setText("constitution: " + player.getCon());
        }
        if (player.getInt() > (int) player.species.iq) {
            intLabel.setText("<html>intelligence: <font color=green>" + player.getInt() + "</font></html>");
        } else if (player.getInt() < (int) player.species.iq) {
            intLabel.setText("<html>intelligence: <font color=red>" + player.getInt() + "</font></html>");
        } else {
            intLabel.setText("intelligence: " + player.getInt());
        }
        if (player.getWis() > (int) player.species.wis) {
            wisLabel.setText("<html>wisdom: <font color=green>" + player.getWis() + "</font></html>");
        } else if (player.getWis() < (int) player.species.wis) {
            wisLabel.setText("<html>wisdom: <font color=red>" + player.getWis() + "</font></html>");
        } else {
            wisLabel.setText("wisdom: " + player.getWis());
        }
        if (player.getCha() > (int) player.species.cha) {
            chaLabel.setText("<html>charisma: <font color=green>" + player.getCha() + "</font></html>");
        } else if (player.getCha() < (int) player.species.cha) {
            chaLabel.setText("<html>charisma: <font color=red>" + player.getCha() + "</font></html>");
        } else {
            chaLabel.setText("charisma: " + player.getCha());
        }
        if (player.getConditions().contains(Condition.DISEASED) || player.getConditions().contains(Condition.CURSED) || player.getConditions().contains(Condition.POISONED)) {
            sBorder.setBorder(new LineBorder(ColorFactory.getColor("darkGreen")));
        } else {
            sBorder.setBorder(new LineBorder(Color.LIGHT_GRAY));
        }
    }

    /**
     * Shows a cursor on the main game screen.
     * 
	 * @return	the cursor
     */
    public DefaultRenderable showCursor() {
        drawing.getScene().addElement(cursor, new Rectangle(cursor.x, cursor.y, 1, 1), 3);
        return cursor;
    }

    /**
     * Removes the cursor from the screen.
     */
    public void hideCursor() {
        drawing.getScene().removeElement(cursor);
    }

    /**
	 * Turns the HUD on and off.
	 */
    public void toggleHUD() {
        if (!stats.isVisible()) {
            stats.setVisible(true);
            scroller.setVisible(true);
            layout(false);
            doLayout();
        } else if (getLayout() instanceof BorderLayout) {
            layout(true);
            doLayout();
        } else {
            stats.setVisible(!stats.isVisible());
            scroller.setVisible(!scroller.isVisible());
        }
        repaint();
    }

    public Rectangle getVisibleRectangle() {
        return drawing.getVisibleRectangle();
    }

    public Animator getAnimator() {
        return animator;
    }

    private class LightFilter implements BufferedImageOp {

        public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {
            return new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
        }

        public BufferedImage filter(BufferedImage src, BufferedImage dest) {
            Rectangle view = drawing.getVisibleRectangle();
            Player player = Engine.getPlayer();
            float zoom = drawing.getZoom();
            Area area = new Area(new Rectangle(src.getWidth(), src.getHeight()));
            area.subtract(new Area(new Ellipse2D.Float((player.x - 8) * zoom - view.x * zoom, (player.y - 8) * zoom - view.y * zoom, (int) (17 * zoom), (int) (17 * zoom))));
            for (Point p : Atlas.getCurrentZone().getLightMap().keySet()) {
                area.subtract(new Area(new Ellipse2D.Float((p.x - 4) * zoom - view.x * zoom, (p.y - 4) * zoom - view.y * zoom, 9 * zoom, 9 * zoom)));
            }
            dest = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) dest.getGraphics();
            g.drawImage(src, src.getMinX(), src.getMinY(), null);
            if (Atlas.getCurrentMap() instanceof World) {
                int hour = (Engine.getTimer().getTime() / (60 * 1) + 12) % 24;
                g.setColor(new Color(0, 0, 0, (hour - 12) * (hour - 12)));
                g.fill(area);
            } else {
                g.setColor(new Color(0, 0, 0, 100));
                g.fill(area);
            }
            return dest;
        }

        public Rectangle2D getBounds2D(BufferedImage src) {
            return src.getGraphics().getClip().getBounds();
        }

        public Point2D getPoint2D(Point2D srcPt, Point2D destPt) {
            destPt = srcPt;
            return destPt;
        }

        public RenderingHints getRenderingHints() {
            return null;
        }
    }
}
