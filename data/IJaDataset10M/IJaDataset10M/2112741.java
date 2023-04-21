package edu.semaster.figurearea.model;

public class InvalidParameterException extends Exception {

    private static final long serialVersionUID = 1L;

    private String m_errorMessage;

    public InvalidParameterException(String errorMessage) {
        m_errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return m_errorMessage;
    }
}
