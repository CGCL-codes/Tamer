package at.ac.univie.mminf.luceneSKOS.skos;

import java.util.Arrays;
import junit.framework.Assert;
import org.junit.Test;

/**
 * Tests the functionality of the Lucene-backed SKOS Engine implementation
 * 
 * @author Bernhard Haslhofer <bernhard.haslhofer@univie.ac.at>
 *
 */
public class SKOSEngineTest {

    @Test
    public void testSimpleSKOSSamplesRDFXML() throws Exception {
        String skosFile = "src/test/resources/skos_samples/simple_test_skos.rdf";
        SKOSEngine skosEngine = SKOSEngineFactory.getSKOSEngine(skosFile);
        Assert.assertEquals(2, skosEngine.getAltTerms("quick").length);
        Assert.assertEquals(1, skosEngine.getAltTerms("over").length);
    }

    @Test
    public void testSimpleSKOSSamplesN3() throws Exception {
        String skosFile = "src/test/resources/skos_samples/simple_test_skos.rdf";
        SKOSEngine skosEngine = SKOSEngineFactory.getSKOSEngine(skosFile);
        Assert.assertEquals(2, skosEngine.getAltTerms("quick").length);
        Assert.assertEquals(1, skosEngine.getAltTerms("over").length);
    }

    @Test
    public void testSKOSSpecSamples() throws Exception {
        String skosFile = "src/test/resources/skos_samples/skos_spec_samples.n3";
        SKOSEngine skosEngine = SKOSEngineFactory.getSKOSEngine(skosFile);
        Assert.assertEquals(3, skosEngine.getAltTerms("animals").length);
        Assert.assertEquals(1, skosEngine.getAltTerms("Food and Agriculture Organization").length);
        Assert.assertEquals(4, skosEngine.getMaxPrefLabelTerms());
    }

    @Test
    public void testSKOSSpecSamplesWithLanguageRestriction() throws Exception {
        String skosFile = "src/test/resources/skos_samples/skos_spec_samples.n3";
        SKOSEngine skosEngine = SKOSEngineFactory.getSKOSEngine(skosFile, "en");
        String[] altTerms = skosEngine.getAltTerms("animals");
        Assert.assertEquals(1, altTerms.length);
        Assert.assertEquals("creatures", altTerms[0]);
    }

    @Test
    public void testUKATSamples() throws Exception {
        String skosFile = "src/test/resources/skos_samples/ukat_examples.n3";
        String conceptURI = "http://www.ukat.org.uk/thesaurus/concept/859";
        SKOSEngine skosEngine = SKOSEngineFactory.getSKOSEngine(skosFile);
        String[] prefLabel = skosEngine.getPrefLabels(conceptURI);
        Assert.assertEquals(1, prefLabel.length);
        Assert.assertEquals("weapons", prefLabel[0]);
        String[] altLabel = skosEngine.getAltLabels(conceptURI);
        Assert.assertEquals(2, altLabel.length);
        Assert.assertTrue(Arrays.asList(altLabel).contains("armaments"));
        Assert.assertTrue(Arrays.asList(altLabel).contains("arms"));
        String[] broader = skosEngine.getBroaderConcepts(conceptURI);
        Assert.assertEquals(1, broader.length);
        Assert.assertEquals("http://www.ukat.org.uk/thesaurus/concept/5060", broader[0]);
        String[] narrower = skosEngine.getNarrowerConcepts(conceptURI);
        Assert.assertEquals(2, narrower.length);
        Assert.assertTrue(Arrays.asList(narrower).contains("http://www.ukat.org.uk/thesaurus/concept/18874"));
        Assert.assertTrue(Arrays.asList(narrower).contains("http://www.ukat.org.uk/thesaurus/concept/7630"));
        String[] broaderLabels = skosEngine.getBroaderLabels(conceptURI);
        Assert.assertEquals(3, broaderLabels.length);
        Assert.assertTrue(Arrays.asList(broaderLabels).contains("military equipment"));
        Assert.assertTrue(Arrays.asList(broaderLabels).contains("defense equipment and supplies"));
        Assert.assertTrue(Arrays.asList(broaderLabels).contains("ordnance"));
        String[] narrowerLabels = skosEngine.getNarrowerLabels(conceptURI);
        Assert.assertEquals(2, narrowerLabels.length);
        Assert.assertTrue(Arrays.asList(narrowerLabels).contains("ammunition"));
        Assert.assertTrue(Arrays.asList(narrowerLabels).contains("artillery"));
    }
}
