package lc.ui.data;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.EnumMap;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lc.accessDB.tl.TLLoanApp.LoanType;
import lc.commonservice.accessInterface.LCActor;
import lc.commonservice.accessInterface.LCOrder;
import lc.commonservice.accessInterface.LCOrderItem;
import lc.commonservice.accessInterface.LCPortfolio;
import lc.commonservice.accessInterface.LCShoppingCartItem;
import lc.commonservice.impl.LCImmutableActor;
import lc.commonservice.impl.LCLoanItemSearchQuery;
import lc.commonservice.impl.LCLoanItemSearchResult;
import lc.commonservice.impl.LCOrderImpl;
import lc.commonservice.impl.LCPortfolioSummary;
import lc.commonservice.impl.base.BaseLCLoanItemRecord;
import lc.commonservice.repository.LCLoanRepository;
import lc.init.TLCConstants;
import lc.lm.LMV2MarkoSliderPoint;
import lc.search.FicoRange;
import lc.search.LCLoanSearchResult;
import lc.secmarketservice.LCSecuritizedLoan.CreditTrend;
import lc.secmarketservice.impl.LCSecuritizedLoanDetail;
import lc.ui.request.RequestErrorKey;
import lc.ui.request.RequestErrorObject;
import lc.ui.util.FormatUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tlc.platform.core.loan.LoanMaturity;
import tlc.platform.core.util.LoggerManager;
import tlc.platform.impl.loan.LoanGrade;
import tlc.ui.action.ResultCode;
import tlc.ui.test.Borrower;

/**
 * Provide methods for converting objects into JSON equivalents
 *
 * TODO: convert to use org.json code for all JSON coding
 * @author Tom
 *
 */
public class JSONBuilder {

    private static final Logger logger = LoggerManager.getLogger(JSONBuilder.class);

    public static String UNEXPECTED_ERROR;

