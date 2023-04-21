package org.sinaxe.runtime;

import org.sinaxe.runtime.RuntimeComponent;
import org.sinaxe.*;
import java.util.ArrayList;
import java.util.Vector;

public class InPortImpl extends SinaxePortBase implements SinaxeInPort, SinaxeEventDestination {

    private ArrayList listeners = new ArrayList();

    public InPortImpl(String name, int datatype, RuntimeComponent parent) {
        super(name, datatype, parent);
    }

    public void matchInType(int datatype) throws SinaxeException {
        try {
            matchDataTypes(datatype, getDataType());
        } catch (SinaxeException e) {
            throw new SinaxeException("'" + getParent().getFullName() + "." + getName() + "'", e);
        }
    }

    public int getDirection() {
        return IN_DIR;
    }

    public void addListener(SinaxePortListener listener) {
        listeners.add(listener);
    }

    public void removeListener(SinaxePortListener listener) {
        listeners.remove(listener);
    }

    public void sendEvent(SinaxeEvent event) {
        if (SinaxeDebugger.level(1)) {
            try {
                matchDataType(event.getData());
            } catch (SinaxeException e) {
                Object data = event.getData();
                String errorStr = "port '" + getParent().getFullName() + '.' + getName() + "' recieved invalid data type!";
                SinaxeErrorHandler.error(errorStr, e);
                return;
            }
        }
        if (SinaxeDebugger.level(2)) {
            event.addPath(getParent().getFullName() + '.' + getName());
        }
        for (int i = 0; i < listeners.size(); i++) {
            try {
                ((SinaxePortListener) listeners.get(i)).sinaxePortEvent(this, event.getData());
            } catch (Exception e) {
                SinaxeErrorHandler.error("while processing '" + getName() + "' event for component '" + getParent().getFullName() + "'!!!", e);
            }
        }
        if (!listeners.isEmpty()) event.recieved();
    }
}
