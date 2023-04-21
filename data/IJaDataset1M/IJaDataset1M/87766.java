package de.erdesignerng.test.sql.mssql;

import de.erdesignerng.dialect.mssql.MSSQLDialect;
import de.erdesignerng.test.sql.AbstractDialectTestCase;

/**
 * Test for the Microsoft SQL Server SQL Generator.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2009-03-09 19:07:29 $
 */
public class MSSQLDialectTest extends AbstractDialectTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dialect = new MSSQLDialect();
        textDataType = dialect.getDataTypes().findByName("varchar");
        intDataType = dialect.getDataTypes().findByName("int");
        basePath = "/de/erdesignerng/test/sql/mssql/";
    }
}
