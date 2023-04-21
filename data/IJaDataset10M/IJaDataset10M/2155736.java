package com.esri.gpt.framework.jsf;

import com.esri.gpt.framework.context.RequestContext;
import com.esri.gpt.framework.jsf.BaseActionListener;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;

/**
 * Handles a sample action.
 * <p>
 * This class simply serves as a template for a controller.
 */
public class SampleController extends BaseActionListener {

    /** Default constructor. */
    public SampleController() {
    }

    /**
 * Handles a secondary action.
 * <br/>This is a template for processing an action that is not
 * entered through the normal JSF processAction method.
 * @param event the associated JSF action event
 * @throws AbortProcessingException if processing should be aborted
 */
    public void processSecondaryAction(ActionEvent event) throws AbortProcessingException {
        try {
            RequestContext context = onExecutionPhaseStarted();
            authorizeAction(context);
        } catch (AbortProcessingException e) {
            throw (e);
        } catch (Throwable t) {
            handleException(t);
        } finally {
            onExecutionPhaseCompleted();
        }
    }

    /**
 * Handles the primary action.
 * <br/>This is the default entry point for a sub-class of BaseActionListener.
 * <br/>This BaseActionListener handles the JSF processAction method and
 * invokes the processSubAction method of the sub-class.
 * @param event the associated JSF action event
 * @param context the context associated with the active request
 * @throws AbortProcessingException if processing should be aborted
 * @throws Exception if an exception occurs
 */
    @Override
    protected void processSubAction(ActionEvent event, RequestContext context) throws AbortProcessingException, Exception {
    }
}
