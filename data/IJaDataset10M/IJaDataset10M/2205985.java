package net.cblicher.mvxconnectorj.b.mm.dao;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import net.cblicher.mvxconnectorj.b.DataAccessObject;
import net.cblicher.mvxconnectorj.b.DataAccessObjectList;
import net.cblicher.mvxconnectorj.b.MoreThanOneRecordWasDeletedException;
import net.cblicher.mvxconnectorj.b.MoreThanOneRecordWasUpdatedException;
import net.cblicher.mvxconnectorj.b.MovexSession;
import net.cblicher.mvxconnectorj.b.NoRecordWasDeletedException;
import net.cblicher.mvxconnectorj.b.NoRecordWasInsertedException;
import net.cblicher.mvxconnectorj.b.NoRecordWasUpdatedException;
import net.cblicher.mvxconnectorj.b.RecordNotFoundException;
import net.cblicher.mvxconnectorj.b.TooManyRecordsException;
import com.ibm.as400.access.AS400SecurityException;

/**
 * Data object class for the Movex table <code>Mitmah</code>.
 * This class is auto-generated and may be re-generated.
 *
 * @version 1.0 2010/12/5
 * @author Christian Blicher
 */
public class DefaultMitmah extends DataAccessObject {

    /**
 * Ease-of-use constant for specifying the field prefix.
 */
    public static final String PREFIX = "HM";

    /**
 * Ease-of-use constant for specifying a CONO field string.
 */
    public static final String CONO = "HMCONO";

    /**
 * Ease-of-use constant for specifying a ITNO field string.
 */
    public static final String ITNO = "HMITNO";

    /**
 * Ease-of-use constant for specifying a STYN field string.
 */
    public static final String STYN = "HMSTYN";

    /**
 * Ease-of-use constant for specifying a SEA1 field string.
 */
    public static final String SEA1 = "HMSEA1";

    /**
 * Ease-of-use constant for specifying a SQFZ field string.
 */
    public static final String SQFZ = "HMSQFZ";

    /**
 * Ease-of-use constant for specifying a FTIZ field string.
 */
    public static final String FTIZ = "HMFTIZ";

    /**
 * Ease-of-use constant for specifying a SQNZ field string.
 */
    public static final String SQNZ = "HMSQNZ";

    /**
 * Ease-of-use constant for specifying a OPTZ field string.
 */
    public static final String OPTZ = "HMOPTZ";

    /**
 * Ease-of-use constant for specifying a TZ15 field string.
 */
    public static final String TZ15 = "HMTZ15";

    /**
 * Ease-of-use constant for specifying a SQFX field string.
 */
    public static final String SQFX = "HMSQFX";

    /**
 * Ease-of-use constant for specifying a FTIX field string.
 */
    public static final String FTIX = "HMFTIX";

    /**
 * Ease-of-use constant for specifying a SQNX field string.
 */
    public static final String SQNX = "HMSQNX";

    /**
 * Ease-of-use constant for specifying a OPTX field string.
 */
    public static final String OPTX = "HMOPTX";

    /**
 * Ease-of-use constant for specifying a TX15 field string.
 */
    public static final String TX15 = "HMTX15";

    /**
 * Ease-of-use constant for specifying a SQFY field string.
 */
    public static final String SQFY = "HMSQFY";

    /**
 * Ease-of-use constant for specifying a FTIY field string.
 */
    public static final String FTIY = "HMFTIY";

    /**
 * Ease-of-use constant for specifying a SQNY field string.
 */
    public static final String SQNY = "HMSQNY";

    /**
 * Ease-of-use constant for specifying a OPTY field string.
 */
    public static final String OPTY = "HMOPTY";

    /**
 * Ease-of-use constant for specifying a TY15 field string.
 */
    public static final String TY15 = "HMTY15";

    /**
 * Ease-of-use constant for specifying a RGDT field string.
 */
    public static final String RGDT = "HMRGDT";

    /**
 * Ease-of-use constant for specifying a RGTM field string.
 */
    public static final String RGTM = "HMRGTM";

