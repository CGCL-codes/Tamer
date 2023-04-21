package com.frameworkset.db2;

import java.sql.SQLException;
import org.junit.Test;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.sql.TableMetaData;

public class TestDB2 {

    @Test
    public void testPagine() {
        PreparedDBUtil dbutil = new PreparedDBUtil();
        try {
            dbutil.preparedSelect("select * from td_sm_log", 11, 20);
            dbutil.executePrepared();
            System.out.println(dbutil.size());
            System.out.println(dbutil.getString(1, "log_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSelect() {
        PreparedDBUtil dbutil = new PreparedDBUtil();
        try {
            dbutil.executeSelect("select * from td_sm_log", 11, 20);
            System.out.println(dbutil.size());
            System.out.println(dbutil.getString(1, "log_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMeta() {
        TableMetaData meta = DBUtil.getTableMetaData("bspf", "tableinfo");
        System.out.println(meta);
    }
}
