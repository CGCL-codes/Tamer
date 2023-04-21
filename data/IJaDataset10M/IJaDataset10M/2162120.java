package de.ipkgatersleben.agbi.uploader.gui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.JPanel;
import de.ipkgatersleben.agbi.uploader.gui.AbstractAppAction;
import de.ipkgatersleben.agbi.uploader.gui.actions.ColumnSelectionPropertiesAction;
import de.ipkgatersleben.agbi.uploader.gui.bannerPanel.IBannelPanel;
import de.ipkgatersleben.agbi.uploader.gui.panels.advancedSettings.AdvancedSettingsGUI;
import de.ipkgatersleben.agbi.uploader.gui.panels.advancedSettings.IAdvancedSettings;
import de.ipkgatersleben.agbi.uploader.gui.panels.columnHeader.ColumnHeaderGUI;
import de.ipkgatersleben.agbi.uploader.gui.panels.columnHeader.IColumnHeaders;
import de.ipkgatersleben.agbi.uploader.gui.panels.columnSelection.ColumnSelectionGUI;
import de.ipkgatersleben.agbi.uploader.gui.panels.columnSelection.IColumnSelection;
import de.ipkgatersleben.agbi.uploader.gui.panels.dataSource.ColumnDataSourceGUI;
import de.ipkgatersleben.agbi.uploader.gui.panels.dataSource.IColumnDataSource;
import de.ipkgatersleben.agbi.uploader.gui.panels.saveOrder.ISaveOrder;
import de.ipkgatersleben.agbi.uploader.gui.panels.saveOrder.SaveOrderGUI;
import de.ipkgatersleben.agbi.uploader.gui.panels.selectedColumns.ISelectedColumns;
import de.ipkgatersleben.agbi.uploader.gui.panels.selectedColumns.SelectedColumnsGUI;
import de.ipkgatersleben.agbi.uploader.model.UplParameter;
import java.awt.Dimension;

public class SettingsPaneGUI extends JPanel implements ISettingsPane {

    private static final long serialVersionUID = 1L;

    private ColumnSelectionGUI columnSelectionGUI = null;

    private AdvancedSettingsGUI advencedSettingsGUI = null;

    private SaveOrderGUI saveOrderGUI = null;

    private JPanel helper = null;

    private ColumnHeaderGUI columnHeaderGUI = null;

    private SelectedColumnsGUI selectedColumnsGUI = null;

    private ColumnDataSourceGUI columnDataSourceGUI = null;

    private List<UplParameter> parameters;

