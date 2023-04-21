package org.commerce.mismo;

import java.math.BigDecimal;

/**
 * 
 * @version $Id: TransmittalData.java,v 1.1.1.1 2007/04/16 05:07:03 clafonta Exp $
 */
public interface TransmittalData {

    /**
     * Returns the lenders identification code for the branch number associated
     * with the loan.
     * 
     * @return the lenders identification code for the branch number associated
     *         with the loan
     */
    public String getLendersBranchIdentifier();

    /**
     * Sets the lenders identification code for the branch number associated
     * with the loan.
     * 
     * @param identifier the lenders identification code for the branch number
     *        associated with the loan; may be <code>null</code>
     */
    public void setLendersBranchIdentifier(String identifier);

    /**
     * Statement of property�s value from a valid property valuation source.
     * 
     * @return the property�s value from a valid property valuation source;
     *         may be <code>null</code>
     */
    public BigDecimal getPropertyAppraisedValueAmount();

    /**
     * Statement of property�s value from a valid property valuation source.
     * 
     * @param amount the property�s value from a valid property valuation source;
     *        may be <code>null</code>
     */
    public void setPropertyAppraisedValueAmount(BigDecimal amount);

    /**
     * Returns the description of the point in the loan origination process at
     * which the loan is being sent.
     * 
     * @return a description of the point in the loan origination process at
     *         which the loan is being sent; may be <code>null</code>
     */
    public CaseStateType getCaseStateType();

    /**
     * Sets the description of the point in the loan origination process at
     * which the loan is being sent.
     * 
     * @param type the description of the point in the loan origination process at
     *        which the loan is being sent; may be <code>null</code>
     */
    public void setCaseStateType(CaseStateType type);

    /**
     * Returns the control number (i.e. key) for the loan in the sending loan
     * origination system.
     *
     * @return the control number (i.e. key) for the loan in the sending loan
     *         origination system; may be <code>null</code>
     */
    public String getLoanOriginationSystemLoanIdentifier();

    /**
     * Sets the control number (i.e. key) for the loan in the sending loan
     * origination system.
     *
     * @param id the control number (i.e. key) for the loan in the sending loan
     *        origination system; may be <code>null</code>
     */
    public void setLoanOriginationSystemLoanIdentifier(String id);

    /**
     * answers whether this is an Arms Length Transaction.  (Mismo 2.3)
     * <p>
     * An Arms length transaction is between a willing buyer
     * and a willing seller with no undue influence on either
     * party and there is no relationship between the parties
     * except that of the specific transaction.
     * 
     * @return <code>true</code> if this is an Arms Length Transaction.
     */
    public Boolean getArmsLengthIndicator();

    /**
     * sets whether this is an Arms Length Transaction.  (Mismo 2.3)
     * <p>
     * An Arms length transaction is between a willing buyer
     * and a willing seller with no undue influence on either
     * party and there is no relationship between the parties
     * except that of the specific transaction.
     * 
     * @param armsLengthIndicator <code>true</code> if
     *          this is an Arms Length Transaction.
     */
    public void setArmsLengthIndicator(Boolean armsLengthIndicator);

    /**
     * answers whether the borrower gave the lender consent
     * to pull credit report.  (Mismo 2.3)
     * 
     * @return <code>true</code> if consent was given.
     */
    public Boolean getCreditReportAuthorizationIndicator();

    /**
     * sets whether the borrower gave the lender consent
     * to pull credit report.  (Mismo 2.3)
     * 
     * @param creditReportAuthorizationIndicator <code>true</code>
     *          if consent was given.
     */
    public void setCreditReportAuthorizationIndicator(Boolean creditReportAuthorizationIndicator);
}
