package org.gvsig.raster.beans.createlayer;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import org.gvsig.fmap.raster.layers.FLyrRasterSE;
import org.gvsig.raster.RasterLibrary;
import org.gvsig.raster.dataset.GeoRasterWriter;
import org.gvsig.raster.dataset.io.RasterDriverException;
import org.gvsig.raster.hierarchy.IRasterGeoOperations;
import org.gvsig.raster.util.ExtendedFileFilter;
import org.gvsig.raster.util.PropertyEvent;
import org.gvsig.raster.util.PropertyListener;
import org.gvsig.raster.util.RasterToolsUtil;
import com.iver.andami.PluginServices;
import com.iver.andami.Utilities;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.addlayer.fileopen.FileOpenWizard;

/**
 * 
 * @version 18/06/2008
 * @author BorSanZa - Borja S�nchez Zamorano (borja.sanchez@iver.es)
 */
public class NewLayerPanel implements PropertyListener {

    private static final long serialVersionUID = -8569485642371687008L;

    private JPanel panel = null;

    private JRadioButton jRBFileGenerate = null;

    private JRadioButton jRBOpenMemory = null;

    private JTextField filenameTextField = null;

    private JLabel labelFilename = null;

    private JPanel panelNameFile = null;

    private boolean compress = false;

    private FLyrRasterSE lyr = null;

    private boolean onlyReprojectables = false;

    /**
	 * Constructor de un CreateLayerPanel
	 */
    public NewLayerPanel(FLyrRasterSE lyr) {
        this.lyr = lyr;
        initialize();
        translate();
    }

    /**
	 * Devuelve el panel principal
	 * @return
	 */
    public JPanel getJPanel() {
        if (panel == null) {
            panel = new JPanel();
        }
        return panel;
    }

    private void initialize() {
        ButtonGroup buttonGroup2;
        GridBagConstraints gridBagConstraints;
        getJPanel().setLayout(new GridBagLayout());
        getJPanel().setBorder(BorderFactory.createTitledBorder(""));
        buttonGroup2 = new ButtonGroup();
        buttonGroup2.add(getRadioFileGenerate());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        gridBagConstraints.weightx = 1.0;
        getJPanel().add(getRadioFileGenerate(), gridBagConstraints);
        buttonGroup2.add(getRadioOpenMemory());
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = GridBagConstraints.WEST;
        gridBagConstraints.insets = new Insets(3, 3, 3, 3);
        getJPanel().add(getRadioOpenMemory(), gridBagConstraints);
    }

    /**
	 * Seccion donde ir�n todas las traducciones invariables del componente
	 */
    private void translate() {
        getRadioFileGenerate().setText(PluginServices.getText(this, "generar_fichero"));
        getRadioOpenMemory().setText(PluginServices.getText(this, "abrir_memoria"));
        getLabelFilename().setText(PluginServices.getText(this, "nombre_capa") + ":");
    }

    /**
	 * Devuelve el JRadioButton de generar fichero
	 * @return
	 */
    public JRadioButton getRadioFileGenerate() {
        if (jRBFileGenerate == null) {
            jRBFileGenerate = new JRadioButton();
            jRBFileGenerate.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            jRBFileGenerate.setMargin(new Insets(0, 0, 0, 0));
        }
        return jRBFileGenerate;
    }

    /**
	 * Devuelve el JRadioButton de Abrir en memoria
	 * @return
	 */
    public JRadioButton getRadioOpenMemory() {
        if (jRBOpenMemory == null) {
            jRBOpenMemory = new JRadioButton();
            jRBOpenMemory.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            jRBOpenMemory.setMargin(new Insets(0, 0, 0, 0));
            jRBOpenMemory.addItemListener(new ItemListener() {

                public void itemStateChanged(ItemEvent evt) {
                    jRBOpenMemoryStateChanged(evt);
                }
            });
            jRBOpenMemory.setSelected(true);
        }
        return jRBOpenMemory;
    }

    /**
	 * This method initializes filenameTextField
	 *
	 * @return javax.swing.JTextField
	 */
    private JTextField getFilenameTextField() {
        if (filenameTextField == null) {
            filenameTextField = new JTextField();
            filenameTextField.setPreferredSize(new Dimension(100, filenameTextField.getPreferredSize().height));
            RasterLibrary.addOnlyLayerNameListener(this);
        }
        return filenameTextField;
    }

    /**
	 * This method initializes jPNameFile
	 *
	 * @return javax.swing.JPanel
	 */
    public JPanel getFileNamePanel() {
        if (panelNameFile == null) {
            panelNameFile = new JPanel();
            panelNameFile.add(getLabelFilename());
            panelNameFile.add(getFilenameTextField());
            updateNewLayerText();
        }
        return panelNameFile;
    }

