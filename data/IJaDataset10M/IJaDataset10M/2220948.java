package jaxlib.swing.progress;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import jaxlib.event.ConcurrentListenerList;

/**
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: AbstractProgressSource.java 2696 2009-01-05 05:38:40Z joerg_wassmer $
 */
public abstract class AbstractProgressSource extends Object implements ProgressSource {

    private final ConcurrentListenerList<ChangeListener> changeListeners;

    private final ChangeEvent event;

    private final Executor eventExecutor;

    private final AbstractProgressSource.Notifier notifier;

    protected AbstractProgressSource() {
        this(null);
    }

    protected AbstractProgressSource(final Executor eventExecutor) {
        super();
        this.changeListeners = new ConcurrentListenerList<ChangeListener>(ChangeListener.class);
        this.event = new ChangeEvent(this);
        this.eventExecutor = eventExecutor;
        this.notifier = (eventExecutor == null) ? null : new Notifier(this.event, this);
    }

    protected void fireStateChanged() {
        final ChangeListener[] listeners = this.changeListeners.get();
        if (listeners != null) {
            for (int i = 0; i < listeners.length; i++) listeners[i].stateChanged(this.event);
        }
    }

    protected void stateChanged() {
        if (this.notifier == null) fireStateChanged(); else if (!this.changeListeners.isEmpty()) this.notifier.schedule(this.eventExecutor);
    }

    @Override
    public void addChangeListener(final ChangeListener listener) {
        this.changeListeners.add(listener);
    }

    @Override
    public void removeChangeListener(final ChangeListener listener) {
        this.changeListeners.remove(listener);
    }

    private static final class Notifier extends Object implements Runnable {

        private final AtomicBoolean queued;

        private final ChangeEvent event;

        private final AbstractProgressSource model;

        Notifier(final ChangeEvent event, final AbstractProgressSource model) {
            super();
            this.event = event;
            this.model = model;
            this.queued = new AtomicBoolean();
        }

        final void schedule(final Executor executor) {
            if (this.queued.compareAndSet(false, true)) executor.execute(this);
        }

        @Override
        public final void run() {
            if (this.queued.compareAndSet(true, false)) this.model.fireStateChanged();
        }
    }
}
