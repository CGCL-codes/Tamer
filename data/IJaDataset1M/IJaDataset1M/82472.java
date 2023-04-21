package com.objectwave.persist.xml;

import com.objectwave.exception.*;
import com.objectwave.logging.*;
import com.objectwave.persist.*;
import com.objectwave.persist.file.FileBroker;
import com.objectwave.persist.setup.Configurator;
import com.objectwave.persist.query.SQLQuery;
import com.objectwave.persist.query.SQLQuery;
import com.objectwave.persist.broker.*;
import com.objectwave.persist.examples.*;
import com.objectwave.persist.mapping.RDBPersistentAdapter;
import com.objectwave.transactionalSupport.ObjectEditingView;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Collection;

/**
 *  A sample for using a XML file to read match field. This implementation is
 *  showing the absolute minimal amount of information that could is required in
 *  a persistent object.
 *
 * @author  Zhou Cai
 * @version  $Id: TestXMLCompany.java,v 1.4 2005/02/13 19:38:29 dave_hoag Exp $
 */
public class TestXMLCompany extends DomainObject {

    /**
	 */
    public static Field _name;

    /**
	 */
    public static Field _address;

    /**
	 */
    public static Field _employees;

    /**
	 */
    public static Field _founders;

    static Vector classDescriptor;

    static String tableName;

    protected String name;

    protected String address;

    protected Vector employees = new Vector();

    protected Vector founders = new Vector();

    /**
	 *  Using this method to do the field match with xml file called
	 *  testCompany.xml. There are two possible parameters for the
	 *  initializeObjectEditor method. The first is the actual xml file name.
	 *  This file will be located within the same directory of the persisent
	 *  class definition. The second could be a key to System properties that
	 *  identifies the name of an XML file to use.
	 *
	 * @exception  FileNotFoundException The specified file could not be located
	 * @exception  ConfigurationException Some other problem related to the configuration occured
	 */
    public TestXMLCompany() throws FileNotFoundException, ConfigurationException {
        setObjectEditor(initializeObjectEditor("testCompany.xml", this));
    }

    /**
	 *  Sets the Employees attribute of the TestXMLCompany object
	 *
	 * @param  aValue The new Employees value
	 */
    public void setEmployees(Vector aValue) {
        editor.set(_employees, aValue, employees);
    }

    /**
	 *  Sets the Founders attribute of the TestXMLCompany object
	 *
	 * @param  aValue The new Founders value
	 */
    public void setFounders(Vector aValue) {
        editor.set(_founders, aValue, founders);
    }

    /**
	 *  Sets the Name attribute of the TestXMLCompany object
	 *
	 * @param  aValue The new Name value
	 */
    public void setName(String aValue) {
        editor.set(_name, aValue, name);
    }

    /**
	 *  Sets the Address attribute of the TestXMLCompany object
	 *
	 * @param  aValue The new Address value
	 */
    public void setAddress(String aValue) {
        editor.set(_address, aValue, address);
    }

    /**
	 *  Gets the Employees attribute of the TestXMLCompany object
	 *
	 * @return  The Employees value
	 */
    public Vector getEmployees() {
        return (Vector) editor.get(_employees, employees);
    }

    /**
	 *  Gets the Founders attribute of the TestXMLCompany object
	 *
	 * @return  The Founders value
	 */
    public Vector getFounders() {
        return (Vector) editor.get(_founders, founders);
    }

    /**
	 *  Gets the Name attribute of the TestXMLCompany object
	 *
	 * @return  The Name value
	 */
    public String getName() {
        return (String) editor.get(_name, name);
    }

    /**
	 *  Gets the Address attribute of the TestXMLCompany object
	 *
	 * @return  The Address value
	 */
    public String getAddress() {
        return (String) editor.get(_address, address);
    }

    /**
	 *  Use an instance method to access a static variable. This method MUST be
	 *  duplicated in each and every subclass. This allows our generic logic in
	 *  this super class to modify static state in a subclass.
	 *
	 * @param  table The new TableName value
	 */
    protected void setTableName(final String table) {
        tableName = table;
    }

