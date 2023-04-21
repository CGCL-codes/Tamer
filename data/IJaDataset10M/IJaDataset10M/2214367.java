package com.xy.sframe.test;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import com.xy.sframe.component.config.ConfigProperty;
import com.xy.sframe.component.datawindow.DataWindow;
import com.xy.sframe.component.datawindow.LogContext;
import com.xy.sframe.component.datawindow.ORConnection;
import com.xy.sframe.component.datawindow.UserTransaction;
import com.xy.sframe.component.log.LoggerFactory;
import com.xy.sframe.component.util.SqlUtil;

/**
 * @author luwenpeng
 * ���Ի� ���ڵ�����Ա���
 */
public class TestCase {

    public static void main(String[] args) throws Exception {
        ConfigProperty.config();
        LogContext.getInstance().setDwLog(LoggerFactory.getDefaultLog("mmm"));
        UserTransaction ut = new UserTransaction("SS1111");
        ut.begin();
        try {
            SqlUtil su = new SqlUtil();
            su.setPoolName("reframe");
            su.setTableName("syslog");
            su.setValue("hwspno", "0001");
            su.setValue("optrno", "000001");
            su.setValue("hoptdt", "20079801");
            su.setValue("hopttm", "000001");
            su.setValue("hsvcnm", "test");
            su.setValue("optdsc", "redata test");
            DataWindow.executeSql("reframe", su.getInsertSql());
            ut.commit();
        } catch (Exception e) {
            ut.rollback();
            e.printStackTrace();
        }
        DataWindow dw = DataWindow.executeSql("reframe", "select * from syslog where optdsc like 'redata%'");
        System.out.println(dw.toXML().toString());
    }
}
