package anuo_xxz.study.velocity.context_example;

import java.util.TreeMap;
import org.apache.velocity.context.AbstractContext;
import org.apache.velocity.context.Context;

/**
 *   Example context impl that uses a TreeMap
 *
 *   Not much point other than to show how easy it is.
 *
 *   This is unsupported, example code.
 *
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: TreeMapContext.java 463298 2006-10-12 16:10:32Z henning $
 */
public class TreeMapContext extends AbstractContext {

    private TreeMap context = new TreeMap();

    public TreeMapContext() {
        super();
    }

    public TreeMapContext(Context inner) {
        super(inner);
    }

    public Object internalGet(String key) {
        return context.get(key);
    }

    public Object internalPut(String key, Object value) {
        return context.put(key, value);
    }

    public boolean internalContainsKey(Object key) {
        return context.containsKey(key);
    }

    public Object[] internalGetKeys() {
        return context.keySet().toArray();
    }

    public Object internalRemove(Object key) {
        return context.remove(key);
    }
}
