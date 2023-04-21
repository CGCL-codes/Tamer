package gawky.service.oliva;

import gawky.message.Formatter;
import gawky.message.part.Desc;
import gawky.message.part.DescC;
import gawky.message.part.DescF;
import gawky.message.part.DescV;
import gawky.message.part.Reserved;

/**
 * @author Ingo Harbeck
 */
public class RequestPayment10 extends RequestPayment {

    public Desc[] getDesc() {
        return new Desc[] { new DescC("PAYM"), new DescC("10"), new DescF(Desc.FMT_9, Desc.CODE_R, 8, "date"), new DescF(Desc.FMT_9, Desc.CODE_R, 6, "time"), new DescF(Desc.FMT_A, Desc.CODE_R, 1, "creditdebit"), new DescF(Desc.FMT_A, Desc.CODE_R, 1, "encr"), new DescF(Desc.FMT_A, Desc.CODE_R, 1, "debitorcreditor"), new Reserved(3), new DescF(Desc.FMT_A, Desc.CODE_R, 3, "country_code"), new DescF(Desc.FMT_A, Desc.CODE_R, 3, "currency_code"), new DescF(Desc.FMT_9, Desc.CODE_R, 18, "amount"), new DescV(Desc.FMT_A, Desc.CODE_R, 64, "holdername", Desc.END01), new DescF(Desc.FMT_9, Desc.CODE_R, 2, "type_of_handling"), new DescF(Desc.FMT_A, Desc.CODE_R, 0, "paymentdetails") };
    }

    private String creditdebit = " ";

    private String encr = "0";

    private String debitorcreditor = " ";

    private String holdername = "";

    String TH01_reference_number = "";

    String TH00_cv2 = "";

    String TH00_auth_value = "";

    String TH02_order_number;

    String TH02_language_code;

    String TH02_reference_id;

    String TH02_invoice_number;

    String TH02_invvoice_date;

    String TH02_order_date;

    String TH04_IBAN;

    String TH04_BIC;

    String TH04_reference_number;

    static final String TOH_BIC = "04";

    public final void setIBANInformation(String iban, String bic, String reference) {
        this.type_of_handling = RequestPayment10.TOH_BIC;
        this.TH04_IBAN = iban;
        this.TH04_BIC = bic;
        this.TH04_reference_number = reference;
    }

    public final void setBankInformation(String bank_sortcode, String bank_accountnumber, String rib_code, String sloppy_flag, String reference_number) {
        this.type_of_handling = RequestPayment.TOH_DIRECTDEBIT;
        this.TH01_bank_ID = bank_sortcode;
        this.TH01_account_number = bank_accountnumber;
        this.TH01_RIB_code = rib_code;
        this.TH01_sloppy_flag = sloppy_flag;
        this.TH01_reference_number = reference_number;
    }

    public final void setCreditCardInformation(String credit_card_number, String expiry_date, String issue_date, String issue_number, String cv2, String auth_value) {
        this.type_of_handling = RequestPayment.TOH_CREDITCARD;
        this.TH00_credit_card_number = credit_card_number;
        this.TH00_expiry_date = expiry_date;
        this.TH00_issue_date = issue_date;
        this.TH00_issue_number = issue_number;
        this.TH00_cv2 = cv2;
        this.TH00_auth_value = auth_value;
    }

    public String getPaymentdetails() {
        StringBuilder buff = new StringBuilder();
        if (TOH_CREDITCARD.equals(type_of_handling)) {
            buff.append(Formatter.getStringC(48, TH00_credit_card_number));
            buff.append(Formatter.getSpacer(3));
            buff.append(Formatter.getStringN(4, TH00_expiry_date));
            buff.append(Formatter.getStringC(4, TH00_cv2));
            buff.append(Formatter.getStringC(4, TH00_issue_date));
            buff.append(Formatter.getStringC(2, TH00_issue_number));
            buff.append(Formatter.getStringC(48, TH00_auth_value));
        } else if (TOH_DIRECTDEBIT.equals(type_of_handling)) {
            buff.append(Formatter.getStringC(12, TH01_bank_ID));
            buff.append(Formatter.getSpacer(3));
            buff.append(Formatter.getStringC(80, TH01_account_number));
            buff.append(Formatter.getStringC(2, TH01_RIB_code));
            buff.append(Formatter.getStringC(1, TH01_sloppy_flag));
            buff.append(Formatter.getStringC(20, TH01_reference_number));
        } else if (TOH_BIC.equals(type_of_handling)) {
            buff.append(Formatter.getStringC(80, TH04_IBAN));
            buff.append(Formatter.getStringC(11, TH04_BIC));
            buff.append(Formatter.getStringC(20, TH04_reference_number));
        }
        return buff.toString();
    }

    public String getCreditdebit() {
        return creditdebit;
    }

    public void setCreditdebit(String creditdebit) {
        this.creditdebit = creditdebit;
    }

    public String getDebitorcreditor() {
        return debitorcreditor;
    }

    public void setDebitorcreditor(String debitorcreditor) {
        this.debitorcreditor = debitorcreditor;
    }

    public String getEncr() {
        return encr;
    }

    public void setEncr(String encr) {
        this.encr = encr;
    }

    public String getHoldername() {
        return holdername;
    }

    public void setHoldername(String holdername) {
        this.holdername = holdername;
    }
}
