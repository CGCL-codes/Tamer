package alice.cartago;

import java.util.*;

/**
 * Class used for managing contexts requested by Swing Agent
 * @author aricci
 *
 */
public class GUIManager {

    HashMap<String, ICartagoContext> ctxs;

    private static GUIManager instance;

    private GUIManager() {
        ctxs = new HashMap<String, ICartagoContext>();
    }

    public synchronized ICartagoContext getContext(String wspName) throws CartagoException {
        ICartagoContext ctx = ctxs.get(wspName);
        if (ctx == null) {
            ctx = CartagoService.joinWorkspace(wspName, null, null, new alice.cartago.security.UserIdCredential("__swingAgent__"));
            ctxs.put(wspName, ctx);
        }
        return ctx;
    }

    static synchronized GUIManager getInstance() {
        if (instance == null) {
            instance = new GUIManager();
        }
        return instance;
    }
}
