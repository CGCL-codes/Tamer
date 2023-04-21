package com.tictactec.ta.lib.meta;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.RetCode;

/**
 * @deprecated in favor of CoreMetaData class
 */
public class CoreMetaInfo {

    static final Class coreClass = Core.class;

    static final String LOOKBACK_SUFFIX = "_Lookback";

    static final String INT_PREFIX = "INT_";

    Map<TaFuncSignature, TaFuncMetaInfo> taFuncMap;

    public CoreMetaInfo() {
        this.taFuncMap = getTaFuncMetaInfoMap();
    }

    protected Map<String, Method> getLookbackMethodMap() {
        Map<String, Method> map = new HashMap<String, Method>();
        Method[] ms = coreClass.getDeclaredMethods();
        for (Method m : ms) {
            if (m.getName().endsWith(LOOKBACK_SUFFIX)) {
                map.put(m.getName(), m);
            }
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    protected Map<TaFuncSignature, TaFuncMetaInfo> getTaFuncMetaInfoMap() {
        Map<TaFuncSignature, TaFuncMetaInfo> result = new TreeMap<TaFuncSignature, TaFuncMetaInfo>();
        Method[] ms = coreClass.getDeclaredMethods();
        Map<String, Method> lookbackMap = getLookbackMethodMap();
        for (Method taMethod : ms) {
            String fn = taMethod.getName();
            if (taMethod.getReturnType().equals(RetCode.class) && !fn.startsWith(INT_PREFIX)) {
                String lookbackName = fn + LOOKBACK_SUFFIX;
                Method lookbackMethod = lookbackMap.get(lookbackName);
                if (lookbackMethod != null) {
                    TaFuncMetaInfo mi = new TaFuncMetaInfo(fn, taMethod, lookbackMethod);
                    result.put(mi, mi);
                }
            }
        }
        return result;
    }

    public Collection<TaFuncMetaInfo> getAllFuncs() {
        return taFuncMap.values();
    }

    public TaFuncMetaInfo get(String taName, Class[] inVarTypes) {
        return taFuncMap.get(new TaFuncSignature(taName, inVarTypes));
    }

    public void forEach(TaFuncClosure closure) throws Exception {
        for (TaFuncMetaInfo mi : getAllFuncs()) {
            closure.execute(mi);
        }
    }

    public static void main(String[] args) {
        CoreMetaInfo mi = new CoreMetaInfo();
        Collection<TaFuncMetaInfo> fs = mi.getAllFuncs();
        int i = 0;
        for (TaFuncMetaInfo f : fs) {
            System.out.println(" " + (i++) + " " + f);
        }
    }
}
