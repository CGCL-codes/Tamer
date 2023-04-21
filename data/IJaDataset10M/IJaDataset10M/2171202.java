package com.mymaps.apps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.SwingWorker3.SwingWorker;
import se.datadosen.component.RiverLayout;
import com.mymaps.ui.DisplayHelper;

/**
 * Simple "selector" for other sample applications
 * 
 * @author todd
 *
 */
public class ApplicationSelector extends JFrame {

    private static final int APP_WME = 0;

    private static final int APP_USGS = 1;

    private static final int APP_SAMP = 2;

    private static final int APP_BUILD = 3;

    public ApplicationSelector() {
        super("MyMaps Application Sampler");
        Container pane = getContentPane();
        JPanel headerPanel = new JPanel(new RiverLayout());
        JLabel header = new JLabel("<html><big><center>Welcome to MyMaps Sample Apps</big></html>");
        JLabel headerText = new JLabel("<html><center>The following samples are just a taste of what MyMaps can do.<br>Pick one and enjoy!</center></html>");
        headerPanel.add("center", header);
        headerPanel.add("br", new JLabel(" "));
        headerPanel.add("br center", headerText);
        headerPanel.add("br", new JLabel(" "));
        pane.add(headerPanel, BorderLayout.NORTH);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(0, 1));
        buttonPanel.add(createApplicationLauncher("The Globe Explorer", "Explore web mapping services from various parts of the world.", "Start Globe Explorer", APP_WME));
        buttonPanel.add(createApplicationLauncher("Latitude/Longitude Inspector", "Find any location in the US using USGS maps,<br>and discover the coordinates of the area.", "Launch Lat/Long Inspector", APP_USGS));
        buttonPanel.add(createApplicationLauncher("MyMap Builder", "Load local data files and view them as simple line drawings", "Begin Building", APP_BUILD));
        pane.add(buttonPanel, BorderLayout.CENTER);
        JPanel cancelPanel = new JPanel(new RiverLayout());
        cancelPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        cancelPanel.add("center", cancel);
        pane.add(cancelPanel, BorderLayout.SOUTH);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        pack();
        DisplayHelper.center(this);
        setVisible(true);
    }

    private JPanel createApplicationLauncher(String header, String text, String buttonText, final int action) {
        JPanel retVal = new JPanel(new RiverLayout());
        retVal.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        JLabel headerLabel = new JLabel("<html><big><center>" + header + "</center></big></html>");
        JLabel textLabel = new JLabel("<html><center>" + text + "</center></html>");
        JButton button = new JButton(buttonText);
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                launch(action);
            }
        });
        retVal.add("center", headerLabel);
        retVal.add("br center", textLabel);
        retVal.add("br", new JLabel(" "));
        retVal.add("br center", button);
        return retVal;
    }

    private void launch(int app) {
        try {
            switch(app) {
                case APP_WME:
                    final String config = "data/sampledefinitions.xml";
                    final String worldfile = "data/shapefiles/world_adm0.shp";
                    final String navSLD = "data/navstylefile.sld";
                    new SwingWorker() {

                        public Object construct() {
                            new WebMapExplorer(worldfile, navSLD, config);
                            return null;
                        }
                    }.start();
                    break;
                case APP_USGS:
                    new SwingWorker() {

                        public Object construct() {
                            new USGSExplorer().setVisible(true);
                            return null;
                        }
                    }.start();
                    break;
                case APP_BUILD:
                    final MapBuilder builder = new MapBuilder();
                    builder.setSize(500, 500);
                    new SwingWorker() {

                        public Object construct() {
                            builder.setVisible(true);
                            return null;
                        }
                    }.start();
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "That application hasn't been added to the launch method yet");
                    break;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "The application couldn't start:" + e.getMessage());
        }
    }

    private void close() {
        this.dispose();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                new ApplicationSelector();
            }
        });
    }
}
