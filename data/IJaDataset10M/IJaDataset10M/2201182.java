package com.germinus.portlet.lms.model;

public class GeneralScormDataModelException extends ScormModelException {

    private static final int CODE = 0;

    public GeneralScormDataModelException(String string) {
        super(string);
    }

    @Override
    public int getErrorCode() {
        return CODE;
    }

    private static final long serialVersionUID = -581700702190875307L;
}
