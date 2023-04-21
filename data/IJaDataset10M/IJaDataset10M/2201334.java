package org.gdbms.engine.data;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.gdbms.engine.data.DataSource;
import org.gdbms.engine.data.DataSourceFactory;
import org.gdbms.engine.data.driver.AlphanumericObjectDriver;
import org.gdbms.engine.data.driver.DriverException;
import org.gdbms.engine.data.edition.Field;
import org.gdbms.engine.data.metadata.DefaultDriverMetadata;
import org.gdbms.engine.data.metadata.DefaultMetadata;
import org.gdbms.engine.data.metadata.DriverMetadata;
import org.gdbms.engine.data.metadata.Metadata;
import org.gdbms.engine.spatial.PTTypes;
import org.gdbms.engine.values.Value;
import org.gdbms.engine.values.ValueFactory;

/**
 * 
 */
public class FakeObjectDriver implements AlphanumericObjectDriver {

    private Value[][] values = new Value[3][6];

    private String[] names = new String[] { "id", "nombre", "apellido", "fecha", "tiempo", "marcatiempo" };

    private int[] types = new int[] { Value.INT, Value.STRING, Value.STRING, Value.DATE, Value.TIME, Value.TIMESTAMP };

    public FakeObjectDriver() throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        values[0][0] = ValueFactory.createValue(0);
        values[0][1] = ValueFactory.createValue("fernando");
        values[0][2] = ValueFactory.createValue("gonzalez");
        values[0][3] = ValueFactory.createValue(df.parse("1980-9-5"));
        values[0][4] = ValueFactory.createValue(Time.valueOf("10:30:00"));
        values[0][5] = ValueFactory.createValue(Timestamp.valueOf("1980-9-5 10:30:00.666666666"));
        values[1][0] = ValueFactory.createValue(1);
        values[1][1] = ValueFactory.createValue("huracan");
        values[1][2] = ValueFactory.createValue("gonsales");
        values[1][3] = ValueFactory.createValue(df.parse("1980-9-5"));
        values[1][4] = ValueFactory.createValue(Time.valueOf("10:30:00"));
        values[1][5] = ValueFactory.createValue(Timestamp.valueOf("1980-9-5 10:30:00.666666666"));
        values[2][0] = ValueFactory.createValue(2);
        values[2][1] = ValueFactory.createValue("fernan");
        values[2][2] = null;
        values[2][3] = ValueFactory.createValue(df.parse("1980-9-5"));
        values[2][4] = ValueFactory.createValue(Time.valueOf("10:30:00"));
        values[2][5] = ValueFactory.createValue(Timestamp.valueOf("1980-9-5 10:30:00.666666666"));
    }

    /**
     * @see org.gdbms.engine.data.driver.ObjectDriver#write(org.gdbms.engine.data.edition.DataWare)
     */
    public void write(DataSource dataWare) throws DriverException {
        names = new String[dataWare.getDataSourceMetadata().getFieldCount()];
        types = new int[names.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = dataWare.getDataSourceMetadata().getFieldName(i);
            types[i] = dataWare.getDataSourceMetadata().getFieldType(i);
        }
        Value[][] newValues = new Value[(int) dataWare.getRowCount()][dataWare.getDataSourceMetadata().getFieldCount()];
        for (int i = 0; i < dataWare.getRowCount(); i++) {
            for (int j = 0; j < dataWare.getDataSourceMetadata().getFieldCount(); j++) {
                newValues[i][j] = dataWare.getFieldValue(i, j);
            }
        }
        values = newValues;
    }

    /**
     * @see org.gdbms.engine.data.driver.ReadAccess#getFieldValue(long, int)
     */
    public Value getFieldValue(long rowIndex, int fieldId) throws DriverException {
        return values[(int) rowIndex][fieldId];
    }

    /**
     * @see org.gdbms.engine.data.driver.ReadAccess#getFieldCount()
     */
    public int getFieldCount() throws DriverException {
        return names.length;
    }

    /**
     * @see org.gdbms.engine.data.driver.ReadAccess#getFieldName(int)
     */
    public String getFieldName(int fieldId) throws DriverException {
        return names[fieldId];
    }

    /**
     * @see org.gdbms.engine.data.driver.ReadAccess#getRowCount()
     */
    public long getRowCount() throws DriverException {
        return values.length;
    }

    /**
     * @see org.gdbms.engine.data.driver.ReadAccess#getFieldType(int)
     */
    public int getFieldType(int i) throws DriverException {
        switch(i) {
            case 0:
                return Value.INT;
            case 1:
            case 2:
                return Value.STRING;
            case 3:
                return Value.DATE;
            case 4:
                return Value.TIME;
            case 5:
                return Value.TIMESTAMP;
            case 6:
                return Value.LONG;
        }
        throw new RuntimeException();
    }

    public void setDataSourceFactory(DataSourceFactory dsf) {
    }

    public String getName() {
        return null;
    }

    public Metadata getMetadata() throws DriverException {
        return new DefaultMetadata(types, names, null, null);
    }

    /**
     * @see org.gdbms.engine.data.driver.GDBMSDriver#getDriverMetadata()
     */
    public DriverMetadata getDriverMetadata() throws DriverException {
        DefaultDriverMetadata ret = new DefaultDriverMetadata();
        for (int i = 0; i < getFieldCount(); i++) {
            int type = getFieldType(i);
            ret.addField(getFieldName(i), PTTypes.typesDescription.get(type));
        }
        return ret;
    }

    public int getType(String driverType) {
        if ("STRING".equals(driverType)) {
            return Value.STRING;
        } else if ("LONG".equals(driverType)) {
            return Value.LONG;
        } else if ("BOOLEAN".equals(driverType)) {
            return Value.BOOLEAN;
        } else if ("DATE".equals(driverType)) {
            return Value.DATE;
        } else if ("DOUBLE".equals(driverType)) {
            return Value.DOUBLE;
        } else if ("INT".equals(driverType)) {
            return Value.INT;
        } else if ("FLOAT".equals(driverType)) {
            return Value.FLOAT;
        } else if ("SHORT".equals(driverType)) {
            return Value.SHORT;
        } else if ("BYTE".equals(driverType)) {
            return Value.BYTE;
        } else if ("BINARY".equals(driverType)) {
            return Value.BINARY;
        } else if ("TIMESTAMP".equals(driverType)) {
            return Value.TIMESTAMP;
        } else if ("TIME".equals(driverType)) {
            return Value.TIME;
        }
        throw new RuntimeException();
    }

    public String check(Field field, Value value) throws DriverException {
        return null;
    }

    public boolean isReadOnly(int i) {
        return false;
    }

    public String[] getPrimaryKeys() {
        return new String[0];
    }

    public String[] getAvailableTypes() throws DriverException {
        return null;
    }

    public String[] getParameters(String driverType) throws DriverException {
        return null;
    }

    public boolean isValidParameter(String driverType, String paramName, String paramValue) {
        return false;
    }

    public void start() throws DriverException {
    }

    public void stop() throws DriverException {
    }
}
