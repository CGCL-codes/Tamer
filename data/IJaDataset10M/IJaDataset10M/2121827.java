package com.fddtool.si.database;

import java.util.Enumeration;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.RefAddr;
import javax.naming.Reference;
import javax.naming.spi.ObjectFactory;
import org.enhydra.jdbc.pool.StandardXAPoolDataSource;
import org.enhydra.jdbc.standard.StandardXADataSource;
import org.objectweb.jotm.Jotm;
import org.objectweb.transaction.jta.TMService;

/**
 * This is a factory that can create datasources when they are looked up
 * via JNDI.
 *
 * This file originally is part of objectweb datasource.
 * It is modified to support more parameters. Namely, I am interested in
 * configuring the datasource so that is supports connection testing.
 *
 * @author jmesnil, Serguei Khramtchenko
 */
public class JndiDatasourceFactory implements ObjectFactory {

    private static Hashtable<StandardXADataSource, StandardXAPoolDataSource> table = new Hashtable<StandardXADataSource, StandardXAPoolDataSource>();

    public static TMService jotm;

    static {
        try {
            jotm = new Jotm(true, false);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    /**
    * @see javax.naming.spi.ObjectFactory#getObjectInstance(Object, Name, Context, Hashtable)
    */
    public Object getObjectInstance(Object obj, Name n, Context nameCtx, Hashtable environment) throws Exception {
        StandardXAPoolDataSource xads = null;
        StandardXADataSource ds = null;
        try {
            Reference ref = (Reference) obj;
            ds = new StandardXADataSource();
            xads = new StandardXAPoolDataSource(ds);
            Enumeration<RefAddr> addrs = ref.getAll();
            while (addrs.hasMoreElements()) {
                RefAddr addr = addrs.nextElement();
                String name = addr.getType();
                String value = (String) addr.getContent();
                if (name.equals("driverClassName")) {
                    ds.setDriverName(value);
                } else if (name.equals("url")) {
                    ds.setUrl(value);
                } else if (name.equals("username")) {
                    xads.user = value;
                    ds.setUser(value);
                } else if (name.equals("password")) {
                    ds.setPassword(value);
                    xads.password = value;
                } else if (name.equals("min")) {
                    try {
                        int min = Integer.parseInt(value);
                        xads.setMinSize(min);
                    } catch (NumberFormatException e) {
                    }
                } else if (name.equals("max")) {
                    try {
                        int max = Integer.parseInt(value);
                        xads.setMaxSize(max);
                    } catch (NumberFormatException e) {
                    }
                } else if (name.equals("lifeTime")) {
                    try {
                        int lifeTime = Integer.parseInt(value);
                        xads.setLifeTime(lifeTime);
                    } catch (NumberFormatException e) {
                    }
                } else if (name.equals("sleepTime")) {
                    try {
                        int sleepTime = Integer.parseInt(value);
                        xads.setSleepTime(sleepTime);
                    } catch (NumberFormatException e) {
                    }
                } else if (name.equals("validationQuery")) {
                    if (xads.getCheckLevelObject() < 2) {
                        xads.setCheckLevelObject(2);
                    }
                    xads.setJdbcTestStmt(value);
                } else if (name.equals("validationLevel")) {
                    try {
                        int level = Integer.parseInt(value);
                        xads.setCheckLevelObject(level);
                    } catch (NumberFormatException e) {
                    }
                }
            }
            xads.setTransactionManager(jotm.getTransactionManager());
            xads.setDataSource(ds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (table.containsKey(ds)) {
            return table.get(ds);
        } else {
            table.put(ds, xads);
            return xads;
        }
    }
}
