package telnetd.io.terminal;

/**
 * Implements a special variant which is common on
 * windows plattforms (i.e. the telnet application thats
 * coming with all of those OSes).
 *
 * @author Dieter Wimberger
 * @version 2.0 (16/07/2006)
 */
public class Windoof extends BasicTerminal {

    public boolean supportsSGR() {
        return false;
    }

    public boolean supportsScrolling() {
        return true;
    }
}
