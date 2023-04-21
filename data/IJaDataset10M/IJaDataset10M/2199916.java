package org.waveprotocol.wave.model.operation;

import org.waveprotocol.wave.model.operation.Operation;
import org.waveprotocol.wave.model.operation.OperationException;
import java.util.List;

/**
 * This represents an operation that is able to return the reverse operation of
 * itself after application.
 *
 *
 * @param <O> The Operation Class
 * @param <T> The Class on which apply() and applyAndReturnReverse() can be called.
 */
public interface ReversibleOperation<O extends Operation<T>, T> extends Operation<T> {

    /**
   * Applies the operation to a target and returns a sequence of operations
   * which can reverse the application.
   *
   * @param target The target onto which to apply the operation.
   * @return A sequence of operations that reverses the application of this
   *         operation. The returned sequence of operations, when applied in
   *         order after this operation is applied, should reverse the effect of
   *         this operation.
   */
    public List<? extends O> applyAndReturnReverse(T target) throws OperationException;
}
