package br.ufmg.lcc.arangi.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author Cesar Correia
 *
 */
public class BOCaching {

    private static Map BOCachingMap = new HashMap();

    public static void putInCache(String dtoClassName, Map dtoMap) {
        BOCachingMap.put(dtoClassName, dtoMap);
    }

    public static Map getFromCache(String dtoClassName) {
        return (Map) BOCachingMap.get(dtoClassName);
    }

    public static void removeFromCache(String dtoClassName) {
        if (BOCachingMap.containsKey(dtoClassName)) {
            BOCachingMap.remove(dtoClassName);
        }
    }

    public static Iterator iterator() {
        return BOCachingMap.keySet().iterator();
    }
}
