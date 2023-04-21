package net.taylor.tracker.entity.editor;

import javax.ejb.Local;
import net.taylor.richfaces.RootTreeNode;
import net.taylor.seam.AbstractEditor;
import net.taylor.tracker.entity.Tag;

/**
 * The local interface for a CRUD user interface.
 *
 * @author jgilbert01
 * @version $Id: Editor.javajet,v 1.20 2007/12/27 20:48:54 jgilbert01 Exp $
 * @generated
 */
@Local
public interface TagEditor extends AbstractEditor<Tag> {

    /** @generated */
    RootTreeNode initTagTree();

    /** @generated */
    Tag initTag();
}
