package uk.icat3.security;

import java.util.Random;
import junit.framework.JUnit4TestAdapter;
import org.apache.log4j.Logger;
import uk.icat3.exceptions.ICATAPIException;
import static org.junit.Assert.*;
import org.junit.Test;
import uk.icat3.entity.Datafile;
import uk.icat3.entity.DatafileParameter;
import uk.icat3.entity.Dataset;
import uk.icat3.entity.Investigation;
import uk.icat3.exceptions.InsufficientPrivilegesException;
import uk.icat3.util.AccessType;
import static uk.icat3.util.TestConstants.*;

/**
 *
 * @author gjd37
 */
public class TestGateKeeperDeleterDatafile extends TestGateKeeperUtil {

    private static Logger log = Logger.getLogger(TestGateKeeperDeleterDatafile.class);

    private Random random = new Random();

    /**
     * Tests deleter on valid Datafile for read
     *
     * ACTION_SELECT  - Y
     */
    @Test
    public void testDeleterReadOnDatafile() throws ICATAPIException {
        log.info("Testing  user: " + DELETER_USER + " for reading Datafile Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Datafile Datafile = getDatafile(true);
        GateKeeper.performAuthorisation(DELETER_USER, Datafile, AccessType.READ, em);
        assertTrue("This should be true", true);
    }

    /**
     * Tests deleter on valid Datafile for delete
     *
     * ACTION_DELETE - N (But no on SET_FA = 'Y')
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testDeleterDeleteOnDatafile() throws ICATAPIException {
        log.info("Testing  user: " + DELETER_USER + " for deleting Datafile is FA Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Datafile Datafile = getDatafile(true);
        try {
            GateKeeper.performAuthorisation(DELETER_USER, Datafile, AccessType.DELETE, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
    }

    /**
     * Tests deleter on valid Datafile for delete
     *
     * ACTION_DELETE - Y (But no on SET_FA = 'Y')
     */
    @Test
    public void testDeleterDeleteOnDatafileNotFA() throws ICATAPIException {
        log.info("Testing  user: " + DELETER_USER + " for deleting Datafile not FA Id: " + VALID_DATASET_ID_FOR_INVESTIGATION_FOR_NOT_FACILITY_ACQURED);
        Datafile Datafile = getDatafileNotFA_Acquired();
        GateKeeper.performAuthorisation(DELETER_USER, Datafile, AccessType.DELETE, em);
        assertTrue("This should be true", true);
    }

    /**
     * Tests deleter on valid Datafile for download
     *
     * ACTION_DOWNLOAD - Y
     */
    @Test
    public void testDeleterDownloadOnDatafile() throws ICATAPIException {
        log.info("Testing  user: " + DELETER_USER + " for download Datafile Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Datafile Datafile = getDatafile(true);
        GateKeeper.performAuthorisation(DELETER_USER, Datafile, AccessType.DOWNLOAD, em);
        assertTrue("This should be true", true);
    }

    /**
     * Tests deleter on valid Datafile for remove (cos Datafile this test remove root)
     *
     * ACTION_REMOVE_ROOT - N
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testDeleterRemoveOnDatafile() throws ICATAPIException {
        log.info("Testing  user: " + DELETER_USER + " for remove Datafile Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Datafile Datafile = getDatafile(true);
        try {
            GateKeeper.performAuthorisation(DELETER_USER, Datafile, AccessType.REMOVE, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
    }

    /**
     * Tests deleter on valid Datafile for update
     *
     * ACTION_UPDATE - N (But no on SET_FA = 'Y')
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testDeleterUpdateOnDatafile() throws ICATAPIException {
        log.info("Testing  user: " + DELETER_USER + " for update Datafile is FA Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Datafile Datafile = getDatafile(true);
        try {
            GateKeeper.performAuthorisation(DELETER_USER, Datafile, AccessType.UPDATE, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
    }

    /**
     * Tests deleter on valid Datafile for update
     *
     * ACTION_UPDATE - Y (But no on SET_FA = 'Y')
     */
    @Test
    public void testDeleterUpdateOnDatafileNotFA() throws ICATAPIException {
        log.info("Testing  user: " + DELETER_USER + " for update Datafile not FA Id: " + VALID_DATASET_ID_FOR_INVESTIGATION_FOR_NOT_FACILITY_ACQURED);
        Datafile Datafile = getDatafileNotFA_Acquired();
        GateKeeper.performAuthorisation(DELETER_USER, Datafile, AccessType.UPDATE, em);
        assertTrue("This should be true", true);
    }

    /**
     * Tests deleter on valid Datafile for insert (cos Datafile this test insert root)
     *
     * ACTION_ROOT_INSERT - N
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testDeleterInsertOnDatafile() throws ICATAPIException {
        log.info("Testing  user: " + DELETER_USER + " for insert on Datafile Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Datafile Datafile = getDatafile(true);
        try {
            GateKeeper.performAuthorisation(DELETER_USER, Datafile, AccessType.CREATE, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
    }

    /**
     * Tests deleter on valid Datafile for insert (cos Datafile this test insert root)
     *
     * ACTION_ROOT_INSERT - Y (set null in inv_id for ICAT_DELETER_USER)
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testDeleterDatafileInsertOnDatafile() throws ICATAPIException {
        log.info("Testing  user: " + DELETER_USER + " for insert on Datafile Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Dataset dataset = em.find(Dataset.class, VALID_DATASET_ID_FOR_INVESTIGATION_FOR_NOT_FACILITY_ACQURED);
        Datafile datafile = getDatafile(false);
        datafile.setDataset(dataset);
        try {
            GateKeeper.performAuthorisation(DELETER_USER, datafile, AccessType.CREATE, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
    }

    /**
     * Tests deleter on valid Datafile for update
     *
     * ACTION_SET_FA - N
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testDeleterSetFAOnDatafile() throws ICATAPIException {
        log.info("Testing  user: " + DELETER_USER + " for set FA on Datafile Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Datafile Datafile = getDatafile(true);
        try {
            GateKeeper.performAuthorisation(DELETER_USER, Datafile, AccessType.SET_FA, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
    }

    /**
     * Tests deleter on valid Datafile for insert keyword
     *
     * ACTION_INSERT - Y
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testDeleterInsertDatafileParameterOnDatafile() throws ICATAPIException {
        log.info("Testing  user: " + DELETER_USER + " for insert DatafileParameter on Datafile Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Datafile dataset = getDatafile(true);
        DatafileParameter dsp = new DatafileParameter();
        dsp.setDatafile(dataset);
        try {
            GateKeeper.performAuthorisation(DELETER_USER, dsp, AccessType.CREATE, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
    }

    /**
     * Tests deleter on valid Datafile for update keyword
     *
     * ACTION_UPDATE - Y
     */
    @Test
    public void testDeleterUpdateDatafileParameterOnDatafile() throws ICATAPIException {
        log.info("Testing  user: " + DELETER_USER + " for update DatafileParameter on Datafile Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Datafile dataset = getDatafile(true);
        DatafileParameter dsp = new DatafileParameter();
        dsp.setDatafile(dataset);
        GateKeeper.performAuthorisation(DELETER_USER, dsp, AccessType.UPDATE, em);
        assertTrue("This should be true", true);
    }

    /**
     * Tests deleter on valid Datafile for update keyword
     *
     * MANAGE_USERS - N
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testDeleterManageUsersOnDatafile() throws ICATAPIException {
        log.info("Testing  user: " + DELETER_USER + " for ManageUsers on Datafile Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Datafile dataset = getDatafile(true);
        try {
            GateKeeper.performAuthorisation(DELETER_USER, dataset, AccessType.MANAGE_USERS, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
    }

    /**
     * Tests deleter on valid Datafile for update keyword
     *
     * ACTION_REMOVE - N
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testDeleterRemoveDatafileParameterOnDatafile() throws ICATAPIException {
        log.info("Testing  user: " + DELETER_USER + " for remove DatafileParameter on Datafile Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Datafile dataset = getDatafile(true);
        DatafileParameter dsp = new DatafileParameter();
        dsp.setDatafile(dataset);
        try {
            GateKeeper.performAuthorisation(DELETER_USER, dsp, AccessType.REMOVE, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
    }

    /**
     * Tests deleter on valid Datafile for update keyword
     *
     * ACTION_REMOVE - N
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testDeleterRemoveDatafileParameterOnDatafile2() throws ICATAPIException {
        log.info("Testing  user: " + DELETER_USER + " for remove DatafileParameter on Datafile Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Datafile dataset = getDatafile(true);
        DatafileParameter dsp = new DatafileParameter();
        dsp.setCreateId(ADMIN_USER);
        dsp.setDatafile(dataset);
        try {
            GateKeeper.performAuthorisation(DELETER_USER, dsp, AccessType.REMOVE, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestGateKeeperDeleterDatafile.class);
    }
}
