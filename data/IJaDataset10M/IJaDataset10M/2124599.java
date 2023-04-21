package jimo.osgi.impl.framework.dependancy;

import jimo.osgi.impl.framework.FrameworkImpl;
import org.osgi.framework.FrameworkListener;

public class FrameworkListenerDependent implements BundleDependent {

    FrameworkListener listener;

    public FrameworkListenerDependent(FrameworkListener listener) {
        this.listener = listener;
    }

    public Object getDependant() {
        return listener;
    }

    public boolean stop() {
        FrameworkImpl.INSTANCE.removeFrameworkListener(listener);
        return true;
    }

    public void uninstall() {
    }

    public void remove() {
    }
}
