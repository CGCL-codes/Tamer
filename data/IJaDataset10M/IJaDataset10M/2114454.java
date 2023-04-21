package org.ala.documentmappers;

import java.util.List;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.ala.documentmapper.IssgDocumentMapper;
import org.ala.repository.ParsedDocument;
import org.ala.repository.Triple;
import org.ala.util.DebugUtils;
import org.ala.util.WebUtils;
import org.junit.Test;

/**
 *
 * @author Tommy Wang (twang@wollemisystems.com)
 */
public class IssgDocumentMapperTest extends TestCase {

    final String hasScientificName = "Abelmoschus moschatus";

    final String[] hasDescriptiveText = { "Abelmoschus moschatus is a weedy, herbaceous plant that is native to " + "India, parts of China and tropical Asia, and some Pacific islands. It is cultivated in India for the " + "musk-like oil contained in its seeds, which is valued for perfume manufacture. It is considered a weed " + "in open and disturbed areas. It has been found to be a suitable host plant for the insect Dysdercus " + "cingulatus, which is a serious pest of cotton crops.", "Abelmoschus moschatus is an herbaceous trailing plant that grows to 2m in diameter with soft, hairy " + "stems. It can grow up to 1.5m tall. Leaves are alternate, rough, hairy and heart-shaped. They have 3 " + "to 5 lobes and can grow to 15cm long. Flowers resemble those of the hibiscus and are usually " + "watermelon pink, although they are sometimes white or cream in colour. They last for only one day " + "and their flowering depends on the timing of the wet season.Seeds are contained within hairy capsules " + "up to 8cm long, which are tough but papery. A delicate musk-like odour is produced by the seed coat. " + "(Mishra et. al , 2000; PIER, 2003; Townsend, 2000)." };

    final String[] hasHabitatText = { "agricultural areas, natural forests, planted forests, range/grasslands, " + "riparian zones, ruderal/disturbed, scrub/shrublands, urban areas, wetlands", "Abelmoschus moschatus grows in a range of habitats, from marshy areas to forest edges, at elevations " + "of up to 450m. Commonly found in disturbed, open areas, as well as gardens, plantations and ricefields " + "(PIER, 2003). Is able to grow on salt-affected wastelands (Mishra & Naik, 2000)." };

    final String hasDistributionText = "Native Range : Native to India, southern China, tropical Asia and some " + "parts of the Pacific. Known introduced range : Introduced to Fiji and Tonga.";

    final String hasReproductionText = "Propagation can be from seeds, small tubers, or stem cuttings (Townsend, 2000).";

    final String hasSynonym = "Hibiscus abelmoschus L.";

    @Test
    public void test() throws Exception {
        IssgDocumentMapper dm = new IssgDocumentMapper();
        String uri = "http://www.issg.org/database/species/ecology.asp?si=15&fr=1&sts=&lang=EN";
        String xml = WebUtils.getHTMLPageAsXML(uri);
        List<ParsedDocument> parsedDocs = dm.map(uri, xml.getBytes());
        for (ParsedDocument pd : parsedDocs) {
            DebugUtils.debugParsedDoc(pd);
            List<Triple<String, String, String>> triples = pd.getTriples();
            for (Triple triple : triples) {
                String predicate = (String) triple.getPredicate();
                String object = (String) triple.getObject();
                if (predicate.endsWith("hasScientificName")) {
                    Assert.assertEquals(object, hasScientificName);
                }
                if (predicate.endsWith("hasDistributionText")) {
                    Assert.assertEquals(object, hasDistributionText);
                }
                if (predicate.endsWith("hasReproductionText")) {
                    Assert.assertEquals(object, hasReproductionText);
                }
                if (predicate.endsWith("hasSynonym")) {
                    Assert.assertEquals(object, hasSynonym);
                }
                if (predicate.endsWith("hasDescriptiveText")) {
                    Assert.assertTrue(arrayContainsElement(hasDescriptiveText, object));
                }
                if (predicate.endsWith("hasHabitatText")) {
                    Assert.assertTrue(arrayContainsElement(hasHabitatText, object));
                }
            }
        }
    }

    private boolean arrayContainsElement(String[] strArray, String element) {
        for (int i = 0; i < strArray.length; i++) {
            if (element.equals(strArray[i])) {
                return true;
            }
        }
        return false;
    }
}
