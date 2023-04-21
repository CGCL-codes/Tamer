package com.liferay.portlet.polls.ejb;

import java.io.Serializable;
import com.liferay.util.StringPool;

/**
 * <a href="PollsVotePK.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.78 $
 *
 */
public class PollsVotePK implements Comparable, Serializable {

    public String questionId;

    public String userId;

    public PollsVotePK() {
    }

    public PollsVotePK(String questionId, String userId) {
        this.questionId = questionId;
        this.userId = userId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int compareTo(Object obj) {
        if (obj == null) {
            return -1;
        }
        PollsVotePK pk = (PollsVotePK) obj;
        int value = 0;
        value = questionId.compareTo(pk.questionId);
        if (value != 0) {
            return value;
        }
        value = userId.compareTo(pk.userId);
        if (value != 0) {
            return value;
        }
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        PollsVotePK pk = null;
        try {
            pk = (PollsVotePK) obj;
        } catch (ClassCastException cce) {
            return false;
        }
        if ((questionId.equals(pk.questionId)) && (userId.equals(pk.userId))) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        return (questionId + userId).hashCode();
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(StringPool.OPEN_CURLY_BRACE);
        sb.append("questionId");
        sb.append(StringPool.EQUAL);
        sb.append(questionId);
        sb.append(StringPool.COMMA);
        sb.append(StringPool.SPACE);
        sb.append("userId");
        sb.append(StringPool.EQUAL);
        sb.append(userId);
        sb.append(StringPool.CLOSE_CURLY_BRACE);
        return sb.toString();
    }
}
