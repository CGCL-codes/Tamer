package de.bea.domingo.proxy;

import lotus.domino.Item;
import lotus.domino.NotesException;
import lotus.domino.RichTextItem;
import de.bea.domingo.DBase;
import de.bea.domingo.DBaseItem;
import de.bea.domingo.DNotesMonitor;

/**
 * This class represents the Domino-Class <code>Item</code>.
 */
public abstract class BaseItemProxy extends BaseProxy implements DBaseItem {

    /** serial version ID for serialization. */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor for DItemImpl.
     *
     * @param theFactory the controlling factory
     * @param parent the parent object
     * @param item the Notes item object
     * @param monitor the monitor
     */
    protected BaseItemProxy(final NotesProxyFactory theFactory, final DBase parent, final Item item, final DNotesMonitor monitor) {
        super(theFactory, parent, item, monitor);
    }

    /**
     * Creates or returns a cached implementation of the requested item
     * interface.
     *
     * @param theFactory the controlling factory
     * @param parent the parent object
     * @param item the associated Notes Item
     * @param monitor the monitor
     *
     * @return implementation of interface DBaseItem or null
     */
    static BaseItemProxy getInstance(final NotesProxyFactory theFactory, final DBase parent, final Item item, final DNotesMonitor monitor) {
        if (item == null) {
            return null;
        }
        BaseItemProxy itemProxy = (BaseItemProxy) theFactory.getBaseCache().get(item);
        if (itemProxy == null) {
            if (item instanceof RichTextItem) {
                itemProxy = new RichTextItemProxy(theFactory, parent, (RichTextItem) item, monitor);
            } else {
                itemProxy = new ItemProxy(theFactory, parent, item, monitor);
            }
            itemProxy.setMonitor(monitor);
            theFactory.getBaseCache().put(item, itemProxy);
        }
        return itemProxy;
    }

    /**
     * Returns the associated notes item object.
     *
     * @return notes item object
     */
    protected final Item getItem() {
        return (Item) getNotesObject();
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DBaseItem#getName()
     */
    public final String getName() {
        getFactory().preprocessMethod();
        try {
            return getItem().getName();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot get name", e);
        }
    }

    /**
     * {@inheritDoc}
     * @see de.bea.domingo.DBaseItem#remove()
     */
    public final void remove() {
        getFactory().preprocessMethod();
        try {
            getItem().remove();
        } catch (NotesException e) {
            throw newRuntimeException("Cannot remove item", e);
        }
    }

    /**
     * @see de.bea.domingo.proxy.BaseProxy#toString()
     * @return the name of the item
     */
    public final String toString() {
        return getName();
    }
}
