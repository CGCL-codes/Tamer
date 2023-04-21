package org.datascooter.test;

import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.datascooter.DataScooter;
import org.datascooter.exception.EntityNotMappedException;
import org.datascooter.exception.SnipManagerException;
import org.datascooter.extension.DatascooterStarter;
import org.datascooter.test.example.ContextTestClassInt;
import org.datascooter.utils.LangUtils;
import org.datascooter.utils.policy.BackupPolicy;
import org.datascooter.utils.policy.RestorePolicy;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class BackupTest {

    private static final int TESTCOUNT = 999;

    private static final String TEST_BACK_SQL = "test_back.sql";

    private static final String TEST_BACK_BACK = "test_back.back";

    private static File file;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        String userDir = System.getProperty("user.dir");
        file = new File(userDir, "backuptest");
        if (!file.exists()) {
            file.mkdirs();
        } else {
            LangUtils.clearDir(file);
        }
        DatascooterStarter.startH2DefaultPath(file.getPath());
        DataScooter.getDefault().dropAll("", true);
        DataScooter.getDefault().verifyTables();
        List<ContextTestClassInt> list = new ArrayList<ContextTestClassInt>();
        for (int a = 0; a < TESTCOUNT; a++) {
            list.add(new ContextTestClassInt(500, null));
        }
        DataScooter.getDefault().batchSave(list);
    }

    @Test
    public void testAll() throws EntityNotMappedException, SQLException, IOException, SnipManagerException {
        assertTrue(DataScooter.getDefault().count(ContextTestClassInt.class) == TESTCOUNT);
        DataScooter.getDefault().getBackup().backup("", file.getPath(), TEST_BACK_BACK, BackupPolicy.ALL_TABLES, true);
        DataScooter.getDefault().getBackup().backupToSql("", file.getPath(), TEST_BACK_SQL, BackupPolicy.ALL_TABLES, true);
        DataScooter.getDefault().dropAll(null, true);
        assertTrue(!DataScooter.getDefault().tableExists(ContextTestClassInt.class));
        DataScooter.getDefault().getBackup().restore("", file.getPath(), TEST_BACK_BACK, RestorePolicy.TRY_TO_CREATE, true);
        assertTrue(DataScooter.getDefault().tableExists(ContextTestClassInt.class));
        assertTrue(DataScooter.getDefault().count(ContextTestClassInt.class) == TESTCOUNT);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        DataScooter.getDefault().dropAll("", true);
        DataScooter.getDefault().close();
    }
}
