package com.iver.cit.gvsig.fmap.drivers.jdbc.mysql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.cit.gvsig.fmap.core.DefaultFeature;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.drivers.DBLayerDefinition;
import com.iver.cit.gvsig.fmap.drivers.IFeatureIterator;
import com.iver.cit.gvsig.fmap.drivers.WKBParser2;

/**
 * Iterator over the features of a MySQL driver.
 * 
 *
 * 
 * @author FJP
 */
public class MySqlFeatureIterator implements IFeatureIterator {

    private WKBParser2 parser = new WKBParser2();

    ResultSet rs;

    String strAux;

    IGeometry geom;

    int numColumns;

    int idFieldID = -1;

    int[] relIds;

    private DBLayerDefinition lyrDef;

    private ResultSetMetaData metaData = null;

    Value[] regAtt;

    /**
     * @throws SQLException
     *
     */
    public MySqlFeatureIterator(ResultSet rs) {
        this.rs = rs;
        try {
            numColumns = rs.getMetaData().getColumnCount();
            regAtt = new Value[numColumns - 1];
            metaData = rs.getMetaData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasNext() throws ReadDriverException {
        try {
            if (rs.next()) return true;
            closeIterator();
            return false;
        } catch (SQLException e) {
            throw new ReadDriverException("MySQL Driver", e);
        }
    }

    public IFeature next() throws ReadDriverException {
        byte[] data;
        try {
            data = rs.getBytes(1);
            geom = parser.parse(data);
            for (int fieldId = 2; fieldId <= numColumns; fieldId++) {
                Value val = null;
                if (metaData.getColumnType(fieldId) == Types.VARCHAR) {
                    String strAux = rs.getString(fieldId);
                    if (strAux == null) strAux = "";
                    val = ValueFactory.createValue(strAux);
                } else if (metaData.getColumnType(fieldId) == Types.FLOAT) val = ValueFactory.createValue(rs.getFloat(fieldId)); else if (metaData.getColumnType(fieldId) == Types.DOUBLE) val = ValueFactory.createValue(rs.getDouble(fieldId)); else if (metaData.getColumnType(fieldId) == Types.INTEGER) val = ValueFactory.createValue(rs.getInt(fieldId)); else if (metaData.getColumnType(fieldId) == Types.BIGINT) val = ValueFactory.createValue(rs.getLong(fieldId)); else if (metaData.getColumnType(fieldId) == Types.BIT) val = ValueFactory.createValue(rs.getBoolean(fieldId)); else if (metaData.getColumnType(fieldId) == Types.DATE) val = ValueFactory.createValue(rs.getDate(fieldId));
                regAtt[relIds[fieldId - 2]] = val;
            }
            IFeature feat = null;
            if (idFieldID != -1) {
                int fieldId = lyrDef.getIdFieldID();
                Value idValue = regAtt[fieldId];
                String theID = "";
                if (idValue != null) theID = idValue.toString();
                feat = new DefaultFeature(geom, regAtt, theID);
            } else {
                throw new ReadDriverException("MySQL Driver", null);
            }
            return feat;
        } catch (SQLException e) {
            throw new ReadDriverException("MySQL Driver", e);
        }
    }

    public void closeIterator() throws ReadDriverException {
        try {
            rs.close();
        } catch (SQLException e) {
            throw new ReadDriverException("MySQL Driver", e);
        }
    }

    public void setLyrDef(DBLayerDefinition lyrDef) {
        this.lyrDef = lyrDef;
        regAtt = new Value[lyrDef.getFieldNames().length];
        relIds = new int[numColumns - 1];
        try {
            for (int i = 2; i <= numColumns; i++) {
                int idRel = lyrDef.getFieldIdByName(metaData.getColumnName(i));
                if (idRel == -1) {
                    throw new RuntimeException("No se ha encontrado el nombre de campo " + metaData.getColumnName(i));
                }
                relIds[i - 2] = idRel;
                if (lyrDef.getFieldID().equals(metaData.getColumnName(i))) {
                    idFieldID = i;
                    break;
                }
            }
        } catch (SQLException e) {
        }
    }
}
