package org.ugr.bluerose.android.test;

import org.ugr.bluerose.ObjectProxy;
import org.ugr.bluerose.events.Event;
import org.ugr.bluerose.events.EventHandler;
import org.ugr.bluerose.events.EventListener;
import org.ugr.bluerose.events.Value;
import android.util.Log;

class TestEventListener extends EventListener {

    protected int k = 0;

    public TestEventListener() {
        super(1);
    }

    @Override
    public void performAction(Event evt) {
        Value x = evt.getMemberValue("x");
        Value y = evt.getMemberValue("y");
        k++;
    }
}

public class TestAuxiliary {

    public static void testObjects() throws Exception {
        java.util.Vector<ObjectProxy> test_objs = new java.util.Vector<ObjectProxy>();
        long inicio = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            TestProxy test = null;
            test = new TestProxy();
            if (test != null) test_objs.add(test);
        }
        long fin = System.currentTimeMillis();
    }

    public static void testLocal() {
        int result = 0;
        for (int i = 0; i < 100; i++) {
            result = i + i + 1;
        }
    }

    public static void testPubSub() {
        EventListener evt_listener = new TestEventListener();
        EventHandler.addEventListener(evt_listener);
        Event evt = new Event();
        evt.topic = 1;
        java.util.Random rand = new java.util.Random(System.currentTimeMillis());
        long ini = System.currentTimeMillis();
        while (true) {
            Value x = new Value();
            x.setFloat(rand.nextFloat());
            Value y = new Value();
            y.setFloat(rand.nextFloat());
            evt.setMember("x", x);
            evt.setMember("y", y);
            EventHandler.publish(evt);
            long fin = System.currentTimeMillis();
            if (((fin - ini) / 1000.0) >= 1.0) break;
        }
        EventHandler.removeEventListener(evt_listener);
    }
}
