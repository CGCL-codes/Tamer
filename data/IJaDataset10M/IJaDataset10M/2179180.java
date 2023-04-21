package org.gamegineer.common.persistence.schemes.beans;

import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import net.jcip.annotations.NotThreadSafe;

/**
 * A persistence delegate for the {@code FakeNonPersistableClass} class.
 */
@NotThreadSafe
public final class FakeNonPersistableClassPersistenceDelegate extends PersistenceDelegate {

    /**
     * Initializes a new instance of the {@code
     * FakeNonPersistableClassPersistenceDelegate} class.
     */
    public FakeNonPersistableClassPersistenceDelegate() {
        super();
    }

    @Override
    @SuppressWarnings("boxing")
    protected Expression instantiate(final Object oldInstance, @SuppressWarnings("unused") final Encoder out) {
        final FakeNonPersistableClass obj = (FakeNonPersistableClass) oldInstance;
        return new Expression(oldInstance, FakeNonPersistableClass.class, "new", new Object[] { obj.getIntField(), obj.getStringField() });
    }
}
