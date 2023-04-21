package ru.kuban.chess.config;

/**
 * ������ �������������� ���� ������.
 */
public class ConvertConfigureException extends ConfigureException {

    /**
     * ����������� ��� ������.
     */
    private final Class type;

    /**
     * ��������, ������� �������� �������������.
     */
    private final String value;

    /**
     * ���������� ����������� ��� ������.
     */
    public Class getType() {
        return type;
    }

    /**
     * ���������� ��������, ������� �������� �������������.
     */
    public String getValue() {
        return value;
    }

    /**
     * �����������.
     * @param type ����������� ��� ������;
     * @param value ��������, ������� �������� �������������.
     */
    public ConvertConfigureException(Class type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * �����������.
     * @param type ����������� ��� ������;
     * @param value ��������, ������� �������� �������������;
     * @param cause �������.
     */
    public ConvertConfigureException(Class type, String value, Throwable cause) {
        super(cause);
        this.type = type;
        this.value = value;
    }

    @Override
    public String getMessage() {
        return "Unable to convert to " + (type == null ? null : type.getName()) + ": \"" + value + "\"";
    }
}
