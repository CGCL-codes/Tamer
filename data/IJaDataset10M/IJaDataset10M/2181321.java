package org.eclipse.emf.examples.extlibrary.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.DeleteAction;
import org.eclipse.emf.examples.extlibrary.Library;

/**
 * The extended library example supports multi-rooted resources
 * and therefore this action allows the user to delete a library
 * root from the resource.
 */
public class ExtendedDeleteAction extends DeleteAction {

    /**
   * Constructor
   * 
   * @param domain
   *            the editing domain
   */
    public ExtendedDeleteAction(EditingDomain domain) {
        super(domain);
    }

    /**
   * Constructor
   */
    public ExtendedDeleteAction() {
        super();
    }

    @Override
    public Command createCommand(Collection<?> selection) {
        Command cmd = super.createCommand(selection);
        if (cmd != null) {
            ArrayList<Object> list = new ArrayList<Object>();
            if (selection != null && !selection.isEmpty()) {
                Iterator<?> iter = selection.iterator();
                while (iter.hasNext()) {
                    Object next = iter.next();
                    if (next instanceof Library) {
                        list.add(next);
                    }
                }
                if (!list.isEmpty()) {
                    Library library = (Library) list.get(0);
                    if (library.eResource() != null) {
                        Command removeLibraries = new RemoveLibraryCommand(domain, library.eResource().getContents(), list);
                        if (cmd instanceof UnexecutableCommand) {
                            return removeLibraries;
                        } else {
                            cmd.chain(removeLibraries);
                        }
                    }
                }
            }
        }
        return cmd;
    }
}
