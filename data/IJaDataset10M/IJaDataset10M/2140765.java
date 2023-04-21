package com.ynhenc.droute.map.link;

import java.sql.SQLException;
import oracle.sql.ARRAY;
import oracle.sql.Datum;
import oracle.sql.NUMBER;
import oracle.sql.STRUCT;
import com.ynhenc.comm.*;

public class SdoGeometry extends GisComLib {

    public double[] getCoordinates(STRUCT struct) throws Exception {
        Datum data[] = struct.getOracleAttributes();
        if (false) {
            int gType = this.asInteger(data[0], 0);
            double point[] = this.asDoubleArray((STRUCT) data[2], Double.NaN);
            int elemInfo[] = this.asIntArray((ARRAY) data[3], 0);
        }
        double coords[] = this.asDoubleArray((ARRAY) data[4], Double.NaN);
        return coords;
    }

    /** Presents datum as an int */
    private int asInteger(Datum datum, final int DEFAULT) throws SQLException {
        if (datum == null) {
            return DEFAULT;
        }
        return ((NUMBER) datum).intValue();
    }

    /** Presents datum as a double */
    private double asDouble(Datum datum, final double DEFAULT) {
        if (datum == null) {
            return DEFAULT;
        }
        return ((NUMBER) datum).doubleValue();
    }

    /** Presents struct as a double[] */
    private double[] asDoubleArray(STRUCT struct, final double DEFAULT) throws SQLException {
        if (struct == null) {
            return null;
        }
        return this.asDoubleArray(struct.getOracleAttributes(), DEFAULT);
    }

    /** Presents array as a double[] */
    private double[] asDoubleArray(ARRAY array, final double DEFAULT) throws SQLException {
        if (array == null) {
            return null;
        }
        if (DEFAULT == 0) {
            return array.getDoubleArray();
        }
        return this.asDoubleArray(array.getOracleArray(), DEFAULT);
    }

    /** Presents Datum[] as a double[] */
    private double[] asDoubleArray(Datum data[], final double DEFAULT) {
        if (data == null) {
            return null;
        }
        double array[] = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            array[i] = this.asDouble(data[i], DEFAULT);
        }
        return array;
    }

    private int[] asIntArray(ARRAY array, int DEFAULT) throws SQLException {
        if (array == null) {
            return null;
        }
        if (DEFAULT == 0) {
            return array.getIntArray();
        }
        return this.asIntArray(array.getOracleArray(), DEFAULT);
    }

    /** Presents Datum[] as a int[] */
    private int[] asIntArray(Datum data[], final int DEFAULT) throws SQLException {
        if (data == null) {
            return null;
        }
        int array[] = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            array[i] = this.asInteger(data[i], DEFAULT);
        }
        return array;
    }
}
