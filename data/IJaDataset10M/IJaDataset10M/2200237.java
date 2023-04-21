package org.jfonia.connect;

/**
 * Adapter class which caches a value (which is costly to get/calculate).
 * The class keeps track of the validity of the cash. When called
 * (due to a change of the source value), the validity is set to false.
 * When the value is asked (get()), then the cached value is
 * renewed if necessary, and returned.
 * A user may query (get) a lazy value as often as preferred, without
 * worrying about excessive costs due to the calculation of a source value.  
 * 
 * @author wijnand.schepens@gmail.com
 *
 */
public class LazyValue<T> extends BasicNode implements Value<T> {

    protected Value<T> source;

    protected T cache;

    protected boolean valid;

    /**
	 * if cache-value is invalid then it is renewed (by querying the source value). The cache is returned
	 * @return cached value, revalidated if necessary
	 */
    public T get() {
        if (!valid) {
            cache = source.get();
            valid = true;
        }
        return cache;
    }

    public boolean callThis(Object param) {
        valid = false;
        return true;
    }
}
