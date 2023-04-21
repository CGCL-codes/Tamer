package net.sf.passwordpurse;

import java.io.PrintWriter;
import junit.framework.TestCase;
import net.sf.passwordpurse.control.PurseControllerExternalDummy;
import net.sf.passwordpurse.core.SelfDefinedField;
import net.sf.passwordpurse.core.PurseStructuralException;
import net.sf.passwordpurse.core.LengthDefinedFieldParser;
import net.sf.passwordpurse.crypto.CryptoManager;
import net.sf.passwordpurse.crypto.SaltManager;
import net.sf.passwordpurse.crypto.SaltManagerFakeConstant;
import net.sf.passwordpurse.crypto.EncryptedField;
import net.sf.passwordpurse.crypto.EncryptedPurse;
import net.sf.passwordpurse.structural.Secrets;
import net.sf.passwordpurse.structural.Pocket;
import net.sf.passwordpurse.structural.Purse;
import net.sf.passwordpurse.managers.SecretsManager;
import net.sf.passwordpurse.managers.PurseManager;
import net.sf.passwordpurse.control.Watchdog;

public class PurseControllerTest extends TestCase {

    private static final String USERID1 = "abcde";

    private static final String PASSWORD1 = "secret";

    private static final String NOTES1 = "";

    private static final Secrets SECRET1 = new Secrets(USERID1, PASSWORD1, NOTES1);

    private static final String USERID2 = "mickey";

    private static final String PASSWORD2 = "shhh";

    private static final String NOTES2 = "x";

    private static final Secrets SECRET2 = new Secrets(USERID2, PASSWORD2, NOTES2);

    private static final String PASSPHRASE1 = "top secret";

    private static final CryptoManager CRYPTO_MGR;

    private static final SaltManager SALT_MGR;

    private static final String ID1 = "Great";

    private static final Pocket POCKET1;

    private static final String ID2 = "Wonderful";

    private static final Pocket POCKET2;

    private static final Pocket[] NO_POCKETS = new Pocket[0];

    private static final Purse EMPTY_PURSE;

    private static final EncryptedPurse ENCRYPTED_EMPTY_PURSE;

    private static final byte[] EMPTY_PURSE_BYTES;

    private static final int EMPTY_PURSE_LENGTH;

    private static final byte[] EMPTY_RAW_PURSE_BYTES;

    private static final int EMPTY_RAW_PURSE_LENGTH;

    private static final Pocket[] SINGLE_POCKET;

    private static final Purse SINGLE_PURSE;

    private static final EncryptedPurse ENCRYPTED_SINGLE_PURSE;

    private static final byte[] SINGLE_PURSE_BYTES;

    private static final int SINGLE_PURSE_LENGTH;

    private static final byte[] SINGLE_RAW_PURSE_BYTES;

    private static final int SINGLE_RAW_PURSE_LENGTH;

    private static final Pocket[] DOUBLE_POCKETS;

    private static final Purse DOUBLE_PURSE;

    private static final EncryptedPurse ENCRYPTED_DOUBLE_PURSE;

    private static final byte[] DOUBLE_PURSE_BYTES;

    private static final int DOUBLE_PURSE_LENGTH;

    private static final byte[] DOUBLE_RAW_PURSE_BYTES;

    private static final int DOUBLE_RAW_PURSE_LENGTH;

    private static final byte[] FAKE_SALT = { 0x03, 0x13, 0x23, 0x33, 0x43, 0x53, 0x63, 0x73 };

    private static final int SALT_LENGTH = FAKE_SALT.length;

