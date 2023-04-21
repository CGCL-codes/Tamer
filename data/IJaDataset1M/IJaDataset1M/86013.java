package gaea.junit.ui;

import gaea.junit.Constants;
import java.awt.Color;
import java.util.Vector;
import junit.framework.TestCase;
import org.gaea.common.GaeaError;
import org.gaea.common.GaeaLoggerFactory;
import org.gaea.common.GaeaLoggerFactory.Severity;
import org.gaea.common.command.CommandResult;
import org.gaea.common.command.database.DatabaseImpl;
import org.gaea.common.command.database.versant.Database;
import org.gaea.common.command.output.OutputConnect;
import org.gaea.common.exception.GaeaException;
import org.gaea.demo.Jouet;
import org.gaea.demo.Personne;
import org.gaea.ui.treatment.Connection;
import org.gaea.ui.treatment.Data;
import org.gaea.ui.treatment.Treatment;

/**
 * Test class for the ui.treatment.Data class.
 * 
 * @author mdmajor
 */
public class TreatmentDataTest extends TestCase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        if (Treatment.getInstance().getConnection().getConnector().getHostname() == null) {
            Connection connection = new Connection();
            DatabaseImpl impl = new Database();
            try {
                boolean successful = connection.setup(impl, Constants.DATABASE_HOSTNAME, Constants.DATABASE_USERNAME, Constants.DATABASE_PASSWORD, Constants.DATABASE_DATABASE, null);
                assertTrue(successful == true);
                CommandResult<OutputConnect> result = connection.getResult();
                Treatment.getInstance().setConnection(connection);
                assertTrue(result.getErrorCode() == GaeaError.NO_ERROR);
            } catch (GaeaException e) {
                e.printStackTrace();
                assertTrue(false);
            }
        }
        GaeaLoggerFactory.setDefaultSeverity(Severity.Finest);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
	 * Method to test add and delete of an instance
	 */
    public void testAddDelete() throws GaeaException {
        try {
            Data dataInstance = Treatment.getInstance().getData();
            Vector<Object> jouetsVect = dataInstance.getInstancesForClass("org.gaea.demo.Jouet");
            assertTrue(jouetsVect != null);
            int sizeBefore = jouetsVect.size();
            Jouet hochet = new Jouet(235243, Color.BLUE, 0, 3, true);
            dataInstance.addInstance(hochet);
            dataInstance.commitTransaction();
            jouetsVect = dataInstance.getInstancesForClass("org.gaea.demo.Jouet");
            assertTrue(jouetsVect != null);
            assertTrue(jouetsVect.size() == sizeBefore + 1);
            dataInstance.deleteInstance(hochet);
            dataInstance.commitTransaction();
            jouetsVect = dataInstance.getInstancesForClass("org.gaea.demo.Jouet");
            assertTrue(jouetsVect != null);
            assertTrue(jouetsVect.size() == sizeBefore);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    /**
	 * Tests the modification of an existing instance in the db.
	 */
    public void testModify() throws GaeaException {
        try {
            Personne personne = getFirstPersonne();
            if (personne == null) {
                return;
            }
            int oldSecuid = personne.secuid;
            int tempSecuid = 6666;
            Data dataInstance = Treatment.getInstance().getData();
            dataInstance.modifyInstance(personne, personne.getClass().getField("secuid"), tempSecuid);
            dataInstance.commitTransaction();
            Personne personneCheck = getFirstPersonne();
            if (personne == null) {
                assertTrue(false);
                return;
            }
            assertTrue(personneCheck.secuid == tempSecuid);
            dataInstance.modifyInstance(personne, personne.getClass().getField("secuid"), oldSecuid);
            dataInstance.commitTransaction();
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    /**
	 * Fetches the list of "personnes" and returns the first one.
	 * 
	 * @return An instance of Personne.
	 */
    private Personne getFirstPersonne() {
        try {
            Vector<Object> personnesVect = Treatment.getInstance().getData().getInstancesForClass("org.gaea.demo.Personne");
            assertTrue(personnesVect != null);
            if (personnesVect.size() > 0) {
                Object objPersonne = personnesVect.get(0);
                assertTrue(objPersonne instanceof Personne);
                return (Personne) objPersonne;
            }
        } catch (GaeaException e) {
            e.printStackTrace();
            assertTrue(false);
        }
        return null;
    }
}
