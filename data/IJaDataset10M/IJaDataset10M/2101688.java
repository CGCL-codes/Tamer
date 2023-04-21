package ircam.jmax.guiobj;

import java.io.*;
import java.util.*;
import ircam.jmax.*;
import ircam.jmax.fts.*;
import ircam.fts.client.*;

/**
 * Proxy of an Outlet FTS object.
 */
public class FtsOutletObject extends FtsGraphicObject {

    /**
   * Create a FtsOutletObject object.
   */
    public FtsOutletObject(FtsServer server, FtsObject parent, int id, String className, FtsAtom[] args, int offset, int length) {
        super(server, parent, id, className, args[offset].stringValue);
        ninlets = 1;
        noutlets = 0;
    }

    /** Get the number of outlets of the object 
    Overwrite the FtsObject method because inlets
    do not believe to FTS, when it say outlets have
    outlets.
    */
    public int getNumberOfOutlets() {
        return 0;
    }
}
