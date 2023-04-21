package de.mindcrimeilab.xsanalyzer;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNamespaceItem;
import org.apache.xerces.xs.XSNamespaceItemList;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSTypeDefinition;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import de.mindcrimeilab.xsanalyzer.util.XSModelHelper;
import de.mindcrimeilab.xsanalyzer.util.XsModelFactory;

/**
 * @author Michael Engelhardt<me@mindcrime-ilab.de>
 * @author $Author: agony $
 * @version $Revision: 165 $
 * 
 */
public class UnusedTypeFinderWorkerTest {

    private static final Log logger = LogFactory.getLog("xsAnalyzerJunitTestLogger");

    private XSModel model;

    private static final String NS = "http://mindcrime-ilab.de/xsdanalyzer/test/unusedTypes";

    private static final List<String> usedTypes = Arrays.asList(new String[] { UnusedTypeFinderWorkerTest.NS + ":usedSimpleType1", UnusedTypeFinderWorkerTest.NS + ":usedSimpleType2", UnusedTypeFinderWorkerTest.NS + ":sndLevelUsedSimpleType", UnusedTypeFinderWorkerTest.NS + ":usedComplexType2", UnusedTypeFinderWorkerTest.NS + ":usedComlexType", UnusedTypeFinderWorkerTest.NS + ":restrictedNestedUsedType", UnusedTypeFinderWorkerTest.NS + ":restrictedUsedType", UnusedTypeFinderWorkerTest.NS + ":usedAttributeType", UnusedTypeFinderWorkerTest.NS + ":usedNestetAttributeType", UnusedTypeFinderWorkerTest.NS + ":usedNestedAttribute" });

    private static final List<String> unusedTypes = Arrays.asList(new String[] { UnusedTypeFinderWorkerTest.NS + ":unusedSimpleType", UnusedTypeFinderWorkerTest.NS + ":unusedComlexType", UnusedTypeFinderWorkerTest.NS + ":RestrictedNestedUser" });

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        model = XsModelFactory.createXsModel(new File("src/test/resources/xsd/unusedTypes.xsd"));
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for
     * {@link de.mindcrimeilab.xsanalyzer.UnusedTypeFinder#findUnusedTypes(org.apache.xerces.xs.XSModel)} .
     */
    @Test
    public final void testGetUnusedTypes() {
        final XSNamespaceItemList nsList = model.getNamespaceItems();
        final List<XSNamespaceItem> namespaceItems = new LinkedList<XSNamespaceItem>();
        for (int i = 0; i < nsList.getLength(); ++i) {
            XSNamespaceItem item = nsList.item(i);
            namespaceItems.add(item);
        }
        Set<? extends XSTypeDefinition> definedTypes = XSModelHelper.getComponents(model, namespaceItems);
        UnusedTypeFinderWorker finder = new UnusedTypeFinderWorker(definedTypes);
        finder.setFilteredNamespaces(new String[] { "http://www.w3.org/2001/XMLSchema" });
        ComponentVisitor visitor = new ComponentVisitor();
        XsModelWalker walker = new XsModelWalker();
        walker.addWorker(finder);
        walker.addWorker(visitor);
        walker.walkModel(model);
        Set<XSTypeDefinition> unusedTypes = finder.getUnusedTypes();
        Assert.assertNotNull(unusedTypes != null);
        Assert.assertFalse(unusedTypes.isEmpty());
        for (XSTypeDefinition t : unusedTypes) {
            if (!t.getNamespace().equals("http://www.w3.org/2001/XMLSchema")) {
                final String qname = t.getNamespace() + ":" + t.getName();
                UnusedTypeFinderWorkerTest.logger.debug("Found unused type [" + qname + "]-->[" + t + "]");
                Assert.assertFalse("Found defined used type in unused types list -->[" + qname + "].", UnusedTypeFinderWorkerTest.usedTypes.contains(qname));
            }
        }
        Map<XSObject, Integer> visits = visitor.getVisits();
        for (XSObject object : visits.keySet()) {
            System.out.println("- [{" + object.getNamespace() + "}:" + object.getName() + "] visited [" + visits.get(object) + "] times.");
        }
    }
}
