package com.frameworkset.common.rowhandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.frameworkset.common.TestNewface;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.NullRowHandler;

/**
 * 
 * <p>Title: PreparedDBUtilRowhandler.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 * @Date 2010-2-7 ����03:50:42
 * @author biaoping.yin
 * @version 1.0
 */
public class PreparedDBUtilRowhandler {

    @Test
    public void testNullRowhandler() {
        PreparedDBUtil dbUtil = new PreparedDBUtil();
        try {
            dbUtil.preparedSelect("select * from testnewface where object_id < ?");
            dbUtil.setInt(1, 100);
            final List<TestNewface> datas = new ArrayList<TestNewface>();
            dbUtil.executePreparedWithRowHandler(new NullRowHandler() {

                public void handleRow(Record record) {
                    TestNewface t = new TestNewface();
                    try {
                        t.setCREATED(record.getDate("created"));
                        t.setDATA_OBJECT_ID(record.getInt("DATA_OBJECT_ID"));
                        datas.add(t);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    System.out.println("row handler:" + t);
                }
            });
            System.out.print("dbUtil.size():" + dbUtil.size());
            for (int i = 0; i < datas.size(); i++) {
                TestNewface testNewface = datas.get(i);
                System.out.println(testNewface);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
	 * ��ҳnullhandler��ʾ
	 */
    @Test
    public void testPageinNullRowhandler() {
        PreparedDBUtil dbUtil = new PreparedDBUtil();
        try {
            dbUtil.preparedSelect("select * from testnewface where object_id < ?", 0, 10);
            dbUtil.setInt(1, 100);
            final List<TestNewface> datas = new ArrayList<TestNewface>();
            dbUtil.executePreparedWithRowHandler(new NullRowHandler() {

                public void handleRow(Record record) {
                    TestNewface t = new TestNewface();
                    try {
                        t.setCREATED(record.getDate("created"));
                        t.setDATA_OBJECT_ID(record.getInt("DATA_OBJECT_ID"));
                        datas.add(t);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    System.out.println("row handler:" + t);
                }
            });
            System.out.print("dbUtil.size():" + dbUtil.size());
            System.out.print("dbUtil.getTotalSize():" + dbUtil.getTotalSize());
            for (int i = 0; i < datas.size(); i++) {
                TestNewface testNewface = datas.get(i);
                System.out.println(testNewface);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
