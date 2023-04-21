package hogs.common;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Logs messages to the given Writer. If you want to use this,
 * use a prefix specific to your package, so that we can easily
 * separate the messages. I'm also sending most of my stuff to
 * System.err, not System.out. You can still see it in a console,
 * but it's colored differently in the Eclipse console, and you
 * can separately redirect output of stdout and stderr from bash.
 * 
 * At some point, if there's demand (i.e., let me know if you want
 * me to add this), I'll add various log levels (like alert and warn
 * and error), so you can change a variable and have it ignore non-critical
 * messages. (That's another reason to use stderr instead of stdout though.)
 * 
 * @author dapachec
 */
public class Logger {

    protected Writer m_out;

    protected String m_prefix;

    /**
     * @param writer log here
     * @param prefix use the given prefix (so all log messages from this logger start with this)
     */
    public Logger(OutputStreamWriter writer, String prefix) {
        m_out = writer;
        m_prefix = prefix + ": ";
    }

    public void log(String s) {
        logMessage(s + "\n");
    }

    protected void logMessage(String s) {
        try {
            m_out.write(m_prefix + s);
            m_out.flush();
        } catch (IOException e) {
        }
    }
}
