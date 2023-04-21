package nl.gamayun.whohasit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import nl.gamayun.whohasit.utils.DurationFormat;
import nl.gamayun.whohasit.utils.WhoHasItUtils;

public class Loan {

    public static String LOAN_KEY = "loanID";

    private long loanID;

    private Calendar date;

    private Calendar returnDate;

    private String header;

    private String detail;

    private long contactId;

    private String loanerName;

    private TYPE type;

    List<String> errors = new ArrayList<String>();

    public Loan(Calendar returnDate) {
        this.date = Calendar.getInstance();
        this.returnDate = returnDate;
        this.header = "";
        this.detail = "";
        this.loanerName = "";
        this.type = TYPE.lent_from;
    }

    public Loan(Calendar returnDate, String header, String detail, long contactId, String loanerName, TYPE type) {
        this.date = Calendar.getInstance();
        this.returnDate = returnDate;
        this.header = header;
        this.detail = detail;
        this.contactId = contactId;
        this.loanerName = loanerName;
        this.type = type;
    }

    public Loan(long LoanId, Calendar date, Calendar returnDate, String header, String detail, long contactId, String loanerName, TYPE type) {
        this.loanID = LoanId;
        this.date = date;
        this.returnDate = returnDate;
        this.header = header;
        this.detail = detail;
        this.contactId = contactId;
        this.loanerName = loanerName;
        this.type = type;
    }

    public Loan(Loan loan) {
        this.loanID = loan.getLoanID();
        this.date = loan.getDate();
        this.returnDate = loan.getReturnDate();
        this.header = loan.getHeader();
        this.detail = loan.getDetail();
        this.contactId = loan.getContactId();
        this.loanerName = loan.getLoanerName();
        this.type = loan.getType();
    }

    public long getLoanID() {
        return loanID;
    }

    public void setLoanID(long loanID) {
        this.loanID = loanID;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Calendar getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Calendar returnDate) {
        this.returnDate = returnDate;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public long getContactId() {
        return contactId;
    }

    public void setContactId(long contactId) {
        this.contactId = contactId;
    }

    public String getLoanerName() {
        return loanerName;
    }

    public void setLoanerName(String loanerName) {
        this.loanerName = loanerName;
    }

    public TYPE getType() {
        return type;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

    public String toString() {
        return WhoHasItUtils.LOAN_DATE_TIME_FORMAT.format(date.getTime()) + ", " + type.getTypeValue() + " " + loanerName + ": " + header;
    }

    public String getDetailFull() {
        StringBuffer sb = new StringBuffer();
        sb.append(header);
        sb.append("\n\r");
        sb.append(type.getTypeValue() + ": " + loanerName);
        sb.append("\n\r");
        sb.append(DurationFormat.getDurationFromTodayBig(returnDate));
        sb.append("\n\r");
        sb.append(detail);
        return sb.toString();
    }

    public String getDetailSmall() {
        StringBuffer sb = new StringBuffer();
        sb.append(header);
        sb.append("\n\r");
        sb.append(type.getTypeValue() + ": " + loanerName);
        sb.append("\n\r");
        sb.append(DurationFormat.getDurationFromTodayBig(returnDate));
        return sb.toString();
    }

    public String getNotify() {
        StringBuffer sb = new StringBuffer();
        sb.append(header + " " + type.getTypeValue() + " " + loanerName);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        Loan compare = (Loan) o;
        return getDetailSmall().equals(compare.getDetailSmall());
    }

    public List<String> isValid() {
        errors.clear();
        if (header == null || header.trim().equals("")) {
            errors.add(WhoHasIt.getContext().getString(R.string.loan_error_1));
        }
        if (loanerName == null || loanerName.trim().equals("")) {
            errors.add(WhoHasIt.getContext().getString(R.string.loan_error_2));
        }
        return errors;
    }

    public boolean isToLate() {
        return Calendar.getInstance().after(returnDate);
    }
}
