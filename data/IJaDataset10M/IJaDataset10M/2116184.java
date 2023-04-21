package edu.ucdavis.genomics.metabolomics.binbase.algorythm.export.dest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import edu.ucdavis.genomics.metabolomics.binbase.algorythm.export.source.QuantificationTableSourceFactoryImpl;
import edu.ucdavis.genomics.metabolomics.util.PropertySetter;
import edu.ucdavis.genomics.metabolomics.util.database.ConnectionFactory;
import edu.ucdavis.genomics.metabolomics.util.io.dest.AbstractDestinationTest;

public class QuantificationTableDestinationTest extends AbstractDestinationTest {

    private Connection c;

    ConnectionFactory fact;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(QuantificationTableDestinationTest.class);
    }

    /**
	 * make a clean table
	 * 
	 * @author wohlgemuth
	 * @version Nov 10, 2005
	 * @see junit.framework.TestCase#setUp()
	 */
    protected void setUp() throws Exception {
        PropertySetter.setPropertiesToSystem("src/test/resources/test.properties");
        fact = ConnectionFactory.getFactory();
        fact.setProperties(System.getProperties());
        c = fact.getConnection();
        PreparedStatement statement = c.prepareStatement("DELETE FROM QUANTIFICATION WHERE \"sample_id\" = ?");
        statement.setInt(1, getProperty().hashCode());
        statement.execute();
        statement.close();
        super.setUp();
    }

    /**
	 * make a clean table
	 * 
	 * @author wohlgemuth
	 * @version Nov 10, 2005
	 * @see junit.framework.TestCase#tearDown()
	 */
    protected void tearDown() throws Exception {
        fact.close(c);
    }

    @Override
    protected String getDestinationFactoryImpl() {
        return QuantificationTableDestinationFactoryImpl.class.getName();
    }

    @Override
    protected String getSourceFactoryImpl() {
        return QuantificationTableSourceFactoryImpl.class.getName();
    }
}
