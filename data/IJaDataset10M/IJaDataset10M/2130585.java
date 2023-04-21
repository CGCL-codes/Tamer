package cn.myapps.core.report.crossreport.runtime.dataset;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.sql.RowSet;
import cn.myapps.util.DateUtil;

/**
 * The smallest unit data, include the the data type & data.
 * 
 */
public class ConsoleData {

    /**
	 * The data type
	 */
    private ConsoleMetaData metaData;

    /**
	 * The number value.
	 */
    private long integerValue;

    /**
	 * The number value.
	 */
    private double numericValue;

    /**
	 * The String value.
	 */
    private String stringValue;

    /**
	 * The date value.
	 */
    private Date dateValue;

    /**
	 * The boolean value.
	 */
    private boolean booleanValue;

    /**
	 * Constructor with the Jdbc rowset and Meta Data, it will convert the
	 * current row set data to console data.
	 * 
	 * @param rowSet
	 *            The Jdbc row set
	 * @param metaData
	 *            The console meta data .
	 */
    public ConsoleData(ResultSet rowSet, ConsoleMetaData metaData) throws Exception {
        String fieldName = metaData.getColumnName();
        this.metaData = metaData;
        if (metaData.getDataType().getValue() == ConsoleDataType.Boolean.getValue()) booleanValue = rowSet.getBoolean(fieldName); else if (metaData.getDataType().getValue() == ConsoleDataType.String.getValue()) stringValue = rowSet.getString(fieldName); else if (metaData.getDataType().getValue() == ConsoleDataType.Numberic.getValue()) numericValue = rowSet.getDouble(fieldName); else if (metaData.getDataType().getValue() == ConsoleDataType.Integer.getValue()) integerValue = rowSet.getLong(fieldName); else if (metaData.getDataType().getValue() == ConsoleDataType.Date.getValue()) dateValue = rowSet.getDate(fieldName); else if (metaData.getDataType().getValue() == ConsoleDataType.DateTime.getValue()) {
            Object temp = rowSet.getObject(fieldName);
            if (temp instanceof oracle.sql.TIMESTAMP) {
                dateValue = new Date(((oracle.sql.TIMESTAMP) rowSet.getObject(fieldName)).timestampValue().getTime());
            } else dateValue = rowSet.getTimestamp(fieldName);
        } else if (metaData.getDataType().getValue() == ConsoleDataType.Other.getValue()) {
            System.out.println("metaData.getColumnName()---->" + metaData.getColumnName());
            throw new Exception("Unsupport Data Type");
        }
    }

    /**
	 * Constructor with the console meta data and value.
	 * 
	 * @param metaData
	 *            The console meta data.
	 * @param stringValue
	 *            The string value;
	 * @throws Exception
	 */
    public ConsoleData(ConsoleMetaData metaData, String stringValue) throws Exception {
        this.metaData = metaData;
        if (metaData.getDataType() == ConsoleDataType.String) this.stringValue = stringValue;
        if (metaData.getDataType() == ConsoleDataType.Numberic) this.numericValue = new Double(stringValue).doubleValue();
        if (metaData.getDataType() == ConsoleDataType.Integer) this.integerValue = new Integer(stringValue).intValue();
        if (metaData.getDataType() == ConsoleDataType.Date) {
            SimpleDateFormat format = new SimpleDateFormat(SystemParameters.SRV_DATE_FORMAT);
            this.dateValue = (stringValue != null) ? format.parse(stringValue) : null;
        }
        if (metaData.getDataType() == ConsoleDataType.DateTime) {
            SimpleDateFormat format = new SimpleDateFormat(SystemParameters.SRV_DATETIME_FORMAT);
            this.dateValue = (stringValue != null) ? format.parse(stringValue) : null;
        }
        if (metaData.getDataType() == ConsoleDataType.Other) throw new Exception("Unsupport Data Type");
    }

    /**
	 * @return the metaData
	 */
    public ConsoleMetaData getMetaData() {
        return metaData;
    }

    /**
	 * @param metaData
	 *            the metaData to set
	 */
    public void setMetaData(ConsoleMetaData metaData) {
        this.metaData = metaData;
    }

    /**
	 * @return the numberValue
	 */
    public double getNumericValue() {
        if (metaData.getDataType() == ConsoleDataType.Numberic) return numericValue;
        if (metaData.getDataType() == ConsoleDataType.Integer) return integerValue;
        return numericValue;
    }

    /**
	 * @param numberValue
	 *            the numberValue to set
	 */
    public void setNumericValue(double numberValue) {
        this.numericValue = numberValue;
    }

