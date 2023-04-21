package vademecum.ui.visualizer.vgraphics.box.additionalfeatures;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.border.CompoundBorder;
import vademecum.ui.visualizer.vgraphics.AbstractFeature;
import vademecum.ui.visualizer.vgraphics.IActivePaintable;
import vademecum.ui.visualizer.vgraphics.IMouseOverPaintable;
import vademecum.ui.visualizer.vgraphics.VGraphics;

public class FeatureBoxSelect extends AbstractFeature implements IMouseOverPaintable, IActivePaintable {

    int featureID = 3;

    String featureName = "Selection";

    String featureDescription = "Selection - Mode";

    int featurePriority = 2;

    float thickness = 2.7f;

    JPopupMenu pMenu;

    @Override
    public JLabel getFeatureLabel() {
        JLabel label = new JLabel(featureName);
        return label;
    }

    @Override
    public JMenuItem getMenuItem() {
        JMenuItem item = new JMenuItem(featureName);
        return item;
    }

    public void mouseClicked(MouseEvent e) {
        if (refBase.getParentMode() == this.featureID) {
            if ((e.getModifiers() & MouseEvent.BUTTON3_MASK) == MouseEvent.BUTTON3_MASK) {
                this.showPopupMenu(e.getX() - 20, e.getY() - 10);
            }
        }
    }

    public void mouseEntered(MouseEvent arg0) {
        if (refBase.getParentMode() == this.featureID) {
            Cursor cursor = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
            refBase.setCursor(cursor);
        }
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void drawWhenMouseOver(Graphics2D g2) {
        if (refBase.getParentMode() == this.featureID) {
            drawCustomBorder(g2);
        }
    }

    private void drawCustomBorder(Graphics2D g2) {
        if (!refBase.isActive()) {
            Color c = Color.GREEN;
            g2.setColor(c);
            Rectangle r = g2.getClipBounds();
            r.width = r.width - 1;
            r.height = r.height - 1;
            Stroke saveStroke = g2.getStroke();
            g2.setStroke(new BasicStroke(thickness));
            g2.draw(r);
            g2.setStroke(saveStroke);
        } else {
        }
    }

    @Override
    public int getFeatureID() {
        return this.featureID;
    }

    @Override
    public String getName() {
        return this.featureName;
    }

    @Override
    public int getPriority() {
        return this.featurePriority;
    }

    public void drawWhenActive(Graphics2D g2) {
    }

    private void drawSelectionBorder(Graphics2D g2, boolean visible, Color c) {
        if (visible == true) {
            g2.setColor(c);
            Rectangle r = g2.getClipBounds();
            r.width = r.width - 1;
            r.height = r.height - 1;
            Stroke saveStroke = g2.getStroke();
            g2.setStroke(new BasicStroke(thickness));
            g2.draw(r);
            g2.setStroke(saveStroke);
        } else {
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void propertyChange(PropertyChangeEvent arg0) {
    }

    @Override
    public String getFeatureType() {
        return "General";
    }

    @Override
    public void initPopupMenu() {
        pMenu = new JPopupMenu("Box");
        pMenu.add(new JMenuItem("Background"));
        pMenu.add(new JMenuItem("Border"));
    }

    @Override
    public void showPopupMenu(int x, int y) {
        pMenu.show(refBase, x, y);
    }
}
