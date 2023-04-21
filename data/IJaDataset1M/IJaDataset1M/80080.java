package com.inet.qlcbcc.mdb.pub.adapter.support;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.webos.core.util.StringUtils;
import com.inet.qlcbcc.mdb.pub.AbstractMessagePublisher;
import com.inet.qlcbcc.mdb.pub.adapter.ReceivingRecordMessagePublisher;

/**
 * ReceivingRecordMessagePublisherSupport.
 *
 * @author Dung Nguyen
 * @version $Id: ReceivingRecordMessagePublisherSupport.java 2011-08-12 16:47:42z nguyen_dv $
 *
 * @since 1.0
 */
public class ReceivingRecordMessagePublisherSupport extends AbstractMessagePublisher implements ReceivingRecordMessagePublisher {

    private static final Logger LOG = LoggerFactory.getLogger(ReceivingRecordMessagePublisherSupport.class);

    @Value("${amqp.adapter.receiving.record.pattern}")
    private String routingKey;

    public void sendRequest(String code, boolean force) {
        try {
            final JSONObject message = new JSONObject();
            if (StringUtils.hasText(code)) {
                message.accumulate("code", code);
            }
            message.accumulate("force-update", force);
            rabbitTemplate.convertAndSend(routingKey, message);
        } catch (JSONException jex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Could not send request to adapter with code [{}], force update mode [{}].", new Object[] { code, force }, jex);
            } else if (LOG.isWarnEnabled()) {
                LOG.warn("Could not send request to adapter with code [{}], force update mode [{}] and message [{}]", new Object[] { code, force, jex.getMessage() });
            }
        }
    }
}
