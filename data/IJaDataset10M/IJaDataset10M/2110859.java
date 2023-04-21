package com.iver.cit.gvsig.geoprocess.impl.spatialjoin.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.geoprocess.core.fmap.XTypes;
import com.iver.cit.gvsig.geoprocess.core.gui.AbstractGeoprocessPanel;
import com.iver.cit.gvsig.geoprocess.core.gui.SpatialJoinNumericFieldSelection;
import com.iver.utiles.GenericFileFilter;

/**
 * @author jmorell
 */
public class GeoProcessingSpatialjoinPanel extends AbstractGeoprocessPanel implements SpatialJoinPanelIF, IWindow {

    private static final long serialVersionUID = 1L;

    private JButton jButton = null;

    private JComboBox firstLayerCombo = null;

    private JCheckBox firstSelectionCheck = null;

    private JComboBox secondLayerCombo = null;

    private JCheckBox secondSelectionCheck = null;

    private JLabel jLabel = null;

    private File outputFile = null;

    private JCheckBox nearestCheckbox = null;

    private SpatialJoinNumericFieldSelection fieldDialog = null;

    private Map sumarizeFunctions;

    private WindowInfo viewInfo;

    private JPanel resultLayerPanel = null;

    private JLabel jLabel7 = null;

    private JPanel targetLayerPanel = null;

    private JLabel jLabel6 = null;

    private JLabel jLabel5 = null;

    private JLabel jLabel4 = null;

    private JPanel sourceLayerPanel = null;

    private JLabel jLabel3 = null;

    private JLabel jLabel2 = null;

    private JLabel jLabel1 = null;

    /**
	 * This constructor initializes the set of layers
	 */
    public GeoProcessingSpatialjoinPanel(FLayers layers) {
        super();
        this.layers = layers;
        initialize();
    }

    /**
	 * This method initializes this
	 *
	 * @return void
	 */
    private void initialize() {
        GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
        gridBagConstraints3.insets = new java.awt.Insets(8, 14, 4, 32);
        gridBagConstraints3.gridy = 1;
        gridBagConstraints3.ipadx = 439;
        gridBagConstraints3.ipady = 85;
        gridBagConstraints3.gridx = 0;
        GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
        gridBagConstraints2.insets = new java.awt.Insets(4, 14, 4, 35);
        gridBagConstraints2.gridy = 2;
        gridBagConstraints2.ipadx = 436;
        gridBagConstraints2.ipady = 131;
        gridBagConstraints2.gridx = 0;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.insets = new java.awt.Insets(5, 14, 49, 37);
        gridBagConstraints1.gridy = 3;
        gridBagConstraints1.ipadx = 434;
        gridBagConstraints1.ipady = 44;
        gridBagConstraints1.gridx = 0;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(11, 15, 8, 202);
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 32;
        gridBagConstraints.ipady = 5;
        gridBagConstraints.gridx = 0;
        jLabel = new JLabel();
        this.setLayout(new GridBagLayout());
        this.setBounds(new java.awt.Rectangle(0, 0, 486, 377));
        this.add(jLabel, gridBagConstraints);
        this.add(getResultLayerPanel(), gridBagConstraints1);
        this.add(getTargetLayerPanel(), gridBagConstraints2);
        this.add(getSourceLayerPanel(), gridBagConstraints3);
        jLabel.setText(PluginServices.getText(this, "Enlace_espacial._Introduccion_de_datos") + ":");
        changeSelectedItemsJCheckBox();
        changeSelectedItemsJCheckBox1();
        changeSelectedItemsNumberJLabel();
        changeSelectedItemsNumberJLabel1();
    }

    private void changeSelectedItemsJCheckBox() {
        FLyrVect inputSelectable = (FLyrVect) (layers.getLayer((String) firstLayerCombo.getSelectedItem()));
        FBitSet fBitSet = null;
        try {
            fBitSet = inputSelectable.getRecordset().getSelection();
        } catch (ReadDriverException e) {
            e.printStackTrace();
        }
        if (fBitSet.cardinality() == 0) {
            firstSelectionCheck.setEnabled(false);
            firstSelectionCheck.setSelected(false);
        } else {
            firstSelectionCheck.setEnabled(true);
            firstSelectionCheck.setSelected(true);
        }
    }

