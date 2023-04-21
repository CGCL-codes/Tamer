package ps.client.gui.util;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GblPanel extends JPanel {

    public static final int ANCHOR_CENTER = GridBagConstraints.CENTER;

    public static final int ANCHOR_EAST = GridBagConstraints.EAST;

    public static final int ANCHOR_WEST = GridBagConstraints.WEST;

    public static final int ANCHOR_NORTHEAST = GridBagConstraints.NORTHEAST;

    GridBagLayout gbl = new GridBagLayout();

    GridBagConstraints gblCons = new GridBagConstraints();

    public GblPanel() {
        setLayout(gbl);
        gblCons.fill = GridBagConstraints.NONE;
        gblCons.weightx = 1.0;
        gblCons.insets = new Insets(0, 0, 0, 0);
        gblCons.anchor = GridBagConstraints.NORTHWEST;
    }

    public void addComponent(Component comp) {
        gbl.setConstraints(comp, gblCons);
        add(comp);
    }

    public void addComponent(Component comp, int gridwidth) {
        gblCons.gridwidth = gridwidth;
        gbl.setConstraints(comp, gblCons);
        add(comp);
        gblCons.gridwidth = 1;
    }

    public void addComponent(Component comp, int gridwidth, int gridheight) {
        gblCons.gridwidth = gridwidth;
        gblCons.gridheight = gridheight;
        gbl.setConstraints(comp, gblCons);
        add(comp);
        gblCons.gridwidth = 1;
        gblCons.gridheight = 1;
    }

    public void addLastComponent(Component comp, boolean center) {
        int oldFill = gblCons.fill;
        int oldAnchor = gblCons.anchor;
        if (center) {
            gblCons.fill = GridBagConstraints.NONE;
            gblCons.anchor = GridBagConstraints.CENTER;
        } else {
            gblCons.fill = GridBagConstraints.BOTH;
            gblCons.anchor = GridBagConstraints.NORTHWEST;
        }
        gblCons.gridwidth = GridBagConstraints.REMAINDER;
        gbl.setConstraints(comp, gblCons);
        add(comp);
        gblCons.gridwidth = 1;
        gblCons.fill = oldFill;
        gblCons.anchor = oldAnchor;
    }

    public void addLastComponent(Component comp) {
        gblCons.gridwidth = GridBagConstraints.REMAINDER;
        gbl.setConstraints(comp, gblCons);
        add(comp);
        gblCons.gridwidth = 1;
    }

    public void setAnchor(int anchor) {
        gblCons.anchor = anchor;
    }

    public void setFillBoth() {
        gblCons.fill = GridBagConstraints.BOTH;
    }

    public void setFillNone() {
        gblCons.fill = GridBagConstraints.NONE;
    }

    public void setInsets(Insets insets) {
        gblCons.insets = insets;
    }

    public JLabel createPlaceholder(int width, int height) {
        JLabel ph = new JLabel();
        ph.setPreferredSize(new Dimension(width, height));
        return ph;
    }
}
