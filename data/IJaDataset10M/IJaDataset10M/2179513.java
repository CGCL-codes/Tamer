package net.sf.javarisk.three.event;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.swing.event.EventListenerList;
import net.sf.javarisk.three.game.GameField;
import net.sf.javarisk.tools.GeneralTools;

/**
 * <p>
 * The <code>FieldChangedPublisher</code> is meant to be the central class for registering {@link FieldChangedListener}s
 * and firing {@link FieldEvent}s.
 * </p>
 * 
 * @author <a href='mailto:sebastiankirsch@users.sourceforge.net'>Sebastian Kirsch</a>
 * @version 0.1; $Rev: 243 $
 * @since 0.1
 */
public class FieldChangedPublisher {

    private static final Logger LOG = Logger.getLogger(FieldChangedPublisher.class.getName());

    private final EventListenerList listeners = new EventListenerList();

    public void addFieldChangedListener(FieldChangedListener listener) {
        this.listeners.add(FieldChangedListener.class, GeneralTools.notNull(listener, "listener"));
        if (LOG.isLoggable(Level.FINE)) {
            LOG.log(Level.FINE, "FieldListener added.", listener);
        }
    }

    public void fireFieldChangedEvent(@Nullable GameField field) {
        FieldEvent event = new FieldEvent(this, field);
        for (FieldChangedListener listener : this.listeners.getListeners(FieldChangedListener.class)) {
            listener.fieldChanged(event);
        }
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.log(Level.FINEST, "Fired field change.", event);
        }
    }

    public void fireFieldChangedEvents(@Nonnull Iterable<GameField> fields) {
        for (GameField field : fields) fireFieldChangedEvent(field);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " having " + this.listeners.getListenerCount() + " listeners registerd";
    }
}