    private void changeSelectedItemsJCheckBox1() {
        FLyrVect inputSelectable = (FLyrVect) (layers.getLayer((String) secondLayerCombo.getSelectedItem()));
        FBitSet fBitSet = null;
        try {
            fBitSet = inputSelectable.getRecordset().getSelection();
        } catch (ReadDriverException e) {
            e.printStackTrace();
        }
        if (fBitSet.cardinality() == 0) {
            secondSelectionCheck.setEnabled(false);
            secondSelectionCheck.setSelected(false);
        } else {
            secondSelectionCheck.setEnabled(true);
            secondSelectionCheck.setSelected(true);
        }
    }

    private void changeSelectedItemsNumberJLabel() {
        if (getFirstSelectionCheck().isSelected()) {
            FLyrVect inputSelectable = (FLyrVect) (layers.getLayer((String) firstLayerCombo.getSelectedItem()));
            FBitSet fBitSet = null;
            try {
                fBitSet = inputSelectable.getRecordset().getSelection();
            } catch (ReadDriverException e) {
                e.printStackTrace();
            }
            jLabel3.setText(new Integer(fBitSet.cardinality()).toString());
        } else {
            ReadableVectorial va = ((FLyrVect) layers.getLayer((String) firstLayerCombo.getSelectedItem())).getSource();
            try {
                jLabel3.setText(new Integer(va.getShapeCount()).toString());
            } catch (ReadDriverException e) {
                e.printStackTrace();
            }
        }
    }

