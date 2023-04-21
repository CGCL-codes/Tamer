package net.aditsu.depeche.objects;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import net.aditsu.depeche.FKey;
import net.aditsu.depeche.Query;
import net.aditsu.depeche.Record;

public class RecordHandler implements InvocationHandler {

    private final Manager m;

    private final Record rec;

    private final EntityInfo<?> einfo;

    private final String name;

    private Map<String, Record> parents;

    public RecordHandler(final Manager m, final Record rec, final Map<String, Record> parents, final Class<? extends Entity> cl) {
        this.m = m;
        this.rec = rec;
        this.parents = parents;
        einfo = m.getEInfo(cl);
        name = cl.getSimpleName();
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        final MethodInfo mi = einfo.getInfo(method);
        if (mi == null) {
            if (args == null) {
                final String mname = method.getName();
                if ("toString".equals(mname)) {
                    return name + rec.getPKey();
                } else if ("getDepecheRecord".equals(mname)) {
                    return rec;
                } else if ("getPrimaryKey".equals(mname)) {
                    return rec.getPKey();
                } else if ("save".equals(mname)) {
                    rec.save();
                    return null;
                } else if ("getEntityType".equals(mname)) {
                    return einfo.getEntityType();
                }
            }
            throw new RuntimeException("Unexpected method: " + method);
        }
        switch(mi.getPType()) {
            case PLAIN:
                final String s = mi.getColumn();
                if (mi.isSetter()) {
                    rec.setValue(s, (Serializable) args[0]);
                    return null;
                } else {
                    return rec.getValue(s);
                }
            case PARENT:
                final FKey fk = mi.getFKey();
                final String field = mi.getProperty();
                if (mi.isSetter()) {
                    final Record r = args[0] == null ? null : ((Entity) args[0]).getDepecheRecord();
                    rec.setParent(fk, r);
                    if (parents == null) {
                        parents = new HashMap<String, Record>();
                    }
                    parents.put(field, r);
                    return null;
                } else {
                    if (parents == null) {
                        parents = new HashMap<String, Record>();
                    } else if (parents.containsKey(field)) {
                        return m.get(parents.get(field));
                    }
                    final EntityInfo<?> ei = m.getEInfo(fk.pkTable);
                    final Record r = rec.getParent(fk).fields(ei.getColumnInfo().getColumnNames()).getRecord();
                    parents.put(field, r);
                    return m.get(r);
                }
            case CHILD:
                final FKey xk = mi.getFKey();
                if (mi.isSetter()) {
                    throw new RuntimeException("Invalid method");
                } else {
                    Query q = rec.getChildren(xk);
                    final RawFilter f = method.getAnnotation(RawFilter.class);
                    if (f != null) {
                        q = q.rawFilter(f.value());
                    }
                    final RawOrder o = method.getAnnotation(RawOrder.class);
                    if (f != null) {
                        q = q.rawOrder(o.value());
                    }
                    return m.find(q);
                }
            default:
                throw new RuntimeException(mi.getPType() + " not handled");
        }
    }
}
