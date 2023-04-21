package net.turingcomplete.phosphor.client;

import net.turingcomplete.phosphor.shared.*;

/**
 * Used to give info to the creating thread about the file transfer
 * <p>
 * REVISION HISTORY:
 * <p>
 *
 * @version 1.0, March 29, 2001
 * @see     ####
 * @since   1.0
 */
public interface XFerCallback {

    public void xferStarted(LocalFileDescription file);

    public void xferInterrupted(LocalFileDescription file);

    public void xferCompleted(LocalFileDescription file);

    public void xferTimedout(LocalFileDescription file);
}
