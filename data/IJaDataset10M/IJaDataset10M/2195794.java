package net.sourceforge.dalutils4j.eclipse.vo;

public class ConnectionProperties {

    public ConnectionProperties() {
    }

    /**
	 * Path-name of JDBC Driver JAR
	 * 
	 */
    private String JdbcDriverJar;

    /**
	 * JDBC Driver class name
	 * 
	 */
    private String Driver;

    /**
	 * Database URL
	 * 
	 */
    private String URL;

    /**
	 * Database User ID
	 * 
	 */
    private String UserName;

    /**
	 * Database Password
	 * 
	 */
    private String Password;

    private String projectRoot;

    public String getJdbcDriverJar() {
        return JdbcDriverJar;
    }

    public void setJdbcDriverJar(String jdbcDriverJar) {
        JdbcDriverJar = jdbcDriverJar;
    }

    public String getDriver() {
        return Driver;
    }

    public void setDriver(String driver) {
        Driver = driver;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String uRL) {
        URL = uRL;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getProjectRoot() {
        return projectRoot;
    }

    public void setProjectRoot(String projectRoot) {
        this.projectRoot = projectRoot;
    }
}