    /**
	 *  Use an instance method to access a static variable. This method MUST be
	 *  duplicated in each and every subclass. This allows our generic logic in
	 *  this super class to modify static state in a subclass.
	 *
	 * @param  v The new ClassDescriptor value
	 */
    protected void setClassDescriptor(final Vector v) {
        classDescriptor = v;
    }

    /**
	 *  Use an instance method to access a static variable. This method MUST be
	 *  duplicated in each and every subclass. This allows our generic logic in
	 *  this super class to modify static state in a subclass.
	 *
	 * @return  The ClassDescriptor value
	 */
    protected Vector getClassDescriptor() {
        return classDescriptor;
    }

    /**
	 *  Use an instance method to access a static variable. This method MUST be
	 *  duplicated in each and every subclass. This allows our generic logic in
	 *  this super class to modify static state in a subclass.
	 *
	 * @return  The TableName value
	 */
    protected String getTableName() {
        return tableName;
    }

    /**
	 *  Unit Test
	 *
	 * @author  trever
	 * @version  $Id: TestXMLCompany.java,v 1.4 2005/02/13 19:38:29 dave_hoag Exp $
	 */
    public static class Test extends com.objectwave.test.UnitTestBaseImpl {

        /**
		 *  The main program for the Test class
		 *
		 * @param  args The command line arguments
		 */
        public static void main(String[] args) {
            com.objectwave.test.TestRunner.run(new Test(), args);
        }

        /**
		 *  The JUnit setup method
		 *
		 * @param  str The new Up value
		 * @param  context The new Up value
		 * @exception  Exception
		 */
        public void setUp(String str, com.objectwave.test.TestContext context) throws Exception {
            System.setProperty("ow.persistVerbose", "true");
            System.setProperty("ow.persistConnectionVerbose", "true");
            System.setProperty("table.testPerson", "testPerson.xml");
            System.setProperty("table.testCompany", "testCompany.xml");
            super.setUp(str, context);
            Broker broker = new FileBroker();
            System.out.println("Using default broker of " + broker);
            BrokerFactory.setDefaultBroker(broker);
            SQLQuery.setDefaultBroker(broker);
            com.objectwave.transactionalSupport.TransactionLogTest.reset();
        }

