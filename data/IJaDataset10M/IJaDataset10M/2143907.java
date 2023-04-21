package com.sshtools.j2ssh.util;

/**
 *
 *
 * @author $author$
 * @version $Revision: 1.13 $
 */
public class OpenClosedState extends State {

    /**  */
    public static final int OPEN = 1;

    /**  */
    public static final int CLOSED = 2;

    /**
     * Creates a new OpenClosedState object.
     *
     * @param initial
     */
    public OpenClosedState(int initial) {
        super(initial);
    }

    /**
     *
     *
     * @param state
     *
     * @return
     */
    public boolean isValidState(int state) {
        return (state == OPEN) || (state == CLOSED);
    }
}
