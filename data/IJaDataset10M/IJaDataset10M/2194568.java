package org.op4j.functions;

import org.op4j.exceptions.NullTargetException;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public abstract class AbstractNotNullFunc<R, T> implements IFunction<R, T> {

    protected AbstractNotNullFunc() {
        super();
    }

    public final R execute(final T object, final ExecCtx ctx) throws Exception {
        if (object == null) {
            throw new NullTargetException();
        }
        return notNullExecute(object, ctx);
    }

    public abstract R notNullExecute(final T object, final ExecCtx ctx) throws Exception;
}
