package org.gamegineer.common.ui.databinding.swing;

import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import javax.swing.SwingUtilities;
import net.jcip.annotations.ThreadSafe;
import org.eclipse.core.databinding.observable.Realm;
import org.gamegineer.common.internal.ui.Loggers;

/**
 * A data binding realm for a Swing event dispatch thread.
 */
@ThreadSafe
public final class SwingRealm extends Realm {

    /**
     * Initializes a new instance of the {@code SwingRealm} class.
     */
    private SwingRealm() {
    }

    @Override
    public void asyncExec(final Runnable runnable) {
        final Runnable safeRunnable = new Runnable() {

            @Override
            @SuppressWarnings("synthetic-access")
            public void run() {
                safeRun(runnable);
            }
        };
        SwingUtilities.invokeLater(safeRunnable);
    }

    /**
     * Executes the specified runnable from within the Swing system realm.
     * 
     * <p>
     * If this method is called from within the Swing system realm, the runnable
     * is executed directly; otherwise it executes the runnable from within the
     * Swing system realm and waits for it to complete.
     * </p>
     * 
     * @param runnable
     *        The runnable to execute; must not be {@code null}.
     */
    private static void execSystem(final Runnable runnable) {
        assert runnable != null;
        if (SwingUtilities.isEventDispatchThread()) {
            safeRun(runnable);
        } else {
            safeInvokeAndWait(runnable);
        }
    }

    public static SwingRealm getSystemRealm() {
        final AtomicReference<SwingRealm> systemRealm = new AtomicReference<SwingRealm>();
        execSystem(new Runnable() {

            @Override
            public void run() {
                systemRealm.set((SwingRealm) Realm.getDefault());
            }
        });
        return systemRealm.get();
    }

    /**
     * Installs a Swing realm on the system event dispatch thread.
     * 
     * <p>
     * This method does nothing if a realm has already been installed on the
     * system event dispatch thread.
     * </p>
     */
    public static void installSystemRealm() {
        execSystem(new Runnable() {

            @Override
            @SuppressWarnings("synthetic-access")
            public void run() {
                if (Realm.getDefault() == null) {
                    Realm.setDefault(new SwingRealm());
                }
            }
        });
    }

    @Override
    public boolean isCurrent() {
        return SwingUtilities.isEventDispatchThread();
    }

    /**
     * Executes the specified runnable on the system event dispatch thread and
     * waits for it to complete.
     * 
     * <p>
     * Any exception thrown from the runnable is logged and not re-thrown.
     * </p>
     * 
     * @param runnable
     *        The runnable to execute; must not be {@code null}.
     */
    private static void safeInvokeAndWait(final Runnable runnable) {
        assert runnable != null;
        try {
            SwingUtilities.invokeAndWait(runnable);
        } catch (final Exception e) {
            Loggers.getDefaultLogger().log(Level.SEVERE, NonNlsMessages.SwingRealm_safeInvokeAndWait_error, e);
        }
    }

    /**
     * Uninstalls the Swing realm from the system event dispatch thread.
     * 
     * <p>
     * This method does nothing if no realm is installed on the system event
     * dispatch thread.
     * </p>
     */
    public static void uninstallSystemRealm() {
        execSystem(new Runnable() {

            @Override
            @SuppressWarnings("synthetic-access")
            public void run() {
                Realm.setDefault(null);
            }
        });
    }
}
