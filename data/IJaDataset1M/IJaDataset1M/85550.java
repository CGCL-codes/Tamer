package pl.voidsystems.yajf.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import pl.voidsystems.yajf.servlet.ComponentStructureException;

/**
 * ArrayList of components with extended functionality.
 *
 */
public class ComponentArrayList extends ArrayList<IComponent> {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -4089289404758461744L;

    /**
     * @uml.property  name="parent"
     * @uml.associationEnd  
     */
    protected IComponent parent = null;

    /**
     * Getter of <code>parent</code> property.
     * @return  Returns the <code>parent</code>.
     * @uml.property  name="parent"
     */
    public IComponent getParent() {
        return this.parent;
    }

    /**
     * Sets to all components same parent equal to <code>parent</code> property.
     * @param parent  The parent to set.
     * @uml.property  name="parent"
     */
    public void setParent(IComponent parent) {
        this.parent = parent;
        if (parent != null) {
            for (Iterator iter = this.iterator(); iter.hasNext(); ) {
                IComponent element = (IComponent) iter.next();
                element.setParent(this.parent);
            }
        }
    }

    /**
     * This method runs java.util.ArrayList#add(int, E)
     * @see java.util.ArrayList#add(int, E)
     */
    @Override
    public void add(int index, IComponent comp) {
        if (this.parent != null) {
            comp.setParent(this.parent);
        }
        super.add(index, comp);
    }

    /**
     * This method runs java.util.ArrayList#add(E)
     * @see java.util.ArrayList#add(E)
     */
    @Override
    public boolean add(IComponent component) {
        component.setParent(this.parent);
        return super.add(component);
    }

    /**
     * This method runs java.util.ArrayList#addAll(java.util.Collection)
     * @see java.util.ArrayList#addAll(java.util.Collection)
     */
    @Override
    public boolean addAll(Collection<? extends IComponent> coll) {
        for (IComponent component : coll) {
            if (this.parent != null) {
                component.setParent(this.parent);
            }
        }
        return super.addAll(coll);
    }

    /**
     * This method runs java.util.ArrayList#addAll(int, java.util.Collection)
     * @see java.util.ArrayList#addAll(int, java.util.Collection)
     */
    @Override
    public boolean addAll(int index, Collection<? extends IComponent> coll) {
        for (IComponent c : coll) {
            if (this.parent != null) {
                c.setParent(this.parent);
            }
        }
        return super.addAll(index, coll);
    }

    /**
     * This method runs java.util.ArrayList#set(int, E)
     * @see java.util.ArrayList#set(int, E)
     */
    @Override
    public IComponent set(int index, IComponent component) {
        if (this.parent != null) {
            component.setParent(this.parent);
        }
        return super.set(index, component);
    }

    /**
     * Constructor.
     * @param paren Parent of all elements in list.
     */
    public ComponentArrayList(IComponent parent) {
        super();
        this.parent = parent;
    }

    /**
     * Default constructor.
     */
    public ComponentArrayList() {
        super();
    }

    /**
     * Returns all components in flat list not in tree-like structure.
     * @return All components as list.
     */
    public ComponentArrayList getFlat() {
        ComponentArrayList result = (ComponentArrayList) this.clone();
        for (Iterator iter = this.iterator(); iter.hasNext(); ) {
            Object element = iter.next();
            if (element instanceof IContainer) {
                result.addAll(((IContainer) element).getComponents());
            }
        }
        return result;
    }

    /**
     * For each element of list if it is instance of IContainer, this method runs 
     * java.util.Collection#remove method.
     * @see java.util.Collection#remove(java.lang.Object)
     */
    @Override
    public boolean remove(Object object) {
        boolean temp = super.remove(object);
        if (!temp) {
            for (Iterator iter = this.iterator(); iter.hasNext(); ) {
                Object element = iter.next();
                if (element instanceof IContainer) {
                    ((IContainer) element).remove(object);
                }
            }
        }
        return temp;
    }

    /**
     * Finds component in list by its name.
     * 
     * @param name
     *            Name of component to find.
     * @return Founded component or null.
     */
    public IComponent find(String name) {
        for (Iterator iter = this.iterator(); iter.hasNext(); ) {
            IComponent element = (IComponent) iter.next();
            if (element.getName().equals(name)) {
                return element;
            }
        }
        return null;
    }

    /**
     * Registers all components in ComponentRegistry
     * 
     * @throws ComponentStructureException Throws when econcounters errors in component tree structure.
     */
    public void register() throws ComponentStructureException {
        for (IComponent comp : this) {
            comp.register();
        }
    }
}
