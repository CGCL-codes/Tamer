package net.sf.logshark.simple;

public class GeneralConfigurationError {

    private String m_message;

    private int m_lineNumber;

    public GeneralConfigurationError(String message, int lineNumber) {
        super();
        m_message = message;
        m_lineNumber = lineNumber;
    }

    public String getMessage() {
        return m_message;
    }

    public int getLineNumber() {
        return m_lineNumber;
    }
}
