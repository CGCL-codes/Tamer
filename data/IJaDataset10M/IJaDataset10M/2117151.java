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
 * Text field component.
 */
public class TextComponent extends JPanel implements PropertiesConfig {

    String text;

    int len;

    String prop;

    Color colorEnabled = Color.black;

    Color colorDisabled = Color.gray;

    JTextField ftext;

    JLabel label;

    Frame frame;

    public TextComponent(String text, String prop) {
        this(text, -1, prop);
    }

    public TextComponent(String text, int len, String prop) {
        LogTools.trace(logger, 25, "TextComponent.<init> - text=" + text + ",len=" + len + ",prop=" + prop);
        this.text = text;
        this.len = len;
        this.prop = prop;
        setFont(new Font("Sanserif", Font.PLAIN, 12));
        initUI();
    }

    void initUI() {
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(grid);
        label = new JLabel(text);
        label.setFont(getFont());
        if (len != -1) {
            ftext = new JTextField(len);
            ftext.setMinimumSize(ftext.getPreferredSize());
        } else {
            ftext = new JTextField();
        }
        label.setForeground(Color.black);
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
        c.fill = (len == -1) ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
        c.ipadx = 0;
        c.ipady = 0;
        c.insets = new Insets(0, 0, 0, 0);
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;
        c.weighty = 1.0;
        grid.setConstraints(ftext, c);
        this.add(label);
        this.add(ftext);
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            ftext.setEnabled(true);
            label.setForeground(colorEnabled);
        } else {
            ftext.setEnabled(false);
            label.setForeground(colorDisabled);
        }
    }

    void setState(String state) {
        ftext.setText(state);
    }

    /**
     * Initialise from a provided list of properties.
     *
     * @param settings a list of settings representing the options.
     */
    public void init(Properties settings) {
        String textvalue = settings.getProperty(prop);
        setState(textvalue);
        LogTools.trace(logger, 25, "TextComponent.init() - " + prop + "=" + textvalue);
    }

    /**
     * Get the list of property settings from the preference object.
     *
     * @return settings as a properties object.
     */
    public Properties getProperties() {
        Properties p = new Properties();
        if (ftext.getText() != null) p.put(prop, ftext.getText());
        LogTools.trace(logger, 25, "TextComponent.getProperties() - properties " + p);
        return p;
    }

    /** Logger. */
    private static final Logger logger = LogFactory.getLogger();

    /** Resource provider. */
    private static final ResourceProvider resources = Singleton.Factory.getInstance().getResourceProvider();
}
