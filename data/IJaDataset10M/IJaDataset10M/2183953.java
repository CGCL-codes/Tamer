package se.notima.bg.bgmax;

import java.io.IOException;
import java.util.Currency;
import java.util.Date;
import se.notima.bg.BgEntry;
import se.notima.bg.BgFactory;
import se.notima.bg.BgParseException;
import se.notima.bg.BgRecordsReader;
import se.notima.bg.BgSet;
import se.notima.bg.BgStructureException;

/**
 * BgMax deposit set.
 * 
 * @author Ilya Ismagilov ismagilov@gmail.com
 * @author Daniel Tamm daniel@notima.se
 *
 */
public class BgMaxSet extends BgSet<BgMaxReceipt> {

    @Override
    public void fromRecordStream(BgRecordsReader reader, BgFactory<? extends BgEntry> factory) throws BgParseException, IOException {
        super.fromRecordStream(reader, factory);
        double total = 0.0;
        BgMaxTk15Record setFooter = (BgMaxTk15Record) footer;
        for (BgMaxReceipt receipt : entries) {
            receipt.setPaymentDate(setFooter.getPaymentDate());
            receipt.setCurrency(setFooter.getCurrency());
            try {
                total += receipt.getAmount();
            } catch (BgStructureException ex) {
                throw new BgParseException("Receipt was not parsed correctly", ex);
            }
        }
        if (total != setFooter.getAmount()) {
            throw new BgParseException("Sum of entries amounts: " + total + " and footer sum: " + setFooter.getAmount() + " are not equal");
        }
    }

    @Override
    public String toRecordString() {
        return "";
    }

    @Override
    public Currency getCurrency() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRecipientBankAccount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSenderBankAccount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Date getCreditRecordDate(String recipientBg, double amount) {
        throw new UnsupportedOperationException();
    }
}
