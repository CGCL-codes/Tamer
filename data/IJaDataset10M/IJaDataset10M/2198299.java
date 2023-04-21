package de.eqc.srcds.handlers;

import java.io.IOException;
import com.sun.net.httpserver.HttpExchange;
import de.eqc.srcds.configuration.exceptions.ConfigurationException;
import de.eqc.srcds.xmlbeans.enums.ResponseCode;
import de.eqc.srcds.xmlbeans.impl.ControllerResponse;
import de.eqc.srcds.xmlbeans.impl.Message;

public class SetConfigurationValueHandler extends AbstractRegisteredHandler implements RegisteredHandler {

    @Override
    public String getPath() {
        return "/setConfig";
    }

    public void handleRequest(final HttpExchange httpExchange) throws IOException {
        final String key = getParameter("key");
        final String value = getParameter("value");
        ResponseCode code = ResponseCode.INFORMATION;
        final Message message = new Message();
        if (key == null || value == null) {
            code = ResponseCode.ERROR;
            message.addLine("Either key or value parameter is missing");
        } else {
            try {
                getConfig().setValue(key, value);
                message.addLine(String.format("Set key %s to value %s", key, value));
            } catch (ConfigurationException e) {
                code = ResponseCode.ERROR;
                message.addLine(e.getLocalizedMessage());
            }
        }
        outputXmlContent(new ControllerResponse(code, message).toXml());
    }
}
