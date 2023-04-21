package org.apache.struts.flow.sugar;

import org.mozilla.javascript.*;
import java.util.*;
import java.io.Serializable;

/**  Wrap a java.util.List for JavaScript.  */
@SuppressWarnings("unchecked")
public class ScriptableList extends JavaObjectWrapper implements Scriptable, Wrapper, Serializable {

    private static final long serialVersionUID = 8799164266164194514L;

    private List list;

    public ScriptableList() {
    }

    public ScriptableList(List list) {
        this.list = list;
    }

    public ScriptableList(Scriptable scope, Object javaObject, Class staticType, Map funcs) {
        super(scope, javaObject, staticType, funcs);
        if (javaObject instanceof List) {
            this.list = (List) javaObject;
        } else {
            throw new IllegalArgumentException("Passed object " + javaObject + " is not an instance of List");
        }
    }

    public String getClassName() {
        return staticType.toString();
    }

    public boolean has(int index, Scriptable start) {
        return index >= 0 && index < list.size();
    }

    public Object get(int index, Scriptable start) {
        return list.get(index);
    }

    public void put(int index, Scriptable start, Object value) {
        int max = index + 1;
        if (max > list.size()) {
            for (int i = list.size(); i < index; i++) {
                list.add(i, null);
            }
            list.add(index, value);
        } else {
            list.set(index, value);
        }
    }

    public void delete(int index) {
        list.remove(index);
    }

    public Object[] getIds() {
        Integer[] ids = new Integer[list.size()];
        for (int x = 0; x < ids.length; x++) {
            ids[x] = new Integer(x);
        }
        return ids;
    }

    public Object unwrap() {
        return this.list;
    }
}
