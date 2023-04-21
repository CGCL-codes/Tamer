package org.gvsig.raster.gui.preferences.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import org.gvsig.raster.Configuration;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.util.BasePanel;

/**
 * Clase para la configuracion general de Raster. Aqui tenemos todas las variables
 * que no tienen una secci�n en concreto.
 * 
 * @version 12/12/2007
 * @author BorSanZa - Borja S�nchez Zamorano (borja.sanchez@iver.es)
 */
public class PreferenceGeneral extends BasePanel {

    private static final long serialVersionUID = 1L;

    private JCheckBox checkBoxPreview = null;

    private JCheckBox checkBoxCoordinates = null;

    private JLabel labelNumClases = null;

    private JComboBox comboBoxNumClases = null;

    private JCheckBox checkBoxProjection = null;

    /**
	 *Inicializa componentes gr�ficos y traduce
	 */
    public PreferenceGeneral() {
        init();
        translate();
    }

    /**
	 * Todas las traducciones de esta clase.
	 */
    protected void translate() {
        setBorder(BorderFactory.createTitledBorder(getText(this, "general")));
        getCheckBoxPreview().setText(getText(this, "previsualizar_automaticamente_raster"));
        getCheckBoxPreview().setToolTipText(getCheckBoxPreview().getText());
        getLabelNumClases().setText(getText(this, "num_clases") + ":");
        getCheckBoxCoordinates().setText(getText(this, "pedir_coordenadas_georreferenciacion"));
        getCheckBoxProjection().setText(getText(this, "ask_for_projection"));
    }

    protected void init() {
        GridBagConstraints gridBagConstraints;
        setLayout(new GridBagLayout());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(5, 5, 2, 5);
        add(getCheckBoxPreview(), gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.EAST;
        gridBagConstraints.insets = new Insets(2, 5, 2, 2);
        add(getLabelNumClases(), gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new Insets(2, 2, 2, 5);
        add(getComboBoxNumClases(), gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new Insets(2, 5, 5, 5);
        add(getCheckBoxCoordinates(), gridBagConstraints);
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(2, 5, 5, 5);
        add(getCheckBoxProjection(), gridBagConstraints);
    }

    private JCheckBox getCheckBoxPreview() {
        if (checkBoxPreview == null) {
            checkBoxPreview = new JCheckBox();
            checkBoxPreview.setMargin(new Insets(0, 0, 0, 0));
        }
        return checkBoxPreview;
    }

    private JLabel getLabelNumClases() {
        if (labelNumClases == null) {
            labelNumClases = new JLabel();
        }
        return labelNumClases;
    }

    private JComboBox getComboBoxNumClases() {
        if (comboBoxNumClases == null) {
            comboBoxNumClases = new JComboBox();
            comboBoxNumClases.setModel(new DefaultComboBoxModel(new String[] { "32", "64", "128", "256" }));
        }
        return comboBoxNumClases;
    }

    private JCheckBox getCheckBoxCoordinates() {
        if (checkBoxCoordinates == null) {
            checkBoxCoordinates = new JCheckBox();
            checkBoxCoordinates.setMargin(new Insets(0, 0, 0, 0));
        }
        return checkBoxCoordinates;
    }

    /**
	 * Obtiene el checkbox que indica si se se muestran las opciones de proyecci�n
	 * en la carga de un raster o no. Si se marca no, se aplicar�n siempre las opciones
	 * por defecto.
	 * @return JCheckBox
	 */
    private JCheckBox getCheckBoxProjection() {
        if (checkBoxProjection == null) {
            checkBoxProjection = new JCheckBox();
            checkBoxProjection.setMargin(new Insets(0, 0, 0, 0));
        }
        return checkBoxProjection;
    }

    /**
	 * Establece los valodres por defecto
	 */
    public void initializeDefaults() {
        getCheckBoxCoordinates().setSelected(((Boolean) Configuration.getDefaultValue("general_ask_coordinates")).booleanValue());
        getCheckBoxProjection().setSelected(((Boolean) Configuration.getDefaultValue("general_ask_projection")).booleanValue());
        getCheckBoxPreview().setSelected(((Boolean) Configuration.getDefaultValue("general_auto_preview")).booleanValue());
        Integer defaultNumberOfClasses = (Integer) Configuration.getDefaultValue("general_defaultNumberOfClasses");
        for (int i = 0; i < getComboBoxNumClases().getItemCount(); i++) {
            if (getComboBoxNumClases().getItemAt(i).toString().equals(defaultNumberOfClasses.toString())) {
                getComboBoxNumClases().setSelectedIndex(i);
                break;
            }
        }
    }

    /**
	 * Establece los valores que han sido definidas por el usuario
	 */
    public void initializeValues() {
        getCheckBoxCoordinates().setSelected(Configuration.getValue("general_ask_coordinates", Boolean.valueOf(false)).booleanValue());
        getCheckBoxProjection().setSelected(Configuration.getValue("general_ask_projection", Boolean.valueOf(true)).booleanValue());
        getCheckBoxPreview().setSelected(Configuration.getValue("general_auto_preview", Boolean.valueOf(true)).booleanValue());
        Integer defaultNumberOfClasses = Configuration.getValue("general_defaultNumberOfClasses", Integer.valueOf(RasterLibrary.defaultNumberOfClasses));
        for (int i = 0; i < getComboBoxNumClases().getItemCount(); i++) {
            if (getComboBoxNumClases().getItemAt(i).toString().equals(defaultNumberOfClasses.toString())) {
                getComboBoxNumClases().setSelectedIndex(i);
                break;
            }
        }
    }

    /**
	 * Guarda los valores que han sido definidas por el usuario
	 */
    public void storeValues() {
        Configuration.setValue("general_ask_coordinates", Boolean.valueOf(getCheckBoxCoordinates().isSelected()));
        Configuration.setValue("general_auto_preview", Boolean.valueOf(getCheckBoxPreview().isSelected()));
        Configuration.setValue("general_defaultNumberOfClasses", Integer.valueOf(getComboBoxNumClases().getSelectedItem().toString()));
        Configuration.setValue("general_ask_projection", Boolean.valueOf(getCheckBoxProjection().isSelected()));
    }
}
