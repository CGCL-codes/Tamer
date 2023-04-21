package com.diancai.base;

import java.util.Properties;
import javax.ejb.embeddable.EJBContainer;
import javax.naming.NamingException;
import org.junit.Before;

/**
 *
 * @author ZhongFuqiang
 * @since 2012/3/30
 */
public class BaseBeanTest {

    @Before
    public void setUp() throws NamingException {
        Properties properties = new Properties();
        properties.setProperty("openejb.embedded.remotable", "true");
        EJBContainer.createEJBContainer(properties);
    }
}
