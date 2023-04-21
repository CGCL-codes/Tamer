package atp.reporter.exception;

/**
 * @author Shteinke_KE
 * ���������� ��� ������ � ������������ �������
 */
public class RPrepareException extends Exception {

    /**
	 * �������� - ���������� ��� ����
	 */
    private static final long serialVersionUID = -8972708788925946198L;

    public RPrepareException() {
        super();
    }

    public RPrepareException(String message, Throwable cause) {
        super(message, cause);
    }

    public RPrepareException(String message) {
        super(message);
    }

    public RPrepareException(Throwable cause) {
        super(cause);
    }
}
