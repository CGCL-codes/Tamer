package com.qbrowser.localstore.genericdest;

import javax.jms.JMSException;

/**
 *
 * @author takemura
 */
public class LocalTopic extends com.qbrowser.localstore.genericdest.LocalDestination implements javax.jms.Topic {

    public LocalTopic(String name) {
        this.nameofthis = name;
    }

    @Override
    public String getTopicName() throws JMSException {
        return this.nameofthis;
    }
}
