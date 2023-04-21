package es.prodevelop.cit.gvsig.jdbc_spatial;

import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import jwizardcomponent.FinishAction;
import jwizardcomponent.JWizardComponents;
import org.apache.log4j.Logger;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.ViewPort;
import com.iver.cit.gvsig.fmap.drivers.DBLayerDefinition;
import com.iver.cit.gvsig.fmap.drivers.FieldDescription;
import com.iver.cit.gvsig.fmap.drivers.IConnection;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;
import com.prodevelop.cit.gvsig.vectorialdb.wizard.NewVectorDBConnectionPanel;
import es.prodevelop.cit.gvsig.fmap.drivers.jdbc.oracle.OracleSpatialDriver;
import es.prodevelop.cit.gvsig.fmap.drivers.jdbc.oracle.OracleSpatialWriter;
import es.prodevelop.cit.gvsig.jdbc_spatial.gui.jdbcwizard.RepeatedChooseGeometryTypePanel;
import es.prodevelop.cit.gvsig.jdbc_spatial.gui.jdbcwizard.RepeatedFieldDefinitionPanel;

public class NewOracleSpatialTableFinishAction extends FinishAction {

    private static Logger logger = Logger.getLogger(NewOracleSpatialTableFinishAction.class.getName());

    private NewVectorDBConnectionPanel connectionPanel;

    private RepeatedChooseGeometryTypePanel geoTypePanel;

    private RepeatedFieldDefinitionPanel fieldsPanel;

    private MapContext theMapContext;

    private IWindow closeThis;

    public NewOracleSpatialTableFinishAction(JWizardComponents wc, IWindow closeit, NewVectorDBConnectionPanel connPanel, MapContext mc) {
        super(wc);
        theMapContext = mc;
        closeThis = closeit;
        connectionPanel = connPanel;
        geoTypePanel = (RepeatedChooseGeometryTypePanel) wc.getWizardPanel(0);
        fieldsPanel = (RepeatedFieldDefinitionPanel) wc.getWizardPanel(1);
    }

    public void performAction() {
        PluginServices.getMDIManager().closeWindow(closeThis);
        ViewPort vp = theMapContext.getViewPort();
        DBLayerDefinition lyr_def = new DBLayerDefinition();
        FieldDescription[] flds = fieldsPanel.getFieldsDescription();
        FieldDescription[] flds_new = getValidFields(flds);
        lyr_def.setFieldsDesc(flds_new);
        String usr = connectionPanel.getConnectionWithParams().getUser();
        String table_name = connectionPanel.getTableName();
        lyr_def.setTableName(table_name);
        lyr_def.setUser(usr.toUpperCase());
        String epsg_code = vp.getProjection().getAbrev();
        lyr_def.setSRID_EPSG(epsg_code.substring(5));
        lyr_def.setFieldID(OracleSpatialDriver.ORACLE_ID_FIELD);
        lyr_def.setFieldGeometry(OracleSpatialDriver.DEFAULT_GEO_FIELD);
        lyr_def.setWhereClause("");
        Rectangle2D extent = vp.getAdjustedExtent();
        if (extent == null) {
            int h = vp.getImageHeight();
            int w = vp.getImageWidth();
            extent = new Rectangle2D.Double(0, 0, w, h);
        }
        IConnection iconn = connectionPanel.getConnectionWithParams().getConnection();
        try {
            OracleSpatialWriter.createEmptyTable(lyr_def, iconn, extent);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(connectionPanel, PluginServices.getText(this, "nombre_no_valido") + ": " + PluginServices.getText(this, "Tabla") + " " + lyr_def.getTableName() + "\n" + PluginServices.getText(this, "Process_canceled") + ": " + PluginServices.getText(this, "elemento_ya_existe"), PluginServices.getText(this, "nombre_no_valido"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        theMapContext.beginAtomicEvent();
        OracleSpatialDriver oracleDrv = new OracleSpatialDriver();
        oracleDrv.setData(iconn, lyr_def);
        int shptype = geoTypePanel.getSelectedGeometryType();
        oracleDrv.setShapeType(shptype);
        String lyr_name = geoTypePanel.getLayerName();
        FLyrVect lyr = (FLyrVect) LayerFactory.createDBLayer(oracleDrv, lyr_name, vp.getProjection());
        lyr.setVisible(true);
        theMapContext.getLayers().addLayer(lyr);
        theMapContext.endAtomicEvent();
    }

    private boolean validFieldName(FieldDescription f_desc) {
        if ((f_desc.getFieldName().compareToIgnoreCase(OracleSpatialDriver.DEFAULT_ID_FIELD_CASE_SENSITIVE) == 0) || (f_desc.getFieldName().compareToIgnoreCase(OracleSpatialDriver.ORACLE_ID_FIELD) == 0)) {
            return false;
        }
        return true;
    }

    private FieldDescription[] getValidFields(FieldDescription[] ff) {
        ArrayList aux = new ArrayList();
        FieldDescription row_fld = new FieldDescription();
        row_fld.setFieldName(OracleSpatialDriver.ORACLE_ID_FIELD);
        row_fld.setFieldType(Types.VARCHAR);
        aux.add(row_fld);
        for (int i = 0; i < ff.length; i++) {
            if (validFieldName(ff[i])) {
                aux.add(ff[i]);
            }
        }
        FieldDescription gid_fld = new FieldDescription();
        gid_fld.setFieldName(OracleSpatialDriver.DEFAULT_ID_FIELD_CASE_SENSITIVE);
        gid_fld.setFieldType(Types.INTEGER);
        aux.add(gid_fld);
        return (FieldDescription[]) aux.toArray(new FieldDescription[0]);
    }
}
