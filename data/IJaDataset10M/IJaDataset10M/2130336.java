package org.carabiner.harness;

import java.awt.Component;
import java.awt.Window;

/**
 * 
 * A harness used for testing dialogs, frames, and other swing objects that
 * extends from the <code>Window</code> class.
 * 
 * <p>
 * Carabiner Testing Framework
 * </p>
 * <p>
 * Copyright: <a href="http://www.gnu.org/licenses/gpl.html">GNU Public License</a>
 * </p>
 * 
 * @author Ben Rady (benrady@gmail.com)
 * 
 */
class WindowHarness extends AbstractHarness {

    private Window window;

    protected WindowHarness(Window testableWindow) {
        super(testableWindow);
    }

    protected void attach(Component testSubject) {
        window = (Window) testSubject;
        super.attach(window);
        getFrame().pack();
    }

    public void dispose() {
        super.dispose();
        if (window != null) {
            window.dispose();
        }
    }

    public void showHarness() {
        window.setVisible(true);
        super.showHarness();
    }
}
