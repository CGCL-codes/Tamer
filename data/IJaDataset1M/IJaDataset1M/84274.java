package org.dozer.classmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dozer.BeanFactory;
import org.dozer.util.MappingUtils;

/**
 * Internal class that determines the appropriate class mapping to be used for
 * the source and destination object being mapped. Only intended for internal
 * use.
 * 
 * GECKO: Forked class to implement concrete/interface mapping. The find method has
 * been modified to search mapping only with srcClass and destClass that are assignable
 * to the classMap arguments.
 * <b>Please keep the format to be comparable with the original Dozer class.</b>
 * 
 * @author tierney.matt
 * @author garsombke.franz
 */
public class ClassMappings {

    private static final Log log = LogFactory.getLog(ClassMappings.class);

    private Map<String, ClassMap> classMappings = new ConcurrentHashMap<String, ClassMap>();

    private ClassMapKeyFactory keyFactory;

    public ClassMappings() {
        keyFactory = new ClassMapKeyFactory();
    }

    public void add(Class<?> srcClass, Class<?> destClass, ClassMap classMap) {
        classMappings.put(keyFactory.createKey(srcClass, destClass), classMap);
    }

    public void add(Class<?> srcClass, Class<?> destClass, String mapId, ClassMap classMap) {
        classMappings.put(keyFactory.createKey(srcClass, destClass, mapId), classMap);
    }

    public void addAll(ClassMappings classMappings) {
        this.classMappings.putAll(classMappings.getAll());
    }

    public Map<String, ClassMap> getAll() {
        return classMappings;
    }

    public long size() {
        return classMappings.size();
    }

    public ClassMap find(Class<?> srcClass, Class<?> destClass) {
        return classMappings.get(keyFactory.createKey(srcClass, destClass));
    }

    public boolean contains(Class<?> srcClass, Class<?> destClass, String mapId) {
        String key = keyFactory.createKey(srcClass, destClass, mapId);
        return classMappings.containsKey(key);
    }

    public ClassMap find(Class<?> srcClass, Class<?> destClass, String mapId) {
        ClassMap mapping = classMappings.get(keyFactory.createKey(srcClass, destClass, mapId));
        if (mapping == null) {
            mapping = findInterfaceMapping(destClass, srcClass, mapId);
        }
        if (mapping == null && destClass.isInterface() && !destClass.isAssignableFrom(BeanFactory.class)) {
            for (Entry<String, ClassMap> entry : classMappings.entrySet()) {
                ClassMap classMap = entry.getValue();
                if (srcClass.isAssignableFrom(classMap.getSrcClassToMap()) && destClass.isAssignableFrom(classMap.getDestClassToMap())) {
                    return classMap;
                }
            }
        }
        if (mapId != null && mapping == null) {
            for (Entry<String, ClassMap> entry : classMappings.entrySet()) {
                ClassMap classMap = entry.getValue();
                if (StringUtils.equals(classMap.getMapId(), mapId) && classMap.getSrcClassToMap().isAssignableFrom(srcClass) && classMap.getDestClassToMap().isAssignableFrom(destClass)) {
                    return classMap;
                } else if (StringUtils.equals(classMap.getMapId(), mapId) && srcClass.equals(destClass)) {
                    return classMap;
                }
            }
            log.info("No ClassMap found for mapId:" + mapId);
        }
        return mapping;
    }

    public List<ClassMap> findInterfaceMappings(Class<?> srcClass, Class<?> destClass) {
        Class<?>[] srcInterfaces = srcClass.getInterfaces();
        Class<?>[] destInterfaces = destClass.getInterfaces();
        List<ClassMap> interfaceMaps = new ArrayList<ClassMap>();
        int size = destInterfaces.length;
        for (int i = 0; i < size; i++) {
            ClassMap interfaceClassMap = classMappings.get(keyFactory.createKey(srcClass, destInterfaces[i]));
            if (interfaceClassMap != null) {
                interfaceMaps.add(interfaceClassMap);
            }
        }
        for (Class<?> srcInterface : srcInterfaces) {
            ClassMap interfaceClassMap = classMappings.get(keyFactory.createKey(srcInterface, destClass));
            if (interfaceClassMap != null) {
                interfaceMaps.add(interfaceClassMap);
            }
        }
        Collections.reverse(interfaceMaps);
        return interfaceMaps;
    }

    private ClassMap findInterfaceMapping(Class<?> destClass, Class<?> srcClass, String mapId) {
        Object[] keys = classMappings.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            ClassMap map = classMappings.get(keys[i]);
            Class<?> mappingDestClass = map.getDestClassToMap();
            Class<?> mappingSrcClass = map.getSrcClassToMap();
            if ((mapId == null && map.getMapId() != null) || (mapId != null && !mapId.equals(map.getMapId()))) {
                continue;
            }
            if (mappingSrcClass.isInterface() && mappingSrcClass.isAssignableFrom(srcClass)) {
                if (mappingDestClass.isInterface() && mappingDestClass.isAssignableFrom(destClass)) {
                    return map;
                } else if (destClass.equals(mappingDestClass)) {
                    return map;
                }
            }
            if (mappingDestClass.isInterface() && mappingDestClass.isAssignableFrom(destClass)) {
                if (srcClass.equals(mappingSrcClass)) {
                    return map;
                }
            }
        }
        return null;
    }
}
