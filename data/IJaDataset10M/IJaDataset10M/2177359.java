package edu.math;

/**
 * <p/>
 * Title: DataTypeException
 * </p>
 * <p/>
 * Description: ��������� ��������, ��� ����������� ��� ������� ����������
 * ���� �����
 * </p>
 * <p/>
 * Copyright: (c) 2004 by PR
 * </p>
 * <p/>
 * Company: ZSTU, FICT, AK-15
 * </p>
 *
 * @author Roman Petruk aka PR
 * @version 1.0
 */
public class DataException extends RuntimeException {

    /**
     * ��� ��������� ��������: ������������ ��� �����
     */
    public static final byte INVALID_TYPE = 1;

    /**
     * ��� ��������� ��������: �������� ���������������� �����
     */
    public static final byte UNINITIALIZED_VARIABLE = 2;

    /**
     * ��� ��������� ��������: ��� ���� ���������� (��� ���������� ���
     * �����)
     */
    public static final byte WRONG_DATA = 3;

    /**
     * ��� ��������� ��������: ������� �����
     */
    public static final byte UNKNOWN_VARIABLE = 4;

    /**
     * ��� ��������� ��������: ����� ��� ����
     */
    public static final byte VARIABLE_EXISTS = 5;

    /**
     * ��� ��������� ��������
     */
    public byte type;

    /**
     * �������� ��������� ��������
     */
    public String error;

    /**
     * �����������, ���� ������� ��������� �������� �� ������� �����
     *
     * @param type ��� ��������� ��������
     */
    public DataException(byte type) {
        this.type = type;
    }

    /**
     * �����������, ���� ������� ��������� �������� �� ������� �� ����� ��
     * ���������
     *
     * @param type  ��� ��������� ��������
     * @param error �������� ��������� ��������
     */
    public DataException(byte type, String error) {
        this.type = type;
        this.error = error;
    }

    /**
     * ����� ����� � ��������� ��������� ��������
     *
     * @return �������� ��������� ��������
     */
    @Override
    public String toString() {
        String errMsg = "";
        switch(type) {
            case DataException.INVALID_TYPE:
                errMsg = "Invalid Type";
                break;
            case DataException.UNINITIALIZED_VARIABLE:
                errMsg = "Detected uninitialized variable";
                break;
            case DataException.WRONG_DATA:
                errMsg = "Wrong data";
                break;
            case DataException.UNKNOWN_VARIABLE:
                errMsg = "Unknown variable";
                break;
            case DataException.VARIABLE_EXISTS:
                errMsg = "Variable already exists";
                break;
        }
        if (error != null) {
            errMsg += " (" + error + ")";
        }
        return errMsg;
    }
}
