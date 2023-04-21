package edu.ucdavis.genomics.metabolomics.util.io.dest;

import java.sql.Connection;
import junit.framework.AssertionFailedError;
import org.dbunit.dataset.xml.XmlDataSet;
import edu.ucdavis.genomics.metabolomics.util.PropertySetter;
import edu.ucdavis.genomics.metabolomics.util.database.ConnectionFactory;
import edu.ucdavis.genomics.metabolomics.util.database.test.InitializeDBUtil;
import edu.ucdavis.genomics.metabolomics.util.io.source.DatabaseSourceFactoryImpl;
import edu.ucdavis.genomics.metabolomics.util.io.source.ResourceSource;

/**
 * sets factorys for the database acces
 * 
 * @author wohlgemuth
 * @version Nov 10, 2005
 * 
 */
public class DatabaseDestinationTest extends AbstractDestinationTest {

    private Connection c = null;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(DatabaseDestinationTest.class);
    }

    /**
	 * make a clean table
	 * 
	 * @author wohlgemuth
	 * @version Nov 10, 2005
	 * @see junit.framework.TestCase#setUp()
	 */
    protected void setUp() throws Exception {
        try {
            PropertySetter.setPropertiesToSystem("src/test/resources/test.properties");
            ConnectionFactory factory = ConnectionFactory.getFactory();
            factory.setProperties(System.getProperties());
            c = factory.getConnection();
            InitializeDBUtil.initialize(c, new XmlDataSet(new ResourceSource("/minimum-rtx5.xml").getStream()));
            super.setUp();
        } catch (Throwable e) {
            new AssertionFailedError("error at setup: " + e.getMessage());
        }
        if (System.getProperty("Binbase.user") == null) {
            throw new AssertionFailedError("you need to provide all the database connection properties!");
        }
    }

    @Override
    protected String getDestinationFactoryImpl() {
        return DatabaseDestinationFactoryImpl.class.getName();
    }

    @Override
    protected String getSourceFactoryImpl() {
        return DatabaseSourceFactoryImpl.class.getName();
    }
}
