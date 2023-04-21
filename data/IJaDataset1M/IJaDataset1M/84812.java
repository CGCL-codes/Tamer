package net.sf.uibuilder.tree;

import javax.swing.tree.TreeModel;

/**  
 * The class AbstractTreeModel is facility class to minimize the work you need
 * to do to create a tree model.  You can simply subclass AbstractTreeModel to
 * build your own tree model.  This class extends {@link TreeModelSupport},
 * which handles the registration of tree model listeners and event
 * notifications.  Since it also declares the {@link TreeModel} interface,
 * leaving you to implement the methods defined in that interface except for the
 * listener registration methods, which are already handled by {@link
 * TreeModelSupport}.
 *
 * @version   1.0 2002-7-26
 * @author <A HREF="mailto:chyxiang@yahoo.com">Chen Xiang (Sean)</A>
 */
public abstract class AbstractTreeModel extends TreeModelSupport implements TreeModel {
}
