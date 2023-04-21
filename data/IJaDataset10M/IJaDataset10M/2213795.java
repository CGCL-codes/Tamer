package com.sshtools.daemon.terminal;

/**
 *
 *
 * @author $author$
 * @version $Revision: 1.11 $
 */
public class ansi extends BasicTerminal {

    /**
 *
 *
 * @return
 */
    public boolean supportsSGR() {
        return true;
    }

    public boolean supportsScrolling() {
        return true;
    }

    public String getName() {
        return "ansi";
    }
}
