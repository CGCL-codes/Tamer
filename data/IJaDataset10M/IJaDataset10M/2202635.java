package org.carp.test.query;

import org.carp.CarpDataSetQuery;
import org.carp.CarpSession;
import org.carp.CarpSessionBuilder;
import org.carp.DataSet;
import org.carp.cfg.CarpConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DatasetQueryCase {

    CarpConfig config = null;

    ;

    CarpSessionBuilder builder;

    CarpSession s = null;

    @Before
    public void setUp() throws Exception {
        config = new CarpConfig();
        builder = config.getSessionBuilder();
        s = builder.getSession();
    }

    @After
    public void setDown() throws Exception {
        s.close();
    }

    @Test
    public void query() throws Exception {
        CarpDataSetQuery q = s.creatDataSetQuery("select * from ss_sys_perm where perm_id > ?");
        q.setFirstIndex(0);
        q.setMaxCount(10);
        q.setInt(1, 10);
        DataSet ds = q.dataSet();
        System.out.println(ds.count());
        while (ds.next()) System.out.println(ds.getData("perm_id") + " ---  " + ds.getData("perm_name"));
    }

    @Test
    public void querySql() throws Exception {
        CarpDataSetQuery q = s.creatDataSetQuery("select * from ss_sys_perm ");
        DataSet ds = q.dataSet();
        System.out.println(ds.count());
        while (ds.next()) System.out.println(ds.getData("perm_name"));
    }
}
