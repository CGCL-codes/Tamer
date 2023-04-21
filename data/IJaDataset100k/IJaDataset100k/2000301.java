package org.rubypeople.rdt.internal.ui.actions;

import java.util.ResourceBundle;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.source.projection.IProjectionListener;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.ui.actions.ActionGroup;
import org.eclipse.ui.editors.text.IFoldingCommandIds;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.ui.texteditor.IUpdate;
import org.eclipse.ui.texteditor.ResourceAction;
import org.eclipse.ui.texteditor.TextOperationAction;
import org.rubypeople.rdt.internal.ui.RubyPlugin;
import org.rubypeople.rdt.internal.ui.rubyeditor.RubyEditor;
import org.rubypeople.rdt.ui.PreferenceConstants;
import org.rubypeople.rdt.ui.actions.IRubyEditorActionDefinitionIds;

/**
 * Groups the RDT folding actions.
 *  
 * @since 0.9.0
 */
public class FoldingActionGroup extends ActionGroup {

    private abstract static class PreferenceAction extends ResourceAction implements IUpdate {

        PreferenceAction(ResourceBundle bundle, String prefix, int style) {
            super(bundle, prefix, style);
        }
    }

    /**
	 * @since 0.9.0
	 */
    private class FoldingAction extends PreferenceAction {

        FoldingAction(ResourceBundle bundle, String prefix) {
            super(bundle, prefix, IAction.AS_PUSH_BUTTON);
        }

        public void update() {
            setEnabled(FoldingActionGroup.this.isEnabled() && fViewer.isProjectionMode());
        }
    }

    private ProjectionViewer fViewer;

    private final PreferenceAction fToggle;

    private final TextOperationAction fExpand;

    private final TextOperationAction fCollapse;

    private final TextOperationAction fExpandAll;

    private final PreferenceAction fRestoreDefaults;

    private final FoldingAction fCollapseMembers;

    private final FoldingAction fCollapseComments;

    private final TextOperationAction fCollapseAll;

    private final IProjectionListener fProjectionListener;

