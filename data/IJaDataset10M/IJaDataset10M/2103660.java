package uk.ac.ebi.intact.config;

import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.Oracle9iDialect;
import org.hibernate.ejb.Ejb3Configuration;
import org.junit.Assert;
import org.junit.Test;
import java.util.Properties;

/**
 * TODO comment that class header
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: IntactAuxiliaryConfiguratorTest.java 11705 2008-07-01 08:55:05Z baranda $
 */
public class IntactAuxiliaryConfiguratorTest {

    @Test
    public void configure() throws Exception {
        Properties props = new Properties();
        props.put(Environment.DIALECT, Oracle9iDialect.class.getName());
        Ejb3Configuration ejbConfig = new Ejb3Configuration();
        ejbConfig.configure("intact-core-default", props);
        IntactAuxiliaryConfigurator.configure(ejbConfig);
        String[] sqls = ejbConfig.getHibernateConfiguration().generateSchemaCreationScript(Dialect.getDialect(props));
        boolean containsCvLocalSeq = false;
        for (String sql : sqls) {
            if (sql.contains("cv_local_seq")) containsCvLocalSeq = true;
        }
        Assert.assertTrue("Sequence cv_local_seq should be included in the create DDL", containsCvLocalSeq);
    }
}
