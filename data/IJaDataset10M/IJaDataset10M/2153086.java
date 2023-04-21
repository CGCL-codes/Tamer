package cwsexamples.stockquote;

import cartago.ArtifactId;
import cartago.CartagoException;
import cartago.Op;
import cartago.OpFeedbackParam;
import cartagows.CartagowsParam;
import cartagows.WSMsgBasicFilter;
import cartagows.WSMsgInfo;
import cartagows.util.WSAgent;

public class StockQuoteServiceAgent extends WSAgent {

    ArtifactId msgBox;

    public StockQuoteServiceAgent(String agentName) throws CartagoException {
        super(agentName);
        try {
            msgBox = makeArtifact("StockQuoteService", "cartagows.WSPanel", new Object[] { "data/StockQuote.wsdl", "StockQuoteService" });
        } catch (CartagoException e) {
            log("[SERVICE_AGENT:]artifact already there.");
            try {
                msgBox = lookupArtifact("StockQuoteService");
                disposeArtifact(msgBox);
                msgBox = makeArtifact("StockQuoteService", CartagowsParam.WSPANEL_CLASS_PATH, new Object[] { "data/StockQuote.wsdl" });
            } catch (CartagoException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void run() {
        OpFeedbackParam<WSMsgInfo> res = new OpFeedbackParam<WSMsgInfo>();
        while (true) {
            try {
                log("[SERVICE_AGENT:]waiting messages");
                doAction(msgBox, new Op("getWSMsgWithFilter", new WSMsgBasicFilter("GetLastTradePrice"), res));
                WSMsgInfo msg = res.get();
                String name = msg.getOperationName();
                log("[SERVICE_AGENT:]New req: " + name);
                log("[SERVICE_AGENT:]Msg:: " + msg.getMsgContent());
                if (name.equals("GetLastTradePrice")) {
                    log("[SERVICE_AGENT:]Sending reply..");
                    String replyMsg = "<tns:TradePrice xmlns:tns=\"http://example.com/stockquote.wsdl\">" + "  <tns:price>303</tns:price>" + "</tns:TradePrice>";
                    doAction(msgBox, new Op("sendWSReply", msg, replyMsg, new OpFeedbackParam<String>()));
                    log("[SERVICE_AGENT:]Reply sent..");
                }
            } catch (CartagoException ex) {
                ex.printStackTrace();
            }
        }
    }
}
