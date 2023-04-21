package jvc.util;

import java.util.Arrays;
import java.util.Random;

/**
 * <p>Title :RandomUtils</p>
 * <p>Description:������������ࡣ������ʵ���˲���һ����Χ�������������һ�����ȵ�����ַ��Ͳ�������ַ�</p>
 * <p>Created on 2004-3-4</p>
 * <p>Company :YPL</p>
 *  @author : zhang_b
 *  @version : 1.0
 */
public class RandomUtils {

    /**
	   * ����ַ�����Ͷ��壬�����͹涨�ַ��������ĸ��ͷ
	   */
    public static final int START_BY_CHAR_TYPE = 1;

    /**
	   * ����ַ�����Ͷ��壬�����͹涨�ַ�ȫ���ɴ�д��ĸ���
	   */
    public static final int UPPERCASE_CHAR_TYPE = 2;

    /**
	   *  ����ַ�����Ͷ��壬�����͹涨�ַ�ȫ����Сд��ĸ���
	   */
    public static final int DOWNCASE_CHAR_TYPE = 3;

    /**
	   *  ����ַ�����Ͷ��壬�����͹涨�ַ�ȫ����������ĸ���
	   */
    public static final int NUMBER_CHAR_TYPE = 4;

    private static Random random;

    static {
        random = new Random();
    }

    public static int[] getRandomIntArray(int _minValue, int _maxValue, int size) {
        int[] re = new int[size];
        if (_maxValue - _minValue <= size) {
            for (int i = 0; i < _maxValue - _minValue + 1; i++) re[i] = _minValue + i;
            return re;
        }
        int iPos = 0;
        while (iPos < size) {
            int ranValue = getNextRandomInt(_minValue, _maxValue);
            if (!ArrayUtils.isInArray(re, ranValue)) {
                re[iPos] = ranValue;
                iPos++;
            }
        }
        Arrays.sort(re);
        return re;
    }

    /**
	   * ����ڷ�Χ[<b>_minValue</b>, <b>_maxValue]�ڵ�һ�������
	   * @param _minValue �������ķ�Χ����
	   * @param _maxValue �������ķ�Χ����
	   * @return �������
	   * @exception java.lang.IllegalArgumentException ������������������С�ڻ��������ʱ����
	   */
    public static int getNextRandomInt(int _minValue, int _maxValue) {
        if (_minValue > _maxValue) {
            throw new IllegalArgumentException("Parameter exception:[" + _minValue + " must < [" + _maxValue + "]");
        }
        if (_minValue == _maxValue) return _minValue;
        float randomFloat = random.nextFloat();
        int temp = (_minValue - 1) + (int) ((_maxValue - _minValue + 2) * randomFloat);
        if (temp < _minValue || temp > _maxValue) {
            temp = getNextRandomInt(_minValue, _maxValue);
        }
        return temp;
    }

    /**
	   * ����ڷ�Χ[0, <b>_maxValue</b>]�ڵ�һ�������ʵ���϶��ڸ÷�������ʹ��{@link java.utils.Random#nextInt(int)}���档
	   * @param _maxValue �������ķ�Χ����
	   * @return �������
	   * @exception java.lang.IllegalArgumentException ������������������С�ڻ����0ʱ����
	   */
    public static int getNextRandomInt(int _maxValue) {
        return getNextRandomInt(0, _maxValue);
    }

    /**
	   * ��ó���Ϊ<b>length</b>������ַ�, ���ַ�����������ֺ���ĸ��ɡ�
	   * @param _length �ַ���
	   * @return ����ַ�
	   */
    public static String getNextRandomString(int _length) {
        char[] strChar = new char[_length];
        for (int i = 0; i < _length; i++) {
            int select = getNextRandomInt(4);
            switch(select) {
                case 0:
                    {
                        strChar[i] = getNextRandomNumberChar();
                        break;
                    }
                case 1:
                    {
                        strChar[i] = getNextRandomNumberChar();
                        break;
                    }
                case 2:
                    {
                        strChar[i] = getNextRandomUpperCaseChar();
                        break;
                    }
                case 3:
                    {
                        strChar[i] = getNextRandomDownCaseChar();
                        break;
                    }
                case 4:
                    {
                        strChar[i] = getNextRandomDownCaseChar();
                        break;
                    }
            }
        }
        return new String(strChar);
    }

    /**
	   * ��ó���Ϊ<b>length</b>������ַ�
	   * @param _length �ַ���
	   * @param _type �ַ�����
	   * @return ����ַ�
	   * @exception java.lang.IllegalArgumentException �������<b>_type</b>����ȷʱ����.
	   * @see #START_BY_CHAR_TYPE
	   * @see #UPPERCASE_CHAR_TYPE
	   * @see #DOWNCASE_CHAR_TYPE
	   * @see #NUMBER_CHAR_TYPE
	   */
    public static String getNextRandomString(int _length, int _type) {
        String temp = null;
        switch(_type) {
            case START_BY_CHAR_TYPE:
                {
                    temp = getNextRandomString(_length);
                    if (Character.isDigit(temp.charAt(0))) {
                        temp = getNextRandomString(_length, START_BY_CHAR_TYPE);
                    }
                    break;
                }
            case UPPERCASE_CHAR_TYPE:
                {
                    char[] strChar = new char[_length];
                    for (int i = 0; i < _length; i++) {
                        strChar[i] = getNextRandomUpperCaseChar();
                    }
                    temp = new String(strChar);
                    break;
                }
            case DOWNCASE_CHAR_TYPE:
                {
                    char[] strChar = new char[_length];
                    for (int i = 0; i < _length; i++) {
                        strChar[i] = getNextRandomDownCaseChar();
                    }
                    temp = new String(strChar);
                    break;
                }
            case NUMBER_CHAR_TYPE:
                {
                    char[] strChar = new char[_length];
                    for (int i = 0; i < _length; i++) {
                        strChar[i] = getNextRandomNumberChar();
                    }
                    temp = new String(strChar);
                    break;
                }
            default:
                {
                    throw new java.lang.IllegalArgumentException("Not support the type[" + _type + "]");
                }
        }
        return temp;
    }

    /**
	   * ������������ַ�
	   * @return ���������ַ�
	   */
    public static char getNextRandomNumberChar() {
        return (char) getNextRandomInt(48, 57);
    }

    /**
	   * ������Ĵ�д��ĸ�ַ�
	   * @return ���Ĵ�д��ĸ�ַ�
	   */
    public static char getNextRandomUpperCaseChar() {
        return (char) getNextRandomInt(65, 90);
    }

    /**
	   * �������Сд��ĸ�ַ�
	   * @return ����Сд��ĸ�ַ�
	   */
    public static char getNextRandomDownCaseChar() {
        return (char) getNextRandomInt(97, 122);
    }

    public static void main(String args[]) {
        int re[] = getRandomIntArray(0, 20, 5);
        Arrays.sort(re);
        for (int i = 0; i < re.length; i++) System.out.print("," + re[i]);
    }
}
