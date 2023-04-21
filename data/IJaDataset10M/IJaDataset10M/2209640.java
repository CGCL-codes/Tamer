package uk.ac.roslin.ensembl.dao.database.factory;

import junit.framework.TestCase;
import org.jmock.Expectations;
import uk.ac.roslin.ensembl.config.EnsemblComparaDivision;
import uk.ac.roslin.ensembl.config.EnsemblDBType;
import uk.ac.roslin.ensembl.dao.database.DBComparisonDatabase;
import uk.ac.roslin.ensembl.dao.database.compara.DBHomologyDAO;
import uk.ac.roslin.ensembl.exception.DAOException;
import uk.ac.roslin.ensembl.model.database.ComparisonDatabase;

/**
 *
 * @author tpaterso
 */
public class DBDAOComparaFactoryTest extends DBDAOFactoryTest {

    final ComparisonDatabase cdb = this.context.mock(ComparisonDatabase.class);

    public DBDAOComparaFactoryTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.context.checking(new Expectations() {

            {
                (atLeast(1).of(cdb)).getDBVersion();
                will(returnValue(mockString));
                (atLeast(1).of(cdb)).getSchemaVersion();
                will(returnValue(mockString));
                (atLeast(1).of(cdb)).getBuild();
                will(returnValue(mockString));
                (atLeast(1).of(cdb)).getdBName();
                will(returnValue(mockString));
                (atLeast(1).of(cdb)).getRegistry();
                will(returnValue(registry));
                (atLeast(1).of(cdb)).getComparisonDivision();
                will(returnValue(EnsemblComparaDivision.FUNGI));
                (atLeast(1).of(cdb)).getType();
                will(returnValue(EnsemblDBType.compara));
                (atLeast(1).of(registry)).findMybatisSchemaForSchemaVersion(EnsemblDBType.compara, mockString);
                will(returnValue(mockString));
            }
        });
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testConstructors() throws Exception {
        DBDAOFactory instance = new DBDAOFactoryImpl(db);
        assertNotNull(instance.getConfiguration());
        assertSame(instance.getDatabase(), db);
        assertEquals(instance.getDBVersion(), db.getDBVersion());
        assertEquals(instance.getEnsemblSchemaVersion(), db.getSchemaVersion());
        assertSame(instance.getRegistry(), db.getRegistry());
        assertSame(instance.getDBType(), db.getType());
    }

    /**
     * Test of getDatabase method, of class DBDAOComparaFactory.
     */
    public void testGetDatabase() throws DAOException {
        System.out.println("getDatabase");
        DBDAOComparaFactory instance = new DBDAOComparaFactory();
        assertNull(instance.getDatabase());
        instance.setDatabase(cdb);
        assertSame(instance.getDatabase(), cdb);
    }

    /**
     * Test of getHomologyDAO method, of class DBDAOComparaFactory.
     */
    public void testGetHomologyDAO() throws Exception {
        System.out.println("getHomologyDAO");
        DBDAOComparaFactory instance = new DBDAOComparaFactory(cdb);
        DBHomologyDAO result = instance.getHomologyDAO();
        assertSame(result.getFactory(), instance);
    }

    /**
     * Test of getComparaDivision method, of class DBDAOComparaFactory.
     */
    public void testGetComparaDivision() throws DAOException {
        System.out.println("getComparaDivision");
        DBDAOComparaFactory instance = new DBDAOComparaFactory(cdb);
        assertEquals(instance.getComparaDivision(), EnsemblComparaDivision.FUNGI);
    }
}
