package org.devyant.magicbeans;

import org.devyant.magicbeans.beans.MagicProperty;
import org.devyant.magicbeans.exceptions.MagicException;
import org.devyant.magicbeans.utils.Previewable;

/**
 * MagicComponent is a <b>cool</b> class.
 * 
 * @author Filipe Tavares
 * @version $Revision: 1.5 $ ($Author: ftavares $)
 * @param <C> The component to use for the binding
 * @since 18/Abr/2005 20:56:13
 */
public interface MagicComponent<C> extends Previewable {

    /**
     * Updates the original data container.
     * @throws MagicException Something went wrong
     */
    public void update() throws MagicException;

    /**
     * Binds the component with the object's property.
     * @param property The property
     * @throws MagicException Something went wrong
     */
    public void bindTo(final MagicProperty property) throws MagicException;

    /**
     * Getter method for the property.
     * @return The <code>MagicProperty</code> instance binded to this component
     */
    public MagicProperty getProperty();

    /**
     * Render the actual graphical component for the corresponding toolkit.
     * @return A GUI component
     * @throws MagicException Something went wrong
     */
    public C render() throws MagicException;

    /**
     * Whether this is a labeled component or not.
     * @return A boolean
     */
    public boolean isLabeled();

    /**
     * Is it a nested or isolated container?.
     * @return The answer
     */
    public boolean isNested();

    /**
     * The getter method for the property's name.
     * <p>This <code>String</code> will
     * be used for the component's label/title.</p>
     * @return A <code>String</code> that contains the name to be displayed
     */
    public String getName();

    /**
     * The setter method for this component's parent container.
     * @param parent The parent container
     */
    public void setParent(MagicContainer<?> parent);

    /**
     * The getter method for this component's parent container.
     * @return The parent container
     */
    public MagicContainer<?> getParent();
}
