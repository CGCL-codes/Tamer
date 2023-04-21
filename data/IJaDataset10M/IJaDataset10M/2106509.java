package com.iver.cit.gvsig.geoprocess.impl.convexhull.gui;

import java.awt.Component;
import java.io.File;
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
import com.iver.cit.gvsig.geoprocess.core.gui.AbstractGeoprocessPanel;
import com.iver.utiles.GenericFileFilter;

public class GeoProcessingConvexHullPanel extends AbstractGeoprocessPanel implements IWindow, ConvexHullPanelIF {

    private static final long serialVersionUID = 1L;

    private JLabel jLabel = null;

    private JCheckBox selectedOnlyCheckbox = null;

    private JButton openFileButton = null;

    private WindowInfo viewInfo;

    private JPanel resultLayerPanel = null;

    private JLabel jLabel1 = null;

    private JPanel inputLayerPanel = null;

    private JLabel jLabel2 = null;

    /**
	 * This constructor initializes the set of layers
	 */
    public GeoProcessingConvexHullPanel(FLayers layers) {
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
        jLabel = new JLabel();
        this.setLayout(null);
        this.setBounds(new java.awt.Rectangle(0, 0, 486, 377));
        jLabel.setText("Convex_Hull._Introduccion_de_datos");
        jLabel.setBounds(10, 26, 426, 21);
        this.add(jLabel, null);
        this.add(getLayersComboBox(), null);
        this.add(getResultLayerPanel(), null);
        this.add(getInputLayerPanel(), null);
        layersComboBox.setSelectedIndex(0);
        initSelectedItemsJCheckBox();
    }

    private void initSelectedItemsJCheckBox() {
        String selectedLayer = (String) layersComboBox.getSelectedItem();
        FLyrVect inputLayer = (FLyrVect) layers.getLayer(selectedLayer);
        FBitSet fBitSet = null;
        try {
            fBitSet = inputLayer.getRecordset().getSelection();
        } catch (ReadDriverException e) {
            e.printStackTrace();
        }
        if (fBitSet.cardinality() == 0) {
            selectedOnlyCheckbox.setEnabled(false);
        } else {
            selectedOnlyCheckbox.setEnabled(true);
        }
        selectedOnlyCheckbox.setSelected(false);
    }

    private JCheckBox getSelectedOnlyCheckBox() {
        if (selectedOnlyCheckbox == null) {
            selectedOnlyCheckbox = new JCheckBox();
            selectedOnlyCheckbox.setText(PluginServices.getText(this, "Usar_solamente_los_elementos_seleccionados"));
            selectedOnlyCheckbox.setBounds(new java.awt.Rectangle(10, 72, 300, 24));
        }
        return selectedOnlyCheckbox;
    }

    /**
	 * This method initializes layersComboBox
	 *
	 * @return javax.swing.JComboBox
	 */
    private JComboBox getLayersComboBox() {
        if (layersComboBox == null) {
            layersComboBox = new JComboBox();
            DefaultComboBoxModel defaultModel = new DefaultComboBoxModel(getLayerNames());
            layersComboBox.setModel(defaultModel);
            layersComboBox.setBounds(142, 26, 123, 23);
            layersComboBox.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    initSelectedItemsJCheckBox();
                }
            });
        }
        return layersComboBox;
    }

    public JTextField getFileNameResultTextField() {
        if (fileNameResultTextField == null) {
            super.getFileNameResultTextField().setBounds(new java.awt.Rectangle(135, 11, 169, 21));
        }
        return fileNameResultTextField;
    }

    /**
	 * This method initializes openFileButton
	 *
	 * @return javax.swing.JButton
	 */
    private JButton getOpenFileButton() {
        if (openFileButton == null) {
            openFileButton = new JButton();
            openFileButton.setText(PluginServices.getText(this, "Abrir"));
            openFileButton.setBounds(new java.awt.Rectangle(311, 12, 101, 21));
            openFileButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    openResultFile();
                }
            });
        }
        return openFileButton;
    }

    public void openResultFile() {
        JFileChooser jfc = new JFileChooser();
        jfc.addChoosableFileFilter(new GenericFileFilter("shp", "Ficheros SHP"));
        if (jfc.showSaveDialog((Component) PluginServices.getMainFrame()) == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            if (!(file.getPath().endsWith(".shp") || file.getPath().endsWith(".SHP"))) {
                file = new File(file.getPath() + ".shp");
            }
        }
        if (jfc.getSelectedFile() != null) {
            getFileNameResultTextField().setText(jfc.getSelectedFile().getAbsolutePath());
        }
    }

    public FLyrVect getInputLayer() {
        FLyrVect solution = null;
        String selectedLayer = (String) layersComboBox.getSelectedItem();
        solution = (FLyrVect) layers.getLayer(selectedLayer);
        return solution;
    }

    public boolean isConvexHullOnlySelected() {
        return selectedOnlyCheckbox.isSelected();
    }

    public WindowInfo getWindowInfo() {
        if (viewInfo == null) {
            viewInfo = new WindowInfo(WindowInfo.MODALDIALOG);
            viewInfo.setTitle(PluginServices.getText(this, "Convex_Hull"));
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
            jLabel1 = new JLabel();
            jLabel1.setBounds(new java.awt.Rectangle(4, 13, 126, 17));
            jLabel1.setText(PluginServices.getText(this, "Cobertura_de_salida") + ":");
            resultLayerPanel = new JPanel();
            resultLayerPanel.setLayout(null);
            resultLayerPanel.setBounds(new java.awt.Rectangle(11, 200, 426, 39));
            resultLayerPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
            resultLayerPanel.add(getOpenFileButton(), null);
            resultLayerPanel.add(getFileNameResultTextField(), null);
            resultLayerPanel.add(jLabel1, null);
        }
        return resultLayerPanel;
    }

    /**
	 * This method initializes inputLayerPanel
	 *
	 * @return javax.swing.JPanel
	 */
    private JPanel getInputLayerPanel() {
        if (inputLayerPanel == null) {
            jLabel2 = new JLabel();
            jLabel2.setBounds(new java.awt.Rectangle(11, 26, 149, 23));
            jLabel2.setText(PluginServices.getText(this, "Cobertura_de_entrada") + ":");
            inputLayerPanel = new JPanel();
            inputLayerPanel.setLayout(null);
            inputLayerPanel.setBounds(new java.awt.Rectangle(10, 60, 428, 133));
            inputLayerPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
            inputLayerPanel.add(getSelectedOnlyCheckBox(), null);
            inputLayerPanel.add(jLabel2, null);
            inputLayerPanel.add(getLayersComboBox(), null);
        }
        return inputLayerPanel;
    }
}
