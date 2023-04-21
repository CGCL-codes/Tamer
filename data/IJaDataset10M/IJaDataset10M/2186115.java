package org.jrazdacha.model.nullmodel;

import java.util.ArrayList;
import org.jrazdacha.model.Peer;

public class NullPeers extends ArrayList<Peer> implements Nullable {

    private static NullPeers _instance;

    /**
	 * 
	 */
    private static final long serialVersionUID = -8876113013068188010L;

    public static NullPeers newNull() {
        if (_instance == null) {
            _instance = new NullPeers();
        }
        return _instance;
    }

    /**
	 * Indicates, when the object is null.
	 * 
	 * @return <code>true</code> if object is null, else return
	 *         <code>false</code>
	 */
    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public void setNull(boolean isNull) {
    }
}
