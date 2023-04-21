package edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.spectra;

import static org.junit.Assert.assertTrue;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.XmlDataSet;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import edu.ucdavis.genomics.metabolomics.binbase.bdi.model.HibernateModelFactoryImpl;
import edu.ucdavis.genomics.metabolomics.binbase.bdi.model.IModel;
import edu.ucdavis.genomics.metabolomics.binbase.bdi.types.experiment.sample.Sample;
import edu.ucdavis.genomics.metabolomics.binbase.bdi.util.hibernate.HibernateFactory;
import edu.ucdavis.genomics.metabolomics.util.config.XMLConfigurator;
import edu.ucdavis.genomics.metabolomics.util.database.test.BinBaseDatabaseTest;
import edu.ucdavis.genomics.metabolomics.util.io.source.ResourceSource;

/**
 * makes sure we can query spectra and sample properties as it supposed to be
 * without excetption
 * 
 * @author wohlgemuth
 */
public class SpectraQueryTest extends BinBaseDatabaseTest {

    private Logger logger = Logger.getLogger(getClass());

    private Properties p;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        logger.info("creating hibernate connection");
        p = XMLConfigurator.getInstance(new ResourceSource("/config/hibernate.xml")).getProperties();
        p.putAll(System.getProperties());
    }

    @After
    public void tearDown() throws Exception {
        HibernateFactory.newInstance().destroyFactory();
        logger.info("calling super to teardown...");
        super.tearDown();
    }

    @Override
    protected IDataSet getDataSet() {
        try {
            logger.info("looking for datafile...");
            ResourceSource source = new ResourceSource("/minimum-rtx5.xml");
            logger.debug("using source: " + source.getSourceName() + " exist: " + source.exist());
            assertTrue(source.exist());
            return new XmlDataSet(source.getStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    boolean failedThread;

    @org.junit.Test
    public void testQuery() {
        final IModel notFound = HibernateModelFactoryImpl.newInstance(HibernateModelFactoryImpl.class.getName()).createModel(NotFoundSpectra.class);
        final IModel found = HibernateModelFactoryImpl.newInstance(HibernateModelFactoryImpl.class.getName()).createModel(Spectra.class);
        IModel sample = HibernateModelFactoryImpl.newInstance(HibernateModelFactoryImpl.class.getName()).createModel(Sample.class);
        sample.setQuery("from Sample a where a.id = 1");
        final Sample sample1 = (Sample) sample.executeQuery()[0];
        failedThread = false;
        for (int i = 0; i < 4; i++) {
            found.setQuery("from Spectra a where a.sample.id = " + sample1.getId());
            found.executeQuery();
            notFound.setQuery("from NotFoundSpectra a where a.sample.id = " + sample1.getId());
            notFound.executeQuery();
        }
    }
}
