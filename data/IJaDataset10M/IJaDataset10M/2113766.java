package pool.basicReport;

import java.util.Vector;

/**
 * @author 
 *
 */
public interface Reports {

    public abstract void initialise(Vector<Object> oldPropertyList, Vector<Object> newPropertyList, String process);

    public abstract void process();

    public abstract void run();
}