    /**
	 * This is the default constructor
	 */
    public SettingsPaneGUI() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 1;
        gridBagConstraints11.fill = GridBagConstraints.BOTH;
        gridBagConstraints11.weightx = 25.0D;
        gridBagConstraints11.insets = new Insets(12, 12, 12, 12);
        gridBagConstraints11.gridheight = 2;
        gridBagConstraints11.weighty = 5.0D;
        gridBagConstraints11.gridy = 1;
        GridBagConstraints gridBagConstraints26 = new GridBagConstraints();
        gridBagConstraints26.gridx = 2;
        gridBagConstraints26.gridheight = 3;
        gridBagConstraints26.fill = GridBagConstraints.BOTH;
        gridBagConstraints26.weighty = 100.0D;
        gridBagConstraints26.weightx = 50.0D;
        gridBagConstraints26.insets = new Insets(12, 12, 12, 12);
        gridBagConstraints26.gridy = 0;
        GridBagConstraints gridBagConstraints18 = new GridBagConstraints();
        gridBagConstraints18.gridx = 0;
        gridBagConstraints18.fill = GridBagConstraints.BOTH;
        gridBagConstraints18.insets = new Insets(12, 12, 12, 12);
        gridBagConstraints18.anchor = GridBagConstraints.CENTER;
        gridBagConstraints18.weightx = 25.0D;
        gridBagConstraints18.gridwidth = 1;
        gridBagConstraints18.gridheight = 1;
        gridBagConstraints18.gridy = 2;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.insets = new Insets(12, 12, 12, 12);
        gridBagConstraints1.fill = GridBagConstraints.BOTH;
        gridBagConstraints1.anchor = GridBagConstraints.CENTER;
        gridBagConstraints1.weightx = 25.0D;
        gridBagConstraints1.weighty = 5.0D;
        gridBagConstraints1.gridy = 1;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.insets = new Insets(12, 12, 12, 12);
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.CENTER;
        gridBagConstraints.weightx = 50.0D;
        gridBagConstraints.weighty = 95.0D;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridy = 0;
        this.setSize(1000, 700);
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(1000, 700));
        this.add(getColumnSelectionGUI(), gridBagConstraints);
        this.add(getAdvencedSettingsGUI(), gridBagConstraints18);
        this.add(getSaveOrderGUI(), gridBagConstraints1);
        this.add(getHelper(), gridBagConstraints26);
        this.add(getColumnHeaderGUI(), gridBagConstraints11);
    }

    /**
	 * This method initializes tableColumnSelectionGUI	
	 * 	
	 * @return de.ipkgatersleben.agbi.uploader.gui.panels.TableColumnSelectionGUI	
	 */
    private ColumnSelectionGUI getColumnSelectionGUI() {
        if (columnSelectionGUI == null) {
            columnSelectionGUI = new ColumnSelectionGUI(getSelectedColumnsGUI());
            columnSelectionGUI.setPreferredSize(new Dimension(500, 300));
        }
        return columnSelectionGUI;
    }

    /**
	 * This method initializes advencedSettingsGUI	
	 * 	
	 * @return de.ipkgatersleben.agbi.uploader.gui.panels.AdvencedSettingsGUI	
	 */
    private AdvancedSettingsGUI getAdvencedSettingsGUI() {
        if (advencedSettingsGUI == null) {
            advencedSettingsGUI = new AdvancedSettingsGUI();
            advencedSettingsGUI.setPreferredSize(new Dimension(300, 250));
        }
        return advencedSettingsGUI;
    }

    /**
	 * This method initializes saveOrderGUI	
	 * 	
	 * @return de.ipkgatersleben.agbi.uploader.gui.panels.SaveOrderGUI	
	 */
    private SaveOrderGUI getSaveOrderGUI() {
        if (saveOrderGUI == null) {
            saveOrderGUI = new SaveOrderGUI();
            saveOrderGUI.setPreferredSize(new Dimension(300, 150));
        }
        return saveOrderGUI;
    }

    /**
	 * This method initializes helper	
	 * 	
	 * @return de.ipkgatersleben.agbi.uploader.gui.panels.Helper	
	 */
    private JPanel getHelper() {
        if (helper == null) {
            helper = new JPanel();
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 0;
            gridBagConstraints.fill = GridBagConstraints.BOTH;
            gridBagConstraints.weightx = 100.0D;
            gridBagConstraints.weighty = 50.0D;
            gridBagConstraints.gridy = 0;
            GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
            gridBagConstraints1.gridx = 0;
            gridBagConstraints1.fill = GridBagConstraints.BOTH;
            gridBagConstraints1.weightx = 100.0D;
            gridBagConstraints1.weighty = 50.0D;
            gridBagConstraints1.insets = new Insets(12, 0, 0, 0);
            gridBagConstraints1.gridy = 1;
            helper.setSize(360, 513);
            helper.setBorder(IBannelPanel.BANNERBODER);
            helper.setLayout(new GridBagLayout());
            helper.setPreferredSize(new Dimension(500, 700));
            helper.add(getSelectedColumnsGUI(), gridBagConstraints);
            helper.add(getColumnDataSourceGUI(), gridBagConstraints1);
        }
        return helper;
    }

    /**
	 * This method initializes columnHeaderGUI	
	 * 	
	 * @return de.ipkgatersleben.agbi.uploader.gui.panels.ColumnHeaderGUI	
	 */
    private ColumnHeaderGUI getColumnHeaderGUI() {
        if (columnHeaderGUI == null) {
            columnHeaderGUI = new ColumnHeaderGUI();
            columnHeaderGUI.setPreferredSize(new Dimension(200, 400));
        }
        return columnHeaderGUI;
    }

    /**
	 * This method initializes selectedColumnsGUI	
	 * 	
	 * @return de.ipkgatersleben.agbi.uploader.gui.panels.SelectedColumnsGUI	
	 */
    private SelectedColumnsGUI getSelectedColumnsGUI() {
        if (selectedColumnsGUI == null) {
            selectedColumnsGUI = new SelectedColumnsGUI(getColumnDataSourceGUI(), getSaveOrderGUI());
            selectedColumnsGUI.setPreferredSize(new Dimension(500, 350));
        }
        return selectedColumnsGUI;
    }

    /**
	 * This method initializes columnDataSourceGUI	
	 * 	
	 * @return de.ipkgatersleben.agbi.uploader.gui.panels.ColumnDataSourceGUI	
	 */
    private ColumnDataSourceGUI getColumnDataSourceGUI() {
        if (columnDataSourceGUI == null) {
            columnDataSourceGUI = new ColumnDataSourceGUI();
            columnDataSourceGUI.setPreferredSize(new Dimension(500, 350));
        }
        return columnDataSourceGUI;
    }

    public void setParameterEditAction(AbstractAppAction a) {
        getColumnDataSourceGUI().setParameterEditAction(a);
    }

    public IAdvancedSettings getAdvancedSettings() {
        return getAdvencedSettingsGUI();
    }

    public IColumnDataSource getColumnDataSource() {
        return getColumnDataSourceGUI();
    }

    public IColumnHeaders getColumnHeader() {
        return getColumnHeaderGUI();
    }

    public IColumnSelection getColumnSelection() {
        return getColumnSelectionGUI();
    }

    public ISaveOrder getSaveOrder() {
        return getSaveOrderGUI();
    }

    public ISelectedColumns getSelectedColumns() {
        return getSelectedColumnsGUI();
    }

    public List<UplParameter> getParameters() {
        return this.parameters;
    }

    public void setParameters(List<UplParameter> parameters) {
        this.parameters = parameters;
    }

    public void setSelectionProperiesAction(AbstractAppAction a) {
        getColumnSelectionGUI().setSelectionProperiesAction(a);
    }
}
