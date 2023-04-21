package org.dbunit.ext.db2;

import java.sql.Types;
import java.util.Arrays;
import java.util.Collection;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.DataTypeException;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;
import org.dbunit.dataset.datatype.StringDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Specialized factory that recognizes DB2 data types.
 *
 * @author Federico Spinazzi
 * @author Manuel Laflamme
 * @since Jul 17, 2003
 * @version $Revision: 1031 $
 */
public class Db2DataTypeFactory extends DefaultDataTypeFactory {

    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(Db2DataTypeFactory.class);

    /**
     * Database product names supported.
     */
    private static final Collection DATABASE_PRODUCTS = Arrays.asList(new String[] { "db2" });

    static final DataType DB2XML_XMLVARCHAR = new StringDataType("DB2XML.XMLVARCHAR", Types.DISTINCT);

    static final DataType DB2XML_XMLCLOB = new StringDataType("DB2XML.XMLCLOB", Types.DISTINCT);

    static final DataType DB2XML_XMLFILE = new StringDataType("DB2XML.XMLFILE", Types.DISTINCT);

    /**
     * @see org.dbunit.dataset.datatype.IDbProductRelatable#getValidDbProducts()
     */
    public Collection getValidDbProducts() {
        return DATABASE_PRODUCTS;
    }

    public DataType createDataType(int sqlType, String sqlTypeName) throws DataTypeException {
        if (logger.isDebugEnabled()) logger.debug("createDataType(sqlType={}, sqlTypeName={}) - start", String.valueOf(sqlType), sqlTypeName);
        if (sqlType == Types.DISTINCT) {
            if (sqlTypeName.equals(DB2XML_XMLVARCHAR.toString())) {
                return DB2XML_XMLVARCHAR;
            }
            if (sqlTypeName.equals(DB2XML_XMLCLOB.toString())) {
                return DB2XML_XMLCLOB;
            }
            if (sqlTypeName.equals(DB2XML_XMLFILE.toString())) {
                return DB2XML_XMLFILE;
            }
        }
        return super.createDataType(sqlType, sqlTypeName);
    }
}
