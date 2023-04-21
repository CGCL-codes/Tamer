package android.graphics;

import junit.framework.TestCase;
import android.graphics.Bitmap;
import android.test.suitebuilder.annotation.LargeTest;

public class ThreadBitmapTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
    }

    @LargeTest
    public void testCreation() {
        for (int i = 0; i < 200; i++) {
            new MThread().start();
        }
    }

    class MThread extends Thread {

        public Bitmap b;

        public MThread() {
            b = Bitmap.createBitmap(300, 300, Bitmap.Config.RGB_565);
        }

        public void run() {
        }
    }
}
