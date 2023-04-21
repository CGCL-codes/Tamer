package mipt.crec.muse.data;

import java.io.DataInput;

/**
 * Interface for asynchronous reading unstructured primitive-type data ("arrays").
 * Any array has an id because multiple arrays can be received.
 * Used not only for reading but for writing too (is implemented by real writer and called by its client).
 * To produce DataInput for you doubles (for instance), you can use the following code:
 *  TO DO
 * @author Alexey Evdokimov
 */
public interface ArrayListener {

    /**
	 * Uses DataInput as argument (in order to support any primitive type) but hopes that
	 *  the caller won't call input method more than once (according to initial DataInput concept).
	 */
    void elementObtained(Object arrayId, DataInput input);

    /**
	 * Called before elementObtained(arrayId,*).
	 * @param elementType Type of array elements (used by universal code only)
	 * @param length Array length
	 */
    void arrayStarted(Object arrayId, Class elementType, int length);
}
