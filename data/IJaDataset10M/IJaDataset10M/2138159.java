package de.erdesignerng.test.sql.mssql;

import de.erdesignerng.dialect.*;
import de.erdesignerng.dialect.mssql.MSSQLDialect;
import de.erdesignerng.model.*;
import de.erdesignerng.modificationtracker.HistoryModificationTracker;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Test for XML based model io.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-11-16 17:48:26 $
 */
public class ReverseEngineeringTest extends AbstractConnectionTest {

    public void testReverseEngineerMSSQL() throws Exception {
        Connection theConnection = null;
        try {
            theConnection = createConnection();
            loadSQL(theConnection, "db.sql");
            Dialect theDialect = new MSSQLDialect();
            JDBCReverseEngineeringStrategy<MSSQLDialect> theST = theDialect.getReverseEngineeringStrategy();
            Model theModel = new Model();
            theModel.setDialect(theDialect);
            theModel.setModificationTracker(new HistoryModificationTracker(theModel));
            List<SchemaEntry> theAllSchemas = theST.getSchemaEntries(theConnection);
            List<SchemaEntry> theSchemas = new ArrayList<SchemaEntry>();
            for (SchemaEntry theEntry : theAllSchemas) {
                System.out.println(theEntry.getSchemaName());
                if ("dbo".equals(theEntry.getSchemaName())) {
                    theSchemas.add(theEntry);
                }
            }
            ReverseEngineeringOptions theOptions = new ReverseEngineeringOptions();
            theOptions.setTableNaming(TableNamingEnum.STANDARD);
            theOptions.getTableEntries().addAll(theST.getTablesForSchemas(theConnection, theSchemas));
            theST.updateModelFromConnection(theModel, new EmptyWorldConnector(), theConnection, theOptions, new EmptyReverseEngineeringNotifier());
            Table theTable = theModel.getTables().findByName("Table1");
            assertTrue(theTable != null);
            Attribute<Table> theAttribute = theTable.getAttributes().findByName("tb2_1");
            assertTrue(theAttribute != null);
            assertTrue(!theAttribute.isNullable());
            assertTrue(theAttribute.getDatatype().getName().equals("varchar"));
            assertTrue(theAttribute.getSize() == 20);
            theAttribute = theTable.getAttributes().findByName("tb2_2");
            assertTrue(theAttribute != null);
            assertTrue(theAttribute.isNullable());
            assertTrue(theAttribute.getDatatype().getName().equals("varchar"));
            assertTrue(theAttribute.getSize() == 100);
            theAttribute = theTable.getAttributes().findByName("tb2_3");
            assertTrue(theAttribute != null);
            assertTrue(!theAttribute.isNullable());
            assertTrue(theAttribute.getDatatype().getName().equals("decimal"));
            assertTrue(theAttribute.getSize() == 20);
            assertTrue(theAttribute.getFraction() == 5);
            theTable = theModel.getTables().findByName("Table2");
            assertTrue(theTable != null);
            theAttribute = theTable.getAttributes().findByName("tb3_1");
            assertTrue(theAttribute != null);
            theAttribute = theTable.getAttributes().findByName("tb3_2");
            assertTrue(theAttribute != null);
            theAttribute = theTable.getAttributes().findByName("tb3_3");
            assertTrue(theAttribute != null);
            Index thePK = theTable.getPrimarykey();
            assertTrue(thePK != null);
            assertTrue(thePK.getExpressions().findByAttributeName("tb3_1") != null);
            View theView = theModel.getViews().findByName("View1");
            assertTrue(theView != null);
            Relation theRelation = theModel.getRelations().findByName("FK1");
            assertTrue(theRelation != null);
            assertTrue("Table1".equals(theRelation.getImportingTable().getName()));
            assertTrue("Table2".equals(theRelation.getExportingTable().getName()));
            assertTrue(theRelation.getMapping().size() == 1);
            Map.Entry<IndexExpression, Attribute<Table>> theEntry = theRelation.getMapping().entrySet().iterator().next();
            assertTrue("tb2_1".equals(theEntry.getValue().getName()));
            assertTrue("tb3_1".equals(theEntry.getKey().getAttributeRef().getName()));
            SQLGenerator theGenerator = theDialect.createSQLGenerator();
            String theResult = statementListToString(theGenerator.createCreateAllObjects(theModel), theGenerator);
            System.out.println(theResult);
            String theReference = readResourceFile("result.sql");
            assertTrue(compareStrings(theResult, theReference));
        } finally {
            if (theConnection != null) {
                theConnection.close();
            }
        }
    }

    public void testReverseEngineeredSQL() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException {
        Connection theConnection = null;
        try {
            theConnection = createConnection();
            loadSingleSQL(theConnection, "result.sql");
        } finally {
            if (theConnection != null) {
                theConnection.close();
            }
        }
    }
}
