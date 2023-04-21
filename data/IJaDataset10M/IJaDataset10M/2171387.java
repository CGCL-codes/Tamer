package org.ensembl.test;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.ensembl.datamodel.CoordinateSystem;
import org.ensembl.datamodel.Exon;
import org.ensembl.datamodel.Location;
import org.ensembl.driver.ExonAdaptor;
import org.ensembl.driver.SupportingFeatureAdaptor;

/**
 * Test class for SupportingFeatureAdaptors. 
 */
public class SupportingFeatureAdaptorTest extends CoreBase {

    private static Logger logger = Logger.getLogger(SupportingFeatureAdaptorTest.class.getName());

    public static final void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public SupportingFeatureAdaptorTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(SupportingFeatureAdaptorTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        exonAdaptor = (ExonAdaptor) driver.getAdaptor("exon");
        if (exonAdaptor == null) throw new Exception("Failed to find exonAdaptor");
        supportingFeatureAdaptor = (SupportingFeatureAdaptor) driver.getAdaptor("supporting_feature");
        if (supportingFeatureAdaptor == null) throw new Exception("Failed to find supportingFeatureAdaptor");
    }

    public void testRetrieveByExonDifferentLocations() {
        try {
            Location assemblyLocation = new Location(new CoordinateSystem("chromosome"), "12", 1, 999660, 0);
            Location chromosome12Location = new Location(chromosomeCS, "12");
            Location wholeGenome = new Location(chromosomeCS);
            Location locations[] = new Location[] { assemblyLocation };
            for (int i = 0; i < locations.length; ++i) {
                logger.fine("Retrieving exons for location : " + locations[i]);
                logger.fine("From exon adaptor : " + exonAdaptor);
                List exons = exonAdaptor.fetch(locations[i]);
                logger.fine("Fetch exons at location=" + locations[i]);
                if (exons == null) {
                    logger.fine("No exons found at location.");
                } else {
                    int counter = 0;
                    int nSupport = 0;
                    Iterator iter = exons.iterator();
                    while (iter.hasNext()) {
                        Exon exon = (Exon) iter.next();
                        counter++;
                        List support = exon.getSupportingFeatures();
                        nSupport += support.size();
                    }
                    logger.fine("Number Exons = " + counter);
                    logger.fine("Number Supporting Features = " + nSupport);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private ExonAdaptor exonAdaptor;

    private SupportingFeatureAdaptor supportingFeatureAdaptor;
}
