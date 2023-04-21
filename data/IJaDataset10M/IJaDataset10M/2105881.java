package org.op4j.operations;

import org.op4j.target.OperationChainingTarget;

/**
 * 
 * @since 1.0
 * 
 * @author Daniel Fern&aacute;ndez
 *
 */
public final class Operation<R, T> {

    private final OperationChainingTarget chainTarget;

    public Operation(final OperationChainingTarget target) {
        super();
        this.chainTarget = target;
    }

    @SuppressWarnings("unchecked")
    public final R execute(final T target) {
        return (R) this.chainTarget.executeChain(target);
    }
}
