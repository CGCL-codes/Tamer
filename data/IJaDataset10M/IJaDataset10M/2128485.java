package org.hip.kernel.bom.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.io.File;
import java.sql.Blob;
import java.sql.SQLException;
import org.hip.kernel.bom.BOMException;
import org.hip.kernel.exc.VException;
import org.hip.kernel.sys.VSys;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class BlobHomeImplTest {

    private static DataHouseKeeper data;

    private static TestBlobHome home;

    @BeforeClass
    public static void init() {
        data = DataHouseKeeper.getInstance();
        home = (TestBlobHome) VSys.homeManager.getHome(TestBlobImpl.HOME_CLASS_NAME);
    }

    @Before
    public void tearDown() throws Exception {
        data.deleteAllFrom("tblBlobTest");
    }

    @Test
    public void testCreate() throws BOMException, VException, SQLException {
        assertEquals("count 0", 0, home.getCount());
        TestBlobImpl lModel = (TestBlobImpl) home.create();
        lModel.set(TestBlobHome.KEY_NAME, "Content");
        lModel.insert(true);
        assertEquals("count 1", 1, home.getCount());
    }

    @Test
    public void testNew() throws VException, SQLException {
        File lFile = new File("tools.gif");
        TestBlobImpl lModel = home.ucNew("Content", lFile);
        assertNotNull("new test blob model", lModel);
        Blob lBlob = (Blob) lModel.get(TestBlobHome.KEY_XVALUE);
        assertNotNull("Blob returned", lBlob);
        assertEquals("length of blob", lFile.length(), lBlob.length());
    }
}
