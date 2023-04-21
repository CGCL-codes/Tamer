package gov.sns.xal.model.test;

import gov.sns.xal.model.xml.LatticeXmlParser;
import gov.sns.xal.model.xml.ParsingException;
import gov.sns.xal.model.Lattice;
import junit.framework.TestCase;

/**
 * Provides basic tests for lattice and factory instance creation methods for
 * providing test data.
 * 
 * @author Craig McChesney
 */
public class LatticeTest extends TestCase {

    public static final String LATTICE_URL = "xml/ModelValidation.lat.mod.xal.xml";

    public static Lattice newTestLattice() {
        try {
            return LatticeXmlParser.parse(LATTICE_URL, false);
        } catch (ParsingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
