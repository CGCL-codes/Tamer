package test.org.mbari.vars.knowledgebase.model;

import java.util.Collection;
import java.util.logging.Logger;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.mbari.vars.knowledgebase.model.Concept;

/**
 * <h2><u>Description</u></h2>
 * <p><!--Insert summary here--></p>
 *
 * <h2><u>UML</u></h2>
 * <pre>
 *
 * </pre>
 *
 * <h2><u>License</u></h2>
 * <p><font size="-1" color="#336699"><a href="http://www.mbari.org">
 * The Monterey Bay Aquarium Research Institute (MBARI)</a> provides this
 * documentation and code &quot;as is&quot;, with no warranty, express or
 * implied, of its quality or consistency. It is provided without support and
 * without obligation on the part of MBARI to assist in its use, correction,
 * modification, or enhancement. This information should not be published or
 * distributed to third parties without specific written permission from
 * MBARI.</font></p>
 *
 * <p><font size="-1" color="#336699">Copyright 2003 MBARI.
 * MBARI Proprietary Information. All rights reserved.</font></p>
 *
 * @author <a href="http://www.mbari.org">MBARI</a>
 * @version $Id: ConceptTest.java 3 2005-10-27 16:20:12Z hohonuuli $
 */
public class ConceptTest extends TestCase {

    public static Test suite() {
        return new TestSuite(ConceptTest.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(ConceptTest.suite());
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Attempt to add this as a childconcept reference. THis should not be allowed
     * and this test checks for this.
     *
     */
    public void testAddChildConcept1() {
        Concept c = KbObjectFactory.makeConcept("testAddChildConcept1");
        c.addChildConcept(c);
        Collection children = c.getChildConceptColl();
        if (children.contains(c)) {
            fail("Test was able to call this.addChildConcept(this). This is not allowed!!");
        }
    }

    /**
     * Attempt to create a cirular reference of parent ---> child ---> parent
     *
     */
    public void testAddChildConcept2() {
        Concept parent = KbObjectFactory.makeConcept("parent");
        Concept child = KbObjectFactory.makeConcept("child");
        parent.addChildConcept(child);
        assertTrue("Failed to add a child concept.", parent.getChildConceptColl().contains(child));
        child.addChildConcept(parent);
        Collection children = child.getChildConceptColl();
        if (children.contains(parent)) {
            fail("Test was able to create a circular reference. This is not allowed!!");
        }
    }

    /**
     * Attempt to create a cirular reference of parent ---> child ---> child 
     * --> parent
     *
     */
    public void testAddChildConcept3() {
        Concept parent = KbObjectFactory.makeConcept("parent");
        Concept c1 = KbObjectFactory.makeConcept("child");
        Concept c2 = KbObjectFactory.makeConcept("child");
        Concept c3 = KbObjectFactory.makeConcept("child");
        parent.addChildConcept(c1);
        c1.addChildConcept(c2);
        c2.addChildConcept(c3);
        assertTrue("Failed to add a child concept.", parent.getChildConceptColl().contains(c1));
        assertTrue("Failed to add a child concept.", c1.getChildConceptColl().contains(c2));
        assertTrue("Failed to add a child concept.", c2.getChildConceptColl().contains(c3));
        c3.addChildConcept(parent);
        Collection children = c3.getChildConceptColl();
        if (children.contains(parent)) {
            fail("Test was able to create a circular reference. This is not allowed!!");
        }
    }

    /**
     * Constructor for ConceptTest.
     * @param arg0
     */
    public ConceptTest(String arg0) {
        super(arg0);
        log.info("Running test: " + getClass().getName());
    }

    private static final Logger log = Logger.getLogger("vars.tests");
}
