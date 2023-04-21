package com.hp.hpl.mars.portal.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import com.hp.hpl.mars.portal.component.LifecycleEvent;
import com.hp.hpl.mars.portal.component.LifecycleEventListener;
import com.hp.hpl.mars.portal.component.LifecycleManager;
import com.hp.hpl.mars.portal.component.LifecycleManagerHelper;

/**
 *
 */
public class LifecycleManagerHelperTest implements LifecycleManager {

    LifecycleEvent.Kind kind;

    int count;

    @Test
    public void add() {
        kind = LifecycleEvent.Kind.execute;
        for (int num = 0; num < 3; num++) {
            LifecycleManagerHelper helper = new LifecycleManagerHelper();
            count = 0;
            for (int i = 0; i < num; i++) {
                helper.addLifecycleEventListener(new MockLifecycleEventListener());
            }
            helper.announce(this, kind);
            assertTrue(count == num);
        }
    }

    @Test
    public void remove() {
        kind = LifecycleEvent.Kind.execute;
        LifecycleEventListener listener;
        LifecycleManagerHelper helper = new LifecycleManagerHelper();
        count = 0;
        for (int i = 0; i < 10; i++) {
            listener = new MockLifecycleEventListener();
            helper.addLifecycleEventListener(listener);
            if ((i & 1) == 0) {
                helper.removeLifecycleEventListener(listener);
            }
        }
        helper.announce(this, kind);
        assertTrue(count == 5);
    }

    class MockLifecycleEventListener implements LifecycleEventListener {

        public void lifecycleEvent(LifecycleEvent event) {
            assertEquals(kind, event.kind);
            count++;
        }
    }

    public void addLifecycleEventListener(LifecycleEventListener listener) {
    }

    public void removeLifecycleEventListener(LifecycleEventListener listener) {
    }
}
