package org.zkoss.bind.impl;

import java.lang.ref.WeakReference;
import org.zkoss.lang.Objects;
import org.zkoss.zk.ui.Component;

/**
 * 
 * @author dennis
 * @since 6.0.0
 */
public class BindingKey implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /** 
	 * The first key. 
	 **/
    private final WeakReference<Component> x;

    /** The second key. */
    private final String y;

    public BindingKey(Component x, String y) {
        this.x = new WeakReference<Component>(x);
        this.y = y;
    }

    public boolean isAvailable() {
        return x.get() != null;
    }

    public final boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof BindingKey)) return false;
        final BindingKey key = (BindingKey) o;
        return Objects.equals(x.get(), key.x.get()) && Objects.equals(y, key.y);
    }

    public final int hashCode() {
        return Objects.hashCode(x.get()) ^ Objects.hashCode(y);
    }

    public String toString() {
        return '(' + Objects.toString(x.get()) + ", " + Objects.toString(y) + ')';
    }
}
