package com.pinae.priderock.jdbc.test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import com.pinae.hm.kernel.context.FileSystemResourceContext;
import com.pinae.hm.kernel.context.ResourceContext;
import com.pinae.priderock.jdbc.JdbcTemplate;
import junit.framework.TestCase;

/**
 * @author ��������
 *
 */
public class JdbcTemplateTest extends TestCase {

    private String strSQL = "select * from users";

    /**
	 * ��com.pinae.hm.beanfactory.xml.XmlBeanFactory.getBean(String)���Ĳ��Է���
	 */
    public void testGetBean() {
        long a = System.currentTimeMillis();
        ResourceContext bean = new FileSystemResourceContext("testJdbc.xml");
        JdbcTemplate obj = (JdbcTemplate) bean.getBean("jdbcTemplate");
        Connection conn = obj.getConnection();
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(strSQL);
            ResultSetMetaData mdrs = rs.getMetaData();
            int ColNum = mdrs.getColumnCount();
            for (int i = 1; i <= ColNum; i++) {
                System.out.print(mdrs.getColumnName(i) + "\t");
            }
            System.out.println("\n" + "*************************************************");
            while (rs.next()) {
                String strData = "";
                for (int i = 1; i <= ColNum; i++) strData = strData + rs.getString(i) + "\t";
                System.out.println(strData);
            }
            System.out.println(System.currentTimeMillis() - a);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void testStore() {
        long a = System.currentTimeMillis();
        ResourceContext bean = new FileSystemResourceContext("testJdbc.xml");
        JdbcTemplate obj = (JdbcTemplate) bean.getBean("jdbcTemplate");
        Connection conn = obj.getConnection();
        try {
            CallableStatement proc = conn.prepareCall("{ CALL insert_data(?,?,?) }");
            proc.setString(1, "ququ");
            proc.setInt(2, 22);
            proc.setInt(3, 0);
            proc.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() - a);
    }
}
