package com.sri.emo;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.io.InputStream;
import com.jcorporate.expresso.core.controller.Block;
import com.jcorporate.expresso.core.controller.ExpressoResponse;
import com.jcorporate.expresso.core.dataobjects.DataObject;
import com.jcorporate.expresso.core.dataobjects.DataObjectFactory;
import com.jcorporate.expresso.core.dbobj.DBObject;
import com.jcorporate.expresso.core.dbobj.SchemaFactory;
import com.jcorporate.expresso.core.registry.RequestRegistry;
import com.jcorporate.expresso.services.controller.DBMaint;
import com.jcorporate.expresso.services.test.ControllerTestFixture;
import com.jcorporate.expresso.services.test.TestSystemInitializer;
import com.sri.emo.test.DatabaseTestFixture;
import com.sri.emo.test.EmoTestSuite;
import junit.framework.TestCase;

/**
 * Test the schema, in particular, test that DBMaint.LIST works for
 * all the objects.
 *
 * @author Michael Rimov
 *
 */
public class TestEmoSchema extends TestCase {

    private EmoSchema emoSchema = null;

    private DatabaseTestFixture databaseTestFixture;

    private ControllerTestFixture testFixture = null;

    protected void setUp() throws Exception {
        super.setUp();
        emoSchema = (EmoSchema) SchemaFactory.getInstance().getSchema(EmoSchema.class.getName());
        InputStream stream = EmoTestSuite.class.getResourceAsStream("WizardTestData.xml");
        if (stream == null) {
            throw new Exception("Cannot find file 'WizardTestData.xml' of test data adjacent to class: " + EmoTestSuite.class.getName());
        }
        databaseTestFixture = new DatabaseTestFixture(TestSystemInitializer.getTestContext(), stream);
        databaseTestFixture.setUp();
        testFixture = new ControllerTestFixture();
        testFixture.setUp();
    }

    protected void tearDown() throws Exception {
        emoSchema = null;
        databaseTestFixture.tearDown();
        databaseTestFixture = null;
        testFixture.tearDown();
        testFixture = null;
        super.tearDown();
    }

    public void testGetMessageBundlePath() {
        String expectedReturn = "com/sri/emo";
        String actualReturn = emoSchema.getMessageBundlePath();
        assertEquals("return value", expectedReturn, actualReturn);
    }

    public void testDBMaintList() throws Exception {
        for (Enumeration e = emoSchema.getMembers(); e.hasMoreElements(); ) {
            DBObject oneDBObject = (DBObject) e.nextElement();
            assertTrue(oneDBObject != null);
            final String dbobjClassName = oneDBObject.getClass().getName();
            Class c = Thread.currentThread().getContextClassLoader().loadClass(dbobjClassName);
            DataObject oneObj = DataObjectFactory.createDataObject(c, RequestRegistry.getDataContext(), RequestRegistry.getUser());
            Map params = new HashMap();
            params.put("dbobj", dbobjClassName);
            ExpressoResponse response = testFixture.invokeFacade(DBMaint.class, params, DBMaint.LIST);
            assertTrue(response.getErrors() == null || response.getErrors().getErrorCount() == 0);
            int numObjects = oneObj.count();
            Block rows = response.getBlock("recordList");
            assertTrue(rows != null);
            final int MAX_ROWS = 50;
            if (numObjects > MAX_ROWS) {
                assertEquals(MAX_ROWS, rows.getNested().size());
            } else {
                assertEquals(numObjects, rows.getNested().size());
            }
        }
    }
}
