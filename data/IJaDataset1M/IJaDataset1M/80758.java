package org.notima.bg.lb;

import org.notima.bg.BgParseException;
import org.notima.bg.BgRecord;
import org.notima.bg.BgUtil;

public class LbTk4Record extends BgRecord {

    private int m_recipientNo;

    private String m_swift;

    private String m_iban;

    public LbTk4Record(int recipientNo, String swift, String iban) {
        super("4");
        m_recipientNo = recipientNo;
        m_swift = swift;
        m_iban = iban;
    }

    @Override
    public BgRecord parse(String line) throws BgParseException {
        return null;
    }

    @Override
    public String toRecordString() {
        StringBuffer line = new StringBuffer(getTransCode());
        line.append(BgUtil.fillToLength(new Integer(m_recipientNo).toString(), true, '0', 7));
        line.append(BgUtil.fillToLength(m_iban.toUpperCase(), false, ' ', 59));
        line.append(BgUtil.fillToLength(m_swift.toUpperCase(), false, ' ', 11));
        line.append("BP");
        while (line.length() < 80) {
            line.append(" ");
        }
        return (line.toString());
    }
}
