package org.fluid.uimodeling.uivocabulary.diagram.edit.policies;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.gef.commands.Command;
import org.eclipse.gmf.runtime.diagram.core.commands.DeleteCommand;
import org.eclipse.gmf.runtime.emf.commands.core.command.CompositeTransactionalCommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.gmf.runtime.notation.View;
import org.fluid.uimodeling.uivocabulary.diagram.providers.UIVocabularyElementTypes;

/**
 * @generated
 */
public class UIPropertyItemSemanticEditPolicy extends UIVocabularyBaseItemSemanticEditPolicy {

    /**
	 * @generated
	 */
    public UIPropertyItemSemanticEditPolicy() {
        super(UIVocabularyElementTypes.UIProperty_3001);
    }

    /**
	 * @generated
	 */
    protected Command getDestroyElementCommand(DestroyElementRequest req) {
        View view = (View) getHost().getModel();
        CompositeTransactionalCommand cmd = new CompositeTransactionalCommand(getEditingDomain(), null);
        cmd.setTransactionNestingEnabled(false);
        EAnnotation annotation = view.getEAnnotation("Shortcut");
        if (annotation == null) {
            addDestroyShortcutsCommand(cmd, view);
            cmd.add(new DestroyElementCommand(req));
        } else {
            cmd.add(new DeleteCommand(getEditingDomain(), view));
        }
        return getGEFWrapper(cmd.reduce());
    }
}
