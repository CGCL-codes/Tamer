package org.ikross.h2.adapters.gml;

import java.io.File;
import java.sql.SQLException;
import org.gvsig.h2.gml.GMLFeature;
import org.gvsig.h2.gml.GmlCursor;
import org.gvsig.remoteClient.gml.exceptions.GMLException;
import org.h2.engine.Session;
import org.h2.index.Cursor;
import org.h2.result.Row;
import org.h2.result.SearchRow;
import org.h2.table.Column;
import org.h2.table.TableExtern;
import org.h2.table.TableExternAdapter;
import org.h2.value.DataType;
import org.h2.value.Value;

public class TableGML extends TableExternAdapter {

    private static final String TYPE_NAME = "gml";

    private Column[] columns = null;

    private GmlCursor gmlCursor = null;

    public TableGML() {
        this.setTypeName(TYPE_NAME);
    }

    public static void registerType() {
        TableExtern.registerType(TableGML.TYPE_NAME, TableGML.class);
    }

    private boolean initializeGMLCursor() throws SQLException {
        if (this.gmlCursor == null) {
            this.gmlCursor = this.createNewGmlCursor();
            return true;
        } else {
            return false;
        }
    }

    private GmlCursor createNewGmlCursor() throws SQLException {
        GmlCursor gmlCursor = null;
        String filename = String.valueOf(this.params.get(S_FILE));
        File file = new File(filename);
        if (!file.exists()) {
            throw new SQLException("GML file not found");
        }
        try {
            gmlCursor = new GmlCursor(file);
            return gmlCursor;
        } catch (GMLException e) {
            throw new SQLException("GML driver creation exception");
        }
    }

    private boolean destroyGMLCursor() {
        this.gmlCursor = null;
        return true;
    }

    public Column[] getColumns() throws SQLException {
        if (this.columns == null) {
            this.initializeGMLCursor();
            GMLFeature firstFeature = null;
            if (gmlCursor.hasNext()) {
                firstFeature = (GMLFeature) gmlCursor.next();
            } else {
                throw new SQLException("All the GML's has to have one geometry at least");
            }
            Object fieldNames[] = firstFeature.getAttributeNames();
            int fieldTypes[] = firstFeature.getAttributeTypes();
            this.columns = new Column[fieldNames.length];
            int fieldType;
            long decimalCount;
            int fieldScale;
            for (int i = 0; i < this.columns.length; i++) {
                fieldType = DataType.convertSQLTypeToValueType(fieldTypes[i]);
                DataType dataType = DataType.getDataType(fieldType);
                decimalCount = dataType.defaultPrecision;
                fieldScale = dataType.defaultScale;
                this.columns[i] = new Column(((String) fieldNames[i]).toUpperCase(), fieldType, decimalCount, fieldScale);
            }
            return this.columns;
        } else {
            return this.columns;
        }
    }

    public void setColumns(Column[] columns) {
        this.columns = columns;
    }

    public void checkParameters() throws SQLException {
        if (!this.params.containsKey(S_FILE)) {
            throw new SQLException("Parameter \"" + S_FILE + "\" not found");
        }
    }

    public String getParam(String key) throws SQLException {
        String param = String.valueOf(this.params.get(key));
        if (param == null) throw new SQLException("The key" + key + " does not represent any parameter");
        return param;
    }

    public Cursor getData(Session session, SearchRow first, SearchRow last) throws SQLException {
        GmlCursor gmlCursor = this.createNewGmlCursor();
        return new CursorGML(this.tableExtern, gmlCursor);
    }

    public boolean closeSource() {
        this.destroyGMLCursor();
        return true;
    }

    public void drop() {
    }

    public void insertData(Session session, Row row) throws SQLException {
    }

    public void removeData(Session session, Row row) throws SQLException {
    }

    public boolean canGetRowCount() {
        return false;
    }

    public int getRowCount() {
        return 0;
    }

    public boolean canGetRowPos() {
        return false;
    }

    public int getRowPos() {
        return 0;
    }

    public boolean needRebuild() {
        return false;
    }

    public boolean canGetFirstOrLast() {
        return false;
    }

    public Value findFirstOrLast(Session session, boolean first) {
        return null;
    }
}
