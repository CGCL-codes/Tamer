package org.gamegineer.common.internal.persistence.memento;

import java.util.ArrayList;
import java.util.Collection;
import org.gamegineer.test.core.AbstractEquatableTestCase;

/**
 * A fixture for testing the
 * {@link org.gamegineer.common.internal.persistence.memento.Memento} class to
 * ensure it does not violate the contract of the equatable interface.
 */
public final class MementoAsEquatableTest extends AbstractEquatableTestCase {

    /**
     * Initializes a new instance of the {@code MementoAsEquatableTest} class.
     */
    public MementoAsEquatableTest() {
        super();
    }

    @Override
    protected Object createReferenceInstance() {
        return Mementos.createMemento(4, 1);
    }

    @Override
    protected Collection<Object> createUnequalInstances() {
        final Collection<Object> others = new ArrayList<Object>();
        others.add(Mementos.createMemento(3, 1));
        others.add(Mementos.createMemento(4, 2));
        return others;
    }
}