    static {
        SaltManagerFakeConstant.setTestRandom(FAKE_SALT);
        SALT_MGR = new SaltManagerFakeConstant();
        CRYPTO_MGR = new CryptoManager(SALT_MGR, PASSPHRASE1);
        POCKET1 = new Pocket(ID1, CRYPTO_MGR, SECRET1);
        POCKET2 = new Pocket(ID2, CRYPTO_MGR, SECRET2);
        EMPTY_PURSE = new Purse(CRYPTO_MGR, NO_POCKETS);
        ENCRYPTED_EMPTY_PURSE = new EncryptedPurse(CRYPTO_MGR, EMPTY_PURSE);
        EMPTY_PURSE_BYTES = ENCRYPTED_EMPTY_PURSE.getBytesReadonly();
        EMPTY_PURSE_LENGTH = EMPTY_PURSE_BYTES.length;
        EMPTY_RAW_PURSE_LENGTH = SALT_LENGTH + EMPTY_PURSE_LENGTH;
        EMPTY_RAW_PURSE_BYTES = new byte[EMPTY_RAW_PURSE_LENGTH];
        System.arraycopy(FAKE_SALT, 0, EMPTY_RAW_PURSE_BYTES, 0, SALT_LENGTH);
        System.arraycopy(EMPTY_PURSE_BYTES, 0, EMPTY_RAW_PURSE_BYTES, SALT_LENGTH, EMPTY_PURSE_LENGTH);
        SINGLE_POCKET = new Pocket[1];
        SINGLE_POCKET[0] = POCKET1;
        SINGLE_PURSE = new Purse(CRYPTO_MGR, SINGLE_POCKET);
        ENCRYPTED_SINGLE_PURSE = new EncryptedPurse(CRYPTO_MGR, SINGLE_PURSE);
        SINGLE_PURSE_BYTES = ENCRYPTED_SINGLE_PURSE.getBytesReadonly();
        SINGLE_PURSE_LENGTH = SINGLE_PURSE_BYTES.length;
        SINGLE_RAW_PURSE_LENGTH = SALT_LENGTH + SINGLE_PURSE_LENGTH;
        SINGLE_RAW_PURSE_BYTES = new byte[SINGLE_RAW_PURSE_LENGTH];
        System.arraycopy(FAKE_SALT, 0, SINGLE_RAW_PURSE_BYTES, 0, SALT_LENGTH);
        System.arraycopy(SINGLE_PURSE_BYTES, 0, SINGLE_RAW_PURSE_BYTES, SALT_LENGTH, SINGLE_PURSE_LENGTH);
        DOUBLE_POCKETS = new Pocket[2];
        DOUBLE_POCKETS[0] = POCKET1;
        DOUBLE_POCKETS[1] = POCKET2;
        DOUBLE_PURSE = new Purse(CRYPTO_MGR, DOUBLE_POCKETS);
        ENCRYPTED_DOUBLE_PURSE = new EncryptedPurse(CRYPTO_MGR, DOUBLE_PURSE);
        DOUBLE_PURSE_BYTES = ENCRYPTED_DOUBLE_PURSE.getBytesReadonly();
        DOUBLE_PURSE_LENGTH = DOUBLE_PURSE_BYTES.length;
        DOUBLE_RAW_PURSE_LENGTH = SALT_LENGTH + DOUBLE_PURSE_LENGTH;
        DOUBLE_RAW_PURSE_BYTES = new byte[DOUBLE_RAW_PURSE_LENGTH];
        System.arraycopy(FAKE_SALT, 0, DOUBLE_RAW_PURSE_BYTES, 0, SALT_LENGTH);
        System.arraycopy(DOUBLE_PURSE_BYTES, 0, DOUBLE_RAW_PURSE_BYTES, SALT_LENGTH, DOUBLE_PURSE_LENGTH);
    }

    private PrintWriter outWriter = new PrintWriter(System.out, true);

    public PurseControllerTest(String name) {
        super(name);
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(PurseControllerTest.class);
    }

    /**
 * common setup for all tests
 */
    public void setUp() {
        Watchdog.setTestingTimeout(0);
    }

    public void test01simpleOneOK() throws PurseStructuralException {
        PurseControllerExternalDummy controller = new PurseControllerExternalDummy(this.outWriter);
        assertNotNull("should instantiate", controller);
        assertTrue("should not be located yet", !controller.isLocated());
        controller.locate("dontCare");
        assertTrue("should be located now", controller.isLocated());
        assertTrue("should not be loaded yet", !controller.isLoaded());
        controller.load(SINGLE_RAW_PURSE_BYTES, 0, SINGLE_RAW_PURSE_LENGTH);
        assertTrue("should be loaded now", controller.isLoaded());
        assertTrue("should be closed", !controller.isOpen());
        boolean isOpen = controller.open(CRYPTO_MGR);
        assertTrue("should open ok", isOpen);
        assertTrue("should be opened", controller.isOpen());
        controller.close();
        assertTrue("should be closed", !controller.isOpen());
    }

