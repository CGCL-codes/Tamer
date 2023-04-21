package com.mymail.test;

import java.sql.Connection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import com.mymail.factory.TomcatPools;

public class DataTest {

    public void test() {
        Test t = new Test(TomcatPools.getConnection());
        t.displayAll();
        t.close();
    }
}
