package sk.tuke.ess.editor.base.components.history;

import javax.swing.*;
import javax.swing.undo.AbstractUndoableEdit;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: zladovan
 * Date: 14.5.2011
 * Time: 17:54
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseUndoableEdit extends AbstractUndoableEdit {

    private Date created = new Date();

    @Override
    public boolean canRedo() {
        return true;
    }

    public abstract Icon getActiveIcon();

    public abstract Icon getInactiveIcon();

    public Date getCreated() {
        return created;
    }
}
