package org.nakedobjects.nos.client.dnd.debug;

import org.nakedobjects.noa.adapter.Naked;
import org.nakedobjects.noa.adapter.NakedCollection;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.nof.core.util.DebugInfo;
import org.nakedobjects.nof.core.util.DebugString;
import org.nakedobjects.nof.core.util.Dump;

public class DebugAdapter implements DebugInfo {

    private final Naked object;

    public DebugAdapter(final Naked object) {
        this.object = object;
    }

    public void debugData(final DebugString debug) {
        if (object instanceof NakedObject) {
            NakedObject obj = (NakedObject) object;
            dumpObject(obj, debug);
        } else if (object instanceof NakedCollection) {
            NakedCollection collection = (NakedCollection) object;
            dumpObject(collection, debug);
        }
    }

    public String debugTitle() {
        return "Adapter";
    }

    private void dumpObject(final Naked object, final DebugString info) {
        if (object != null) {
            Dump.adapter(object, info);
        }
    }
}
