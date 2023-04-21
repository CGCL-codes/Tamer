package test;

import org.junit.BeforeClass;
import testApp.TestApplication;

public class RiafTestCase {

    @BeforeClass
    public static void initGUI() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                TestApplication.main(new String[] {});
            }
        }).start();
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
