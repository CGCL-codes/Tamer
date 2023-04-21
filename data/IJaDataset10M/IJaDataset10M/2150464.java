package org.ensembl.test;

import java.util.List;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.ensembl.datamodel.Location;
import org.ensembl.datamodel.DitagFeature;
import org.ensembl.driver.AdaptorException;

public class DitagFeatureAdaptorTest extends CoreBase {

    public DitagFeatureAdaptorTest(String name) throws AdaptorException {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(DitagFeatureAdaptorTest.class);
    }

    public void testRetreiveByID() throws Exception {
        DitagFeature f = driver.getDitagFeatureAdaptor().fetch(12831);
        assertNotNull(f);
        assertNotNull(f.getAnalysis());
        assertNotNull(f.getDitag());
        assertNotNull(f.getDisplayName());
    }

    public void testRetrieveByLocation() throws Exception {
        List r = driver.getDitagFeatureAdaptor().fetch(new Location("chromosome:22:21m-22m"));
        assertTrue(r.size() > 0);
    }

    public void testRetrieveByLocationWithChildren() throws Exception {
        List r = driver.getDitagFeatureAdaptor().fetch(new Location("chromosome:22:21m-22m"), true);
        assertTrue(r.size() > 0);
    }

    public void testRetrieveByLocationAndLogicNamesWithChildren() throws Exception {
        String[] lnames = { "GIS_PET" };
        List r = driver.getDitagFeatureAdaptor().fetch(new Location("chromosome:22:21m-22m"), lnames, true);
        assertTrue(r.size() > 0);
    }
}
