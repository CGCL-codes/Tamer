package org.signserver.web;

import java.util.HashMap;
import java.util.Map;
import org.signserver.common.CryptoTokenAuthenticationFailureException;
import org.signserver.common.CryptoTokenOfflineException;
import org.signserver.common.InvalidWorkerIdException;

/**
 * Tests that the right HTTP status codes are returned in different situations.
 * 
 * @author Markus Kilås
 * @version $Id: GenericProcessServletResponseTest.java 1836 2011-08-11 10:09:05Z netmackan $
 */
public class GenericProcessServletResponseTest extends WebTestCase {

    private static final String KEYDATA = "KEYDATA";

    @Override
    protected String getServletURL() {
        return "http://localhost:8080/signserver/process";
    }

    /**
     * Sets up a dummy signer.
     * @throws Exception in case of error
     */
    public void test00SetupDatabase() throws Exception {
        addDummySigner1();
    }

    /**
     * Test that a successful request returns status code 200.
     */
    public void test01HttpStatus200() {
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("workerName", getSignerNameDummy1());
        fields.put("data", "<root/>");
        assertStatusReturned(fields, 200);
    }

    /**
     * Test that a bad request returns status code 400.
     * This request misses the "data" field.
     */
    public void test02HttpStatus400_missingField() {
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("workerName", getSignerNameDummy1());
        assertStatusReturned(fields, 400);
    }

    /**
     * Test that a bad request returns status code 400.
     * This request contains an invalid XML document.
     */
    public void test02HttpStatus400_invalidDocument() {
        final String invalidXMLDoc = "<noEndTagToThis>";
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("workerId", String.valueOf(getSignerIdDummy1()));
        fields.put("data", invalidXMLDoc);
        assertStatusReturned(fields, 400);
    }

    /**
     * Test that a bad request returns status code 400.
     * This request contains an unknown encoding property.
     */
    public void test02HttpStatus400_unknownEncoding() {
        final String unknownEncoding = "_unknownEncoding123_";
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("workerId", String.valueOf(getSignerIdDummy1()));
        fields.put("data", "<root/>");
        fields.put("encoding", unknownEncoding);
        assertStatusReturned(fields, 400, true);
    }

    /**
     * Test that a request for non-existing worker returns status code 404.
     */
    public void test03HttpStatus404_nonExistingName() {
        final String nonExistingWorker = "_NotExistingWorker123_";
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("workerName", nonExistingWorker);
        fields.put("data", "<root/>");
        assertStatusReturned(fields, 404);
    }

    /**
     * Test that a request for non-existing worker returns status code 404.
     */
    public void test03HttpStatus404_nonExistingId() {
        final int nonExistingId = 0;
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("workerId", String.valueOf(nonExistingId));
        fields.put("data", "<root/>");
        assertStatusReturned(fields, 404);
    }

    /**
     * Test that when the cryptotoken is offline the status code is 503.
     */
    public void test04HttpStatus503() {
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("workerName", getSignerNameDummy1());
        fields.put("data", "<root/>");
        try {
            try {
                getWorkerSession().deactivateSigner(getSignerIdDummy1());
            } catch (CryptoTokenOfflineException ex) {
                fail(ex.getMessage());
            } catch (InvalidWorkerIdException ex) {
                fail(ex.getMessage());
            }
            assertStatusReturned(fields, 503);
        } finally {
            try {
                getWorkerSession().activateSigner(getSignerIdDummy1(), "");
            } catch (CryptoTokenAuthenticationFailureException ex) {
                fail(ex.getMessage());
            } catch (CryptoTokenOfflineException ex) {
                fail(ex.getMessage());
            } catch (InvalidWorkerIdException ex) {
                fail(ex.getMessage());
            }
        }
    }

    /**
     * Test that when an exception occurs status code 500 is returned.
     */
    public void test05HttpStatus500_exception() {
        Map<String, String> fields = new HashMap<String, String>();
        fields.put("workerName", getSignerNameDummy1());
        fields.put("data", "<root/>");
        final String originalKeyData = getWorkerSession().getCurrentWorkerConfig(getSignerIdDummy1()).getProperty(KEYDATA);
        final String badKeyData = "_any-bad-key-data_";
        getWorkerSession().setWorkerProperty(getSignerIdDummy1(), KEYDATA, badKeyData);
        getWorkerSession().reloadConfiguration(getSignerIdDummy1());
        try {
            assertStatusReturned(fields, 500);
        } finally {
            getWorkerSession().setWorkerProperty(getSignerIdDummy1(), KEYDATA, originalKeyData);
            getWorkerSession().reloadConfiguration(getSignerIdDummy1());
        }
    }

    /**
     * Remove the workers created etc.
     * @throws Exception in case of error
     */
    public void test99TearDownDatabase() throws Exception {
        removeWorker(getSignerIdDummy1());
    }
}
