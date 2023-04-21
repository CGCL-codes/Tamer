package org.alicebot.server.core.logging;

import java.util.HashMap;
import org.alicebot.server.core.Globals;
import org.alicebot.server.core.util.Trace;
import org.alicebot.server.core.util.XMLResourceSpec;
import org.alicebot.server.core.util.XMLWriter;

/**
 *  Provides a simplistic XML file writing facility.
 *
 *  @author Noel Bush
 *  @version 4.1.3
 */
public class XMLLog {

    /** The string &quot;UTF-8&quot; (for character encoding conversion). */
    private static final String ENC_UTF8 = "UTF-8";

    /** Keeps a count of entries made in a log file. */
    private static HashMap entryCounts = new HashMap();

    /** Chat log spec. */
    public static final XMLResourceSpec CHAT = new XMLResourceSpec();

    static {
        CHAT.description = "Chat Log";
        CHAT.path = Globals.getProperty("programd.logging.xml.chat.log-path", "./logs/chat.xml");
        CHAT.rolloverAtMax = true;
        CHAT.rolloverAtRestart = true;
        CHAT.root = "exchanges";
        CHAT.stylesheet = Globals.getProperty("programd.logging.xml.chat.stylesheet-path", "../resources/logs/view-chat.xsl");
        CHAT.encoding = Globals.getProperty("programd.logging.xml.chat.encoding", "UTF-8");
        CHAT.dtd = XMLResourceSpec.HTML_ENTITIES_DTD;
    }

    /** Limits the number of responses written to a log file before it is rolled over. */
    private static int ROLLOVER;

    static {
        try {
            ROLLOVER = Integer.parseInt(Globals.getProperty("programd.logging.xml.rollover", "2000"));
        } catch (NumberFormatException e) {
            ROLLOVER = 2000;
        }
    }

    /**
     *  Writes a message to an XML log file.  If the number of entries
     *  has exceeded {@link #ROLLOVER}, the file will be renamed and a
     *  new file created.  Note that the approach currently used has an
     *  important defect: it only counts entries during the runtime of
     *  the bot; it will not count entries in an existing log file, so
     *  if the bot is restarted many times and {@link #ROLLOVER} is rather
     *  large, actual logfiles may exceed the limit significantly.
     *
     *  @param message  the text of the log event
     *  @param spec     the log spec
     */
    public static synchronized void log(String message, XMLResourceSpec spec) {
        int entryCount;
        Object atSpec = entryCounts.get(spec);
        if (atSpec != null) {
            entryCount = ((Integer) atSpec).intValue();
            if (spec.rolloverAtMax) {
                if (++entryCount % ROLLOVER == 0) {
                    XMLWriter.rollover(spec);
                }
                entryCounts.put(spec, new Integer(entryCount));
            }
        } else if (spec.rolloverAtRestart) {
            XMLWriter.rollover(spec);
            entryCounts.put(spec, new Integer(1));
        }
        XMLWriter.write(message, spec);
    }
}
