package net.taylor.uml2.activitydiagram.edit.helpers;

import net.taylor.mda.util.ModelUtil;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.emf.type.core.commands.DestroyElementCommand;
import org.eclipse.gmf.runtime.emf.type.core.edithelper.AbstractEditHelperAdvice;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;
import org.eclipse.uml2.uml.Element;

public class ActivityEditHelperAdvice extends AbstractEditHelperAdvice {

    public ActivityEditHelperAdvice() {
        super();
    }

    protected ICommand getBeforeDestroyElementCommand(DestroyElementRequest request) {
        Object e = request.getElementToDestroy();
        if (e instanceof Element) {
            return new DestroyElementCommand(request) {

                protected CommandResult doExecuteWithResult(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
                    Element element = (Element) getElementToDestroy();
                    ModelUtil.clean(element, element);
                    return CommandResult.newOKCommandResult();
                }
            };
        }
        return null;
    }
}
