package clubmixer.commons.plugins.communication;

import java.lang.reflect.Method;

/**
 *
 * @author alex
 */
public class RemoteMethodEntry {

    private Object ressource;

    private Method method;

    public RemoteMethodEntry() {
    }

    public RemoteMethodEntry(Object ressource, Method method) {
        this.ressource = ressource;
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getRessource() {
        return ressource;
    }

    public void setRessource(Object ressource) {
        this.ressource = ressource;
    }
}
