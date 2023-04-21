package com.skruk.elvis.admin.plugin;

import com.skruk.elvis.admin.JElvisAdmin;
import com.skruk.elvis.admin.gui.Progressable;
import com.skruk.elvis.admin.gui.SetupPanel;
import com.skruk.elvis.admin.gui.Wizard;
import com.skruk.elvis.admin.gui.WizardContext;
import com.skruk.elvis.admin.gui.WizardFinishListener;
import com.skruk.elvis.admin.i18n.ResourceFormatter;
import com.skruk.elvis.admin.manage.descriptions.gui.DescriptionsManagementForm;
import com.skruk.elvis.admin.registry.ElvisApp;
import com.skruk.elvis.admin.registry.ElvisPlugin;
import com.skruk.elvis.admin.registry.ElvisRegistry;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

/**
 *  Description of the Class
 *  :tabSize=2:indentSize=2:noTabs=false:
 *  :folding=explicit:collapseFolds=1:*
 *
 * @author     skruk
 * @created    17 październik 2003
 */
public final class DescriptionsPlugin implements ElvisPlugin, WizardFinishListener {

    /**  Description of the Field */
    private static final DescriptionsPlugin instance = new DescriptionsPlugin();

    static {
        ElvisRegistry.getInstance().registerPlugin(instance);
    }

    /**  Constructor for the OntologyPlugin object */
    private DescriptionsPlugin() {
        this.mainFormater = JElvisAdmin.getFormater();
        SetupPanel.addPanel("dialog_setup_description_panel", formater);
    }

    /**
	 *  Gets the description attribute of the ElvisPlugin object
	 *
	 * @return    The description value
	 */
    public String getDescription() {
        return "Elvis Description Management plugin - by skruk";
    }

    /**  Description of the Field */
    private ResourceFormatter formater = new ResourceFormatter("com.skruk.elvis.admin.i18n.ManageDescription");

    /**  Description of the Field */
    private ResourceFormatter mainFormater = null;

    /**  Description of the Field */
    private ElvisApp eaApp = null;

    /**
	 *  Description of the Method
	 *
	 * @param  app   Description of the Parameter
	 * @param  info  Description of the Parameter
	 * @return       Description of the Return Value
	 */
    public boolean notify(ElvisApp app, int info) {
        this.eaApp = app;
        this.registerMenu(app);
        return true;
    }

    /**
	 *  Description of the Method
	 *
	 * @param  params  Description of the Parameter
	 */
    public void synchronize(java.util.Map params) {
    }

    /**
	 *  Gets the instance attribute of the OntologyPlugin class
	 *
	 * @return    The instance value
	 */
    public static ElvisPlugin getInstance() {
        return instance;
    }

    /**
	 *  Description of the Method
	 *
	 * @param  app  Description of the Parameter
	 */
    protected void registerMenu(ElvisApp app) {
        JMenu jmResources = new JMenu();
        JMenuItem[] ajmiItems = new JMenuItem[formater.getInt("action_count")];
        jmResources.setText(formater.getText("menu_name"));
        for (int i = 0; i < ajmiItems.length; i++) {
            ajmiItems[i] = new JMenuItem();
            ajmiItems[i].setText(formater.getText("action_name", i));
            ajmiItems[i].setIcon(formater.getIcon("action_icon", mainFormater.getInt("action_icon_size"), "", i));
            ajmiItems[i].setActionCommand(formater.getText("action_command", i));
            ajmiItems[i].addActionListener(actionListener);
            jmResources.add(ajmiItems[i]);
            if (formater.getBoolean("action_separator", i)) {
                jmResources.addSeparator();
            }
        }
        int cnt = app.getMenu().getMenuCount();
        JMenu cmp = (JMenu) app.getMenu().getComponent(cnt - 1);
        app.getMenu().remove(cnt - 1);
        app.getMenu().add(jmResources);
        app.getMenu().add(cmp);
        JToolBar jtbResources = new JToolBar();
        jtbResources.setRollover(true);
        JButton[] ajbButtons = new JButton[formater.getInt("action_count")];
        for (int i = 0; i < ajbButtons.length; i++) {
            ajbButtons[i] = new JButton();
            if (formater.getBoolean("buttons_show_label")) {
                ajbButtons[i].setText(formater.getText("action_name", i));
                ajbButtons[i].setFont(new java.awt.Font("Dialog", 0, 10));
            }
            if (formater.getBoolean("buttons_show_icon")) {
                ajbButtons[i].setIcon(formater.getIcon("action_icon", mainFormater.getInt("button_icon_size"), "", i));
            }
            ajbButtons[i].setToolTipText(formater.getText("action_description", i));
            ajbButtons[i].setActionCommand(formater.getText("action_command", i));
            ajbButtons[i].setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            ajbButtons[i].setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
            ajbButtons[i].addActionListener(actionListener);
            jtbResources.add(ajbButtons[i]);
            if (formater.getBoolean("action_separator", i)) {
                jtbResources.addSeparator();
            }
        }
        int sz = app.getToolbarHolder().getComponentCount();
        java.awt.Component cp = app.getToolbarHolder().getComponent(sz - 1);
        app.getToolbarHolder().remove(sz - 1);
        app.getToolbarHolder().add(jtbResources);
        app.getToolbarHolder().add(cp);
    }

    /**
	 *  Description of the Method
	 *
	 * @param  evt  Description of the Parameter
	 */
    void descriptionActionPerformed(java.awt.event.ActionEvent evt) {
        final String action = evt.getActionCommand();
        Thread t = new Thread(new Runnable() {

            public void run() {
                JElvisAdmin.getSystemProgress().setIndeterminate(true);
                if (action.equals("manage_descriptions")) {
                    manageDescriptions();
                }
                JElvisAdmin.getSystemProgress().setIndeterminate(false);
            }
        });
        t.setDaemon(true);
        t.start();
    }

    /**
	 *  Adds a feature to the Resource attribute of the ResourcesPlugin object
	 *
	 * @return    Description of the Return Value
	 */
    protected boolean addResource() {
        return true;
    }

    /**  Description of the Field */
    java.awt.event.ActionListener actionListener = new java.awt.event.ActionListener() {

        public void actionPerformed(java.awt.event.ActionEvent evt) {
            descriptionActionPerformed(evt);
        }
    };

    /**
	 *  Description of the Method
	 *
	 * @param  context   Description of the Parameter
	 * @param  progress  Description of the Parameter
	 * @param  wizard    Description of the Parameter
	 */
    public void finishProcess(final WizardContext context, final Progressable progress, final Wizard wizard) {
    }

    /**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
    protected boolean manageDescriptions() {
        if (DescriptionsManagementForm.isInstance()) {
            return false;
        }
        JInternalFrame frame = DescriptionsManagementForm.getInstance();
        JDesktopPane desktop = this.eaApp.getDesktop();
        desktop.add(frame);
        frame.setVisible(true);
        try {
            frame.setMaximum(true);
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
	 *  Gets the formater attribute of the OntologyPlugin object
	 *
	 * @return    The formater value
	 */
    public com.skruk.elvis.admin.i18n.ResourceFormatter getFormater() {
        return this.formater;
    }
}
