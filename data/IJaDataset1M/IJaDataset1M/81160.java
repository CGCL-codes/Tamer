package de.uka.aifb.owl.gui.actions;

import org.eclipse.jface.viewers.StructuredSelection;
import com.ontoprise.ontostudio.owl.gui.navigator.AbstractOwlEntityTreeElement;

public class CreateModuleActionFromTree extends CreateModuleAction {

    @Override
    protected String getNamespace() {
        AbstractOwlEntityTreeElement treeElement = (AbstractOwlEntityTreeElement) ((StructuredSelection) actualSelection).getFirstElement();
        return treeElement.getNamespace();
    }
}
