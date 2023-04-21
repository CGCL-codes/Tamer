package perfectjpattern.core.api.behavioral.observer.data;

/**
 * Null Object Pattern implementation of <code>IEventData</code>. 
 * Prevents different <code>ISubject</code> implementations from pushing 
 * <code>null</code> into the <code>IObserver</code>'s 
 * <code>update</code> method.
 * <br><br>
 * <code>NullEventData</code> is a Singleton therefore it can not be directly 
 * instantiated, neither it may be extended.
 * <br><br>
 * 
 * @author <a href="mailto:bravegag@hotmail.com">Giovanni Azua</a>
 * @version $Revision: 1.0 $ $Date: Jun 19, 2007 10:17:38 PM $
 */
public final class NullEventData implements IEventData {

    /**
     * Returns Singleton instance of <code>NullEventData</code>.
     * 
     * @return Singleton instance of <code>NullEventData</code>.
     */
    public static NullEventData getInstance() {
        return INSTANCE;
    }

    private NullEventData() {
    }

    /**
     * Singleton instance of <code>NullEventData</code>
     */
    private static final NullEventData INSTANCE = new NullEventData();
}
