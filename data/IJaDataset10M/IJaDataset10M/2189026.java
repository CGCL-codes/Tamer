package netgest.bo.runtime3;

import java.util.Iterator;
import java.util.List;
import netgest.bo.boException;
import netgest.bo.data.DataSet;
import netgest.bo.def.boDefHandler;
import netgest.bo.runtime.EboContext;
import netgest.bo.runtime.boObject;
import netgest.bo.runtime.boRuntimeException;

public class XEOManager {

    private XEOCache cacheManager;

    public XEOManager() {
        init();
    }

    private void init() {
    }

    public DataSet list(EboContext ctx, boDefHandler baseObjectDef, String nativeQuery, List<Object> nativeQueryArgs, int page, int pageSize) {
        String className;
        StringBuilder queryHash;
        int queryHashCode;
        className = baseObjectDef.getName();
        queryHash = new StringBuilder();
        queryHash.append(nativeQuery);
        if (nativeQueryArgs != null && nativeQueryArgs.size() > 0) {
            Iterator<Object> it = nativeQueryArgs.iterator();
            while (it.hasNext()) {
                queryHash.append('{').append(it.next()).append('}');
            }
        }
        queryHashCode = queryHash.toString().hashCode();
        DataSet retDataSet = cacheManager.getList(className, queryHashCode);
        if (retDataSet == null) {
            retDataSet = netgest.bo.data.ObjectDataManager.executeNativeQuery(ctx, baseObjectDef, (String) nativeQuery, page, pageSize, nativeQueryArgs, false);
            cacheManager.putList(className, queryHashCode, retDataSet);
        }
        return retDataSet;
    }

    public boObject loadObject(EboContext ctx, long boui) throws boRuntimeException {
        DataSet objectData;
        String className;
        className = getXEOClassName(ctx, boui);
        objectData = cacheManager.getObject(className, boui);
        boObject retObject = getObject(ctx, className);
        retObject.setEboContext(ctx);
        if (objectData != null) {
            retObject.load(objectData);
        } else {
            retObject.load(boui);
        }
        return retObject;
    }

    @SuppressWarnings("unchecked")
    private boObject getObject(EboContext ctx, String name) throws boRuntimeException {
        try {
            boDefHandler bodef = boDefHandler.getBoDefinition(name);
            if (bodef == null) {
                throw new boRuntimeException(this.getClass().getName() + ".getObject(...)", "BO-3019", null, name);
            }
            String version = "v" + bodef.getBoVersion().replace('.', '_');
            name = version + "." + name;
            Class xclass = Class.forName(name, true, ctx.getApplication().getClassLoader());
            boObject retobj = (boObject) xclass.newInstance();
            return retobj;
        } catch (ClassNotFoundException e) {
            throw new boException("netgest.bo.runtime.boObjectLoader.loadObject(String)", "BO-2101", e, name);
        } catch (IllegalAccessException e) {
            throw new boException("netgest.bo.runtime.boObjectLoader.loadObject(String)", "BO-2101", e, name);
        } catch (InstantiationException e) {
            throw new boException("netgest.bo.runtime.boObjectLoader.loadObject(String)", "BO-2101", e, name);
        }
    }

    private String getXEOClassName(EboContext ctx, long boui) throws boRuntimeException {
        return null;
    }
}
