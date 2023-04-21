package de.walware.statet.ext.ui.editors;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.source.projection.IProjectionListener;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.editors.text.IFoldingCommandIds;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.TextOperationAction;

/**
 * Groups the folding actions.
 */
public class FoldingActionGroup extends ActionGroup {

    private ProjectionViewer fViewer;

    private final IProjectionListener fProjectionListener;

    private final TextOperationAction fToggle;

    private final TextOperationAction fExpand;

    private final TextOperationAction fCollapse;

    private final TextOperationAction fExpandAll;

    private final TextOperationAction fCollapseAll;

    /**
	 * Creates a new projection action group for <code>editor</code>. If the
	 * supplied viewer is not an instance of <code>ProjectionViewer</code>,
	 * the action group is disabled.
	 * 
	 * @param editor
	 *            the text editor to operate on
	 * @param viewer
	 *            the viewer of the editor
	 */
    public FoldingActionGroup(final ITextEditor editor, final ProjectionViewer viewer) {
        fViewer = viewer;
        fProjectionListener = new IProjectionListener() {

            public void projectionEnabled() {
                update();
                fToggle.setChecked(fViewer.isProjectionMode());
            }

            public void projectionDisabled() {
                update();
                fToggle.setChecked(fViewer.isProjectionMode());
            }
        };
        fViewer.addProjectionListener(fProjectionListener);
        fToggle = new TextOperationAction(EditorMessages.getCompatibilityBundle(), "Projection.Toggle.", editor, ProjectionViewer.TOGGLE);
        fToggle.setChecked(fViewer.isProjectionMode());
        fToggle.setActionDefinitionId(IFoldingCommandIds.FOLDING_TOGGLE);
        editor.setAction("FoldingToggle", fToggle);
        fExpandAll = new TextOperationAction(EditorMessages.getCompatibilityBundle(), "Projection.ExpandAll.", editor, ProjectionViewer.EXPAND_ALL, true);
        fExpandAll.setActionDefinitionId(IFoldingCommandIds.FOLDING_EXPAND_ALL);
        editor.setAction("FoldingExpandAll", fExpandAll);
        fCollapseAll = new TextOperationAction(EditorMessages.getCompatibilityBundle(), "Projection.CollapseAll.", editor, ProjectionViewer.COLLAPSE_ALL, true);
        fCollapseAll.setActionDefinitionId(IFoldingCommandIds.FOLDING_COLLAPSE_ALL);
        editor.setAction("FoldingCollapseAll", fCollapseAll);
        fExpand = new TextOperationAction(EditorMessages.getCompatibilityBundle(), "Projection.Expand.", editor, ProjectionViewer.EXPAND, true);
        fExpand.setActionDefinitionId(IFoldingCommandIds.FOLDING_EXPAND);
        editor.setAction("FoldingExpand", fExpand);
        fCollapse = new TextOperationAction(EditorMessages.getCompatibilityBundle(), "Projection.Collapse.", editor, ProjectionViewer.COLLAPSE, true);
        fCollapse.setActionDefinitionId(IFoldingCommandIds.FOLDING_COLLAPSE);
        editor.setAction("FoldingCollapse", fCollapse);
    }

    /**
	 * Returns <code>true</code> if the group is enabled.
	 * 
	 * <pre>
	 * Invariant: isEnabled() &lt;=&gt; fViewer and all actions are != null.
	 * </pre>
	 * 
	 * @return <code>true</code> if the group is enabled
	 */
    protected boolean isEnabled() {
        return fViewer != null;
    }

    @Override
    public void dispose() {
        if (isEnabled()) {
            fViewer.removeProjectionListener(fProjectionListener);
            fViewer = null;
        }
        super.dispose();
    }

    /**
	 * Updates the actions.
	 */
    protected void update() {
        if (isEnabled()) {
            fExpand.update();
            fExpandAll.update();
            fCollapse.update();
            fCollapseAll.update();
        }
    }

    /**
	 * Fills the menu with all folding actions.
	 * 
	 * @param manager
	 *            the menu manager for the folding submenu
	 */
    public void fillMenu(final IMenuManager manager) {
        if (isEnabled()) {
            update();
            manager.add(fToggle);
            manager.add(new Separator());
            manager.add(fExpandAll);
            manager.add(fCollapseAll);
        }
    }

    @Override
    public void updateActionBars() {
        update();
    }
}