    private JLabel getLabelFilename() {
        if (labelFilename == null) {
            labelFilename = new JLabel();
        }
        return labelFilename;
    }

    /**
	 * Establece si esta habilitado o no el cuadro de texto de entrada de nombre de
	 * fichero
	 * @param value
	 */
    public void setFilenameEnabled(boolean value) {
        getFilenameTextField().setEnabled(value);
        getLabelFilename().setEnabled(value);
    }

    /**
	 * Poner los estados de los RadioButton en caso de que cambien de valor
	 * @param evt
	 */
    protected void jRBOpenMemoryStateChanged(ItemEvent evt) {
        if (getRadioOpenMemory().getSelectedObjects() != null) {
            setFilenameEnabled(true);
        } else {
            setFilenameEnabled(false);
        }
    }

    /**
	 * Especificar el nombre de la nueva capa para el recuadro de texto asignando
	 * en cada llamada un nombre consecutivo.
	 */
    public void updateNewLayerText() {
        getFilenameTextField().setText(RasterLibrary.getOnlyLayerName());
    }

    /**
	 * Establece el texto de la etiqueta del nombre de fichero
	 * @param text
	 */
    public void setLabelFilename(String text) {
        getLabelFilename().setText(text);
    }

    /**
	 * Asigna un valor para el par�metro que informa de si el raster de salida hay
	 * que comprimirlo o no. Este valor es necesario cuando el raster de salida 
	 * es mayor de 4G ya que no se puede crear un tiff tan grande.
	 * @param compress true para comprimir el raster de salida y false para no hacerlo.
	 */
    public void setCompress(boolean compress) {
        this.compress = compress;
    }

    /**
	 * Devuelve la ruta del fichero donde se va a guardar, en caso de guardarse
	 * en memoria, calcula el nombre sin preguntar y devuelve la ruta.
	 * @return
	 */
    public String getFileSelected() {
        String path = "";
        if (getRadioFileGenerate().isSelected()) {
            JFileChooser chooser = new JFileChooser(FileOpenWizard.getLastPath());
            chooser.setDialogTitle(PluginServices.getText(this, "seleccionar_fichero"));
            ArrayList extList = new ArrayList();
            try {
                extList = GeoRasterWriter.getExtensionsSupported(((IRasterGeoOperations) lyr).getDataType()[0], ((IRasterGeoOperations) lyr).getBandCount(), onlyReprojectables);
            } catch (RasterDriverException e2) {
                RasterToolsUtil.messageBoxError("error_extensiones_soportadas", chooser, e2);
                return null;
            }
            ExtendedFileFilter selected = null;
            for (int i = 0; i < extList.size(); i++) {
                ExtendedFileFilter filter = new ExtendedFileFilter((String) extList.get(i));
                if (extList.get(i).equals("tif")) {
                    selected = filter;
                    filter.addExtension((String) "tiff");
                }
                if (extList.get(i).equals("jpg")) {
                    filter.addExtension((String) "jpeg");
                }
                if (extList.get(i).equals("mpl")) {
                    filter.addExtension((String) "mpr");
                }
                if (extList.get(i).equals("lan")) {
                    filter.addExtension((String) "gis");
                }
                chooser.addChoosableFileFilter(filter);
            }
            if (selected != null) chooser.setFileFilter(selected);
            chooser.setAcceptAllFileFilterUsed(false);
            IWindow w = PluginServices.getMDIManager().getActiveWindow();
            if (!(w instanceof Component)) w = null;
            if (chooser.showSaveDialog((Component) w) != JFileChooser.APPROVE_OPTION) return null;
            FileOpenWizard.setLastPath(chooser.getSelectedFile().getPath().substring(0, chooser.getSelectedFile().getPath().lastIndexOf(File.separator)));
            ExtendedFileFilter fileFilter = (ExtendedFileFilter) chooser.getFileFilter();
            path = fileFilter.getNormalizedFilename(chooser.getSelectedFile());
        } else {
            String file = getFilenameTextField().getText();
            if (file.compareTo(RasterLibrary.getOnlyLayerName()) == 0) RasterLibrary.usesOnlyLayerName();
            if (!compress) path = Utilities.createTempDirectory() + File.separator + file + ".tif"; else path = Utilities.createTempDirectory() + File.separator + file + ".jp2";
            updateNewLayerText();
        }
        return path;
    }

    /**
	 * Cuando alguien ha cambiado la propiedad del nombre de la 
	 * capa se actualiza autom�ticamente
	 */
    public void actionValueChanged(PropertyEvent e) {
        updateNewLayerText();
    }

    /**
	 * @param onlyReprojectables the onlyReprojectables to set
	 */
    public void setOnlyReprojectables(boolean onlyReprojectables) {
        this.onlyReprojectables = onlyReprojectables;
    }
}
