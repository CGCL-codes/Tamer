package org.progeeks.extract.filter;

import java.util.*;
import org.progeeks.extract.*;

/**
 *  Flattens one or more maps into a single Map with
 *  a concatenation of the values.  If the same key exists
 *  in more than one of the maps than the key in the final
 *  map will be associated with a list of values.
 *
 *  <p>Optionally, this filter supports a set of mappings
 *  for further indirection.  The mapping key will be used
 *  to look up the source key value and the mapping value
 *  will be used to look up the source value value.</p>
 *
 *  <p>For example:</p>
 *  <pre>
 *  m1.put( "animal", "Elephant" );
 *  m1.put( "description", "Big animal." );
 *
 *  m2.put( "animal", "Mouse" );
 *  m2.put( "description", "Small animal." );
 *  </pre>
 *
 *  <p>Running the merge maps filter on a list of those maps
 *  with mapping set to "animal" = "description" will result in
 *  a single map with "Elephant" = "Big animal." and
 *  "Mouse" = "Small animal."</p>
 *
 *  <p>A common use case for this is when extracing name/value
 *  pairs using a regex filter.  The regex filter will generate
 *  a Map for every match with the extracted capture groups as key/value
 *  pairs.  Each capture group gets its own entry in the Map.
 *  So for these sorts of name value pairs, it is often desirable
 *  to flatten them into a single map with the key being the extracted
 *  "name" and the value being the extracted "value".</p>
 *
 *
 *  @version   $Revision: 3835 $
 *  @author    Paul Speed
 */
public class MergeMapsFilter extends AbstractFilter implements CollectionCapable {

    private Map mappings;

    private Set valueKeys;

    private boolean ignoreMissingKeys = true;

    private boolean copyUnmappedValues = true;

    public MergeMapsFilter() {
    }

    /**
     *  Merges maps by pulling the mapping keys to use
     *  as the target keys and the mapping values to use
     *  as the target values.
     *  So a mapping parm with "key" = "value" will pull
     *  m.get("key") for the key and m.get("value") for
     *  the value to put in the target map.
     */
    public MergeMapsFilter(Map mappings) {
        setMappings(mappings);
    }

    public void setMappings(Map mappings) {
        if (this.mappings == mappings) return;
        if (mappings == null) {
            this.mappings = null;
            return;
        }
        this.mappings = new HashMap(mappings);
        this.valueKeys = new HashSet(mappings.values());
    }

    public Map getMappings() {
        return (mappings);
    }

    public void setIgnoreMissingKeys(boolean f) {
        this.ignoreMissingKeys = f;
    }

    public boolean getIgnoreMissingKeys() {
        return ignoreMissingKeys;
    }

    public void setCopyUnmappedValues(boolean f) {
        this.copyUnmappedValues = f;
    }

    public boolean getCopyUnmappedValues() {
        return copyUnmappedValues;
    }

    /**
     *  A short-cut for mapping key to values in the merge
     *  that only accepts strings.  This is primarily to
     *  support direct configuation in XML.
     */
    public void set(String key, String value) {
        if (mappings == null) {
            mappings = new HashMap();
            valueKeys = new HashSet();
        }
        mappings.put(key, value);
        valueKeys.add(value);
    }

    public String get(String key) {
        return ((String) mappings.get(key));
    }

    public Object filter(ExecutionContext context, DataElement container, Object o) {
        if (o instanceof Map) o = Collections.singleton(o);
        if (!(o instanceof Collection)) return (o);
        Collection c = (Collection) o;
        List result = new ArrayList();
        Map<Object, Object> currentMap = null;
        for (Iterator i = c.iterator(); i.hasNext(); ) {
            Object v = i.next();
            if (v instanceof Collection) {
                result.add(filter(context, container, v));
                currentMap = null;
                continue;
            }
            if (!(v instanceof Map)) throw new RuntimeException("List element is not a map[" + o.getClass() + "]");
            if (currentMap == null) {
                currentMap = new LinkedHashMap<Object, Object>();
                result.add(currentMap);
            }
            merge(currentMap, (Map<Object, Object>) v);
        }
        if (result.size() == 1) return result.get(0);
        return (result);
    }

    protected void appendValue(Map dest, Object key, Object val) {
        Object target = dest.get(key);
        if (target == null) {
            dest.put(key, val);
            return;
        }
        if (target instanceof Collection) {
            ((Collection) target).add(val);
            return;
        }
        List l = new ArrayList();
        l.add(target);
        l.add(val);
        dest.put(key, l);
    }

    protected void merge(Map<Object, Object> dest, Map<Object, Object> src) {
        if (mappings != null && mappings.size() > 0) {
            for (Iterator i = mappings.entrySet().iterator(); i.hasNext(); ) {
                Map.Entry e = (Map.Entry) i.next();
                Object key = src.get(e.getKey());
                if (key == null) {
                    if (ignoreMissingKeys) continue;
                    throw new RuntimeException("Source map does not contain key[" + e.getKey() + "]");
                }
                key = String.valueOf(key);
                Object val = src.get(e.getValue());
                if (val == null) {
                    throw new RuntimeException("Source map does not contain value for[" + e.getValue() + "]");
                }
                appendValue(dest, key, val);
            }
            if (!copyUnmappedValues) return;
        }
        for (Map.Entry<Object, Object> e : src.entrySet()) {
            Object key = e.getKey();
            if (mappings != null && (mappings.containsKey(key) || valueKeys.contains(key))) continue;
            appendValue(dest, e.getKey(), e.getValue());
        }
    }
}
