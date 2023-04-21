package net.sf.joafip.btree.service;

import java.io.Serializable;
import java.util.Comparator;
import net.sf.joafip.btree.entity.IBTreeNode;
import net.sf.joafip.btree.entity.IBTreePage;

/**
 * interface for btree page and node manager<br>
 * 
 * @author luc peuvrier
 * 
 * @param <E>
 */
public interface IBTreePageAndNodeManager<E> extends Serializable {

    /**
	 * get the root page from page storage
	 * 
	 * @return the root page, may be null for empty tree
	 */
    IBTreePage<E> getRootPage();

    /**
	 * set the root page in file storage
	 * 
	 * @param page
	 *            the head page
	 */
    void setRootPage(IBTreePage<E> page);

    /**
	 * create a new empty page in page storage
	 * 
	 * @return created empty page
	 */
    IBTreePage<E> createNewPage();

    /**
	 * remove a page from page storage
	 * 
	 * @param page
	 *            page to remove
	 */
    void removePage(IBTreePage<E> page);

    /**
	 * create a leaf node for an element<br>
	 * leaf node do not reference sub page<br>
	 * 
	 * @param element
	 *            the element of leaf node
	 * @return the created leaf node for element
	 */
    IBTreeNode<E> createLeafNode(E element, final Comparator<E> comparator);

    /**
	 * create a non leaf node<br>
	 * 
	 * @param pageOfPreviousElement
	 *            page of previous element for this node
	 * @param element
	 *            the element of leaf node
	 * @return the created node for element
	 */
    IBTreeNode<E> createNode(IBTreePage<E> pageOfPreviousElement, E element, final Comparator<E> comparator);
}
