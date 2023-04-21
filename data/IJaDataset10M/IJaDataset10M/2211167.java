package progranet.omg.ocl.customlibrary.opers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class SequenceSortedByImpl extends IteratorImpl {

    private Map<Object, Set<Object>> acc;

    public SequenceSortedByImpl(Collection collection) {
        super(collection);
        this.acc = new HashMap<Object, Set<Object>>();
    }

    @SuppressWarnings("unchecked")
    protected Object calculateResult() {
        List<Object> keys = new ArrayList<Object>(this.acc.keySet());
        Collections.sort((List) keys, new Comparator<Object>() {

            public int compare(Object o1, Object o2) {
                if (o1 == null) return -1;
                if (o2 == null) return 1;
                if (o1 instanceof Comparable && o2 instanceof Comparable) return ((Comparable) o1).compareTo(o2);
                return 0;
            }
        });
        List<Object> result = new ArrayList<Object>();
        Iterator<Object> i = keys.iterator();
        while (i.hasNext()) result.addAll(this.acc.get(i.next()));
        return result;
    }

    public void callback(Object bodyExpressionResult) {
        if (this.acc.get(bodyExpressionResult) == null) this.acc.put(bodyExpressionResult, new HashSet<Object>());
        acc.get(bodyExpressionResult).add(this.element);
    }
}
