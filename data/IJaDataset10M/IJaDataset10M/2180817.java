package dk.bot.betfairservice;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.betfair.publicapi.types.exchange.v5.GetAccountFundsResp;
import com.betfair.publicapi.types.global.v3.EventType;
import com.betfair.publicapi.types.global.v3.LoginErrorEnum;
import com.betfair.publicapi.types.global.v3.LoginReq;
import com.betfair.publicapi.types.global.v3.LoginResp;
import com.betfair.publicapi.v3.bfglobalservice.BFGlobalService;
import com.betfair.publicapi.v5.bfexchangeservice.BFExchangeService;
import dk.bot.betfairservice.command.CancelBetCommand;
import dk.bot.betfairservice.command.GetAccountFundsCommand;
import dk.bot.betfairservice.command.GetAccountStatementCommand;
import dk.bot.betfairservice.command.GetEventTypesCommand;
import dk.bot.betfairservice.command.GetMUBetCommand;
import dk.bot.betfairservice.command.GetMUBetsCommand;
import dk.bot.betfairservice.command.GetMarketDetailsCommand;
import dk.bot.betfairservice.command.GetMarketRunnersCommand;
import dk.bot.betfairservice.command.GetMarketTradedVolumeCommand;
import dk.bot.betfairservice.command.GetMarketsCommand;
import dk.bot.betfairservice.command.PlaceBetCommand;
import dk.bot.betfairservice.command.PlaceSPBetCommand;
import dk.bot.betfairservice.counters.DataRequestCounter;
import dk.bot.betfairservice.counters.TransactionCounter;
import dk.bot.betfairservice.model.BFAccountStatementItem;
import dk.bot.betfairservice.model.BFBetCancelResult;
import dk.bot.betfairservice.model.BFBetPlaceResult;
import dk.bot.betfairservice.model.BFBetStatus;
import dk.bot.betfairservice.model.BFBetType;
import dk.bot.betfairservice.model.BFMUBet;
import dk.bot.betfairservice.model.BFMarketData;
import dk.bot.betfairservice.model.BFMarketDetails;
import dk.bot.betfairservice.model.BFMarketRunners;
import dk.bot.betfairservice.model.BFMarketTradedVolume;
import dk.bot.betfairservice.model.BFSPBetPlaceResult;
import dk.bot.betfairservice.model.LoginResponse;
import dk.bot.betfairservice.state.BetFairState;
import dk.bot.betfairservice.state.BetFairStateImpl;

public class BetFairServiceImpl implements BetFairService {

    private final Log log = LogFactory.getLog(BetFairServiceImpl.class.getSimpleName());

    private static final String BF_RESP_OK = "OK";

    private static final String BF_RESP_NOSESSION = "NO_SESSION";

    private String user;

    private String password;

    private int productId;

    private BFExchangeService exchangeWebService;

    private BFGlobalService globalWebService;

    private BetFairState state;

    private TransactionCounter txCounter;

    private DataRequestCounter dataRequestCounter;

