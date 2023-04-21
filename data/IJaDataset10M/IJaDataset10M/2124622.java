package org.apache.myfaces.trinidad.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * =-=AEW Can this truly be "static public", or do we need a typed
 * API registered by class-loader?
 */
public class TypeRepository {

    static void registerType(String componentFamily, String rendererType, FacesBean.Type type) {
        String renderKitId = "org.apache.myfaces.trinidad.core";
        synchronized (_repos) {
            Map<String, Map<String, FacesBean.Type>> rkMap = _repos.get(renderKitId);
            if (rkMap == null) {
                rkMap = new HashMap<String, Map<String, FacesBean.Type>>();
                _repos.put(renderKitId, rkMap);
            }
            Map<String, FacesBean.Type> familyMap = rkMap.get(componentFamily);
            if (familyMap == null) {
                familyMap = new HashMap<String, FacesBean.Type>();
                rkMap.put(componentFamily, familyMap);
            }
            familyMap.put(rendererType, type);
        }
    }

    public static FacesBean.Type getType(String componentFamily, String rendererType) {
        String renderKitId = "org.apache.myfaces.trinidad.core";
        synchronized (_repos) {
            Map<String, Map<String, FacesBean.Type>> rkMap = _repos.get(renderKitId);
            if (rkMap == null) return null;
            Map<String, FacesBean.Type> familyMap = rkMap.get(componentFamily);
            if (familyMap == null) return null;
            return familyMap.get(rendererType);
        }
    }

    private static Map<String, Map<String, Map<String, FacesBean.Type>>> _repos = new HashMap<String, Map<String, Map<String, FacesBean.Type>>>();
}
