package org.foo.jedit.extender;

import java.util.*;
import org.osgi.framework.*;

/**
 * This is a very simple bundle tracker utility class that tracks active
 * bundles. The tracker must be given a bundle context upon creation, which it
 * uses to listen for bundle events. The bundle tracker must be opened to track
 * objects and closed when it is no longer needed. This class is abstract, which
 * means in order to use it you must create a subclass of it. Subclasses must
 * implement the <tt>addedBundle()</tt> and <tt>removedBundle()</tt> methods,
 * which can be used to perform some custom action upon the activation or
 * deactivation of bundles. Since this tracker is quite simple, its concurrency
 * control approach is also simplistic. This means that subclasses should take
 * great care to ensure that their <tt>addedBundle()</tt> and
 * <tt>removedBundle()</tt> methods are very simple and do not do anything to
 * change the state of any bundles.
 **/
public abstract class BundleTracker {

    final Set m_bundleSet = new HashSet();

    final BundleContext m_context;

    final SynchronousBundleListener m_listener;

    boolean m_open;

    /**
   * Constructs a bundle tracker object that will use the specified bundle
   * context.
   * 
   * @param context The bundle context to use to track bundles.
   **/
    public BundleTracker(BundleContext context) {
        m_context = context;
        m_listener = new SynchronousBundleListener() {

            public void bundleChanged(BundleEvent evt) {
                synchronized (BundleTracker.this) {
                    if (!m_open) {
                        return;
                    }
                    if (evt.getType() == BundleEvent.STARTED) {
                        if (!m_bundleSet.contains(evt.getBundle())) {
                            m_bundleSet.add(evt.getBundle());
                            addedBundle(evt.getBundle());
                        }
                    } else if (evt.getType() == BundleEvent.STOPPED) {
                        if (m_bundleSet.contains(evt.getBundle())) {
                            m_bundleSet.remove(evt.getBundle());
                            removedBundle(evt.getBundle());
                        }
                    }
                }
            }
        };
    }

    /**
   * Returns the current set of active bundles.
   * 
   * @return The current set of active bundles.
   **/
    public synchronized Bundle[] getBundles() {
        return (Bundle[]) m_bundleSet.toArray(new Bundle[m_bundleSet.size()]);
    }

    /**
   * Call this method to start the tracking of active bundles.
   **/
    public synchronized void open() {
        if (!m_open) {
            m_open = true;
            m_context.addBundleListener(m_listener);
            Bundle[] bundles = m_context.getBundles();
            for (int i = 0; i < bundles.length; i++) {
                if (bundles[i].getState() == Bundle.ACTIVE) {
                    m_bundleSet.add(bundles[i]);
                    addedBundle(bundles[i]);
                }
            }
        }
    }

    /**
   * Call this method to stop the tracking of active bundles.
   **/
    public synchronized void close() {
        if (m_open) {
            m_open = false;
            m_context.removeBundleListener(m_listener);
            Bundle[] bundles = (Bundle[]) m_bundleSet.toArray(new Bundle[m_bundleSet.size()]);
            for (int i = 0; i < bundles.length; i++) {
                if (m_bundleSet.remove(bundles[i])) {
                    removedBundle(bundles[i]);
                }
            }
        }
    }

    /**
   * Subclasses must implement this method; it can be used to perform actions
   * upon the activation of a bundle. Subclasses should keep this method
   * implementation as simple as possible and should not cause the change in any
   * bundle state to avoid concurrency issues.
   * 
   * @param bundle The bundle being added to the active set.
   **/
    protected abstract void addedBundle(Bundle bundle);

    /**
   * Subclasses must implement this method; it can be used to perform actions
   * upon the deactivation of a bundle. Subclasses should keep this method
   * implementation as simple as possible and should not cause the change in any
   * bundle state to avoid concurrency issues.
   * 
   * @param bundle The bundle being removed from the active set.
   **/
    protected abstract void removedBundle(Bundle bundle);
}
