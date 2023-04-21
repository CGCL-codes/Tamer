package net.sf.matrixappender.ui.keylistener;

import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

public class ExitListener implements CombinationKeyListener {

    private Set<Integer> keys;

    @Override
    public Set<Integer> getKeyCodes() {
        if (keys == null) {
            keys = new HashSet<Integer>();
            keys.add(KeyEvent.VK_Q);
            keys.add(KeyEvent.VK_U);
            keys.add(KeyEvent.VK_I);
            keys.add(KeyEvent.VK_T);
        }
        return keys;
    }

    @Override
    public void onEvent() {
        System.exit(0);
    }
}
