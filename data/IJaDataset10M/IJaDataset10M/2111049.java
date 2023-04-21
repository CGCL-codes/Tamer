package cwsexamples.stockquote.java;

import org.apache.axiom.om.OMElement;
import cwsexamples.stockquote.utils.ExampleParam;
import cartago.ArtifactId;
import cartago.CartagoException;
import cartagows.WSMsgInfo;
import cartagows.util.WSAgent;
import cartagows.util.XMLib;

public class StockQuoteConsumerAgent extends WSAgent {

    public StockQuoteConsumerAgent(String agentName) throws CartagoException {
        super(agentName);
    }

    public StockQuoteConsumerAgent(String agentName, String workspaceName, String workspaceHost) throws CartagoException {
        super(agentName, workspaceName, workspaceHost);
    }

    public void run() {
        try {
            log("HELLO FROM THE USER!");
            ArtifactId wsProxy;
            wsProxy = createWSInterface("proxyConsumer", ExampleParam.STOCKQUOTE_WSDL_URI);
            String quoteName = "ACME";
            String msgBody = "<tns:TradePriceRequest xmlns:tns=\"http://example.com/stockquote.wsdl\">" + "  <tns:tickerSymbol>" + quoteName + "</tns:tickerSymbol>" + "</tns:TradePriceRequest>";
            while (true) {
                log("[CONSUMER:] doing GetLastTradePrice request for quote: " + quoteName);
                WSMsgInfo resMsg = doRequestResponse(wsProxy, "GetLastTradePrice", msgBody);
                if (resMsg == null) log("resMsg is null");
                OMElement el = XMLib.getInstance().buildElementFromString(resMsg.getBody());
                log("[CONSUMER:] quote value: " + XMLib.getInstance().getElementValue("TradePrice", "price", el));
                Thread.sleep(1000);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
