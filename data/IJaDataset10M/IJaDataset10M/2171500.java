package at.nullpointer.trayrss.test.checks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import at.nullpointer.trayrss.gui.tablemodel.FeedTableValidator;

public class FeedTableValidatorTest {

    @Test
    public void testCheckURLFalse() {
        String erg = "test";
        assertFalse(FeedTableValidator.checkURL(erg));
    }

    @Test
    public void testCheckURLValidUrl() {
        String erg = "http://god.sprossenwanne.at";
        assertFalse(FeedTableValidator.checkURL(erg));
    }

    @Test
    public void testCheckURLValidFeed() {
        String erg = "http://www.nullpointer.at/feed/";
        assertTrue(FeedTableValidator.checkURL(erg));
    }
}
