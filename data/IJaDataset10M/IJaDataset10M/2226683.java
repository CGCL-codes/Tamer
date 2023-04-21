package com.intabulas.intellij.plugin.jetstyle.panels;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import com.intabulas.intellij.plugin.jetstyle.JetStyleConfiguration;
import com.intellij.openapi.diagnostic.Logger;

/**
 * The configuration panel for the IntelliJ IDEA Checkstyle Plugin. This is the
 * panel that is shown when you click on Checkstyle in IDEA's Settings dialog.
 *
 * @author Mark Lussier
 * @author Lars K&uuml;hne
 * @author Dennis Lundberg
 * @version $Id: JetStyleConfigurationPanel.java 182 2008-02-25 22:57:29Z dennislundberg $
 */
public class JetStyleConfigurationPanel extends JPanel {

    /**
     * Only directories and files with the extension .xml should be selectable.
     */
    private static class ConfigFileFilter extends FileFilter {

        public boolean accept(File f) {
            return f.isDirectory() || f.getPath().endsWith(".xml");
        }

        public String getDescription() {
            return "XML files";
        }
    }

    private Logger logger = Logger.getInstance(this.getClass().getName());

    private JTextField configurationLocation;

    private JetStyleConfiguration jetStyleConfiguration;

    public JetStyleConfigurationPanel(JetStyleConfiguration jetStyleConfiguration) {
        this.jetStyleConfiguration = jetStyleConfiguration;
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(1, 1, 1, 1));
        initializePanel();
    }

    private void initializePanel() {
        removeAll();
        add(new JetStyleConfigModulesPanel(jetStyleConfiguration), BorderLayout.CENTER);
        JPanel northpanel;
        northpanel = new JPanel(new BorderLayout(5, 5));
        northpanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        String location = jetStyleConfiguration.getConfigLocation();
        configurationLocation = new JTextField(location);
        configurationLocation.setEditable(false);
        configurationLocation.setMinimumSize(configurationLocation.getPreferredSize());
        northpanel.add(new JLabel("Configuration File"), BorderLayout.WEST);
        northpanel.add(configurationLocation, BorderLayout.CENTER);
        Action selectConfigFileAction = new AbstractAction("Select Checkstyle Configuration ...") {

            public void actionPerformed(ActionEvent event) {
                JFileChooser fc = new JFileChooser();
                fc.setFileFilter(new ConfigFileFilter());
                final Component parent = SwingUtilities.getRoot(configurationLocation);
                fc.showDialog(parent, "Open");
                File selected = fc.getSelectedFile();
                String newLocation = selected.getPath();
                logger.debug("Loading config: System.currentTimeMillis() = " + System.currentTimeMillis());
                jetStyleConfiguration = new JetStyleConfiguration(newLocation);
                logger.debug("Init panel: System.currentTimeMillis() = " + System.currentTimeMillis());
                initializePanel();
                logger.debug("done: System.currentTimeMillis() = " + System.currentTimeMillis());
            }
        };
        JButton selectConfigurationButton = new JButton(selectConfigFileAction);
        northpanel.add(selectConfigurationButton, BorderLayout.EAST);
        add(northpanel, BorderLayout.NORTH);
        revalidate();
    }

    /**
     *
     * @param config
     * @return
     */
    public JetStyleConfiguration saveConfiguration(JetStyleConfiguration config) {
        return jetStyleConfiguration;
    }

    /**
     * Helper for development, so we can test the gui without restarting IDEA.
     */
    public static void main(String[] args) {
        JFrame f = new JFrame();
        JetStyleConfiguration config;
        if (args.length >= 1) {
            config = new JetStyleConfiguration(args[0]);
        } else {
            config = new JetStyleConfiguration();
        }
        JPanel panel = new JetStyleConfigurationPanel(config);
        f.getContentPane().add(panel);
        f.pack();
        f.setVisible(true);
    }
}
