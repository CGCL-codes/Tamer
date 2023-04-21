package net.jadoth.sqlengine.dbms.oracle11g;

import net.jadoth.meta.License;
import net.jadoth.sqlengine.SQL;
import net.jadoth.sqlengine.internal.SqlxAggregateCOLLECT_asString;
import net.jadoth.sqlengine.license.SqlEngineLicense;

/**
 * The Class OracleSQL.
 * 
 * @author Thomas Muenz
 */
@License(name = SqlEngineLicense.LICENSE_NAME, licenseClass = SqlEngineLicense.class, declaringClass = OracleSQL.class)
public class OracleSQL extends SQL {

    /**
	 * W m_ concat.
	 * 
	 * @param expression the expression
	 * @return the sqlx aggregate collec t_as string
	 */
    public static SqlxAggregateCOLLECT_asString WM_CONCAT(final Object expression) {
        return new SqlxAggregateCOLLECT_asString(expression);
    }
}
