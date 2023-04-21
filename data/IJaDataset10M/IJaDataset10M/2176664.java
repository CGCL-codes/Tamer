package org.argouml.uml;

import org.argouml.util.ChangeRegistry;
import ru.novosoft.uml.MElementEvent;
import ru.novosoft.uml.MElementListener;

/** This class holds the information about the saving state of the current UML project.
 *  The state is changed by every change made either to the model or to its diagrams.
 *
 * @see org.argouml.kernel.Project
 * @see ChangeRegistry
 */
public class UMLChangeRegistry extends ChangeRegistry implements MElementListener {

    public UMLChangeRegistry() {
        super();
    }

    public void propertySet(MElementEvent mee) {
        setChangeFlag(true);
    }

    public void listRoleItemSet(MElementEvent mee) {
    }

    public void recovered(MElementEvent mee) {
    }

    public void removed(MElementEvent mee) {
        setChangeFlag(true);
    }

    public void roleAdded(MElementEvent mee) {
        setChangeFlag(true);
    }

    public void roleRemoved(MElementEvent mee) {
        setChangeFlag(true);
    }
}