    /**
 * Ease-of-use constant for specifying a LMDT field string.
 */
    public static final String LMDT = "HMLMDT";

    /**
 * Ease-of-use constant for specifying a CHNO field string.
 */
    public static final String CHNO = "HMCHNO";

    /**
 * Ease-of-use constant for specifying a CHID field string.
 */
    public static final String CHID = "HMCHID";

    /**
 * Defines the primary key for this data access object.
 */
    private final String[] primaryKeys = { "HMCONO", "HMITNO" };

    /**
     * Creates a new <code>Mitmah</code> class type object.
     *
     *@param movexSession
     */
    public DefaultMitmah(MovexSession movexSession) {
        setMovexSession(movexSession);
    }

    /**
     * Returns the <code>HMCONO</code> property.
     *
     * @return the HMCONO property
     */
    public BigDecimal getCono() {
        return (BigDecimal) getFieldValue("HMCONO");
    }

    /**
     * Sets the <code>HMCONO</code> property.
     *
     * @param value
     */
    public void setCono(BigDecimal value) {
        setField("HMCONO", value);
    }

    /**
     * Returns the <code>HMITNO</code> property.
     * The property is a {@link String} class type object of length 15 characters.
     *
     * @return the HMITNO property
     */
    public String getItno() {
        return (String) getFieldValue("HMITNO");
    }

    /**
     * Sets the <code>HMITNO</code> property.
     * The property is a {@link String} class type object of length 15 characters.
     * If the supplied String length is longer than 15 character(s),
     * only the first 15 character(s) are used.
     *
     * @param value
     */
    public void setItno(String value) {
        if (value.length() > 15) {
            value = value.substring(0, 15);
        }
        setField("HMITNO", value);
    }

    /**
     * Returns the <code>HMSTYN</code> property.
     * The property is a {@link String} class type object of length 15 characters.
     *
     * @return the HMSTYN property
     */
    public String getStyn() {
        return (String) getFieldValue("HMSTYN");
    }

    /**
     * Sets the <code>HMSTYN</code> property.
     * The property is a {@link String} class type object of length 15 characters.
     * If the supplied String length is longer than 15 character(s),
     * only the first 15 character(s) are used.
     *
     * @param value
     */
    public void setStyn(String value) {
        if (value.length() > 15) {
            value = value.substring(0, 15);
        }
        setField("HMSTYN", value);
    }

    /**
     * Returns the <code>HMSEA1</code> property.
     * The property is a {@link String} class type object of length 4 characters.
     *
     * @return the HMSEA1 property
     */
    public String getSea1() {
        return (String) getFieldValue("HMSEA1");
    }

    /**
     * Sets the <code>HMSEA1</code> property.
     * The property is a {@link String} class type object of length 4 characters.
     * If the supplied String length is longer than 4 character(s),
     * only the first 4 character(s) are used.
     *
     * @param value
     */
    public void setSea1(String value) {
        if (value.length() > 4) {
            value = value.substring(0, 4);
        }
        setField("HMSEA1", value);
    }

    /**
     * Returns the <code>HMSQFZ</code> property.
     *
     * @return the HMSQFZ property
     */
    public BigDecimal getSqfz() {
        return (BigDecimal) getFieldValue("HMSQFZ");
    }

    /**
     * Sets the <code>HMSQFZ</code> property.
     *
     * @param value
     */
    public void setSqfz(BigDecimal value) {
        setField("HMSQFZ", value);
    }

    /**
     * Returns the <code>HMFTIZ</code> property.
     * The property is a {@link String} class type object of length 5 characters.
     *
     * @return the HMFTIZ property
     */
    public String getFtiz() {
        return (String) getFieldValue("HMFTIZ");
    }

    /**
     * Sets the <code>HMFTIZ</code> property.
     * The property is a {@link String} class type object of length 5 characters.
     * If the supplied String length is longer than 5 character(s),
     * only the first 5 character(s) are used.
     *
     * @param value
     */
    public void setFtiz(String value) {
        if (value.length() > 5) {
            value = value.substring(0, 5);
        }
        setField("HMFTIZ", value);
    }

