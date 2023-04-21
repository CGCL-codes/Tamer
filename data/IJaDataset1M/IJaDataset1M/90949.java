package net.taylor.tracker.entity.facts.editor;

import javax.ejb.Local;
import net.taylor.richfaces.RootTreeNode;
import net.taylor.seam.AbstractEditor;
import net.taylor.tracker.entity.facts.Activity;

/**
 * The local interface for a CRUD user interface.
 *
 * @author jgilbert
 * @generated
 */
@Local
public interface ActivityEditor extends AbstractEditor<Activity> {

    /** @generated */
    RootTreeNode initActivityTree();

    /** @generated */
    Activity initActivity();
}
