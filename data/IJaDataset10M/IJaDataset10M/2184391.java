package com.once.servicescout.data.qos.impl;

import com.once.servicescout.config.QoSConfig;
import com.once.servicescout.data.qos.QoSMeta;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author cwchen
 * @Created 2008-1-16
 * @Contact comain@gmail.com
 */
public class ThroughoutputImpl extends QoSMetaImpl {

    public ThroughoutputImpl(QoSBean bean, QoSConfig qconfig) {
        super(bean, qconfig);
    }

    Log log = LogFactory.getLog(ThroughoutputImpl.class);

    public String getName() {
        return qconfig.getAttributeName(ThroughoutputImpl.class);
    }

    public boolean getDirection() {
        return qconfig.getAttributeDirection(ThroughoutputImpl.class);
    }

    public String getValueLiteral() {
        return bean.getQosvalue();
    }

    public int compareTo(QoSMeta o) {
        if (o instanceof ThroughoutputImpl == false) {
            log.error("Uncomparable types in throughoutput comparision");
            return -2;
        }
        float value = Float.parseFloat(bean.getQosvalue());
        float ov = Float.parseFloat(o.getValueLiteral());
        if (value == ov) return 0;
        if (getDirection()) {
            return value > ov ? 1 : -1;
        }
        return value < ov ? 1 : -1;
    }

    public float getAbsValue() {
        return Float.parseFloat(bean.getQosvalue());
    }
}