    /**
     * Returns the <code>HMSQNZ</code> property.
     *
     * @return the HMSQNZ property
     */
    public BigDecimal getSqnz() {
        return (BigDecimal) getFieldValue("HMSQNZ");
    }

    /**
     * Sets the <code>HMSQNZ</code> property.
     *
     * @param value
     */
    public void setSqnz(BigDecimal value) {
        setField("HMSQNZ", value);
    }

    /**
     * Returns the <code>HMOPTZ</code> property.
     * The property is a {@link String} class type object of length 15 characters.
     *
     * @return the HMOPTZ property
     */
    public String getOptz() {
        return (String) getFieldValue("HMOPTZ");
    }

    /**
     * Sets the <code>HMOPTZ</code> property.
     * The property is a {@link String} class type object of length 15 characters.
     * If the supplied String length is longer than 15 character(s),
     * only the first 15 character(s) are used.
     *
     * @param value
     */
    public void setOptz(String value) {
        if (value.length() > 15) {
            value = value.substring(0, 15);
        }
        setField("HMOPTZ", value);
    }

    /**
     * Returns the <code>HMTZ15</code> property.
     * The property is a {@link String} class type object of length 15 characters.
     *
     * @return the HMTZ15 property
     */
    public String getTz15() {
        return (String) getFieldValue("HMTZ15");
    }

    /**
     * Sets the <code>HMTZ15</code> property.
     * The property is a {@link String} class type object of length 15 characters.
     * If the supplied String length is longer than 15 character(s),
     * only the first 15 character(s) are used.
     *
     * @param value
     */
    public void setTz15(String value) {
        if (value.length() > 15) {
            value = value.substring(0, 15);
        }
        setField("HMTZ15", value);
    }

    /**
     * Returns the <code>HMSQFX</code> property.
     *
     * @return the HMSQFX property
     */
    public BigDecimal getSqfx() {
        return (BigDecimal) getFieldValue("HMSQFX");
    }

    /**
     * Sets the <code>HMSQFX</code> property.
     *
     * @param value
     */
    public void setSqfx(BigDecimal value) {
        setField("HMSQFX", value);
    }

    /**
     * Returns the <code>HMFTIX</code> property.
     * The property is a {@link String} class type object of length 5 characters.
     *
     * @return the HMFTIX property
     */
    public String getFtix() {
        return (String) getFieldValue("HMFTIX");
    }

    /**
     * Sets the <code>HMFTIX</code> property.
     * The property is a {@link String} class type object of length 5 characters.
     * If the supplied String length is longer than 5 character(s),
     * only the first 5 character(s) are used.
     *
     * @param value
     */
    public void setFtix(String value) {
        if (value.length() > 5) {
            value = value.substring(0, 5);
        }
        setField("HMFTIX", value);
    }

    /**
     * Returns the <code>HMSQNX</code> property.
     *
     * @return the HMSQNX property
     */
    public BigDecimal getSqnx() {
        return (BigDecimal) getFieldValue("HMSQNX");
    }

    /**
     * Sets the <code>HMSQNX</code> property.
     *
     * @param value
     */
    public void setSqnx(BigDecimal value) {
        setField("HMSQNX", value);
    }

    /**
     * Returns the <code>HMOPTX</code> property.
     * The property is a {@link String} class type object of length 15 characters.
     *
     * @return the HMOPTX property
     */
    public String getOptx() {
        return (String) getFieldValue("HMOPTX");
    }

    /**
     * Sets the <code>HMOPTX</code> property.
     * The property is a {@link String} class type object of length 15 characters.
     * If the supplied String length is longer than 15 character(s),
     * only the first 15 character(s) are used.
     *
     * @param value
     */
    public void setOptx(String value) {
        if (value.length() > 15) {
            value = value.substring(0, 15);
        }
        setField("HMOPTX", value);
    }

    /**
     * Returns the <code>HMTX15</code> property.
     * The property is a {@link String} class type object of length 15 characters.
     *
     * @return the HMTX15 property
     */
    public String getTx15() {
        return (String) getFieldValue("HMTX15");
    }

