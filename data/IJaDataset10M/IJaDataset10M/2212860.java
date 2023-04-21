package org.hip.vif.usersettings.rating;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;
import org.hip.kernel.bom.KeyObject;
import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.bom.KeyObject.BinaryBooleanOperator;
import org.hip.kernel.bom.impl.KeyObjectImpl;
import org.hip.kernel.bom.impl.SQLNull;
import org.hip.kernel.exc.VException;
import org.hip.kernel.servlet.HtmlView;
import org.hip.vif.bom.impl.BOMHelper;
import org.hip.vif.bom.impl.JoinRatingsToRater;
import org.hip.vif.bom.impl.RatingsHome;
import org.hip.vif.servlets.ForumGroupManager;
import org.hip.vif.servlets.VIFContext;
import org.hip.vif.usersettings.Activator;
import org.hip.vif.usersettings.views.ShowRatingView;
import org.hip.vif.usertasks.IUserTask;

/**
 * User task: after publishing a contribution, the author and reviewer have to rate the interaction.
 *
 * @author Luthiger
 * Created: 27.08.2009
 */
public class RatingUserTask implements IUserTask {

    public String getId() {
        return Activator.getBundleName() + ".rating";
    }

    /**
	 * Checks whether the specified user has open rating tasks.
	 * 
	 * @param inMemberID Long
	 * @return boolean <code>true</code> if the task is open for the user, else <code>false</code>.
	 * @throws VException 
	 */
    public boolean isOpen(Long inMemberID) throws VException, SQLException {
        return BOMHelper.getRatingsHome().getCount(createKeyOpenTasks(inMemberID)) != 0;
    }

    public Collection<HtmlView> createUserTaskViews(VIFContext inContext, Long inMemberID) throws Exception {
        Collection<HtmlView> outViews = new Vector<HtmlView>();
        QueryResult lQuery = BOMHelper.getJoinRatingsToRaterHome().select(createKeyOpenTasks(inMemberID));
        while (lQuery.hasMoreElements()) {
            JoinRatingsToRater lRating = (JoinRatingsToRater) lQuery.nextAsDomainObject();
            outViews.add(new ShowRatingView(inContext, lRating, lRating.getFullName(), lRating.getQuestionsToBeRated(), lRating.getCompletionsToBeRated(), lRating.getTextsToBeRated()));
        }
        if (outViews.size() > 0) {
            outViews.add(ForumGroupManager.renderBibliographyLookupScript(inContext.getRequestURL(), 500, 620));
        }
        return outViews;
    }

    /**
	 * SELECT * FROM tblRatings WHERE RaterID = 1 AND nEfficiency IS NULL AND nEtiquette IS NULL
	 * 
	 * @param inMemberID
	 * @return KeyObject
	 * @throws VException
	 */
    private KeyObject createKeyOpenTasks(Long inMemberID) throws VException {
        KeyObject outKey = new KeyObjectImpl();
        outKey.setValue(RatingsHome.KEY_RATER_ID, inMemberID);
        outKey.setValue(RatingsHome.KEY_EFFICIENCY, SQLNull.getInstance(), "IS", BinaryBooleanOperator.AND);
        outKey.setValue(RatingsHome.KEY_ETIQUETTE, SQLNull.getInstance(), "IS", BinaryBooleanOperator.AND);
        return outKey;
    }
}
