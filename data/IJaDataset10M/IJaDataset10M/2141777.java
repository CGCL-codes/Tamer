package tlc.ui.action.secMarket;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lc.commonservice.impl.LCImmutableActor;
import lc.commonservice.impl.LCRole;
import lc.search.LoanSearchOld;
import lc.secmarketservice.impl.LCSecuritizedLoanDetailLightWeight;
import lc.secmarketservice.repository.LCTradeRepository;
import lc.ui.misc.LCActorWrapper;
import lc.ui.request.RequestErrorKey;
import lc.ui.request.RequestErrorObject;
import lc.ui.request.RequestParam;
import lc.ui.request.RequestUtils;
import lc.ui.state.SecMarketLoanSearchResults;
import org.apache.struts2.ServletActionContext;
import tlc.platform.core.util.LoggerManager;
import tlc.ui.SearchAttribute;
import tlc.ui.action.ResultCode;
import tlc.ui.action.TLCAction;
import tlc.ui.thirdparty.omniture.OmnitureReporter;

/**
 * Encapsulates all logic required for searching for sec market loan items
 * @author Craig
 * @author Tom
 * modified amassillo - Performance Project 07/16/2010
 */
@SuppressWarnings({ "unqualified-field-access", "serial" })
public class SecMarketLoanSearch extends TLCAction {

    private static Logger logger = LoggerManager.getLogger(SecMarketLoanSearch.class);

    public static final String DATA_SESSION_KEY = "SecMarketLoanSearch_data";

    private String mode = null;

    private LCImmutableActor actor = null;

    private int maxNumOfRemainingPayments = 60;

    /**
	 * Handler for browsing loans. This method executes a search based on any
	 * search terms found in request, sets the search results as a request
	 * attribute encapsulated in a LoanSearchResults object, and resets the
	 * page index back to 1. 
     * 
     * ResultCode.SUCCESS: if search was executed successfully. Results can be accessed via
     *     LoanSearchResults object.
     * ResultCode.INPUT: if an invalid search parameter was specified
	 * @return
	 */
    public String browseLoans() {
        actor = getCachedActor();
        LoanSearchOld sq;
        try {
            sq = buildSearchQuery(request);
        } catch (IllegalArgumentException e) {
            RequestErrorObject.setError(request, RequestErrorKey.INVALID_SEARCH_CRITERIA);
            return ResultCode.INPUT;
        }
        List<LCSecuritizedLoanDetailLightWeight> results = execSearch(sq, actor);
        if (results == null) return ResultCode.ERROR;
        SecMarketLoanSearchResults lsr = SecMarketLoanSearchResults.getInstance(request.getSession());
        lsr.setResults(results);
        lsr.setCurrentPage(1);
        request.setAttribute(RequestParam.search_term.toString(), sq);
        lsr.setSearchQuery(request);
        if (mode != null && mode.equalsIgnoreCase("search")) {
            if (sq != null && sq.getTextQuery() != null) OmnitureReporter.setVariable(request.getSession(), "eVar12", sq.getTextQuery());
            int numResults = results.size();
            if (numResults > 0) {
                OmnitureReporter.setVariable(request.getSession(), "events", "event4");
                OmnitureReporter.setVariable(request.getSession(), "eVar22", Integer.toString(numResults));
                OmnitureReporter.setVariable(request.getSession(), "eVar21", Double.toString(sq.getMinPercFunded()));
                double totalInterest = 0;
                for (LCSecuritizedLoanDetailLightWeight loan : results) totalInterest += loan.getIntRate();
                OmnitureReporter.setVariable(request.getSession(), "eVar20", Double.toString(totalInterest / numResults));
            } else OmnitureReporter.setVariable(request.getSession(), "events", "event7");
        }
        return ResultCode.SUCCESS;
    }

