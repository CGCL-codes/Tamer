package org.milligan.eccles.tags;

import org.milligan.eccles.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Arrays;
import java.util.Map;

/**
 * Loop over items in a collection
 * @author Ian Tomey
 *
 */
public class ForEachTag extends Tag {

    protected static final String PROP_ITERATOR = "iterator";

    public ForEachTag() {
    }

    private String property = "item";

    private String collection;

    private String index = "forEachIndex";

    public String getTagName() {
        return "for-each";
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getProperty() {
        return property;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getCollection() {
        return collection;
    }

    /**
	 *
	 * @param state
	 * @return
	 * @throws EcclesException
	 */
    public EcclesReturnValue doStartTag(RunState state) throws org.milligan.eccles.EcclesException {
        Object coll = state.evaluate(collection, Object.class);
        if (coll == null) return SKIP_CHILDREN;
        Iterator iterator = null;
        if (coll.getClass().isArray()) iterator = Arrays.asList((Object[]) coll).iterator(); else if (coll instanceof Collection) iterator = ((Collection) coll).iterator(); else if (coll instanceof Iterator) iterator = (Iterator) coll; else if (coll instanceof Map) iterator = ((Map) coll).entrySet().iterator(); else throw new EcclesException(state, "Object from " + collection + " is not a recognised type for iteration. type is " + coll.getClass());
        state.setPrivateProperty(PROP_ITERATOR, iterator);
        state.setProperty(property, iterator.next());
        if (index != null) state.setProperty(index, new Integer(0));
        return PROCESS_CHILDREN;
    }

    /**
	 *
	 * @param state
	 * @return
	 * @throws EcclesException
	 */
    public EcclesReturnValue doAfterChildren(RunState state) throws org.milligan.eccles.EcclesException {
        Iterator iterator = (Iterator) state.getPrivateProperty(PROP_ITERATOR);
        if (iterator == null) throw new EcclesException(state, "Iterator is null");
        if (!iterator.hasNext()) return SKIP_CHILDREN;
        state.setProperty(property, iterator.next());
        if (index != null) {
            Integer indexVal = (Integer) state.getProperty(index, false);
            state.setProperty(index, new Integer(indexVal.intValue() + 1));
        }
        return PROCESS_CHILDREN;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getIndex() {
        return index;
    }
}
