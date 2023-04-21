package org.dubh.common.collect;

import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import static com.google.common.collect.Lists.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class AuditedWriteListTests {

    @Test(expected = ConcurrentModificationException.class)
    public void testConcurrentModification() {
        List<String> list = createList();
        iterateBadly(list);
    }

    @Test
    public void testConcurrentModificationWithAudit() {
        try {
            List<String> list = Lists.newAuditedWriteList(createList());
            iterateBadly(list);
        } catch (ConcurrentModificationException e) {
            e.printStackTrace();
            assertNotNull(e.getCause());
        }
    }

    private void iterateBadly(List<String> list) {
        for (Iterator<String> i = list.iterator(); i.hasNext(); ) {
            i.next();
            list.add("Four");
        }
    }

    private List<String> createList() {
        return newArrayList("One", "Two", "Three");
    }

    public static void main(String[] args) throws Throwable {
        new AuditedWriteListTests().testMultipleThreadModification();
    }

    @Test
    public void testMultipleThreadModification() throws Throwable {
        final List<String> list = Collections.synchronizedList(Lists.newAuditedWriteList(createList()));
        ThrowableCatchingRunnable r = new ThrowableCatchingRunnable() {

            public void runImpl() {
                for (int i = 0; i < 250000; i++) {
                    Iterator<String> iterator = list.iterator();
                    synchronized (list) {
                        while (iterator.hasNext()) iterator.next();
                    }
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
        for (int i = 0; i < 250000; i++) {
            list.add("Foo");
        }
        t.join();
        assertNotNull(r.throwable().getCause());
    }

    private abstract static class ThrowableCatchingRunnable implements Runnable {

        private Throwable throwable;

        public final void run() {
            try {
                runImpl();
            } catch (Throwable t) {
                synchronized (this) {
                    this.throwable = t;
                }
            }
        }

        protected abstract void runImpl();

        public synchronized Throwable throwable() {
            return throwable;
        }
    }
}
