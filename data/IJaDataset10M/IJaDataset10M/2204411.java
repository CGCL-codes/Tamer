package com.versant.core.jdbc.logging;

import com.versant.core.jdbc.metadata.JdbcTypes;
import java.sql.Statement;
import java.util.List;
import java.util.Arrays;
import java.io.Serializable;

/**
 * A statement event with parameters.
 * @keep-all
 */
public class JdbcStatementParamsEvent extends JdbcStatementEvent {

    /**
     * A row of parameters.
     */
    public static class Row implements Serializable {

        public Object[] values;

        public int[] sqlTypes;

        public int size;

        public Row(int sz) {
            values = new Object[sz];
            sqlTypes = new int[sz];
        }

        public void set(int index, Object value, int sqlType) {
            if (--index >= values.length) {
                int len = values.length;
                Object[] o = new Object[len * 2];
                System.arraycopy(values, 0, o, 0, len);
                int[] t = new int[len * 2];
                System.arraycopy(sqlTypes, 0, t, 0, len);
                values = o;
                sqlTypes = t;
            }
            if (index >= size) size = index + 1;
            values[index] = value;
            sqlTypes[index] = sqlType;
        }

        public void trim() {
            if (size == values.length) return;
            Object[] o = new Object[size];
            System.arraycopy(values, 0, o, 0, size);
            int[] t = new int[size];
            System.arraycopy(sqlTypes, 0, t, 0, size);
            values = o;
            sqlTypes = t;
        }

        public String toString() {
            return getStringValue();
        }

        public List getValueList() {
            return Arrays.asList(values);
        }

        public String getStringValue() {
            StringBuffer s = new StringBuffer();
            s.append('[');
            for (int i = 0; i < size; i++) {
                if (i > 0) s.append(", ");
                Object v = values[i];
                try {
                    s.append(v);
                } catch (Exception e) {
                    s.append("<toString failed: " + e + ">");
                }
                if (v == null) {
                    s.append('(');
                    s.append(JdbcTypes.toString(sqlTypes[i]));
                    s.append(')');
                }
            }
            s.append(']');
            return s.toString();
        }

        public void setStringValue(String s) {
        }
    }

    private Row[] params;

    public JdbcStatementParamsEvent(long txId, Statement stat, String descr, int type) {
        super(txId, stat, descr, type);
    }

    public Row[] getParams() {
        return params;
    }

    public List getParamsList() {
        return Arrays.asList(params);
    }

    public void setParams(Row[] params) {
        this.params = params;
    }

    public String toString() {
        if (params.length == 1) {
            return super.toString() + " " + params[0];
        } else {
            return super.toString() + " " + params.length + " param rows";
        }
    }
}
