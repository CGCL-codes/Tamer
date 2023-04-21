package com.aipo.social.core.model;

import java.util.List;
import com.aipo.social.opensocial.model.Activity;

/**
 * @see org.apache.shindig.social.core.model.ActivityImpl
 */
public class ActivityImpl extends org.apache.shindig.social.core.model.ActivityImpl implements Activity {

    private List<String> recipients;

    /**
   * @return
   */
    public List<String> getRecipients() {
        return recipients;
    }

    /**
   * @param userIds
   */
    public void setRecipients(List<String> userIds) {
        this.recipients = userIds;
    }
}
