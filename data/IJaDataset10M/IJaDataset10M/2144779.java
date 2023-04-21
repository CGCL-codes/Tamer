package com.iver.cit.gvsig.gui.preferencespage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import org.cresques.cts.IProjection;
import org.gvsig.gui.beans.swing.JButton;
import com.iver.andami.PluginServices;
import com.iver.andami.preferences.AbstractPreferencePage;
import com.iver.andami.preferences.StoreException;
import com.iver.cit.gvsig.addlayer.AddLayerDialog;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.gui.panels.CRSSelectPanel;
import com.iver.cit.gvsig.gui.panels.ColorChooserPanel;
import com.iver.cit.gvsig.gui.panels.crs.ISelectCrsPanel;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.cit.gvsig.project.documents.view.info.gui.CSSelectionDialog;
import com.iver.utiles.StringUtilities;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.swing.JComboBox;

/**
 *  View document configuration page.
 *  <b><b>
 *  Here the user can establish what settings wants to use by default regarding to
 *  the document View.
 *
 *
 * @author jaume dominguez faus - jaume.dominguez@iver.es
 */
public class ViewPage extends AbstractPreferencePage {

    private static final String DEFAULT_PROJECTION_KEY_NAME = "DefaultProjection";

    private static final String FACTORY_DEFAULT_PROJECTION = "EPSG:23030";

    private static final String ZOOM_IN_FACTOR_KEY_NAME = "ZoomInFactor";

    private static final String ZOOM_OUT_FACTOR_KEY_NAME = "ZoomOutFactor";

    private static final String ADD_NEW_LAYERS_IN_INVISIBLE_MODE_KEY_NAME = "NewLayersInInvisibleMode";

    private static final String SHOW_FILE_EXTENSIONS_KEY_NAME = "ShowFileExtensions";

    private static final String KEEP_SCALE_ON_RESIZING_KEY_NAME = "KeepScaleOnResizing";

    private static final String DEFAULT_SELECTION_COLOR_KEY_NAME = "DefaultSelectionColor";

    private static final String DEFAULT_VIEW_BACK_COLOR_KEY_NAME = "DefaultViewBackColor";

    private static final String DEFAULT_DISTANCE_AREA_KEY_NAME = "DefaultDistanceArea";

    private static final String DEFAULT_DISTANCE_UNITS_KEY_NAME = "DefaultDistanceUnits";

    private static final String DEFAULT_MAP_UNITS_KEY_NAME = "DefaultMapUnits";

    private static final double DEFAULT_ZOOM_IN_FACTOR = 2.0;

    private static final double DEFAULT_ZOOM_OUT_FACTOR = 0.5;

    private static final Color FACTORY_DEFAULT_SELECTION_COLOR = Color.YELLOW;

    private static final Color FACTORY_DEFAULT_VIEW_BACK_COLOR = Color.WHITE;

    private static String[] unitsNames = null;

    private static int FACTORY_DEFAULT_MAP_UNITS;

    private static int FACTORY_DEFAULT_DISTANCE_UNITS;

    private static int FACTORY_DEFAULT_DISTANCE_AREA;

    private JTextField txtZoomInFactor;

    private JTextField txtZoomOutFactor;

    protected static String id = ViewPage.class.getName();

    private ImageIcon icon;

    private JLabel lblDefaultProjection;

    private JButton btnChangeProjection;

    private String fontName;

    private JCheckBox chkInvisibleNewLayers;

    private JCheckBox chkKeepScaleOnResizing;

    private JCheckBox chkShowFileExtensions;

    private ColorChooserPanel jccDefaultSelectionColor;

    private ColorChooserPanel jccDefaultViewBackColor;

    private JComboBox jCmbMapUnits;

    private JComboBox jCmbDistanceUnits;

    private JComboBox jCmbDistanceArea;

    CRSSelectPanel jPanelProj = null;

