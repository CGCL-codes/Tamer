package view.world;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import parameters.GUIParameters;
import view.main.MainPanel;

public class ColorPanel extends JPanel implements ActionListener {

    private MainPanel gui;

    private JComboBox item;

    private JButton col;

    private static final String[] list = { "Path", "Grenade", "LOS", "Text" };

    public ColorPanel(MainPanel gui_) {
        gui = gui_;
        item = new JComboBox(list);
        col = new JButton();
        item.setMaximumSize(new Dimension(100, 20));
        col.setActionCommand("color");
        col.setBorder(BorderFactory.createBevelBorder(3));
        col.setMinimumSize(new Dimension(30, 30));
        col.setPreferredSize(new Dimension(30, 30));
        col.setMaximumSize(new Dimension(30, 30));
        col.setBackground(Color.red);
        item.addActionListener(this);
        col.addActionListener(this);
        this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        this.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Color setup"), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
        this.add(item);
        this.add(Box.createHorizontalStrut(5));
        this.add(col);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("color")) {
            Color c = JColorChooser.showDialog(this, "Choose the " + item.getSelectedItem() + " color", col.getBackground());
            if (c == null) {
                return;
            }
            col.setBackground(c);
            switch(item.getSelectedIndex()) {
                case 0:
                    GUIParameters.setPathColor(col.getBackground());
                    break;
                case 1:
                    GUIParameters.setGrenadeColor(col.getBackground());
                    break;
                case 2:
                    GUIParameters.setLOSColor(col.getBackground());
                    break;
                case 3:
                    GUIParameters.setStringColor(col.getBackground());
                    break;
                default:
                    break;
            }
        } else {
            switch(item.getSelectedIndex()) {
                case 0:
                    col.setBackground(GUIParameters.PATH_COLOR);
                    break;
                case 1:
                    col.setBackground(GUIParameters.GRENADE_COLOR);
                    break;
                case 2:
                    col.setBackground(GUIParameters.LOS_COLOR);
                    break;
                case 3:
                    col.setBackground(GUIParameters.STRING_COLOR);
                    break;
                default:
                    break;
            }
        }
        gui.repaint();
    }
}