    /**
 * test convenience method to get an empty EncryptedPurse instance
 */
    public void test02getEmpty() throws PurseStructuralException {
        byte[] purseBytes = PurseManager.makeEmptyPurseRaw(CRYPTO_MGR);
        assertNotNull("should not be null", purseBytes);
        PurseControllerExternalDummy controller = new PurseControllerExternalDummy(this.outWriter);
        assertNotNull("should instantiate", controller);
        controller.locate("dontCare");
        controller.load(purseBytes, 0, purseBytes.length);
        boolean isOpen = controller.open(CRYPTO_MGR);
        assertTrue("should open ok", isOpen);
        int count1 = controller.size();
        assertEquals("should be empty", 0, count1);
        controller.close();
        assertTrue("should be closed", !controller.isOpen());
    }

    public void test03badPassphraseOK() throws PurseStructuralException {
        PurseControllerExternalDummy controller = new PurseControllerExternalDummy(this.outWriter);
        assertNotNull("should instantiate", controller);
        controller.locate("dontCare");
        controller.load(SINGLE_RAW_PURSE_BYTES, 0, SINGLE_RAW_PURSE_LENGTH);
        final String PASSPHRASE2 = "Top Secret";
        final CryptoManager BAD_CRYPTO_MGR = new CryptoManager(PASSPHRASE2);
        try {
            boolean isOpen = controller.open(BAD_CRYPTO_MGR);
            fail("Exception expected");
        } catch (Exception e) {
            assertTrue("should throw PurseStructuralException", e instanceof PurseStructuralException);
            String reason = e.getMessage();
            assertTrue("reason expected", (reason.indexOf("field length would overflow array") > -1));
        } finally {
            controller.close();
            assertTrue("should be closed", !controller.isOpen());
        }
    }

    public void test04listOne() throws PurseStructuralException {
        PurseControllerExternalDummy controller = new PurseControllerExternalDummy(this.outWriter);
        assertNotNull("should instantiate", controller);
        controller.locate("dontCare");
        controller.load(SINGLE_RAW_PURSE_BYTES, 0, SINGLE_RAW_PURSE_LENGTH);
        boolean isOpen = controller.open(CRYPTO_MGR);
        assertTrue("should open ok", isOpen);
        Pocket[] contents = controller.list();
        assertNotNull("should list ok", contents);
        assertEquals("should have single Object", 1, contents.length);
        assertTrue("should be Pocket", contents[0] instanceof Pocket);
        Pocket pocket = contents[0];
        assertEquals("should match source", POCKET1, pocket);
        assertEquals("should match pocket id", ID1, pocket.getIdentifier().getPayloadAsString());
        controller.close();
    }

    public void test05containsOK() throws PurseStructuralException {
        PurseControllerExternalDummy controller = new PurseControllerExternalDummy(this.outWriter);
        assertNotNull("should instantiate", controller);
        controller.locate("dontCare");
        controller.load(SINGLE_RAW_PURSE_BYTES, 0, SINGLE_RAW_PURSE_LENGTH);
        boolean isOpen = controller.open(CRYPTO_MGR);
        assertTrue("should open ok", isOpen);
        boolean exists1 = controller.contains(ID1);
        assertTrue("should be found", exists1);
        boolean exists2 = controller.contains(ID2);
        assertTrue("should not be found", !exists2);
        controller.close();
    }

    public void test10listMultiple() throws PurseStructuralException {
        PurseControllerExternalDummy controller = new PurseControllerExternalDummy(this.outWriter);
        assertNotNull("should instantiate", controller);
        controller.locate("dontCare");
        controller.load(DOUBLE_RAW_PURSE_BYTES, 0, DOUBLE_RAW_PURSE_LENGTH);
        boolean isOpen = controller.open(CRYPTO_MGR);
        assertTrue("should open ok", isOpen);
        Pocket[] contents = controller.list();
        assertNotNull("should list ok", contents);
        assertEquals("should have list", DOUBLE_POCKETS.length, contents.length);
        int numFound = 0;
        for (int i = 0; i < DOUBLE_POCKETS.length; i++) {
            assertTrue("should be Pocket", contents[i] instanceof Pocket);
            Pocket pocket = contents[i];
            if (POCKET1.equals(pocket)) numFound++; else if (POCKET2.equals(pocket)) numFound++;
        }
        assertEquals("should find each Pocket", DOUBLE_POCKETS.length, numFound);
        controller.close();
        assertTrue("should be closed", !controller.isOpen());
    }

