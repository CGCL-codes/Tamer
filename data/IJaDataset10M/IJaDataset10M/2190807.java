package org.bing.engine.console.service;

import org.junit.After;
import org.junit.Before;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration(locations = { "classpath*:/META-INF/bing-console-spring-*.xml", "classpath*:/META-INF/bing-test-console-spring-*.xml" })
public abstract class AbstractConsoleTest extends AbstractJUnit4SpringContextTests {

    protected JdbcTemplate jdbcTemplate;

    protected HibernateTemplate hibernateTemplate;

    @Before
    public void setUp() throws Exception {
        jdbcTemplate = (JdbcTemplate) this.applicationContext.getBean("console.JdbcTemplate");
        hibernateTemplate = (HibernateTemplate) this.applicationContext.getBean("console.HibernateTemplate");
    }

    @After
    public void tearDown() throws Exception {
    }
}
