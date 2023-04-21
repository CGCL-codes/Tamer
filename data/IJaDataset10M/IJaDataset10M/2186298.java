package uk.icat3.security;

import uk.icat3.investigationmanager.*;
import java.util.Random;
import junit.framework.JUnit4TestAdapter;
import org.apache.log4j.Logger;
import uk.icat3.exceptions.ICATAPIException;
import static org.junit.Assert.*;
import org.junit.Test;
import uk.icat3.entity.Investigation;
import uk.icat3.entity.Keyword;
import uk.icat3.exceptions.InsufficientPrivilegesException;
import uk.icat3.util.AccessType;
import uk.icat3.util.BaseTestClassTX;
import static uk.icat3.util.TestConstants.*;

/**
 *
 * @author gjd37
 */
public class TestGateKeeperReaderInvestigation extends TestGateKeeperUtil {

    private static Logger log = Logger.getLogger(TestGateKeeperCreatorInvestigation.class);

    private Random random = new Random();

    /**
     * Tests reader on valid investigation for read
     *
     * ACTION_SELECT  - Y
     */
    @Test
    public void testReaderReadOnInvestigation() throws ICATAPIException {
        log.info("Testing  user: " + READER_USER + " for reading investigation Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Investigation investigation = getInvestigation(true);
        GateKeeper.performAuthorisation(READER_USER, investigation, AccessType.READ, em);
        assertTrue("This should be true", true);
    }

    /**
     * Tests reader on valid investigation for delete
     *
     * ACTION_DELETE - N
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testReaderDeleteOnInvestigation() throws ICATAPIException {
        log.info("Testing  user: " + READER_USER + " for deleting investigation Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Investigation investigation = getInvestigation(true);
        try {
            GateKeeper.performAuthorisation(READER_USER, investigation, AccessType.DELETE, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
    }

    /**
     * Tests Reader on valid investigation for delete
     *
     * ACTION_DELETE - Y (But no on SET_FA = 'Y')
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testReaderDeleteOnInvestigationNotFA() throws ICATAPIException {
        log.info("Testing  user: " + READER_USER + " for deleting investigation not FA Id: " + VALID_INVESTIGATION_ID_FOR_NOT_FACILITY_ACQURED);
        Investigation investigation = getInvestigationNotFA_Acquired();
        try {
            GateKeeper.performAuthorisation(READER_USER, investigation, AccessType.DELETE, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
    }

    /**
     * Tests reader on valid investigation for download
     *
     * ACTION_DOWNLOAD - N
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testReaderDownloadOnInvestigation() throws ICATAPIException {
        log.info("Testing  user: " + READER_USER + " for download investigation Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Investigation investigation = getInvestigation(true);
        try {
            GateKeeper.performAuthorisation(READER_USER, investigation, AccessType.DOWNLOAD, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
    }

    /**
     * Tests reader on valid investigation for remove (cos investigation this test remove root)
     *
     * ACTION_REMOVE_ROOT - N
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testReaderRemoveOnInvestigation() throws ICATAPIException {
        log.info("Testing  user: " + READER_USER + " for remove investigation Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Investigation investigation = getInvestigation(true);
        try {
            GateKeeper.performAuthorisation(READER_USER, investigation, AccessType.REMOVE, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
    }

    /**
     * Tests reader on valid investigation for update
     *
     * ACTION_UPDATE - N (But no on SET_FA = 'Y')
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testReaderUpdateOnInvestigation() throws ICATAPIException {
        log.info("Testing  user: " + READER_USER + " for update investigation Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Investigation investigation = getInvestigation(true);
        try {
            GateKeeper.performAuthorisation(READER_USER, investigation, AccessType.UPDATE, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
    }

    /**
     * Tests admin on valid investigation for update
     *
     * ACTION_UPDATE - N (But no on SET_FA = 'Y')
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testAdminUpdateOnInvestigationNotFA() throws ICATAPIException {
        log.info("Testing  user: " + READER_USER + " for update investigation not FA Id: " + VALID_INVESTIGATION_ID_FOR_NOT_FACILITY_ACQURED);
        Investigation investigation = getInvestigationNotFA_Acquired();
        try {
            GateKeeper.performAuthorisation(READER_USER, investigation, AccessType.UPDATE, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
    }

    /**
     * Tests reader on valid investigation for insert (cos investigation this test insert root)
     *
     * ACTION_ROOT_INSERT - N
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testReaderInsertOnInvestigation() throws ICATAPIException {
        log.info("Testing  user: " + READER_USER + " for insert on investigation Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Investigation investigation = getInvestigation(true);
        try {
            GateKeeper.performAuthorisation(READER_USER, investigation, AccessType.CREATE, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
    }

    /**
     * Tests reader on valid investigation for insert (cos investigation this test insert root)
     *
     * ACTION_ROOT_INSERT - Y (set null in inv_id for ICAT_ADMIN_USER)
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testReaderInvestigationInsertOnInvestigation() throws ICATAPIException {
        log.info("Testing  user: " + READER_USER + " for insert on investigation Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Investigation investigation = getInvestigation(false);
        try {
            GateKeeper.performAuthorisation(READER_USER, investigation, AccessType.CREATE, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
    }

    /**
     * Tests reader on valid investigation for update
     *
     * ACTION_SET_FA - N
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testReaderSetFAOnInvestigation() throws ICATAPIException {
        log.info("Testing  user: " + READER_USER + " for set FA on investigation Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Investigation investigation = getInvestigation(true);
        try {
            GateKeeper.performAuthorisation(READER_USER, investigation, AccessType.SET_FA, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
    }

    /**
     * Tests reader on valid investigation for insert keyword
     *
     * ACTION_INSERT - N
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testReaderInsertKeywordOnInvestigation() throws ICATAPIException {
        log.info("Testing  user: " + READER_USER + " for insert keyword on investigation Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Investigation investigation = getInvestigation(true);
        Keyword keyword = new Keyword();
        keyword.setInvestigation(investigation);
        try {
            GateKeeper.performAuthorisation(READER_USER, keyword, AccessType.CREATE, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
        assertTrue("This should be true", true);
    }

    /**
     * Tests reader on valid investigation for update keyword
     *
     * ACTION_UPDATE - N
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testReaderUpdateKeywordOnInvestigation() throws ICATAPIException {
        log.info("Testing  user: " + READER_USER + " for update keyword on investigation Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Investigation investigation = getInvestigation(true);
        Keyword keyword = new Keyword();
        keyword.setInvestigation(investigation);
        try {
            GateKeeper.performAuthorisation(READER_USER, keyword, AccessType.UPDATE, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
    }

    /**
     * Tests reader on valid investigation for update keyword
     *
     * ACTION_REMOVE - N
     */
    @Test(expected = InsufficientPrivilegesException.class)
    public void testReaderRemoveKeywordOnInvestigation() throws ICATAPIException {
        log.info("Testing  user: " + READER_USER + " for remove keyword on investigation Id: " + VALID_INVESTIGATION_ID_FOR_GATEKEEPER_TEST);
        Investigation investigation = getInvestigation(true);
        Keyword keyword = new Keyword();
        keyword.setInvestigation(investigation);
        try {
            GateKeeper.performAuthorisation(READER_USER, keyword, AccessType.REMOVE, em);
        } catch (InsufficientPrivilegesException ex) {
            log.warn("caught: " + ex.getClass() + " " + ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            throw ex;
        }
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestGateKeeperReaderInvestigation.class);
    }
}