    public void test11containsMultiple() throws PurseStructuralException {
        PurseControllerExternalDummy controller = new PurseControllerExternalDummy(this.outWriter);
        assertNotNull("should instantiate", controller);
        controller.locate("dontCare");
        controller.load(DOUBLE_RAW_PURSE_BYTES, 0, DOUBLE_RAW_PURSE_LENGTH);
        boolean isOpen = controller.open(CRYPTO_MGR);
        assertTrue("should open ok", isOpen);
        boolean exists1 = controller.contains(ID1);
        assertTrue("should be found", exists1);
        boolean exists2 = controller.contains(ID2);
        assertTrue("should be found", exists2);
        final String ID3 = "unknown";
        boolean exists3 = controller.contains(ID3);
        assertTrue("should not be found", !exists3);
        controller.close();
        assertTrue("should be closed", !controller.isOpen());
    }

    public void test12findInMultiple() throws PurseStructuralException {
        PurseControllerExternalDummy controller = new PurseControllerExternalDummy(this.outWriter);
        assertNotNull("should instantiate", controller);
        controller.locate("dontCare");
        controller.load(DOUBLE_RAW_PURSE_BYTES, 0, DOUBLE_RAW_PURSE_LENGTH);
        boolean isOpen = controller.open(CRYPTO_MGR);
        assertTrue("should open ok", isOpen);
        boolean exists1 = controller.contains(ID1);
        assertTrue("should be found", exists1);
        boolean exists2 = controller.contains(ID2);
        assertTrue("should be found", exists2);
        Pocket pocket = controller.find(ID2);
        assertNotNull("should be found", pocket);
        assertEquals("should find Pocket", POCKET2, pocket);
        controller.close();
        assertTrue("should be closed", !controller.isOpen());
    }

    /**
 * update method tests will automatically force synchronisation of the
 * EncryptedPurse and its contents.
 */
    public void test20replaceOneInMany() throws PurseStructuralException {
        PurseControllerExternalDummy controller = new PurseControllerExternalDummy(this.outWriter);
        assertNotNull("should instantiate", controller);
        controller.locate("dontCare");
        controller.load(DOUBLE_RAW_PURSE_BYTES, 0, DOUBLE_RAW_PURSE_LENGTH);
        boolean isOpen = controller.open(CRYPTO_MGR);
        assertTrue("should open ok", isOpen);
        boolean exists1 = controller.contains(ID1);
        assertTrue("should be found", exists1);
        boolean exists2 = controller.contains(ID2);
        assertTrue("should be found", exists2);
        final Pocket POCKET1a = new Pocket(ID1, CRYPTO_MGR, SECRET2);
        controller.replace(POCKET1a);
        assertTrue("should be changed", controller.isDirty());
        Pocket pocket = controller.find(ID1);
        assertNotNull("should be found", pocket);
        EncryptedField secret = pocket.getEncryptedField();
        LengthDefinedFieldParser parser = new LengthDefinedFieldParser();
        assertNotNull("should instantiate", parser);
        SecretsManager mgr = new SecretsManager(secret, parser);
        assertNotNull("should get encrypted", secret);
        assertTrue("should open ok", mgr.open(CRYPTO_MGR));
        SelfDefinedField[] contents = mgr.list();
        assertNotNull("should list ok", contents);
        assertEquals("should have single Object", 1, contents.length);
        assertTrue("should be Secrets", contents[0] instanceof Secrets);
        Secrets secrets = (Secrets) contents[0];
        assertEquals("should match change", SECRET2, secrets);
        mgr.close();
        controller.close();
        assertTrue("should be closed", !controller.isOpen());
        assertTrue("should NOT be changed", !controller.isDirty());
    }

    public void test21renameOneInMany() throws PurseStructuralException {
        PurseControllerExternalDummy controller = new PurseControllerExternalDummy(this.outWriter);
        assertNotNull("should instantiate", controller);
        controller.locate("dontCare");
        controller.load(DOUBLE_RAW_PURSE_BYTES, 0, DOUBLE_RAW_PURSE_LENGTH);
        boolean isOpen = controller.open(CRYPTO_MGR);
        assertTrue("should open ok", isOpen);
        boolean exists1 = controller.contains(ID1);
        assertTrue("should be found", exists1);
        boolean exists2 = controller.contains(ID2);
        assertTrue("should be found", exists2);
        Pocket pocket = controller.find(ID1);
        assertNotNull("should be found", pocket);
        EncryptedField oldSecret = pocket.getEncryptedField();
        assertNotNull("should be found", oldSecret);
        final String ID3 = "new identity";
        controller.rename(pocket, ID3);
        assertTrue("should be changed", controller.isDirty());
        Pocket newPocket = controller.find(ID3);
        assertNotNull("should be found", newPocket);
        EncryptedField newSecret = newPocket.getEncryptedField();
        assertNotNull("should be found", newSecret);
        assertEquals("should keep old secret", oldSecret, newSecret);
        controller.close();
        assertTrue("should be closed", !controller.isOpen());
    }

