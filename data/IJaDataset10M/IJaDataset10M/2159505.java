package com.mchange.v2.c3p0;

import java.util.*;
import com.mchange.v2.coalesce.*;
import com.mchange.v2.log.*;
import com.mchange.v2.c3p0.cfg.C3P0ConfigUtils;
import com.mchange.v2.c3p0.impl.*;
import java.sql.SQLException;
import com.mchange.v2.c3p0.impl.IdentityTokenized;
import com.mchange.v2.c3p0.subst.C3P0Substitutions;
import com.mchange.v2.sql.SqlUtils;
import com.mchange.v2.util.DoubleWeakHashMap;
import com.mchange.v2.c3p0.management.*;

public final class C3P0Registry {

    private static final String MC_PARAM = "com.mchange.v2.c3p0.management.ManagementCoordinator";

    static final MLogger logger = MLog.getLogger(C3P0Registry.class);

    static boolean banner_printed = false;

    static boolean registry_mbean_registered = false;

    private static CoalesceChecker CC = IdentityTokenizedCoalesceChecker.INSTANCE;

    private static Coalescer idtCoalescer = CoalescerFactory.createCoalescer(CC, true, false);

    private static Map tokensToTokenized = new DoubleWeakHashMap();

    private static HashSet unclosedPooledDataSources = new HashSet();

    private static Map classNamesToConnectionTesters = Collections.synchronizedMap(new HashMap());

    private static Map classNamesToConnectionCustomizers = Collections.synchronizedMap(new HashMap());

    private static ManagementCoordinator mc;

    static {
        classNamesToConnectionTesters.put(C3P0Defaults.connectionTesterClassName(), C3P0Defaults.connectionTester());
        String userManagementCoordinator = C3P0ConfigUtils.getPropFileConfigProperty(MC_PARAM);
        if (userManagementCoordinator != null) {
            try {
                mc = (ManagementCoordinator) Class.forName(userManagementCoordinator).newInstance();
            } catch (Exception e) {
                if (logger.isLoggable(MLevel.WARNING)) logger.log(MLevel.WARNING, "Could not instantiate user-specified ManagementCoordinator " + userManagementCoordinator + ". Using NullManagementCoordinator (c3p0 JMX management disabled!)", e);
                mc = new NullManagementCoordinator();
            }
        } else {
            try {
                Class.forName("java.lang.management.ManagementFactory");
                mc = (ManagementCoordinator) Class.forName("com.mchange.v2.c3p0.management.ActiveManagementCoordinator").newInstance();
            } catch (Exception e) {
                if (logger.isLoggable(MLevel.INFO)) logger.log(MLevel.INFO, "jdk1.5 management interfaces unavailable... JMX support disabled.", e);
                mc = new NullManagementCoordinator();
            }
        }
    }

    public static ConnectionTester getConnectionTester(String className) {
        try {
            ConnectionTester out = (ConnectionTester) classNamesToConnectionTesters.get(className);
            if (out == null) {
                out = (ConnectionTester) Class.forName(className).newInstance();
                classNamesToConnectionTesters.put(className, out);
            }
            return out;
        } catch (Exception e) {
            if (logger.isLoggable(MLevel.WARNING)) logger.log(MLevel.WARNING, "Could not create for find ConnectionTester with class name '" + className + "'. Using default.", e);
            return C3P0Defaults.connectionTester();
        }
    }

    public static ConnectionCustomizer getConnectionCustomizer(String className) throws SQLException {
        if (className == null) return null; else {
            try {
                ConnectionCustomizer out = (ConnectionCustomizer) classNamesToConnectionCustomizers.get(className);
                if (out == null) {
                    out = (ConnectionCustomizer) Class.forName(className).newInstance();
                    classNamesToConnectionCustomizers.put(className, out);
                }
                return out;
            } catch (Exception e) {
                if (logger.isLoggable(MLevel.WARNING)) logger.log(MLevel.WARNING, "Could not create for find ConnectionCustomizer with class name '" + className + "'.", e);
                throw SqlUtils.toSQLException(e);
            }
        }
    }

