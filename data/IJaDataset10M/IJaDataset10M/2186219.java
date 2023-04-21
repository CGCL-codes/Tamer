package org.modss.facilitator.port.ui.option.comp;

import org.modss.facilitator.port.ui.option.*;
import org.modss.facilitator.shared.singleton.*;
import org.modss.facilitator.shared.resource.*;
import org.swzoo.log2.core.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

/**
 * File chooser component.
 */
public class FileChooserComponent extends JPanel implements PropertiesConfig {

    String text;

    String prop;

    boolean dironly = false;

    JFileChooser chooser = null;

    Color colorEnabled = Color.black;

    Color colorDisabled = Color.gray;

    JTextField ftext;

    JLabel label;

    JButton choose;

    Frame frame;

    String _choosebutton = resources.getProperty("dss.file.chooser.button.text", "CHOOSE");

    public FileChooserComponent(Frame frame, String text, String prop, boolean dironly) {
        this.frame = frame;
        this.text = text;
        this.prop = prop;
        this.dironly = dironly;
        setFont(new Font("Sanserif", Font.PLAIN, 12));
        initUI();
    }

    void initUI() {
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(grid);
        label = new JLabel(text);
        label.setFont(getFont());
        ftext = new JTextField(20);
        ftext.setMinimumSize(ftext.getPreferredSize());
        choose = new JButton(_choosebutton);
        choose.setFont(getFont());
        label.setForeground(choose.getForeground());
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.VERTICAL;
        c.ipadx = 0;
        c.ipady = 0;
        c.insets = new Insets(0, 0, 0, 10);
        c.anchor = GridBagConstraints.EAST;
        c.weightx = 0.0;
        c.weighty = 0.0;
        grid.setConstraints(label, c);
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipadx = 0;
        c.ipady = 0;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 1.0;
        c.weighty = 1.0;
        grid.setConstraints(ftext, c);
        c.gridx = 2;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.VERTICAL;
        c.ipadx = 0;
        c.ipady = 0;
        c.insets = new Insets(0, 10, 0, 0);
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 0.0;
        c.weighty = 0.0;
        grid.setConstraints(choose, c);
        this.add(label);
        this.add(ftext);
        this.add(choose);
        choose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent aev) {
                chooseIt();
            }
        });
    }

    void setState(String state) {
        ftext.setText(state);
    }

    void configureChooser() {
        chooser = new JFileChooser();
        chooser.setDialogTitle(text);
        if (dironly) {
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        } else {
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        }
        chooser.setApproveButtonText("Select");
    }

    /**
     * Invoked from a choose button action.
     */
    void chooseIt() {
        if (chooser == null) {
            configureChooser();
        }
        String filename = ftext.getText();
        if (filename == null) {
            LogTools.trace(logger, 25, "FileChooserComponent.choose() - filename is null.");
        } else {
            LogTools.trace(logger, 25, "FileChooserComponent.choose() - filename=" + filename);
            File parent = new File(new File(filename).getParent());
            chooser.setCurrentDirectory(parent);
            if (!dironly) chooser.setSelectedFile(new File(filename));
            chooser.rescanCurrentDirectory();
        }
        int choice = chooser.showDialog(frame, null);
        File chooserFile = chooser.getSelectedFile();
        File chooserDir = chooser.getCurrentDirectory();
        LogTools.trace(logger, 25, "FileChooserComponent.choose() - chooserFile=" + chooserFile);
        LogTools.trace(logger, 25, "FileChooserComponent.choose() - chooserDir=" + chooserDir);
        switch(choice) {
            case JFileChooser.APPROVE_OPTION:
                LogTools.trace(logger, 25, "FileChooserComponent.choose() - APPROVED.");
                break;
            case JFileChooser.CANCEL_OPTION:
                LogTools.trace(logger, 25, "FileChooserComponent.choose() - CANCELLED.");
                return;
            default:
                LogTools.warn(logger, "FileChooserComponent.choose() - Unexpected choice from JFileChooser.showDialog(); " + choice);
                return;
        }
        LogTools.trace(logger, 25, "FileChooserComponent.choose() - newPath=" + chooserFile.getAbsolutePath());
        try {
            ftext.setText(chooserFile.getCanonicalPath());
        } catch (IOException ioex) {
            LogTools.warn(logger, "FileChooserComponent.choose() - Could not convert=" + chooserFile.getAbsolutePath() + " into canonical path.  Reason: " + ioex.getMessage());
        }
    }

    /**
     * Initialise from a provided list of properties.
     *
     * @param settings a list of settings representing the options.
     */
    public void init(Properties settings) {
        String filename = settings.getProperty(prop);
        setState(filename);
        LogTools.trace(logger, 25, "FileChooserComponent.init() - " + prop + "=" + filename);
    }

    /**
     * Get the list of property settings from the preference object.
     *
     * @return settings as a properties object.
     */
    public Properties getProperties() {
        Properties p = new Properties();
        if (ftext.getText() != null) p.put(prop, ftext.getText());
        LogTools.trace(logger, 25, "FileChooserComponent.getProperties() - properties " + p);
        return p;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            ftext.setEnabled(true);
            label.setForeground(colorEnabled);
            choose.setEnabled(true);
        } else {
            ftext.setEnabled(false);
            label.setForeground(colorDisabled);
            choose.setEnabled(false);
        }
    }

    /** Logger. */
    private static final Logger logger = LogFactory.getLogger();

    /** Resource provider. */
    private static final ResourceProvider resources = Singleton.Factory.getInstance().getResourceProvider();
}
