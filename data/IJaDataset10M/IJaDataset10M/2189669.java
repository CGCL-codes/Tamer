package streemaplib.size;

import streemaplib.MapItem;

/**
 * @author Ricardo
 * @version 1.0
 * @created 20-Abr-2010 15:06:24
 */
public abstract class STNodeSize {

    public STNodeSize() {
    }

    public void finalize() throws Throwable {
    }

    /**
	 * 
	 * @param node
	 */
    public long getSize(MapItem node) {
        return 0;
    }
}
