package uk.ac.osswatch.simal.integrationTest.model;

import static org.junit.Assert.assertTrue;
import java.util.Iterator;
import org.junit.Test;
import uk.ac.osswatch.simal.integrationTest.model.repository.BaseRepositoryTest;
import uk.ac.osswatch.simal.model.IDocument;
import uk.ac.osswatch.simal.rdf.SimalRepositoryException;

public class TestHomepage extends BaseRepositoryTest {

    @Test
    public void testURL() throws SimalRepositoryException {
        Iterator<IDocument> homepages = project1.getHomepages().iterator();
        boolean hasHomepageOne = false;
        boolean hasHomepageTwo = false;
        while (homepages.hasNext()) {
            IDocument homepage = homepages.next();
            if (homepage.getURI().toString().equals(TEST_SIMAL_PROJECT_HOMEPAGE_URL_ONE)) {
                hasHomepageOne = true;
            } else if (homepage.getURI().toString().equals(TEST_SIMAL_PROJECT_HOMEPAGE_URL_TWO)) {
                hasHomepageTwo = true;
            }
        }
        assertTrue("Homepage ONE is missing", hasHomepageOne);
        assertTrue("Homepage TWO is missing", hasHomepageTwo);
    }

    @Test
    public void testNames() {
        boolean hasHomepageOne = false;
        boolean hasHomepageTwo = false;
        Iterator<IDocument> homepages = project1.getHomepages().iterator();
        String label;
        while (homepages.hasNext()) {
            IDocument homepage = homepages.next();
            label = homepage.getLabel();
            if (label.equals(TEST_SIMAL_PROJECT_HOMEPAGE_NAME_ONE)) {
                hasHomepageOne = true;
            } else if (label.equals(TEST_SIMAL_PROJECT_HOMEPAGE_NAME_TWO)) {
                hasHomepageTwo = true;
            }
        }
        assertTrue("Homepages do not include " + TEST_SIMAL_PROJECT_HOMEPAGE_NAME_ONE, hasHomepageOne);
        assertTrue("Homepages do not include " + TEST_SIMAL_PROJECT_HOMEPAGE_NAME_TWO, hasHomepageTwo);
    }
}
