package java.lang;

import junit.framework.TestCase;

/**
 * ###############################################################################
 * ###############################################################################
 * TODO: 1.
 * ###############################################################################
 * ###############################################################################
 */
public class RuntimeAdditionalTest28 extends TestCase {

    /**
     * wait for (via exitValue loop) finish of the java process then destroy,
     * get jvm process' streams, read err stream, then exitValue
     */
    public void test_28() {
        System.out.println("==test_28===");
        if (RuntimeAdditionalTest0.os.equals("Unk")) {
            fail("WARNING (test_28): unknown operating system.");
        }
        try {
            String cmnd = RuntimeAdditionalTest0.javaStarter + " MAIN";
            Process pi3 = Runtime.getRuntime().exec(cmnd);
            while (true) {
                try {
                    Thread.sleep(50);
                    pi3.exitValue();
                    break;
                } catch (IllegalThreadStateException e) {
                    continue;
                }
            }
            pi3.destroy();
            Thread.sleep(100);
            pi3.getOutputStream();
            java.io.InputStream es = pi3.getErrorStream();
            pi3.getInputStream();
            pi3.waitFor();
            int ia;
            while (true) {
                while ((ia = es.available()) != 0) {
                    byte[] bbb = new byte[ia];
                    es.read(bbb);
                }
                try {
                    pi3.exitValue();
                    while ((ia = es.available()) != 0) {
                        byte[] bbb = new byte[ia];
                        es.read(bbb);
                    }
                    break;
                } catch (IllegalThreadStateException e) {
                    continue;
                }
            }
            pi3.exitValue();
        } catch (Exception eeee) {
            eeee.printStackTrace();
            fail("ERROR (test_28): unexpected exception.");
        }
    }
}
