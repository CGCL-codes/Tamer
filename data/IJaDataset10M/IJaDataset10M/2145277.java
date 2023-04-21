package uk.co.wilson.ng.ast;

import ng.ast.IntegerConstant;
import ng.runtime.metaclass.MetaClass;
import ng.runtime.threadcontext.ThreadContext;
import uk.co.wilson.ng.runtime.NgInt;

/**
 * @author John
 * 
 */
public class IntegerConstantImpl extends NodeImpl implements IntegerConstant {

    private final NgInt value;

    public IntegerConstantImpl(final int value) {
        this.value = NgInt.valueOf(value);
    }

    public Object evaluate(final Object instance, final MetaClass metaClass, final ThreadContext tc) {
        return this.value;
    }
}
