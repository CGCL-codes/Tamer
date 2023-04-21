package com.skruk.elvis.admin.plugin;

import com.skruk.elvis.admin.*;
import com.skruk.elvis.admin.gui.*;
import com.skruk.elvis.admin.i18n.*;
import com.skruk.elvis.admin.manage.marc21.gui.BinaryMarcLoadPanel;
import com.skruk.elvis.admin.manage.marc21.gui.MarcViewPanel;
import com.skruk.elvis.admin.manage.resources.*;
import com.skruk.elvis.admin.manage.resources.gui.*;
import com.skruk.elvis.admin.registry.*;
import com.skruk.elvis.admin.structure.*;
import javax.swing.*;

/**
 *  Description of the Class
 *  :tabSize=2:indentSize=2:noTabs=false:
 *  :folding=explicit:collapseFolds=1:*
 *
 * @author     skruk
 * @created    17 październik 2003
 */
public final class ResourcesPlugin implements ElvisPlugin, WizardFinishListener {

    /**  Description of the Field */
    com.skruk.elvis.admin.gui.Wizard dialogNewClassWizard = null;

    /**  Description of the Field */
    private static final ResourcesPlugin instance = new ResourcesPlugin();

    static {
        ElvisRegistry.getInstance().registerPlugin(instance);
    }

    /**  Constructor for the OntologyPlugin object */
    private ResourcesPlugin() {
        this.mainFormater = JElvisAdmin.getFormater();
    }

    /**
	 *  Gets the description attribute of the ElvisPlugin object
	 *
	 * @return    The description value
	 */
    public String getDescription() {
        return "Elvis Resources Management plugin - by skruk";
    }

    /**  Description of the Field */
    private ResourceFormatter formater = new ResourceFormatter("com.skruk.elvis.admin.i18n.ManageResources");

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
    void structureActionPerformed(java.awt.event.ActionEvent evt) {
        final String action = evt.getActionCommand();
        Thread t = new Thread(new Runnable() {

            public void run() {
                JElvisAdmin.getSystemProgress().setIndeterminate(true);
                if (action.equals("add_resource")) {
                    addResource();
                } else if (action.equals("manage_resources")) {
                    manageResources();
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
        com.skruk.elvis.admin.gui.WizardPanel[] panels = new com.skruk.elvis.admin.gui.WizardPanel[6];
        StructureElement entry = StructureStorage.getInstance().getBook();
        panels[0] = new NewResourcePanel(new ResourceEntry(entry));
        panels[0].setHelp(formater.getText("NewClassPanel_help"));
        panels[1] = new UploadResourcePanel();
        panels[1].setHelp(formater.getText("UploadResourcePanel_help"));
        panels[2] = new DescriptionSendPanel();
        panels[2].setHelp(formater.getText("DescriptionSendPanel_help"));
        panels[3] = new FinishSendPanel();
        panels[3].setHelp(formater.getText("FinishSendPanel_help"));
        panels[4] = new BinaryMarcLoadPanel();
        panels[4].setHelp(Marc21Plugin.getInstance().getFormater().getText("BinaryMarcLoadPanel_help"));
        panels[5] = new MarcViewPanel();
        panels[5].setHelp(Marc21Plugin.getInstance().getFormater().getText("MarcViewPanel_help"));
        for (int i = 0; i < panels.length; i++) {
            panels[i].setTitle(formater.getText("upload_panel_text", i));
        }
        panels[4].setTitle(Marc21Plugin.getInstance().getFormater().getText("upload_panel_text", 1));
        panels[5].setTitle(Marc21Plugin.getInstance().getFormater().getText("upload_panel_text", 2));
        dialogNewClassWizard = new com.skruk.elvis.admin.gui.Wizard(eaApp.getMainFrame(), panels, this);
        JDesktopPane desktop = this.eaApp.getDesktop();
        desktop.add(dialogNewClassWizard);
        dialogNewClassWizard.setVisible(true);
        try {
            dialogNewClassWizard.setMaximum(true);
            dialogNewClassWizard.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**  Description of the Field */
    java.awt.event.ActionListener actionListener = new java.awt.event.ActionListener() {

        public void actionPerformed(java.awt.event.ActionEvent evt) {
            structureActionPerformed(evt);
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
    protected boolean manageResources() {
        if (ResourcesManagementForm.isInstance()) {
            return false;
        }
        JInternalFrame frame = ResourcesManagementForm.getInstance();
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
