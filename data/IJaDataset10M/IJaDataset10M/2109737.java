package org.hip.vif.forum.groups.data;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.hip.kernel.bom.GeneralDomainObject;
import org.hip.kernel.bom.QueryResult;
import org.hip.kernel.exc.VException;
import org.hip.vif.core.bom.CompletionHome;
import org.hip.vif.core.bom.ResponsibleHome;
import org.hip.vif.core.util.BeanWrapperHelper;
import org.hip.vif.forum.groups.util.AuthorReviewerRenderHelper;
import org.hip.vif.forum.groups.util.AuthorReviewerRenderHelper.MemberWrapper;

/**
 * Helper class to handle a question's completions<br />
 * Reason: the query to retrieve the question's completions may retrieve each completion twice,
 * first with the author data and second with the reviewer data.<br/>
 * This helper class has to normalize such multiple entries. 
 * 
 * @author Luthiger
 * Created: 27.10.2011
 */
public class CompletionsHelper {

    /**
	 * Normalize (i.e. remove duplicates) the list of completions.
	 * 
	 * @param inCompletions {@link QueryResult} the set of completions linked to authors and reviewers
	 * @return List of {@link Completion} the normalized list of completions
	 * @throws VException
	 * @throws SQLException
	 */
    public static List<Completion> getNormalizedCompletions(QueryResult inCompletions) throws VException, SQLException {
        Map<Long, Completion> lCompletions = new HashMap<Long, CompletionsHelper.Completion>();
        List<Long> lCompletionIDs = new Vector<Long>();
        while (inCompletions.hasMoreElements()) {
            GeneralDomainObject lModel = inCompletions.nextAsDomainObject();
            Long lCompletionID = BeanWrapperHelper.getLong(CompletionHome.KEY_ID, lModel);
            Completion lCompletion = lCompletions.get(lCompletionID);
            if (lCompletion == null) {
                lCompletions.put(lCompletionID, new Completion(lModel));
                lCompletionIDs.add(lCompletionID);
            } else {
                lCompletion.addContributor(lModel);
            }
        }
        List<Completion> out = new Vector<CompletionsHelper.Completion>();
        for (Long lCompletionID : lCompletionIDs) {
            out.add(lCompletions.get(lCompletionID));
        }
        return out;
    }

    public static class Completion {

        private String completionText;

        private String completionState;

        private String completionDate;

        private Collection<MemberWrapper> authors = new Vector<AuthorReviewerRenderHelper.MemberWrapper>();

        private Collection<MemberWrapper> reviewers = new Vector<AuthorReviewerRenderHelper.MemberWrapper>();

        private Completion(GeneralDomainObject inModel) {
            completionText = BeanWrapperHelper.getString(CompletionHome.KEY_COMPLETION, inModel);
            completionState = BeanWrapperHelper.getString(CompletionHome.KEY_STATE, inModel);
            completionDate = BeanWrapperHelper.getFormattedDate(CompletionHome.KEY_MUTATION, inModel);
            addContributor(inModel);
        }

        private void addContributor(GeneralDomainObject inModel) {
            MemberWrapper lContributor = AuthorReviewerRenderHelper.wrapMember(inModel);
            Integer lType = BeanWrapperHelper.getInteger(ResponsibleHome.KEY_TYPE, inModel);
            if (ResponsibleHome.Type.AUTHOR.check(lType)) {
                authors.add(lContributor);
            } else if (ResponsibleHome.Type.REVIEWER.check(lType)) {
                reviewers.add(lContributor);
            }
        }

        /**
		 * @return String the completion's text
		 */
        public String getCompletionText() {
            return completionText;
        }

        /**
		 * @return String the completion's state
		 */
        public String getState() {
            return completionState;
        }

        public String getFormattedDate() {
            return completionDate;
        }

        /**
		 * @return Collection of {@link MemberWrapper} this completion's authors
		 */
        public Collection<MemberWrapper> getAuthors() {
            return authors;
        }

        /**
		 * @return Collection of {@link MemberWrapper} this completion's reviewers
		 */
        public Collection<MemberWrapper> getReviewers() {
            return reviewers;
        }
    }
}
