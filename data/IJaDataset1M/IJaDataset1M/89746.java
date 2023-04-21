package org.subrecord.kit.fluent.impl;

import java.util.Map;
import org.subrecord.kit.SubRecordApiException;
import org.subrecord.kit.fluent.ITracing;
import org.subrecord.kit.fluent.SubRecordBuilder;

/**
 * @author przemek
 * 
 */
public class TracingImpl extends SubRecordBuilder implements ITracing {

    private SubRecordBuilder parent;

    public TracingImpl(String host, int port, String user, String password, SubRecordBuilder parent) {
        super(host, port, user, password, false);
        this.parent = parent;
    }

    @Override
    public SubRecordBuilder trace(String domain, String transactionId, String rq, String rs, Map<String, String> properties) throws SubRecordApiException {
        subrecord.trace(parent.getToken(), domain, transactionId, rq, rs, properties);
        return this;
    }
}
