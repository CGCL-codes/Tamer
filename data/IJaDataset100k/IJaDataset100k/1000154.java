package test.model.data;

import model.data.Ad;
import junit.framework.TestCase;

public class AdTest extends TestCase {

    public void testHashCode() {
        Ad ad = new Ad("id", "enddate");
        assertEquals(ad.hashCode(), "id".hashCode());
    }

    public void testAd() {
        assertNotNull(new Ad("id", "enddate"));
    }

    public void testEqualsObject() {
        Ad ad = new Ad("123", "enddate");
        Ad ad2 = new Ad("id2", "enddate");
        Ad ad3 = new Ad("123", "enddate");
        assertEquals(ad, ad3);
        assertNotSame(ad, ad2);
    }

    public void testGetEndDate() {
        Ad ad = new Ad("id", "enddate");
        assertEquals(ad.getEndDate(), "enddate");
    }
}
