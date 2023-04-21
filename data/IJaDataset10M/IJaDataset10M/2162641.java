package scamsoft.squadleader.rules;

import java.io.Serializable;

/**
 * A source of unit IDs.
 * IDs start at 1 and are incremented each time an id is fetched.
 * User: amross
 * Date: 21-Jan-2004
 * Time: 17:09:06
 */
public class UnitIDFactory implements Serializable {

    private int currentID;

    /**
     * Create a new UnitIDFactory. IDs start at 0.
     */
    public UnitIDFactory() {
        this(0);
    }

    /**
     * Create a UnitIDFactory whos Ids start at the given ID.
     * @param currentID
     */
    public UnitIDFactory(int currentID) {
        this.currentID = currentID;
    }

    /**
     * Return the next unique unit ID.
     *
     * @return
     */
    public int getNextID() {
        return currentID++;
    }

    static final long serialVersionUID = -8394064985825921363L;
}
