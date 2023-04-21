package edu.ucdavis.genomics.metabolomics.binbase.bdi.util.hibernate;

import java.io.File;
import junit.framework.TestCase;
import org.hibernate.Session;
import edu.ucdavis.genomics.metabolomics.util.config.XMLConfigurator;
import edu.ucdavis.genomics.metabolomics.util.io.source.FileSource;

/**
 * @author wohlgemuth
 *
 */
public abstract class AbstractHibernateTest extends TestCase {

    private HibernateFactory factory;

    private Session session;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(AbstractHibernateTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        XMLConfigurator.forceInstance(new FileSource(new File("config/configurator.xml")));
        factory = HibernateFactory.newInstance();
        session = factory.getSession();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        getSession().close();
    }

    /**
	 * returns the needed session for the test
	 * @author wohlgemuth
	 * @version Dec 9, 2005
	 * @return
	 */
    protected Session getSession() {
        return session;
    }
}
