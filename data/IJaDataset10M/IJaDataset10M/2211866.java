package android.os.cts;

import junit.framework.TestCase;
import android.os.RemoteException;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;

@TestTargetClass(RemoteException.class)
public class RemoteExceptionTest extends TestCase {

    @TestTargetNew(level = TestLevel.COMPLETE, method = "RemoteException", args = {  })
    public void testRemoteException() throws Exception {
        new RemoteException();
    }
}
