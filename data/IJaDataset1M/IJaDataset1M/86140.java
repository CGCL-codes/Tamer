package se.notima.bg.bgmax;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import se.notima.bg.BgFooter;
import se.notima.bg.BgParseException;

/**
 * Presents BgMax record with transaction code 70: trailer record.
 * 
 * @author Daniel Tamm daniel@notima.se
 * @author Ilya Ismagilov ismagilov@gmail.com
 */
public class BgMaxTk70Footer extends BgFooter {

    public static final String TC = "70";

    private static Pattern linePattern = Pattern.compile(TC + "(\\d{8})(\\d{8})(\\d{8})(\\d{8}) {46}.*");

    private int paymentRecordsNumber;

    private int deductionRecordsNumber;

    private int extraReferenceRecordsNumber;

    private int depositesNumber;

    public BgMaxTk70Footer() {
        super(TC);
    }

    @Override
    public BgFooter fromRecordString(String line) throws BgParseException {
        Matcher m = linePattern.matcher(line);
        if (m.matches()) {
            paymentRecordsNumber = Integer.valueOf(m.group(1));
            deductionRecordsNumber = Integer.valueOf(m.group(2));
            extraReferenceRecordsNumber = Integer.valueOf(m.group(3));
            depositesNumber = Integer.valueOf(m.group(4));
            return this;
        } else {
            throw new BgParseException("Trailer record doesn`t match expected pattern", line);
        }
    }

    @Override
    public String toRecordString() {
        return "";
    }

    public int getPaymentRecordsNumber() {
        return paymentRecordsNumber;
    }

    public int getDeductionRecordsNumber() {
        return deductionRecordsNumber;
    }

    public int getExtraReferenceRecordsNumber() {
        return extraReferenceRecordsNumber;
    }

    public int getDepositesNumber() {
        return depositesNumber;
    }
}
