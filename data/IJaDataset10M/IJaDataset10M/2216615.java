package com.frameworkset.derby;

import java.sql.SQLException;
import com.frameworkset.common.poolman.DBUtil;

public class TestUUIDPrimarykey {

    @org.junit.Test
    public void test() {
        try {
            System.out.println(DBUtil.getNextStringPrimaryKey("derby", "CIM_DATACACHE"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
