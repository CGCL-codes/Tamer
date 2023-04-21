package applications.rfid.ambientLibrary.main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class HandlerManager {

    private HashMap<Object, Vector<JavaHandler>> enabledHandlers_;

    public HandlerManager() {
        enabledHandlers_ = new HashMap<Object, Vector<JavaHandler>>();
    }

    public void addHandler(Object target, JavaHandler handler) {
        Vector<JavaHandler> handlers = enabledHandlers_.get(target);
        if (handlers == null) {
            handlers = new Vector<JavaHandler>();
        }
        handlers.add(handler);
    }

    public void removeHandler(Object target, JavaHandler handler) {
        Vector<JavaHandler> handlers = enabledHandlers_.get(target);
        if (handlers != null) {
            handlers.remove(handler);
        }
    }

    public void clearHandlers(Object target) {
        Vector<JavaHandler> handlers = enabledHandlers_.get(target);
        if (handlers != null) {
            JavaHandler current;
            for (Iterator<JavaHandler> i = handlers.iterator(); i.hasNext(); ) {
                current = i.next();
                current.cancel();
            }
            handlers.clear();
            enabledHandlers_.remove(target);
        }
    }

    public interface JavaHandler {

        public void cancel();
    }
}
