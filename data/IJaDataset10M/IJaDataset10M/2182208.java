package xbrlcore.junit.linkbase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.junit.BeforeClass;
import org.junit.Test;
import xbrlcore.junit.sax.TestHelper;
import xbrlcore.linkbase.DefinitionLinkbase;
import xbrlcore.linkbase.LabelLinkbase;
import xbrlcore.linkbase.PresentationLinkbase;
import xbrlcore.taxonomy.Concept;
import xbrlcore.taxonomy.DiscoverableTaxonomySet;
import xbrlcore.xlink.Arc;
import xbrlcore.xlink.ExtendedLinkElement;
import xbrlcore.xlink.Locator;
import xbrlcore.xlink.Resource;
import static org.junit.Assert.*;

/**
 * This test class contains various test classes for linkbase tests.
 * @author Daniel
 */
public final class XBRLLinkbaseTest {

    /**
     * DTS used in this test case.
     */
    private static DiscoverableTaxonomySet prTaxonomy;

    /**
     * DTS used in this test case.
     */
    private static DiscoverableTaxonomySet taxProhibitedLink;

    /**
     * This method is executed before the test cases in this JUnit test.
     */
    @BeforeClass
    public static void setUp() {
        try {
            prTaxonomy = TestHelper.getDTS("xbrl/test/taxonomy_original/p-pr-2006-12-31.xsd");
            taxProhibitedLink = TestHelper.getDTS("xbrl/test/linkbase_test/tax_prohibited_link_2.xsd");
        } catch (Exception ex) {
            fail("Fehler beim Erstellen der Taxonomy pr: " + ex.getMessage());
        }
    }

    /**
     * @TODO: Add comment.
     */
    @Test
    public void getXLinkElementByLabel() {
        LabelLinkbase labelLinkbase = prTaxonomy.getLabelLinkbase();
        List<ExtendedLinkElement> xLinkElementProdukteList = labelLinkbase.getExtendedLinkElements("label_Produkte", null, null);
        assertEquals(1, xLinkElementProdukteList.size());
        ExtendedLinkElement xLinkElementProdukte = (ExtendedLinkElement) xLinkElementProdukteList.get(0);
        assertNotNull(xLinkElementProdukte);
        assertEquals("label_Produkte", xLinkElementProdukte.getLabel());
        assertEquals("http://www.xbrl.org/2003/role/link", xLinkElementProdukte.getExtendedLinkRole());
        assertEquals("de", ((Resource) xLinkElementProdukte).getLang());
        assertEquals("Produkte (domain)", ((Resource) xLinkElementProdukte).getValue());
        List<ExtendedLinkElement> xLinkElementProdukte2List = labelLinkbase.getExtendedLinkElements("label_Produkte_2", null, null);
        assertEquals(1, xLinkElementProdukte2List.size());
        ExtendedLinkElement xLinkElementProdukte2 = (ExtendedLinkElement) xLinkElementProdukte2List.get(0);
        assertNotNull(xLinkElementProdukte2);
        assertEquals("Products (domain)", ((Resource) xLinkElementProdukte2).getValue());
        List<ExtendedLinkElement> xLinkElementEssenList = labelLinkbase.getExtendedLinkElements("Essen", "http://www.xbrl.org/2003/role/link", null);
        assertEquals(1, xLinkElementEssenList.size());
        ExtendedLinkElement xLinkElementEssen = (ExtendedLinkElement) xLinkElementEssenList.get(0);
        assertNotNull(((Locator) xLinkElementEssen).getConcept());
        assertEquals("p-pr_Essen", ((Locator) xLinkElementEssen).getConcept().getId());
    }

    /**
     * TODO: Add comment.
     */
    @Test
    public void getXLinkElementByXBRLElement() {
        LabelLinkbase labelLinkbase = prTaxonomy.getLabelLinkbase();
        Concept elementEssen = prTaxonomy.getConceptByID("p-pr_Essen");
        ExtendedLinkElement xLinkElementEssen = labelLinkbase.getExtendedLinkElementFromBaseSet(elementEssen, null);
        assertNotNull(xLinkElementEssen);
        assertEquals("Essen", xLinkElementEssen.getLabel());
    }

    /**
     * TODO: Add comment.
     */
    @Test
    public void getTargetXLinkElements() {
        PresentationLinkbase presentationLinkbase = prTaxonomy.getPresentationLinkbase();
        Concept elementEssen = prTaxonomy.getConceptByID("p-pr_Essen");
        Concept elementGetraenke = prTaxonomy.getConceptByID("p-pr_Getraenke");
        List<ExtendedLinkElement> xLinkElementListEssen = presentationLinkbase.getTargetExtendedLinkElements(elementEssen, null);
        List<ExtendedLinkElement> xLinkElementListGetraenke = presentationLinkbase.getTargetExtendedLinkElements(elementGetraenke, null);
        assertEquals(2, xLinkElementListEssen.size());
        assertEquals(3, xLinkElementListGetraenke.size());
        ExtendedLinkElement xLinkElementWurst = (ExtendedLinkElement) xLinkElementListEssen.get(0);
        assertEquals("Wurst", xLinkElementWurst.getLabel());
        assertNotNull(((Locator) xLinkElementWurst).getConcept());
    }

    /**
     * TODO: Add comment.
     */
    @Test
    public void getSourceXLinkElements() {
        PresentationLinkbase presentationLinkbase = prTaxonomy.getPresentationLinkbase();
        Concept elementWurst = prTaxonomy.getConceptByID("p-pr_Wurst");
        List<ExtendedLinkElement> xLinkElementList = presentationLinkbase.getSourceExtendedLinkElements(elementWurst, "http://www.xbrl.org/2003/role/link");
        assertEquals(1, xLinkElementList.size());
        ExtendedLinkElement xLinkElementEssen = (ExtendedLinkElement) xLinkElementList.get(0);
        assertNotNull(xLinkElementEssen);
        assertEquals("Essen", xLinkElementEssen.getLabel());
        assertEquals("p-pr_Essen", ((Locator) xLinkElementEssen).getConcept().getId());
    }

