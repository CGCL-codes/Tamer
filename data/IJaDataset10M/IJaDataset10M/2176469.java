package com.tensegrity.palorules.source;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import com.tensegrity.palorules.IRuleEditor;
import com.tensegrity.palorules.IRuleSourcePartEditor;

/**
 * This interface defines the functionality of a container for content items.
 * @author AndreasEbbert
 * @version $Id: IContentItemContainer.java,v 1.5 2007/09/19 11:19:09
 *          AndreasEbbert Exp $
 */
public interface IContentItemContainer extends IContentItem {

    /**
   * Appends the {@link IContentItem} to the list of items.
   * @param cItem the {@link IContentItem} to append.
   */
    void appendItem(IContentItem cItem);

    /**
   * Inserts the <code>insertItem</code> after the reference item
   * <code>refItem</code>.
   * @param refItem the reference item.
   * @param insertItem the {@link IContentItem} to insert.
   */
    void insertAfter(IContentItem refItem, IContentItem insertItem);

    /**
   * Inserts the <code>insertItem</code> before the reference item
   * <code>refItem</code>.
   * @param refItem the reference item.
   * @param insertItem the {@link IContentItem} to insert.
   */
    void insertBefore(IContentItem refItem, IContentItem insertItem);

    /**
   * Remove the given {@link IContentItem} <code>cItem</code> from the
   * container.
   * @param cItem the {@link IContentItem} to remove.
   * @param dispose true if the {@link IContentItem} should be disposed.
   */
    void removeItem(IContentItem cItem, boolean dispose);

    /**
   * Removes all {@link IContentItem}s from the container.
   * @param dispose set to <code>true</code> to dispose the items.
   */
    void removeAllItems(boolean dispose);

    /**
   * Returns an array containing all top-level {@link IContentItem}s.
   * @return an array containing all top-level {@link IContentItem}s.
   */
    IContentItem[] getContentItems();

    /**
   * Returns the index of the given {@link IContentItem} or <code>-1</code> if
   * no such item exists.
   * @param cItem the {@link IContentItem} to return the index for.
   * @return the index of the given {@link IContentItem}.
   */
    int getIndexOf(IContentItem cItem);

    /**
   * Returns the {@link IContentItem} having the given {@link Control} as the
   * top-level control. This is the control that is returned on a call to
   * {@link IContentItem#getControl()}.
   * @param ctrl the top level {@link Control}.
   * @return the {@link IContentItem} having the given {@link Control} as the
   *         top-level control.
   */
    IContentItem getContentItem(Control ctrl);

    /**
   * Returns the shared {@link IRuleEditor} instance.
   * @return the shared {@link IRuleEditor} instance.
   */
    IRuleSourcePartEditor getRuleSourcePartEditor();

    /**
   * Returns the container panel. That is the panel where nested
   * {@link IContentItem}s are added.
   * @return the container panel.
   */
    Composite getContainerPanel();
}
