package freebrain.common;

import java.util.HashMap;
import freebrain.main.FreeMindSplash;
import freebrain.main.ISplash;
import freebrain.resource.FreemindResourceBundle;
import freebrain.resource.IResourceBundle;
import freebrain.resource.IResourceManager;
import freebrain.resource.ResourceManager;

public class InterfaceImpManager {

    private HashMap<Class<?>, Class<?>> classMap = new HashMap<Class<?>, Class<?>>();

    {
        setImplement(IResourceManager.class, ResourceManager.class);
        setImplement(ISplash.class, FreeMindSplash.class);
        setImplement(IResourceBundle.class, FreemindResourceBundle.class);
    }

    public <T extends Object> Class<?> getImplement(Class<?> inf) {
        if (inf == null || !inf.isInterface()) {
            throw new RuntimeException("parameter 'inf =[" + inf + "]' should be a interface.");
        }
        return classMap.get(inf);
    }

    public <T extends Object> void setImplement(Class<T> inf, Class<? extends T> cla) {
        if (inf == null || !inf.isInterface()) {
            throw new RuntimeException("parameter 'inf =[" + inf + "]' should be a interface.");
        }
        if (cla == null) {
            classMap.remove(inf);
        } else if (cla.isInterface()) {
            throw new RuntimeException("parameter 'cla =[" + cla + "]' could not be a interface.");
        }
        classMap.put(inf, cla);
    }

    public void clear() {
        classMap.clear();
    }
}
