package vilaug.exceptions;

import jung.refact.FatalException;

/**
 * The <code>MutationException</code> is thrown in the context of mutations.
 * For example when the cargo of a mutation is not like expected. With a
 * <code>MutationType.CREATE_VERTEX</code> the carried cargo should be from
 * the type <code>Vertex</code>. If not, a <code>MutationException</code>
 * can be thrown.
 * 
 * @author A.C. van Rossum
 *
 */
public class MutationException extends FatalException {

    public MutationException(String classID, String methodID, String suggestion) {
        super(classID, methodID, suggestion);
    }

    public MutationException(String classID, String methodID, String suggestion, Throwable cause) {
        super(classID, methodID, suggestion, cause);
    }
}
