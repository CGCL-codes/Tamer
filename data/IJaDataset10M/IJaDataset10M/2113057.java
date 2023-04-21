package uk.co.wilson.ng.ast;

import ng.runtime.metaclass.MetaClass;
import ng.runtime.threadcontext.ThreadContext;

/**
 * @author John
 * 
 */
public class OrImpl extends BinaryOperationImpl {

    @Override
    public Object evaluate(final Object instance, final MetaClass metaClass, final ThreadContext tc) {
        return tc.or().apply(this.lhs.evaluate(instance, metaClass, tc), this.rhs.evaluate(instance, metaClass, tc));
    }
}
