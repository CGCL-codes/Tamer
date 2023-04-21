package ch.jester.common.test.internal;

import org.osgi.framework.BundleContext;
import ch.jester.common.activator.AbstractActivator;

/**
 * Erbt von AbstractActivator, so dass in den Tests nicht alle Zugriffe auf
 * Services manuell passieren muss.
 * 
 */
public class TestActivator extends AbstractActivator {

    private static TestActivator mActivator;

    public TestActivator() {
    }

    public static TestActivator getActivator() {
        return mActivator;
    }

    @Override
    public void startDelegate(BundleContext context) {
        mActivator = this;
    }

    @Override
    public void stopDelegate(BundleContext context) {
    }
}
