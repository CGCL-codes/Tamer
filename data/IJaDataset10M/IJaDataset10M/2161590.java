package mw.client.utils;

public class TimeUtil {

    public static void wait(int millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
