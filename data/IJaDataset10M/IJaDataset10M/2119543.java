package com.entelience.test.test17command;

import org.junit.*;
import static org.junit.Assert.*;
import com.entelience.test.OurDbTestCase;
import com.entelience.esis.HistoryManager;
import com.entelience.sql.DbHelper;
import com.entelience.util.Config;

public class test02HistoryManager extends OurDbTestCase {

    private static long salt = System.currentTimeMillis();

    @Test
    public void test00_reset() throws Exception {
        core.begin();
        core.executeSql("TRUNCATE e_esiscmd_history");
        core.commit();
    }

    @Test
    public void test01_add_history() throws Exception {
        for (int i = 0; i < 6000; ++i) {
            long salt = System.currentTimeMillis();
            HistoryManager.addCmdHistory(core, "test_" + salt, "args_" + salt);
        }
        assertEquals(5000, DbHelper.countRows(core, "e_esiscmd_history", "public"));
    }

    @Test
    public void test02_trim_history() throws Exception {
        db.disableTx();
        int g = HistoryManager.trimCmdHistory(core);
        assertEquals(0, g);
    }

    @Test
    public void test03_check() throws Exception {
        int delta = Config.getProperty(core, "com.entelience.esis.cmdHistorySize", 5000);
        assertEquals(delta, DbHelper.countRows(core, "e_esiscmd_history", "public"));
    }
}
