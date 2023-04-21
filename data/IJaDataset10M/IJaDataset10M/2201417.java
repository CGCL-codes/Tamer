package org.brandao.brutos.scope;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.brandao.brutos.BrutosContext;
import org.brandao.brutos.programatic.IOCManager;

/**
 *
 * @author Afonso Brandao
 */
public class Scopes {

    private static Map<String, Scope> scopes;

    static {
        scopes = new HashMap<String, Scope>();
    }

    public Scopes() {
    }

    public static void register(String id, Scope scope) {
        scopes.put(id, scope);
    }

    public static void remove(String id) {
        scopes.remove(id);
    }

    public static Scope get(String id) {
        return scopes.get(id);
    }

    public static Map getScopes() {
        return Collections.unmodifiableMap(scopes);
    }
}