    /**
     * Sets the <code>HMTX15</code> property.
     * The property is a {@link String} class type object of length 15 characters.
     * If the supplied String length is longer than 15 character(s),
     * only the first 15 character(s) are used.
     *
     * @param value
     */
    public void setTx15(String value) {
        if (value.length() > 15) {
            value = value.substring(0, 15);
        }
        setField("HMTX15", value);
    }

    /**
     * Returns the <code>HMSQFY</code> property.
     *
     * @return the HMSQFY property
     */
    public BigDecimal getSqfy() {
        return (BigDecimal) getFieldValue("HMSQFY");
    }

    /**
     * Sets the <code>HMSQFY</code> property.
     *
     * @param value
     */
    public void setSqfy(BigDecimal value) {
        setField("HMSQFY", value);
    }

    /**
     * Returns the <code>HMFTIY</code> property.
     * The property is a {@link String} class type object of length 5 characters.
     *
     * @return the HMFTIY property
     */
    public String getFtiy() {
        return (String) getFieldValue("HMFTIY");
    }

    /**
     * Sets the <code>HMFTIY</code> property.
     * The property is a {@link String} class type object of length 5 characters.
     * If the supplied String length is longer than 5 character(s),
     * only the first 5 character(s) are used.
     *
     * @param value
     */
    public void setFtiy(String value) {
        if (value.length() > 5) {
            value = value.substring(0, 5);
        }
        setField("HMFTIY", value);
    }

    /**
     * Returns the <code>HMSQNY</code> property.
     *
     * @return the HMSQNY property
     */
    public BigDecimal getSqny() {
        return (BigDecimal) getFieldValue("HMSQNY");
    }

    /**
     * Sets the <code>HMSQNY</code> property.
     *
     * @param value
     */
    public void setSqny(BigDecimal value) {
        setField("HMSQNY", value);
    }

    /**
     * Returns the <code>HMOPTY</code> property.
     * The property is a {@link String} class type object of length 15 characters.
     *
     * @return the HMOPTY property
     */
    public String getOpty() {
        return (String) getFieldValue("HMOPTY");
    }

    /**
     * Sets the <code>HMOPTY</code> property.
     * The property is a {@link String} class type object of length 15 characters.
     * If the supplied String length is longer than 15 character(s),
     * only the first 15 character(s) are used.
     *
     * @param value
     */
    public void setOpty(String value) {
        if (value.length() > 15) {
            value = value.substring(0, 15);
        }
        setField("HMOPTY", value);
    }

    /**
     * Returns the <code>HMTY15</code> property.
     * The property is a {@link String} class type object of length 15 characters.
     *
     * @return the HMTY15 property
     */
    public String getTy15() {
        return (String) getFieldValue("HMTY15");
    }

    /**
     * Sets the <code>HMTY15</code> property.
     * The property is a {@link String} class type object of length 15 characters.
     * If the supplied String length is longer than 15 character(s),
     * only the first 15 character(s) are used.
     *
     * @param value
     */
    public void setTy15(String value) {
        if (value.length() > 15) {
            value = value.substring(0, 15);
        }
        setField("HMTY15", value);
    }

    /**
     * Returns the <code>HMRGDT</code> property.
     *
     * @return the HMRGDT property
     */
    public BigDecimal getRgdt() {
        return (BigDecimal) getFieldValue("HMRGDT");
    }

    /**
     * Sets the <code>HMRGDT</code> property.
     *
     * @param value
     */
    public void setRgdt(BigDecimal value) {
        setField("HMRGDT", value);
    }

    /**
     * Returns the <code>HMRGTM</code> property.
     *
     * @return the HMRGTM property
     */
    public BigDecimal getRgtm() {
        return (BigDecimal) getFieldValue("HMRGTM");
    }

    /**
     * Sets the <code>HMRGTM</code> property.
     *
     * @param value
     */
    public void setRgtm(BigDecimal value) {
        setField("HMRGTM", value);
    }

    /**
     * Returns the <code>HMLMDT</code> property.
     *
     * @return the HMLMDT property
     */
    public BigDecimal getLmdt() {
        return (BigDecimal) getFieldValue("HMLMDT");
    }