    {
        Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                FontUIResource fur = (FontUIResource) value;
                fontName = fur.getFontName();
            }
        }
        String[] unitNames = MapContext.getDistanceNames();
        for (int i = 0; i < unitNames.length; i++) {
            if (unitNames[i].equals("Metros")) {
                FACTORY_DEFAULT_DISTANCE_UNITS = i;
                FACTORY_DEFAULT_DISTANCE_AREA = i;
                FACTORY_DEFAULT_MAP_UNITS = i;
                break;
            }
        }
    }

    /**
	 * Creates a new panel containing View preferences settings.
	 *
	 */
    public ViewPage() {
        super();
        icon = PluginServices.getIconTheme().get("vista-icono");
        lblDefaultProjection = new JLabel();
        lblDefaultProjection.setFont(new java.awt.Font(fontName, java.awt.Font.BOLD, 11));
        btnChangeProjection = new JButton(PluginServices.getText(this, "change"));
        btnChangeProjection.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ISelectCrsPanel panel = CRSSelectPanel.getUIFactory().getSelectCrsPanel(CRSFactory.getCRS(lblDefaultProjection.getText()), false);
                PluginServices.getMDIManager().addWindow(panel);
                if (panel.isOkPressed()) {
                    IProjection proj = panel.getProjection();
                    lblDefaultProjection.setText(proj.getAbrev());
                }
            }
        });
        addComponent(PluginServices.getText(this, "default_projection") + ":", lblDefaultProjection);
        IProjection proj = CRSFactory.getCRS("EPSG:23030");
        if (PluginServices.getMainFrame() != null) {
            proj = AddLayerDialog.getLastProjection();
        }
        jPanelProj = CRSSelectPanel.getPanel(proj);
        jPanelProj.setTransPanelActive(true);
        jPanelProj.setBounds(11, 400, 448, 35);
        jPanelProj.setPreferredSize(new Dimension(448, 35));
        jPanelProj.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (jPanelProj.isOkPressed()) {
                    lblDefaultProjection.setText(jPanelProj.getCurProj().getAbrev());
                }
            }
        });
        JPanel aux = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        aux.add(btnChangeProjection);
        addComponent("", aux);
        addComponent(new JLabel(" "));
        chkInvisibleNewLayers = new JCheckBox(PluginServices.getText(this, "options.view.invisible_new_layers"));
        addComponent("", chkInvisibleNewLayers);
        chkShowFileExtensions = new JCheckBox(PluginServices.getText(this, "options.view.show_file_extensions"));
        addComponent("", chkShowFileExtensions);
        chkKeepScaleOnResizing = new JCheckBox(PluginServices.getText(this, "options.view.keep_scale_on_resizing"));
        chkKeepScaleOnResizing.setEnabled(false);
        addComponent("", chkKeepScaleOnResizing);
        addComponent(new JLabel(" "));
        addComponent(PluginServices.getText(this, "zoom_in_factor") + ":", txtZoomInFactor = new JTextField("", 15));
        addComponent(PluginServices.getText(this, "zoom_out_factor") + ":", txtZoomOutFactor = new JTextField("", 15));
        addComponent(new JLabel(" "));
        addComponent(PluginServices.getText(this, "options.view.default_view_back_color"), jccDefaultViewBackColor = new ColorChooserPanel());
        addComponent(PluginServices.getText(this, "options.view.default_selection_color"), jccDefaultSelectionColor = new ColorChooserPanel(true));
        addComponent(new JLabel(" "));
        addComponent(PluginServices.getText(this, "map_units"), jCmbMapUnits = new JComboBox());
        addComponent(PluginServices.getText(this, "distance_units"), jCmbDistanceUnits = new JComboBox());
        addComponent(PluginServices.getText(this, "distance_area"), jCmbDistanceArea = new JComboBox());
        initializeValues();
    }

    public void initializeValues() {
        PluginServices ps = PluginServices.getPluginServices(this);
        XMLEntity xml = ps.getPersistentXML();
        if (xml.contains(DEFAULT_PROJECTION_KEY_NAME)) {
            lblDefaultProjection.setText(xml.getStringProperty(DEFAULT_PROJECTION_KEY_NAME));
        } else {
            lblDefaultProjection.setText(FACTORY_DEFAULT_PROJECTION);
        }
        Project.setDefaultProjection(CRSFactory.getCRS(lblDefaultProjection.getText()));
        if (xml.contains(ADD_NEW_LAYERS_IN_INVISIBLE_MODE_KEY_NAME)) {
            chkInvisibleNewLayers.setSelected(xml.getBooleanProperty(ADD_NEW_LAYERS_IN_INVISIBLE_MODE_KEY_NAME));
        }
        if (xml.contains(SHOW_FILE_EXTENSIONS_KEY_NAME)) {
            chkShowFileExtensions.setSelected(xml.getBooleanProperty(SHOW_FILE_EXTENSIONS_KEY_NAME));
        } else {
            chkShowFileExtensions.setSelected(false);
        }
        if (xml.contains(KEEP_SCALE_ON_RESIZING_KEY_NAME)) {
            chkKeepScaleOnResizing.setSelected(xml.getBooleanProperty(KEEP_SCALE_ON_RESIZING_KEY_NAME));
        }
        if (xml.contains(ZOOM_IN_FACTOR_KEY_NAME)) {
            double zoomInFactor = xml.getDoubleProperty(ZOOM_IN_FACTOR_KEY_NAME);
            txtZoomInFactor.setText(String.valueOf(zoomInFactor));
        } else {
            txtZoomInFactor.setText(String.valueOf(MapContext.ZOOMINFACTOR));
        }
        MapContext.ZOOMINFACTOR = Double.parseDouble(txtZoomInFactor.getText());
        if (xml.contains(ZOOM_OUT_FACTOR_KEY_NAME)) {
            double zoomOutFactor = xml.getDoubleProperty(ZOOM_OUT_FACTOR_KEY_NAME);
            txtZoomOutFactor.setText(String.valueOf(zoomOutFactor));
        } else {
            txtZoomOutFactor.setText(String.valueOf(MapContext.ZOOMOUTFACTOR));
        }
        MapContext.ZOOMOUTFACTOR = Double.parseDouble(txtZoomOutFactor.getText());
        ;
        jccDefaultViewBackColor.setColor(View.getDefaultBackColor());
        jccDefaultViewBackColor.setAlpha(255);
        jccDefaultSelectionColor.setColor(Project.getDefaultSelectionColor());
        String[] distanceNames = MapContext.getDistanceNames();
        if (Project.getDefaultMapUnits() <= distanceNames.length && distanceNames.length != jCmbMapUnits.getItemCount()) {
            ((DefaultComboBoxModel) jCmbMapUnits.getModel()).removeAllElements();
            for (int i = 0; i < distanceNames.length; i++) {
                ((DefaultComboBoxModel) jCmbMapUnits.getModel()).addElement(PluginServices.getText(this, distanceNames[i]));
            }
            jCmbMapUnits.setSelectedIndex(Project.getDefaultMapUnits());
        }
        if (Project.getDefaultDistanceUnits() <= distanceNames.length && distanceNames.length != jCmbDistanceUnits.getItemCount()) {
            ((DefaultComboBoxModel) jCmbDistanceUnits.getModel()).removeAllElements();
            for (int i = 0; i < distanceNames.length; i++) {
                ((DefaultComboBoxModel) jCmbDistanceUnits.getModel()).addElement(PluginServices.getText(this, distanceNames[i]));
            }
            jCmbDistanceUnits.setSelectedIndex(Project.getDefaultDistanceUnits());
        }
        String[] names = MapContext.getAreaNames();
        if (Project.getDefaultDistanceArea() <= names.length && names.length != jCmbDistanceArea.getItemCount()) {
            ((DefaultComboBoxModel) jCmbDistanceArea.getModel()).removeAllElements();
            for (int i = 0; i < names.length; i++) {
                ((DefaultComboBoxModel) jCmbDistanceArea.getModel()).addElement(PluginServices.getText(this, names[i]) + MapContext.getOfLinear(i));
            }
            jCmbDistanceArea.setSelectedIndex(Project.getDefaultDistanceArea());
        }
    }

    public String getID() {
        return id;
    }

    public String getTitle() {
        return PluginServices.getText(this, "Vista");
    }

    public JPanel getPanel() {
        return this;
    }

    public void storeValues() throws StoreException {
        String projName = lblDefaultProjection.getText();
        double zif = 1;
        double zof = 1;
        boolean invisibleNewLayers, keepScaleOnResize, showFileExtensions;
        Color selectionColor, viewBackColor;
        try {
            zif = Double.parseDouble(txtZoomInFactor.getText());
            zof = Double.parseDouble(txtZoomOutFactor.getText());
            if (zif == 0 || zof == 0) {
                throw new NumberFormatException();
            }
            Project.setDefaultProjection(CRSFactory.getCRS(projName));
            selectionColor = jccDefaultSelectionColor.getColor();
            Project.setDefaultSelectionColor(selectionColor);
            viewBackColor = jccDefaultViewBackColor.getColor();
            View.setDefaultBackColor(viewBackColor);
            Project.setDefaultMapUnits(jCmbMapUnits.getSelectedIndex());
            Project.setDefaultDistanceUnits(jCmbDistanceUnits.getSelectedIndex());
            Project.setDefaultDistanceArea(jCmbDistanceArea.getSelectedIndex());
            invisibleNewLayers = chkInvisibleNewLayers.isSelected();
            keepScaleOnResize = chkKeepScaleOnResizing.isSelected();
            showFileExtensions = chkShowFileExtensions.isSelected();
        } catch (Exception e) {
            throw new StoreException(PluginServices.getText(this, "factor_zoom_incorrecto"));
        }
        MapContext.ZOOMINFACTOR = zif;
        MapContext.ZOOMOUTFACTOR = zof;
        PluginServices ps = PluginServices.getPluginServices(this);
        XMLEntity xml = ps.getPersistentXML();
        xml.putProperty(DEFAULT_PROJECTION_KEY_NAME, projName);
        xml.putProperty(ZOOM_IN_FACTOR_KEY_NAME, zif);
        xml.putProperty(ZOOM_OUT_FACTOR_KEY_NAME, zof);
        xml.putProperty(ADD_NEW_LAYERS_IN_INVISIBLE_MODE_KEY_NAME, invisibleNewLayers);
        xml.putProperty(KEEP_SCALE_ON_RESIZING_KEY_NAME, keepScaleOnResize);
        xml.putProperty(SHOW_FILE_EXTENSIONS_KEY_NAME, showFileExtensions);
        xml.putProperty(DEFAULT_VIEW_BACK_COLOR_KEY_NAME, StringUtilities.color2String(viewBackColor));
        xml.putProperty(DEFAULT_SELECTION_COLOR_KEY_NAME, StringUtilities.color2String(selectionColor));
        xml.putProperty(DEFAULT_MAP_UNITS_KEY_NAME, jCmbMapUnits.getSelectedIndex());
        xml.putProperty(DEFAULT_DISTANCE_UNITS_KEY_NAME, jCmbDistanceUnits.getSelectedIndex());
        xml.putProperty(DEFAULT_DISTANCE_AREA_KEY_NAME, jCmbDistanceArea.getSelectedIndex());
    }

    public void initializeDefaults() {
        lblDefaultProjection.setText(FACTORY_DEFAULT_PROJECTION);
        txtZoomInFactor.setText(String.valueOf(DEFAULT_ZOOM_IN_FACTOR));
        txtZoomOutFactor.setText(String.valueOf(DEFAULT_ZOOM_OUT_FACTOR));
        chkInvisibleNewLayers.setSelected(false);
        chkKeepScaleOnResizing.setSelected(false);
        jccDefaultViewBackColor.setColor(FACTORY_DEFAULT_VIEW_BACK_COLOR);
        jccDefaultSelectionColor.setColor(FACTORY_DEFAULT_SELECTION_COLOR);
        jCmbMapUnits.setSelectedIndex(FACTORY_DEFAULT_MAP_UNITS);
        jCmbDistanceUnits.setSelectedIndex(FACTORY_DEFAULT_DISTANCE_UNITS);
        jCmbDistanceArea.setSelectedIndex(FACTORY_DEFAULT_DISTANCE_AREA);
    }

    public ImageIcon getIcon() {
        return icon;
    }

    public boolean isValueChanged() {
        return super.hasChanged();
    }

    public void setChangesApplied() {
        setChanged(false);
    }
}