    /**
	 * Creates a new projection action group for <code>editor</code>. If the
	 * supplied viewer is not an instance of <code>ProjectionViewer</code>, the
	 * action group is disabled.
	 * 
	 * @param editor the text editor to operate on
	 * @param viewer the viewer of the editor
	 */
    public FoldingActionGroup(final ITextEditor editor, ITextViewer viewer) {
        if (!(viewer instanceof ProjectionViewer)) {
            fToggle = null;
            fExpand = null;
            fCollapse = null;
            fExpandAll = null;
            fCollapseAll = null;
            fRestoreDefaults = null;
            fCollapseMembers = null;
            fCollapseComments = null;
            fProjectionListener = null;
            return;
        }
        fViewer = (ProjectionViewer) viewer;
        fProjectionListener = new IProjectionListener() {

            public void projectionEnabled() {
                update();
            }

            public void projectionDisabled() {
                update();
            }
        };
        fViewer.addProjectionListener(fProjectionListener);
        fToggle = new PreferenceAction(FoldingMessages.getResourceBundle(), "Projection.Toggle.", IAction.AS_CHECK_BOX) {

            public void run() {
                IPreferenceStore store = RubyPlugin.getDefault().getPreferenceStore();
                boolean current = store.getBoolean(PreferenceConstants.EDITOR_FOLDING_ENABLED);
                store.setValue(PreferenceConstants.EDITOR_FOLDING_ENABLED, !current);
            }

            public void update() {
                ITextOperationTarget target = (ITextOperationTarget) editor.getAdapter(ITextOperationTarget.class);
                boolean isEnabled = (target != null && target.canDoOperation(ProjectionViewer.TOGGLE));
                setEnabled(isEnabled);
            }
        };
        fToggle.setChecked(true);
        fToggle.setActionDefinitionId(IFoldingCommandIds.FOLDING_TOGGLE);
        editor.setAction("FoldingToggle", fToggle);
        fExpandAll = new TextOperationAction(FoldingMessages.getResourceBundle(), "Projection.ExpandAll.", editor, ProjectionViewer.EXPAND_ALL, true);
        fExpandAll.setActionDefinitionId(IFoldingCommandIds.FOLDING_EXPAND_ALL);
        editor.setAction("FoldingExpandAll", fExpandAll);
        fCollapseAll = new TextOperationAction(FoldingMessages.getResourceBundle(), "Projection.CollapseAll.", editor, ProjectionViewer.COLLAPSE_ALL, true);
        fCollapseAll.setActionDefinitionId(IFoldingCommandIds.FOLDING_COLLAPSE_ALL);
        editor.setAction("FoldingCollapseAll", fCollapseAll);
        fExpand = new TextOperationAction(FoldingMessages.getResourceBundle(), "Projection.Expand.", editor, ProjectionViewer.EXPAND, true);
        fExpand.setActionDefinitionId(IFoldingCommandIds.FOLDING_EXPAND);
        editor.setAction("FoldingExpand", fExpand);
        fCollapse = new TextOperationAction(FoldingMessages.getResourceBundle(), "Projection.Collapse.", editor, ProjectionViewer.COLLAPSE, true);
        fCollapse.setActionDefinitionId(IFoldingCommandIds.FOLDING_COLLAPSE);
        editor.setAction("FoldingCollapse", fCollapse);
        fRestoreDefaults = new FoldingAction(FoldingMessages.getResourceBundle(), "Projection.Restore.") {

            public void run() {
                if (editor instanceof RubyEditor) {
                    RubyEditor javaEditor = (RubyEditor) editor;
                    javaEditor.resetProjection();
                }
            }
        };
        fRestoreDefaults.setActionDefinitionId(IFoldingCommandIds.FOLDING_RESTORE);
        editor.setAction("FoldingRestore", fRestoreDefaults);
        fCollapseMembers = new FoldingAction(FoldingMessages.getResourceBundle(), "Projection.CollapseMembers.") {

            public void run() {
                if (editor instanceof RubyEditor) {
                    RubyEditor javaEditor = (RubyEditor) editor;
                    javaEditor.collapseMembers();
                }
            }
        };
        fCollapseMembers.setActionDefinitionId(IRubyEditorActionDefinitionIds.FOLDING_COLLAPSE_MEMBERS);
        editor.setAction("FoldingCollapseMembers", fCollapseMembers);
        fCollapseComments = new FoldingAction(FoldingMessages.getResourceBundle(), "Projection.CollapseComments.") {

            public void run() {
                if (editor instanceof RubyEditor) {
                    RubyEditor javaEditor = (RubyEditor) editor;
                    javaEditor.collapseComments();
                }
            }
        };
        fCollapseComments.setActionDefinitionId(IRubyEditorActionDefinitionIds.FOLDING_COLLAPSE_COMMENTS);
        editor.setAction("FoldingCollapseComments", fCollapseComments);
    }

    /**
	 * Returns <code>true</code> if the group is enabled. 
	 * <pre>
	 * Invariant: isEnabled() <=> fViewer and all actions are != null.
	 * </pre>
	 * 
	 * @return <code>true</code> if the group is enabled
	 */
    protected boolean isEnabled() {
        return fViewer != null;
    }

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
            fToggle.update();
            fToggle.setChecked(fViewer.isProjectionMode());
            fExpand.update();
            fExpandAll.update();
            fCollapse.update();
            fCollapseAll.update();
            fRestoreDefaults.update();
            fCollapseMembers.update();
            fCollapseComments.update();
        }
    }

    /**
	 * Fills the menu with all folding actions.
	 * 
	 * @param manager the menu manager for the folding submenu
	 */
    public void fillMenu(IMenuManager manager) {
        if (isEnabled()) {
            update();
            manager.add(fToggle);
            manager.add(fExpandAll);
            manager.add(fExpand);
            manager.add(fCollapse);
            manager.add(fCollapseAll);
            manager.add(fRestoreDefaults);
            manager.add(fCollapseMembers);
            manager.add(fCollapseComments);
        }
    }

    public void updateActionBars() {
        update();
    }
}
