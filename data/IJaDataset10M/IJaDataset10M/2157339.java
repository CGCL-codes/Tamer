package net.sf.passwordpurse.j2se;

import java.io.PrintWriter;
import junit.framework.TestCase;
import net.sf.passwordpurse.junit.UtilsForTests;
import net.sf.passwordpurse.control.PurseControllerExternal;
import net.sf.passwordpurse.core.PurseStructuralException;
import net.sf.passwordpurse.structural.Secrets;
import net.sf.passwordpurse.structural.Pocket;
import net.sf.passwordpurse.crypto.CryptoManager;
import net.sf.passwordpurse.crypto.SaltManager;

public class PurseControllerFileRealSaltTest extends TestCase {

    private static final String FILENAME = "test.purse";

    private static final String PASSPHRASE1 = "top secret";

    private CryptoManager cryptoManager;

    private static final int SALT_LENGTH = SaltManager.SALT_LENGTH;

    private static final String USERID1 = "abcde";

    private static final String PASSWORD1 = "secret";

    private static final String NOTES1 = "";

    private static final Secrets SECRET1 = new Secrets(USERID1, PASSWORD1, NOTES1);

    private static final String USERID2 = "mickey";

    private static final String PASSWORD2 = "shhh";

    private static final String NOTES2 = "x";

    private static final Secrets SECRET2 = new Secrets(USERID2, PASSWORD2, NOTES2);

    private static final String ID1 = "Great";

    private static final Pocket POCKET1;

    private static final String ID2 = "Wonderful";

    private static final Pocket POCKET2;

    static {
        CryptoManager CRYPTO_MGR = new CryptoManager(PASSPHRASE1);
        POCKET1 = new Pocket(ID1, CRYPTO_MGR, SECRET1);
        POCKET2 = new Pocket(ID2, CRYPTO_MGR, SECRET2);
    }

    private PrintWriter outWriter = new PrintWriter(System.out, true);

    public PurseControllerFileRealSaltTest(String name) {
        super(name);
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(PurseControllerFileRealSaltTest.class);
    }

    /**
 * common setup for all tests
 */
    @Override
    protected void setUp() {
        this.cryptoManager = new CryptoManager(PASSPHRASE1);
    }

    @Override
    protected void tearDown() {
        UtilsForTests.removeScratchBinaryFile(FILENAME);
        this.cryptoManager = null;
    }

    public void test01noPurseYet() throws PurseStructuralException {
        PurseControllerExternal controller = new PurseControllerExternal(this.outWriter);
        assertNotNull("should instantiate", controller);
        boolean found = controller.locate(FILENAME);
        assertTrue("should not exist", !found);
        controller.close();
        assertTrue("should be closed", !controller.isOpen());
    }

    public void test02newEmpty() throws PurseStructuralException {
        PurseControllerExternal controller = new PurseControllerExternal(this.outWriter);
        assertNotNull("should instantiate", controller);
        controller.locate(FILENAME);
        assertTrue("should be located now", controller.isLocated());
        assertTrue("should not exist", !controller.fileExists());
        boolean isOpen = controller.open(this.cryptoManager);
        assertTrue("should open ok", isOpen);
        int count1 = controller.size();
        assertEquals("should be empty", 0, count1);
        controller.close();
        assertTrue("should be closed", !controller.isOpen());
        boolean found = controller.locate(FILENAME);
        assertTrue("should not be saved", !found);
    }

    public void test10createNew() throws PurseStructuralException {
        PurseControllerExternal controller = new PurseControllerExternal(this.outWriter);
        assertNotNull("should instantiate", controller);
        boolean found1 = controller.locate(FILENAME);
        assertTrue("should not exist", !found1);
        boolean isOpen1 = controller.open(this.cryptoManager);
        assertTrue("should open ok", isOpen1);
        int count1 = controller.size();
        assertEquals("should be empty", 0, count1);
        controller.replace(POCKET1);
        int count2 = controller.size();
        assertEquals("should not be empty", 1, count2);
        Pocket pocket1 = controller.find(ID1);
        assertNotNull("should be found", pocket1);
        controller.close();
        assertTrue("should be closed", !controller.isOpen());
        boolean found2 = controller.locate(FILENAME);
        assertTrue("should exist now", found2);
        controller.load();
        boolean isOpen2 = controller.open(this.cryptoManager);
        assertTrue("should open ok", isOpen2);
        int count3 = controller.size();
        assertEquals("should have a Pocket", 1, count3);
        Pocket pocket2 = controller.find(ID1);
        assertNotNull("should be found", pocket2);
        controller.close();
        assertTrue("should be closed", !controller.isOpen());
    }

