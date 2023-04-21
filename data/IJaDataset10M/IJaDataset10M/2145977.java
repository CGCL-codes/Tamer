package org.devyant.magicbeans.layout;

import org.devyant.magicbeans.MagicComponent;
import org.devyant.magicbeans.MagicLayout;
import org.devyant.magicbeans.MagicUtils;
import org.devyant.magicbeans.exceptions.MagicException;

/**
 * AbstractMagicLayout is a <b>cool</b> class.
 * 
 * @author ftavares
 * @version $Revision: 1.3 $ $Date: 2006/04/21 15:26:40 $ ($Author: ftavares $)
 * @param <T> The most basic type of this toolkit's components
 * @since Mar 23, 2006 3:07:49 PM
 */
public abstract class AbstractMagicLayout<T> implements MagicLayout<T> {

    /**
     * The behaviour to use for the handling of isolated components.
     */
    private LayoutIsolatedBehaviour<T> ibehaviour;

    /**
     * @see org.devyant.magicbeans.MagicLayout#addLabeledIsolatedComponent(java.lang.Object, java.lang.Object, org.devyant.magicbeans.MagicComponent)
     */
    public final void addLabeledIsolatedComponent(T container, T label, MagicComponent<? extends T> component) throws MagicException {
        if (MagicUtils.mayBeIsolated(component)) {
            this.ibehaviour.addLabeledIsolatedComponent(container, label, component);
        } else {
            addLabeledComponent(container, label, component);
        }
    }

    /**
     * @see org.devyant.magicbeans.MagicLayout#setIsolatedBehaviour(org.devyant.magicbeans.layout.LayoutIsolatedBehaviour)
     */
    public void setIsolatedBehaviour(LayoutIsolatedBehaviour<T> behaviour) {
        this.ibehaviour = behaviour;
    }
}