    public static String JSON_ERROR;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(JSONKey.STARTOBJECT);
        String error = JSONBuilder.generateNameValuePair(JSONKey.ERROR_MESSAGE, "Invalid data request received", true);
        error = JSONKey.STARTARRAY + error + JSONKey.ENDARRAY;
        sb.append(JSONBuilder.generateNameValuePair(JSONKey.ERRORS, error, false));
        sb.append(JSONKey.ENDOBJECT);
        UNEXPECTED_ERROR = sb.toString();
        UNEXPECTED_ERROR = "{\"result\":\"error\",\"error\":[\"" + RequestErrorKey.UNEXPECTED_ERROR + "\"]}";
        sb.delete(0, sb.length());
        sb.append("{\"result\":\"error\",\"error\":[\"" + RequestErrorKey.JSON_BUILD_ERROR + "\"]}");
        JSON_ERROR = sb.toString();
    }

    static String format(double d) {
        return TLCConstants.RateFormat.format(d);
    }

    /**
	 * Return a JSON String for a name value pair.
	 *   myKey:"keyValue"
	 * @param name
	 * @param value
	 * @param quoted if the value should be in quotes or not
	 * @return
     * @deprecated
	 */
    public static String generateNameValuePair(JSONKey key, String value, boolean quoted) {
        StringBuffer sb = new StringBuffer();
        sb.append(key);
        sb.append(JSONKey.COLON);
        if (quoted) {
            sb.append(JSONKey.QUOTE);
            sb.append(value);
            sb.append(JSONKey.QUOTE);
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    /**
	 * Return a JSON String for a name value pair. All names are quoted. e.g.
	 *   "myKey":"1234"
	 * @param name
	 * @param value
	 * @param quoted if the value should be in quotes or not
	 * @return
     * @deprecated
	 */
    public static String generateNameValuePair(JSONKey key, int value, boolean quoted) {
        return generateNameValuePair(key, Integer.toString(value), quoted);
    }

    /**
	 * Return a JSON String for a name value pair.
	 *   myKey:"1234.12412"
	 * @param name
	 * @param value
	 * @param quoted if the value should be in quotes or not
	 * @return
	 */
    public static String generateNameValuePair(JSONKey key, double value, boolean quoted) {
        return generateNameValuePair(key, Double.toString(value), quoted);
    }

    /**
	 * Return a JSON String for a name/array pair.
	 *   myKey:["val1", "val2", "val3"]
	 * @param key
	 * @param c collection to populate the array
	 * @param quoted if the value should be in quotes or not
	 * @return
	 * @deprecated
	 */
    public static String generateNameArrayPair(JSONKey key, Collection<Object> c, boolean quoted) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append(JSONKey.COLON);
        sb.append(JSONKey.STARTARRAY);
        Iterator<Object> i = c.iterator();
        int counter = 1;
        while (i.hasNext()) {
            Object o = i.next();
            if (counter != 1) sb.append(JSONKey.COMMA);
            if (quoted) sb.append(JSONKey.QUOTE);
            sb.append(o.toString());
            if (quoted) sb.append(JSONKey.QUOTE);
            counter++;
        }
        sb.append(JSONKey.ENDARRAY);
        return sb.toString();
    }

    public static long daysBetween(Date start, Date end) {
        GregorianCalendar c1 = (GregorianCalendar) Calendar.getInstance();
        c1.setTime(start);
        GregorianCalendar c2 = (GregorianCalendar) Calendar.getInstance();
        c2.setTime(end);
        Calendar date = (Calendar) c1.clone();
        long daysBetween = 0;
        while (date.before(c2)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    /**
	 * Return JSON representation of the Securitized Loan, e.g.

	 * @return
	 */
    public static JSONObject build(LCSecuritizedLoanDetail loan, LCImmutableActor member) throws JSONException {
        JSONObject jso = new JSONObject();
        try {
            jso.put("checkboxes", loan.isSelected());
            if (member != null) {
                if (member.getGuid() == loan.getOwnerAid().longValue()) {
                    jso.put(JSONKey.SELFNOTE.toString(), 1);
                } else {
                    jso.put(JSONKey.SELFNOTE.toString(), 0);
                }
            } else {
                jso.put(JSONKey.SELFNOTE.toString(), 0);
            }
            jso.put(JSONKey.LOANGUID.toString(), loan.getLoanId());
            jso.put(JSONKey.LOANTITLE.toString(), loan.getLoanTitle());
            jso.put(JSONKey.LOANGRADE.toString(), loan.getGrade());
            jso.put(JSONKey.LOANRATE.toString(), format(100 * loan.getIntRate()));
            jso.put(JSONKey.LOANSTATUS.toString(), loan.getLoanStatus().getLabel());
            jso.put(JSONKey.LOAN_CLASS.toString(), loan.getLoanMaturity());
            double ytm = loan.getYTM();
            if (Double.isNaN(ytm)) {
                jso.put(JSONKey.LOANYTM.toString(), "null");
            } else {
                jso.put(JSONKey.LOANYTM.toString(), format(100 * ytm));
            }
            jso.put(JSONKey.NOTEID.toString(), loan.getNoteId());
            CreditTrend trend = loan.getCreditTrend();
            int loanTrend = 1;
            if (trend == CreditTrend.DOWN) loanTrend = 0; else if (trend == CreditTrend.UP) loanTrend = 2; else if (trend == CreditTrend.FLAT) loanTrend = 1;
            jso.put(JSONKey.LOAN_CREDIT_SCORE_TREND.toString(), loanTrend);
            Long daysSinceLastPayment = loan.getDaysFromLastPayment();
            if (null == daysSinceLastPayment) {
                jso.put(JSONKey.LOAN_DAYS_SINCE_PAYMENT.toString(), "null");
            } else {
                jso.put(JSONKey.LOAN_DAYS_SINCE_PAYMENT.toString(), daysSinceLastPayment);
            }
            jso.put(JSONKey.LOAN_REMAINING_PAYMENTS.toString(), loan.getRemainingPaymentCount());
            jso.put(JSONKey.LOAN_OUTSTANDING_PRINCIPAL.toString(), FormatUtil.formatTwoDecimalAmount(loan.getOutstandingPrincipal()));
            double accrual = 0;
            if (null != loan.getAccruedInterest()) accrual = new Double(FormatUtil.formatTwoDecimalAmount(loan.getAccruedInterest())).doubleValue();
            jso.put(JSONKey.LOAN_ACCRUED_INTEREST.toString(), accrual);
            jso.put(JSONKey.LOAN_ASKING_PRICE.toString(), loan.getSalePrice());
            double markDiscount = loan.getMarkup();
            jso.put(JSONKey.LOAN_MARKUP_DISCOUNT.toString(), FormatUtil.formatTwoDecimalAmount(markDiscount));
            jso.put(JSONKey.ORDER_ID.toString(), loan.getOrderId());
            return jso;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception while processing sec loan for search");
            e.printStackTrace();
            return jso;
        }
    }

    /**
     * JSONArray of loans
     * @param loans
     * @return
     */
    public static JSONArray buildLoanSearchJSON(List<LCLoanSearchResult> loans, LCActor actor) throws JSONException {
        JSONArray list = new JSONArray();
        for (Iterator<LCLoanSearchResult> iter = loans.iterator(); iter.hasNext(); ) {
            LCLoanSearchResult loan = iter.next();
            JSONObject loanJS = build(loan, actor);
            list.put(loanJS);
        }
        return list;
    }

    /**
	 * Return JSON representation of the Loan, e.g.
	 * {"name" : "John Doe",
	 *  "loanAmount" : 1000,
	 *  "rate" : 12.5,
	 *  "rating" : "B",
	 *  "loanLength" : 12,
	 *  "loanAmountRemaining" : 300,
	 *  "connections" : "MIT Alumni"
	 * }
	 * @return
	 */
    public static JSONObject build2(LCLoanSearchResult loan, LCActor member) throws JSONException {
        JSONObject jso = new JSONObject();
        jso.put(JSONKey.NO_SERVICE_FEE.toString(), loan.isNoFee() ? 1 : 0);
        jso.put(JSONKey.FICO.toString(), FicoRange.getEnum(loan.getFico()).getLabel());
        jso.put(JSONKey.LOANTITLE.toString(), loan.getName());
        jso.put(JSONKey.LOANAMOUNT.toString(), loan.getAppAmount());
        jso.put(JSONKey.LOANAMOUNTREQUESTED.toString(), loan.getAppAmount());
        jso.put(JSONKey.LOANGRADE.toString(), loan.getLoanClass().classGetClassName());
        jso.put(JSONKey.LOANUNFUNDEDAMOUNT.toString(), loan.getUnfundedAmnt());
        jso.put(JSONKey.PURPOSE.toString(), loan.getPurpose());
        jso.put(JSONKey.LOANRATE.toString(), format(100 * loan.getIntRate().doubleValue()));
        jso.put(JSONKey.LOANRATEDIFF.toString(), format(100 * loan.get36_60RateDiff().doubleValue()));
        jso.put(JSONKey.LOANLENGTH.toString(), loan.getMaturity());
        jso.put(JSONKey.HASCOSIGNER.toString(), loan.hasCoSigner());
        jso.put(JSONKey.LOANAMTREMAINING.toString(), loan.getAmountMissingToClose());
        jso.put(JSONKey.LOANTIMEREMAINING.toString(), loan.getTimeLeftBeforeExpiration());
        jso.put(JSONKey.LOANGUID.toString(), loan.getId().toString());
        jso.put(JSONKey.LOANSTATUS.toString(), loan.getLoanStatus());
        jso.put(JSONKey.ALREADYINVESTEDIN.toString(), loan.isAlreadyOwned());
        jso.put(JSONKey.AMOUNTTOINVEST.toString(), loan.getAmountToInvest());
        jso.put(JSONKey.ALREADYSELECTED.toString(), loan.isSelected());
        jso.put(JSONKey.ISINCURRENTORDER.toString(), loan.isAlreadyCarted());
        jso.put(JSONKey.PRIME_FRACTIONS.toString(), loan.getNumberOfPrimeFractions());
        jso.put(JSONKey.PRIME_TOTAL_INVESTMENT.toString(), loan.getPrimeToInvest());
        jso.put(JSONKey.PRIME_UNFUNDED.toString(), loan.getPrimeUnfundedAmount());
        jso.put(JSONKey.PRIME_MARKED_INVESTMENT.toString(), loan.getMarkedToFundAmount());
        jso.put(JSONKey.LOAN_TYPE.toString(), LoanType.values()[loan.getType()].getLabel());
        try {
            jso.put(JSONKey.SEARCHRANK.toString(), loan.getRank());
        } catch (Exception e) {
            logger.warning("Error getting search rank or affinities " + e.toString());
        }
        return jso;
    }

    /**
	 * Return JSON representation of the Loan, e.g.
	 * {"name" : "John Doe",
	 *  "loanAmount" : 1000,
	 *  "rate" : 12.5,
	 *  "rating" : "B",
	 *  "loanLength" : 12,
	 *  "loanAmountRemaining" : 300,
	 *  "connections" : "MIT Alumni"
	 * }
	 * @return
	 */
    public static JSONObject build(LCLoanSearchResult loan, LCActor member) throws JSONException {
        JSONObject jso = new JSONObject();
        jso.put(JSONKey.AMOUNTTOINVEST.toString(), loan.getAmountToInvestHtml());
        jso.put(JSONKey.RATE_GRADE.toString(), loan.getRateAndAmountRequestedHtml());
        jso.put(JSONKey.TERM.toString(), loan.getTypeAndTermRequestedHtml());
        jso.put(JSONKey.FICO.toString(), loan.getFicoHtml());
        jso.put(JSONKey.LOANAMOUNT.toString(), loan.getLoanAmountRequestedHtml());
        jso.put(JSONKey.TITLE_PURPOSE.toString(), loan.getTitleAndPurposeHtml());
        jso.put(JSONKey.FUNDED_PERCENT.toString(), loan.getFundedPercentHtml());
        jso.put(JSONKey.TIME_AMOUNT_LEFT.toString(), loan.getUnfundedAndTimeLeftHtml());
        jso.put(JSONKey.ALREADYSELECTED.toString(), loan.isSelected());
        jso.put(JSONKey.ISINCURRENTORDER.toString(), loan.isAlreadyCarted());
        jso.put(JSONKey.LOANGUID.toString(), loan.getId().toString());
        jso.put(JSONKey.LOANSTATUS.toString(), loan.getLoanStatus());
        return jso;
    }

    /**
	 * Return JSON representation of the Borrower, e.g.
	 * {"name" : "John Doe",
	 *  "loanAmount" : 1000,
	 *  "rate" : 12.5,
	 *  "rating" : "B",
	 *  "loanLength" : 12,
	 *  "loanAmountRemaining" : 300,
	 *  "connections" : "MIT Alumni"
	 * }
	 * @return
	 */
    public static String build(Borrower borrower) {
        StringBuffer sb = new StringBuffer();
        sb.append(JSONKey.STARTOBJECT);
        sb.append(generateNameValuePair(JSONKey.LOANTITLE, borrower.getName(), true));
        sb.append(JSONKey.COMMA);
        sb.append(generateNameValuePair(JSONKey.LOANAMOUNT, borrower.getLoanAmount(), false));
        sb.append(JSONKey.COMMA);
        sb.append(generateNameValuePair(JSONKey.LOANRATE, borrower.getRate(), false));
        sb.append(JSONKey.COMMA);
        sb.append(generateNameValuePair(JSONKey.LOANRATING, borrower.getRating(), true));
        sb.append(JSONKey.COMMA);
        sb.append(generateNameValuePair(JSONKey.LOANLENGTH, borrower.getLoanLength(), false));
        sb.append(JSONKey.COMMA);
        sb.append(generateNameValuePair(JSONKey.LOANAMTREMAINING, borrower.getLoanAmountRemaining(), false));
        sb.append(JSONKey.COMMA);
        sb.append(generateNameValuePair(JSONKey.CONNECTIONS, borrower.getConnections(), true));
        sb.append(JSONKey.COMMA);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy hh:mm:ss");
        sb.append(generateNameValuePair(JSONKey.LOANTIMEREMAINING, sdf.format(borrower.getExpirationDate()), true));
        sb.append(JSONKey.COMMA);
        sb.append(generateNameValuePair(JSONKey.LOANGUID, borrower.getID(), true));
        sb.append(JSONKey.ENDOBJECT);
        return sb.toString();
    }

    /**
	 * Builds an object which contains borrower details.
	 * @param borrowers
	 * @return
	 */
    public static String build(List<Borrower> borrowers) {
        StringBuilder result = new StringBuilder();
        result.append(JSONKey.STARTOBJECT);
        StringBuilder details = new StringBuilder();
        details.append(JSONKey.STARTARRAY);
        boolean first = true;
        for (Borrower b : borrowers) {
            if (!first) {
                details.append(JSONKey.COMMA);
            }
            details.append(JSONBuilder.build(b));
            first = false;
        }
        details.append(JSONKey.ENDARRAY);
        result.append(JSONBuilder.generateNameValuePair(JSONKey.DETAILS, details.toString(), false));
        result.append(JSONKey.ENDOBJECT);
        return result.toString();
    }

    /**
	 * Builds an object which contains an id list for the borrowers as well as borrower details.
	 * @param lrs
	 * @return
	 */
    public static String buildFromLoans2(List<LCSecuritizedLoanDetail> loans) {
        StringBuilder result = new StringBuilder();
        result.append(JSONKey.STARTOBJECT);
        StringBuilder details = new StringBuilder();
        details.append(JSONKey.STARTARRAY);
        boolean first = true;
        Iterator<LCSecuritizedLoanDetail> iter = loans.iterator();
        while (iter.hasNext()) {
            LCSecuritizedLoanDetail loan = iter.next();
            String loanStr;
            try {
                loanStr = JSONBuilder.build(loan, null).toString();
                if (!first) {
                    details.append(JSONKey.COMMA);
                }
                details.append(loanStr);
                first = false;
            } catch (JSONException e) {
            }
        }
        details.append(JSONKey.ENDARRAY);
        result.append(JSONBuilder.generateNameValuePair(JSONKey.DETAILS, details.toString(), false));
        result.append(JSONKey.ENDOBJECT);
        return result.toString();
    }

    /**
	 * Returns a JSON object representing the standard request error object
	 * @param errs
	 * @return
	 */
    public static String build(RequestErrorObject errs) {
        JSONObject container = new JSONObject();
        try {
            container.put(JSONKey.RESULT.toString(), ResultCode.ERROR);
            JSONArray errsarray = new JSONArray();
            Iterator<RequestErrorKey> iter = errs.getErrorKeys().iterator();
            while (iter.hasNext()) {
                errsarray.put(iter.next().toString());
            }
            container.put(JSONKey.ERROR.toString(), errsarray);
        } catch (JSONException e) {
            logger.warning("Error creating JSON error message return string");
            return JSON_ERROR;
        }
        return container.toString();
    }

    /**
	 * Returns a JSON object representing the given order. The order object will contain both
	 * the order entries as well as the risk information associated with the order.
	 *
	 * If the order is empty, "{}" is returned (TODO: is this correct?)
	 *
	 * If an error occurs during translation, "{}" is returned (TODO: is this correct and desired? update to return an error)
	 *
	 * @param order order to convert
	 * @return JSON object
	 */
    public static String build(LCOrder order) {
        try {
            JSONObject container = new JSONObject();
            List<JSONObject> fractionsJSON = new ArrayList<JSONObject>();
            JSONObject lfJSON = null;
            Collection<LCOrderItem> orderItems = order.getOrderItems();
            long now = new Date().getTime();
            for (LCOrderItem oi : orderItems) {
                Timestamp expirationTime = oi.getAppExpiration();
                lfJSON = new JSONObject();
                lfJSON.put(JSONKey.PURPOSE.toString(), oi.getLoanPurpose());
                lfJSON.put(JSONKey.LOANFRACTIONGUID.toString(), oi.getId());
                lfJSON.put(JSONKey.LOANFRACTIONAMOUNT.toString(), oi.getInvAmt());
                lfJSON.put(JSONKey.LOANTITLE.toString(), oi.getLoanTitle());
                lfJSON.put(JSONKey.LOANAMOUNTREQUESTED.toString(), oi.getLoanAmt());
                lfJSON.put(JSONKey.LOANRATE.toString(), format(100 * oi.getIntrestRate()));
                lfJSON.put(JSONKey.LOANUNFUNDEDAMOUNT.toString(), oi.getUnfundedAmt());
                lfJSON.put(JSONKey.LOANTIMEREMAINING.toString(), expirationTime.getTime() - now);
                lfJSON.put(JSONKey.LOANEXPIRATIONDATE.toString(), expirationTime);
                lfJSON.put(JSONKey.LOANGRADE.toString(), oi.getLoanClass().classGetClassName());
                lfJSON.put(JSONKey.LOANID.toString(), oi.getLoanId());
                lfJSON.put(JSONKey.UNCRUNCH.toString(), oi.getChannel().toString());
                lfJSON.put(JSONKey.LOANSTATUS.toString(), oi.getLoanStatus());
                lfJSON.put(JSONKey.LOANLENGTH.toString(), oi.getMaturity());
                lfJSON.put(JSONKey.ALREADYINVESTEDIN.toString(), oi.isAlreadyOwned());
                lfJSON.put(JSONKey.LOAN_TYPE.toString(), oi.getLoanType().getLabel());
                fractionsJSON.add(lfJSON);
            }
            container.put(JSONKey.LOANFRACTIONS.toString(), fractionsJSON);
            return container.toString();
        } catch (Exception e) {
            logger.warning("Error converting ORDER to JSON object: " + e);
            return "{}";
        }
    }

    /**
	 * Returns a JSON object representing the given composition of a order object. If an error occurs during processing,
	 * an empty object will be returned.
	 * @param order
	 * @return JSON String
	 */
    public static JSONObject buildRisk(LCOrder order) {
        try {
            JSONObject riskJSON = new JSONObject();
            riskJSON.put(JSONKey.RISKLEVEL.toString(), 0.0 + "");
            LMV2MarkoSliderPoint mPoint = order.getV2MarkoPoint();
            EnumMap<LoanGrade, Double> composition = mPoint.getGradeComposition();
            double hadr = mPoint.getHistoricalAnnualDefaultRate();
            double fee = mPoint.getServiceFeePercentage();
            double pReturn = mPoint.getProjectedReturns();
            if (Double.isNaN(hadr)) hadr = 0;
            if (Double.isNaN(fee)) fee = 0;
            if (Double.isNaN(pReturn)) pReturn = 0;
            riskJSON.put(JSONKey.HISTORICALANNUALDEFAULTRATE.toString(), hadr);
            riskJSON.put(JSONKey.SERVICEFEE.toString(), fee);
            riskJSON.put(JSONKey.PROJECTEDRETURNS.toString(), pReturn);
            double aa = composition.get(LoanGrade.AA).doubleValue();
            double a = composition.get(LoanGrade.A).doubleValue();
            double b = composition.get(LoanGrade.B).doubleValue();
            double c = composition.get(LoanGrade.C).doubleValue();
            double d = composition.get(LoanGrade.D).doubleValue();
            double e = composition.get(LoanGrade.E).doubleValue();
            double f = composition.get(LoanGrade.F).doubleValue();
            double g = composition.get(LoanGrade.G).doubleValue();
            riskJSON.put(JSONKey.EXPECTEDRETURN.toString(), format(100 * mPoint.normalizedReturn));
            riskJSON.put(JSONKey.RISKOFLOSS.toString(), "???");
            riskJSON.put(JSONKey.WEIGHTAA.toString(), FormatUtil.formatPercentage(aa));
            riskJSON.put(JSONKey.WEIGHTA.toString(), FormatUtil.formatPercentage(a));
            riskJSON.put(JSONKey.WEIGHTB.toString(), FormatUtil.formatPercentage(b));
            riskJSON.put(JSONKey.WEIGHTC.toString(), FormatUtil.formatPercentage(c));
            riskJSON.put(JSONKey.WEIGHTD.toString(), FormatUtil.formatPercentage(d));
            riskJSON.put(JSONKey.WEIGHTE.toString(), FormatUtil.formatPercentage(e));
            riskJSON.put(JSONKey.WEIGHTF.toString(), FormatUtil.formatPercentage(f));
            riskJSON.put(JSONKey.WEIGHTG.toString(), FormatUtil.formatPercentage(g));
            riskJSON.put(JSONKey.AA_PERCENT.toString(), aa);
            riskJSON.put(JSONKey.A_PERCENT.toString(), a);
            riskJSON.put(JSONKey.B_PERCENT.toString(), b);
            riskJSON.put(JSONKey.C_PERCENT.toString(), c);
            riskJSON.put(JSONKey.D_PERCENT.toString(), d);
            riskJSON.put(JSONKey.E_PERCENT.toString(), e);
            riskJSON.put(JSONKey.F_PERCENT.toString(), f);
            riskJSON.put(JSONKey.G_PERCENT.toString(), g);
            riskJSON.put(JSONKey.AALOANNUMBER.toString(), mPoint.getLoanCountByGrade(LoanGrade.AA));
            riskJSON.put(JSONKey.ALOANNUMBER.toString(), mPoint.getLoanCountByGrade(LoanGrade.A));
            riskJSON.put(JSONKey.BLOANNUMBER.toString(), mPoint.getLoanCountByGrade(LoanGrade.B));
            riskJSON.put(JSONKey.CLOANNUMBER.toString(), mPoint.getLoanCountByGrade(LoanGrade.C));
            riskJSON.put(JSONKey.DLOANNUMBER.toString(), mPoint.getLoanCountByGrade(LoanGrade.D));
            riskJSON.put(JSONKey.ELOANNUMBER.toString(), mPoint.getLoanCountByGrade(LoanGrade.E));
            riskJSON.put(JSONKey.FLOANNUMBER.toString(), mPoint.getLoanCountByGrade(LoanGrade.F));
            riskJSON.put(JSONKey.GLOANNUMBER.toString(), mPoint.getLoanCountByGrade(LoanGrade.G));
            riskJSON.put(JSONKey.NUMLOANFRACTIONS.toString(), order.getCartItems().size());
            riskJSON.put(JSONKey.PORTFOLIOTITLE.toString(), order.getTitle());
            riskJSON.put(JSONKey.PORTFOLIOTOTALAMOUNT.toString(), ((LCOrderImpl) order).getDBOrder().getCurrAmnt());
            EnumMap<LoanType, Double> loanTypeComposition = mPoint.getLoanTypeComposition();
            riskJSON.put(JSONKey.CCOLLATERALIZED_LOANNUMBER.toString(), mPoint.getLoanCountByType(LoanType.COLLATERALIZED));
            riskJSON.put(JSONKey.PERSONAL_LOANNUMBER.toString(), mPoint.getLoanCountByType(LoanType.PERSONAL));
            double percent_collateral = loanTypeComposition.get(LoanType.COLLATERALIZED).doubleValue();
            riskJSON.put(JSONKey.CCOLLATERALIZED_PERCENT.toString(), percent_collateral);
            riskJSON.put(JSONKey.CCOLLATERALIZED_WIDTH.toString(), FormatUtil.formatPercentage(percent_collateral));
            double percent_personal = loanTypeComposition.get(LoanType.PERSONAL).doubleValue();
            riskJSON.put(JSONKey.PERSONAL_PERCENT.toString(), percent_personal);
            riskJSON.put(JSONKey.PERSONAL_WIDTH.toString(), FormatUtil.formatPercentage(percent_personal));
            EnumMap<LoanMaturity, Double> loanMaturityeComposition = mPoint.getMaturityComposition();
            double percent_year3 = loanMaturityeComposition.get(LoanMaturity.Year3).doubleValue();
            riskJSON.put(JSONKey.YEAR3_PERCENT.toString(), percent_year3);
            riskJSON.put(JSONKey.YEAR3_WIDTH.toString(), FormatUtil.formatPercentage(percent_year3));
            double percent_year5 = loanMaturityeComposition.get(LoanMaturity.Year5).doubleValue();
            riskJSON.put(JSONKey.YEAR5_PERCENT.toString(), percent_year5);
            riskJSON.put(JSONKey.YEAR5_WIDTH.toString(), FormatUtil.formatPercentage(percent_year5));
            riskJSON.put(JSONKey.YEAR3_LOANNUMBER.toString(), mPoint.getLoanCountByMaturity(LoanMaturity.Year3));
            riskJSON.put(JSONKey.YEAR5_LOANNUMBER.toString(), mPoint.getLoanCountByMaturity(LoanMaturity.Year5));
            return riskJSON;
        } catch (Exception e) {
            logger.warning("Error converting the order composition details to  JSON object: " + e);
            return new JSONObject();
        }
    }

    public static void main(String[] argv) throws Exception {
        System.out.println("empty JSONObject: " + new JSONObject().toString());
    }

    /**
     * JSONArray of loans
     * @param loans
     * @return
     */
    public static JSONArray build2(List<LCSecuritizedLoanDetail> secLoans, LCImmutableActor actor) throws JSONException {
        JSONArray list = new JSONArray();
        for (Iterator<LCSecuritizedLoanDetail> iter = secLoans.iterator(); iter.hasNext(); ) {
            LCSecuritizedLoanDetail loan = iter.next();
            JSONObject loanJS = build(loan, actor);
            list.put(loanJS);
        }
        return list;
    }

    /**
     * In case we add a group of notes to an order and notes need to be modified, create a json representation of the notes that were modified and return so that the 
     * corresponding notes are updated.
     * Note, in parallel the state object LSR is updated for pagination requests and sorting
     * @param notAdded
     * @return
     */
    public static JSONObject buildAddModifications(List<LCShoppingCartItem> notAdded) {
        try {
            JSONObject container = new JSONObject();
            List<JSONObject> fractionsJSON = new ArrayList<JSONObject>();
            JSONObject lfJSON = null;
            for (LCShoppingCartItem oi : notAdded) {
                lfJSON = new JSONObject();
                lfJSON.put(JSONKey.LOANFRACTIONAMOUNTADDED.toString(), oi.getAmount());
                lfJSON.put(JSONKey.LOANFRANCTIONAMOUNTREQUESTEDTOADD.toString(), oi.getRequestedAmount());
                lfJSON.put(JSONKey.LOANID.toString(), oi.getLoanId());
                lfJSON.put(JSONKey.ALREADYINVESTEDIN.toString(), true);
                lfJSON.put(JSONKey.ISINCURRENTORDER.toString(), true);
                fractionsJSON.add(lfJSON);
            }
            container.put(JSONKey.LOANFRACTIONS.toString(), fractionsJSON);
            return container;
        } catch (Exception e) {
            logger.warning("Error converting ORDER to JSON object: " + e);
            return new JSONObject();
        }
    }

    public static JSONObject build(LCLoanItemSearchResult loan, LCImmutableActor actor) throws JSONException {
        JSONObject jso = new JSONObject();
        if (null == loan || null == actor) return jso;
        try {
            jso.put(JSONKey.LCPORTFOLIO_NAME.toString(), loan.getPortfolioName());
            jso.put(JSONKey.SEARCHRESULT_TITLE.toString(), loan.getLoanTitle());
            jso.put(JSONKey.SEARCHRESULT_AMOUNTLENT.toString(), loan.getInvestment());
            jso.put(JSONKey.SEARCHRESULT_RATE.toString(), loan.getLoanClass().name() + " : " + FormatUtil.formatPercentageTwoDecimal(loan.getIntRate()));
            jso.put(JSONKey.SEARCHRESULT_PRINCIPAL_REMAININT.toString(), FormatUtil.formatTwoDecimalAmount(loan.getPrincipalRemaining()));
            jso.put(JSONKey.SEARCHRESULT_PAYMENTS_RECEIVED_TO_DATE.toString(), FormatUtil.formatTwoDecimalAmount(loan.getPaymentsRecieved()));
            jso.put(JSONKey.SEARCHRESULT_PORTFOLIO_ID.toString(), loan.getPortfolioId());
            jso.put(JSONKey.SEARCHRESULT_NEXT_PAYMENT_DATE.toString(), FormatUtil.dateFormat("MMMM dd, yyyy", loan.getActualNextPaymentDate()));
            jso.put(JSONKey.SEARCHRESULTS_STATUS.toString(), loan.getLoanStatus().getLabel());
            jso.put(JSONKey.SEARCHRESULTS_NOTEID.toString(), loan.getNoteId());
            int noteType = 0;
            if (loan.isSecuritized()) noteType = 1;
            jso.put(JSONKey.SEARCHRESULTS_NOTETYPE.toString(), noteType);
            jso.put(JSONKey.SEARCHRESULT_LOANID.toString(), loan.getLoanId());
            CreditTrend trend = loan.getCreditTrend();
            int loanTrend = 1;
            if (trend == CreditTrend.DOWN) loanTrend = 0; else if (trend == CreditTrend.UP) loanTrend = 2; else if (trend == CreditTrend.FLAT) loanTrend = 1;
            jso.put(JSONKey.LOAN_CREDIT_SCORE_TREND.toString(), loanTrend);
            double accrual = 0;
            try {
                accrual = loan.getAccruedInterest();
                if (accrual > 0) {
                } else {
                    accrual = 0;
                }
            } catch (Exception e) {
                accrual = 0;
            }
            jso.put(JSONKey.SEARCHRESULT_ACCRUAL.toString(), FormatUtil.formatTwoDecimalAmount(accrual));
            jso.put(JSONKey.ORDER_ID.toString(), loan.getOrderId());
            jso.put(JSONKey.LOANLENGTH.toString(), loan.getLoanMaturity().getMaturity());
            jso.put(JSONKey.LOAN_TYPE.toString(), loan.getLoanType().getLabel());
        } catch (NullPointerException e) {
            logger.warning("NullPointer while accessing LCLoanItemSearchResult for JSON generation");
        }
        return jso;
    }

    public static JSONObject build(BaseLCLoanItemRecord loan, LCPortfolio porty, String recordIndex) throws JSONException {
        JSONObject jso = new JSONObject();
        if (null == loan) return jso;
        try {
            if (null == porty) {
                jso.put(JSONKey.LCPORTFOLIO_NAME.toString(), "");
                jso.put(JSONKey.LCPORTFOLIO_ID.toString(), "0");
            } else {
                jso.put(JSONKey.LCPORTFOLIO_NAME.toString(), porty.getName());
                jso.put(JSONKey.LCPORTFOLIO_ID.toString(), porty.getGuid());
            }
            jso.put(JSONKey.SEARCHRESULT_LOANID.toString(), loan.getLoanID());
            jso.put(JSONKey.ORDER_ID.toString(), loan.getOrderId());
            jso.put(JSONKey.RECORD_ID.toString(), recordIndex);
        } catch (NullPointerException e) {
            logger.warning("NullPointer while accessing LCLoanItemSearchResult for JSON generation");
        }
        return jso;
    }

    public static JSONObject build(LCPortfolioSummary porty, LCImmutableActor actor) throws JSONException {
        JSONObject jso = new JSONObject();
        if (null == actor) return jso;
        if (null == porty) {
            jso.put(JSONKey.LCPORTFOLIO_ID.toString(), -1);
            jso.put(JSONKey.LCPORTFOLIO_COUNT.toString(), 0);
            jso.put(JSONKey.SEARCHRESULT_AMOUNTLENT.toString(), 0);
            jso.put(JSONKey.SEARCHRESULT_RATE.toString(), "0.00%");
            jso.put(JSONKey.SEARCHRESULT_PRINCIPAL_REMAININT.toString(), 0.00);
            jso.put(JSONKey.SEARCHRESULT_PAYMENTS_RECEIVED_TO_DATE.toString(), 0.00);
            jso.put(JSONKey.LCPORTFOLIO_EXPECTED_MONTHLY_PAYMENT.toString(), 0.00);
            return jso;
        }
        try {
            jso.put(JSONKey.LCPORTFOLIO_ID.toString(), porty.getPortfolioId());
            jso.put(JSONKey.LCPORTFOLIO_NAME.toString(), porty.getName());
            jso.put(JSONKey.LCPORTFOLIO_COUNT.toString(), porty.getCount());
            double temp = 0;
            if (null != porty.getAmountLent()) {
                temp = porty.getAmountLent();
            }
            jso.put(JSONKey.SEARCHRESULT_AMOUNTLENT.toString(), temp);
            temp = 0;
            if (null != porty.getWeightedrate()) {
                temp = porty.getWeightedrate();
            }
            jso.put(JSONKey.SEARCHRESULT_RATE.toString(), FormatUtil.formatPercentageTwoDecimal(temp));
            temp = 0;
            if (null != porty.getRemPrincipal()) {
                temp = porty.getRemPrincipal();
            }
            jso.put(JSONKey.SEARCHRESULT_PRINCIPAL_REMAININT.toString(), FormatUtil.formatTwoDecimalAmount(temp));
            temp = 0;
            if (null != porty.getRecievedPayments()) {
                temp = porty.getRecievedPayments();
            }
            jso.put(JSONKey.SEARCHRESULT_PAYMENTS_RECEIVED_TO_DATE.toString(), FormatUtil.formatTwoDecimalAmount(temp));
            temp = 0;
            if (null != porty.getExpectedMtlPayments()) {
                temp = porty.getExpectedMtlPayments();
            }
            jso.put(JSONKey.LCPORTFOLIO_EXPECTED_MONTHLY_PAYMENT.toString(), porty.getExpectedMtlPayments());
        } catch (NullPointerException e) {
            logger.warning("NullPointer while accessing LCLoanItemSearchResult for JSON generation");
        }
        return jso;
    }

    public static JSONArray getAllLCPortfolioNames(LCImmutableActor actor) throws JSONException {
        JSONArray array = new JSONArray();
        if (null == actor) return array;
        try {
            List<LCPortfolio> portys = actor.getAllPortfolios();
            for (LCPortfolio porty : portys) {
                JSONObject jso = new JSONObject();
                jso.put(JSONKey.LCPORTFOLIO_ID.toString(), porty.getGuid());
                jso.put(JSONKey.LCPORTFOLIO_NAME.toString(), porty.getName());
                array.put(jso);
            }
        } catch (NullPointerException e) {
            logger.warning("NullPointer while accessing LCLoanItemSearchResult for JSON generation");
        }
        return array;
    }

    /**
	 * Returns a JSON object representing the given lcportfolio. 
	 * The string representation will detail 
	 * the name of the lcportfolio
	 * the id of the lcportfolio
	 * the json representation of the lc loans contained by the portfolio
	 * 
	 * @param portfolio portfolio to convert
	 * @return JSON object
	 */
    public static JSONObject build(LCPortfolio portfolio, LCImmutableActor actor) {
        JSONObject container = new JSONObject();
        try {
            container.put(JSONKey.LCPORTFOLIO_NAME.toString(), portfolio.getName());
            container.put(JSONKey.LCPORTFOLIO_DESC.toString(), portfolio.getDescription());
            LCLoanItemSearchQuery query = new LCLoanItemSearchQuery();
            query.addPortfolioFilter(portfolio.getGuid());
            List<LCLoanItemSearchResult> results = LCLoanRepository.filterOutExpiredForUI(LCLoanRepository.searchLoanItemRecord(actor.getGuid(), query));
            container.put(JSONKey.RESULTS.toString(), JSONBuilder.build(results, actor));
            container.put("itemsFound", results.size());
            return container;
        } catch (JSONException e) {
            return null;
        }
    }

    /**
	 * JSONArray of loans
	 * @param loans
	 * @return a non-null JSONObject.
	 */
    public static JSONObject build(List<LCLoanItemSearchResult> loans, LCImmutableActor actor) throws JSONException {
        JSONObject container = new JSONObject();
        JSONArray list = new JSONArray();
        for (Iterator<LCLoanItemSearchResult> iter = loans.iterator(); iter.hasNext(); ) {
            LCLoanItemSearchResult loan = iter.next();
            JSONObject loanJS = new JSONObject();
            loanJS = build(loan, actor);
            list.put(loanJS);
        }
        container.put(JSONKey.TOTAL_NUMBER_OF_LOANS.toString(), list.length());
        container.put(JSONKey.RESULTS.toString(), list);
        return container;
    }

    public static JSONArray build(List<BaseLCLoanItemRecord> loans, LCPortfolio porty, String[] records) throws JSONException {
        JSONArray list = new JSONArray();
        if (null == loans) return list;
        int orphans = 0;
        for (int i = 0; i < loans.size(); i++) {
            BaseLCLoanItemRecord record = loans.get(i);
            JSONObject recordJS = new JSONObject();
            if (record != null) {
                recordJS = build(record, porty, records[i]);
                list.put(recordJS);
            }
        }
        return list;
    }

    /**
	 * JSONArray of loans
	 * @param loans
	 * @return
	 */
    public static JSONObject buildJSON(List<LCPortfolioSummary> portfolio, LCImmutableActor actor) throws JSONException {
        JSONObject container = new JSONObject();
        JSONArray list = new JSONArray();
        int orphans = 0;
        for (Iterator<LCPortfolioSummary> iter = portfolio.iterator(); iter.hasNext(); ) {
            LCPortfolioSummary porty = iter.next();
            JSONObject portyJS = new JSONObject();
            if (porty.getName() != null) {
                portyJS = build(porty, actor);
                list.put(portyJS);
            } else {
                orphans = porty.getCount();
            }
        }
        container.put(JSONKey.TOTAL_NUMBER_OF_LOANS.toString(), orphans);
        container.put(JSONKey.RESULTS.toString(), list);
        return container;
    }
}
