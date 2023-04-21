package org.exist.eclipse.browse.document;

/**
 * Interface, on which you will inform about {@link IDocumentItem} changes. You
 * have to register it on {@link DocumentCoordinator}.
 * 
 * @see DocumentCoordinator
 * @author Pascal Schmidiger
 * 
 */
public interface IDocumentItemListener {

    /**
	 * The given <code>item</code> does not exist.
	 * 
	 * @param item
	 *            the removed item.
	 */
    public void removed(IDocumentItem item);

    /**
	 * The given <code>fromItem</code> has moved to <code>toItem</code>.
	 * 
	 * @param fromItem
	 * @param toItem
	 */
    public void moved(IDocumentItem fromItem, IDocumentItem toItem);
}
