package gnu.testlet.java2.lang.String;

import gnu.testlet.Testlet;
import gnu.testlet.TestHarness;
import java.io.UnsupportedEncodingException;

public class decode implements Testlet {

    public void test(TestHarness harness) {
        char[] cstr = { 'a', 'b', 'c', '\t', 'A', 'B', 'C', ' ', '1', '2', '3' };
        byte[] bstr = new byte[cstr.length];
        for (int i = 0; i < cstr.length; ++i) bstr[i] = (byte) cstr[i];
        String a = new String(bstr);
        String a_utf8 = "";
        String b = new String(bstr, 3, 3);
        String b_utf8 = "";
        String c = "";
        String d = "";
        try {
            a_utf8 = new String(bstr, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
        }
        try {
            b_utf8 = new String(bstr, 3, 3, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
        }
        try {
            c = new String(bstr, "8859_1");
        } catch (UnsupportedEncodingException ex) {
        }
        try {
            d = new String(bstr, 3, 3, "8859_1");
        } catch (UnsupportedEncodingException ex) {
        }
        harness.check(a, "abc	ABC 123");
        harness.check(a_utf8, "abc	ABC 123");
        harness.check(b, "	AB");
        harness.check(b_utf8, "	AB");
        harness.check(c, "abc	ABC 123");
        harness.check(d, "	AB");
        boolean ok = false;
        try {
            c = new String(bstr, "foobar8859_1");
        } catch (UnsupportedEncodingException ex) {
            ok = true;
        }
        harness.check(ok);
        ok = false;
        try {
            d = new String(bstr, 3, 3, "foobar8859_1");
        } catch (UnsupportedEncodingException ex) {
            ok = true;
        }
        harness.check(ok);
        harness.check(String.copyValueOf(cstr), "abc	ABC 123");
        harness.check(String.copyValueOf(cstr, 3, 3), "	AB");
        byte[] leWithBOM = new byte[] { (byte) 0xFF, (byte) 0xFE, (byte) 'a', (byte) 0x00 };
        byte[] leWithoutBOM = new byte[] { (byte) 'a', (byte) 0x00 };
        byte[] beWithBOM = new byte[] { (byte) 0xFE, (byte) 0xFF, (byte) 0x00, (byte) 'a' };
        byte[] beWithoutBOM = new byte[] { (byte) 0x00, (byte) 'a' };
        harness.check(decodeTest(leWithBOM, "UTF-16", "a"));
        harness.check(!decodeTest(leWithoutBOM, "UTF-16", "a"));
        harness.check(decodeTest(beWithBOM, "UTF-16", "a"));
        harness.check(decodeTest(beWithoutBOM, "UTF-16", "a"));
        harness.check(!decodeTest(leWithBOM, "UTF-16LE", "a"));
        harness.check(decodeTest(leWithoutBOM, "UTF-16LE", "a"));
        harness.check(!decodeTest(beWithBOM, "UTF-16LE", "a"));
        harness.check(!decodeTest(beWithoutBOM, "UTF-16LE", "a"));
        harness.check(!decodeTest(leWithBOM, "UTF-16BE", "a"));
        harness.check(!decodeTest(leWithoutBOM, "UTF-16BE", "a"));
        harness.check(!decodeTest(beWithBOM, "UTF-16BE", "a"));
        harness.check(decodeTest(beWithoutBOM, "UTF-16BE", "a"));
        harness.check(decodeTest(leWithBOM, "UnicodeLittle", "a"));
        harness.check(decodeTest(leWithoutBOM, "UnicodeLittle", "a"));
        harness.check(!decodeTest(beWithBOM, "UnicodeLittle", "a"));
        harness.check(!decodeTest(beWithoutBOM, "UnicodeLittle", "a"));
    }

    public boolean decodeTest(byte[] bytes, String encoding, String expected) {
        try {
            String s = new String(bytes, encoding);
            return s.equals(expected);
        } catch (UnsupportedEncodingException ex) {
            return false;
        }
    }
}
