package com.objectwave.persist.sqlConstruction;

import java.util.Vector;
import com.objectwave.persist.examples.*;

/**
 * @author Dave Hoag
 * @version $Date: 2004/10/28 16:31:52 $ $Revision: 1.1 $
 */
public class SQLSelectTest extends com.objectwave.test.UnitTestBaseImpl {

    /**
     *  The main program for the Test class
     *
     * @param  args The command line arguments
     */
    public static void main(String[] args) {
        com.objectwave.test.TestRunner.run(new SQLSelectTest(), args);
    }

    /**
     *  A unit test for JUnit
     */
    public void testAddOrderBy() {
        SQLSelect sel = new SQLSelect("testEntity");
        char c = sel.getAlias();
        TestCompany p = new TestCompany();
        p.setPrimaryContact(new TestPerson());
        Vector paths = new Vector();
        paths.add("name");
        paths.add("primaryContact.title");
        SQLSelect sel2 = new SQLSelect("person");
        char c2 = sel2.getAlias();
        sel.joinWith("primaryContact", sel2, "databaseIdentifier", "employeeIdentifier");
        sel.addOrderBy(paths, p);
        testContext.assertEquals(" " + c + ".name, " + c2 + ".title", sel.orderByList.toString());
    }

    /**
     *  A unit test for JUnit
     */
    public void testFindColumnName() {
        SQLSelect sel = new SQLSelect("employee");
        char c = sel.getAlias();
        SQLSelect sel2 = new SQLSelect("person");
        char c2 = sel2.getAlias();
        sel.joinWith("person", sel2, "databaseIdentifier", "employeeIdentifier");
        ExampleEmployee emp = new ExampleEmployee();
        String name = sel.findColumnName(emp, "title");
        testContext.assertEquals(" " + c + ".title", name);
        emp.setPerson(new ExamplePerson());
        name = sel.findColumnName(emp, "person.name");
        testContext.assertEquals(" " + c2 + ".name", name);
        sel = new SQLSelect("testEntity");
        sel2 = new SQLSelect("testEntity");
        c2 = sel2.getAlias();
        c = sel.getAlias();
        sel.joinWith("primaryContact", sel2, "primaryContact", "databaseIdentifier");
        TestCompany p = new TestCompany();
        name = sel.findColumnName(p, "name");
        testContext.assertEquals(" " + c + ".name", name);
        p.setPrimaryContact(new TestPerson());
        name = sel.findColumnName(p, "primaryContact.title");
        testContext.assertEquals(" " + c2 + ".title", name);
    }

    /**
     *  A unit test for JUnit
     */
    public void testSetAsDelete() {
        SQLSelect sel = new SQLSelect("employee");
        sel.addColumnList("one");
        sel.addColumnList("two");
        sel.addWhereClause(sel.getAlias() + ".col = 100");
        sel.addWhereClause(sel.getAlias() + ".col2 = 101");
        testContext.assertEquals("Invalid select", "SELECT one, two FROM employee " + sel.getAlias() + " WHERE " + sel.getAlias() + ".col = 100 AND " + sel.getAlias() + ".col2 = 101", sel.getSqlStatement().toString());
        sel.reusableStatementBuffer.setLength(0);
        sel.setAsDelete(true);
        testContext.assertEquals("Invalid delete", "DELETE  FROM employee WHERE col = 100 AND col2 = 101", sel.getSqlStatement().toString());
    }
}