    public void test22addOneToEmpty() throws PurseStructuralException {
        PurseControllerExternalDummy controller = new PurseControllerExternalDummy(this.outWriter);
        assertNotNull("should instantiate", controller);
        controller.locate("dontCare");
        controller.load(EMPTY_RAW_PURSE_BYTES, 0, EMPTY_RAW_PURSE_LENGTH);
        boolean isOpen = controller.open(CRYPTO_MGR);
        assertTrue("should open ok", isOpen);
        int count1 = controller.size();
        assertEquals("should be empty", 0, count1);
        controller.replace(POCKET1);
        assertTrue("should be changed", controller.isDirty());
        int count2 = controller.size();
        assertEquals("should not be empty", 1, count2);
        Pocket pocket = controller.find(ID1);
        assertNotNull("should be found", pocket);
        controller.close();
        assertTrue("should be closed", !controller.isOpen());
    }

    public void test23removeOneInMany() throws PurseStructuralException {
        PurseControllerExternalDummy controller = new PurseControllerExternalDummy(this.outWriter);
        assertNotNull("should instantiate", controller);
        controller.locate("dontCare");
        controller.load(DOUBLE_RAW_PURSE_BYTES, 0, DOUBLE_RAW_PURSE_LENGTH);
        boolean isOpen = controller.open(CRYPTO_MGR);
        assertTrue("should open ok", isOpen);
        boolean exists1 = controller.contains(ID1);
        assertTrue("should be found", exists1);
        boolean exists2 = controller.contains(ID2);
        assertTrue("should be found", exists2);
        controller.remove(ID1);
        assertTrue("should be changed", controller.isDirty());
        boolean exists3 = controller.contains(ID1);
        assertTrue("should not be found", !exists3);
        boolean exists4 = controller.contains(ID2);
        assertTrue("should be found", exists4);
        controller.close();
        assertTrue("should be closed", !controller.isOpen());
    }

    public void test30changePassphrase() throws PurseStructuralException {
        PurseControllerExternalDummy controller = new PurseControllerExternalDummy(this.outWriter);
        assertNotNull("should instantiate", controller);
        controller.locate("dontCare");
        controller.load(DOUBLE_RAW_PURSE_BYTES, 0, DOUBLE_RAW_PURSE_LENGTH);
        boolean isOpen = controller.open(CRYPTO_MGR);
        assertTrue("should open ok", isOpen);
        boolean exists1 = controller.contains(ID1);
        assertTrue("should be found", exists1);
        boolean exists2 = controller.contains(ID2);
        assertTrue("should be found", exists2);
        final String NEW_PASSPHRASE = "hush hush";
        final CryptoManager NEW_CRYPTO_MGR = new CryptoManager(NEW_PASSPHRASE);
        controller.setNewPassphrase(NEW_CRYPTO_MGR);
        assertTrue("should not be changed after write", !controller.isDirty());
        boolean exists3 = controller.contains(ID1);
        assertTrue("should be found", exists3);
        boolean exists4 = controller.contains(ID2);
        assertTrue("should be found", exists4);
        controller.close();
    }

    public void test40idleTimeout() throws PurseStructuralException {
        Watchdog.setTestingTimeout(1);
        PurseControllerExternalDummy controller = new PurseControllerExternalDummy(this.outWriter);
        assertNotNull("should instantiate", controller);
        controller.locate("dontCare");
        controller.load(SINGLE_RAW_PURSE_BYTES, 0, SINGLE_RAW_PURSE_LENGTH);
        boolean isOpen = controller.open(CRYPTO_MGR);
        assertTrue("should open ok", isOpen);
        boolean exists1 = controller.contains(ID1);
        assertTrue("should be found", exists1);
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException ignore) {
        }
        assertTrue("should not be open", !controller.isOpen());
        assertTrue("should be timed out and trashed", !controller.isAvailable());
        controller.close();
    }
}
