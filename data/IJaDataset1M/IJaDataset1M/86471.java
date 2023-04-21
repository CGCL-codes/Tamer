package com.globalretailtech.data;

import java.sql.*;
import java.util.Vector;
import com.globalretailtech.util.Application;
import org.apache.log4j.Logger;

/**
 * Currency is used to manage all currencies (money). Includes conversion
 * rates in reference to the local currency and flags to indicate whether
 * it is the base currency and the local currency.
 *
 * @author  Quentin Olson
 */
public class Currency extends DBRecord {

    static Logger logger = Logger.getLogger(Currency.class);

    private static String table;

    private static String[] columns;

    private static int[] col_types;

    static {
        table = "currency";
        columns = new String[11];
        columns[0] = "currency_id";
        columns[1] = "config_no";
        columns[2] = "currency_code_id";
        columns[3] = "conversion_rate";
        columns[4] = "smallest_denom";
        columns[5] = "decimal_digits";
        columns[6] = "locale";
        columns[7] = "is_base";
        columns[8] = "is_local";
        columns[9] = "last_update";
        columns[10] = "currency_class";
        col_types = new int[11];
        col_types[0] = DBRecord.INT;
        col_types[1] = DBRecord.INT;
        col_types[2] = DBRecord.INT;
        col_types[3] = DBRecord.DOUBLE;
        col_types[4] = DBRecord.INT;
        col_types[5] = DBRecord.INT;
        col_types[6] = DBRecord.STRING;
        col_types[7] = DBRecord.BOOLEAN;
        col_types[8] = DBRecord.BOOLEAN;
        col_types[9] = DBRecord.DATE;
        col_types[10] = DBRecord.STRING;
    }

    private int currencyid;

    private int configno;

    private int currencycodeid;

    private double conversionrate;

    private int smallestdenom;

    private int decimaldigits;

    private String locale;

    private boolean isbase;

    private boolean islocal;

    private Date lastupdate;

    private String currencyclass;

    public Currency() {
    }

    public int currencyID() {
        return currencyid;
    }

    public int configNo() {
        return configno;
    }

    public int currencyCodeID() {
        return currencycodeid;
    }

    public double conversionRate() {
        return conversionrate;
    }

    public int smallestDenom() {
        return smallestdenom;
    }

    public int decimalDigits() {
        return decimaldigits;
    }

    public String locale() {
        return locale;
    }

    public boolean isBase() {
        return isbase;
    }

    public boolean isLocal() {
        return islocal;
    }

    public Date lastUpdate() {
        return lastupdate;
    }

    public String currencyClass() {
        return currencyclass;
    }

    public void setCurrencyID(int value) {
        currencyid = value;
    }

    public void setConfigNo(int value) {
        configno = value;
    }

    public void setCurrencyCodeID(int value) {
        currencycodeid = value;
    }

    public void setConversionRate(double value) {
        conversionrate = value;
    }

    public void setSmallestDenom(int value) {
        smallestdenom = value;
    }

    public void setDecimalDigits(int value) {
        decimaldigits = value;
    }

    public void setLocale(String value) {
        locale = value;
    }

    public void setIsBase(boolean value) {
        isbase = value;
    }

    public void setIsLocal(boolean value) {
        islocal = value;
    }

    public void setLastUpdate(Date value) {
        lastupdate = value;
    }

    public void setCurrencyClass(String value) {
        currencyclass = value;
    }

    public static String getByID(int id) {
        StringBuffer s = new StringBuffer("select * from ");
        s.append(table);
        s.append(" where ");
        s.append(columns[0]);
        s.append(" = ");
        s.append(Integer.toString(id));
        return new String(s.toString());
    }

    public DBRecord copy() {
        Currency b = new Currency();
        return b;
    }

    public void populate(ResultSet rset) {
        try {
            setCurrencyID(rset.getInt("currency_id"));
            setConfigNo(rset.getInt("config_no"));
            setCurrencyCodeID(rset.getInt("currency_code_id"));
            setConversionRate(rset.getDouble("conversion_rate"));
            setSmallestDenom(rset.getInt("smallest_denom"));
            setDecimalDigits(rset.getInt("decimal_digits"));
            setLocale(rset.getString("locale"));
            setIsBase(rset.getInt("is_base") > 0);
            setIsLocal(rset.getInt("is_local") > 0);
            setLastUpdate(rset.getDate("last_update"));
            setCurrencyClass(rset.getString("currency_class"));
        } catch (java.sql.SQLException e) {
            Application.dbConnection().setException(e);
        }
    }

    public boolean save() {
        return true;
    }

    public boolean update() {
        return true;
    }

    public String toXML() {
        return super.toXML(table, columnObjects(), columns, col_types);
    }

    private CurrencyCode currenycode;

    public CurrencyCode currencyCode() {
        return currenycode;
    }

    public void setCurrencyCode(CurrencyCode value) {
        currenycode = value;
    }

    public void relations() {
        String fetchSpec = CurrencyCode.getByID(currencyCodeID());
        Vector v = Application.dbConnection().fetch(new CurrencyCode(), fetchSpec);
        if (v.size() > 0) {
            setCurrencyCode((CurrencyCode) v.elementAt(0));
        } else {
            logger.warn("Currency code not found for " + currencyID());
        }
    }

    public Vector columnObjects() {
        Vector objs = new Vector();
        objs.addElement(new Integer(currencyID()));
        objs.addElement(new Integer(configNo()));
        objs.addElement(new Integer(currencyCodeID()));
        objs.addElement(new Double(conversionRate()));
        objs.addElement(new Integer(smallestDenom()));
        objs.addElement(new Integer(decimalDigits()));
        objs.addElement(new String(locale()));
        objs.addElement(new Boolean(isBase()));
        objs.addElement(new Boolean(isLocal()));
        objs.addElement(lastUpdate());
        objs.addElement(new String(currencyClass()));
        return objs;
    }
}
