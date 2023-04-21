package org.didicero.rap.base.controls;

import java.util.*;
import org.eclipse.rwt.SessionSingletonBase;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

final class DefaultButtonManager extends SessionSingletonBase {

    static final class ChangeEvent extends EventObject {

        private static final long serialVersionUID = 1L;

        public ChangeEvent(final Shell source) {
            super(source);
        }
    }

    static interface ChangeListener extends EventListener {

        void defaultButtonChanged(ChangeEvent event);
    }

    static DefaultButtonManager getInstance() {
        return (DefaultButtonManager) getInstance(DefaultButtonManager.class);
    }

    private Set changeListeners = new HashSet();

    private DefaultButtonManager() {
    }

    void change(final Shell shell, final Button defaultButton) {
        shell.setDefaultButton(defaultButton);
        if (changeListeners.size() > 0) {
            ChangeListener[] listeners = new ChangeListener[changeListeners.size()];
            changeListeners.toArray(listeners);
            ChangeEvent event = new ChangeEvent(shell);
            for (int i = 0; i < listeners.length; i++) {
                listeners[i].defaultButtonChanged(event);
            }
        }
    }

    void addChangeListener(final ChangeListener listener) {
        changeListeners.add(listener);
    }

    void removeChangeListener(final ChangeListener listener) {
        changeListeners.remove(listener);
    }
}
