package riverbed.jelan.lexer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A decorator class that supports debug logging for a CharSource. A decorator 
 * has been used in place of direct logging in CharSource implementations to 
 * avoid impacting the perfomance of the low level character retrieval operations
 * when debug is not required.
 */
public class DebugCharSource implements CharSource {

    /** Destination for all log output */
    private static final Log log = LogFactory.getLog(DebugCharSource.class);

    private CharSource charSource;

    public DebugCharSource(CharSource charSource) {
        this.charSource = charSource;
        if (log.isDebugEnabled()) {
            if (charSource.finished()) log.debug("Scan end"); else log.debug("Scan char '" + charSource.getChar() + "'");
        }
    }

    public void consume() {
        charSource.consume();
        if (log.isDebugEnabled()) {
            if (charSource.finished()) log.debug("Scan end"); else log.debug("Scan char '" + charSource.getChar() + "'");
        }
    }

    public int currentLocation() {
        return charSource.currentLocation();
    }

    public boolean finished() {
        return charSource.finished();
    }

    public char getChar() {
        return charSource.getChar();
    }

    public String getSaved() {
        return charSource.getSaved();
    }

    public boolean hasSaved() {
        return charSource.hasSaved();
    }

    public boolean isSaving() {
        return charSource.isSaving();
    }

    public void startSaving() {
        charSource.startSaving();
    }

    public void stopSaving() {
        charSource.stopSaving();
    }
}
