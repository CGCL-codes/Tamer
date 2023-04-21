package slotimaker.SipgateSMSClient.backend;

import java.text.NumberFormat;
import java.util.Map;
import java.util.Vector;
import slotimaker.SipgateSMSClient.core.Core;

/**
 * This class gets the credit from Sipgate
 */
public class MoneyGet implements Runnable {

    private static boolean isRunning = false;

    public void run() {
        if (isRunning) return;
        isRunning = true;
        Core core = Core.getInstance();
        core.setStatus(core.getString("updateLabel"));
        if (!WebServiceConnector.waitForConnection()) {
            core.showMessage(1, core.getString("error"), core.getString("errorWebserviceCallTimeout"));
            core.setStatus("");
            core.setMoney("--,--");
            isRunning = false;
            return;
        }
        WebServiceConnector connector = WebServiceConnector.getInstance();
        Vector params = new Vector();
        Map result = null;
        try {
            result = (Map) connector.getClient().execute("samurai.BalanceGet", params);
        } catch (Exception e) {
            core.showMessage(1, core.getString("error"), e.toString());
            core.setStatus("");
            core.setMoney("--,--");
            isRunning = false;
            return;
        }
        if (result != null) {
            if (result.get("StatusCode").toString().trim().equalsIgnoreCase("200")) {
                Map currentBalance = (Map) result.get("CurrentBalance");
                if (currentBalance == null) {
                    core.showMessage(1, core.getString("error"), core.getString("errorWebserviceCall"));
                    core.setStatus("");
                    core.setMoney("--,--");
                    isRunning = false;
                    return;
                }
                Double value = 0.0;
                if (currentBalance.containsKey("TotalIncludingVat")) {
                    value = (Double) currentBalance.get("TotalIncludingVat");
                } else {
                    value = (Double) currentBalance.get("TotalExcludingVat");
                }
                NumberFormat n = NumberFormat.getInstance();
                n.setMaximumFractionDigits(2);
                n.setMinimumFractionDigits(2);
                core.setStatus("");
                core.setMoney(n.format(value) + " " + currentBalance.get("Currency"));
            } else {
                core.showMessage(1, core.getString("error"), (String) result.get("StatusString"));
                core.setStatus("");
                core.setMoney("--,--");
            }
        } else {
            core.showMessage(1, core.getString("error"), core.getString("errorWebserviceCall"));
            core.setStatus("");
            core.setMoney("--,--");
        }
        isRunning = false;
    }
}
