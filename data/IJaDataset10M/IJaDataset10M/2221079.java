package com.iver.cit.gvsig.project.documents.view.legend.gui;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;
import org.gvsig.gui.beans.swing.GridBagLayoutPanel;
import org.gvsig.gui.beans.swing.JBlank;
import com.hardcode.driverManager.Driver;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.driver.DriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.fmap.drivers.DBLayerDefinition;
import com.iver.cit.gvsig.fmap.drivers.DriverIOException;
import com.iver.cit.gvsig.fmap.drivers.dbf.DBFDriver;
import com.iver.cit.gvsig.fmap.drivers.shp.IndexedShpDriver;
import com.iver.cit.gvsig.fmap.edition.VectorialEditableAdapter;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrDefault;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.layers.VectorialAdapter;
import com.iver.cit.gvsig.fmap.layers.VectorialDBAdapter;
import com.iver.cit.gvsig.fmap.layers.VectorialFileAdapter;
import com.iver.cit.gvsig.project.documents.view.IProjectView;
import com.iver.cit.gvsig.project.documents.view.legend.CreateSpatialIndexMonitorableTask;
import com.iver.utiles.swing.threads.IMonitorableTask;

/**
 * This class implements an useful and intuitive graphic interface to change some
 * properties of the layer. This class extends AbstractThemeManager. The properties
 * that allow modified are the name of the layer, the scale, if the user want to use
 * Spatial index or not and the HyperLink. Also shows a scroll with a little resume
 * with the properties of the layer.
 * @author jmorell
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class General extends AbstractThemeManagerPage {

    private static final long serialVersionUID = 1L;

    private FLayer layer;

    private IProjectView view;

    private NumberFormat nf = NumberFormat.getInstance();

    private JPanel pnlLayerName = null;

    private GridBagLayoutPanel pnlScale = null;

    private JPanel pnlProperties = null;

    private JLabel lblLayerName = null;

    private JTextField txtLayerName = null;

    private JTextField txtMaxScale = null;

    private JTextArea propertiesTextArea = null;

    private JRadioButton rdBtnShowAlways = null;

    private JRadioButton rdBtnDoNotShow = null;

    private JTextField txtMinScale = null;

    private JComboBox cmbLinkField = null;

    private JTextField txtLinkExtension = null;

    private JComboBox cmbLinkType = null;

    private JCheckBox jCheckBoxSpatialIndex = null;

    private JPanel pnlHyperLink;

    private JLabel lblLinkExtension;

    private JLabel lblLinkField;

    private JLabel lblDefaultAction;

    private JPanel pnlFieldAndExtension;

    private JPanel pnlHyperLinkAction;

    private JScrollPane scrlProperties;

    /**
	 * This is the default constructor.
	 */
    public General() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 *
	 * @return void
	 */
    private void initialize() {
        this.setLayout(new BorderLayout());
        GridBagLayoutPanel aux = new GridBagLayoutPanel();
        aux.addComponent(getPnlLayerName());
        aux.addComponent(new JBlank(10, 10));
        aux.addComponent(getJCheckBoxSpatialIndex());
        aux.addComponent("", getPnlScale());
        aux.addComponent("", getPnlProperties());
        aux.setPreferredSize(getPreferredSize());
        this.add(aux, BorderLayout.CENTER);
        this.add(new JBlank(5, 10), BorderLayout.WEST);
        this.add(new JBlank(5, 10), BorderLayout.EAST);
    }

    /**
	 * Sets the necessary properties in the panel. This properties are
	 * extracted from the layer. With this properties fills the TextFields,
	 * ComboBoxes and the rest of GUI components.
	 * @param FLayer layer,
	 */
    public void setModel(FLayer layer) {
        this.layer = layer;
        if (layer instanceof FLyrVect) {
            FLyrVect lyrVect = (FLyrVect) layer;
            if (lyrVect.getISpatialIndex() == null) {
                getJCheckBoxSpatialIndex().setSelected(false);
            } else {
                getJCheckBoxSpatialIndex().setSelected(true);
            }
        }
        if (layer.getMinScale() != -1) getTxtMaxScale().setText(nf.format(layer.getMinScale()));
        if (layer.getMaxScale() != -1) getTxtMinScale().setText(nf.format(layer.getMaxScale()));
        if (layer.getMinScale() == -1 && layer.getMaxScale() == -1) {
            getRdBtnShowAlways().setSelected(true);
            txtMaxScale.setEnabled(false);
            txtMinScale.setEnabled(false);
        } else {
            getRdBtnDoNotShowWhen().setSelected(true);
            txtMaxScale.setEnabled(true);
            txtMinScale.setEnabled(true);
        }
        txtLayerName.setText(layer.getName());
        showLayerInfo();
    }

    /**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
    private JPanel getPnlLayerName() {
        if (pnlLayerName == null) {
            lblLayerName = new JLabel();
            pnlLayerName = new JPanel();
            lblLayerName.setText(PluginServices.getText(this, "Nombre") + ":");
            lblLayerName.setComponentOrientation(ComponentOrientation.UNKNOWN);
            pnlLayerName.setComponentOrientation(ComponentOrientation.UNKNOWN);
            pnlLayerName.add(lblLayerName, null);
            pnlLayerName.add(getTxtLayerName(), null);
        }
        return pnlLayerName;
    }

    /**
	 * This method initializes jPanel1
	 *
	 * @return javax.swing.JPanel
	 */
    private GridBagLayoutPanel getPnlScale() {
        if (pnlScale == null) {
            pnlScale = new GridBagLayoutPanel();
            pnlScale.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), PluginServices.getText(this, "rango_de_escalas"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(getRdBtnShowAlways());
            buttonGroup.add(getRdBtnDoNotShowWhen());
            pnlScale.addComponent(getRdBtnShowAlways());
            pnlScale.addComponent(getRdBtnDoNotShowWhen());
            JPanel aux;
            aux = new JPanel(new FlowLayout(FlowLayout.LEFT));
            aux.add(getTxtMaxScale());
            aux.add(new JLabel("(" + PluginServices.getText(this, "escala_maxima") + ")"));
            GridBagLayoutPanel aux2;
            aux2 = new GridBagLayoutPanel();
            aux2.addComponent(PluginServices.getText(this, "este_por_encima_de") + " 1:", aux);
            aux = new JPanel(new FlowLayout(FlowLayout.LEFT));
            aux.add(getTxtMinScale());
            aux.add(new JLabel("(" + PluginServices.getText(this, "escala_minima") + ")"));
            aux2.addComponent(PluginServices.getText(this, "este_por_debajo_de_") + " 1:", aux);
            pnlScale.addComponent(new JBlank(20, 1), aux2);
            pnlScale.addComponent(new JBlank(20, 1), aux2);
        }
        return pnlScale;
    }

    /**
	 * This method initializes jPanel2, this contains the ScrollPane with the
	 * properies.
	 *
	 * @return javax.swing.JPanel
	 */
    private JPanel getPnlProperties() {
        if (pnlProperties == null) {
            pnlProperties = new JPanel(new GridBagLayout());
            pnlProperties.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), PluginServices.getText(this, "propiedades"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets = new Insets(5, 5, 5, 5);
            constraints.fill = constraints.BOTH;
            constraints.weightx = 1.0;
            constraints.weighty = 1.0;
            pnlProperties.add(getScrlProperties(), constraints);
        }
        return pnlProperties;
    }

    /**
	 * This method initializes jTextField
	 *
	 * @return javax.swing.JTextField
	 */
    private JTextField getTxtLayerName() {
        if (txtLayerName == null) {
            txtLayerName = new JTextField(25);
            txtLayerName.setEditable(true);
        }
        return txtLayerName;
    }

    /**
	 * This method initializes TxtMaxScale
	 * @return jTextField1
	 */
    private JTextField getTxtMaxScale() {
        if (txtMaxScale == null) {
            txtMaxScale = new JTextField(15);
            txtMaxScale.setEnabled(false);
        }
        return txtMaxScale;
    }

    /**
	 * This method initilizes TxtArea, in this TextArea sets the text with
	 * the properties of the layer
	 * @return
	 */
    private JTextArea getPropertiesTextArea() {
        if (propertiesTextArea == null) {
            propertiesTextArea = new JTextArea();
            propertiesTextArea.setEditable(false);
            propertiesTextArea.setBackground(SystemColor.control);
        }
        return propertiesTextArea;
    }

    /**
	 * This method initializes jScrollPane
	 *
	 * @return javax.swing.JScrollPane
	 */
    private JScrollPane getScrlProperties() {
        if (scrlProperties == null) {
            scrlProperties = new JScrollPane();
            scrlProperties.setViewportView(getPropertiesTextArea());
            scrlProperties.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            scrlProperties.setPreferredSize(new Dimension(350, 180));
        }
        return scrlProperties;
    }

    /**
	 * This method initializes jRadioButton
	 *
	 * @return javax.swing.JRadioButton
	 */
    private JRadioButton getRdBtnShowAlways() {
        if (rdBtnShowAlways == null) {
            rdBtnShowAlways = new JRadioButton();
            rdBtnShowAlways.setText(PluginServices.getText(this, "Mostrar_siempre"));
            rdBtnShowAlways.setSelected(true);
            rdBtnShowAlways.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    txtMaxScale.setEnabled(false);
                    txtMinScale.setEnabled(false);
                }
            });
        }
        return rdBtnShowAlways;
    }

    /**
	 * This method initializes jRadioButton1
	 *
	 * @return javax.swing.JRadioButton
	 */
    private JRadioButton getRdBtnDoNotShowWhen() {
        if (rdBtnDoNotShow == null) {
            rdBtnDoNotShow = new JRadioButton();
            rdBtnDoNotShow.setText(PluginServices.getText(this, "No_mostrar_la_capa_cuando_la_escala"));
            rdBtnDoNotShow.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    txtMaxScale.setEnabled(true);
                    txtMinScale.setEnabled(true);
                }
            });
        }
        return rdBtnDoNotShow;
    }

    /**
	 * This method initializes jTextField2
	 *
	 * @return javax.swing.JTextField
	 */
    private JTextField getTxtMinScale() {
        if (txtMinScale == null) {
            txtMinScale = new JTextField(15);
            txtMinScale.setEnabled(false);
        }
        return txtMinScale;
    }

    private String getLayerName() {
        return txtLayerName.getText().toString();
    }

    /**
     * This method initializes jPanel3, this panel contains the components of the
     * HyperLink
     *
     * @return javax.swing.JPanel
     */
    private JPanel getPnlHyperLink() {
        if (pnlHyperLink == null) {
            pnlHyperLink = new JPanel();
            pnlHyperLink.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), PluginServices.getText(this, "Hiperenlace"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
            JPanel aux = new JPanel(new BorderLayout());
            pnlHyperLink.setLayout(new BorderLayout());
            aux.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
            aux.add(getPnlFieldAndExtension(), BorderLayout.NORTH);
            aux.add(getPnlHyperLinkAction(), BorderLayout.CENTER);
            pnlHyperLink.add(aux);
        }
        return pnlHyperLink;
    }

    private JPanel getPnlFieldAndExtension() {
        if (pnlFieldAndExtension == null) {
            lblLinkExtension = new JLabel();
            lblLinkExtension.setText(" \t \t" + PluginServices.getText(this, "extension"));
            lblLinkField = new JLabel();
            lblLinkField.setText(PluginServices.getText(this, "Campo"));
            pnlFieldAndExtension = new JPanel();
            pnlFieldAndExtension.add(lblLinkField, null);
            pnlFieldAndExtension.add(getCmbLinkField(), null);
            pnlFieldAndExtension.add(lblLinkExtension, null);
            pnlFieldAndExtension.add(getTxtLinkExtension(), null);
        }
        return pnlFieldAndExtension;
    }

    /**
     * This method initializes jPanel8. This panel contains the ComboBox to select
     * the action, (type of HyperLink)
     *
     * @return javax.swing.JPanel
     */
    private JPanel getPnlHyperLinkAction() {
        if (pnlHyperLinkAction == null) {
            lblDefaultAction = new JLabel();
            lblDefaultAction.setText(PluginServices.getText(this, "Accion_Predefinida") + "  ");
            pnlHyperLinkAction = new JPanel();
            pnlHyperLinkAction.add(lblDefaultAction, null);
            pnlHyperLinkAction.add(getCmbLinkType(), null);
        }
        return pnlHyperLinkAction;
    }

    /**
     * This method initializes jComboBox
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox getCmbLinkField() {
        if (cmbLinkField == null) {
            cmbLinkField = new JComboBox();
        }
        return cmbLinkField;
    }

    /**
     * This method initializes jTextField
     *
     * @return javax.swing.JTextField
     */
    private JTextField getTxtLinkExtension() {
        if (txtLinkExtension == null) {
            txtLinkExtension = new JTextField();
            txtLinkExtension.setPreferredSize(new Dimension(40, 20));
        }
        return txtLinkExtension;
    }

    /**
     * This method initializes jComboBox1
     *
     * @return javax.swing.JComboBox
     */
    private JComboBox getCmbLinkType() {
        if (cmbLinkType == null) {
            cmbLinkType = new JComboBox();
        }
        return cmbLinkType;
    }

    /**
     * @return Returns the view.
     */
    private IProjectView getView() {
        return view;
    }

    /**
     * This method initializes jCheckBox
     *
     * @return javax.swing.JCheckBox
     */
    private JCheckBox getJCheckBoxSpatialIndex() {
        if (jCheckBoxSpatialIndex == null) {
            jCheckBoxSpatialIndex = new JCheckBox();
            jCheckBoxSpatialIndex.setBounds(2, 33, 242, 23);
            jCheckBoxSpatialIndex.setText(PluginServices.getText(this, "Usar_indice_espacial"));
        }
        return jCheckBoxSpatialIndex;
    }

    /**
     * Returns the selected Type in the ComboBox
     */
    private int getLinkType() {
        return getCmbLinkType().getSelectedIndex();
    }

    /**
     * Returns the Selected Field in the ComboBox
     */
    private String getSelectedLinkField() {
        return (String) getCmbLinkField().getSelectedItem();
    }

    /**
     * Returns the Extension in the TextField
     */
    private String getExtensionLink() {
        return getTxtLinkExtension().getText();
    }

    /**
     * Returns true or false if the CheckBox is marked or not
     */
    private boolean isSpatialIndexSelected() {
        return getJCheckBoxSpatialIndex().isSelected();
    }

    /**
     * Add the information of the layer to the textArea
     */
    private void showLayerInfo() {
        try {
            String info = ((FLyrDefault) layer).getInfoString();
            if (info == null) {
                Rectangle2D fullExtentViewPort = layer.getFullExtent();
                IProjection viewPortProj = layer.getMapContext().getProjection();
                info = PluginServices.getText(this, "Extent") + " " + viewPortProj.getAbrev() + " (" + PluginServices.getText(this, "view_projection") + "):\n\t" + PluginServices.getText(this, "Superior") + ":\t" + fullExtentViewPort.getMaxY() + "\n\t" + PluginServices.getText(this, "Inferior") + ":\t" + fullExtentViewPort.getMinY() + "\n\t" + PluginServices.getText(this, "Izquierda") + ":\t" + fullExtentViewPort.getMinX() + "\n\t" + PluginServices.getText(this, "Derecha") + ":\t" + fullExtentViewPort.getMaxX() + "\n";
                if (!layer.getProjection().getAbrev().equals(viewPortProj.getAbrev())) {
                    IProjection nativeLayerProj = layer.getProjection();
                    ICoordTrans ct = viewPortProj.getCT(nativeLayerProj);
                    Rectangle2D nativeLayerExtent = ct.convert(fullExtentViewPort);
                    info += PluginServices.getText(this, "Extent") + " " + nativeLayerProj.getAbrev() + " (" + PluginServices.getText(this, "layer_native_projection") + "):\n\t" + PluginServices.getText(this, "Superior") + ":\t" + nativeLayerExtent.getMaxY() + "\n\t" + PluginServices.getText(this, "Inferior") + ":\t" + nativeLayerExtent.getMinY() + "\n\t" + PluginServices.getText(this, "Izquierda") + ":\t" + nativeLayerExtent.getMinX() + "\n\t" + PluginServices.getText(this, "Derecha") + ":\t" + nativeLayerExtent.getMaxX() + "\n";
                }
                if (layer instanceof FLyrVect) {
                    ReadableVectorial rv = ((FLyrVect) layer).getSource();
                    if (rv instanceof VectorialEditableAdapter) {
                        rv = ((VectorialEditableAdapter) ((FLyrVect) layer).getSource()).getOriginalAdapter();
                    }
                    info = info + PluginServices.getText(this, "Origen_de_datos") + ": ";
                    if (rv instanceof VectorialFileAdapter) {
                        Driver driver = rv.getDriver();
                        info = info + "\n" + driver.getName() + "\n" + PluginServices.getText(this, "fichero") + ": " + ((VectorialFileAdapter) rv).getFile();
                    } else if (rv instanceof VectorialDBAdapter) {
                        DBLayerDefinition dbdef = ((VectorialDBAdapter) rv).getLyrDef();
                        info = info + "\n" + rv.getDriver().getName() + "\n";
                        try {
                            info = info + PluginServices.getText(this, "url") + ": " + dbdef.getConnection().getURL() + "\n";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        info = info + PluginServices.getText(this, "Tabla") + ": " + dbdef.getComposedTableName() + "\n";
                    } else if (rv instanceof VectorialAdapter) {
                        info = info + "\n" + rv.getDriver().getName() + "\n";
                    }
                    info += "\n" + PluginServices.getText(this, "type") + ": " + ((FLyrVect) layer).getTypeStringVectorLayer() + "\n";
                } else {
                    info = info + PluginServices.getText(this, "Origen_de_datos") + ": " + layer.getName();
                }
            }
            getPropertiesTextArea().setText(info);
        } catch (ReadDriverException e) {
            NotificationManager.addError(e.getMessage(), e);
        }
    }

    /**
     * Returns true or false if the scale is activa
     */
    private boolean isScaleActive() {
        return getRdBtnDoNotShowWhen().isSelected();
    }

    /**
     * Instanciates a ITask (@see com.iver.utiles.swing.threads.ITask)
     * to create a spatial index for the selected layer in background.
     * This task also allow to monitor process evolution, and to cancel
     * this process.
     * @throws DriverException
     * @throws DriverIOException
     */
    private IMonitorableTask getCreateSpatialIndexTask() throws ReadDriverException {
        return new CreateSpatialIndexMonitorableTask((FLyrVect) layer);
    }

    public void acceptAction() {
        if (PluginServices.getMainFrame() != null) {
            IProjectView view = getView();
            view.setTypeLink(getLinkType());
            if (getSelectedLinkField() != null) {
                view.setSelectedField(getSelectedLinkField().toString().trim());
                view.setExtLink(getExtensionLink());
            }
        }
    }

    public void cancelAction() {
    }

    /**
	 * When we press the apply button, sets the new properties of the layer thar the
	 * user modified using the UI components
	 */
    public void applyAction() {
        if (isScaleActive()) {
            try {
                layer.setMinScale((nf.parse(getTxtMaxScale().getText())).doubleValue());
            } catch (ParseException ex) {
                if (getTxtMaxScale().getText().compareTo("") == 0) layer.setMinScale(-1); else System.err.print(ex.getLocalizedMessage());
            }
            try {
                layer.setMaxScale((nf.parse(getTxtMinScale().getText())).doubleValue());
            } catch (ParseException ex) {
                if (getTxtMinScale().getText().compareTo("") == 0) layer.setMaxScale(-1); else System.err.print(ex.getLocalizedMessage());
            }
        } else {
            layer.setMinScale(-1);
            layer.setMaxScale(-1);
        }
        if (!getLayerName().equals(layer.getName())) {
            layer.setName(getLayerName());
        }
        if (layer instanceof FLyrVect) {
            FLyrVect lyrVect = (FLyrVect) layer;
            if (isSpatialIndexSelected()) {
                if (lyrVect.getISpatialIndex() == null) {
                    try {
                        PluginServices.cancelableBackgroundExecution(getCreateSpatialIndexTask());
                    } catch (ReadDriverException e) {
                        NotificationManager.addError(e.getMessage(), e);
                    }
                }
            } else {
                lyrVect.deleteSpatialIndex();
            }
        }
        if (layer instanceof FLyrVect) {
            FLyrVect vectlyr = (FLyrVect) layer;
            if (getSelectedLinkField() != null) {
                vectlyr.getLinkProperties().setExt(getExtensionLink());
                vectlyr.getLinkProperties().setField(getSelectedLinkField().toString().trim());
                vectlyr.getLinkProperties().setType(getLinkType());
                System.out.println("Link Properties");
                System.out.println("Extensi�n: " + vectlyr.getLinkProperties().getExt());
                System.out.println("Campo: " + vectlyr.getLinkProperties().getField());
                System.out.println("Tipo: " + vectlyr.getLinkProperties().getType());
            }
        }
    }

    public String getName() {
        return PluginServices.getText(this, "General");
    }
}
