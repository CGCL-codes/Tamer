package net.derquinse.common.orm.hib;

import net.derquinse.common.test.h2.H2Tests;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.H2Dialect;

/**
 * Hibernate configurations used in tests.
 * @author Andres Rodriguez
 */
class TestConfigurations {

    /** Not instantiable. */
    private TestConfigurations() {
    }

    /** Creates a basic configuration. */
    static Configuration basic() {
        return new Configuration().addAnnotatedClass(TestUUIDEntity.class);
    }

    /** Creates a configuration with types. */
    static Configuration types() {
        return Configurations.registerTypes(basic());
    }

    /** Decorates a configuration with H2 database information. */
    static Configuration h2(Configuration c) {
        c.setProperty("hibernate.connection.driver_class", H2Tests.DRIVER);
        c.setProperty("hibernate.connection.url", H2Tests.MEM_URL);
        c.setProperty("hibernate.connection.username", H2Tests.MEM_USER);
        c.setProperty("hibernate.connection.password", H2Tests.MEM_PWD);
        c.setProperty("hibernate.connection.pool_size", "1");
        c.setProperty("hibernate.hbm2ddl.auto", "update");
        c.setProperty("hibernate.dialect", H2Dialect.class.getName());
        return c;
    }
}
