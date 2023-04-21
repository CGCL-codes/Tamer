package org.qfirst.batavia.ui.options;

import javax.swing.*;
import org.qfirst.batavia.utils.*;
import org.qfirst.batavia.ui.*;
import org.qfirst.batavia.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import org.apache.log4j.*;
import org.apache.commons.digester.*;
import org.apache.commons.lang.builder.*;
import java.io.*;
import org.xml.sax.*;
import bsh.*;
import java.awt.*;
import org.qfirst.batavia.model.*;
import java.net.*;
import org.qfirst.i18n.*;
import org.qfirst.options.*;
import org.qfirst.options.loader.*;
import org.qfirst.options.ui.*;
import org.qfirst.options.components.*;
import org.qfirst.batavia.mime.*;

/**
 *  Description of the Class
 *
 * @author     francisdobi
 * @created    May 15, 2004
 */
public class OptionDialog extends JDialog {

    Logger logger = Logger.getLogger(getClass());

    private Box buttons;

    private java.util.List optionPanels = new ArrayList();

    private OptionTreeNode rootNode;

    private OptionTreeNode appNode;

    private OptionTreeNode pluginNode;

    private OptionTreeNode mimeNode;

    private OptionPanel optionPanel;

    private OptionTreeNode shortcutNode = new OptionTreeNode();

    /**
	 *  Constructor for the OptionDialog object
	 */
    public OptionDialog(Frame parent) {
        super(parent);
        init();
    }

    /**
	 *  Constructor for the OptionDialog object
	 */
    public OptionDialog(Dialog parent) {
        super(parent);
        init();
    }

    /**
	 *  Description of the Method
	 */
    private void init() {
        loadOptionStructure();
        initComponents();
        loadOptions();
        setTitle(Message.getInstance().getMessage(Batavia.getLocale(), "title.options", "Options"));
        pack();
    }