    /**
	 * 
	 * @param userName
	 * @param password
	 * @param productId
	 *            , e.g. 82 for free api
	 * @throws BetFairException
	 */
    public BetFairServiceImpl() {
        state = new BetFairStateImpl();
        txCounter = new TransactionCounter();
        dataRequestCounter = new DataRequestCounter();
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public LoginResponse login() {
        boolean success = false;
        String apiStatusCode = null;
        String loginStatusCode = null;
        String exceptionMessage = null;
        try {
            Thread.sleep(250);
            LoginReq req = new LoginReq();
            req.setUsername(user);
            req.setPassword(password);
            req.setProductId(productId);
            req.setLocationId(0);
            req.setVendorSoftwareId(0);
            LoginResp resp = globalWebService.login(req);
            if (resp.getErrorCode().equals(LoginErrorEnum.OK)) {
                state.setSessionToken(resp.getHeader().getSessionToken());
                success = true;
            }
            apiStatusCode = resp.getHeader().getErrorCode().name();
            loginStatusCode = resp.getErrorCode().name();
        } catch (Exception e) {
            exceptionMessage = e.getMessage();
        }
        if (success) {
            log.info("Login successfull.");
        } else {
            log.info("Login error." + "User: " + user + ", ProductId: " + productId + ", apiCode: " + apiStatusCode + ", loginCode: " + loginStatusCode + ", exception: " + exceptionMessage);
        }
        return new LoginResponse(success, apiStatusCode, loginStatusCode, exceptionMessage);
    }

    public List<EventType> getEventTypes() {
        GetEventTypesCommand cmd = new GetEventTypesCommand();
        execute(cmd, 1);
        return cmd.getEventTypes();
    }

    public List<BFMarketData> getMarkets(Date fromDate, Date toDate, Set<Integer> eventTypeIds) {
        GetMarketsCommand cmd = new GetMarketsCommand(fromDate, toDate, eventTypeIds);
        execute(cmd, 1);
        return cmd.getMarkets();
    }

    public BFMarketRunners getMarketRunners(int marketId) throws BetFairException {
        GetMarketRunnersCommand command = new GetMarketRunnersCommand(marketId);
        execute(command, 1);
        return command.getMarketRunners();
    }

    public List<BFMUBet> getMUBets(BFBetStatus betStatus) throws BetFairException {
        GetMUBetsCommand command = new GetMUBetsCommand(betStatus, null, null);
        execute(command, 5);
        return command.getMUBets();
    }

    public List<BFMUBet> getMUBets(BFBetStatus betStatus, int marketId) throws BetFairException {
        GetMUBetsCommand command = new GetMUBetsCommand(betStatus, marketId, null);
        execute(command, 1);
        return command.getMUBets();
    }

    /** Returns all matched and unmatched portions of a bet. */
    public List<BFMUBet> getMUBet(long betId) {
        GetMUBetCommand command = new GetMUBetCommand(betId);
        execute(command, 1);
        return command.getMUBet();
    }

    /**
	 * Returns bets since given time for a given market
	 * 
	 * @param betStatus
	 * @param marketId
	 * @param matchedSince
	 * @return
	 * @throws BetFairException
	 */
    public List<BFMUBet> getMUBets(BFBetStatus betStatus, int marketId, Date matchedSince) {
        GetMUBetsCommand command = new GetMUBetsCommand(betStatus, marketId, matchedSince);
        execute(command, 1);
        return command.getMUBets();
    }

    public BFMarketDetails getMarketDetails(int marketId) throws BetFairException {
        GetMarketDetailsCommand command = new GetMarketDetailsCommand(marketId);
        execute(command, 1);
        return command.getMarketDetails();
    }

    @Override
    public BFMarketTradedVolume getMarketTradedVolume(int marketId) {
        GetMarketTradedVolumeCommand command = new GetMarketTradedVolumeCommand(marketId);
        execute(command, 1);
        return command.getMarketTradedVolume();
    }

    public BFBetCancelResult cancelBet(long betId) throws BetFairException {
        CancelBetCommand command = new CancelBetCommand(betId);
        execute(command, 1);
        return command.cancelBet();
    }

    public BFBetPlaceResult placeBet(int marketId, int selectionId, BFBetType betType, double price, double size, boolean checkTxLimit) {
        if (checkTxLimit) {
            txCounter.waitForPermission(System.currentTimeMillis(), false);
        }
        PlaceBetCommand command = new PlaceBetCommand(marketId, selectionId, betType, price, size);
        execute(command, 1);
        return command.placeBet();
    }

    public BFSPBetPlaceResult placeSPBet(int marketId, int selectionId, BFBetType betType, double liability, Double limit) {
        txCounter.waitForPermission(System.currentTimeMillis(), false);
        PlaceSPBetCommand command = new PlaceSPBetCommand(marketId, selectionId, betType, liability, limit);
        execute(command, 1);
        return command.placeBet();
    }

    public List<BFAccountStatementItem> getAccountStatement(Date startDate, Date endDate, int recordCount) {
        GetAccountStatementCommand command = new GetAccountStatementCommand(startDate, endDate, recordCount);
        execute(command, 1);
        return command.getAccountStatementItems();
    }

    public GetAccountFundsResp getAccountFunds() {
        GetAccountFundsCommand command = new GetAccountFundsCommand();
        execute(command, 1);
        return command.getAccountFunds();
    }

    /**
	 * 
	 * @param command
	 * @param delay
	 * @param weight
	 *            Weight of request used to protect against Data requests
	 *            charge.
	 */
    private void execute(BFCommand command, int weight) {
        dataRequestCounter.waitForPermission(System.currentTimeMillis(), weight);
        try {
            command.execute(exchangeWebService, globalWebService, state.getSessionToken());
            if (command.getErrorCode().equals(BF_RESP_OK)) {
                state.setSessionToken(command.getSessionToken());
            } else if (command.getErrorCode().equals(BF_RESP_NOSESSION)) {
                log.info("No session. Logging in...");
                login();
                throw new BetFairException("No session. Logging in...");
            }
        } catch (RuntimeException e) {
            throw new BetFairException(e);
        }
    }

    public BetFairServiceInfo getBetFairServiceInfo() {
        BetFairServiceInfo info = new BetFairServiceInfo();
        info.setTxCounterState(txCounter.getState());
        info.setMaxDataRequestPerSecond(dataRequestCounter.getMaxRequestsPerSecond());
        info.setLastSecDataRequest(dataRequestCounter.getLastSecRequestsNum());
        return info;
    }

    public void setExchangeWebService(BFExchangeService exchangeWebService) {
        this.exchangeWebService = exchangeWebService;
    }

    public void setGlobalWebService(BFGlobalService globalWebService) {
        this.globalWebService = globalWebService;
    }
}
