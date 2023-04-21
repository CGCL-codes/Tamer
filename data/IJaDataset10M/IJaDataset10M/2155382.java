package yocef;

import org.junit.Assert;
import org.junit.Test;
import yocef.client.Event;
import yocef.client.EventContext;

@SuppressWarnings({ "nls", "static-method" })
public class EventContextTest {

    /**
	 * Test method for
	 * {@link yocef.client.EventContext#put(java.lang.String, java.lang.Object)}
	 * . and {@link yocef.client.EventContext#get(java.lang.String)}.
	 */
    @Test
    public void testPutGet() {
        final EventContext ctx = new EventContext().put("test", null);
        Assert.assertNull(ctx.get("test"));
        final Object element1 = new Object();
        ctx.put("test", element1);
        Assert.assertSame(element1, ctx.get("test"));
        final Object element2 = new Object();
        ctx.put("test", element2);
        Assert.assertSame(element2, ctx.get("test"));
        Event.cleanup(element1);
        Event.cleanup(element2);
    }

    /**
	 */
    @Test
    public void testRemove() {
        final EventContext ctx = new EventContext().put("test", new Object());
        ctx.remove("test");
        Assert.assertNull(ctx.get("test"));
        ctx.remove("other");
    }

    /**
	 * Test method for {@link yocef.client.Event#cleanup(Object)}.
	 */
    @Test
    public void testCleanup1() {
        Assert.assertEquals(0, refCount);
        Object o1 = new ObjectForGC();
        Object o2 = new ObjectForGC();
        final EventContext ctx1 = new EventContext().put("k1", o1);
        final EventContext ctx2 = new EventContext().put("k1", o1).put("k2", o2);
        ctx1.remove("k1");
        ctx2.remove("k1");
        ctx2.remove("k2");
        Event.cleanup(o1);
        Event.cleanup(o2);
        o1 = null;
        o2 = null;
        System.gc();
        System.runFinalization();
        Assert.assertEquals(0, refCount);
    }

    @Test
    public void testCleanup2() {
        Assert.assertEquals(0, refCount);
        Object o1 = new ObjectForGC();
        Object o2 = new ObjectForGC();
        new EventContext().put("k1", o1).put("k2", o2);
        Event.cleanup(o1);
        Event.cleanup(o2);
        o1 = null;
        o2 = null;
        System.gc();
        System.runFinalization();
        Assert.assertEquals(0, refCount);
    }

    static int refCount = 0;

    class ObjectForGC {

        ObjectForGC() {
            super();
            refCount++;
        }

        @Override
        protected void finalize() throws Throwable {
            refCount--;
            super.finalize();
        }
    }
}
