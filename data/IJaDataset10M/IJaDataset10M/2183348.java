package com.icbc.webbank;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

public class Account implements SessionBean {

    private static final long serialVersionUID = 1L;

    public void ejbActivate() throws EJBException, RemoteException {
    }

    public void ejbPassivate() throws EJBException, RemoteException {
    }

    public void ejbRemove() throws EJBException, RemoteException {
    }

    public void setSessionContext(SessionContext arg0) throws EJBException, RemoteException {
    }

    public void ejbCreate() throws CreateException {
    }

    private Context ctx = null;

    private Context getInitialContext() throws NamingException {
        if (ctx == null) {
            Hashtable environment = new Hashtable();
            environment.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
            environment.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
            environment.put(Context.PROVIDER_URL, "jnp://localhost:1099");
            ctx = new InitialContext(environment);
        }
        return ctx;
    }

    public Connection getConnection() throws SQLException, NamingException {
        Context context = getInitialContext();
        Object ref = context.lookup("java:/MySqlXADS");
        java.sql.Connection conn = ((javax.sql.DataSource) ref).getConnection();
        return conn;
    }

    public double save(String acctId, double amt) throws RemoteException {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "insert into inf_acct (acct_id, balance) values(?,?)";
        try {
            Context context = getInitialContext();
            Object ref = context.lookup("java:/MySqlXADS");
            conn = ((javax.sql.DataSource) ref).getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, acctId);
            ps.setDouble(2, amt);
            ps.executeUpdate();
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        } catch (NamingException nex) {
            nex.printStackTrace();
        } finally {
            DBUtil.closeResource(ps);
            DBUtil.closeResource(conn);
        }
        return amt;
    }

    private TransactionManager getFromJNDI() throws Exception {
        InitialContext ctx = new InitialContext();
        return (TransactionManager) ctx.lookup("java:/TransactionManager");
    }

    private void callNewTrans(String acctId) throws Exception {
        Context initCtx = null;
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        env.put(Context.PROVIDER_URL, "localhost:1099");
        env.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
        Context ctx = new InitialContext(env);
        Object obj = ctx.lookup("IcbcAccount");
        AccountHome home = (AccountHome) PortableRemoteObject.narrow(obj, AccountHome.class);
        AccountIntf acct = home.create();
        acct.draw("zhang2", 34);
    }

    public void testOther(String acctId, double amt) throws Exception {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "insert into inf_acct (acct_id, balance) values(?,?)";
        try {
            Context context = getInitialContext();
            Object ref = context.lookup("java:/OracleXADS");
            conn = ((javax.sql.DataSource) ref).getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, acctId);
            ps.setDouble(2, amt);
            ps.executeUpdate();
            try {
                TransactionManager tm = getFromJNDI();
                Transaction t = null;
                if (tm != null) {
                    t = tm.getTransaction();
                    System.out.println("====Got Trans In 22: " + t.toString());
                }
            } catch (Exception e) {
                System.out.println("######## ERROR GET TRANSACTION MANAGER 2 ##########");
                e.printStackTrace();
            }
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        } catch (NamingException nex) {
            nex.printStackTrace();
        } finally {
            DBUtil.closeResource(ps);
            DBUtil.closeResource(conn);
        }
    }

    public double draw(String acctId, double amt) throws RemoteException {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "update inf_acct set balance=(balance - ?) where acct_id = ?";
        try {
            conn = getConnection();
            ps = conn.prepareStatement(sql);
            ps.setDouble(1, amt);
            ps.setString(2, acctId);
            ps.executeUpdate();
            try {
                TransactionManager tm = getFromJNDI();
                Transaction t = null;
                if (tm != null) {
                    t = tm.getTransaction();
                    System.out.println("====Got Trans In New Trans: " + t.toString());
                }
            } catch (Exception e) {
                System.out.println("######## ERROR GET TRANSACTION MANAGER 2 ##########");
                e.printStackTrace();
            }
            try {
                System.out.println("---------Prepare call other db... sleep 1 secs");
                Thread.sleep(1000);
                testOther(acctId + "-draw", amt);
            } catch (Exception eeex) {
                eeex.printStackTrace();
            }
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        } catch (NamingException nex) {
            nex.printStackTrace();
        } finally {
            DBUtil.closeResource(ps);
            DBUtil.closeResource(conn);
        }
        return amt;
    }
}
