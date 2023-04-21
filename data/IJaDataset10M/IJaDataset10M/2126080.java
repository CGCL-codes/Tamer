package jmax.fts;

import java.io.*;
import java.util.*;

/**
 * Proxy of an Outlet FTS object.
 */
public class FtsOutletObject extends FtsInOutletObject {

    /*****************************************************************************/
    int position;

    /**
   * Create a FtsOutletObject object.
   */
    public FtsOutletObject(Fts fts, FtsObject parent, int position, int objId) {
        super(fts, parent, "outlet", position, objId);
    }

    /** Set the position. Tell the server about the new position */
    public void setPosition(int i) {
        super.setPosition(i);
        fts.getServer().repositionOutletObject(this, i);
    }
}
