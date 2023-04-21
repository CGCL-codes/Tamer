package lc.commonservice.impl.mthlystatement;

import java.util.Date;
import java.util.Map;
import lc.accessDB.tl.TLLpDetail;
import lc.commonservice.exceptions.LCCalculationException;
import lc.commonservice.impl.LCLoanItemDetail;
import lc.commonservice.impl.LCLoanStatusSummary;
import lc.init.TLCConstants;
import lc.ui.util.TLCUtil;

/**
 * This object gives the details of the Loan Item record at a certain point in time.
 * @author paresh
 *
 */
public class LCMTLLoanItemDetail extends LCLoanItemDetail {

    private double m_principalRecievedYearToDate;

    private double m_interestRecievedYearToDate;

    private double m_lateFeesRecievedYearToDate;

    private double m_lcFeesPaidYearToDate;

    private double m_principalRecievedToDate;

    private double m_principalRecieved;

    private double m_interestRecieved;

    private double m_lateFeesRecieved;

    private double m_lcFeesPaid;

    private Date m_chngD;

    private Date m_lpdStatusD;

    private boolean m_isNoFee;

    public LCMTLLoanItemDetail(Map<String, Object> result) {
        super(result);
        this.m_principalRecievedYearToDate = result.get("YTODATEPRIN") != null ? Double.valueOf(result.get("YTODATEPRIN").toString()) : 0;
        this.m_interestRecievedYearToDate = result.get("YTODATEINT") != null ? Double.valueOf(result.get("YTODATEINT").toString()) : 0;
        this.m_lateFeesRecievedYearToDate = result.get("YTODATEFEES") != null ? Double.valueOf(result.get("YTODATEFEES").toString()) : 0;
        this.m_lcFeesPaidYearToDate = result.get("YTODATELCFEES") != null ? Double.valueOf(result.get("YTODATELCFEES").toString()) : 0;
        this.m_principalRecieved = result.get("MTL_PRINCIPAL_RECIEVED") != null ? Double.valueOf(result.get("MTL_PRINCIPAL_RECIEVED").toString()) : 0;
        this.m_interestRecieved = result.get("MTL_INTEREST_RECIEVED") != null ? Double.valueOf(result.get("MTL_INTEREST_RECIEVED").toString()) : 0;
        this.m_lateFeesRecieved = result.get("MTL_LATE_FEES_RECIEVED") != null ? Double.valueOf(result.get("MTL_LATE_FEES_RECIEVED").toString()) : 0;
        this.m_lcFeesPaid = result.get("MTL_LCFEES_RECIEVED") != null ? Double.valueOf(result.get("MTL_LCFEES_RECIEVED").toString()) : 0;
        this.m_principalRecievedToDate = result.get("TODATEPRIN") != null ? Double.valueOf(result.get("TODATEPRIN").toString()) : 0;
        this.m_chngD = (Date) result.get("CHNG_D");
        this.m_lpdStatusD = (Date) result.get("LPD_STATUS_D");
        this.m_isNoFee = true;
    }

    public void validateData(String debugString) throws LCCalculationException {
        TLCUtil.validate(this.getCommittedAmount(), TLCConstants.SENSITIVITY_ERROR, debugString + " commitedAmount");
        TLCUtil.validate(this.getMTLPrincipalReceived(), TLCConstants.SENSITIVITY_ERROR, debugString);
        TLCUtil.validate(this.getMTLInterestReceived(), TLCConstants.SENSITIVITY_ERROR, debugString);
        TLCUtil.validate(this.getMTLLateFeesReceived(), TLCConstants.SENSITIVITY_ERROR, debugString);
        TLCUtil.validate(this.getPrincipalRecievedYearToDate(), TLCConstants.SENSITIVITY_ERROR, debugString);
        TLCUtil.validate(this.getInterestRecievedYearToDate(), TLCConstants.SENSITIVITY_ERROR, debugString);
        TLCUtil.validate(this.getLateFeesRecievedYearToDate(), TLCConstants.SENSITIVITY_ERROR, debugString);
        TLCUtil.validate(this.getPrincipalRemainingYearToDate(), TLCConstants.SENSITIVITY_ERROR, debugString);
        TLCUtil.validate(this.getPrincipalRemainingToDate(), TLCConstants.SENSITIVITY_ERROR, debugString);
        TLCUtil.validate((this.getPrincipalRecievedYearToDate() - this.getMTLPrincipalReceived()), TLCConstants.SENSITIVITY_ERROR, debugString);
        TLCUtil.validate((this.getInterestRecievedYearToDate() - this.getMTLInterestReceived()), TLCConstants.SENSITIVITY_ERROR, debugString);
    }

    public double getPrincipalRemainingYearToDate() {
        if (this.getStatusSummary() == LCLoanStatusSummary.ExpiredRemoved || this.getLPDStatus() == TLLpDetail.Status.SOLD || this.getLPDStatus() == TLLpDetail.Status.MOVED) return new Double(0);
        return this.getCommittedAmount() - this.m_principalRecievedYearToDate;
    }

    public double getPrincipalRemainingToDate() {
        if (this.getStatusSummary() == LCLoanStatusSummary.ExpiredRemoved || this.getLPDStatus() == TLLpDetail.Status.SOLD || this.getLPDStatus() == TLLpDetail.Status.MOVED) return new Double(0);
        return this.getCommittedAmount() - this.m_principalRecievedToDate;
    }

    /**
	 * Returns the principal received this month
	 * @return
	 */
    public double getMTLPrincipalReceived() {
        return this.m_principalRecieved;
    }

    /**
	 * Returns the principal received this month
	 * @return
	 */
    public double getMTLLCFeesPaid() {
        return this.m_lcFeesPaid;
    }

    /**
	 * Returns the interest received this month
	 * @return
	 */
    public double getMTLInterestReceived() {
        return this.m_interestRecieved;
    }

    /**
	 * Returns the interest received this month
	 * @return
	 */
    public double getMTLLateFeesReceived() {
        return this.m_lateFeesRecieved;
    }

    /**
	 * Returns the total principal received to the date of month for which the record is queried
	 */
    public double getPrincipalRecievedYearToDate() {
        return m_principalRecievedYearToDate;
    }

    /**
	 * Returns the total principal received to the date of month for which the record is queried
	 */
    public double getLCFeesPaidYearToDate() {
        return m_lcFeesPaidYearToDate;
    }

    /**
	 * Returns the total interest received to the date of month for which the record is queried 
	 */
    public double getInterestRecievedYearToDate() {
        return m_interestRecievedYearToDate;
    }

    /**
	 * Returns the total late fees received to the date of month for which the record is queried 
	 */
    public double getLateFeesRecievedYearToDate() {
        return m_lateFeesRecievedYearToDate;
    }

    /**
	 * Returns the total principal received to the date of month for which the record is queried
	 */
    public double getPrincipalRecievedToDate() {
        return m_principalRecievedToDate;
    }

    public Date getLoanStatusChngD() {
        return this.m_chngD;
    }

    public Date getLpdStatusChngD() {
        return this.m_lpdStatusD;
    }

    public boolean isNoFee() {
        return m_isNoFee;
    }

    public void setNoFee(boolean isNoFee) {
        m_isNoFee = isNoFee;
    }
}
