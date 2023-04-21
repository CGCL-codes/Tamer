package java.lang;

import junit.framework.TestCase;

/**
 * ###############################################################################
 * ###############################################################################
 * TODO: 1.
 * ###############################################################################
 * ###############################################################################
 */
public class RuntimeAdditionalTest25 extends TestCase {

    /**
     * get jvm process' streams, waitFor finish of the process, waitFor,
     * exitValue, destroy, waitFor then read err stream, then exitValue
     */
    public void test_25() {
        System.out.println("==test_25===");
        if (RuntimeAdditionalTest0.os.equals("Unk")) {
            fail("WARNING (test_25): unknown operating system.");
        }
        try {
            String cmnd = RuntimeAdditionalTest0.javaStarter + " MAIN";
            Process pi3 = Runtime.getRuntime().exec(cmnd);
            pi3.getOutputStream();
            java.io.InputStream es = pi3.getErrorStream();
            pi3.getInputStream();
            pi3.waitFor();
            pi3.waitFor();
            pi3.exitValue();
            pi3.destroy();
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
            fail("ERROR (test_25): unexpected exception.");
        }
    }
}
