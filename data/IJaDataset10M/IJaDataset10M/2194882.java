package de.iqcomputing.flap.options;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import de.iqcomputing.swing.*;

class OptionsPanel extends JPanel {

    protected JLabel lbSection;

    protected JPanel paOptionPanes;

    protected CardPanel cpOptionPanes;

    protected Hashtable optionPaneCards = new Hashtable();

    public OptionsPanel() {
        createGUI();
    }

    protected void createGUI() {
        setLayout(new BorderLayout());
        add(createLabelPanel(), BorderLayout.NORTH);
        add(cpOptionPanes = new CardPanel(), BorderLayout.CENTER);
        cpOptionPanes.showCard(new JPanel());
    }

    protected JPanel createLabelPanel() {
        JPanel panel = new JPanel();
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        Component c;
        panel.setLayout(gb);
        lbSection = new JLabel(" ");
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.insets = new Insets(0, 0, 2, 0);
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.anchor = GridBagConstraints.WEST;
        gb.setConstraints(lbSection, gbc);
        panel.add(lbSection);
        lbSection.setFont(lbSection.getFont().deriveFont(Font.BOLD));
        c = new JSeparator();
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.insets = new Insets(0, 0, 8, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(c, gbc);
        panel.add(c);
        return panel;
    }

    public void setOptionPane(String label, OptionPane optionPane) {
        lbSection.setText(label);
        cpOptionPanes.showCard(optionPane.createComponent());
    }
}
