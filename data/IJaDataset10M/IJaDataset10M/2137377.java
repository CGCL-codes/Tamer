package org.digitall.apps.gaia.forms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.text.DecimalFormat;
import javax.swing.JTextField;
import org.digitall.apps.gaia.classes.GaiaClassProduccion;
import org.digitall.apps.gaia.components.GaiaCatastroInputPanel;
import org.digitall.common.components.combos.CachedCombo;
import org.digitall.common.components.inputpanels.CBInput;
import org.digitall.common.reports.BasicReport;
import org.digitall.deprecatedlibs.Proced;
import org.digitall.lib.common.OrganizationInfo;
import org.digitall.lib.components.Advisor;
import org.digitall.lib.components.basic.BasicPanel;
import org.digitall.lib.components.inputpanels.TFInput;
import org.digitall.lib.data.DataTypes;
import org.digitall.lib.environment.Environment;
import org.digitall.lib.geo.esri.ESRIPoint;
import org.digitall.lib.geo.gaia.GaiaPrimitiveForm;

public class GaiaFormProduccion extends GaiaPrimitiveForm {

    private BasicPanel panelData = new BasicPanel();

    private GaiaClassProduccion produccion;

    private CBInput cbTiposProduccion = new CBInput(CachedCombo.PRODUCCION_TABS, "Type", false);

    private ESRIPoint point;

    private GaiaCatastroInputPanel tfCatastro = new GaiaCatastroInputPanel();

    private TFInput tfNombre = new TFInput(DataTypes.STRING, "Name", false);

    private TFInput tfActividad = new TFInput(DataTypes.STRING, "ProductionActivity", false);

    private TFInput tfResponsable = new TFInput(DataTypes.STRING, "PersonCharge", false);

    private TFInput tfTelefono1 = new TFInput(DataTypes.STRING, "Phone1", false);

    private TFInput tfTelefono2 = new TFInput(DataTypes.STRING, "Phone2", false);

    private TFInput tfFax = new TFInput(DataTypes.STRING, "Fax", false);

    private TFInput tfPunto = new TFInput(DataTypes.STRING, "Coords", false);

    public GaiaFormProduccion() {
        this(new ESRIPoint(0, 0));
    }

    public GaiaFormProduccion(ESRIPoint _point) {
        super();
        try {
            point = _point;
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        setTitle("PRODUCCION");
        this.setSize(new Dimension(432, 308));
        panelData.setBounds(new Rectangle(10, 10, 290, 326));
        panelData.setLayout(null);
        panelData.setPreferredSize(new Dimension(270, 1));
        panelData.setSize(new Dimension(270, 352));
        cbTiposProduccion.setBounds(new Rectangle(5, 70, 260, 35));
        tfCatastro.setBounds(new Rectangle(5, 150, 75, 35));
        tfActividad.setBounds(new Rectangle(5, 190, 255, 35));
        tfTelefono1.setBounds(new Rectangle(5, 270, 110, 35));
        tfTelefono2.setBounds(new Rectangle(150, 270, 110, 35));
        tfFax.setBounds(new Rectangle(5, 310, 110, 35));
        tfPunto.setBounds(new Rectangle(5, 30, 255, 35));
        panelData.add(tfResponsable, null);
        panelData.add(tfNombre, null);
        panelData.add(tfPunto, null);
        panelData.add(tfFax, null);
        panelData.add(tfTelefono2, null);
        panelData.add(tfTelefono1, null);
        panelData.add(tfActividad, null);
        panelData.add(tfCatastro, null);
        panelData.add(cbTiposProduccion, null);
        this.setCentralPanel(panelData);
        cbTiposProduccion.autoSize();
        tfPunto.getTextField().setHorizontalAlignment(JTextField.CENTER);
        tfPunto.getTextField().setForeground(Color.red);
        tfPunto.setEditable(false);
        tfResponsable.setBounds(new Rectangle(5, 230, 255, 35));
        tfNombre.setBounds(new Rectangle(5, 110, 255, 35));
    }

    private void clearData() {
        tfPunto.setValue("");
        tfNombre.setValue("");
        tfCatastro.setValue(0);
        tfActividad.setValue("");
        tfResponsable.setValue("");
        tfTelefono1.setValue("");
        tfTelefono2.setValue("");
        tfFax.setValue("");
        cbTiposProduccion.setSelectedID(0);
        setEnabledDeleteButton(false);
    }

    public void delete() {
        if (produccion.delete()) {
            getLayer().getGeometrySet().removeGeometry(point);
            clearData();
        }
    }

    public void saveData() {
        boolean _isnew = produccion.getIdProduccion() == -1;
        produccion.setIdTipoProduccion(Integer.parseInt(cbTiposProduccion.getSelectedValue().toString()));
        produccion.setCatastro(Integer.parseInt(tfCatastro.getValue().toString()));
        produccion.setNombre(tfNombre.getValue().toString());
        produccion.setActividad((tfActividad.getValue().toString()));
        produccion.setResponsable(tfResponsable.getValue().toString());
        produccion.setTelefono1(tfTelefono1.getValue().toString());
        produccion.setTelefono2(tfTelefono2.getValue().toString());
        produccion.setFax(tfFax.getValue().toString());
        if (produccion.saveData() > 0) {
            getLayer().addLabelValue(point.getIdPoint(), produccion.getNombre());
            point.setIdPoint(produccion.getIdProduccion());
            point.setSymbol(produccion.getIdTipoProduccion());
            clearData();
            if (_isnew) {
                getLayer().getGeometrySet().addGeometry(point);
            }
        } else {
            Advisor.messageBox("Ocurri� un error al grabar los datos", "Error");
        }
    }

    public void setProduccionObject(GaiaClassProduccion _produccion) {
        produccion = _produccion;
        cbTiposProduccion.setSelectedID(produccion.getIdProduccion());
        loadData();
    }

    private void loadData() {
        tfPunto.setValue("(" + (new DecimalFormat("0.0000")).format(produccion.getX()) + "; " + (new DecimalFormat("0.0000")).format(produccion.getY()) + ")");
        cbTiposProduccion.setSelectedID(produccion.getIdTipoProduccion());
        tfNombre.setValue(produccion.getNombre());
        tfCatastro.setValue(produccion.getCatastro());
        tfActividad.setValue(produccion.getActividad());
        tfResponsable.setValue(produccion.getResponsable());
        tfTelefono1.setValue(produccion.getTelefono1());
        tfTelefono2.setValue(produccion.getTelefono2());
        tfFax.setValue(produccion.getFax());
        if (produccion.getIdProduccion() != -1) {
            setEnabledDeleteButton(true);
        } else {
            setEnabledDeleteButton(false);
        }
    }

    @Override
    public void setContentObject(Object _contentObject) {
        if (ESRIPoint.class.isInstance(_contentObject)) {
            produccion = new GaiaClassProduccion();
            point = (ESRIPoint) _contentObject;
            produccion.setIdProduccion(point.getIdPoint());
            produccion.retrieveData();
            produccion.setX(point.getX());
            produccion.setY(point.getY());
            setProduccionObject(produccion);
            tfCatastro.fetchCatastro(point);
        }
    }

    @Override
    public Object getContentObject() {
        return produccion;
    }

    @Override
    public void printReport() {
        BasicReport report = new BasicReport(GaiaFormProduccion.class.getResource("xml/ProduccionReport.xml"));
        String param = "0,0";
        report.addTableModel("gis_oran.xmlGetProduccion", param);
        report.doReport();
    }
}
