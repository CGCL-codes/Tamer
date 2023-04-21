package de.uka.aifb.owl.module;

import org.eclipse.emf.ecore.EObject;
import com.ontoprise.ontostudio.owl.gui.navigator.AbstractOwlEntityTreeElement;

public class DropTransferObject {

    private EObject objectToTransfer;

    private AbstractOwlEntityTreeElement treeElement;

    private boolean moduleTransfer;

    public DropTransferObject(AbstractOwlEntityTreeElement treeElement) {
        this.treeElement = treeElement;
    }

    public DropTransferObject(EObject objectToTransfer, AbstractOwlEntityTreeElement treeElement) {
        this.objectToTransfer = objectToTransfer;
        this.treeElement = treeElement;
    }

    public EObject getObjectToTransfer() {
        return objectToTransfer;
    }

    public void setObjectToTransfer(EObject objectToTransfer) {
        this.objectToTransfer = objectToTransfer;
    }

    public AbstractOwlEntityTreeElement getTreeElement() {
        return treeElement;
    }

    public boolean isModuleTransfer() {
        return moduleTransfer;
    }

    protected void setModuleTransfer(boolean moduleTransfer) {
        this.moduleTransfer = moduleTransfer;
    }
}
