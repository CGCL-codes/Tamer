package hu.cubussapiens.modembed.model.editor.internal;

import org.eclipse.emf.ecore.EObject;
import hu.cubussapiens.modembed.model.datatypes.DatatypesFactory;
import hu.cubussapiens.modembed.model.datatypes.Operation;
import hu.cubussapiens.modembed.model.editor.GenericModelWizard;
import hu.cubussapiens.modembed.ui.descriptor.IModelDescriptor;

/**
 * @author balazs.grill
 *
 */
public class OperationModelWizard extends GenericModelWizard {

    @Override
    protected IModelDescriptor getDescriptor() {
        return new IModelDescriptor() {

            @Override
            public EObject createInitialModel() {
                Operation op = DatatypesFactory.eINSTANCE.createOperation();
                op.setName("operation");
                return op;
            }
        };
    }
}
