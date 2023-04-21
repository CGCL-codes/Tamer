package pgr.sample.pgrtest.client.services;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Service interface.
 * @author Pawel Majewski
 */
public interface PrimitiveStringNullService extends RemoteService {

    /**
     * @param value boolean value
     * @return negate of value
     */
    boolean booleanRevert(boolean value);

    /**
     * @param value boolean array
     * @return revert boolean array
     */
    boolean[] booleanReverse(boolean[] value);

    /**
     * @param a int val
     * @param b int val
     * @return sum of a and b
     */
    int intSum(int a, int b);

    /**
     * @param a int array
     * @return summ of array elements
     */
    int intSumArray(int[] a);

    /**
     * @param a int value
     * @return all natural dividers of a
     */
    int[] intFindDividers(int a);

    /**
     * @param a short value
     * @param b short value
     * @return sum of a and b
     */
    short shortSum(short a, short b);

    /**
     * @param a short array
     * @return summ of array elements
     */
    short shortSumArray(short[] a);

    /**
     * @param a short value
     * @return all natural dividers of a
     */
    short[] shortFindDividers(short a);

    /**
     * @param a long val
     * @param b long val
     * @return a - b
     */
    long longSubtraction(long a, long b);

    /**
     * @param a long array
     * @return sum of array elements
     */
    long longSum(long[] a);

    /**
     * @param a long value
     * @return all natural dividers of a
     */
    long[] longFindDividers(long a);

    /**
     * @param a double value
     * @param b double value
     * @return a * b
     */
    double doubleMultiplication(double a, double b);

    /**
     * @param a double array
     * @return sum of array elements
     */
    double doubleSum(double[] a);

    /**
     * @param a double value
     * @return floor and ceil of a
     */
    double[] doubleFloorCeil(double a);

    /**
     * @param a float value
     * @param b float value
     * @return a / b
     */
    float floatDivision(float a, float b);

    /**
     * @param a float array
     * @return sum of array elements
     */
    float floatSum(float[] a);

    /**
     * @param a float value
     * @return floor and ceil of a
     */
    float[] floatFloorCeil(float a);

    /**
     * @param c character
     * @return uppercase of c
     */
    char charUppercase(char c);

    /**
     * @param c characters array
     * @return string from c
     */
    String charContactArray(char[] c);

    /**
     * @param string text value
     * @return uppercase of string
     */
    String stringUppercase(String string);

    /**
     * @param s string
     * @return chars of s
     */
    char[] stringSplitToChars(String s);

    /**
     * @param strings array
     * @param separator separator of array
     * @return single string
     */
    String stringContactArray(String[] strings, String separator);

    /**
     * @param strings string to split
     * @param separator by separator
     * @return string array
     */
    String[] stringSplit(String strings, String separator);

    /**
     * @param val string (null)
     * @return null
     */
    String nullResend(String val);

    /**
     * @param val array of null
     * @return array of null
     */
    String[] nullArray(String[] val);

    /**
     * @param val array of texts with nuls
     * @return array of texts with nuls
     */
    String[] nullArrayWith(String[] val);
}
