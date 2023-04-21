package org.jrcaf.mvc.validator;

import org.eclipse.swt.widgets.Widget;
import org.jrcaf.mvc.IController;
import org.jrcaf.mvc.IView;

/**
 * Interface for validators in views.
 */
public interface IValidator {

    /**
    *  Possible error types.
    */
    public enum ErrorType {

        /** No error. */
        OK, /** Error. */
        ERROR, /** Warning. */
        WARNING
    }

    /**
    *  The result of a validation.
    */
    public static class ValidationResult {

        private ErrorType errorType;

        private String errorMessage;

        private Object errorObject;

        /**
       * Creates a new ValidationResult.
       * @param aErrorType The type of the error.
       * @param aErrorMessage The error message.
       * @param aErrorObject The faulty object.
       */
        public ValidationResult(ErrorType aErrorType, String aErrorMessage, Object aErrorObject) {
            this(aErrorType);
            errorMessage = aErrorMessage;
            errorObject = aErrorObject;
        }

        /**
       * Creates a new ValidationResult.
       * @param aErrorType The type of the error.
       */
        public ValidationResult(ErrorType aErrorType) {
            errorType = aErrorType;
        }

        /**
       * @return Returns the errorMessage.
       */
        public String getErrorMessage() {
            return errorMessage;
        }

        /**
       * @return Returns the errorType.
       */
        public ErrorType getErrorType() {
            return errorType;
        }

        /**
       * @return The Object causing the error
       */
        public Object getErrorObject() {
            return errorObject;
        }
    }

    /**
    * Validates the state of the passed widget.  
    * @param aWidget The widget to validate.
    * @param aView The IView.
    * @param aController The IController.
    * @param aDataName The name of the data widget.
    * @param aValidatorParam The parameters for the validation.
    * @return The result of the validation
    */
    public abstract ValidationResult validate(Widget aWidget, IView aView, IController aController, String aDataName, String aValidatorParam);
}
