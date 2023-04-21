package com.ericsson.xsmp.service.cdr.parser;

import com.ericsson.xsmp.service.cdr.AbstractCDR;
import com.ericsson.xsmp.service.cdr.Record;
import com.ericsson.xsmp.service.cdr.SMSCDR;

public class SmsRecordParser extends AbstractRecordParser {

    public void fillDetailFields(Record record, AbstractCDR cdr) {
        SMSCDR smsCdr = (SMSCDR) cdr;
        record.addField("MsgId", smsCdr.getMsgId());
        record.addField("SourceAddress", smsCdr.getSourceAddress());
        record.addField("DestAddress", smsCdr.getDestAddress());
        record.addField("Operation", smsCdr.getOperation());
        record.addField("Count", smsCdr.getCount());
    }

    public boolean accept(Object event) {
        return event instanceof SMSCDR;
    }
}
