package com.liferay.portlet.polls.service.impl;

import com.liferay.portal.PortalException;
import com.liferay.portal.SystemException;
import com.liferay.portal.security.auth.PrincipalException;
import com.liferay.portal.security.permission.ActionKeys;
import com.liferay.portlet.polls.model.PollsVote;
import com.liferay.portlet.polls.service.base.PollsVoteServiceBaseImpl;
import com.liferay.portlet.polls.service.permission.PollsQuestionPermission;

/**
 * <a href="PollsVoteServiceImpl.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class PollsVoteServiceImpl extends PollsVoteServiceBaseImpl {

    public PollsVote addVote(long questionId, long choiceId) throws PortalException, SystemException {
        return addVote(questionId, choiceId, null);
    }

    public PollsVote addVote(long questionId, long choiceId, String comment) throws PortalException, SystemException {
        long userId = 0;
        try {
            userId = getUserId();
        } catch (PrincipalException pe) {
            userId = counterLocalService.increment();
        }
        PollsQuestionPermission.check(getPermissionChecker(), questionId, ActionKeys.ADD_VOTE);
        return pollsVoteLocalService.addVote(userId, questionId, choiceId, comment);
    }
}
