package ca.ubc.icics.mss.dgms.dao;

import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import ca.ubc.icics.mss.dgms.model.Ad;

/**
 *
 * @author Jason
 */
public class AdDAOTest extends BaseDAOTestCase {

    private AdDAO dao = null;

    public void setAdDAO(AdDAO dao) {
        this.dao = dao;
    }

    public void testGetRandomPublishedImageAds() throws Exception {
        List ads = dao.getRandomPublishedImageAds(1);
        assertNotNull(ads);
        assertEquals(0, ads.size());
    }

    public void testGetRandomPublishedTextAds() throws Exception {
        List ads = dao.getRandomPublishedTextAds(1);
        assertNotNull(ads);
        Ad ad = (Ad) ads.get(0);
        assertEquals(1, ads.size());
        log.debug("adname: " + ad.getAdName());
    }
}
