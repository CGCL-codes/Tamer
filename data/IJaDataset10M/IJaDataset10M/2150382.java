package se.notima.bg.autogiro;

import se.notima.bg.BgParseException;
import se.notima.bg.BgRecord;
import se.notima.bg.BgSettings;
import se.notima.bg.BgUtil;

/**
 * Autogiro mandate record.
 * 
 * @author Ilya Ismagilov ismagilov@gmail.com
 *
 */
public class AgMandateRecord extends AgRecord {

    protected static final String RECORD_FORMAT = "%1$2.2s%2$10.10s%3$16.16s%4$4.4s%5$12.12s%6$12.12s%7$20.20s%8$2.2s%9$2.2s%10$s";

    public AgMandateRecord(String code) {
        super(code);
    }

    @Override
    public BgRecord fromRecordString(String line) throws BgParseException {
        return this;
    }

    @Override
    public String toRecordString() {
        return String.format(RECORD_FORMAT, transCode, BgUtil.rightAlignNumber(10, payeeBankgiroNumber), BgUtil.rightAlignNumber(16, payerNumber), "", "", "", "", "", "", BgSettings.RECORDS_DELIMITER);
    }
}
