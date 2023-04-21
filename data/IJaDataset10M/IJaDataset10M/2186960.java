package xbrowser.options;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import xbrowser.XProjectConstants;

public abstract class XOptionPage extends JPanel {

    public void addPage(XOptionPage page) {
        children.add(page);
    }

    public void removePage(XOptionPage page) {
        children.remove(page);
    }

    public Iterator getChildPages() {
        return children.iterator();
    }

    public boolean hasChildPage() {
        return (!children.isEmpty());
    }

    protected void addToContainer(Component comp, Container container, GridBagLayout gridbag, GridBagConstraints constraints, int grid_width, double weight_x) {
        constraints.gridwidth = grid_width;
        constraints.weightx = weight_x;
        gridbag.setConstraints(comp, constraints);
        container.add(comp);
    }

    public JPanel getTitleComponent() {
        if (pnlTitle == null) {
            JLabel lbl_title = new JLabel(XProjectConstants.PRODUCT_NAME + " : " + getName(), JLabel.CENTER);
            Font fnt = lbl_title.getFont();
            lbl_title.setFont(fnt.deriveFont(Font.BOLD, fnt.getSize() + 4));
            pnlTitle = new JPanel(new BorderLayout());
            pnlTitle.add(new JLabel(getIcon()), BorderLayout.EAST);
            pnlTitle.add(new JLabel(getIcon()), BorderLayout.WEST);
            pnlTitle.add(lbl_title, BorderLayout.CENTER);
            pnlTitle.setBorder(BorderFactory.createTitledBorder(""));
        }
        return pnlTitle;
    }

    public void updateUI() {
        super.updateUI();
        if (pnlTitle != null) SwingUtilities.updateComponentTreeUI(pnlTitle);
    }

    public abstract String getName();

    public abstract ImageIcon getIcon();

    public abstract void loadInfo();

    public abstract void saveInfo();

    private LinkedList children = new LinkedList();

    private JPanel pnlTitle = null;
}
