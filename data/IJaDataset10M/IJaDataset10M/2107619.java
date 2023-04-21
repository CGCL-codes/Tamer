package org.gameplate.event;

/**
 * Describe interface <code>MoveListener</code> here.
 *
 * @author <a href="mailto:kleiba@dfki.de">Thomas Kleinbauer</a>
 * @version 1.0
 */
public interface MoveListener {

    public void moveStarted(MoveEvent e);

    public void moveFinished(MoveEvent e);

    public void moveAborted(MoveEvent e);
}
