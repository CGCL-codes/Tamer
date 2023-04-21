package mike;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import lc.accessDB.tl.TLLoan;
import lc.accessDB.tl.TLLpDetail;
import lc.accessDB.tl.TLTransaction;
import lc.commonservice.accessInterface.LCActor;
import lc.commonservice.impl.LCLenderAccountSummary.AccountsDataPoints;
import lc.commonservice.impl.LCLenderAccountSummary.PaymentsDataPoints;
import lc.commonservice.repository.LCActorRepository;
import lc.commonservice.repository.LCRepository;
import lc.ui.util.TLCDate;
import lc.ui.util.TLCUtil;

/**
 * Class to generate the data for the monthly statements
 * @author paresh
 *
 */
public class LCMonthlyStatements {

    Date m_startDate;

    Date m_endDate;

    Date m_ytdStartD;

    Long m_actorId;

    private List<LCMTLLoanItemDetail> m_results = null;

    private List<LCAccountActivityDetail> m_payments = null;

    private List<LCAccountActivityDetail> m_bankActivity = null;

    private List<LCAccountActivityDetail> m_misc = null;

    private List<LCAccountActivityDetail> m_investmentActivity = null;

    private EnumMap<AccountsDataPoints, Double> m_accountsDataPoints = null;

    private EnumMap<PaymentsDataPoints, Double> m_paymentsData = null;

    private LCMTLLoanItemSummary m_loanSummary = null;

    public LCMonthlyStatements(Date sdate, Date edate, Date ytdStartD, Long actorId) {
        this.m_startDate = sdate;
        this.m_endDate = edate;
        this.m_actorId = actorId;
        this.m_ytdStartD = ytdStartD;
    }

    public Long getActorId() {
        return this.m_actorId;
    }

    public Date getStartDate() {
        return this.m_startDate;
    }

    public Date getYTDStartDate() {
        return this.m_ytdStartD;
    }

    public Date getEndDate() {
        return this.m_endDate;
    }

    /**
	 * Method which generates the statement and stores the data in this class.This method
	 * should be called before calling any getters
	 */
    public void generateStatement() {
    }

