package com.siberhus.stars.ejb;

import javax.naming.NamingException;
import com.siberhus.stars.stripes.StarsConfiguration;
import net.sourceforge.stripes.config.Configuration;

public class DefaultEjbLocator implements EjbLocator {

    protected StarsConfiguration configuration;

    protected JndiLocator jndiLocator;

    @Override
    public void init(Configuration configuration) throws Exception {
        this.configuration = (StarsConfiguration) configuration;
        this.jndiLocator = this.configuration.getJndiLocator();
    }

    @Override
    public Object lookup(String contextPath, Class<?> beanInterface, String beanName, String lookup, String name, String mappedName) throws NamingException {
        if (!"".equals(lookup)) {
            return jndiLocator.lookup(lookup);
        }
        return jndiLocator.lookup(beanInterface);
    }
}
