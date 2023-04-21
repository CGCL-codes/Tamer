package com.smb.MMUtil.testcase;

import java.sql.Connection;
import java.sql.DriverManager;
import org.junit.Test;

public class JDBCTestCase {

    @Test
    public void createPROCEDURE() throws Exception {
        Connection connection = null;
        String pSQL = "CREATE DEFINER=`root`@`localhost` PROCEDURE `pl`()	select id,level from crm_users order by id-level desc;";
        Class.forName("com.mysql.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1/test", "root", "123456");
        connection.createStatement().execute(pSQL);
    }
}
