package org.eclipse.ui.texteditor;

/**
 * Defines the definitions ids for workbench actions.
 * @since 2.0
 */
public interface IWorkbenchActionDefinitionIds {

    /**
	 * Action definition id of the file print action.
	 * Value: <code>"org.eclipse.ui.file.print"</code>
	 */
    public static final String PRINT = "org.eclipse.ui.file.print";

    /**
	 * Action definition id of the file save action.
	 * Value: <code>"org.eclipse.ui.file.save"</code>
	 */
    public static final String SAVE = "org.eclipse.ui.file.save";

    /**
	 * Action definition id of the file revert action.
	 * Value: <code>"org.eclipse.ui.edit.revertToSaved"</code>
	 */
    public static final String REVERT_TO_SAVED = "org.eclipse.ui.edit.revertToSaved";

    /**
	 * Action definition id of the edit cut action.
	 * Value: <code>"org.eclipse.ui.edit.cut"</code>
	 */
    public static final String CUT = "org.eclipse.ui.edit.cut";

    /**
	 * Action definition id of the edit copy action.
	 * Value: <code>"org.eclipse.ui.edit.copy"</code>
	 */
    public static final String COPY = "org.eclipse.ui.edit.copy";

    /**
	 * Action definition id of the edit past action.
	 * Value: <code>"org.eclipse.ui.edit.paste"</code>
	 */
    public static final String PASTE = "org.eclipse.ui.edit.paste";

    /**
	 * Action definition id of the edit undo action.
	 * Value: <code>"org.eclipse.ui.edit.undo"</code>
	 */
    public static final String UNDO = "org.eclipse.ui.edit.undo";

    /**
	 * Action definition id of the file properties action.
	 * Value: <code>"org.eclipse.ui.file.properties"</code>
	 * @since 3.1
	 */
    public static final String PROPERTIES = "org.eclipse.ui.file.properties";

    /**
	 * Action definition id of the edit redo action.
	 * Value: <code>"org.eclipse.ui.edit.redo"</code>
	 */
    public static final String REDO = "org.eclipse.ui.edit.redo";

    /**
	 * Action definition id of the edit delete action.
	 * Value: <code>"org.eclipse.ui.edit.delete"</code>
	 */
    public static final String DELETE = "org.eclipse.ui.edit.delete";

    /**
	 * Action definition id of the edit select all action.
	 * Value: <code>"org.eclipse.ui.edit.selectAll"</code>
	 */
    public static final String SELECT_ALL = "org.eclipse.ui.edit.selectAll";

    /**
	 * Action definition id of the edit find/replace action.
	 * Value: <code>"org.eclipse.ui.edit.findReplace"</code>
	 */
    public static final String FIND_REPLACE = "org.eclipse.ui.edit.findReplace";

    /**
	 * Action definition id of the edit add bookmark action.
	 * Value: <code>"org.eclipse.ui.edit.addBookmark"</code>
	 */
    public static final String ADD_BOOKMARK = "org.eclipse.ui.edit.addBookmark";

    /**
	 * Action definition id of the edit add task action.
	 * Value: <code>"org.eclipse.ui.edit.addTask"</code>
	 */
    public static final String ADD_TASK = "org.eclipse.ui.edit.addTask";

    /**
	 * Action definition id of the edit find next action.
	 * Value: <code>"org.eclipse.ui.edit.findNext"</code>
	 */
    public static final String FIND_NEXT = "org.eclipse.ui.edit.findNext";

    /**
	 * Action definition id of the edit find previous action.
	 * Value: <code>"org.eclipse.ui.edit.findPrevious"</code>
	 */
    public static final String FIND_PREVIOUS = "org.eclipse.ui.edit.findPrevious";

    /**
	 * Action definition id of the edit incremental find action.
	 * Value: <code>"org.eclipse.ui.edit.findIncremental"</code>
	 */
    public static final String FIND_INCREMENTAL = "org.eclipse.ui.edit.findIncremental";

    /**
	 * Action definition id of the edit incremental find reverse action.
	 * Value: <code>"org.eclipse.ui.edit.findIncrementalReverse"</code>
	 *
	 * @since 2.1
	 */
    public static final String FIND_INCREMENTAL_REVERSE = "org.eclipse.ui.edit.findIncrementalReverse";

    /**
	 * The command identifier for the "move" action that typically appears in
	 * the file menu.
	 * Value: <code>"org.eclipse.ui.edit.move"</code>
	 * @since 3.0
	 */
    public static final String MOVE = "org.eclipse.ui.edit.move";

    /**
	 * The command identifier for the "rename" action that typically appears in
	 * the file menu.
	 * Value: <code>"org.eclipse.ui.edit.rename"</code>
	 * @since 3.0
	 */
    public static final String RENAME = "org.eclipse.ui.edit.rename";
}
