package edu.asu.vogon.quadriga.ids;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import edu.asu.vogon.digitalHPS.DigitalHPSPackage;
import edu.asu.vogon.digitalHPS.IElement;
import edu.asu.vogon.util.domain.EditingDomainManager;

public abstract class AIdRemove {

    protected IDRemover remover;

    public AIdRemove(IDRemover remover) {
        this.remover = remover;
    }

    protected abstract void processSubelements(IElement element);

    public void removeIds(IElement element) {
        removeId(element);
        processSubelements(element);
    }

    protected void removeId(IElement element) {
        if (element.getId() != null) {
            EditingDomain domain = EditingDomainManager.INSTANCE.getEditingDomain();
            Command cmd = SetCommand.create(domain, element, DigitalHPSPackage.Literals.IELEMENT__ID, null);
            domain.getCommandStack().execute(cmd);
        }
    }
}