    /**
     * TODO: Add comment.
     */
    @Test
    public void getXArcList() {
        PresentationLinkbase presentationLinkbase = prTaxonomy.getPresentationLinkbase();
        List<Arc> xArcList = presentationLinkbase.getArcBaseSet("http://www.xbrl.org/2003/arcrole/parent-child", null);
        assertEquals(10, xArcList.size());
        Arc xArc = (Arc) xArcList.get(0);
        assertEquals("http://www.xbrl.org/2003/arcrole/parent-child", xArc.getArcrole());
        assertEquals("http://www.xbrl.org/2003/role/link", xArc.getExtendedLinkRole());
        assertNull(xArc.getTargetRole());
        assertEquals(new Float(1.0f), new Float(xArc.getOrder()));
        List<String> arcroleList = new ArrayList<String>();
        arcroleList.add("test");
        arcroleList.add("quatsch");
        arcroleList.add("http://www.xbrl.org/2003/arcrole/parent-child");
        List<Arc> xArcList2 = presentationLinkbase.getArcBaseSet(arcroleList, "http://www.xbrl.org/2003/role/link");
        assertEquals(xArcList, xArcList2);
    }

    /**
     * TODO: Add comment.
     */
    @Test
    public void buildSourceNetwork() {
        DefinitionLinkbase defLinkbase = prTaxonomy.getDefinitionLinkbase();
        Set<ExtendedLinkElement> sourceNetwork = defLinkbase.buildSourceNetwork(prTaxonomy.getConceptByID("p-pr_Bier"), "http://xbrl.org/int/dim/arcrole/domain-member", null);
        assertEquals(3, sourceNetwork.size());
        Iterator<ExtendedLinkElement> sourceNetworkIterator = sourceNetwork.iterator();
        while (sourceNetworkIterator.hasNext()) {
            Concept currCon = ((Locator) (ExtendedLinkElement) sourceNetworkIterator.next()).getConcept();
            System.out.println(currCon.getId());
        }
    }

    /**
     * TODO: Add comment.
     */
    @Test
    public void getExtendedLinkElementFromBaseSet() {
        Concept elementEssen = prTaxonomy.getConceptByID("p-pr_Essen");
        Concept elementFleisch = prTaxonomy.getConceptByID("p-pr_Fleisch");
        Concept p0a = taxProhibitedLink.getConceptByID("p0_a");
        Concept p0b = taxProhibitedLink.getConceptByID("p0_b");
        assertNotNull(prTaxonomy.getDefinitionLinkbase().getExtendedLinkElementFromBaseSet(elementEssen, "http://www.xbrl.org/2003/role/link2"));
        assertNull(prTaxonomy.getDefinitionLinkbase().getExtendedLinkElementFromBaseSet(elementFleisch, "http://www.xbrl.org/2003/role/link2"));
        assertNotNull(taxProhibitedLink.getDefinitionLinkbase().getExtendedLinkElementFromBaseSet(p0a, null));
        assertNull(taxProhibitedLink.getDefinitionLinkbase().getExtendedLinkElementFromBaseSet(p0b, null));
    }

    /**
     * TODO: Add comment.
     */
    @Test
    public void buildTargetNetwork() {
        try {
            DefinitionLinkbase defLinkbase = prTaxonomy.getDefinitionLinkbase();
            Set<ExtendedLinkElement> targetNetwork = defLinkbase.buildTargetNetwork(prTaxonomy.getConceptByID("p-pr_Produkte"), "http://xbrl.org/int/dim/arcrole/domain-member", null);
            assertEquals(10, targetNetwork.size());
        } catch (Exception ex) {
            fail("Fehler beim Erstellen der Taxonomy pr: " + ex.getMessage());
        }
    }

    /**
     * TODO: Add comment.
     */
    @Test
    public void getArcForSourceLocator() {
        Concept elementEssen = prTaxonomy.getConceptByID("p-pr_Essen");
        Locator loc = (Locator) prTaxonomy.getDefinitionLinkbase().getExtendedLinkElementFromBaseSet(elementEssen, null);
        Arc arc = prTaxonomy.getDefinitionLinkbase().getArcForSourceLocator(loc);
        assertEquals("p-pr_Wurst", ((Locator) arc.getTargetElement()).getConcept().getId());
        Locator loc2 = (Locator) prTaxonomy.getDefinitionLinkbase().getExtendedLinkElementFromBaseSet(elementEssen, "http://www.xbrl.org/2003/role/link2");
        Arc arc2 = prTaxonomy.getDefinitionLinkbase().getArcForSourceLocator(loc2);
        assertNull(arc2);
        Arc arc3 = prTaxonomy.getDefinitionLinkbase().getArcForSourceLocator(null);
        assertNull(arc3);
    }

    /**
     * TODO: Add comment.
     */
    @Test
    public void getArcForTargetLocator() {
        Concept elementWurst = prTaxonomy.getConceptByID("p-pr_Wurst");
        Locator loc = (Locator) prTaxonomy.getDefinitionLinkbase().getExtendedLinkElementFromBaseSet(elementWurst, null);
        Arc arc = prTaxonomy.getDefinitionLinkbase().getArcForTargetLocator(loc);
        assertEquals("p-pr_Essen", ((Locator) arc.getSourceElement()).getConcept().getId());
        Arc arc2 = prTaxonomy.getDefinitionLinkbase().getArcForTargetLocator(null);
        assertNull(arc2);
    }
}