    /**
     * Sets the <code>HMLMDT</code> property.
     *
     * @param value
     */
    public void setLmdt(BigDecimal value) {
        setField("HMLMDT", value);
    }

    /**
     * Returns the <code>HMCHNO</code> property.
     *
     * @return the HMCHNO property
     */
    public BigDecimal getChno() {
        return (BigDecimal) getFieldValue("HMCHNO");
    }

    /**
     * Sets the <code>HMCHNO</code> property.
     *
     * @param value
     */
    public void setChno(BigDecimal value) {
        setField("HMCHNO", value);
    }

    /**
     * Returns the <code>HMCHID</code> property.
     * The property is a {@link String} class type object of length 10 characters.
     *
     * @return the HMCHID property
     */
    public String getChid() {
        return (String) getFieldValue("HMCHID");
    }

    /**
     * Sets the <code>HMCHID</code> property.
     * The property is a {@link String} class type object of length 10 characters.
     * If the supplied String length is longer than 10 character(s),
     * only the first 10 character(s) are used.
     *
     * @param value
     */
    public void setChid(String value) {
        if (value.length() > 10) {
            value = value.substring(0, 10);
        }
        setField("HMCHID", value);
    }

    /**
     * Returns a {@link DataAccessObjectList} of <code>Mitmah</code> class type objects by executing a generated SQL statement.
     * The generated sql statement will have a <code>where <property> = <value></code> clause based on properties who's value is not <code>null</code>.
     *
     * @param movexSession
     * @param dataAccessObject
     * @param selectionColumns if <code>null</code> all columns from the record are selected
     * @return list the selected <code>Mitmah</code> class type objects
     * @throws SQLException
     */
    public static DataAccessObjectList getList(MovexSession movexSession, DataAccessObject dataAccessObject, String[] selectionColumns) throws SQLException {
        DataAccessObjectList list = new DataAccessObjectList();
        PreparedStatement statement = null;
        try {
            String columnsString = "";
            if (selectionColumns != null) {
                for (int i = 0; i < selectionColumns.length; i++) {
                    if (i > 0) {
                        columnsString = columnsString + ", ";
                    }
                    columnsString = columnsString + selectionColumns[i];
                }
            } else {
                columnsString = "*";
            }
            String whereString = "";
            for (int i = 0; i < dataAccessObject.getFieldCount(); i++) {
                if (dataAccessObject.getFieldValue(i) != null) {
                    if (!whereString.isEmpty()) {
                        whereString = whereString + " and ";
                    }
                    whereString = whereString + dataAccessObject.getFieldName(i) + "=?";
                }
            }
            statement = movexSession.getDatabaseConnection().prepareStatement("select " + columnsString + " from mitmah where " + whereString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            for (int i = 0; i < dataAccessObject.getFieldCount(); i++) {
                if (dataAccessObject.getFieldValue(i) != null) {
                    statement.setObject(i + 1, dataAccessObject.getFieldValue(i));
                }
            }
            ResultSet resultSet = DataAccessObject.readRecords(movexSession, statement);
            ResultSetMetaData metaData = resultSet.getMetaData();
            while (resultSet.next()) {
                Mitmah mitmah = new Mitmah(movexSession);
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    mitmah.setField(metaData.getColumnName(i + 1), resultSet.getObject(i + 1));
                }
                list.add(mitmah);
            }
            return list;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * Returns a {@link DataAccessObjectList} of <code>Mitmah</code> class type objects by executing the supplied SQL statement.
     *
     * @param movexSession
     * @param sqlStatement
     * @return list the selected <code>Mitmah</code> class type objects
     * @throws SQLException
     */
    public static DataAccessObjectList getList(MovexSession movexSession, PreparedStatement sqlStatement) throws SQLException {
        DataAccessObjectList list = new DataAccessObjectList();
        ResultSet resultSet = DataAccessObject.readRecords(movexSession, sqlStatement);
        ResultSetMetaData metaData = resultSet.getMetaData();
        while (resultSet.next()) {
            Mitmah mitmah = new Mitmah(movexSession);
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                mitmah.setField(metaData.getColumnName(i + 1), resultSet.getObject(i + 1));
            }
            list.add(mitmah);
        }
        return list;
    }

    /**
     * Returns a {@link DataAccessObjectList} of <code>Mitmah</code> class type objects by executing the supplied SQL statement.
     *
     * @param movexSession
     * @param sqlStatement
     * @return list the selected <code>Mitmah</code> class type objects
     * @throws SQLException
     */
    public static DataAccessObjectList getList(MovexSession movexSession, String sqlStatement) throws SQLException {
        DataAccessObjectList list = new DataAccessObjectList();
        ResultSet resultSet = DataAccessObject.readRecords(movexSession, sqlStatement);
        ResultSetMetaData metaData = resultSet.getMetaData();
        while (resultSet.next()) {
            Mitmah mitmah = new Mitmah(movexSession);
            for (int i = 0; i < metaData.getColumnCount(); i++) {
                mitmah.setField(metaData.getColumnName(i + 1), resultSet.getObject(i + 1));
            }
            list.add(mitmah);
        }
        return list;
    }

    /**
     * Returns the runtime class of the field value Object.
     *
     * @param fieldName
     * @return The <code>Class</code> object that represents the runtime class of this object.
     */
    public final Class getFieldValueClass(String fieldName) {
        if (fieldName.equals(DefaultMitmah.CONO)) {
            return java.math.BigDecimal.class;
        }
        if (fieldName.equals(DefaultMitmah.ITNO)) {
            return java.lang.String.class;
        }
        if (fieldName.equals(DefaultMitmah.STYN)) {
            return java.lang.String.class;
        }
        if (fieldName.equals(DefaultMitmah.SEA1)) {
            return java.lang.String.class;
        }
        if (fieldName.equals(DefaultMitmah.SQFZ)) {
            return java.math.BigDecimal.class;
        }
        if (fieldName.equals(DefaultMitmah.FTIZ)) {
            return java.lang.String.class;
        }
        if (fieldName.equals(DefaultMitmah.SQNZ)) {
            return java.math.BigDecimal.class;
        }
        if (fieldName.equals(DefaultMitmah.OPTZ)) {
            return java.lang.String.class;
        }
        if (fieldName.equals(DefaultMitmah.TZ15)) {
            return java.lang.String.class;
        }
        if (fieldName.equals(DefaultMitmah.SQFX)) {
            return java.math.BigDecimal.class;
        }
        if (fieldName.equals(DefaultMitmah.FTIX)) {
            return java.lang.String.class;
        }
        if (fieldName.equals(DefaultMitmah.SQNX)) {
            return java.math.BigDecimal.class;
        }
        if (fieldName.equals(DefaultMitmah.OPTX)) {
            return java.lang.String.class;
        }
        if (fieldName.equals(DefaultMitmah.TX15)) {
            return java.lang.String.class;
        }
        if (fieldName.equals(DefaultMitmah.SQFY)) {
            return java.math.BigDecimal.class;
        }
        if (fieldName.equals(DefaultMitmah.FTIY)) {
            return java.lang.String.class;
        }
        if (fieldName.equals(DefaultMitmah.SQNY)) {
            return java.math.BigDecimal.class;
        }
        if (fieldName.equals(DefaultMitmah.OPTY)) {
            return java.lang.String.class;
        }
        if (fieldName.equals(DefaultMitmah.TY15)) {
            return java.lang.String.class;
        }
        if (fieldName.equals(DefaultMitmah.RGDT)) {
            return java.math.BigDecimal.class;
        }
        if (fieldName.equals(DefaultMitmah.RGTM)) {
            return java.math.BigDecimal.class;
        }
        if (fieldName.equals(DefaultMitmah.LMDT)) {
            return java.math.BigDecimal.class;
        }
        if (fieldName.equals(DefaultMitmah.CHNO)) {
            return java.math.BigDecimal.class;
        }
        if (fieldName.equals(DefaultMitmah.CHID)) {
            return java.lang.String.class;
        }
        return java.lang.Object.class;
    }

    /**
     * Creates and returns a new <code>Mitmah</code> class type object and reads a record by using the supplied values.
     *
     * @param movexSession
     * @param cono
     * @param itno
     * @param selectionColumns if <code>null</code> all columns from the record are selected
     * @return the Mitmah class type object that was created
     * @throws AS400SecurityException
     * @throws InterruptedException
     * @throws IOException
     * @throws SQLException
     * @throws RecordNotFoundException
     * @throws TooManyRecordsException
     */
    public static Mitmah createByConoItno(MovexSession movexSession, BigDecimal cono, String itno, String[] selectionColumns) throws AS400SecurityException, InterruptedException, IOException, SQLException, RecordNotFoundException, TooManyRecordsException {
        Mitmah mitmah = new Mitmah(movexSession);
        mitmah.readByConoItno(cono, itno, selectionColumns);
        return mitmah;
    }

    /**
     * Returns true if the specified field is a primary key for this <code>dataAccessObject</code>.
     *
     * @param fieldName
     * @return true if the field is a primary key
     */
    public boolean isFieldPrimaryKey(String fieldName) {
        for (int i = 0; i < primaryKeys.length; i++) {
            if (fieldName.equals(primaryKeys[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes a <code>Mitmah</code> record by using the supplied values.
     *
     * @param cono
     * @param itno
     * @throws SQLException
     * @throws RecordNotFoundException
     * @throws TooManyRecordsException
     * @throws NoRecordWasDeletedException
     * @throws MoreThanOneRecordWasDeletedException
     */
    public static void deleteByConoItno(MovexSession movexSession, BigDecimal cono, String itno) throws SQLException, RecordNotFoundException, TooManyRecordsException, NoRecordWasDeletedException, MoreThanOneRecordWasDeletedException {
        PreparedStatement statement = null;
        try {
            statement = movexSession.getDatabaseConnection().prepareStatement("delete from mitmah where hmcono = ? and hmitno = ?");
            statement.setObject(1, cono);
            statement.setObject(2, itno);
            int updateCount = statement.executeUpdate();
            if (updateCount == 0) {
                throw new NoRecordWasDeletedException("A delete in the persistence table was called but no recod was acctually deleted.");
            }
            if (updateCount > 1) {
                throw new MoreThanOneRecordWasDeletedException("A delete in the persistence table was called but more than one record was acctually deleted.");
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * Deletes this {@link DataAccessObject} in the Mitmah table.
     *
     * @throws SQLException
     * @throws NoRecordWasDeletedException
     * @throws MoreThanOneRecordWasDeletedException
     */
    public void delete() throws SQLException, NoRecordWasDeletedException, MoreThanOneRecordWasDeletedException {
        PreparedStatement statement = null;
        try {
            String keysString = "";
            for (int i = 0; i < getFieldCount(); i++) {
                if (getFieldValue(i) != null) {
                    if (isFieldPrimaryKey(getFieldName(i))) {
                        if (!keysString.isEmpty()) {
                            keysString = keysString + " and ";
                        }
                    }
                    if (isFieldPrimaryKey(getFieldName(i))) {
                        keysString = keysString + getFieldName(i) + "=?";
                    }
                }
            }
            statement = getMovexSession().getDatabaseConnection().prepareStatement("delete from mitmah where " + keysString);
            int index = 0;
            for (int i = 0; i < getFieldCount(); i++) {
                if (getFieldValue(i) != null && isFieldPrimaryKey(getFieldName(i))) {
                    index++;
                    statement.setObject(index, getFieldValue(i));
                }
            }
            int updateCount = statement.executeUpdate();
            if (updateCount == 0) {
                throw new NoRecordWasDeletedException("A delete in the persistence table was called but no recod was acctually deleted.");
            }
            if (updateCount > 1) {
                throw new MoreThanOneRecordWasDeletedException("A delete in the persistence table was called but more than one record was acctually deleted.");
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * Reads a <code>Mitmah</code> record by using the supplied values.
     *
     * @param cono
     * @param itno
     * @param selectionColumns if <code>null</code> all columns from the record are selected
     * @throws SQLException
     * @throws RecordNotFoundException
     * @throws TooManyRecordsException
     */
    public void readByConoItno(BigDecimal cono, String itno, String[] selectionColumns) throws SQLException, RecordNotFoundException, TooManyRecordsException {
        PreparedStatement statement = null;
        try {
            String columnsString = "";
            if (selectionColumns != null) {
                for (int i = 0; i < selectionColumns.length; i++) {
                    if (i > 0) {
                        columnsString = columnsString + ", ";
                    }
                    columnsString = columnsString + selectionColumns[i];
                }
            } else {
                columnsString = "*";
            }
            statement = getMovexSession().getDatabaseConnection().prepareStatement("select " + columnsString + " from mitmah where hmcono = ? and hmitno = ?", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setObject(1, cono);
            statement.setObject(2, itno);
            readRecord(statement);
        } catch (SQLException e) {
            throw e;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * Writes this {@link DataAccessObject} to the Mitmah table.
     *
     * @throws SQLException
     */
    public void write() throws SQLException, NoRecordWasInsertedException {
        PreparedStatement statement = null;
        try {
            String columnsString = "";
            String valuesString = "";
            for (int i = 0; i < getFieldCount(); i++) {
                if (getFieldValue(i) != null) {
                    if (i > 0) {
                        columnsString = columnsString + ", ";
                        valuesString = valuesString + ", ";
                    }
                    columnsString = columnsString + getFieldName(i);
                    valuesString = valuesString + "?";
                }
            }
            statement = getMovexSession().getDatabaseConnection().prepareStatement("insert into Mitmah (" + columnsString + ") values (" + valuesString + ")");
            for (int i = 0; i < getFieldCount(); i++) {
                if (getFieldValue(i) != null) {
                    statement.setObject(i + 1, getFieldValue(i));
                }
            }
            int updateCount = statement.executeUpdate();
            if (updateCount == 0) {
                throw new NoRecordWasInsertedException("An insert in the persistence table was called but no recod was acctually inserted.");
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * Updates this {@link DataAccessObject} in the Mitmah table.
     *
     * @throws SQLException
     * @throws NoRecordWasUpdatedException
     * @throws MoreThanOneRecordWasUpdatedException
     */
    public void update() throws SQLException, NoRecordWasUpdatedException, MoreThanOneRecordWasUpdatedException {
        PreparedStatement statement = null;
        try {
            String columnsString = "";
            String keysString = "";
            for (int i = 0; i < getFieldCount(); i++) {
                if (getFieldValue(i) != null) {
                    if (isFieldPrimaryKey(getFieldName(i))) {
                        if (!keysString.isEmpty()) {
                            keysString = keysString + " and ";
                        }
                    } else {
                        if (!columnsString.isEmpty()) {
                            columnsString = columnsString + ", ";
                        }
                    }
                    if (isFieldPrimaryKey(getFieldName(i))) {
                        keysString = keysString + getFieldName(i) + "=?";
                    } else {
                        columnsString = columnsString + getFieldName(i) + "=?";
                    }
                }
            }
            statement = getMovexSession().getDatabaseConnection().prepareStatement("update mitmah set " + columnsString + " where " + keysString);
            int index = 0;
            for (int i = 0; i < getFieldCount(); i++) {
                if (getFieldValue(i) != null && !isFieldPrimaryKey(getFieldName(i))) {
                    index++;
                    statement.setObject(index, getFieldValue(i));
                }
            }
            for (int i = 0; i < getFieldCount(); i++) {
                if (getFieldValue(i) != null && isFieldPrimaryKey(getFieldName(i))) {
                    index++;
                    statement.setObject(index, getFieldValue(i));
                }
            }
            int updateCount = statement.executeUpdate();
            if (updateCount == 0) {
                throw new NoRecordWasUpdatedException("An update on the record in the persistence table was called but no recod was acctually updated.");
            }
            if (updateCount > 1) {
                throw new MoreThanOneRecordWasUpdatedException("An update on the record in the persistence table was called but more than one record was acctually updated.");
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }
}
