package org.omwg.mediation.language.objectmodel.api;

import org.omwg.mediation.language.objectmodel.api.comparators.Comparator;
import org.omwg.mediation.language.objectmodel.impl.Path;

public interface ClassCondition<Type extends Comparator> extends Condition {

    public abstract void setAttribute(Path path);

    public abstract Path getAttribute();

    public abstract void setComparator(Type comp);

    public abstract Type getComparator();
}