    private void generateTransactions() {
        this.m_bankActivity = new ArrayList<LCAccountActivityDetail>();
        this.m_payments = new ArrayList<LCAccountActivityDetail>();
        this.m_misc = new ArrayList<LCAccountActivityDetail>();
        m_accountsDataPoints = new EnumMap<AccountsDataPoints, Double>(AccountsDataPoints.class);
        m_paymentsData = new EnumMap<PaymentsDataPoints, Double>(PaymentsDataPoints.class);
        List<LCAccountActivityDetail> details = getAccountActivity(this.m_actorId, this.m_startDate, this.m_endDate);
        if (details != null) {
            Double amount = null;
            Double principal = null;
            Double interest = null;
            Double lateFee = null;
            TLTransaction.Type transType = null;
            double tempAmount = 0;
            for (LCAccountActivityDetail detail : details) {
                amount = detail.getAmount();
                principal = detail.getPrincipal();
                interest = detail.getInterest();
                lateFee = detail.getLateFees();
                transType = detail.getTransType();
                switch(transType) {
                    case T3_VERIFY_PIN_AMT:
                    case T0_ACH_LENDER_LOAD_FUNDS:
                    case T20_WIRE_CHECK_RECEIVED:
                    case T21_PAYPAL_FUNDS_RECEIVED:
                        tempAmount = TLCUtil.validateNumberNotNegative(m_accountsDataPoints.get(AccountsDataPoints.ADDED_FUNDS));
                        tempAmount += amount.doubleValue();
                        m_accountsDataPoints.put(AccountsDataPoints.ADDED_FUNDS, new Double(tempAmount));
                        this.m_bankActivity.add(detail);
                        break;
                    case T35_WIRE_CHECK_SENT:
                    case T1_ACH_LENDER_WITHDRAW_FUNDS:
                        tempAmount = TLCUtil.validateNumberNotNegative(m_accountsDataPoints.get(AccountsDataPoints.WITHDRAWN_FUNDS));
                        tempAmount += amount.doubleValue();
                        m_accountsDataPoints.put(AccountsDataPoints.WITHDRAWN_FUNDS, new Double(tempAmount));
                        this.m_bankActivity.add(detail);
                        break;
                    case T11_BORROWER_MTL_PYMT_TO_LENDER:
                        tempAmount = TLCUtil.validateNumberNotNegative(m_paymentsData.get(PaymentsDataPoints.TOTAL_PRINCIPAL_RECIEVED));
                        tempAmount += principal.doubleValue();
                        m_paymentsData.put(PaymentsDataPoints.TOTAL_PRINCIPAL_RECIEVED, new Double(tempAmount));
                        tempAmount = TLCUtil.validateNumberNotNegative(m_paymentsData.get(PaymentsDataPoints.TOTAL_INTEREST_RECIEVED));
                        tempAmount += interest.doubleValue();
                        m_paymentsData.put(PaymentsDataPoints.TOTAL_INTEREST_RECIEVED, new Double(tempAmount));
                        tempAmount = TLCUtil.validateNumberNotNegative(m_paymentsData.get(PaymentsDataPoints.TOTAL_LATE_FEE_RECIEVED));
                        tempAmount += lateFee.doubleValue();
                        m_paymentsData.put(PaymentsDataPoints.TOTAL_LATE_FEE_RECIEVED, new Double(tempAmount));
                        this.m_payments.add(detail);
                        break;
                    case T8_LENDER_PAYS_SERVICE_FEE:
                        this.m_payments.add(detail);
                        tempAmount = TLCUtil.validateNumberNotNegative(m_accountsDataPoints.get(AccountsDataPoints.PROCESSING_FEES));
                        tempAmount += amount.doubleValue();
                        m_accountsDataPoints.put(AccountsDataPoints.PROCESSING_FEES, new Double(tempAmount));
                        break;
                    case T14_BONUS_REFERAL:
                        this.m_misc.add(detail);
                        tempAmount = TLCUtil.validateNumberNotNegative(m_accountsDataPoints.get(AccountsDataPoints.REFERRAL_BONUS));
                        tempAmount += amount.doubleValue();
                        m_accountsDataPoints.put(AccountsDataPoints.REFERRAL_BONUS, new Double(tempAmount));
                        break;
                    case T25_DONATED_FUNDS_TO_CHARITY:
                        this.m_misc.add(detail);
                        tempAmount = TLCUtil.validateNumberNotNegative(m_accountsDataPoints.get(AccountsDataPoints.CHARITY_DONATION));
                        tempAmount += amount.doubleValue();
                        m_accountsDataPoints.put(AccountsDataPoints.CHARITY_DONATION, new Double(tempAmount));
                        break;
                    case T18_ADJUSTMENT_PYMT_TO_LENDER:
                    case T16_MANUAL_CREDIT_ADJUSTMENT:
                        this.m_misc.add(detail);
                        tempAmount = TLCUtil.validateNumberNotNegative(m_accountsDataPoints.get(AccountsDataPoints.ALL_ADJUSTMENTS));
                        tempAmount += amount.doubleValue();
                        m_accountsDataPoints.put(AccountsDataPoints.ALL_ADJUSTMENTS, new Double(tempAmount));
                        break;
                    case T32_ACH_PIN_AMNT_AUTOMATIC_WITHDRAW:
                    case T24_MANUAL_DEBIT_ADJUSTMENT:
                        this.m_misc.add(detail);
                        tempAmount = TLCUtil.validateNumberNotNegative(m_accountsDataPoints.get(AccountsDataPoints.WITHDRAWN_FUNDS));
                        tempAmount += amount.doubleValue();
                        m_accountsDataPoints.put(AccountsDataPoints.WITHDRAWN_FUNDS, new Double(tempAmount));
                        break;
                    case T2_LENDER_ISSUE_FUNDS_TO_BORROWER:
                        tempAmount = TLCUtil.validateNumberNotNegative(m_accountsDataPoints.get(AccountsDataPoints.FUNDS_LENT));
                        tempAmount += amount.doubleValue();
                        m_accountsDataPoints.put(AccountsDataPoints.FUNDS_LENT, new Double(tempAmount));
                        break;
                    case T28_SECONDARY_MARKET_TRADE:
                        this.m_misc.add(detail);
                        if (detail.getSign() == 1) {
                            tempAmount = TLCUtil.validateNumberNotNegative(m_accountsDataPoints.get(AccountsDataPoints.LOAD_FROM_FOLIO));
                            tempAmount += amount.doubleValue();
                            m_accountsDataPoints.put(AccountsDataPoints.LOAD_FROM_FOLIO, new Double(tempAmount));
                        } else {
                            tempAmount = TLCUtil.validateNumberNotNegative(m_accountsDataPoints.get(AccountsDataPoints.WITHDRAWAL_TO_FOLIO));
                            tempAmount += amount.doubleValue();
                            m_accountsDataPoints.put(AccountsDataPoints.WITHDRAWAL_TO_FOLIO, new Double(tempAmount));
                        }
                        break;
                    case T29_SECONDARY_MARKET_TRADE_FEES:
                        this.m_misc.add(detail);
                        tempAmount = TLCUtil.validateNumberNotNegative(m_accountsDataPoints.get(AccountsDataPoints.SCMARKET_TRADE_FEES));
                        tempAmount += amount.doubleValue();
                        m_accountsDataPoints.put(AccountsDataPoints.SCMARKET_TRADE_FEES, new Double(tempAmount));
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void generateInvestmentDetails() {
        this.m_investmentActivity = new ArrayList<LCAccountActivityDetail>();
        Vector<Map> results = LCMTLStatementsQueryEngine.getMonthlyInvestmentDetail(LCRepository.getSession(), this.m_actorId, this.m_startDate, this.m_endDate);
        if (results != null) {
            for (Map result : results) {
                LCAccountActivityDetail detail = new LCAccountActivityDetail(result);
                this.m_investmentActivity.add(detail);
            }
        }
    }

    private void generateLoanDetails() {
        Vector<Map> results = LCMTLStatementsQueryEngine.getLoanItemDetails(LCRepository.getSession(), this.m_actorId, this.m_startDate, this.m_endDate, this.m_ytdStartD);
        m_results = new ArrayList<LCMTLLoanItemDetail>();
        if (results != null && results.size() > 0) {
            for (Map m : results) this.m_results.add(new LCMTLLoanItemDetail(m));
        }
    }

    public static List<LCAccountActivityDetail> getAccountActivity(Long actorId, Date fromD, Date toDate) {
        List<LCAccountActivityDetail> details = new ArrayList<LCAccountActivityDetail>();
        TLTransaction.Status[] status = { TLTransaction.Status.EXECUTED };
        TLTransaction.ReconStatus[] reconStatus = { TLTransaction.ReconStatus.COMPLETED };
        if (fromD == null) {
            LCActor actor = LCActorRepository.getActorById(actorId);
            if (actor != null) {
                fromD = actor.getCreationDate();
            }
        }
        if (toDate == null) {
            toDate = new Date();
        }
        Vector<Map> results = TLTransaction.getMonthlyTransactionDetail(LCRepository.getSession(), actorId, fromD, toDate, status, reconStatus);
        for (Map result : results) {
            LCAccountActivityDetail detail = new LCAccountActivityDetail(result);
            details.add(detail);
        }
        return details;
    }

    /**
	 * Returns the details for the performing and non performing loans that the
	 * lender has invested in. 
	 * @return 
	 */
    public LCMTLLoanItemSummary getLoanDetailShort() {
        if (this.m_loanSummary == null) {
            Vector<Map> results = LCMTLStatementsQueryEngine.getLoanItemSummary(LCRepository.getSession(), this.m_actorId, this.m_startDate, this.m_endDate, this.m_ytdStartD);
            if (results != null && results.size() > 0) this.m_loanSummary = new LCMTLLoanItemSummary(results.get(0));
        }
        return this.m_loanSummary;
    }

    /**
	 * Returns the details for the performing and non performing loans that the
	 * lender has invested in. 
	 * @return 
	 */
    public List<LCMTLLoanItemDetail> getLoanDetails() {
        if (this.m_results == null) {
            generateLoanDetails();
        }
        return this.m_results;
    }

    /**
	 * Returns the payments details for this month.  
	 * @return 
	 */
    public List<LCAccountActivityDetail> getPayments() {
        if (this.m_payments == null) generateTransactions();
        return this.m_payments;
    }

    /**
	 * Returns the investment activity   
	 * @return 
	 */
    public List<LCAccountActivityDetail> getInvestmentActivity() {
        if (this.m_investmentActivity == null) generateInvestmentDetails();
        return this.m_investmentActivity;
    }

    /**
	 * Returns the bank account activity.  
	 * @return 
	 */
    public List<LCAccountActivityDetail> getBankActivity() {
        if (this.m_bankActivity == null) generateTransactions();
        return this.m_bankActivity;
    }

    /**
	 * Returns the miscellaneous transactions including referral bonus, adjustments .  
	 * @return 
	 */
    public List<LCAccountActivityDetail> getMiscActivity() {
        if (this.m_misc == null) generateTransactions();
        return this.m_misc;
    }

    /**
	 * Returns the accounts data in the form of an enum map
	 */
    public EnumMap<AccountsDataPoints, Double> getAccountsData() {
        if (this.m_accountsDataPoints == null) generateTransactions();
        return this.m_accountsDataPoints;
    }

    /**
	 * Returns the payments data in the form of an enum map (principal recieved,interest recieved)
	 */
    public EnumMap<PaymentsDataPoints, Double> getPaymentsData() {
        if (this.m_paymentsData == null) generateTransactions();
        return this.m_paymentsData;
    }

    /**
	 * Returns the cash balance for a given month
	 */
    public double getCashBalance(Date d) {
        return 100;
    }

    public double getLoanFractionsAmount(Date date, TLLoan.SummaryStatus... stat) {
        Vector<Map> results = LCMTLStatementsQueryEngine.getLoansCommitedAmount(LCRepository.getSession(), this.m_actorId, date, stat);
        for (Map result : results) {
            Double amount = TLCUtil.validateNumberNotNegative((BigDecimal) result.get("INVESTED_AMOUNT"));
            return amount;
        }
        return 0;
    }

    public double getRemainingPrincipal(Date endDate) {
        BigDecimal remPrinc = LCMTLStatementsQueryEngine.getOutStandingPrincipalByDate(LCRepository.getSession(), this.m_actorId, endDate);
        if (remPrinc != null) return remPrinc.doubleValue();
        return 0;
    }

    public static void main(String[] args) {
        LCRepository.getSession();
        long startT = System.currentTimeMillis();
        Date startdate = TLCDate.normalizeMonth(new Date());
        GregorianCalendar scal = new GregorianCalendar();
        scal.set(2008, Calendar.OCTOBER, 01, 00, 00, 00);
        Date sdate = scal.getTime();
        GregorianCalendar ecal = new GregorianCalendar();
        ecal.set(2008, Calendar.OCTOBER, 31, 23, 59, 59);
        Date edate = ecal.getTime();
        LCMonthlyStatements statements = new LCMonthlyStatements(sdate, edate, sdate, 281740l);
        statements.generateStatement();
        List<LCMTLLoanItemDetail> results = statements.getLoanDetails();
        double princ = 0;
        double interest = 0;
        for (LCMTLLoanItemDetail result : results) {
            if (result.getLPDStatus() == TLLpDetail.Status.SOLD) System.out.println("Commited Amount = " + result.getCommittedAmount());
            System.out.println("Principal Recieved toDate = " + result.getPrincipalRecievedYearToDate());
            System.out.println("Interest Recieved todate= " + result.getInterestRecievedYearToDate());
        }
        double sPrin = statements.getRemainingPrincipal(sdate);
        double ePrin = statements.getRemainingPrincipal(edate);
        double lent = statements.getAccountsData().get(AccountsDataPoints.FUNDS_LENT);
        System.out.println("Principal reciev:" + (sPrin + lent - ePrin));
        System.out.println("Principal:" + princ);
        System.out.println("Interest:" + interest);
        System.err.println("Time Elapsed = " + (System.currentTimeMillis() - startT));
        List<LCAccountActivityDetail> misc = statements.getMiscActivity();
        for (LCAccountActivityDetail detail : misc) {
            System.out.println(detail.getActivityDescription());
            ;
        }
    }
}
