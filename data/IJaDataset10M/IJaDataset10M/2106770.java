package gnu.io;

import java.util.*;

/**
* @author Trent Jarvi
* @version %I%, %G%
* @since JDK1.0
*/
public interface ParallelPortEventListener extends EventListener {

    public abstract void parallelEvent(ParallelPortEvent ev);
}
