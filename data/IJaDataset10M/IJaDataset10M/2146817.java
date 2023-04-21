package carassius.DAL;

/**
 *
 * @author siebz0r
 */
public abstract class Row<K> {

    private K _primaryKeys;

    public Row(K primaryKeys) {
        _primaryKeys = primaryKeys;
    }

    public K getPrimaryKeys() {
        return _primaryKeys;
    }
}
