package purej.web.servlet.converter;

import purej.web.servlet.ParameterException;

/**
 * ������ Ÿ�� ������ �������̽�
 * 
 * @author leesangboo
 * 
 */
public interface Converter {

    /**
     * �ԷµǴ� ���� �������Ѵ�.
     * 
     * @param value
     * @return
     */
    public Object toInput(String value) throws ParameterException;

    /**
     * ��µǴ� ���� �������Ѵ�.
     * 
     * @param value
     * @return
     */
    public String toOutput(Object value) throws ParameterException;
}
