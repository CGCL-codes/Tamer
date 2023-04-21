package dk.bot.betfairservice.command;

import com.betfair.publicapi.types.exchange.v5.APIRequestHeader;
import com.betfair.publicapi.types.exchange.v5.GetAccountFundsErrorEnum;
import com.betfair.publicapi.types.exchange.v5.GetAccountFundsReq;
import com.betfair.publicapi.types.exchange.v5.GetAccountFundsResp;
import com.betfair.publicapi.v3.bfglobalservice.BFGlobalService;
import com.betfair.publicapi.v5.bfexchangeservice.BFExchangeService;
import dk.bot.betfairservice.BFCommand;
import dk.bot.betfairservice.BetFairException;

/**
 * Get account funds command.
 * 
 * @author daniel
 * 
 */
public class GetAccountFundsCommand implements BFCommand {

    private GetAccountFundsResp resp;

    public void execute(BFExchangeService exchangeWebService, BFGlobalService globalWebService, String sessionToken) {
        APIRequestHeader requestHeader = new APIRequestHeader();
        requestHeader.setSessionToken(sessionToken);
        GetAccountFundsReq req = new GetAccountFundsReq();
        req.setHeader(requestHeader);
        resp = exchangeWebService.getAccountFunds(req);
    }

    public String getErrorCode() {
        return resp.getHeader().getErrorCode().name();
    }

    public String getSessionToken() {
        return resp.getHeader().getSessionToken();
    }

    public GetAccountFundsResp getAccountFunds() {
        if (resp.getErrorCode().equals(GetAccountFundsErrorEnum.OK)) {
            return resp;
        } else {
            throw new BetFairException("getAccountFunds: " + resp.getErrorCode().name());
        }
    }
}
