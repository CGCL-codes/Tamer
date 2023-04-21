package jtm.demos;

import com.sun.jna.Pointer;
import jtm.jna.CallbackFunction;
import jtm.jna.FSEventsLib;

/**
 * Created by IntelliJ IDEA.
 * User: tgleason
 * Date: Apr 23, 2008
 * Time: 7:24:32 PM
 * To change this template use File | Settings | File Templates.
 */
public class FSEventsTest {

    public static void main(String[] args) throws Exception {
        Pointer p = FSEventsLib.INSTANCE.CFStringCreateWithCString(Pointer.NULL, "/Users/tgleason", 0x0600);
        System.out.println("p: " + p);
        Pointer paths = FSEventsLib.INSTANCE.CFArrayCreateMutable(Pointer.NULL, 1, Pointer.NULL);
        FSEventsLib.INSTANCE.CFArrayAppendValue(paths, p);
        CallbackFunction callbackFunction = new CallbackFunction() {

            public void callback(Pointer p1, Pointer p2, int n, Pointer paths, Pointer flags1, Pointer flags2) {
                System.out.println("Hello!");
                Pointer ps[] = paths.getPointerArray(0L, n);
                long ids[] = flags2.getLongArray(0l, n);
                for (int i = 0; i < ps.length; i++) {
                    System.out.println(ids[i] + " - File: " + ps[i].getString(0l));
                }
            }
        };
        Pointer stream = FSEventsLib.INSTANCE.FSEventStreamCreate(Pointer.NULL, callbackFunction, Pointer.NULL, paths, -1L, 3, 0.0);
        FSEventsLib.INSTANCE.FSEventStreamScheduleWithRunLoop(stream, FSEventsLib.INSTANCE.CFRunLoopGetCurrent(), FSEventsLib.INSTANCE.CFStringCreateWithCString(Pointer.NULL, "kCFRunLoopDefaultMode", 0x0600));
        FSEventsLib.INSTANCE.FSEventStreamStart(stream);
        FSEventsLib.INSTANCE.CFRunLoopRun();
    }
}
