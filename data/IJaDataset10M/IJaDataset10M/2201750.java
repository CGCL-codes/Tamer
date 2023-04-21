package com.volantis.mcs.eclipse.ab.editors.dom;

/**
 * Class to manage the testing of code using {@link ODOMEditorContext}s.
 * <p>
 * This creates and disposes the context for each testing operation that it
 * is asked to perform.
 * <p>
 * This is necessary because we leave background threads running if we
 * don't dispose of the context properly.
 */
public class ODOMEditorContextManager {

    /**
     * Class to create the contexts we will test.
     */
    private ODOMEditorContextCreator creator;

    /**
     * Initialise.
     *
     * @param creator the class to create contexts to test.
     */
    public ODOMEditorContextManager(ODOMEditorContextCreator creator) {
        this.creator = creator;
    }

    /**
     * Perform the operation passed in, constructing the context to test
     * with the creator passed in the constructor and disposing of the context
     * when the operation has completed.
     *
     * @param operation the operation to perform.
     * @throws Exception if there was a problem performing the operation. Note
     *      that the context will be disposed even if an exception is thrown.
     */
    public void performOperation(ODOMEditorContextOperation operation) throws Exception {
        ODOMEditorContext context = creator.create();
        try {
            operation.perform(context);
        } finally {
            context.dispose();
        }
    }
}
