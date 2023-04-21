package org.rococoa;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.rococoa.cocoa.foundation.NSAutoreleasePool;
import org.rococoa.test.RococoaTestCase;

@SuppressWarnings("nls")
public class FoundationRetainReleaseTest extends RococoaTestCase {

    @Test
    public void test() {
        ID idOfString = Foundation.cfString("Hello world");
        assertRetainCount(1, idOfString);
        assertEquals(idOfString, Foundation.cfRetain(idOfString));
        assertRetainCount(2, idOfString);
        Foundation.cfRelease(idOfString);
        assertRetainCount(1, idOfString);
        Foundation.cfRelease(idOfString);
    }

    @Test
    public void testAutorelease() {
        NSAutoreleasePool pool = NSAutoreleasePool.new_();
        ID idOfString = Foundation.cfString("Hello world");
        assertRetainCount(1, idOfString);
        Foundation.sendReturnsVoid(idOfString, "autorelease");
        assertRetainCount(1, idOfString);
        Foundation.sendReturnsVoid(idOfString, "retain");
        assertRetainCount(2, idOfString);
        pool.drain();
        assertRetainCount(1, idOfString);
        Foundation.cfRelease(idOfString);
    }

    @Test
    public void testInitedObject() {
        NSAutoreleasePool pool = NSAutoreleasePool.new_();
        ID idOfClass = Foundation.getClass("NSString");
        ID idOfString = Foundation.sendReturnsID(idOfClass, "alloc");
        idOfString = Foundation.sendReturnsID(idOfString, "initWithCString:", "Hello world");
        assertRetainCount(1, idOfString);
        pool.drain();
        assertRetainCount(1, idOfString);
        Foundation.cfRelease(idOfString);
    }
}
