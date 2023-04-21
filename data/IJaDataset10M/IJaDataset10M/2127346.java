package org.neodatis.odb.core.layers.layer2.instance;

import java.lang.reflect.Constructor;
import java.util.Map;
import org.neodatis.odb.ODBRuntimeException;
import org.neodatis.odb.OdbConfiguration;
import org.neodatis.odb.core.NeoDatisError;
import org.neodatis.odb.core.layers.layer2.instance.IClassPool;
import org.neodatis.tool.wrappers.map.OdbHashMap;

/**
 * A simple class pool, to optimize instance creation
 * 
 * @author osmadja
 * 
 */
public class ODBClassPool implements IClassPool {

    private static Map<String, Class> classMap = new OdbHashMap<String, Class>();

    private static Map<String, Constructor> construtorsMap = new OdbHashMap<String, Constructor>();

    public void reset() {
        classMap.clear();
        construtorsMap.clear();
    }

    public synchronized Class getClass(String className) {
        Class clazz = classMap.get(className);
        if (clazz == null) {
            try {
                ClassLoader cl = OdbConfiguration.getClassLoader();
                if (cl == null) {
                    throw new ODBRuntimeException(NeoDatisError.INTERNAL_ERROR.addParameter("Class loader is null!"));
                }
                clazz = cl.loadClass(className);
            } catch (Exception e) {
                throw new ODBRuntimeException(NeoDatisError.CLASS_POOL_CREATE_CLASS.addParameter(className), e);
            }
            classMap.put(className, clazz);
        }
        return clazz;
    }

    public Constructor getConstrutor(String className) {
        return construtorsMap.get(className);
    }

    public void addConstrutor(String className, Constructor constructor) {
        construtorsMap.put(className, constructor);
    }
}
