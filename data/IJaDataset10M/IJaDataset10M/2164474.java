package universe.client.database;

import java.io.Serializable;
import universe.common.*;
import universe.common.database.*;
import universe.client.gui.DisplayNodeColor;

/**
 *  This is the base class for all client side database objects.  This
 *  class is used for transmission.
 *
 * @author Sean Starkey
 * @version $Id: DatabaseObjectView.java,v 1.5 2003/04/03 00:20:52 sstarkey Exp $
 */
public abstract class DatabaseObjectView implements Serializable {

    public Index index;

    public DatabaseObjectView(Index index) {
        this.index = index;
    }

    public Index getIndex() {
        return index;
    }
}
