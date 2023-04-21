package saadadb.admintool.panels.editors;

import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import saadadb.admintool.AdminTool;
import saadadb.admintool.components.ToolBarPanel;
import saadadb.admintool.components.input.StringInput;
import saadadb.admintool.panels.EditPanel;
import saadadb.admintool.utils.HelpDesk;
import saadadb.admintool.utils.MyGBC;
import saadadb.database.Database;
import saadadb.database.InstallParamValidator;
import saadadb.exceptions.AbortException;
import saadadb.exceptions.QueryException;
import saadadb.exceptions.SaadaException;
import saadadb.sqltable.SQLTable;
import saadadb.sqltable.Table_SaadaDB;

/**
 * @author michel
 * @version $Id$
 *
 */
public class DBInstallPanel extends EditPanel {

    private JButton modDir, modRep;

    private JLabel dirRep;

    public DBInstallPanel(AdminTool rootFrame, String title, String icon, String ancestor) {
        super(rootFrame, title, null, ancestor);
    }

    public DBInstallPanel(AdminTool rootFrame, String ancestor) {
        super(rootFrame, DB_INSTALL, null, ancestor);
    }

    private static final long serialVersionUID = 1L;

    @Override
    protected void setToolBar() {
        this.initTreePathLabel();
        this.initSelectResourceLabel();
        this.add(new ToolBarPanel(this, false, false, false));
    }

    @Override
    protected void setActivePanel() {
        modDir = new JButton("Modify");
        modRep = new JButton("Modify");
        dirRep = getPlainLabel(Database.getRepository());
        modDir.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (showConfirmDialog(rootFrame, "<HTML>You are going to move your SaadaDB" + "<BR>This operation must be done very carefully" + "<BR>1- Change the installation dir from this panel" + "<BR>2- Move the content of the directory" + Database.getRoot_dir() + " into the new directory" + "<BR>3- Rename NEW_INSTALL_DIR/bin/saadadb.properties.new as saadadb.properties" + "<BR>4- Start saadmintool from the new location" + "<BR>5- Update Tomcat params and the repository directory" + "<BR>6- Deploy the Web application.")) {
                    StringInput ni = new StringInput(DBInstallPanel.this.rootFrame, "Type or paste the new install directory");
                    String new_name = ni.getNew_name();
                    if (new_name != null && new_name.length() > 0 && showConfirmDialog(rootFrame, "Are you really sure?")) {
                        try {
                            SQLTable.beginTransaction();
                            Table_SaadaDB.changeBasedir(new_name);
                            SQLTable.commitTransaction();
                            String old_dir = Database.getRoot_dir();
                            showInfo(rootFrame, "<HTML>You have changed your SaadaDB installation directory" + "<BR>Dot now by hand the following operations" + "<BR>1- Move the content of the directory" + old_dir + " into the directory " + new_name + "<BR>2- Rename " + new_name + Database.getSepar() + "bin " + Database.getSepar() + "saadadb.properties.new as saadadb.properties" + "<BR>3- Start saadmintool from the new location" + "<BR>4- Update Tomcat params and the repository directory" + "<BR>5- Deploy the Web application.");
                        } catch (SaadaException e1) {
                            showFatalError(ni, e1);
                        }
                    }
                }
            }
        });
        modRep.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fcd = new JFileChooser();
                fcd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int retour = fcd.showOpenDialog(DBInstallPanel.this.rootFrame);
                if (retour == JFileChooser.APPROVE_OPTION) {
                    File selected_file = fcd.getSelectedFile();
                    try {
                        InstallParamValidator.canBeRepository(selected_file.getAbsolutePath());
                    } catch (QueryException e) {
                        showFatalError(DBInstallPanel.this.rootFrame, e);
                        return;
                    }
                    try {
                        SQLTable.beginTransaction();
                        Table_SaadaDB.changeRepdir(selected_file.getAbsolutePath());
                        SQLTable.commitTransaction();
                        Database.init(Database.getName());
                        dirRep.setText(selected_file.getAbsolutePath());
                    } catch (AbortException e) {
                        showFatalError(DBInstallPanel.this.rootFrame, e);
                    }
                }
            }
        });
        JPanel panel = this.addSubPanel("SaadaDB Installation");
        MyGBC mgbc = new MyGBC(5, 5, 5, 5);
        mgbc.anchor = GridBagConstraints.EAST;
        panel.add(getPlainLabel("Name:"), mgbc);
        mgbc.next();
        mgbc.anchor = GridBagConstraints.WEST;
        panel.add(getPlainLabel(Database.getName()), mgbc);
        mgbc.rowEnd();
        panel.add(getPlainLabel(""), mgbc);
        mgbc.newRow();
        mgbc.gridwidth = 3;
        panel.add(getHelpLabel(HelpDesk.DBINSTALL_NAME), mgbc);
        mgbc.newRow();
        mgbc.anchor = GridBagConstraints.EAST;
        mgbc.gridwidth = 1;
        panel.add(getPlainLabel("Installation Directory "), mgbc);
        mgbc.next();
        mgbc.anchor = GridBagConstraints.WEST;
        panel.add(getPlainLabel(Database.getRoot_dir()), mgbc);
        mgbc.rowEnd();
        mgbc.anchor = GridBagConstraints.WEST;
        panel.add(modDir, mgbc);
        mgbc.newRow();
        mgbc.gridwidth = 3;
        panel.add(getHelpLabel(HelpDesk.DBINSTALL_DIR), mgbc);
        mgbc.newRow();
        mgbc.anchor = GridBagConstraints.EAST;
        mgbc.gridwidth = 1;
        panel.add(getPlainLabel("Repository "), mgbc);
        mgbc.next();
        mgbc.anchor = GridBagConstraints.WEST;
        panel.add(dirRep, mgbc);
        mgbc.rowEnd();
        mgbc.anchor = GridBagConstraints.WEST;
        panel.add(modRep, mgbc);
        mgbc.newRow();
        mgbc.gridwidth = 3;
        panel.add(getHelpLabel(HelpDesk.DBINSTALL_REP), mgbc);
    }

    @Override
    public void active() {
    }
}
