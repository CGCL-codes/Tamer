package dxunderground.winampController.Common;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import dxunderground.winampController.Common.GUI;

@SuppressWarnings("serial")
public class FilterPanel extends JPanel {

    public FilterPanel(GUI gui, ButtonActionsInterface buttonActions) {
        this.setLayout(new BorderLayout());
        gui.filterField = new JTextField(60);
        JButton filterButton = new JButton("Filter");
        filterButton.addActionListener(buttonActions);
        this.add(gui.filterField, BorderLayout.CENTER);
        this.add(filterButton, BorderLayout.EAST);
    }
}
