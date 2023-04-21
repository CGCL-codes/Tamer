package gnu.hylafax.job;

/**
 * This interface defines what a class interested in receiving receive events
 * should implement. A Listener should register for events from a Notifier.
 * 
 * @author $Author: sjardine $
 * @version $Id: ReceiveListener.java 162 2009-03-26 21:42:09Z sjardine $
 * @see gnu.hylafax.job.ReceiveNotifier
 * @see gnu.hylafax.job.ReceiveEvent
 */
public interface ReceiveListener {

    /**
     * This method is called when send Job state changes.
     */
    public void onReceiveEvent(ReceiveEvent details);
}
