package net.organizer.ui.actionhandler;

import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;

public abstract class ActionHandlerBase {

    private static final Logger logger = Logger.getLogger(ActionHandlerBase.class.getName());

    public abstract void handleAction(final HttpServletRequest req, final JSONObject respJSONObject) throws Exception;

    static Long getParamValueAsLong(final HttpServletRequest req, final String param, final boolean required) {
        String stringValue = getParamValueAsString(req, param, required);
        if (stringValue != null && stringValue.length() > 0) {
            return Long.parseLong(stringValue);
        } else {
            return null;
        }
    }

    static String getParamValueAsString(final HttpServletRequest req, final String param, final boolean required) {
        logger.finest(String.format("getParValueAsString... parameter: %s, required: %b", param, required));
        String result = req.getParameter(param);
        if (required) {
            if (result == null || result.length() == 0) {
                throw new RuntimeException("Missing required HTTP request parameter:" + param);
            }
        }
        return result;
    }
}
