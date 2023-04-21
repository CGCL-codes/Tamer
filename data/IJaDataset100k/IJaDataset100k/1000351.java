package commons.crypto;

import java.text.ParseException;
import org.makagiga.commons.crypto.CryptoUtils;
import org.makagiga.test.Test;
import org.makagiga.test.TestMethod;
import org.makagiga.test.Tester;

@Test(className = CryptoUtils.class)
public final class TestCryptoUtils {

    @Test(methods = @TestMethod(name = "clear", parameters = "byte[]"))
    public void test_clear_byte() {
        CryptoUtils.clear((byte[]) null);
        CryptoUtils.clear(new byte[0]);
        byte[] array = new byte[] { 1, 2, 3 };
        CryptoUtils.clear(array);
        for (byte i : array) assert i == 0;
    }

    @Test(methods = @TestMethod(name = "clear", parameters = "char[]"))
    public void test_clear_char() {
        CryptoUtils.clear((char[]) null);
        CryptoUtils.clear(new char[0]);
        char[] array = new char[] { '1', '2', '3' };
        CryptoUtils.clear(array);
        for (char i : array) assert i == 0;
    }

    @Test(methods = @TestMethod(name = "createSalt", parameters = "int"))
    public void test_createSalt() {
        Tester.testException(NegativeArraySizeException.class, new Tester.Code() {

            public void run() throws Throwable {
                CryptoUtils.createSalt(-1);
            }
        });
        assert CryptoUtils.createSalt(0).length == 0;
        boolean ok = false;
        byte[] salt = CryptoUtils.createSalt(10);
        assert salt.length == 10;
        for (byte i : salt) {
            if (i != 0) {
                ok = true;
                break;
            }
        }
        assert ok;
    }

    @Test(methods = @TestMethod(name = "parseByteArray", parameters = "String"))
    public void test_parseByteArray() throws ParseException {
        assert CryptoUtils.parseByteArray(null).length == 0;
        assert CryptoUtils.parseByteArray("").length == 0;
        assert CryptoUtils.parseByteArray(",").length == 0;
        assert CryptoUtils.parseByteArray(",,").length == 0;
        byte[] array;
        array = CryptoUtils.parseByteArray("1");
        assert array.length == 1;
        assert array[0] == 1;
        array = CryptoUtils.parseByteArray("1,2,3");
        assert array.length == 3;
        assert array[0] == 1;
        assert array[1] == 2;
        assert array[2] == 3;
        array = CryptoUtils.parseByteArray("1,,2");
        assert array.length == 2;
        assert array[0] == 1;
        assert array[1] == 2;
        try {
            CryptoUtils.parseByteArray("foo");
        } catch (ParseException exception) {
            assert exception.getCause() instanceof NumberFormatException;
        }
        Tester.testParseException(new Tester.Code() {

            public void run() throws Throwable {
                CryptoUtils.parseByteArray("foo");
            }
        });
        Tester.testParseException(new Tester.Code() {

            public void run() throws Throwable {
                CryptoUtils.parseByteArray("1,foo");
            }
        });
    }

    @Test(methods = @TestMethod(name = "toString", parameters = "byte[]"))
    public void test_toString() {
        Tester.testNullPointerException(new Tester.Code() {

            public void run() throws Throwable {
                CryptoUtils.toString(null);
            }
        });
        assert CryptoUtils.toString(new byte[0]).isEmpty();
        assert CryptoUtils.toString(new byte[] { 1 }).equals("1");
        assert CryptoUtils.toString(new byte[] { 1, 2 }).equals("1,2");
        assert CryptoUtils.toString(new byte[] { 1, 2, 3 }).equals("1,2,3");
    }
}