    /**
	 * @return the stringValue
	 */
    public String getStringValue() {
        if (metaData != null) {
            if (metaData.getDataType() == ConsoleDataType.String) return (stringValue != null) ? stringValue : "";
            if (metaData.getDataType() == ConsoleDataType.Numberic) return String.valueOf(numericValue);
            if (metaData.getDataType() == ConsoleDataType.Integer) return String.valueOf(integerValue);
            if (metaData.getDataType() == ConsoleDataType.Date) {
                SimpleDateFormat format = new SimpleDateFormat(SystemParameters.SRV_DATE_FORMAT);
                return (dateValue != null) ? format.format(dateValue) : "";
            }
            if (metaData.getDataType() == ConsoleDataType.DateTime) {
                SimpleDateFormat format = new SimpleDateFormat(SystemParameters.SRV_DATETIME_FORMAT);
                return (dateValue != null) ? format.format(dateValue) : "";
            }
        }
        return (stringValue != null) ? stringValue : "";
    }

    /**
	 * @param stringValue
	 *            the stringValue to set
	 */
    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    /**
	 * @return the dateValue
	 */
    public Date getDateValue() {
        return dateValue;
    }

    /**
	 * @param dateValue
	 *            the dateValue to set
	 */
    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    /**
	 * @return the dateValue
	 */
    public Date getDateTimeValue() {
        return dateValue;
    }

    /**
	 * @param dateValue
	 *            the dateValue to set
	 */
    public void setDateTimeValue(Date dateTimeValue) {
        this.dateValue = dateTimeValue;
    }

    /**
	 * @return the integerValue
	 */
    public long getIntegerValue() {
        if (metaData.getDataType() == ConsoleDataType.Numberic) return (long) numericValue;
        if (metaData.getDataType() == ConsoleDataType.Integer) return integerValue;
        return integerValue;
    }

    /**
	 * @param integerValue
	 *            the integerValue to set
	 */
    public void setIntegerValue(long integerValue) {
        this.integerValue = integerValue;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((metaData == null) ? 0 : metaData.hashCode());
        if (metaData != null && metaData.getDataType() == ConsoleDataType.String) result = prime * result + ((stringValue == null) ? 0 : stringValue.hashCode());
        if (metaData != null && metaData.getDataType() == ConsoleDataType.Integer) result = prime * result + (int) (integerValue ^ (integerValue >>> 32));
        if (metaData != null && metaData.getDataType() == ConsoleDataType.Date) result = prime * result + ((dateValue == null) ? 0 : dateValue.hashCode());
        if (metaData != null && metaData.getDataType() == ConsoleDataType.Numberic) {
            long temp;
            temp = Double.doubleToLongBits(numericValue);
            result = prime * result + (int) (temp ^ (temp >>> 32));
        }
        return result;
    }

    public boolean equals(Object otherValue) {
        if (!(otherValue instanceof ConsoleData)) return false;
        if (!metaData.equals(((ConsoleData) otherValue).getMetaData())) return false;
        if (otherValue != null) {
            ConsoleData other = (ConsoleData) otherValue;
            if (metaData.getDataType().getValue() == ConsoleDataType.String.getValue() && getStringValue().equals(other.getStringValue())) return true;
            if (metaData.getDataType().getValue() == ConsoleDataType.Integer.getValue() && integerValue == other.getIntegerValue()) return true;
            if (metaData.getDataType().getValue() == ConsoleDataType.Numberic.getValue() && numericValue == other.getNumericValue()) return true;
            if (metaData.getDataType().getValue() == ConsoleDataType.Date.getValue() && dateValue.equals(other.getDateValue())) return true;
            if (metaData.getDataType().getValue() == ConsoleDataType.DateTime.getValue() && dateValue.equals(other.getDateValue())) return true;
        }
        return false;
    }

    /**
	 * 
	 */
    public int compareTo(Object otherValue) {
        if (!(otherValue instanceof ConsoleData)) return -1;
        if (otherValue != null) {
            ConsoleData other = (ConsoleData) otherValue;
            if (metaData.getDataType() == ConsoleDataType.String) return getStringValue().compareTo(other.getStringValue());
            if (metaData.getDataType() == ConsoleDataType.Integer) if (integerValue > other.getIntegerValue()) return 1; else if (integerValue < other.getIntegerValue()) return -1; else return 0;
            if (metaData.getDataType() == ConsoleDataType.Numberic) if (numericValue > other.getNumericValue()) return 1; else if (numericValue < other.getNumericValue()) return -1; else return 0;
            if (metaData.getDataType() == ConsoleDataType.Date) return dateValue.compareTo(other.getDateValue());
            if (metaData.getDataType() == ConsoleDataType.DateTime) return dateValue.compareTo(other.getDateValue());
        }
        return -1;
    }

    public String toString() {
        if (metaData.getDataType() == ConsoleDataType.String) return getStringValue();
        if (metaData.getDataType() == ConsoleDataType.Integer) return String.valueOf(integerValue);
        if (metaData.getDataType() == ConsoleDataType.Numberic) return String.valueOf(numericValue);
        if (metaData.getDataType() == ConsoleDataType.Date) return dateValue.toString();
        if (metaData.getDataType() == ConsoleDataType.DateTime) return dateValue.toString();
        return "";
    }
}
