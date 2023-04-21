package fido.linguistic;

import fido.db.FidoException;

/**
 * Too many instances match the requested attribute and link set.
 * Only one instance is allowed to match
 */
public class MultipleInstancesMatchException extends FidoException {

    /**
	 * Constructs an <CODE>MultipleInstancesMatchException</CODE>
	 * without a detail message.
	 */
    public MultipleInstancesMatchException() {
        super();
    }
}
