package com.rapidminer.gui.tools;

import javax.swing.Icon;

/** Model for a {@link ParentButtonPanel}.
 * 
 *  @author Simon Fischer
 */
public interface ParentButtonModel<T> {

    public T getRoot();

    public T getParent(T child);

    public int getNumberOfChildren(T node);

    public T getChild(T node, int index);

    public String toString(T node);

    public Icon getIcon(T node);
}