    private static void banner() {
        if (!banner_printed) {
            if (logger.isLoggable(MLevel.INFO)) logger.info("Initializing c3p0-" + C3P0Substitutions.VERSION + " [built " + C3P0Substitutions.TIMESTAMP + "; debug? " + C3P0Substitutions.DEBUG + "; trace: " + C3P0Substitutions.TRACE + ']');
            banner_printed = true;
        }
    }

    private static void attemptRegisterRegistryMBean() {
        if (!registry_mbean_registered) {
            mc.attemptManageC3P0Registry();
            registry_mbean_registered = true;
        }
    }

    private static boolean isIncorporated(IdentityTokenized idt) {
        return tokensToTokenized.keySet().contains(idt.getIdentityToken());
    }

    private static void incorporate(IdentityTokenized idt) {
        tokensToTokenized.put(idt.getIdentityToken(), idt);
        if (idt instanceof PooledDataSource) {
            unclosedPooledDataSources.add(idt);
            mc.attemptManagePooledDataSource((PooledDataSource) idt);
        }
    }

    public static synchronized IdentityTokenized reregister(IdentityTokenized idt) {
        if (idt instanceof PooledDataSource) {
            banner();
            attemptRegisterRegistryMBean();
        }
        if (idt.getIdentityToken() == null) throw new RuntimeException("[c3p0 issue] The identityToken of a registered object should be set prior to registration.");
        IdentityTokenized coalesceCheck = (IdentityTokenized) idtCoalescer.coalesce(idt);
        if (!isIncorporated(coalesceCheck)) incorporate(coalesceCheck);
        return coalesceCheck;
    }

    public static synchronized void markClosed(PooledDataSource pds) {
        unclosedPooledDataSources.remove(pds);
        mc.attemptUnmanagePooledDataSource(pds);
        if (unclosedPooledDataSources.isEmpty()) {
            mc.attemptUnmanageC3P0Registry();
            registry_mbean_registered = false;
        }
    }

    public static synchronized Set getPooledDataSources() {
        return (Set) unclosedPooledDataSources.clone();
    }

    public static synchronized Set pooledDataSourcesByName(String dataSourceName) {
        Set out = new HashSet();
        for (Iterator ii = unclosedPooledDataSources.iterator(); ii.hasNext(); ) {
            PooledDataSource pds = (PooledDataSource) ii.next();
            if (pds.getDataSourceName().equals(dataSourceName)) out.add(pds);
        }
        return out;
    }

    public static synchronized PooledDataSource pooledDataSourceByName(String dataSourceName) {
        for (Iterator ii = unclosedPooledDataSources.iterator(); ii.hasNext(); ) {
            PooledDataSource pds = (PooledDataSource) ii.next();
            if (pds.getDataSourceName().equals(dataSourceName)) return pds;
        }
        return null;
    }

    public static synchronized Set allIdentityTokens() {
        Set out = Collections.unmodifiableSet(tokensToTokenized.keySet());
        return out;
    }

    public static synchronized Set allIdentityTokenized() {
        HashSet out = new HashSet();
        out.addAll(tokensToTokenized.values());
        return Collections.unmodifiableSet(out);
    }

    public static synchronized Set allPooledDataSources() {
        Set out = Collections.unmodifiableSet(unclosedPooledDataSources);
        return out;
    }

    public static synchronized int getNumPooledDataSources() {
        return unclosedPooledDataSources.size();
    }

    public static synchronized int getNumPoolsAllDataSources() throws SQLException {
        int count = 0;
        for (Iterator ii = unclosedPooledDataSources.iterator(); ii.hasNext(); ) {
            PooledDataSource pds = (PooledDataSource) ii.next();
            count += pds.getNumUserPools();
        }
        return count;
    }

    public synchronized int getNumThreadsAllThreadPools() throws SQLException {
        int count = 0;
        for (Iterator ii = unclosedPooledDataSources.iterator(); ii.hasNext(); ) {
            PooledDataSource pds = (PooledDataSource) ii.next();
            count += pds.getNumHelperThreads();
        }
        return count;
    }
}
