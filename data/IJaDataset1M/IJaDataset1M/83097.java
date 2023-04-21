package net.sourceforge.gateway.sstp.rules.ser;

import java.math.BigDecimal;
import net.sourceforge.gateway.sstp.efile.x2010V01.ACHEntityDetailType;
import net.sourceforge.gateway.sstp.efile.x2010V01.FinancialTransactionType;
import net.sourceforge.gateway.sstp.efile.x2010V01.SSTSimplifiedReturnTransmissionDocument;
import net.sourceforge.gateway.sstp.efile.x2010V01.SimplifiedReturnDocumentType;
import net.sourceforge.gateway.sstp.rules.BaseRule;

/**
 * TransmissionPaymentHash in Transmission Header does not equal sum of all
 * payments in the Transmission
 */
public class Rule000021 extends BaseRule implements SimplifiedReturnTransmissionRule {

    public boolean validate(SSTSimplifiedReturnTransmissionDocument transmission, String transmitter) {
        if (transmission.getSSTSimplifiedReturnTransmission().getTransmissionHeader().isSetTransmissionPaymentHash()) {
            BigDecimal total = transmission.getSSTSimplifiedReturnTransmission().getTransmissionHeader().getTransmissionPaymentHash().getBigDecimalValue();
            BigDecimal sum = new BigDecimal(0.0);
            SimplifiedReturnDocumentType[] docs = transmission.getSSTSimplifiedReturnTransmission().getSimplifiedReturnDocumentArray();
            for (int i = 0; i < docs.length; i++) {
                if (docs[i].isSetFinancialTransaction()) {
                    FinancialTransactionType ft = docs[i].getFinancialTransaction();
                    ACHEntityDetailType payment = ft.getStatePayment();
                    sum = payment.getPaymentAmount();
                }
            }
            if (transmission.getSSTSimplifiedReturnTransmission().isSetFinancialTransaction()) {
                FinancialTransactionType ft = transmission.getSSTSimplifiedReturnTransmission().getFinancialTransaction();
                ACHEntityDetailType payment = ft.getStatePayment();
                sum = payment.getPaymentAmount();
            }
            if (sum.compareTo(total) == 0) {
                return true;
            } else {
                this.setErrorCode("000021");
                this.setXPath("/SSTSimplifiedReturnTransmission/TransmissionHeader/TransmissionPaymentHash");
                this.setErrorMessage("TransmissionPaymentHash in Transmission Header does not equal sum of all payments in the Transmission");
                this.setDataValue(transmission.getSSTSimplifiedReturnTransmission().getTransmissionHeader().getTransmissionPaymentHash().getBigDecimalValue().toString());
                return false;
            }
        } else {
            return true;
        }
    }
}
