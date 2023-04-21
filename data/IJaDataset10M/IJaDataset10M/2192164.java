package mike;

import java.math.BigDecimal;
import java.util.Map;
import lc.ui.util.TLCUtil;

public class LCMTLLoanItemSummary {

    private double m_principalRecievedYearToDate;

    private double m_interestRecievedYearToDate;

    private double m_lateFeesRecievedYearToDate;

    private double m_lcFeesPaidYearToDate;

    private double m_principalRecieved;

    private double m_interestRecieved;

    private double m_lateFeesRecieved;

    private double m_lcFeesPaid;

    private double m_chgOffPrinRemYearToDate;

    private double m_chgOffPrinRem;

    private double pendingAdj;

    public LCMTLLoanItemSummary(Map<String, Object> result) {
        this.m_principalRecievedYearToDate = TLCUtil.validateNumber((BigDecimal) result.get("YTODATEPRIN"));
        this.m_interestRecievedYearToDate = TLCUtil.validateNumber((BigDecimal) result.get("YTODATEINT"));
        this.m_lateFeesRecievedYearToDate = TLCUtil.validateNumber((BigDecimal) result.get("YTODATEFEES"));
        this.m_lateFeesRecievedYearToDate = TLCUtil.validateNumber((BigDecimal) result.get("YTODATELCFEES"));
        this.m_principalRecieved = TLCUtil.validateNumber((BigDecimal) result.get("MTL_PRINCIPAL_RECIEVED"));
        this.m_interestRecieved = TLCUtil.validateNumber((BigDecimal) result.get("MTL_INTEREST_RECIEVED"));
        this.m_lateFeesRecieved = TLCUtil.validateNumber((BigDecimal) result.get("MTL_LATE_FEES_RECIEVED"));
        this.m_lcFeesPaid = TLCUtil.validateNumber((BigDecimal) result.get("MTL_LCFEES_RECIEVED"));
        this.m_chgOffPrinRemYearToDate = TLCUtil.validateNumber((BigDecimal) result.get("YTDCHG_OFF_PRIN"));
        this.m_chgOffPrinRem = TLCUtil.validateNumber((BigDecimal) result.get("CHG_OFF_PRIN"));
        this.pendingAdj = TLCUtil.validateNumber((BigDecimal) result.get("PENDING_ADJ"));
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
	 * Returns the principal received this month
	 * @return
	 */
    public double getYTDChargeOffPrin() {
        return this.m_chgOffPrinRemYearToDate;
    }

    /**
	 * Returns the principal received this month
	 * @return
	 */
    public double getMTLChargeOffPrin() {
        return this.m_chgOffPrinRem;
    }

    /**
	 * Returns the principal received this month
	 * @return
	 */
    public double getPendingAdj() {
        return this.pendingAdj;
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
    public double getLCFeesPaidYearToDate() {
        return m_lcFeesPaidYearToDate;
    }

    /**
	 * Returns the total principal received to the date of month for which the record is queried
	 */
    public double getPrincipalRecievedYearToDate() {
        return m_principalRecievedYearToDate;
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
}
