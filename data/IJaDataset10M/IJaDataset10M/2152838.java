package com.hifiremote.jp1;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import com.hifiremote.jp1.Function.User;

/**
 * The Class ExternalFunctionPanel.
 */
public class ExternalFunctionPanel extends TablePanel<ExternalFunction> {

    /**
   * Instantiates a new external function panel.
   * 
   * @param devUpgrade
   *          the dev upgrade
   */
    public ExternalFunctionPanel(DeviceUpgrade devUpgrade) {
        super("External Functions", devUpgrade, new ExternalFunctionTableModel(devUpgrade));
        importItem = new JMenuItem("Import");
        importItem.setToolTipText("Import function(s) from an existing device upgrade.");
        importItem.addActionListener(this);
        popup.add(importItem);
        importButton = new JButton("Import");
        importButton.addActionListener(this);
        importButton.setToolTipText("Import function(s) from an existing device upgrade.");
        buttonPanel.add(importButton);
        initColumns();
    }

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();
        if ((source == importItem) || (source == importButton)) {
            File file = KeyMapMaster.promptForUpgradeFile(null);
            DeviceUpgrade importedUpgrade = new DeviceUpgrade();
            try {
                importedUpgrade.load(file);
                FunctionImportDialog d = new FunctionImportDialog(null, importedUpgrade);
                d.setVisible(true);
                if (d.getUserAction() == JOptionPane.OK_OPTION) {
                    List<Function> importedFunctions = d.getSelectedFunctions();
                    if (importedFunctions.size() > 0) {
                        List<ExternalFunction> externalFunctions = deviceUpgrade.getExternalFunctions();
                        int firstRow = externalFunctions.size();
                        for (Function f : importedFunctions) {
                            ExternalFunction ef = null;
                            if (f.isExternal()) ef = (ExternalFunction) f; else {
                                ef = new ExternalFunction();
                                ef.setName(f.getName());
                                Hex hex = f.getHex();
                                ef.setHex(hex);
                                if (hex.length() == 1) ef.setType(ExternalFunction.EFCType); else ef.setType(ExternalFunction.HexType);
                                ef.setSetupCode(importedUpgrade.getSetupCode());
                                ef.setDeviceTypeAliasName(importedUpgrade.getDeviceTypeAliasName());
                                ef.setNotes(f.getNotes());
                            }
                            externalFunctions.add(ef);
                        }
                        ((AbstractTableModel) table.getModel()).fireTableRowsInserted(firstRow, externalFunctions.size() - 1);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "An error occurred loading the device upgrade from " + file.getName() + ".  Please see rmaster.err for more details.", "Device Upgrade Load Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        super.actionPerformed(event);
    }

    public void update() {
        ((ExternalFunctionTableModel) model).update();
        super.update();
    }

    protected ExternalFunction createRowObject() {
        return new ExternalFunction();
    }

    protected boolean canDelete(ExternalFunction f) {
        return !f.assigned();
    }

    protected void delete(ExternalFunction f) {
        List<User> users = new ArrayList<User>(f.getUsers());
        for (User user : users) {
            deviceUpgrade.setFunction(user.button, null, user.state);
        }
    }

    /** The import item. */
    private JMenuItem importItem = null;

    /** The import button. */
    private JButton importButton = null;
}
