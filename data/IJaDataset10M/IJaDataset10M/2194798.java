package org.mozilla.javascript.ext.corbaadapter;

import org.mozilla.javascript.*;
import java.lang.ref.Reference;

class ScriptableClient implements Scriptable {

    protected Adapter adapter;

    protected org.mozilla.javascript.ext.corbaadapter.js.Scriptable obj;

    protected String oid;

    public ScriptableClient(Adapter adapter, org.mozilla.javascript.ext.corbaadapter.js.Scriptable obj, String oid) {
        this.adapter = adapter;
        this.obj = obj;
        this.oid = oid;
        adapter.log("client create " + oid, 3);
    }

    public org.mozilla.javascript.ext.corbaadapter.js.Scriptable getObj() {
        return obj;
    }

    public void finalize() {
        adapter.log("client finalize " + oid, 3);
        adapter.cleanupClient(oid, obj);
    }

    public String getClassName() {
        return obj.getClassName();
    }

    public Object get(String name, Scriptable start) {
        adapter.log("client: get " + name, 4);
        try {
            return adapter.extractAny(obj.get(name, (org.mozilla.javascript.ext.corbaadapter.js.Scriptable) adapter.exportObj(start)));
        } catch (org.mozilla.javascript.ext.corbaadapter.js.NotFound e) {
            return Scriptable.NOT_FOUND;
        }
    }

    public Object get(int index, Scriptable start) {
        adapter.log("client: getI " + index, 4);
        try {
            return adapter.extractAny(obj.getI(index, (org.mozilla.javascript.ext.corbaadapter.js.Scriptable) adapter.exportObj(start)));
        } catch (org.mozilla.javascript.ext.corbaadapter.js.NotFound e) {
            return Scriptable.NOT_FOUND;
        }
    }

    public boolean has(String name, Scriptable start) {
        adapter.log("client: has " + name, 4);
        return obj.has(name, (org.mozilla.javascript.ext.corbaadapter.js.Scriptable) adapter.exportObj(start));
    }

    public boolean has(int index, Scriptable start) {
        adapter.log("client: hasI " + index, 4);
        return obj.hasI(index, (org.mozilla.javascript.ext.corbaadapter.js.Scriptable) adapter.exportObj(start));
    }

    public void put(String name, Scriptable start, Object value) {
        adapter.log("client: put " + name, 4);
        obj.put(name, (org.mozilla.javascript.ext.corbaadapter.js.Scriptable) adapter.exportObj(start), adapter.createAny(value));
    }

    public void put(int index, Scriptable start, Object value) {
        adapter.log("client: putI " + index, 4);
        obj.putI(index, (org.mozilla.javascript.ext.corbaadapter.js.Scriptable) adapter.exportObj(start), adapter.createAny(value));
    }

    public void delete(String name) {
        adapter.log("client: delete " + name, 4);
        obj.delete(name);
    }

    public void delete(int index) {
        adapter.log("client: deleteI " + index, 4);
        obj.deleteI(index);
    }

    public Scriptable getPrototype() {
        adapter.log("client: getPrototype", 4);
        Scriptable res = (Scriptable) adapter.importObj(obj.getPrototype());
        return (res == this) ? null : res;
    }

    public void setPrototype(Scriptable prototype) {
        adapter.log("client: setPrototype", 4);
        obj.setPrototype((org.mozilla.javascript.ext.corbaadapter.js.Scriptable) adapter.exportObj(prototype));
    }

    public Scriptable getParentScope() {
        adapter.log("client: getParentScope", 4);
        Scriptable res = (Scriptable) adapter.importObj(obj.getParentScope());
        return (res == this) ? null : res;
    }

    public void setParentScope(Scriptable parent) {
        adapter.log("client: setParentScope", 4);
        obj.setParentScope((org.mozilla.javascript.ext.corbaadapter.js.Scriptable) adapter.exportObj(parent));
    }

    public Object[] getIds() {
        adapter.log("client: getIds", 4);
        return obj.getIds();
    }

    public Object getDefaultValue(Class hint) {
        adapter.log("client: getDefaultValue", 4);
        org.mozilla.javascript.ext.corbaadapter.js.Hint h;
        if (hint == String.class) {
            h = org.mozilla.javascript.ext.corbaadapter.js.Hint.HINT_STRING;
        } else if (hint == Number.class) {
            h = org.mozilla.javascript.ext.corbaadapter.js.Hint.HINT_NUMBER;
        } else if (hint == Boolean.class) {
            h = org.mozilla.javascript.ext.corbaadapter.js.Hint.HINT_BOOLEAN;
        } else if (hint == Scriptable.class) {
            h = org.mozilla.javascript.ext.corbaadapter.js.Hint.HINT_OBJECT;
        } else {
            h = org.mozilla.javascript.ext.corbaadapter.js.Hint.HINT_NONE;
        }
        return adapter.extractAny(obj.getDefaultValue(h));
    }

    public boolean hasInstance(Scriptable instance) {
        adapter.log("client: hasInstance", 4);
        return obj.hasInstance((org.mozilla.javascript.ext.corbaadapter.js.Scriptable) adapter.exportObj(instance));
    }
}