    public void test11updateLeavingEmpty() throws PurseStructuralException {
        PurseControllerExternal controller = new PurseControllerExternal(this.outWriter);
        assertNotNull("should instantiate", controller);
        boolean found1 = controller.locate(FILENAME);
        assertTrue("should not exist", !found1);
        boolean isOpen1 = controller.open(this.cryptoManager);
        assertTrue("should open ok", isOpen1);
        int count1 = controller.size();
        assertEquals("should be empty", 0, count1);
        controller.replace(POCKET1);
        controller.remove(ID1);
        int count2 = controller.size();
        assertEquals("should be empty", 0, count2);
        Pocket pocket1 = controller.find(ID1);
        assertNull("should not be found", pocket1);
        controller.close();
        assertTrue("should be closed", !controller.isOpen());
        boolean found2 = controller.locate(FILENAME);
        assertTrue("should exist now", found2);
        controller.load();
        boolean isOpen2 = controller.open(this.cryptoManager);
        assertTrue("should open ok", isOpen2);
        int count3 = controller.size();
        assertEquals("should have no Pockets", 0, count3);
        controller.close();
        assertTrue("should be closed", !controller.isOpen());
    }

    public void test12create2() throws PurseStructuralException {
        PurseControllerExternal controller = new PurseControllerExternal(this.outWriter);
        assertNotNull("should instantiate", controller);
        boolean found1 = controller.locate(FILENAME);
        assertTrue("should not exist", !found1);
        boolean isOpen1 = controller.open(this.cryptoManager);
        assertTrue("should open ok", isOpen1);
        int count1 = controller.size();
        assertEquals("should be empty", 0, count1);
        controller.replace(POCKET1);
        controller.replace(POCKET2);
        int count2 = controller.size();
        assertEquals("should have both", 2, count2);
        controller.close();
        assertTrue("should be closed", !controller.isOpen());
        boolean found2 = controller.locate(FILENAME);
        assertTrue("should exist now", found2);
        controller.load();
        boolean isOpen2 = controller.open(this.cryptoManager);
        assertTrue("should open ok", isOpen2);
        Pocket[] contents = controller.list();
        assertNotNull("should list ok", contents);
        assertEquals("should have list", count2, contents.length);
        int numFound = 0;
        for (int i = 0; i < count2; i++) {
            assertTrue("should be Pocket", contents[i] instanceof Pocket);
            Pocket pocket = contents[i];
            if (POCKET1.equals(pocket)) numFound++; else if (POCKET2.equals(pocket)) numFound++;
        }
        assertEquals("should find each Pocket", count2, numFound);
        controller.close();
        assertTrue("should be closed", !controller.isOpen());
    }

    public void test20changePassphrase() throws PurseStructuralException {
        PurseControllerExternal controller = new PurseControllerExternal(this.outWriter);
        assertNotNull("should instantiate", controller);
        boolean found1 = controller.locate(FILENAME);
        assertTrue("should not exist", !found1);
        boolean isOpen1 = controller.open(this.cryptoManager);
        assertTrue("should open ok", isOpen1);
        controller.replace(POCKET1);
        controller.close();
        assertTrue("should be closed", !controller.isOpen());
        byte[] firstPurse = UtilsForTests.readBinaryFile(FILENAME);
        assertNotNull("should get first purse", firstPurse);
        boolean found2 = controller.locate(FILENAME);
        assertTrue("should exist now", found2);
        controller.load();
        boolean isOpen2 = controller.open(this.cryptoManager);
        assertTrue("should open ok", isOpen2);
        final String NEW_PASSPHRASE = "hush hush hush hush";
        final CryptoManager newCryptoMgr = new CryptoManager(NEW_PASSPHRASE);
        controller.setNewPassphrase(newCryptoMgr);
        assertTrue("should not be changed after write", !controller.isDirty());
        controller.close();
        byte[] secondPurse = UtilsForTests.readBinaryFile(FILENAME);
        assertNotNull("should get second purse", secondPurse);
        assertEquals("files should be same length", firstPurse.length, secondPurse.length);
        UtilsForTests.matchBytes("salts should be different", firstPurse, secondPurse, 0, firstPurse.length, false);
        UtilsForTests.matchBytes("payloads should be different", firstPurse, secondPurse, SALT_LENGTH, (firstPurse.length - SALT_LENGTH), false);
        boolean found3 = controller.locate(FILENAME);
        assertTrue("should exist still", found3);
        controller.load();
        boolean isOpen3 = controller.open(newCryptoMgr);
        assertTrue("should open ok", isOpen3);
        int count = controller.size();
        assertEquals("should have one pocket", 1, count);
        Pocket pocket = controller.find(ID1);
        assertNotNull("should find my pocket", pocket);
        controller.close();
    }

