package org.ourgrid.common.command;

import static org.ourgrid.common.interfaces.Constants.LINE_SEPARATOR;
import org.ourgrid.aggregator.ui.sync.AggregatorUIMessages;
import org.ourgrid.discoveryservice.ui.sync.DiscoveryServiceUIMessages;
import org.ourgrid.peer.ui.sync.PeerUIMessages;
import org.ourgrid.reqtrace.Req;
import org.ourgrid.worker.ui.sync.WorkerUIMessages;

/**
 * Class that maintains the UI error messages.
 * 
 * @see MyGridUIMessages
 * @see PeerUIMessages
 * @see WorkerUIMessages
 * @see DiscoveryServiceUIMessages
 * @see AggregatorUIMessages
 * @see WebStatusUIMessages
 */
public class UIMessages {

    public static final String INVALID_PARAMETERS_MSG = "Invalid command parameters";

    public static final String INVALID_LOGIN_FORMAT_MSG = "Type user@server";

    public static final String ALREADY_RUNNING_MSG = "Component is already running";

    public static final String NOT_RUNNING_MSG = "Component is not running";

    @Req({ "REQ001", "REQ003" })
    public static String getErrorMessage(Exception e, String component, String commandName) {
        return "Could not execute command '" + component + " " + commandName + "'" + LINE_SEPARATOR + "Cause: " + e.getMessage() + LINE_SEPARATOR;
    }

    public static String getSuccessMessage(String componentName, String commandName) {
        return "Command '" + componentName + " " + commandName + "' was successfully executed";
    }

    public static String getLauchingMessage(String componentName, String commandName) {
        return "Launching command '" + componentName + " " + commandName + "'. Please wait...";
    }

    /**
	 * Builds a String with many line separators
	 * 
	 * @param amount minimum considered is one
	 * @return a String with a least one line separator
	 */
    public static String getLineSeparators(int amount) {
        StringBuilder separators = new StringBuilder().append(LINE_SEPARATOR);
        for (int i = 0; i < amount - 1; i++) {
            separators.append(LINE_SEPARATOR);
        }
        return separators.toString();
    }
}
