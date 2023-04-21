package com.liferay.portlet.polls.ejb;

/**
 * <a href="PollsQuestionManagerUtil.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.83 $
 *
 */
public class PollsQuestionManagerUtil {

    public static final String PORTLET_ID = "25";

    public static com.liferay.portlet.polls.model.PollsQuestion addQuestion(java.lang.String portletId, java.lang.String groupId, java.lang.String title, java.lang.String description, int expMonth, int expDay, int expYear, boolean neverExpires, java.util.List choices) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            PollsQuestionManager pollsQuestionManager = PollsQuestionManagerFactory.getManager();
            return pollsQuestionManager.addQuestion(portletId, groupId, title, description, expMonth, expDay, expYear, neverExpires, choices);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static void checkQuestions() throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            PollsQuestionManager pollsQuestionManager = PollsQuestionManagerFactory.getManager();
            pollsQuestionManager.checkQuestions();
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static void deleteQuestion(java.lang.String questionId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            PollsQuestionManager pollsQuestionManager = PollsQuestionManagerFactory.getManager();
            pollsQuestionManager.deleteQuestion(questionId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static com.liferay.portlet.polls.model.PollsQuestion getQuestion(java.lang.String questionId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            PollsQuestionManager pollsQuestionManager = PollsQuestionManagerFactory.getManager();
            return pollsQuestionManager.getQuestion(questionId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static java.util.List getQuestions(java.lang.String portletId, java.lang.String groupId, java.lang.String companyId) throws com.liferay.portal.SystemException {
        try {
            PollsQuestionManager pollsQuestionManager = PollsQuestionManagerFactory.getManager();
            return pollsQuestionManager.getQuestions(portletId, groupId, companyId);
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static java.util.List getQuestions(java.lang.String portletId, java.lang.String groupId, java.lang.String companyId, int begin, int end) throws com.liferay.portal.SystemException {
        try {
            PollsQuestionManager pollsQuestionManager = PollsQuestionManagerFactory.getManager();
            return pollsQuestionManager.getQuestions(portletId, groupId, companyId, begin, end);
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static int getQuestionsSize(java.lang.String portletId, java.lang.String groupId, java.lang.String companyId) throws com.liferay.portal.SystemException {
        try {
            PollsQuestionManager pollsQuestionManager = PollsQuestionManagerFactory.getManager();
            return pollsQuestionManager.getQuestionsSize(portletId, groupId, companyId);
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static boolean hasVoted(java.lang.String questionId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            PollsQuestionManager pollsQuestionManager = PollsQuestionManagerFactory.getManager();
            return pollsQuestionManager.hasVoted(questionId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static com.liferay.portlet.polls.model.PollsQuestion updateQuestion(java.lang.String questionId, java.lang.String title, java.lang.String description, int expMonth, int expDay, int expYear, boolean neverExpires, java.util.List choices) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            PollsQuestionManager pollsQuestionManager = PollsQuestionManagerFactory.getManager();
            return pollsQuestionManager.updateQuestion(questionId, title, description, expMonth, expDay, expYear, neverExpires, choices);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static void vote(java.lang.String questionId, java.lang.String choiceId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            PollsQuestionManager pollsQuestionManager = PollsQuestionManagerFactory.getManager();
            pollsQuestionManager.vote(questionId, choiceId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }

    public static boolean hasAdmin(java.lang.String questionId) throws com.liferay.portal.PortalException, com.liferay.portal.SystemException {
        try {
            PollsQuestionManager pollsQuestionManager = PollsQuestionManagerFactory.getManager();
            return pollsQuestionManager.hasAdmin(questionId);
        } catch (com.liferay.portal.PortalException pe) {
            throw pe;
        } catch (com.liferay.portal.SystemException se) {
            throw se;
        } catch (Exception e) {
            throw new com.liferay.portal.SystemException(e);
        }
    }
}
