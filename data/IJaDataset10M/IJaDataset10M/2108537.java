package oscript.util;

import java.util.Vector;
import java.util.Iterator;
import oscript.exceptions.*;

/**
 * This utility class provides a more Hashtable-like interface to 
 * {@link SymbolTable}, which normally maps a symbol to a table index.
 * <p>
 * Threading note: this class is not synchronized, but is designed to
 * save to read from multiple threads, while write from a single thread
 * context (at a time).
 * 
 * @author Rob Clark (rob@ti.com)
 * @version 0.0
 * @see SymbolTable
 */
public class SymbolMap {

    private final SymbolTable table;

    private Object[] values;

    /**
   * Class Constructor
   */
    public SymbolMap() {
        this(new OpenHashSymbolTable());
    }

    /**
   * Class Constructor
   * 
   * @param table   the underlying table data structure
   */
    public SymbolMap(SymbolTable table) {
        this.table = table;
        this.values = new Object[(table.size() > 0) ? table.size() : 10];
    }

    /**
   * Get a mapping
   */
    public final Object get(int id) {
        int idx = table.get(id);
        if ((idx == -1) || (idx >= values.length)) return null;
        return values[idx];
    }

    /**
   * Put a new mapping in the table
   */
    public final Object put(int id, Object val) {
        int idx = table.create(id);
        if (idx >= values.length) {
            Object[] newValues = new Object[values.length * 2];
            System.arraycopy(values, 0, newValues, 0, values.length);
            values = newValues;
        }
        Object ret = values[idx];
        values[idx] = val;
        return ret;
    }

    /**
   * Return an iterator of keys into the table.  Each key is boxed
   * in an {@link Integer}.
   */
    public Iterator keys() {
        return table.symbols();
    }
}
