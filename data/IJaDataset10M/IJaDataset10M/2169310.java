package com.inet.qlcbcc.mdb.pub.adapter.support;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import com.inet.qlcbcc.mdb.pub.AbstractMessagePublisher;
import com.inet.qlcbcc.mdb.pub.adapter.UserTaskConfigurationMessagePublisher;

/**
 * UserTaskConfigurationMessagePublisher.
 *
 * @author Duyen Tang
 * @version $Id: UserTaskConfigurationMessagePublisher.java 2011-08-10 11:15:46z tttduyen $
 *
 * @since 1.0
 */
public class UserTaskConfigurationMessagePublisherSupport extends AbstractMessagePublisher implements UserTaskConfigurationMessagePublisher {

    @Value("${amqp.adapter.user.task.config.pattern}")
    private String routingKey;

    public void sendRefresh() {
        rabbitTemplate.convertAndSend(routingKey, new JSONObject());
    }
}
