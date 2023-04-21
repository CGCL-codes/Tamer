package net.sourceforge.configured.examples.ui.exception;

public class InvalidCategoryNameException extends FieldValidationException {

    public InvalidCategoryNameException(ErrorMessageInfo errorMessageInfo) {
        super(errorMessageInfo);
    }

    public InvalidCategoryNameException(String arg0, ErrorMessageInfo info, Throwable arg1) {
        super(arg0, info, arg1);
    }

    public InvalidCategoryNameException(String arg0, ErrorMessageInfo info) {
        super(arg0, info);
    }

    public InvalidCategoryNameException(Throwable arg0, ErrorMessageInfo info) {
        super(arg0, info);
    }
}
