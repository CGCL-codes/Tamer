package polr.server.exception;

public class RegisterException extends Exception {

    private Types m_type;

    public enum Types {

        ALREADY_EXISTS, INVALID_DATA, BANNED_USERNAME, OTHER, LOCKDOWN
    }

    public RegisterException(Types type) {
        m_type = type;
    }

    public Types getType() {
        return m_type;
    }
}
