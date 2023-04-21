package org.hip.vif.bom.impl.test;

import java.sql.SQLException;
import junit.framework.TestCase;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.exc.VException;
import org.hip.vif.bom.impl.BOMHelper;
import org.hip.vif.bom.impl.Ratings;
import org.hip.vif.bom.impl.RatingsHome;

/**
 * @author Luthiger
 * Created 05.09.2009
 */
public class RatingsHomeTest extends TestCase {

    private static final Long ratingEventID = new Long(884);

    private static final Long raterID = new Long(62);

    private static final Long ratedID = new Long(439);

    private DataHouseKeeper data;

    public RatingsHomeTest(String name) {
        super(name);
        data = DataHouseKeeper.getInstance();
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        data.deleteAllInAll();
    }

    public void testSaveRating() throws Exception {
        RatingsHome lHome = BOMHelper.getRatingsHome();
        assertEquals("count 0", 0, lHome.getCount());
        createRating(lHome, ratingEventID, raterID, ratedID);
        createRating(lHome, ratingEventID, ratedID, raterID);
        assertEquals("count 1", 2, lHome.getCount());
        Ratings lRatings;
        lRatings = (Ratings) lHome.findByKey(getRatingKey());
        assertTrue("rating is open", checkOpen(lRatings));
        lHome.saveRating(ratingEventID, raterID, "0", "0", "0", "");
        lRatings = (Ratings) lHome.findByKey(getRatingKey());
        assertFalse("rating is closed", checkOpen(lRatings));
    }

    private void createRating(RatingsHome lHome, Long inRatingID, Long inRaterID, Long inRatedID) throws SQLException, VException {
        Ratings lRatings = (Ratings) lHome.create();
        lRatings.set(RatingsHome.KEY_RATINGEVENTS_ID, inRatingID);
        lRatings.set(RatingsHome.KEY_RATER_ID, inRaterID);
        lRatings.set(RatingsHome.KEY_RATED_ID, inRatedID);
        lRatings.insert();
    }

    private boolean checkOpen(Ratings inRatings) throws VException {
        return inRatings.get(RatingsHome.KEY_EFFICIENCY) == null && inRatings.get(RatingsHome.KEY_ETIQUETTE) == null;
    }

    private KeyObject getRatingKey() throws VException {
        KeyObject outKey = new KeyObjectImpl();
        outKey.setValue(RatingsHome.KEY_RATINGEVENTS_ID, ratingEventID);
        outKey.setValue(RatingsHome.KEY_RATER_ID, raterID);
        outKey.setValue(RatingsHome.KEY_RATED_ID, ratedID);
        return outKey;
    }

    public void testCheckRatingCompleted() throws Exception {
        RatingsHome lHome = BOMHelper.getRatingsHome();
        assertEquals("count 0", 0, lHome.getCount());
        createRating(lHome, ratingEventID, raterID, ratedID);
        createRating(lHome, ratingEventID, ratedID, raterID);
        assertEquals("count 1", 2, lHome.getCount());
        assertFalse("check completed 0", lHome.checkRatingCompleted(ratingEventID));
        lHome.saveRating(ratingEventID, raterID, "0", "0", "0", "");
        assertTrue("completed for 1. rater", lHome.checkRatingCompleted(ratingEventID, ratedID));
        assertFalse("not completed for 2. rater", lHome.checkRatingCompleted(ratingEventID, raterID));
        assertFalse("check completed 1", lHome.checkRatingCompleted(ratingEventID));
        lHome.saveRating(ratingEventID, ratedID, "0", "-1", "1", "");
        assertTrue("completed for 2. rater", lHome.checkRatingCompleted(ratingEventID, raterID));
        assertTrue("check completed 2", lHome.checkRatingCompleted(ratingEventID));
    }

    public void testIsInvolved() throws Exception {
        RatingsHome lHome = BOMHelper.getRatingsHome();
        assertEquals("count 0", 0, lHome.getCount());
        createRating(lHome, ratingEventID, raterID, ratedID);
        createRating(lHome, ratingEventID, ratedID, raterID);
        assertEquals("count 1", 2, lHome.getCount());
        assertTrue("first is involved", lHome.isInvolved(ratingEventID, ratedID));
        assertTrue("second is involved", lHome.isInvolved(ratingEventID, raterID));
        assertFalse("not involved", lHome.isInvolved(ratingEventID, raterID + 1));
    }
}
