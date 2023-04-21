package org.riverock.forum.bean;

import java.util.List;

/**
 * @author Sergei Maslyukov
 *         Date: 29.05.2006
 *         Time: 14:14:44
 */
public class ForumIntegrityBean {

    private boolean isValid = true;

    private int countLostUser = 0;

    private List<Long> lostUserId = null;

    public int getCountLostUser() {
        return countLostUser;
    }

    public void setCountLostUser(int countLostUser) {
        this.countLostUser = countLostUser;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public List<Long> getLostUserId() {
        return lostUserId;
    }

    public void setLostUserId(List<Long> lostUserId) {
        this.lostUserId = lostUserId;
    }
}