        /**
		 *  The teardown method for JUnit
		 *
		 * @param  context Description of Parameter
		 */
        public void tearDown(com.objectwave.test.TestContext context) {
            try {
                TestXMLCompany company = new TestXMLCompany();
                ObjectQuery qry = new SQLQuery(company);
                try {
                    qry.deleteAll();
                } catch (Exception ex) {
                }
                BrokerFactory.getDefaultBroker().close();
                removeFile("testCompany.dbf");
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        /**
		 * @exception  ConfigurationException
		 * @exception  Exception
		 */
        public void insert() throws ConfigurationException, Exception {
            try {
                TestXMLCompany c1 = new TestXMLCompany();
                c1.setName("Objectwave");
                c1.setAddress("Wacker Dr.");
                c1.save();
            } catch (ConfigurationException e) {
                MessageLog.error(this, "Failed create persistence map from XML file", e);
                throw e;
            } catch (Exception e) {
                MessageLog.error(this, "Failed to insert a company!", e);
                throw e;
            }
        }

        /**
		 * @exception  Exception
		 */
        public void delete() throws Exception {
            com.objectwave.transactionalSupport.TransactionLog log = com.objectwave.transactionalSupport.TransactionLog.startTransaction("RDB", "context");
            TestXMLCompany c1 = new TestXMLCompany();
            c1.setName("Objectwave");
            ObjectQuery q = new SQLQuery(c1);
            ArrayList v = (ArrayList) q.find();
            System.out.println("find " + v.size() + " to be deleted");
            for (int i = 0; i < v.size(); i++) {
                TestXMLCompany c = (TestXMLCompany) v.get(i);
                c.markForDelete();
            }
            log.commit();
        }

        /**
		 *  A unit test for JUnit
		 *
		 * @exception  Exception
		 */
        public void testDelete() throws Exception {
            delete();
            TestXMLCompany p = new TestXMLCompany();
            ObjectQuery q = new SQLQuery(p);
            p.setName("Objectwave");
            Collection v = q.find();
            testContext.assertTrue("delete not correct", (v.size() == 0));
        }

        /**
		 *  A unit test for JUnit
		 *
		 * @exception  Exception
		 */
        public void testMapBuilding() throws Exception {
            TestXMLCompany newCompany = new TestXMLCompany();
            testContext.assertEquals("TableName wasn't set!", "testCompany", newCompany.getTableName());
            RDBPersistentAdapter adapter = (RDBPersistentAdapter) newCompany.getAdapter();
            AttributeTypeColumn[] desc = adapter.getAttributeDescriptions();
            testContext.assertTrue("There are no attribute descriptions in the map!!", desc.length != 0);
        }

        /**
		 *  A unit test for JUnit
		 *
		 * @exception  Exception
		 */
        public void testInsert() throws Exception {
            TestXMLCompany newCompany = new TestXMLCompany();
            newCompany.setName("Objectwave");
            newCompany.setAddress("Wacker Dr.");
            newCompany.save();
            testContext.assertTrue("Primary Key not assigned!", newCompany.getObjectIdentifier() != null);
            testContext.assertTrue("Address information lost!", newCompany.getAddress() != null);
            TestXMLCompany p = new TestXMLCompany();
            ObjectQuery q = new SQLQuery(p);
            p.setName("Objectwave");
            ArrayList v = (ArrayList) q.find();
            testContext.assertTrue("insert not correct", (v.size() == 1));
            TestXMLCompany t = (TestXMLCompany) v.get(0);
            testContext.assertEquals("Invalid ObjectID ", newCompany.getObjectIdentifier(), t.getObjectIdentifier());
            testContext.assertTrue("Address is null!", t.getAddress() != null);
            testContext.assertTrue("address is not correct", t.getAddress().equals("Wacker Dr."));
        }

        /**
		 *  A unit test for JUnit
		 *
		 * @exception  Exception
		 */
        void testQuery() throws Exception {
            TestXMLCompany t = new TestXMLCompany();
            ObjectQuery q2 = new SQLQuery(t);
            t.setName("Objectwave");
            t.setAddress("1131 Wacker Dr");
            ArrayList vv = (ArrayList) q2.find();
            if (vv != null) {
                System.out.println("find " + vv.size() + " companies");
                if (vv.size() > 0) {
                    TestXMLCompany c = (TestXMLCompany) vv.get(0);
                    Vector vp = c.getEmployees();
                    System.out.println("employees = " + vp.size());
                    Vector fs = c.getFounders();
                    System.out.println("founders = " + fs.size());
                }
            }
        }

        static {
            System.setProperty("ow.persistDriver", "sun.jdbc.odbc.JdbcOdbcDriver");
            System.setProperty("ow.connectUrl", "jdbc:odbc:test");
            System.setProperty("table.testPerson", "testPerson.xml");
            System.setProperty("table.testCompany", "testCompany.xml");
            System.setProperty("ow.databaseImpl", "com.objectwave.persist.broker.AccessBroker");
            Configurator.useDatabase();
        }
    }

    static {
        try {
            _name = TestXMLCompany.class.getDeclaredField("name");
            _address = TestXMLCompany.class.getDeclaredField("address");
            _employees = TestXMLCompany.class.getDeclaredField("employees");
            _founders = TestXMLCompany.class.getDeclaredField("founders");
            _name.setAccessible(true);
            _address.setAccessible(true);
            _employees.setAccessible(true);
            _founders.setAccessible(true);
        } catch (NoSuchFieldException ex) {
            ex.printStackTrace();
        }
    }
}
