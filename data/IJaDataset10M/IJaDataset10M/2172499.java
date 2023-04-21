package com.maiereni.common.stream;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A list if key indexes
 * 
 * @author Petre Maierean
 */
public class KeyIndexes extends KeyIndex implements Cloneable {

    protected List<KeyIndex> keys = new ArrayList<KeyIndex>();

    protected int step = 0;

    public KeyIndexes(KeyIndexes parent) {
        super(parent);
    }

    public KeyIndexes() {
        super(null);
    }

    /**
	 * Idenitify the keyName of this container first and then go through any
	 * keys it may contain
	 * @param b the current character
	 * @param position the current stream position
	 * @return true if the container and it elements have been identified. False otherwise
	 */
    public boolean identified(int b, int position) {
        if (!super.identified(b, position)) return false;
        Iterator iter = keys.iterator();
        boolean ret = true;
        for (int i = 0; iter.hasNext(); i++) {
            KeyIndex index = (KeyIndex) iter.next();
            if (index instanceof OptionalKeyIndexes) {
                if (i < step && (!((OptionalKeyIndexes) index).hasCompleted())) {
                    continue;
                }
            }
            if (!index.identified(b, position)) {
                if (!index.isOptional()) {
                    ret = false;
                    break;
                }
            } else {
                if (index.justIdentified()) {
                    index.reset();
                    step++;
                    break;
                }
            }
        }
        return ret;
    }

    protected void removeKey(KeyIndex key) {
        if (keys != null) {
            keys.remove(key);
        }
    }

    public List<KeyIndex> getKeys() {
        return keys;
    }

    public void setKeys(List<KeyIndex> keys) {
        this.keys = keys;
    }

    public void addListener2All(SearchKeyListener listener) {
        Iterator<KeyIndex> iter = keys.iterator();
        while (iter.hasNext()) {
            KeyIndex ix = iter.next();
            if (ix.len > 0) ix.addListener(listener);
            if (ix instanceof KeyIndexes) {
                ((KeyIndexes) ix).addListener2All(listener);
            }
        }
        addListener(listener);
    }

    public void removeListener(SearchKeyListener listener) {
        Iterator<KeyIndex> iter = keys.iterator();
        while (iter.hasNext()) {
            KeyIndex ix = iter.next();
            ix.removeListener(listener);
        }
        super.removeListener(listener);
    }

    public Object clone() {
        logger.debug("Clone");
        KeyIndexes clone = new KeyIndexes(null);
        clone.setKeyName(this.keyName);
        Iterator<KeyIndex> iter = keys.iterator();
        while (iter.hasNext()) {
            KeyIndex ix = iter.next();
            if (ix instanceof OptionalKeyIndexes) ix = (OptionalKeyIndexes) ix.clone(); else if (ix instanceof KeyIndexes) ix = (KeyIndexes) ix.clone(); else if (ix instanceof RecurringKeyIndexes) ix = (RecurringKeyIndexes) ix.clone(); else ix = (KeyIndex) ix.clone();
            ix.parent = clone;
            clone.keys.add(ix);
        }
        return clone;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<index");
        if (keyName != null) sb.append(" keyName=\"").append(keyName).append("\"");
        sb.append(" startsAt=\"").append(startIndex).append("\" endsAt=\"").append(endIndex).append("\"");
        if (!keys.isEmpty()) {
            sb.append(">");
            Iterator<KeyIndex> iter = keys.iterator();
            while (iter.hasNext()) {
                KeyIndex ix = iter.next();
                sb.append(ix.toString());
            }
            sb.append("</index>");
        }
        return sb.toString();
    }
}
