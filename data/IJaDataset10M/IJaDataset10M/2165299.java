package bt747.sys.interfaces;

/**
 * @author Mario De Weerd
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public interface BT747Thread {

    void run();

    /** Called just before the thread is started. */
    void started();

    /** Called just after the thread is stopped. */
    void stopped();
}
