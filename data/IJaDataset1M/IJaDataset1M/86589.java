package confreg.ejb.tests.organizers;

import confreg.ejb.controls.AdminControlRemote;
import confreg.ejb.controls.RegistrationControlRemote;
import confreg.ejb.domain.ConferencePack;
import confreg.ejb.domain.ConferencePackStatus;
import confreg.ejb.domain.User;
import confreg.ejb.exceptions.NonExistingConferencePackException;
import confreg.ejb.exceptions.NonExistingUserException;
import confreg.ejb.exceptions.ValidationException;
import confreg.ejb.tests.TestBase;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
public class AdminControlTest extends TestBase {

    public static AdminControlRemote adminControl;

    public static RegistrationControlRemote regControl;

    public static Logger logger = Logger.getLogger(AdminControlTest.class.getName());

    private ConferencePack originalConfPack;

    private User user;

    private String userEmail = "joska@pista.com";

    public AdminControlTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
        loginAsOrganizer();
        originalConfPack = regControl.getConferencePack(userEmail);
    }

    @After
    public void tearDown() throws Exception {
        user = regControl.findUserByEmail(userEmail);
        if (originalConfPack != null) {
            regControl.modifyConferencePack(userEmail, originalConfPack);
        }
        logout();
    }

    @Test
    public void testConfirmPayment() throws Exception {
        ConferencePack cP = regControl.getConferencePack(userEmail);
        cP.setStatus(ConferencePackStatus.PAYING);
        regControl.modifyConferencePack(userEmail, cP);
        adminControl.confirmPayment(userEmail);
        ConferencePackStatus st = ConferencePackStatus.PAYING;
        cP = regControl.getConferencePack(userEmail);
        if (cP == null) {
            fail("Conferencepack does not exist!");
        } else {
            st = cP.getStatus();
            if (st != ConferencePackStatus.CONFIRMED) {
                fail("Confirmation failed, status: " + st);
            }
        }
    }

    @Test
    public void testConfirmPayment1() throws Exception {
        ConferencePack cP = regControl.getConferencePack(userEmail);
        cP.setStatus(ConferencePackStatus.MODIFYING);
        regControl.modifyConferencePack(userEmail, cP);
        try {
            adminControl.confirmPayment(userEmail);
            fail("Payment confirmed, but status wasn't PAYING.");
        } catch (NonExistingUserException e) {
        } catch (NonExistingConferencePackException e) {
        } catch (ValidationException e) {
        }
    }

    @Test
    public void testRetractPayment() {
        try {
            ConferencePack cP = regControl.getConferencePack(userEmail);
            cP.setStatus(ConferencePackStatus.PAYING);
            try {
                regControl.modifyConferencePack(userEmail, cP);
            } catch (ValidationException e) {
            } catch (NonExistingUserException e) {
            }
        } catch (NonExistingUserException e) {
        }
        try {
            adminControl.retractPayment(userEmail);
        } catch (NonExistingUserException e) {
        } catch (NonExistingConferencePackException e) {
        } catch (ValidationException e) {
        }
        ConferencePackStatus st = ConferencePackStatus.PAYING;
        ConferencePack cP = null;
        try {
            cP = regControl.getConferencePack(userEmail);
        } catch (NonExistingUserException e) {
        }
        if (cP == null) {
            fail("Conferencepack does not exist!");
        } else {
            st = cP.getStatus();
            if (st != ConferencePackStatus.MODIFYING) {
                fail("Retraction failed.");
            }
        }
    }

    @Test
    public void testRetractPayment1() {
        try {
            ConferencePack cP = regControl.getConferencePack(userEmail);
            cP.setStatus(ConferencePackStatus.CONFIRMED);
            try {
                regControl.modifyConferencePack(userEmail, cP);
            } catch (ValidationException e) {
            } catch (NonExistingUserException e) {
            }
        } catch (NonExistingUserException e) {
        }
        try {
            adminControl.retractPayment(userEmail);
            fail("Payment retracted, but status wasn't PAYING.");
        } catch (NonExistingUserException e) {
        } catch (NonExistingConferencePackException e) {
        } catch (ValidationException e) {
        }
    }

    @Test
    public void testCancelConfPack() {
        try {
            ConferencePack cP = regControl.getConferencePack(userEmail);
            cP.setStatus(ConferencePackStatus.PAYING);
            try {
                regControl.modifyConferencePack(userEmail, cP);
            } catch (ValidationException e) {
            } catch (NonExistingUserException e) {
            }
        } catch (NonExistingUserException e) {
        }
        try {
            adminControl.cancelConferencePack(userEmail);
        } catch (NonExistingUserException e) {
        }
        ConferencePackStatus st = ConferencePackStatus.PAYING;
        ConferencePack cP = null;
        try {
            cP = regControl.getConferencePack(userEmail);
        } catch (NonExistingUserException e) {
        }
        if (cP == null) {
            fail("Conferencpack does not exist!");
        } else {
            st = cP.getStatus();
            if (st != ConferencePackStatus.CANCELLED) {
                fail("Cancel failed, status: " + st);
            }
        }
    }

    @Test
    public void alwaysFail() {
        fail("Fails to show the output!");
    }

    @Test(expected = ValidationException.class)
    public void testModifyDiscount() throws Exception {
        user = regControl.findUserByEmail(userEmail);
        adminControl.modifyDiscount(userEmail, -10, "bad discount");
        fail("Discount validation error!");
    }
}
