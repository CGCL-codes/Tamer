package org.gamegineer.table.core;

import java.awt.Point;
import java.util.List;

/**
 * A table component that can contain other table components.
 * 
 * @noextend This interface is not intended to be extended by clients.
 * 
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IContainer extends IComponent {

    /**
     * Adds the specified component to the top of this container.
     * 
     * @param component
     *        The component; must not be {@code null}.
     * 
     * @throws java.lang.IllegalArgumentException
     *         If {@code component} is already contained in a container or if
     *         {@code component} was created by a table other than the table
     *         that created this container.
     * @throws java.lang.NullPointerException
     *         If {@code component} is {@code null}.
     */
    public void addComponent(IComponent component);

    /**
     * Adds the specified collection of components to the top of this container.
     * 
     * @param components
     *        The collection of components to be added to this container; must
     *        not be {@code null}. The components are added to the top of this
     *        container in the order they appear in the collection.
     * 
     * @throws java.lang.IllegalArgumentException
     *         If {@code components} contains a {@code null} element; if any
     *         component is already contained in a container; or if any
     *         component was created by a table other than the table that
     *         created this container.
     * @throws java.lang.NullPointerException
     *         If {@code components} is {@code null}.
     */
    public void addComponents(List<IComponent> components);

    public IComponent getComponent(int index);

    public IComponent getComponent(Point location);

    /**
     * Gets the count of components in this container.
     * 
     * @return The count of components in this container.
     */
    public int getComponentCount();

    /**
     * Gets the index of the specified component in this container.
     * 
     * @param component
     *        The component; must not be {@code null}.
     * 
     * @return The index of the specified component in this container.
     * 
     * @throws java.lang.IllegalArgumentException
     *         If {@code component} is not contained in this container.
     * @throws java.lang.NullPointerException
     *         If {@code component} is {@code null}.
     */
    public int getComponentIndex(IComponent component);

    public List<IComponent> getComponents();

    public IComponent removeComponent();

    public List<IComponent> removeComponents();

    public List<IComponent> removeComponents(Point location);
}
