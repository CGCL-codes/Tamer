package org.jhotdraw.draw.event;

import org.jhotdraw.draw.*;
import javax.swing.undo.*;

/**
 * An {@code UndoableEdit} event which can undo a change of a {@link Figure}
 * attribute.
 *
 * @author  Werner Randelshofer
 * @version $Id: AttributeChangeEdit.java 717 2010-11-21 12:30:57Z rawcoder $
 */
public class AttributeChangeEdit<T> extends AbstractUndoableEdit {

    private Figure owner;

    private AttributeKey<T> name;

    private T oldValue;

    private T newValue;

    /** Creates a new instance. */
    public AttributeChangeEdit(Figure owner, AttributeKey<T> name, T oldValue, T newValue) {
        this.owner = owner;
        this.name = name;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    public String getPresentationName() {
        return "Eigenschaft ändern";
    }

    @Override
    public void redo() throws CannotRedoException {
        super.redo();
        owner.willChange();
        owner.set(name, newValue);
        owner.changed();
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();
        owner.willChange();
        owner.set(name, oldValue);
        owner.changed();
    }
}
