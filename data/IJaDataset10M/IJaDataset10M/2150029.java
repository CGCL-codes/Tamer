package ircam.jmax.guiobj;

import java.io.*;
import java.util.*;
import ircam.jmax.*;
import ircam.jmax.fts.*;
import ircam.fts.client.*;

public class FtsMonitorObject extends FtsIntValueObject {

    public FtsMonitorObject(FtsServer server, FtsObject parent, int id, String className, FtsAtom args[], int offset, int length) {
        super(server, parent, id, className, args, offset, length);
        setNumberOfInlets(2);
        setNumberOfOutlets(0);
    }
}
