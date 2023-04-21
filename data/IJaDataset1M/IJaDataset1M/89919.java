package vademecum.ui.visualizer.vgraphics.basicfeatures;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import vademecum.ui.visualizer.vgraphics.AbstractInteraction;
import vademecum.ui.visualizer.vgraphics.IMouseOverDrawable;

public class BasicArrangement extends AbstractInteraction implements IMouseOverDrawable {

    /** Logger */
    private static Log log = LogFactory.getLog(BasicArrangement.class);

    int triggerID = 5;

    String interactionName = "Arrangement";

    String interactionDescription = "Moving and Resizing - Mode";

    int interactionPriority = 10;

    /** Popupmenu for further settings */
    JPopupMenu pMenu;

    @Override
    public JLabel getInteractionLabel() {
        JLabel label = new JLabel("Arrangement");
        return label;
    }

    @Override
    public JMenuItem getMenuItem() {
        JMenuItem item = new JMenuItem("Move/Resize");
        return item;
    }

    public void mouseClicked(MouseEvent e) {
        if (refBase.getInteractionMode() == this.triggerID) {
            if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) == MouseEvent.BUTTON3_MASK) {
                this.showPopupMenu(e.getX() - 20, e.getY() - 10);
            }
        }
    }

    public void mouseEntered(MouseEvent arg0) {
        if (refBase.getInteractionMode() == this.getTriggerID()) {
            Cursor cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
            refBase.setCursor(cursor);
        }
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

    public Point translateScreenToComponent(Point screenPoint) {
        Insets insets = refBase.getInsets();
        int x = (int) ((screenPoint.getX() - insets.left));
        int y = (int) ((screenPoint.getY() - insets.top));
        return new Point(x, y);
    }

    public void propertyChange(PropertyChangeEvent arg0) {
    }

    public void drawWhenMouseOver(Graphics2D g2) {
        drawMarkerBox(g2);
    }

    private void drawMarkerBox(Graphics2D g2) {
        Rectangle r = getAnchorRect();
        int x = r.x;
        int y = r.y;
        int width = r.width - 1;
        int height = r.height - 1;
        g2.setColor(Color.green);
        g2.drawRect(x, y, width, height);
    }

    private Rectangle getAnchorRect() {
        Rectangle ref = refBase.getBounds();
        Rectangle r = new Rectangle(0, 0, ref.width, ref.height);
        return r;
    }

    @Override
    public int getTriggerID() {
        return this.triggerID;
    }

    @Override
    public String getName() {
        return this.interactionName;
    }

    @Override
    public int getPriority() {
        return this.interactionPriority;
    }

    @Override
    public String getInteractionType() {
        return "General";
    }

    /**
	 * (non-Javadoc)
	 * @see vademecum.ui.visualizer.vgraphics.AbstractInteraction#initPopupMenu()
	 */
    @Override
    public void initPopupMenu() {
        pMenu = new JPopupMenu("Arrangement");
        JMenu backGMenu = new JMenu("BackGround");
        JMenuItem bItem = new JMenuItem("Invisible");
        bItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                refBase.setBackgroundVisible(!refBase.isBackgroundVisible());
                refBase.repaint();
            }
        });
        backGMenu.add(bItem);
        bItem = new JMenuItem("Color");
        bItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                Color c = JColorChooser.showDialog(null, "Select Background Color", Color.WHITE);
                refBase.setBackgroundColor(c);
                refBase.repaint();
            }
        });
        backGMenu.add(bItem);
        pMenu.add(backGMenu);
        JMenu subMenu = new JMenu("Border");
        JMenuItem eItem = new JMenuItem("Invisible");
        eItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                refBase.setBorderVisible(!refBase.isBorderVisible());
                refBase.repaint();
            }
        });
        subMenu.add(eItem);
        eItem = new JMenuItem("Color");
        eItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                Color c = JColorChooser.showDialog(null, "Select Background Color", Color.WHITE);
                refBase.setBorderColor(c);
                refBase.repaint();
            }
        });
        subMenu.add(eItem);
        eItem = new JMenuItem("Square");
        eItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                refBase.setBorderStyle(0);
                refBase.setBorderVisible(true);
                refBase.repaint();
            }
        });
        subMenu.add(eItem);
        eItem = new JMenuItem("Round");
        eItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                refBase.setBorderStyle(1);
                refBase.setBorderVisible(true);
                refBase.repaint();
            }
        });
        subMenu.add(eItem);
        pMenu.add(subMenu);
    }

    /**
	 * (non-Javadoc)
	 * @see vademecum.ui.visualizer.vgraphics.AbstractInteraction#showPopupMenu()
	 */
    @Override
    public void showPopupMenu(int x, int y) {
        pMenu.show(refBase, x, y);
    }

    @Override
    public Properties getProperties() {
        return null;
    }

    @Override
    public void setProperties(Properties p) {
    }

    public void mouseDragged(MouseEvent arg0) {
    }

    public void mouseMoved(MouseEvent arg0) {
    }
}
