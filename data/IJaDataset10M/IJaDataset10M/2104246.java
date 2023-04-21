package org.jrcaf.flow.ui.internal.action;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.jrcaf.core.model.ModelEvent;
import org.jrcaf.flow.action.IActionCaller;
import org.jrcaf.flow.action.IActionDefinition;
import org.jrcaf.flow.activity.IActivity;
import org.jrcaf.flow.ui.UIFlowPlugin;
import org.jrcaf.flow.ui.internal.ui.editor.StandardEditor;
import org.jrcaf.flow.ui.internal.ui.editor.StandardEditorInput;
import org.jrcaf.mvc.validator.AbstractValidationResultStrategie;
import org.jrcaf.mvc.validator.IValidationResultStrategie;

/**
 * Opens the part in an editor.
 */
public class ShowInEditorAction extends AbstractMVCAction {

    private static final String DELETED_RESULT = "deleted";

    private static final String ORG_JRCAF_EDITORS_STANDARD_EDITOR_ID = "org.jrcaf.flow.ui.internal.ui.editor.StandardEditor";

    private static Map<String, StandardEditorInput> editorInputMap = new WeakHashMap<String, StandardEditorInput>(1);

    StandardEditor standardEditor;

    private IValidationResultStrategie validationResultStrategie;

    /**
    * Creates a new ShowInEditorAction.
    * @param aParameters The passed parameters.
    * @param aActivity The enclosing activity.
    * @param aActionDefinition The definition of the action.
    * @param aCalledBy The caller of the action.
    */
    public ShowInEditorAction(Map<String, Object> aParameters, IActivity aActivity, IActionDefinition aActionDefinition, IActionCaller aCalledBy) {
        super(aParameters, aActivity, aActionDefinition, aCalledBy);
    }

    /**
    * @see org.jrcaf.flow.action.IAction#execute()
    */
    public void execute() {
        createPart();
        part.initMapping();
        IWorkbenchWindow window = UIFlowPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
        if (window == null) {
            assert false;
            return;
        }
        IWorkbenchPage page = window.getActivePage();
        if (page != null) {
            try {
                StandardEditorInput editorInput = getEditorInput();
                standardEditor = (StandardEditor) page.openEditor(editorInput, ORG_JRCAF_EDITORS_STANDARD_EDITOR_ID);
                page.addPartListener(standardEditor);
                editorInputMap.put(part.getModelID(), editorInput);
            } catch (PartInitException ex) {
                UIFlowPlugin.getDefault().getLog().log(new Status(IStatus.ERROR, UIFlowPlugin.ID_PLUGIN, IStatus.OK, "Error in standard editor part init.", ex));
            }
        }
        if (standardEditor.isInitialized() == false) {
            part.connectPart();
            standardEditor.setInitialized();
        }
    }

    private StandardEditorInput getEditorInput() {
        String modelID = part.getModelID();
        StandardEditorInput editorInput = null;
        if (modelID != null) editorInput = editorInputMap.get(part.getModelID());
        if ((editorInput != null) && (editorInput.getAction().getDefinition() == getDefinition())) return editorInput;
        return new StandardEditorInput(part, this);
    }

    /**
    * @see org.jrcaf.flow.ui.internal.action.AbstractMVCAction#setDirty()
    */
    @Override
    public void setDirty() {
        standardEditor.setDirty();
    }

    /**
    * @see org.jrcaf.flow.ui.internal.action.AbstractMVCAction#resetDirty()
    */
    @Override
    public void resetDirty() {
        standardEditor.setDirty();
    }

    /**
    * @see org.jrcaf.flow.ui.internal.action.AbstractMVCAction#update(org.jrcaf.core.model.ModelEvent)
    */
    @Override
    public void update(ModelEvent aEvent) {
        super.update(aEvent);
        switch(aEvent.getType()) {
            case CHANGED:
                {
                    standardEditor.updateTitel();
                    break;
                }
            case DELTED:
                {
                    HashMap<String, Object> results = new HashMap<String, Object>();
                    part.finish(DELETED_RESULT);
                    part.readResult(results);
                    cancelAction(DELETED_RESULT, results);
                    standardEditor.close();
                    break;
                }
            case INIT:
                {
                    assert false : "unreachable";
                }
            case MAP_ALL:
                {
                    assert false : "unreachable";
                }
        }
    }

    @Override
    protected void dispose() {
        super.dispose();
        standardEditor.dispose();
    }

    /**
    * Removes the passed model id from the map of displayed editors.
    * @param aModelId The model id.
    */
    public static void removeInput(Object aModelId) {
        editorInputMap.remove(aModelId);
    }

    /**
    * @see org.jrcaf.flow.ui.internal.action.AbstractMVCAction#getValidationResultStrategy()
    */
    @Override
    public IValidationResultStrategie getValidationResultStrategy() {
        if (validationResultStrategie == null) validationResultStrategie = new AbstractValidationResultStrategie() {

            @Override
            protected void refreshValidationResults(String aStrategyParam) {
            }

            @Override
            protected void removeValidationResult(Object aValidated, String aStrategyParam) {
                super.removeValidationResult(aValidated, aStrategyParam);
                standardEditor.removeValidationResult(aValidated, aStrategyParam);
            }

            @Override
            protected void addValidationResult(IStatus aResult, Object aValidated, String aStrategyParam) {
                super.addValidationResult(aResult, aValidated, aStrategyParam);
                standardEditor.addValidationResult(aResult, aValidated, aStrategyParam);
            }
        };
        return validationResultStrategie;
    }
}
