package ti.plato.hook;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewReference;

/**
 * This interface should be implemented by plugins providing views which
 * hook into shared view-context sensitive menubar actions:
 * <ul>
 *   <li> Edit -&gt; Copy
 *   <li> Edit -&gt; Find
 *   <li> Edit -&gt; Find Next
 *   <li> Edit -&gt; Find Previous
 *   <li> Edit -&gt; Goto Line
 * </ul>
 * 
 * @author Anthony Nudelman
 */
public interface IMenuHook {

    /**
	 * @return <code>[subject class name].class.getName()</code>. 
	 * Return != <code>null</code>
	 *
	 * @author alex.k@ti.com
	 */
    String getId();

    void setViewReference(IViewReference viewReference);

    /**
	 * @param editorReference
	 *
	 * @author alex.k@ti.com
	 */
    void setEditorReference(IEditorReference editorReference);

    /**
	 * @return <code>true</code> if Copy action is supported,
	 * <code>false</code> otherwise
	 *
	 * @author alex.k@ti.com
	 */
    boolean isCopySupported();

    /**
	 * Execute copy action
	 *
	 * @author alex.k@ti.com
	 */
    void runCopy();

    boolean isFindSupported();

    void buildFindDialog(Shell dialog);

    void setFindVisible(boolean visible);

    int[] getFindDialogRange();

    void runFindNext();

    void runFindPrevious();

    boolean isGotoLineSupported();

    void buildGotoLineDialog(Shell dialog);

    void setGotoLineVisible(boolean visible);

    int[] getGotoLineDialogRange();

    boolean isFindCommentSupported();

    void runFindNextComment();

    void runFindPreComment();

    boolean isCutSupported();

    void runCut();

    boolean isPasteSupported();

    void runPaste();

    boolean isDeleteSupported();

    void runDelete();

    boolean isSelectAllSupported();

    void runSelectAll();

    boolean isUndoSupported();

    void runUndo();

    boolean isRedoSupported();

    void runRedo();
}
