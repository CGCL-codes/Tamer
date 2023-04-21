package org.zkoss.zktest.test;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventThreadInit;

/**
 * A test of NPE in EventThreadInit.
 *
 * @author tomyeh
 */
public class NPEEventThreadInit implements EventThreadInit {

    private static boolean _once;

    public void prepare(Component comp, Event evt) throws Exception {
        if (!_once) {
            _once = true;
            throw new NullPointerException("EventThreadInit.prepare failed");
        }
    }

    public boolean init(Component comp, Event evt) throws Exception {
        return true;
    }
}