    public void test30changePassphraseQuality() throws PurseStructuralException {
        PurseControllerExternal controller = new PurseControllerExternal(this.outWriter);
        assertNotNull("should instantiate", controller);
        boolean found1 = controller.locate(FILENAME);
        assertTrue("should not exist", !found1);
        boolean isOpen1 = controller.open(this.cryptoManager);
        assertTrue("should open ok", isOpen1);
        controller.replace(POCKET1);
        controller.replace(POCKET2);
        controller.close();
        assertTrue("should be closed", !controller.isOpen());
        byte[] firstPurse = UtilsForTests.readBinaryFile(FILENAME);
        assertNotNull("should get first purse", firstPurse);
        boolean found2 = controller.locate(FILENAME);
        assertTrue("should exist now", found2);
        controller.load();
        boolean isOpen2 = controller.open(this.cryptoManager);
        assertTrue("should open ok", isOpen2);
        final String NEW_PASSPHRASE = "hush hush hush hush";
        final CryptoManager newCryptoMgr = new CryptoManager(NEW_PASSPHRASE);
        controller.setNewPassphrase(newCryptoMgr);
        assertTrue("should not be changed after write", !controller.isDirty());
        controller.close();
        byte[] secondPurse = UtilsForTests.readBinaryFile(FILENAME);
        assertNotNull("should get second purse", secondPurse);
        assertEquals("files should be same length", firstPurse.length, secondPurse.length);
        UtilsForTests.matchBytes("salts should be different", firstPurse, secondPurse, 0, firstPurse.length, false);
        UtilsForTests.matchBytes("payloads should be different", firstPurse, secondPurse, SALT_LENGTH, (firstPurse.length - SALT_LENGTH), false);
        UtilsForTests.evaluateMismatchBytes("encrypted payloads should be scrambled", firstPurse, secondPurse, SALT_LENGTH, (firstPurse.length - SALT_LENGTH), 95);
        boolean found3 = controller.locate(FILENAME);
        assertTrue("should still exist", found3);
        controller.load();
        boolean isOpen3 = controller.open(newCryptoMgr);
        assertTrue("should open ok", isOpen3);
        Pocket[] contents = controller.list();
        assertNotNull("should list ok", contents);
        assertEquals("should have two pockets", 2, contents.length);
        Pocket pocket1 = controller.find(ID1);
        assertNotNull("should find 1st pocket", pocket1);
        Pocket pocket2 = controller.find(ID2);
        assertNotNull("should find 2nd pocket", pocket2);
        controller.close();
    }

