package pl.xperios.rdk.server.rpcservices;

import java.util.Date;
import pl.xperios.rdk.client.rpcservices.LoggingRpcService;
import pl.xperios.rdk.shared.Constans;
import pl.xperios.rdk.shared.XLog;

/**
 *
 * @author Praca
 */
public class LoggingRpcServiceImpl implements LoggingRpcService {

    /**
     *
     * @param message
     * @param logLevel
     * @param date
     * @param isClient
     * @return
     */
    public Boolean sendMessage(String message, int logLevel, Date date, boolean isClient) {
        XLog.logMessage("[CLIENT " + getFormattedTime(date, Constans.isProduction) + "]" + message, logLevel, isClient);
        return true;
    }

    /**
     * 
     * @param date
     * @param extended
     * @return
     */
    public static String getFormattedTime(Date date, boolean extended) {
        return (extended ? date.getYear() + 1900 + "-" + (date.getMonth() + 1) + "-" + (date.getMinutes() + 1) + " " : "") + date.getHours() + ":" + date.getMinutes() + ":" + (date.getSeconds() < 10 ? "0" : "") + date.getSeconds();
    }
}
