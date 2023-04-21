package com.ericsson.xsmp.service.cdr.parser;

import com.ericsson.xsmp.commons.KeyValuePair;
import com.ericsson.xsmp.service.cdr.Parser;
import com.ericsson.xsmp.service.cdr.Record;
import com.ericsson.xsmp.service.payment.Journal;

public class PaymentJournalParser implements Parser {

    private boolean additional = false;

    public boolean isAdditional() {
        return additional;
    }

    public void setAdditional(boolean additional) {
        this.additional = additional;
    }

    public Object parse(String id, Object event) {
        Journal chargingLog = (Journal) event;
        Record record = new Record();
        record.addField("CDRID", id);
        record.addField("Timestamp", chargingLog.getTimestamp());
        record.addField("LogID", chargingLog.getJournalId());
        record.addField("InitiatorID", chargingLog.getInitiatorId());
        if (chargingLog.xgetInitiatorType() == null) record.addField("InitiatorType", ""); else record.addField("InitiatorType", chargingLog.getInitiatorType());
        record.addField("InitiatorName", chargingLog.getInitiatorName());
        record.addField("DestID", chargingLog.getDestId());
        if (chargingLog.xgetDestType() == null) record.addField("DestType", ""); else record.addField("DestType", chargingLog.getDestType());
        record.addField("DestName", chargingLog.getDestName());
        record.addField("PayerID", chargingLog.getPayerId());
        if (chargingLog.xgetPayerType() == null) record.addField("PayerType", ""); else record.addField("PayerType", chargingLog.getPayerType());
        record.addField("PayerName", chargingLog.getPayerName());
        record.addField("PayerAccount", chargingLog.getPayerAccountId());
        record.addField("PayeeID", chargingLog.getPayeeId());
        if (chargingLog.xgetPayeeType() == null) record.addField("PayeeType", ""); else record.addField("PayeeType", chargingLog.getPayeeType());
        record.addField("PayeeName", chargingLog.getPayeeName());
        record.addField("PayeeAccount", chargingLog.getPayeeAccountId());
        record.addField("SPID", chargingLog.getSPID());
        record.addField("SPName", chargingLog.getSPName());
        record.addField("ServiceID", chargingLog.getServiceId());
        record.addField("ServiceType", chargingLog.getServiceType());
        record.addField("ServiceName", chargingLog.getServiceName());
        record.addField("CPID", chargingLog.getCPID());
        record.addField("CPName", chargingLog.getCPName());
        record.addField("ContentID", chargingLog.getContentId());
        record.addField("ContentType", chargingLog.getContentType());
        record.addField("ContentName", chargingLog.getContentName());
        record.addField("ProductID", chargingLog.getProductId());
        record.addField("ProductName", chargingLog.getProductName());
        record.addField("VASFlag", chargingLog.getVASFlag() ? 1 : 0);
        record.addField("PackageID", chargingLog.getPackageId());
        record.addField("PackageName", chargingLog.getPackageName());
        record.addField("Currency", chargingLog.getCurrency());
        record.addField("Amount", chargingLog.getAmount());
        record.addField("ChargingType", chargingLog.getChargingType());
        if (chargingLog.xgetCount() == null) record.addField("Count", ""); else record.addField("Count", chargingLog.getCount());
        if (chargingLog.xgetUnitPrice() == null) record.addField("UnitPrice", ""); else record.addField("UnitPrice", chargingLog.getUnitPrice());
        if (isAdditional()) {
            KeyValuePair[] prop = chargingLog.getParameterArray();
            for (int i = 0; i < prop.length; i++) record.addField(prop[i].getName(), prop[i].getValue());
        }
        return record;
    }

    public boolean accept(Object event) {
        return event instanceof Journal;
    }
}
