package net.mogray.infinitypfm.core.data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wayne
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Transaction implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private long tranId;

    private int actId;

    private String actName = null;

    private String actTypeName;

    private Date tranDate;

    private String tranMemo = null;

    private float tranAmount = 0;

    private int actOffset = -1;

    private String actOffsetName = null;

    private int tranMonth = 0;

    private int tranYear = 0;

    private DataFormatUtil dateUtil = null;

    private long recurTranId;

    /**
	 * 
	 */
    public Transaction() {
        super();
    }

    /**
	 * @return Returns the actId.
	 */
    public int getActId() {
        return actId;
    }

    /**
	 * @param actId The actId to set.
	 */
    public void setActId(int actId) {
        this.actId = actId;
    }

    /**
	 * @return Returns the tranCreditAmount.
	 */
    public float getTranAmount() {
        return tranAmount;
    }

    /**
	 * @param tranCreditAmount The tranCreditAmount to set.
	 */
    public void setTranAmount(float amt) {
        this.tranAmount = amt;
    }

    /**
	 * @return Returns the tranDate.
	 */
    public Date getTranDate() {
        return tranDate;
    }

    /**
	 * @param tranDate The tranDate to set.
	 */
    public void setTranDate(Date tranDate) {
        this.tranDate = tranDate;
        if (tranDate != null) {
            this.dateUtil = new DataFormatUtil();
            this.dateUtil.setDate(tranDate);
            tranMonth = dateUtil.getMonth();
            tranYear = dateUtil.getYear();
        }
    }

    /**
	 * @return Returns the tranId.
	 */
    public long getTranId() {
        return tranId;
    }

    /**
	 * @param tranId The tranId to set.
	 */
    public void setTranId(long tranId) {
        this.tranId = tranId;
    }

    /**
	 * @return Returns the tranMemo.
	 */
    public String getTranMemo() {
        return tranMemo;
    }

    /**
	 * @param tranMemo The tranMemo to set.
	 */
    public void setTranMemo(String tranMemo) {
        this.tranMemo = tranMemo;
    }

    public String getTranDateFmt() {
        return dateUtil.getFormat("M-dd-yyyy");
    }

    /**
	 * @return Returns the actOffset.
	 */
    public int getActOffset() {
        return actOffset;
    }

    /**
	 * @param actOffset The actOffset to set.
	 */
    public void setActOffset(int actOffset) {
        this.actOffset = actOffset;
    }

    /**
	 * @return Returns the tranMonth.
	 */
    public int getTranMonth() {
        return tranMonth;
    }

    /**
	 * @return Returns the tranYear.
	 */
    public int getTranYear() {
        return tranYear;
    }

    public String getActTypeName() {
        return actTypeName;
    }

    public void setActTypeName(String actTypeName) {
        this.actTypeName = actTypeName;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getActOffsetName() {
        return actOffsetName;
    }

    public void setActOffsetName(String actOffsetName) {
        this.actOffsetName = actOffsetName;
    }

    public long getRecurTranId() {
        return recurTranId;
    }

    public void setRecurTranId(long recurTranId) {
        this.recurTranId = recurTranId;
    }
}
