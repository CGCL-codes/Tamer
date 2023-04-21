package net.bzzt.swift.mt940.exporter;

import net.bzzt.swift.mt940.Mt940Entry;
import net.bzzt.swift.mt940.Mt940Entry.SollHabenKennung;
import net.bzzt.swift.mt940.Mt940File;
import net.bzzt.swift.mt940.Mt940Record;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jgnash.imports.ImportBank;
import jgnash.imports.ImportTransaction;

/**
 * The Mt940 Exporter converts a parsed Mt940File to jGnash-specific
 * Double-entry Transaction objects.
 *
 * @author arnouten
 * @version $Id: Mt940Exporter.java 2322 2010-08-30 00:30:32Z ccavanaugh $
 */
public class Mt940Exporter {

    private Mt940Exporter() {
    }

    public static ImportBank convert(Mt940File file) {
        ImportBank retval = new ImportBank();
        retval.setTransactions(convertTransactions(file));
        return retval;
    }

    /**
     * Convert an entire Mt940File to Transactions
     *
     * @param file file to import
     * @return list of import transactions
     */
    static List<ImportTransaction> convertTransactions(Mt940File file) {
        List<ImportTransaction> retval = new ArrayList<ImportTransaction>();
        for (Mt940Record record : file.getRecords()) {
            for (Mt940Entry entry : record.getEntries()) {
                retval.add(convert(entry));
            }
        }
        return retval;
    }

    /**
     * Convert a single Mt940-entry to a jGnash-Transaction
     *
     * @param entry Mt940Entry to convert
     * @return new import transaction
     */
    private static ImportTransaction convert(Mt940Entry entry) {
        BigDecimal amount;
        String memo = entry.getMehrzweckfeld();
        Date date = entry.getValutaDatum();
        if (entry.getSollHabenKennung() == SollHabenKennung.CREDIT) {
            amount = entry.getBetrag();
        } else if (entry.getSollHabenKennung() == SollHabenKennung.DEBIT) {
            amount = BigDecimal.valueOf(0L).subtract(entry.getBetrag());
        } else {
            throw new UnsupportedOperationException("SollHabenKennung " + entry.getSollHabenKennung() + " not supported");
        }
        return new ImportTransaction(null, amount, date, memo);
    }
}
