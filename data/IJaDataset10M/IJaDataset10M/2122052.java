package se.notima.bg;

import java.io.IOException;
import java.util.Currency;
import java.util.Date;
import java.util.Vector;

public abstract class BgSet<T extends BgEntry> implements Recordable {

    protected Vector<T> entries = new Vector<T>();

    protected BgHeader header;

    protected BgFooter footer;

    public BgHeader getHeader() {
        return header;
    }

    public void setHeader(BgHeader header) {
        this.header = header;
    }

    public BgFooter getFooter() {
        return footer;
    }

    public void setFooter(BgFooter footer) {
        this.footer = footer;
    }

    public void setEntries(Vector<T> entries) {
        this.entries = entries;
    }

    public Vector<T> getEntries() {
        return entries;
    }

    public void addEntry(T entry) {
        entries.add(entry);
    }

    public void fromRecordStream(BgRecordsReader reader, BgFactory<? extends BgEntry> factory) throws BgParseException, IOException {
        while (reader.hasLine()) {
            String line = reader.readLine();
            BgRecord record = factory.createRecord(line);
            if (isStart(record)) {
                if (null == header) {
                    header = (BgHeader) record.fromRecordString(line);
                } else {
                    reader.reset();
                    return;
                }
            } else if (isEnd(record)) {
                footer = (BgFooter) record.fromRecordString(line);
                return;
            } else {
                reader.reset();
                T entry = (T) factory.createEntry(line);
                entry.fromRecordStream(reader, factory);
                entries.add(entry);
            }
        }
    }

    public boolean isStart(BgRecord record) {
        return record instanceof BgHeader;
    }

    public boolean isEnd(BgRecord record) {
        return record instanceof BgFooter;
    }

    public String toRecordString() {
        StringBuilder toRet = new StringBuilder();
        if (null != header) toRet.append(header.toRecordString());
        if (null != entries) {
            for (Recordable entry : entries) {
                toRet.append(entry.toRecordString());
            }
        }
        if (null != footer) toRet.append(footer.toRecordString());
        return toRet.toString();
    }

    public void setRecords(Vector<T> records) {
        this.entries = records;
    }

    public Vector<T> getRecords() {
        return entries;
    }

    public void addTransaction(T trans) {
        entries.add(trans);
    }

    public abstract Date getCreditRecordDate(String recipientBg, double amount);

    public abstract Currency getCurrency();

    public abstract String getRecipientBankAccount();

    public abstract String getSenderBankAccount();
}
