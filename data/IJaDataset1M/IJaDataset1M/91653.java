package gnu.io;

/**
 * @author Trent Jarvi
 * @version %I%, %G%
 * @since JDK1.0
 */
public interface CommDriver {

    public abstract CommPort getCommPort(String portName, int portType);

    public abstract void initialize();
}
