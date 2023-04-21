package net.paoding.rose.web.impl.module;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.paoding.rose.web.annotation.ReqMethod;

/**
 * 
 * @author 王志亮 [qieqie.wang@gmail.com]
 * 
 */
public class MethodRef {

    private Method method;

    private Map<String, Set<ReqMethod>> mappings = new HashMap<String, Set<ReqMethod>>();

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public void addMapping(ReqMethod reqMethod, String[] mappingPaths) {
        for (String mappingPath : mappingPaths) {
            Set<ReqMethod> mapping = mappings.get(mappingPath);
            if (mapping == null) {
                mapping = new HashSet<ReqMethod>();
                mappings.put(mappingPath, mapping);
            }
            mapping.add(reqMethod);
        }
    }

    public Map<String, Set<ReqMethod>> getMappings() {
        return mappings;
    }
}