    private void changeSelectedItemsNumberJLabel1() {
        if (getSecondSelectionCheck().isSelected()) {
            FLyrVect inputSelectable = (FLyrVect) (layers.getLayer((String) secondLayerCombo.getSelectedItem()));
            FBitSet fBitSet = null;
            try {
                fBitSet = inputSelectable.getRecordset().getSelection();
            } catch (ReadDriverException e) {
                e.printStackTrace();
            }
            jLabel6.setText(new Integer(fBitSet.cardinality()).toString());
        } else {
            String selected = (String) secondLayerCombo.getSelectedItem();
            ReadableVectorial va = ((FLyrVect) layers.getLayer(selected)).getSource();
            try {
                jLabel6.setText(new Integer(va.getShapeCount()).toString());
            } catch (ReadDriverException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * This method initializes jTextField
	 *
	 * @return javax.swing.JTextField
	 */
    public JTextField getFileNameResultTextField() {
        if (fileNameResultTextField == null) {
            super.getFileNameResultTextField().setBounds(new java.awt.Rectangle(139, 18, 175, 18));
        }
        return fileNameResultTextField;
    }

    /**
	 * This method initializes jButton
	 *
	 * @return javax.swing.JButton
	 */
    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setText(PluginServices.getText(this, "Abrir"));
            jButton.setBounds(new java.awt.Rectangle(318, 15, 106, 23));
            jButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    openResultFileDialog();
                }
            });
        }
        return jButton;
    }

    /**
	 * This method initializes firstLayerCombo
	 *
	 * @return javax.swing.JComboBox
	 */
    private JComboBox getFirstLayerCombo() {
        if (firstLayerCombo == null) {
            firstLayerCombo = new JComboBox();
            firstLayerCombo.setBounds(new java.awt.Rectangle(188, 6, 243, 25));
            DefaultComboBoxModel defaultModel = new DefaultComboBoxModel(getLayerNames());
            firstLayerCombo.setModel(defaultModel);
            firstLayerCombo.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    changeSelectedItemsJCheckBox();
                    changeSelectedItemsNumberJLabel();
                }
            });
        }
        return firstLayerCombo;
    }

    /**
	 * This method initializes firstSelectionCheck
	 *
	 * @return javax.swing.JCheckBox
	 */
    private JCheckBox getFirstSelectionCheck() {
        if (firstSelectionCheck == null) {
            firstSelectionCheck = new JCheckBox();
            firstSelectionCheck.setText(PluginServices.getText(this, "Usar_solamente_los_elementos_seleccionados"));
            firstSelectionCheck.setBounds(new java.awt.Rectangle(6, 31, 320, 24));
            firstSelectionCheck.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    firstLayerSelectionChecked(firstSelectionCheck.isSelected());
                }
            });
        }
        return firstSelectionCheck;
    }

    /**
	 * This method initializes secondLayerCombo
	 *
	 * @return javax.swing.JComboBox
	 */
    private JComboBox getSecondLayerCombo() {
        if (secondLayerCombo == null) {
            secondLayerCombo = new JComboBox();
            secondLayerCombo.setBounds(new java.awt.Rectangle(185, 20, 238, 25));
            DefaultComboBoxModel defaultModel = new DefaultComboBoxModel(getLayerNames());
            secondLayerCombo.setModel(defaultModel);
            secondLayerCombo.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    changeSelectedItemsJCheckBox1();
                    changeSelectedItemsNumberJLabel1();
                }
            });
        }
        return secondLayerCombo;
    }

    /**
	 * This method initializes secondSelectionCheck
	 *
	 * @return javax.swing.JCheckBox
	 */
    private JCheckBox getSecondSelectionCheck() {
        if (secondSelectionCheck == null) {
            secondSelectionCheck = new JCheckBox();
            secondSelectionCheck.setText(PluginServices.getText(this, "Usar_solamente_los_elementos_seleccionados"));
            secondSelectionCheck.setBounds(new java.awt.Rectangle(7, 79, 300, 24));
            secondSelectionCheck.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    secondLayerSelectionChecked(secondSelectionCheck.isSelected());
                }
            });
        }
        return secondSelectionCheck;
    }

    /**
	 * This method initializes nearestCheckbox
	 *
	 * @return javax.swing.JCheckBox
	 */
    private JCheckBox getNearestCheckbox() {
        if (nearestCheckbox == null) {
            nearestCheckbox = new JCheckBox();
            nearestCheckbox.setText(PluginServices.getText(this, "Obtener_mas_proximo"));
            nearestCheckbox.setBounds(new java.awt.Rectangle(7, 56, 297, 21));
        }
        return nearestCheckbox;
    }

    public boolean isNearestSelected() {
        return nearestCheckbox.isSelected();
    }

    public boolean openSumarizeFunction() {
        fieldDialog = new SpatialJoinNumericFieldSelection(getSecondLayer());
        fieldDialog.pack();
        fieldDialog.setSize(560, 300);
        fieldDialog.setVisible(true);
        this.sumarizeFunctions = fieldDialog.getSumarizationFunctions();
        return fieldDialog.isOk();
    }

    public FLyrVect getFirstLayer() {
        return (FLyrVect) layers.getLayer((String) firstLayerCombo.getSelectedItem());
    }

    public FLyrVect getSecondLayer() {
        return (FLyrVect) layers.getLayer((String) secondLayerCombo.getSelectedItem());
    }

    public boolean onlyFirstLayerSelected() {
        return firstSelectionCheck.isSelected();
    }

    public boolean onlySecondLayerSelected() {
        return secondSelectionCheck.isSelected();
    }

    public void openResultFileDialog() {
        JFileChooser jfc = new JFileChooser();
        jfc.addChoosableFileFilter(new GenericFileFilter("shp", "Ficheros SHP"));
        if (jfc.showSaveDialog((Component) PluginServices.getMainFrame()) == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            if (!(file.getPath().endsWith(".shp") || file.getPath().endsWith(".SHP"))) {
                file = new File(file.getPath() + ".shp");
            }
            outputFile = file;
        }
        if (jfc.getSelectedFile() != null) getFileNameResultTextField().setText(jfc.getSelectedFile().getAbsolutePath());
    }

    public void firstLayerSelectionChecked(boolean checked) {
        changeSelectedItemsNumberJLabel();
    }

    public void secondLayerSelectionChecked(boolean checked) {
        changeSelectedItemsNumberJLabel1();
    }

    public String[] getTargetLayerNumericFields() {
        String[] solution;
        FLyrVect layer = getSecondLayer();
        ArrayList list = new ArrayList();
        try {
            SelectableDataSource recordset = layer.getRecordset();
            int numFields = recordset.getFieldCount();
            for (int i = 0; i < numFields; i++) {
                if (XTypes.isNumeric(recordset.getFieldType(i))) {
                    list.add(recordset.getFieldName(i));
                }
            }
        } catch (ReadDriverException e) {
            return null;
        }
        solution = new String[list.size()];
        list.toArray(solution);
        return solution;
    }

    public Map getSumarizeFunctions() {
        return sumarizeFunctions;
    }

    public WindowInfo getWindowInfo() {
        if (viewInfo == null) {
            viewInfo = new WindowInfo(WindowInfo.MODALDIALOG);
            viewInfo.setTitle(PluginServices.getText(this, "Enlace_espacial"));
        }
        return viewInfo;
    }

    public Object getWindowProfile() {
        return WindowInfo.DIALOG_PROFILE;
    }

    /**
	 * This method initializes resultLayerPanel
	 *
	 * @return javax.swing.JPanel
	 */
    private JPanel getResultLayerPanel() {
        if (resultLayerPanel == null) {
            jLabel7 = new JLabel();
            jLabel7.setBounds(new java.awt.Rectangle(11, 18, 124, 18));
            jLabel7.setText(PluginServices.getText(this, "Cobertura_de_salida") + ":");
            resultLayerPanel = new JPanel();
            resultLayerPanel.setLayout(null);
            resultLayerPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
            resultLayerPanel.add(getJButton(), null);
            resultLayerPanel.add(getFileNameResultTextField(), null);
            resultLayerPanel.add(jLabel7, null);
        }
        return resultLayerPanel;
    }

    /**
	 * This method initializes targetLayerPanel
	 *
	 * @return javax.swing.JPanel
	 */
    private JPanel getTargetLayerPanel() {
        if (targetLayerPanel == null) {
            jLabel4 = new JLabel();
            jLabel4.setBounds(new java.awt.Rectangle(8, 20, 155, 24));
            jLabel4.setText(PluginServices.getText(this, "Cobertura_de_datos") + ":");
            jLabel5 = new JLabel();
            jLabel5.setBounds(new java.awt.Rectangle(7, 106, 238, 20));
            jLabel5.setText(PluginServices.getText(this, "Numero_de_elementos_seleccionados") + ":");
            jLabel6 = new JLabel();
            jLabel6.setBounds(new java.awt.Rectangle(243, 106, 41, 19));
            jLabel6.setText("00");
            targetLayerPanel = new JPanel();
            targetLayerPanel.setLayout(null);
            targetLayerPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
            targetLayerPanel.add(jLabel6, null);
            targetLayerPanel.add(jLabel5, null);
            targetLayerPanel.add(getSecondSelectionCheck(), null);
            targetLayerPanel.add(getNearestCheckbox(), null);
            targetLayerPanel.add(getSecondLayerCombo(), null);
            targetLayerPanel.add(jLabel4, null);
        }
        return targetLayerPanel;
    }

    /**
	 * This method initializes sourceLayerPanel
	 *
	 * @return javax.swing.JPanel
	 */
    private JPanel getSourceLayerPanel() {
        if (sourceLayerPanel == null) {
            jLabel1 = new JLabel();
            jLabel1.setBounds(new java.awt.Rectangle(11, 10, 151, 22));
            jLabel1.setText(PluginServices.getText(this, "Cobertura_de_entrada") + ":");
            jLabel2 = new JLabel();
            jLabel2.setBounds(new java.awt.Rectangle(6, 59, 228, 21));
            jLabel2.setText(PluginServices.getText(this, "Numero_de_elementos_seleccionados") + ":");
            jLabel3 = new JLabel();
            jLabel3.setBounds(new java.awt.Rectangle(243, 62, 41, 16));
            jLabel3.setText("00");
            sourceLayerPanel = new JPanel();
            sourceLayerPanel.setLayout(null);
            sourceLayerPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
            sourceLayerPanel.add(jLabel3, null);
            sourceLayerPanel.add(jLabel2, null);
            sourceLayerPanel.add(getFirstSelectionCheck(), null);
            sourceLayerPanel.add(getFirstLayerCombo(), null);
            sourceLayerPanel.add(jLabel1, null);
        }
        return sourceLayerPanel;
    }
}
