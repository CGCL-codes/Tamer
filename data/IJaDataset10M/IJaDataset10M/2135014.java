package tudresden.ocl20.pivot.modelinstancetype.exception;

import tudresden.ocl20.pivot.pivotmodel.Type;

/**
 * <p>
 * An {@link Exception} that is thrown, if an element of a resource that shall
 * be loaded as <code>IModelInstance</code> cannot be mapped to any {@link Type}
 * of the <code>IModel</code> the <code>IModelInstance</code> is an instance of.
 * </p>
 * 
 * @author Claas Wilke
 */
public class TypeNotFoundInModelException extends Exception {

    /**
	 * <p>
	 * The id required for serialization.
	 * </p>
	 */
    private static final long serialVersionUID = 3880555924270702528L;

    /**
	 * <p>
	 * Creates a new {@link TypeNotFoundInModelException}.
	 * </p>
	 * 
	 * @param msg
	 *          The message of the created {@link Exception}.
	 */
    public TypeNotFoundInModelException(String msg) {
        super(msg);
    }

    /**
	 * <p>
	 * Creates a new {@link TypeNotFoundInModelException}.
	 * </p>
	 * 
	 * @param msg
	 *          The message of the created {@link Exception}.
	 * @param e
	 *          A given {@link Exception} that is the cause of this
	 *          {@link Exception}.
	 */
    public TypeNotFoundInModelException(String msg, Exception e) {
        super(msg, e);
    }
}
