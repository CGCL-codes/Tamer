package net.sf.javarisk.three.event;

import java.util.EventListener;
import javax.annotation.Nonnull;

/**
 * <p>
 * The listener interface for receiving <code>FieldEvent</code>s denoting that a <code>GameField</code>'s properties
 * have changed.
 * </p>
 * 
 * @author <a href='mailto:sebastiankirsch@users.sourceforge.net'>Sebastian Kirsch</a>
 * @version 0.1; $Rev: 81 $
 * @since 0.1
 */
public interface FieldChangedListener extends EventListener {

    /**
     * Invoked when any property of a field has changed.
     * 
     * @param fieldEvent
     *            the <code>FieldEvent</code> identifying the changed <code>GameField</code>
     * @since 0.1
     */
    public abstract void fieldChanged(@Nonnull FieldEvent fieldEvent);
}
