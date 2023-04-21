package il.ac.biu.cs.grossmm.api.keys;

/**
 * A base class for <tt>Key</tt> interface implementation
 * which provides a default implementation for a {@linkplain Key#value(Attribute)}
 */
public abstract class KeyBase implements Key {

    public final Object value(Attribute a) {
        Key subkey = subkey(a);
        if (subkey == null) return null;
        return subkey.value();
    }
}
