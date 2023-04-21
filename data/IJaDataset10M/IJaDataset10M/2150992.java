package hu.cubussapiens.modembed.model.editor.internal;

import org.eclipse.emf.ecore.EObject;
import hu.cubussapiens.modembed.model.assembly.memmapping.ASMMapping;
import hu.cubussapiens.modembed.model.assembly.memmapping.MemmappingFactory;
import hu.cubussapiens.modembed.model.editor.GenericModelWizard;
import hu.cubussapiens.modembed.ui.descriptor.IModelDescriptor;

/**
 * @author balazs.grill
 *
 */
public class ASMMemoryMappingModelWizard extends GenericModelWizard {

    @Override
    protected IModelDescriptor getDescriptor() {
        return new IModelDescriptor() {

            @Override
            public EObject createInitialModel() {
                ASMMapping am = MemmappingFactory.eINSTANCE.createASMMapping();
                am.setName("mapping");
                return am;
            }
        };
    }
}
