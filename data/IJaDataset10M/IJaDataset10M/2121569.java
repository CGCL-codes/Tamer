package net.sf.rcpforms.experimenting.rcp_base.widgets.tree.dnd;

import net.sf.rcpforms.experimenting.rcp_base.dnd.RCPDnDEvent;
import net.sf.rcpforms.experimenting.rcp_base.tree.ITreeBuilder;
import net.sf.rcpforms.experimenting.rcp_base.tree.TreeNode2;
import net.sf.rcpforms.experimenting.rcp_base.tree.TreeNode3;
import net.sf.rcpforms.experimenting.rcp_base.widgets.tree.IRCPAdvancedTree;
import net.sf.rcpforms.experimenting.rcp_base.widgets.tree.RCPAdvancedTree;

/**
 * The Interface <code><i>IRCPTreeModfiyHandler</i></code> is implemented by classes that 
 * <b><i>'know'</i> the models</b> and how to apply tree changes. However, they need not care
 * about manipulating {@link TreeNode3 tree nodes} directly - this highly repetitive task
 * is done by RCPForms.
 * <p>
 * Every {@link RCPAdvancedTree} has an {@link IRCPAdvancedTree#getTreeModifier() associated 
 * tree modifier}. This tree modifier has a vital link to the {@link ITreeBuilder} in charge:
 * see {@link #getTreeBuilder(RCPDnDEvent) getTreeBuilder}.
 * <p>
 * Every manipulation on the tree - mostly DnD operations - will result in one or more
 * calls to that tree modifier. After that model changing operation(s), the {@link ITreeBuilder}
 * is asked to (re)build the affected tree nodes again. See below for the rough algorithm.
 * <p>
 * See {@link DefaultTreeDnDHandler}.
 * <p>
 * <h3 style='color: #227; font-family: monospace; '><u>Implementation details:</u></h3> 
 * <p>
 * These are the standard operations, described with the example operation <code>handleDnDMove</code>:
 * <pre style='background: #FFF; padding: 4px; font-size: 100%; '>
 *  1) ask <b>treeModifier</b> to handle event: {@link IRCPTreeModfiyHandler#handleDnDMove(Object, Object, Object...) treeModifier.handleDnDMove}(oldParent, newParent, Object... children)
 *  
 *  <span style='color:darkmagenta'>FOREACH</span> <code style='color:#3A6; padding:2px; font-size:125%;'><b><i>parent</i></b></code> <span style='color:darkmagenta'>IN</span> { oldParent, newParent } <span style='color:darkmagenta'>DO</span>         <span style='color:darkgreen'>// i.e. all parents of involved nodes...</span>
 *   
 *  2)   <code style='color:#A63; padding:2px; font-size:125%;'><b><i>parent-parent</i></b></code> := <code style='color:#3A6; padding:2px; font-size:125%;'><b><i>parent</i></b></code>{@link TreeNode2#getParent() .getParent()}
 *  3)   <code style='color:#84B; padding:2px; font-size:125%;'><b><i>parent_2</i></b></code> := {@link ITreeBuilder#createNode(TreeNode3, Object) <b>treeBuilder</b>.createNode}(<code style='color:#3A6; padding:2px; font-size:125%;'><b><i>parent</i></b></code>)
 *  4)   <code style='color:#A63; padding:2px; font-size:125%;'><b><i>parent-parent</i></b></code>{@link TreeNode2#getChildren() .getChildren()}{@link java.util.List#remove(Object) .remove}(<code style='color:#3A6; padding:2px; font-size:125%;'><b><i>parent</i></b></code>)
 *  5)   <code style='color:#A63; padding:2px; font-size:125%;'><b><i>parent-parent</i></b></code>{@link TreeNode2#getChildren() .getChildren()}{@link java.util.List#add(Object) .add}(<code style='color:#84B; padding:2px; font-size:125%;'><b><i>parent_2</i></b></code>)
 *  6)   tree-viewer{@link org.eclipse.jface.viewers.TreeViewer#refresh(Object, boolean) .refresh}(<code style='color:#A63; padding:2px; font-size:125%;'><b><i>parent-parent</i></b></code>, true)
 *  
 *  <span style='color:darkmagenta'>END</span>
 *  
 * </pre>
 * 
 * <p>
 * 
 */
public interface IRCPTreeModfiyHandler<BASE_TYPE> {

    /**
	 * Handle drag'n'drop (DnD) move.
	 * 
	 * @param oldParent the old parent or the <code>movedChildren</code>, may be <code>null</code>
	 *        if they children are dropped from outside of the target tree, is a root node or if a 
	 *        new node is created.
	 *        <p>
	 *        Note: if the dragged
	 *        child nodes have <code style='color:#38B; padding:2px; font-size:135%;'><b><i>n</i></b></code> 
	 *        different parents, then the DnD operation is split up in
	 *        <code style='color:#38B; padding:2px; font-size:135%;'><b><i>n</i></b></code> calls
	 *        to this method for each parent object as <code>oldParent</code>.
	 * @param newParent the new parent the <code>movedChildren</code> have been dropped to.
	 *        May be <code>null</code> if the dropped element shall be <b>root</b>.
	 * @param movedChildren the moved children. These are the Objects represented by the moved
	 *        tree nodes or any other Object of the specified type.
	 *        
	 * @return if the operation was accepted by this handler or <code>false</code> if the
	 *        it was denied.
	 */
    public boolean handleDnDMove(BASE_TYPE oldParent, BASE_TYPE newParent, BASE_TYPE... movedChildren);

    public boolean handleDnDCopy(BASE_TYPE oldParent, BASE_TYPE newParent, BASE_TYPE... movedChildren);

    public boolean handleDnDReference(BASE_TYPE oldParent, BASE_TYPE newParent, BASE_TYPE... movedChildren);

    public boolean handleDnDNew(BASE_TYPE parent, BASE_TYPE addedChild);

    public boolean handleDnDDelete(BASE_TYPE parent, BASE_TYPE deletedChild);

    public BASE_TYPE createNew(Object initData);

    public BASE_TYPE createCopy(BASE_TYPE ofObject);

    public ITreeBuilder<TreeNode3> getTreeBuilder(RCPDnDEvent dndEvent);

    /** 
	 * 
	 * Registers a TreeModifedModelSet to which all modified models are added until
	 * the set is {@link #unregisterModelChangeSet(TreeModifiedModelSet) unregistered}.
	 * <p>
	 * Implementing classes are responsible to inform the registered sets about all
	 * modified models, see {@link RCPTreeModfiyHandlerAdapter} and 
	 * {@link DefaultTreeBuilderAndModifier} for a example implementation. (It is advised
	 * to use these as base classes anyway)
	 *  
	 * */
    public void registerModelChangeSet(TreeModifiedModelSet set);

    public void unregisterModelChangeSet(TreeModifiedModelSet set);
}
