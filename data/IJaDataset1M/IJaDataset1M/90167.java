package com.mchange.v2.c3p0.impl;

import java.lang.reflect.*;
import java.util.*;
import com.mchange.v2.c3p0.ConnectionTester;

public final class C3P0Defaults {

    private static final int MAX_STATEMENTS = 0;

    private static final int MAX_STATEMENTS_PER_CONNECTION = 0;

    private static final int INITIAL_POOL_SIZE = 3;

    private static final int MIN_POOL_SIZE = 3;

    private static final int MAX_POOL_SIZE = 15;

    private static final int IDLE_CONNECTION_TEST_PERIOD = 0;

    private static final int MAX_IDLE_TIME = 0;

    private static final int PROPERTY_CYCLE = 0;

    private static final int ACQUIRE_INCREMENT = 3;

    private static final int ACQUIRE_RETRY_ATTEMPTS = 30;

    private static final int ACQUIRE_RETRY_DELAY = 1000;

    private static final int CHECKOUT_TIMEOUT = 0;

    private static final int MAX_ADMINISTRATIVE_TASK_TIME = 0;

    private static final int MAX_IDLE_TIME_EXCESS_CONNECTIONS = 0;

    private static final int MAX_CONNECTION_AGE = 0;

    private static final int UNRETURNED_CONNECTION_TIMEOUT = 0;

    private static final boolean BREAK_AFTER_ACQUIRE_FAILURE = false;

    private static final boolean TEST_CONNECTION_ON_CHECKOUT = false;

    private static final boolean TEST_CONNECTION_ON_CHECKIN = false;

    private static final boolean AUTO_COMMIT_ON_CLOSE = false;

    private static final boolean FORCE_IGNORE_UNRESOLVED_TXNS = false;

    private static final boolean USES_TRADITIONAL_REFLECTIVE_PROXIES = false;

    private static final boolean DEBUG_UNRETURNED_CONNECTION_STACK_TRACES = false;

    private static final ConnectionTester CONNECTION_TESTER = new DefaultConnectionTester();

    private static final int NUM_HELPER_THREADS = 3;

    private static final String AUTOMATIC_TEST_TABLE = null;

    private static final String CONNECTION_CUSTOMIZER_CLASS_NAME = null;

    private static final String DRIVER_CLASS = null;

    private static final String JDBC_URL = null;

    private static final String OVERRIDE_DEFAULT_USER = null;

    private static final String OVERRIDE_DEFAULT_PASSWORD = null;

    private static final String PASSWORD = null;

    private static final String PREFERRED_TEST_QUERY = null;

    private static final String FACTORY_CLASS_LOCATION = null;

    private static final String USER_OVERRIDES_AS_STRING = null;

    private static final String USER = null;

    private static Set KNOWN_PROPERTIES;

    static {
        Method[] methods = C3P0Defaults.class.getMethods();
        Set s = new HashSet();
        for (int i = 0, len = methods.length; i < len; ++i) {
            Method m = methods[i];
            if (Modifier.isStatic(m.getModifiers()) && m.getParameterTypes().length == 0) s.add(m.getName());
        }
        KNOWN_PROPERTIES = Collections.unmodifiableSet(s);
    }

    public static Set getKnownProperties() {
        return KNOWN_PROPERTIES;
    }

    public static boolean isKnownProperty(String s) {
        return KNOWN_PROPERTIES.contains(s);
    }

    public static int maxStatements() {
        return MAX_STATEMENTS;
    }

    public static int maxStatementsPerConnection() {
        return MAX_STATEMENTS_PER_CONNECTION;
    }

    public static int initialPoolSize() {
        return INITIAL_POOL_SIZE;
    }

    public static int minPoolSize() {
        return MIN_POOL_SIZE;
    }

    public static int maxPoolSize() {
        return MAX_POOL_SIZE;
    }

    public static int idleConnectionTestPeriod() {
        return IDLE_CONNECTION_TEST_PERIOD;
    }

    public static int maxIdleTime() {
        return MAX_IDLE_TIME;
    }

    public static int unreturnedConnectionTimeout() {
        return UNRETURNED_CONNECTION_TIMEOUT;
    }

    public static int propertyCycle() {
        return PROPERTY_CYCLE;
    }

    public static int acquireIncrement() {
        return ACQUIRE_INCREMENT;
    }

    public static int acquireRetryAttempts() {
        return ACQUIRE_RETRY_ATTEMPTS;
    }

    public static int acquireRetryDelay() {
        return ACQUIRE_RETRY_DELAY;
    }

    public static int checkoutTimeout() {
        return CHECKOUT_TIMEOUT;
    }

    public static String connectionCustomizerClassName() {
        return CONNECTION_CUSTOMIZER_CLASS_NAME;
    }

    public static ConnectionTester connectionTester() {
        return CONNECTION_TESTER;
    }

    public static String connectionTesterClassName() {
        return CONNECTION_TESTER.getClass().getName();
    }

    public static String automaticTestTable() {
        return AUTOMATIC_TEST_TABLE;
    }

    public static String driverClass() {
        return DRIVER_CLASS;
    }

    public static String jdbcUrl() {
        return JDBC_URL;
    }

    public static int numHelperThreads() {
        return NUM_HELPER_THREADS;
    }

    public static boolean breakAfterAcquireFailure() {
        return BREAK_AFTER_ACQUIRE_FAILURE;
    }

    public static boolean testConnectionOnCheckout() {
        return TEST_CONNECTION_ON_CHECKOUT;
    }

    public static boolean testConnectionOnCheckin() {
        return TEST_CONNECTION_ON_CHECKIN;
    }

    public static boolean autoCommitOnClose() {
        return AUTO_COMMIT_ON_CLOSE;
    }

    public static boolean forceIgnoreUnresolvedTransactions() {
        return FORCE_IGNORE_UNRESOLVED_TXNS;
    }

    public static boolean debugUnreturnedConnectionStackTraces() {
        return DEBUG_UNRETURNED_CONNECTION_STACK_TRACES;
    }

    public static boolean usesTraditionalReflectiveProxies() {
        return USES_TRADITIONAL_REFLECTIVE_PROXIES;
    }

    public static String preferredTestQuery() {
        return PREFERRED_TEST_QUERY;
    }

    public static String userOverridesAsString() {
        return USER_OVERRIDES_AS_STRING;
    }

    public static String factoryClassLocation() {
        return FACTORY_CLASS_LOCATION;
    }

    public static String overrideDefaultUser() {
        return OVERRIDE_DEFAULT_USER;
    }

    public static String overrideDefaultPassword() {
        return OVERRIDE_DEFAULT_PASSWORD;
    }

    public static String user() {
        return USER;
    }

    public static String password() {
        return PASSWORD;
    }

    public static int maxAdministrativeTaskTime() {
        return MAX_ADMINISTRATIVE_TASK_TIME;
    }

    public static int maxIdleTimeExcessConnections() {
        return MAX_IDLE_TIME_EXCESS_CONNECTIONS;
    }

    public static int maxConnectionAge() {
        return MAX_CONNECTION_AGE;
    }
}
