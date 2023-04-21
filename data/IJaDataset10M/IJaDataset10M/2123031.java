package panda.query.scan;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import panda.query.struct.Attribute;
import panda.query.struct.Constant;

/**
 * 
 * @author Tian Yuan
 *
 */
public class DistinctScanner implements Scanner {

    Scanner s;

    Collection<Attribute> attrs;

    Map<Attribute, Constant> lastVal;

    boolean hasNext;

    public DistinctScanner(Scanner s, Collection<Attribute> attrs) {
        this.s = s;
        this.attrs = attrs;
        lastVal = new HashMap<Attribute, Constant>(attrs.size());
        for (Attribute a : attrs) lastVal.put(a, null);
        init();
    }

    @Override
    public void init() {
        s.init();
        hasNext = s.next();
    }

    @Override
    public boolean next() {
        if (!hasNext) return false;
        setCurrentValue();
        while (hasNext && isEqual()) hasNext = s.next();
        return true;
    }

    @Override
    public void close() {
        s.close();
    }

    private boolean isEqual() {
        for (Attribute a : attrs) {
            if (lastVal.get(a).compareTo(s.getValue(a)) != 0) return false;
        }
        return true;
    }

    private void setCurrentValue() {
        for (Attribute a : attrs) lastVal.put(a, s.getValue(a));
    }

    @Override
    public Constant getValue(Attribute attr) {
        return lastVal.get(attr);
    }

    @Override
    public boolean hasAttribute(Attribute name) {
        return s.hasAttribute(name);
    }
}
