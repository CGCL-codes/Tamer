package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import common.EarthSimSettings;

public class QueryRegionPanel extends JPanel implements KeyListener {

    private static final long serialVersionUID = 1519994997213620493L;

    private EarthSimSettings settings = EarthSimSettings.getInstance();

    private JTextField txtLongWest = new JTextField("180");

    private JTextField txtLongEast = new JTextField("180");

    private JTextField txtLatNorth = new JTextField("180");

    private JTextField txtLatSouth = new JTextField("180");

    public QueryRegionPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setBorder(BorderFactory.createTitledBorder("Region Settings"));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        Dimension dim = new Dimension(290, 90);
        setPreferredSize(dim);
        setMaximumSize(dim);
        add(getLatitudeInputs());
        add(Box.createVerticalStrut(10));
        add(getLongitudeInputs());
        settings.setLongEast(Integer.parseInt(txtLongEast.getText()));
        settings.setLongWest(Integer.parseInt(txtLongWest.getText()));
        settings.setLatNorth(Integer.parseInt(txtLatNorth.getText()));
        settings.setLatSouth(Integer.parseInt(txtLatSouth.getText()));
    }

    private JPanel getLatitudeInputs() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.LINE_AXIS));
        pnl.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtLongEast.addKeyListener(this);
        txtLongWest.addKeyListener(this);
        pnl.add(new JLabel("West Longitude: "));
        pnl.add(txtLongWest);
        pnl.add(new JLabel("East Longitude: "));
        pnl.add(txtLongEast);
        return pnl;
    }

    private JPanel getLongitudeInputs() {
        JPanel pnl = new JPanel();
        pnl.setLayout(new BoxLayout(pnl, BoxLayout.LINE_AXIS));
        pnl.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtLatNorth.addKeyListener(this);
        txtLatSouth.addKeyListener(this);
        pnl.add(new JLabel("North Latitude: "));
        pnl.add(txtLatNorth);
        pnl.add(new JLabel("South Latitude: "));
        pnl.add(txtLatSouth);
        return pnl;
    }

    public void keyReleased(KeyEvent e) {
        if (e.getSource().equals(txtLongEast)) settings.setLongEast(Integer.parseInt(txtLongEast.getText())); else if (e.getSource().equals(txtLongWest)) settings.setLongWest(Integer.parseInt(txtLongWest.getText())); else if (e.getSource().equals(txtLatNorth)) settings.setLatNorth(Integer.parseInt(txtLatNorth.getText())); else if (e.getSource().equals(txtLatSouth)) settings.setLatSouth(Integer.parseInt(txtLatSouth.getText()));
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void disable() {
        txtLongEast.setEnabled(false);
        txtLongWest.setEnabled(false);
        txtLatNorth.setEnabled(false);
        txtLatSouth.setEnabled(false);
    }

    public void enable() {
        txtLongEast.setEnabled(true);
        txtLongWest.setEnabled(true);
        txtLatNorth.setEnabled(true);
        txtLatSouth.setEnabled(true);
    }
}
