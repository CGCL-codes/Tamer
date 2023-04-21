package org.liris.schemerger.core.pattern;

import java.util.Map;
import java.util.TreeMap;
import org.liris.schemerger.core.persistence.TypeAdapter;

/**
 * Represents the type of an item in a simple event (
 * {@link org.liris.schemerger.core.event.ISimEvent}). The item type is internally
 * represented as an int.
 * 
 * @author Damien Cram
 * 
 */
public class ItemType implements Comparable<ItemType>, URIDisplayer {

    private final int typeCode;

    private static Map<Integer, ItemType> codeMap = new TreeMap<Integer, ItemType>();

    public static ItemType get(int code) {
        if (codeMap.get(code) == null) codeMap.put(code, new ItemType(code));
        return codeMap.get(code);
    }

    private ItemType(int typeCode) {
        super();
        this.typeCode = typeCode;
    }

    public int asIntCode() {
        return this.typeCode;
    }

    public int compareTo(ItemType o) {
        if (this.typeCode < o.typeCode) return -1;
        if (this.typeCode > o.typeCode) return 1;
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ItemType) return this.typeCode == ((ItemType) o).typeCode;
        return false;
    }

    @Override
    public int hashCode() {
        return this.typeCode;
    }

    @Override
    public String toString() {
        return TypeAdapter.toString(this);
    }

    public String toString(TypeAdapter adapter) {
        return adapter.getURI(this);
    }

    public ItemType copy() {
        return new ItemType(typeCode);
    }
}