    /**
	 *  Description of the Method
	 */
    private void initComponents() {
        Container container = getContentPane();
        container.setLayout(new BorderLayout(10, 10));
        logger.debug("rootNode: " + rootNode);
        optionPanel = new OptionPanel(rootNode);
        buttons = new Box(BoxLayout.X_AXIS);
        JButton ok = new JButton(Message.getInstance().getMessage(Batavia.getLocale(), "button.ok", "OK"));
        JButton cancel = new JButton(Message.getInstance().getMessage(Batavia.getLocale(), "button.cancel", "Cancel"));
        JButton apply = new JButton(Message.getInstance().getMessage(Batavia.getLocale(), "button.apply", "Apply"));
        JButton restoreDefaults = new JButton(Message.getInstance().getMessage(Batavia.getLocale(), "button.restore_defaults", "Restore defaults"));
        Box filler = new Box(BoxLayout.Y_AXIS);
        filler.add(Box.createVerticalStrut(30));
        JPanel p = new JPanel();
        container.add(optionPanel, BorderLayout.CENTER);
        buttons.add(Box.createHorizontalGlue());
        buttons.add(Box.createHorizontalStrut(20));
        buttons.add(ok);
        buttons.add(Box.createHorizontalStrut(10));
        buttons.add(cancel);
        buttons.add(Box.createHorizontalStrut(10));
        buttons.add(apply);
        buttons.add(Box.createHorizontalStrut(10));
        buttons.add(restoreDefaults);
        buttons.add(Box.createHorizontalStrut(20));
        buttons.add(Box.createHorizontalGlue());
        p.add(buttons);
        p.add(filler);
        container.add(p, BorderLayout.SOUTH);
        ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                okPressed();
            }
        });
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                cancelPressed();
            }
        });
        apply.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                applyPressed();
            }
        });
        restoreDefaults.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                restoreDefaultsPressed();
            }
        });
    }

    /**
	 *  Closes this dialog.
	 */
    private void closeDialog() {
        setVisible(false);
        dispose();
    }

    /**
	 *  Loads the option structure.
	 */
    private void loadOptionStructure() {
        try {
            logger.debug("Loading main options");
            appNode = Application.getInstance().getOptionTreeNode();
            pluginNode = new OptionTreeNode();
            pluginNode.setName(Message.getInstance().getMessage(Batavia.getLocale(), "string.plugins", "Plugins"));
            pluginNode.setId("plugins");
            rootNode = new OptionTreeNode();
            rootNode.setName("ROOT");
            rootNode.setId("ROOT");
            rootNode.add(appNode);
            try {
                logger.debug("Loading plugin options");
                loadPluginOptionStructure(pluginNode);
                rootNode.add(pluginNode);
            } catch (Exception ex) {
                logger.error("Cannot load plugin options", ex);
                return;
            }
            addShortcutNode(rootNode);
            mimeNode = MimeManager.getInstance().getMimeOptionTreeNode();
            rootNode.add(mimeNode);
        } catch (Exception ex) {
            logger.error("Error", ex);
        }
    }

    private void addShortcutNode(OptionTreeNode root) {
        shortcutNode.setName("Plugin shortcuts");
        shortcutNode.setId("plugin-shortcuts");
        shortcutNode.setOptionManager(Batavia.getOptionManager());
        root.add(shortcutNode);
        PluginManager pm = Batavia.getPluginManager();
        ListModel all = pm.getAllPlugins();
        for (int i = 0; i < all.getSize(); i++) {
            PluginInfo p = (PluginInfo) all.getElementAt(i);
            if (p.getShortcutNode().getOptionCount() != 0) shortcutNode.add(p.getShortcutNode());
        }
    }

    /**
	 *  Description of the Method
	 *
	 * @param  parent  Description of the Parameter
	 */
    private void loadPluginOptionStructure(OptionTreeNode parent) {
        PluginManager pm = Batavia.getPluginManager();
        pm.loadPluginsOptions();
        ListModel all = pm.getAllPlugins();
        for (int i = 0; i < all.getSize(); i++) {
            PluginInfo p = (PluginInfo) all.getElementAt(i);
            if ((p.getLoadState() & PluginInfo.STARTED) != 0 && p.getOptionTreeNode() != null) {
                parent.add(p.getOptionTreeNode());
            }
        }
    }

    /**
	 *  Description of the Method
	 */
    private void okPressed() {
        saveOptions();
        try {
            Batavia.flushOptions();
            closeDialog();
        } catch (Exception ex) {
            UIUtils.showErrorDialog(this, ex, Message.getInstance().getMessage(Batavia.getLocale(), "dialog.error", "Error"));
        }
    }

    /**
	 *  Description of the Method
	 */
    private void saveOptions() {
        appNode.saveOptions();
        PluginManager pm = Batavia.getPluginManager();
        ListModel all = pm.getAllPlugins();
        for (int i = 0; i < all.getSize(); i++) {
            PluginInfo p = (PluginInfo) all.getElementAt(i);
            if (p.getOptionTreeNode() != null) {
                p.getOptionTreeNode().saveOptions();
            }
        }
        shortcutNode.saveOptions();
        mimeNode.saveOptions();
    }

    /**
	 *  Description of the Method
	 */
    private void loadOptions() {
        appNode.loadOptions();
        PluginManager pm = Batavia.getPluginManager();
        ListModel all = pm.getAllPlugins();
        for (int i = 0; i < all.getSize(); i++) {
            PluginInfo p = (PluginInfo) all.getElementAt(i);
            if (p.getOptionTreeNode() != null) {
                p.getOptionTreeNode().loadOptions();
            }
        }
        shortcutNode.loadOptions();
        mimeNode.loadOptions();
    }

    /**
	 *  Description of the Method
	 */
    private void applyPressed() {
        saveOptions();
        try {
            Batavia.flushOptions();
        } catch (Exception ex) {
            UIUtils.showErrorDialog(this, ex, Message.getInstance().getMessage(Batavia.getLocale(), "dialog.error", "Error"));
        }
    }

    /**
	 *  Description of the Method
	 */
    private void cancelPressed() {
        closeDialog();
    }

    /**
	 *  Restore default values for the displayed panel.
	 */
    private void restoreDefaultsPressed() {
        OptionTreeNode selected = optionPanel.getSelectedGroup();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, Message.getInstance().getMessage(Batavia.getLocale(), "dialog.select_option_node_on_the_left", "Select a node on the left please!"), Message.getInstance().getMessage(Batavia.getLocale(), "title.select_node", "Select a node!"), JOptionPane.PLAIN_MESSAGE);
        } else if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this, Message.getInstance().getMessage(Batavia.getLocale(), "dialog.sure_to_restore_defaults", "Are you sure to restore defaults?"), Message.getInstance().getMessage(Batavia.getLocale(), "title.confirm", "Confirm"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)) {
            selected.restoreDefaults();
        }
    }
}
