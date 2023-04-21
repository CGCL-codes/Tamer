package org.deri.iris.dbstorage;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.derby.tools.ij;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.factory.IBasicFactory;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.basics.BasicFactory;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;

public class DBQueryTest extends TestCase {

    private static DatatypeFactory xmlFactory;

    private static final int ARITY = 2;

    private static final String SYMBOL = "wsml-member-of";

    private static IBasicFactory bFactory = BasicFactory.getInstance();

    public static Test suite() {
        return new TestSuite(DBQueryTest.class, DBQueryTest.class.getSimpleName());
    }

    public void testBasic() throws UnsupportedEncodingException, Exception {
        Map conf = new Hashtable();
        conf.put("DB_URL", "jdbc:derby:reasonerTest;create=true");
        conf.put("DB_CLASS", "org.apache.derby.jdbc.EmbeddedDriver");
        DbStorageManager dbm = new DbStorageManager(conf);
        Connection con = dbm.getConnection();
        xmlFactory = DatatypeFactory.newInstance();
        System.out.println("Created Connection");
        String enc = System.getProperty("file.encoding");
        InputStream sqlIn = DBQueryTest.class.getClassLoader().getResourceAsStream("org/deri/iris/dbstorage/reasoner.sql");
        if (sqlIn == null) {
            fail("Can't read the reasoner.sql file");
        }
        InputStream sql = new BufferedInputStream(sqlIn);
        ij.runScript(con, sql, enc, System.out, enc);
        System.out.println("Loaded data");
        String tableName = dbm.getTable(SYMBOL, ARITY);
        assertNotNull(tableName);
        IPredicate p = bFactory.createPredicate(SYMBOL, ARITY);
        String sqlQuery = "SELECT term1, termType1, term2, termType2 FROM " + tableName;
        IMixedDatatypeRelation rel = dbm.computeIMixedDatatypeRelation(p, sqlQuery);
        System.out.println("Created IMixedDatatypeRelation for predicate " + SYMBOL + " with arity " + ARITY);
        System.out.println(rel.toString());
        assertTrue(rel.size() == 6);
        System.out.println("IMixedDatatypeRelation size is correct");
        List<ITerm> terms = new ArrayList<ITerm>();
        ITerm term1 = CONCRETE.createIri("http://simple#Graham");
        ITerm term2 = CONCRETE.createIri("http://simple#Man");
        terms.add(term1);
        terms.add(term2);
        ITuple tuple = bFactory.createTuple(terms);
        assertTrue(rel.contains(tuple));
        terms.clear();
        System.out.println("IMixedDatatypeRelation contains " + tuple.toString());
        term1 = CONCRETE.createIri("http://simple#Marge");
        term2 = CONCRETE.createIri("http://simple#Human");
        terms.add(term1);
        terms.add(term2);
        tuple = bFactory.createTuple(terms);
        assertTrue(rel.contains(tuple));
        terms.clear();
        System.out.println("IMixedDatatypeRelation contains " + tuple.toString());
        term1 = TERM.createString("Graham");
        term2 = CONCRETE.createIri("http://www.wsmo.org/wsml/wsml-syntax#string");
        terms.add(term1);
        terms.add(term2);
        tuple = bFactory.createTuple(terms);
        assertTrue(rel.contains(tuple));
        terms.clear();
        System.out.println("IMixedDatatypeRelation contains " + tuple.toString());
        term1 = TERM.createString("marge");
        term2 = CONCRETE.createIri("http://www.wsmo.org/wsml/wsml-syntax#string");
        terms.add(term1);
        terms.add(term2);
        tuple = bFactory.createTuple(terms);
        assertTrue(rel.contains(tuple));
        terms.clear();
        System.out.println("IMixedDatatypeRelation contains " + tuple.toString());
        XMLGregorianCalendar cal = xmlFactory.newXMLGregorianCalendar("1976-08-16Z");
        term1 = CONCRETE.createDate(cal.getYear(), cal.getMonth(), cal.getDay());
        term2 = CONCRETE.createIri("http://www.wsmo.org/wsml/wsml-syntax#date");
        terms.add(term1);
        terms.add(term2);
        tuple = bFactory.createTuple(terms);
        assertTrue(rel.contains(tuple));
        terms.clear();
        System.out.println("IMixedDatatypeRelation contains " + tuple.toString());
        cal = xmlFactory.newXMLGregorianCalendar("1976-08-15Z");
        term1 = CONCRETE.createDate(cal.getYear(), cal.getMonth(), cal.getDay());
        term2 = CONCRETE.createIri("http://www.wsmo.org/wsml/wsml-syntax#date");
        terms.add(term1);
        terms.add(term2);
        tuple = bFactory.createTuple(terms);
        assertTrue(rel.contains(tuple));
        terms.clear();
        System.out.println("IMixedDatatypeRelation contains " + tuple.toString());
        dbm.clear();
        con.createStatement().executeUpdate("DROP TABLE referenceTable");
        boolean gotSQLExc = false;
        try {
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
        } catch (SQLException se) {
            if (se.getSQLState().equals("XJ015")) {
                gotSQLExc = true;
            }
        }
        if (!gotSQLExc) {
            System.out.println("Database did not shut down normally");
        } else {
            System.out.println("Database shut down normally");
        }
    }
}
