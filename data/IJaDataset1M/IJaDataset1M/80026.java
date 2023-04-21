package com.sshtools.j2ssh.session;

/**
 *
 *
 * @author $author$
 * @version $Revision: 1.12 $
 */
public interface SignalListener {

    /**
     *
     *
     * @param signal
     */
    public void onSignal(String signal);

    /**
     *
     *
     * @param signal
     * @param coredump
     * @param message
     */
    public void onExitSignal(String signal, boolean coredump, String message);
}