    public void test31changePocketQuality() throws PurseStructuralException {
        PurseControllerExternal controller = new PurseControllerExternal(this.outWriter);
        assertNotNull("should instantiate", controller);
        boolean found1 = controller.locate(FILENAME);
        assertTrue("should not exist", !found1);
        boolean isOpen1 = controller.open(this.cryptoManager);
        assertTrue("should open ok", isOpen1);
        controller.replace(POCKET1);
        controller.replace(POCKET2);
        controller.close();
        assertTrue("should be closed", !controller.isOpen());
        byte[] firstPurse = UtilsForTests.readBinaryFile(FILENAME);
        assertNotNull("should get first purse", firstPurse);
        boolean found2 = controller.locate(FILENAME);
        assertTrue("should exist now", found2);
        controller.load();
        boolean isOpen2 = controller.open(this.cryptoManager);
        assertTrue("should open ok", isOpen2);
        String NEW_ID = "Greet";
        controller.rename(POCKET1, NEW_ID);
        assertTrue("should be changed after write", controller.isDirty());
        controller.close();
        byte[] secondPurse = UtilsForTests.readBinaryFile(FILENAME);
        assertNotNull("should get second purse", secondPurse);
        assertEquals("files should be same length", firstPurse.length, secondPurse.length);
        UtilsForTests.matchBytes("salts should be different", firstPurse, secondPurse, 0, firstPurse.length, false);
        UtilsForTests.matchBytes("payloads should be different", firstPurse, secondPurse, SALT_LENGTH, (firstPurse.length - SALT_LENGTH), false);
        UtilsForTests.evaluateMismatchBytes("encrypted payloads should be scrambled", firstPurse, secondPurse, SALT_LENGTH, (firstPurse.length - SALT_LENGTH), 95);
        boolean found3 = controller.locate(FILENAME);
        assertTrue("should still exist", found3);
        controller.load();
        boolean isOpen3 = controller.open(this.cryptoManager);
        assertTrue("should open ok", isOpen3);
        Pocket[] contents = controller.list();
        assertNotNull("should list ok", contents);
        assertEquals("should have two pockets", 2, contents.length);
        Pocket pocket1 = controller.find(ID1);
        assertNull("should NOT find 1st pocket", pocket1);
        Pocket pocket2 = controller.find(ID2);
        assertNotNull("should find 2nd pocket", pocket2);
        Pocket pocket3 = controller.find(NEW_ID);
        assertNotNull("should find renamed pocket", pocket3);
        controller.close();
    }

    public void test32changePocketLengthQuality() throws PurseStructuralException {
        PurseControllerExternal controller = new PurseControllerExternal(this.outWriter);
        assertNotNull("should instantiate", controller);
        boolean found1 = controller.locate(FILENAME);
        assertTrue("should not exist", !found1);
        boolean isOpen1 = controller.open(this.cryptoManager);
        assertTrue("should open ok", isOpen1);
        controller.replace(POCKET1);
        controller.replace(POCKET2);
        controller.close();
        assertTrue("should be closed", !controller.isOpen());
        byte[] firstPurse = UtilsForTests.readBinaryFile(FILENAME);
        assertNotNull("should get first purse", firstPurse);
        boolean found2 = controller.locate(FILENAME);
        assertTrue("should exist now", found2);
        controller.load();
        boolean isOpen2 = controller.open(this.cryptoManager);
        assertTrue("should open ok", isOpen2);
        String NEW_ID = "Grat";
        controller.rename(POCKET1, NEW_ID);
        assertTrue("should be changed after write", controller.isDirty());
        controller.close();
        byte[] secondPurse = UtilsForTests.readBinaryFile(FILENAME);
        assertNotNull("should get second purse", secondPurse);
        assertEquals("files should be same length", firstPurse.length, secondPurse.length);
        UtilsForTests.matchBytes("salts should be different", firstPurse, secondPurse, 0, firstPurse.length, false);
        UtilsForTests.matchBytes("payloads should be different", firstPurse, secondPurse, SALT_LENGTH, (firstPurse.length - SALT_LENGTH), false);
        UtilsForTests.evaluateMismatchBytes("encrypted payloads should be scrambled", firstPurse, secondPurse, SALT_LENGTH, (firstPurse.length - SALT_LENGTH), 97);
        boolean found3 = controller.locate(FILENAME);
        assertTrue("should still exist", found3);
        controller.load();
        boolean isOpen3 = controller.open(this.cryptoManager);
        assertTrue("should open ok", isOpen3);
        Pocket[] contents = controller.list();
        assertNotNull("should list ok", contents);
        assertEquals("should have two pockets", 2, contents.length);
        Pocket pocket1 = controller.find(ID1);
        assertNull("should NOT find 1st pocket", pocket1);
        Pocket pocket2 = controller.find(ID2);
        assertNotNull("should find 2nd pocket", pocket2);
        Pocket pocket3 = controller.find(NEW_ID);
        assertNotNull("should find renamed pocket", pocket3);
        controller.close();
    }
}
