package org.alicebot.server.core.processor.loadtime;

import org.alicebot.server.core.Globals;
import org.alicebot.server.core.util.Trace;
import org.alicebot.server.core.parser.StartupFileParser;
import org.alicebot.server.core.parser.XMLNode;
import org.alicebot.server.core.util.Toolkit;

/**
 *  Presently supports configuration of a single bot.
 *  Later this will be expanded to handle any number of bots.
 */
public class BotProcessor extends StartupElementProcessor {

    public static final String label = "bot";

    public String process(int level, String botid, XMLNode tag, StartupFileParser parser) throws InvalidStartupElementException {
        String botID = Toolkit.getAttributeValue(ID, tag.XMLAttr);
        if (!botID.equals(EMPTY_STRING)) {
            if (Boolean.valueOf(Toolkit.getAttributeValue(ENABLED, tag.XMLAttr)).booleanValue()) {
                if (Globals.getBotID() == null) {
                    Globals.setBotID(botID);
                    Trace.devinfo("Configuring bot \"" + botID + "\".");
                    return parser.evaluate(level++, botid, tag.XMLChild);
                } else {
                    Trace.userinfo("Sorry, this version of Program D only supports one bot.");
                    Trace.userinfo("Cannot load \"" + botID + "\" right now.");
                    Trace.userinfo("Just wait for a later version!");
                    return EMPTY_STRING;
                }
            }
        }
        return EMPTY_STRING;
    }
}
