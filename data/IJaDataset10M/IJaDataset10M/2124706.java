package grammarscope.dependency.properties;

import java.util.EventListener;

/**
 * Pick listener interface
 * 
 * @author Bernard Bou
 */
public interface PickListener extends EventListener {

    /**
	 * Pick event listener
	 * 
	 * @param thisObject
	 *            parameter
	 */
    public void picked(Object thisObject);
}
