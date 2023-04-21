package org.jtools.condition;

import org.jpattern.condition.Condition;
import org.jtools.meta.meta_inf.antlib.AntDef;
import org.jtools.meta.meta_inf.antlib.AntLib;

@AntLib(@AntDef("and"))
public class AndHelper<E> extends ConditionContainerHelper<E, And<E>> {

    public AndHelper() {
        super(null);
    }

    @Override
    public Condition<? super E> toInstance() {
        return And.valueOf(getChildAsInstance());
    }
}
