package com.umc.gui.content.wizard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.table.AbstractTableModel;
import org.netbeans.spi.wizard.WizardController;
import com.umc.helper.UMCLanguage;
import de.umcProject.xmlbeans.UmcConfigDocument.UmcConfig.Libraries.Library;
import de.umcProject.xmlbeans.UmcConfigDocument.UmcConfig.Libraries.Library.MovieScanDir;

/**
 * TableModel to be used in the wizard. 
 * 
 * @author DonGyros
 *
 */
public class UMCLibsTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -6743229955734533982L;

    private List<Object[]> libList = null;

    private List<String> columns = null;

    private Map map = null;

    private WizardController wizardController = null;

    public UMCLibsTableModel(List<String> columns, WizardController wizardController, Map map) throws Exception {
        this.columns = new ArrayList<String>(columns);
        this.libList = new ArrayList<Object[]>();
        this.map = map;
        this.wizardController = wizardController;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    public int getColumnCount() {
        return columns.size();
    }

    public int getRowCount() {
        return libList.size();
    }

    public String getColumnName(int column) {
        return columns.get(column);
    }

    public Object getValueAt(int row, int col) {
        Object obj = null;
        boolean setProblem = false;
        for (int a = 0; a < getRowCount(); a++) {
            if (!(Boolean) libList.get(a)[0] && !(Boolean) libList.get(a)[1] && !(Boolean) libList.get(a)[2] && !(Boolean) libList.get(a)[3]) {
                setProblem = true;
                break;
            }
        }
        if (setProblem) wizardController.setProblem(UMCLanguage.getText("gui.wizard.step2.text6", UMCWizard.wizardLanguage)); else wizardController.setProblem(null);
        switch(col) {
            case 0:
                return libList.get(row)[0];
            case 1:
                return libList.get(row)[1];
            case 2:
                return libList.get(row)[2];
            case 3:
                return libList.get(row)[3];
            case 4:
                return libList.get(row)[4];
            case 5:
                return libList.get(row)[5];
            default:
                return "";
        }
    }

    public void setValueAt(Object value, int row, int col) {
        switch(col) {
            case 0:
                libList.get(row)[0] = (Boolean) value;
                break;
            case 1:
                libList.get(row)[1] = (Boolean) value;
                break;
            case 2:
                libList.get(row)[2] = (Boolean) value;
                break;
            case 3:
                libList.get(row)[3] = (Boolean) value;
                break;
            case 4:
                libList.get(row)[4] = (String) value;
                break;
            case 5:
                libList.get(row)[5] = (String) value;
                break;
        }
        map.put(UMCWizard.getInstance().getLibKey(UMCWizard.MAP_KEY_LIB_MOVIES, row + 1), (Boolean) libList.get(row)[0]);
        map.put(UMCWizard.getInstance().getLibKey(UMCWizard.MAP_KEY_LIB_TVSHOWS, row + 1), (Boolean) libList.get(row)[1]);
        map.put(UMCWizard.getInstance().getLibKey(UMCWizard.MAP_KEY_LIB_MUSIC, row + 1), (Boolean) libList.get(row)[2]);
        map.put(UMCWizard.getInstance().getLibKey(UMCWizard.MAP_KEY_LIB_PHOTOS, row + 1), (Boolean) libList.get(row)[3]);
        boolean setProblem = false;
        for (int a = 0; a < getRowCount(); a++) {
            if (!(Boolean) libList.get(a)[0] && !(Boolean) libList.get(a)[1] && !(Boolean) libList.get(a)[2] && !(Boolean) libList.get(a)[3]) {
                setProblem = true;
                break;
            }
        }
        if (setProblem) wizardController.setProblem(UMCLanguage.getText("gui.wizard.step2.text6", UMCWizard.wizardLanguage)); else wizardController.setProblem(null);
        this.fireTableRowsUpdated(0, getRowCount() - 1);
    }

    public Object[] getScanDir(int row) {
        if (row >= 0 && row < getRowCount()) return libList.get(row);
        return null;
    }

    public void addScanDir(Object[] obj) {
        libList.add(obj);
        if (libList.size() > 0) wizardController.setProblem(null);
    }

    public void removeLib(int index) {
        libList.remove(index);
    }

    public Class<?> getColumnClass(int col) {
        switch(col) {
            case 0:
                return Boolean.class;
            case 1:
                return Boolean.class;
            case 2:
                return Boolean.class;
            case 3:
                return Boolean.class;
            case 4:
                return String.class;
            case 5:
                return String.class;
            default:
                return String.class;
        }
    }
}

;
