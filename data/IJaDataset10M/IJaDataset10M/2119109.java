package transport.message;

import java.util.Properties;
import transport.InvalidMessageException;
import transport.Message;
import transport.MessageFactory;
import transport.ParserContext;

/**
 *
 * @author rem
 */
public class ServerParamMessage extends Message {

    private Properties properties;

    public ServerParamMessage(ParserContext context) {
        super("server_param");
        properties = new Properties();
        String[] parameter = null;
        while (context.next() != ')') {
            parameter = Message.nextParameter(context);
            setProperty(parameter[0], parameter[1]);
        }
        if (context.next() != ')') {
            throw new InvalidMessageException("Missing ) in message: " + context);
        }
    }

    public void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append(type);
        for (String key : properties.stringPropertyNames()) {
            buf.append(" " + key + "=" + properties.getProperty(key));
        }
        return buf.toString();
    }
}
