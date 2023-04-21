package com.liferay.portlet.polls.ejb;

/**
 * <a href="PollsVoteHBMUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.2 $
 *
 */
public class PollsVoteHBMUtil {

    public static com.liferay.portlet.polls.model.PollsVote model(PollsVoteHBM pollsVoteHBM) {
        com.liferay.portlet.polls.model.PollsVote pollsVote = PollsVotePool.get(pollsVoteHBM.getPrimaryKey());
        if (pollsVote == null) {
            pollsVote = new com.liferay.portlet.polls.model.PollsVote(pollsVoteHBM.getQuestionId(), pollsVoteHBM.getUserId(), pollsVoteHBM.getChoiceId(), pollsVoteHBM.getVoteDate());
            PollsVotePool.put(pollsVote.getPrimaryKey(), pollsVote);
        }
        return pollsVote;
    }
}
