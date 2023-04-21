package java.lang;

import junit.framework.TestCase;

/**
 * ###############################################################################
 * ###############################################################################
 * TODO: 1.
 * ###############################################################################
 * ###############################################################################
 */
public class RuntimeAdditionalTest23 extends TestCase {

    /**
     * get jvm process' streams, wait for finish of the process then read input
     * stream, then exitValue
     */
    public void test_23() {
        System.out.println("==test_23===");
        if (RuntimeAdditionalTest0.os.equals("Unk")) {
            fail("WARNING (test_23): unknown operating system.");
        }
        try {
            String cmnd = RuntimeAdditionalTest0.javaStarter;
            Process pi3 = Runtime.getRuntime().exec(cmnd);
            pi3.getOutputStream();
            pi3.getErrorStream();
            java.io.InputStream is = pi3.getInputStream();
            while (true) {
                try {
                    Thread.sleep(50);
                    pi3.exitValue();
                    break;
                } catch (IllegalThreadStateException e) {
                    continue;
                }
            }
            int ia;
            while (true) {
                while ((ia = is.available()) != 0) {
                    byte[] bbb = new byte[ia];
                    is.read(bbb);
                }
                try {
                    pi3.exitValue();
                    while ((ia = is.available()) != 0) {
                        byte[] bbb = new byte[ia];
                        is.read(bbb);
                    }
                    break;
                } catch (IllegalThreadStateException e) {
                    continue;
                }
            }
            pi3.exitValue();
        } catch (Exception eeee) {
            eeee.printStackTrace();
            fail("ERROR (test_23): unexpected exception.");
        }
    }
}
