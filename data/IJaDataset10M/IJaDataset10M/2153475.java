package org.ufacekit.ui.viewers;

import org.ufacekit.ui.viewers.TreePath;

/**
 * A selection containing tree paths.
 * <p>
 * It is recommended that clients do not implement this interface but instead
 * use the standard implementation of this interface, {@link TreeSelection}.
 * <code>TreeSelection</code> adds API for getting the {@link IElementComparer}
 * of a selection (if available). This is important for clients who want to
 * create a slightly modified tree selection based on an existing tree selection.
 * The recommended coding pattern in this case is as follows:
 * <pre>
 * ITreeSelection selection = (ITreeSelection)treeViewer.getSelection();
 * TreePath[] paths = selection.getPaths();
 * IElementComparer comparer = null;
 * if (selection instanceof TreeSelection) {
 *   comparer = ((TreeSelection)selection).getElementComparer();
 * }
 * TreePath[] modifiedPaths = ... // modify as required
 * TreeSelection modifiedSelection = new TreeSelection(modifiedPaths, comparer);
 * </pre>
 * See bugs 135818 and 133375 for details.
 * </p>
 *
 * @since 3.2
 *
 */
public interface ITreeSelection<ModelElement> extends ISelection<ModelElement> {

    /**
	 * Returns the paths in this selection
	 *
	 * @return the paths in this selection
	 */
    public TreePath<ModelElement>[] getPaths();

    /**
	 * Returns the paths in this selection whose last segment is equal
	 * to the given element
	 *
	 * @param element the element to get the tree paths for
	 *
	 * @return the array of tree paths
	 */
    public TreePath<ModelElement>[] getPathsFor(Object element);
}
