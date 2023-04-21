package jist.swans.misc;

/**
 * Timer expiration interface.
 *
 * @author Rimon Barr &lt;barr+jist@cs.cornell.edu&gt;
 * @version $Id: Timer.java,v 1.1 2006/10/21 00:04:05 lmottola Exp $
 * @since SWANS1.0
 */
public interface Timer {

    /**
   * Timer expiration processing.
   */
    void timeout();
}
