package uk.ac.sanger.cgp.dbcon.config;

import java.io.Serializable;

/**
 * Abstraction of the pooling configuration code away from the core Pool
 * object since now the pool config can come from any number of sources
 * so long as someone wants to spend the time on creating them.
 *
 * @author Andrew Yates
 * @author $Author: ady $
 * @version $Revision: 1.7 $
 */
public class Config implements Serializable, Cloneable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8255104234840048031L;

    /** When the pool is exhausted, an exception is thrown */
    public static final byte FAIL = 0;

    /**
   * When the pool is exhausted, the user waits for a specified amount of
   * time before an exception is thrown
   */
    public static final byte BLOCK = 1;

    /** When the pool is exhausted, the pool grows */
    public static final byte GROW = 2;

    private int maxActive = 0;

    private int maxIdle = 0;

    private int numTestsPerEvictionRun = 0;

    private byte exhausted = 0;

    private long maxWait = 0;

    private long timeBetweenEvictRun = 0;

    private long minEvictTime = 0;

    private int cachedPreparedStatements = 0;

    private boolean testOnBorrow = false;

    private boolean testOnReturn = false;

    private boolean testWhileIdle = false;

    private String url = null;

    private String backupUrl = null;

    private String username = null;

    private String password = null;

    private String name = null;

    private String validationQuery = null;

    private String driver = null;

    private String workingUrl = null;

    /** Creates a new instance of Config */
    private Config() {
        super();
    }

    public static Config createEmptyConfig() {
        return new Config();
    }

    /**
   * Factory method to create a pool object & return it.
   *
   * @param maxActive Maximum number of active connections
   * @param exhausted When exhausted action, 0 = fail, 1 = block, 2 = grow
   * @param maxWait The maximum amount of time before an exception is thrown
   * @param maxIdle The maximum number of objects that can be idle
   * @param testOnBorrow  Tests that the connection works when borrowed
   * @param testOnReturn  Tests that the connection works when returned
   * @param timeBetweenEvictRun The time between eviction runs
   * @param numTestsPerEvictionRun  The number of objects to test per eviction
   *                                run
   * @param minEvictTime  Number of idle milliseconds before the object is up
   *                      for eviction
   * @param testWhileIdle Test the connection when idle
   * @param url The connection url to use
	 * @param username The username to use to connect to the database with
	 * @param password The password to use to connect to the database with
   * @param name The connection name which will be used to register the pool by
   * @param validationQuery The query that will be used to check if a
   *                          connection is okay
   * @param driver The JDBC driver used to connect to the specified DB
	 * @param cachedPreparedStatements Number of statements to cache
   * @return The created pool from the specified options
   */
    public static Config createBasicPoolConfig(int maxActive, byte exhausted, long maxWait, int maxIdle, boolean testOnBorrow, boolean testOnReturn, long timeBetweenEvictRun, int numTestsPerEvictionRun, long minEvictTime, boolean testWhileIdle, String url, String username, String password, String name, String validationQuery, String driver, int cachedPreparedStatements) {
        Config config = new Config(maxActive, exhausted, maxWait, maxIdle, testOnBorrow, testOnReturn, timeBetweenEvictRun, numTestsPerEvictionRun, minEvictTime, testWhileIdle, url, username, password, name, validationQuery, driver, null, cachedPreparedStatements);
        return config;
    }

    /**
   * Factory method to create a pool object & return it.
   *
   * @param maxActive Maximum number of active connections
   * @param exhausted When exhausted action, 0 = fail, 1 = block, 2 = grow
   * @param maxWait The maximum amount of time before an exception is thrown
   * @param maxIdle The maximum number of objects that can be idle
   * @param testOnBorrow  Tests that the connection works when borrowed
   * @param testOnReturn  Tests that the connection works when returned
   * @param timeBetweenEvictRun The time between eviction runs
   * @param numTestsPerEvictionRun  The number of objects to test per eviction
   *                                run
   * @param minEvictTime  Number of idle milliseconds before the object is up
   *                      for eviction
   * @param testWhileIdle Test the connection when idle
   * @param url The connection url to use
	 * @param username The username to use to connect to the database with
	 * @param password The password to use to connect to the database with
   * @param name The connection name which will be used to register the pool by
   * @param validationQuery The query that will be used to check if a
   *                          connection is okay
   * @param driver The JDBC driver used to connect to the specified DB
	 * @param cachedPreparedStatements Number of statements to cache
   * @return The created pool from the specified options
   */
    public static Config createBasicPoolConfigWithBackup(int maxActive, byte exhausted, long maxWait, int maxIdle, boolean testOnBorrow, boolean testOnReturn, long timeBetweenEvictRun, int numTestsPerEvictionRun, long minEvictTime, boolean testWhileIdle, String url, String username, String password, String name, String validationQuery, String driver, String backupUrl, int cachedPreparedStatements) {
        Config config = new Config(maxActive, exhausted, maxWait, maxIdle, testOnBorrow, testOnReturn, timeBetweenEvictRun, numTestsPerEvictionRun, minEvictTime, testWhileIdle, url, username, password, name, validationQuery, driver, backupUrl, cachedPreparedStatements);
        return config;
    }

    /**
   * Working constructor
   *
   * @param maxActive Maximum number of active connections
   * @param exhausted When exhausted action, 0 = fail, 1 = block, 2 = grow
   * @param maxWait The maximum amount of time before an exception is thrown
   * @param maxIdle The maximum number of objects that can be idle
   * @param testOnBorrow  Tests that the connection works when borrowed
   * @param testOnReturn  Tests that the connection works when returned
   * @param timeBetweenEvictRun The time between eviction runs
   * @param numTestsPerEvictionRun  The number of objects to test per eviction
   *                                run
   * @param minEvictTime  Number of idle milliseconds before the object is up
   *                      for eviction
   * @param testWhileIdle Test the connection when idle
   * @param url The connection url to use
	 * @param username The username to use to connect to the database with
	 * @param password The password to use to connect to the database with
   * @param name The connection name which will be used to register the pool by
   * @param validationQuery The query that will be used to check if a
   *                          connection is okay
   * @param driver The JDBC driver used to connect to the specified DB
	 * @param cachedPreparedStatements Number of statements to cache
	 * @param backupUrl The backup URL
   */
    private Config(int maxActive, byte exhausted, long maxWait, int maxIdle, boolean testOnBorrow, boolean testOnReturn, long timeBetweenEvictRun, int numTestsPerEvictionRun, long minEvictTime, boolean testWhileIdle, String url, String username, String password, String name, String validationQuery, String driver, String backupUrl, int cachedPreparedStatements) {
        this.maxActive = maxActive;
        this.exhausted = exhausted;
        this.maxWait = maxWait;
        this.maxIdle = maxIdle;
        this.testOnBorrow = testOnBorrow;
        this.testOnReturn = testOnReturn;
        this.timeBetweenEvictRun = timeBetweenEvictRun;
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
        this.minEvictTime = minEvictTime;
        this.testWhileIdle = testWhileIdle;
        this.url = url;
        this.username = username;
        this.password = password;
        this.name = name;
        this.validationQuery = validationQuery;
        this.driver = driver;
        this.backupUrl = backupUrl;
        this.cachedPreparedStatements = cachedPreparedStatements;
    }

    /**
	 * Clones the object by running the hidden full working constructor and returns
	 * a new object therefore this can be edited without affecting the underlying
	 * configuration object if needed.
	 */
    public Object clone() {
        Config clone = new Config(maxActive, exhausted, maxWait, maxIdle, testOnBorrow, testOnReturn, timeBetweenEvictRun, numTestsPerEvictionRun, minEvictTime, testWhileIdle, url, username, password, name, validationQuery, driver, backupUrl, cachedPreparedStatements);
        return clone;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public byte getExhausted() {
        return exhausted;
    }

    public void setExhausted(byte exhausted) {
        this.exhausted = exhausted;
    }

    public long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public long getTimeBetweenEvictRun() {
        return timeBetweenEvictRun;
    }

    public void setTimeBetweenEvictRun(long timeBetweenEvictRun) {
        this.timeBetweenEvictRun = timeBetweenEvictRun;
    }

    public int getNumTestsPerEvictionRun() {
        return numTestsPerEvictionRun;
    }

    public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    public void setMinEvictTime(long minEvictTime) {
        this.minEvictTime = minEvictTime;
    }

    public long getMinEvictTime() {
        return minEvictTime;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBackupUrl() {
        return backupUrl;
    }

    public void setBackupUrl(String backupUrl) {
        this.backupUrl = backupUrl;
    }

    public String getWorkingUrl() {
        return workingUrl;
    }

    public void setWorkingUrl(String workingUrl) {
        this.workingUrl = workingUrl;
    }

    public int getCachedPreparedStatements() {
        return cachedPreparedStatements;
    }

    public void setCachedPreparedStatements(int cachedPreparedStatements) {
        this.cachedPreparedStatements = cachedPreparedStatements;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("name: " + name + "\n");
        sb.append("driver: " + driver + "\n");
        sb.append("url: " + url + "\n");
        sb.append("backupUrl: " + backupUrl + "\n");
        sb.append("workingUrl: " + workingUrl + "\n");
        sb.append("username: " + username + "\n");
        sb.append("password: " + password + "\n");
        sb.append("validationQuery: " + validationQuery + "\n");
        sb.append("maxActive: " + maxActive + "\n");
        sb.append("exhausted: " + exhausted + "\n");
        sb.append("maxWait: " + maxWait + "\n");
        sb.append("maxIdle: " + maxIdle + "\n");
        sb.append("testOnBorrow: " + testOnBorrow + "\n");
        sb.append("testOnReturn: " + testOnReturn + "\n");
        sb.append("timeBetweenEvictRun: " + timeBetweenEvictRun + "\n");
        sb.append("numTestsPerEvictionRun: " + numTestsPerEvictionRun + "\n");
        sb.append("minEvictTime: " + minEvictTime + "\n");
        sb.append("testWhileIdle: " + testWhileIdle + "\n");
        sb.append("cachedPreparedStatements: " + cachedPreparedStatements + "\n");
        return sb.toString();
    }
}
