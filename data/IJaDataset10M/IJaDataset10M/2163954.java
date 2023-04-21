package org.parallelj.mda.controlflow.diagram.extension.edit.helpers.advices;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.ConfigureRequest;
import org.parallelj.mda.controlflow.model.controlflow.FinalState;

/**
 * Final State Edit Helper Advice. Role is to set a default name when Final
 * State is created
 * 
 * @author Atos Worldline
 * 
 */
public class FinalStateEditHelperAdvice extends AbstractEditHelperAdvice {

    @Override
    protected ICommand getAfterConfigureCommand(ConfigureRequest request) {
        EObject elementToConfigure = request.getElementToConfigure();
        if (elementToConfigure instanceof FinalState) ((FinalState) elementToConfigure).setName("end");
        return super.getAfterConfigureCommand(request);
    }
}