    /**
	 * Build a search query from the HttpServletRequest
	 * TODO: specify the exact parameters we're looking for!
	 * @param req
	 * @return
     * @throws IllegalArgumentException if any of the parameters are invalid
	 */
    public static LoanSearchOld buildSearchQuery(HttpServletRequest req) throws IllegalArgumentException {
        LoanSearchOld sq = new LoanSearchOld();
        String[] searchAttrs = RequestUtils.getRequestParameterValues(req, RequestParam.search_term.toString());
        for (String attr : searchAttrs) {
            try {
                SearchAttribute sa = SearchAttribute.valueOf(attr);
                sa.setSearchQuery(sq);
            } catch (IllegalArgumentException e) {
                logger.warning("Invalid search attribute received: " + attr);
                throw e;
            }
        }
        String[] loanRepaymentStatusAttrs = RequestUtils.getRequestParameterValues(req, RequestParam.search_status.toString());
        for (String attr : loanRepaymentStatusAttrs) {
            try {
                SearchAttribute sa = SearchAttribute.valueOf(attr);
                sa.setSearchQuery(sq);
            } catch (Exception e) {
                logger.warning("Invalid search attribute received: " + attr);
                throw new IllegalArgumentException();
            }
        }
        String[] remainingPaymentsAttrs = RequestUtils.getRequestParameterValues(req, RequestParam.search_remaining_payments.toString());
        for (String attr : remainingPaymentsAttrs) {
            try {
                int myDays = new Integer(attr).intValue();
                sq.setMaxRemainingPaymentsCount(myDays);
            } catch (Exception e) {
                logger.warning("Invalid search attribute received: " + attr);
                throw new IllegalArgumentException();
            }
        }
        return sq;
    }

    /**
	 * Execute a search on the data and return results
	 * @param actor TODO
	 * @return
	 */
    public static List<LCSecuritizedLoanDetailLightWeight> execSearch(LoanSearchOld sq, LCImmutableActor actor) {
        List<LCSecuritizedLoanDetailLightWeight> loans;
        try {
            LCTradeRepository lcTradeRep = new LCTradeRepository();
            loans = lcTradeRep.searchSecuritizedLoansLightweight(sq);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Unexpected exception ", e);
            return null;
        }
        return loans;
    }

    /**
	 * This method is invoked when the user tries to create a loan, and returns the role the user has:
	 * -- "guest"
	 * -- "member"
	 * -- "lender_incomplete" // TODO
	 * -- "lender_completed" // TODO
	 * -- "lender"
	 * @return
	 */
    public String startLendProcess() {
        if (getLoginSession() == null) {
            return "guest";
        }
        LCImmutableActor ractor = getCachedActor();
        if (LCActorWrapper.isLender(ractor)) {
            if (ractor.getCurrentOrder() != null) {
                return "lender_open_portfolio";
            }
            return "lender";
        } else if (ractor.getRole().in(LCRole.ENDUSER)) {
            return "member";
        } else {
            return "guest";
        }
    }

    /**
	 * Call to create environment for clean loan search. 
	 * @return
	 */
    public String newLoanSearch() {
        HttpSession session = request.getSession();
        session.setAttribute(DATA_SESSION_KEY, new HashMap<String, Object>());
        SecMarketLoanSearchResults lsr = SecMarketLoanSearchResults.getInstance(session);
        lsr.clearResults();
        return browseLoans();
    }

    /**
	 * Set the pagesize for each result page
	 * @return
	 */
    public String setPageSize() {
        HttpServletRequest req = ServletActionContext.getRequest();
        String pagesizeStr = RequestUtils.getRequestParameter(req, RequestParam.pagesize.toString());
        int pagesize;
        try {
            pagesize = Integer.parseInt(pagesizeStr);
            SecMarketLoanSearchResults lsr = SecMarketLoanSearchResults.getInstance(req.getSession());
            lsr.setPageSize(pagesize);
        } catch (NumberFormatException e) {
            logger.warning("Attempt to change pagesize to non-integer value: " + pagesizeStr);
        }
        return ResultCode.SUCCESS;
    }

    /**
	 * Set the page index for the result page to be displayed
	 * @return
	 */
    public String setPageIndex() {
        HttpServletRequest req = ServletActionContext.getRequest();
        String pageIndexStr = RequestUtils.getRequestParameter(req, RequestParam.pageindex.toString());
        int pageindex;
        try {
            pageindex = Integer.parseInt(pageIndexStr);
            SecMarketLoanSearchResults lsr = SecMarketLoanSearchResults.getInstance(req.getSession());
            lsr.setCurrentPage(pageindex);
        } catch (NumberFormatException e) {
            logger.warning("Attempt to change pageindex to non-integer value: " + pageIndexStr);
        }
        return ResultCode.SUCCESS;
    }

    /**
	 * Set mode to either "browse" or "search"
	 * @param m
	 */
    public void setMode(String m) {
        this.mode = m;
    }

    public LCImmutableActor getActor() {
        return actor;
    }

    public void setActor(LCImmutableActor actor) {
        this.actor = actor;
    }

    public int getMaxNumOfRemainingPayments() {
        return maxNumOfRemainingPayments;
    }
}
