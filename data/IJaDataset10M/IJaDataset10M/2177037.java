package net.jadoth.sqlengine;

public class SqlEngineNotConnectedException extends SqlEngineException {

    public SqlEngineNotConnectedException() {
        super();
    }

    public SqlEngineNotConnectedException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public SqlEngineNotConnectedException(final String message) {
        super(message);
    }

    public SqlEngineNotConnectedException(final Throwable cause) {
        super(cause);
    }

    private static final long serialVersionUID = 1L;
}
