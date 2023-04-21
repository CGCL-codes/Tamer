package org.jomc.standalone.ri.naming.support;

import javax.persistence.spi.PersistenceProvider;
import org.hibernate.cfg.Environment;
import org.hibernate.ejb.HibernatePersistence;

@javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
public class HibernateContextFactory extends AbstractJPAContextFactory {

    protected PersistenceProvider getPersistenceProvider() {
        System.setProperty(Environment.USER_TRANSACTION, this.getStandaloneEnvironment().getUserTransactionJndiName());
        return new HibernatePersistence();
    }

    /** Creates a new {@code HibernateContextFactory} instance. */
    @javax.annotation.Generated(value = "org.jomc.tools.SourceFileProcessor 1.2", comments = "See http://jomc.sourceforge.net/jomc/1.2/jomc-tools-1.2")
    public HibernateContextFactory() {
        super();
    }
}
