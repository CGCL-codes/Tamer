package vehikel.protocol.serial;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import vehikel.IUserInteraction;
import vehikel.protocol.serial.RxTxLoopConsitencyCheck;
import vehikel.testing.TestsRunner;

public class RxTxLoopCheckTest {

    @Test
    public void testCheck() {
        if (TestsRunner.skipIoTests) return;
        RxTxLoopConsitencyCheck checker = new RxTxLoopConsitencyCheck(new UserInteraction());
        boolean result = checker.check();
        assertTrue(result);
        assertTrue(checker.getRecognisedDevice().equals("/dev/ttyUSB0"));
    }

    private class UserInteraction implements IUserInteraction {

        public int informAndWait(String message) {
            System.out.println(message);
            return 0;
        }
    }
}
