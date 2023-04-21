package hu.cubussapiens.modembed.model.editor.internal;

import org.eclipse.emf.ecore.EObject;
import hu.cubussapiens.modembed.model.editor.GenericModelWizard;
import hu.cubussapiens.modembed.model.highlevelprogram.HighLevelProgram;
import hu.cubussapiens.modembed.model.highlevelprogram.HighLevelProgramFactory;
import hu.cubussapiens.modembed.ui.descriptor.IModelDescriptor;

/**
 * @author balazs.grill
 *
 */
public class HighLevelProgramModelWizard extends GenericModelWizard {

    @Override
    protected IModelDescriptor getDescriptor() {
        return new IModelDescriptor() {

            @Override
            public EObject createInitialModel() {
                HighLevelProgram program = HighLevelProgramFactory.eINSTANCE.createHighLevelProgram();
                program.setName("Program");
                return program;
            }
        };
    }
}
