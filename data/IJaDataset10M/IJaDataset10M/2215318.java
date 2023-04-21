package com.apelon.test;

import java.io.IOException;
import java.util.Properties;

/**
 * The connection information is stored in two properties files.
 *
 * The host, port, and instance name are stored in DbTest.properties.
 *
 * The other properties (userName, password, and anything else) are stored in a
 * file specified by the subclass.
 *
 * Since this information is loaded at runtime, it can be modified without re-compilation.
 * Simply patch the classpath with a different version of the properties file(s), and those
 * values will be used for this test.
 *
 * @author Matt Munz
 */
public abstract class DbTest extends ConfiguredTest {

    public DbTest(String name) {
        super(name);
    }

    protected abstract String propertiesFile();

    /**
   * Overrides method in superclass to provide a default set of property values.
   *
   * @author Matt Munz
   */
    protected PropsLoader propsLoader() {
        if (fPropsLoader == null) {
            String defFileName = "DbTest.properties";
            fPropsLoader = new PropsLoader(DbTest.class, defFileName, resourceClass(), propertiesFile());
        }
        return fPropsLoader;
    }

    protected String host() throws IOException {
        return connectionProps().getProperty("host");
    }

    public boolean testSQL2K() throws IOException {
        return connectionProps().getProperty("test_sql2k").equalsIgnoreCase("true");
    }

    protected int port() throws IOException {
        return Integer.parseInt(connectionProps().getProperty("port"));
    }

    protected String instance() throws IOException {
        return connectionProps().getProperty("instance");
    }

    protected String userName() throws IOException {
        return connectionProps().getProperty("userName");
    }

    protected String password() throws IOException {
        return connectionProps().getProperty("password");
    }

    protected Properties connectionProps() throws IOException {
        return props();
    }
}
