package be.vds.jtb.jtbswingdemo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import be.vds.jtb.swing.component.SearchBox;
import be.vds.jtb.swing.layout.GridBagLayoutManager;

public class SearchBoxPanel extends JPanel {

    public SearchBoxPanel() {
        init();
    }

    private void init() {
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 0);
        GridBagLayoutManager.addComponent(this, createLine1(), c, 0, 0, 1, 1, 1, 0, GridBagConstraints.HORIZONTAL, GridBagConstraints.CENTER);
        GridBagLayoutManager.addComponent(this, Box.createVerticalGlue(), c, 0, 1, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
    }

    private Component createLine1() {
        SearchBox box = new SearchBox() {

            @Override
            protected void lookup() {
                JOptionPane.showMessageDialog(SearchBoxPanel.this, "Implement LookUp method");
            }

            @Override
            protected String formatSelectedObject(Object object) {
                return "no format defined";
            }
        };
        box.setColumn(10);
        box.setBorder(new LineBorder(Color.RED));
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 0);
        GridBagLayoutManager.addComponent(p, new JLabel("A Box Stretchable"), c, 0, 0, 1, 1, 0, 0, GridBagConstraints.NONE, GridBagConstraints.CENTER);
        GridBagLayoutManager.addComponent(p, box, c, 1, 0, 1, 1, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER);
        return p;
    }
}
