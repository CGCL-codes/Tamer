package org.omg.DfResourceAccessDecision;

/***/
public final class InputFormatError extends org.omg.CORBA.UserException {

    public InputFormatError() {
        super(InputFormatErrorHelper.id());
    }

    public InputFormatError(ExceptionData ed) {
        super(InputFormatErrorHelper.id());
        this.ed = ed;
    }

    public InputFormatError(String _reason, ExceptionData ed) {
        super(InputFormatErrorHelper.id() + " " + _reason);
        this.ed = ed;
    }

    public ExceptionData ed;
}
