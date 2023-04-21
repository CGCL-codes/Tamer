package net.openchrom.keystore.internal.support;

import java.io.File;
import java.util.Map;
import net.openchrom.keystore.TestPathHelper;
import junit.framework.TestCase;

public class KeyFileParser_3_ITest extends TestCase {

    private Map<String, String> keyStore;

    @Override
    protected void setUp() throws Exception {
        File file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_KEYSTORE_I_TEST) + "-non-existant");
        keyStore = KeyFileParser.readKeysFromFile(file);
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAmount() {
        assertEquals(0, keyStore.size());
    }

    public void testContainsKey_1() {
        assertFalse(keyStore.containsKey("mykeyid"));
    }

    public void testGetKey_1() {
        assertNull(keyStore.get("mykeyid"));
    }
}
