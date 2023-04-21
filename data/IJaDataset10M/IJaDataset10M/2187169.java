package webml.diagram.edit.helpers;

import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.common.core.command.UnexecutableCommand;
import org.eclipse.gmf.runtime.emf.type.core.requests.DestroyElementRequest;

/**
 * @generated
 */
public class OperationUnitEditHelper extends WebmlBaseEditHelper {

    /**
	 * @generated NOT
	 */
    @Override
    protected ICommand getDestroyElementCommand(DestroyElementRequest req) {
        return UnexecutableCommand.INSTANCE;
    }
}
