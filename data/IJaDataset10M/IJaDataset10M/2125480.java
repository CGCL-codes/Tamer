package org.lhuillier.pwsafe.io.codec;

import org.lhuillier.pwsafe.model.RawField;
import org.lhuillier.pwsafe.model.Record;
import com.google.inject.Inject;

class EmailAddressConverter implements FieldConverter {

    private static final byte TYPE_ID = 0x14;

    private final BinConverter binConv;

    @Inject
    EmailAddressConverter(BinConverter binConv) {
        this.binConv = binConv;
    }

    @Override
    public byte handledTypeId() {
        return TYPE_ID;
    }

    @Override
    public void toRecord(RawField raw, Record record) {
        record.setEmailAddress(binConv.readString(raw.getValue()));
    }

    @Override
    public void toRaw(Record record, RawRecord raw) {
        String value = record.getEmailAddress();
        if (value != null) {
            raw.addField(new RawField(TYPE_ID, binConv.writeString(value)));
        }
    }
}
