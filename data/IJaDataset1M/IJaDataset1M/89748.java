package com.cosylab.vdct.model.edit;

import com.cosylab.vdct.model.Model;
import com.cosylab.vdct.model.primitive.Primitive;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/**
 *
 * @author Janez Golob
 */
public class AddPrimitiveEdit extends AbstractUndoableEdit {

    private Model model;

    private Primitive primitive;

    public AddPrimitiveEdit(Model model, Primitive primitive) {
        this.model = model;
        this.primitive = primitive;
    }

    public void undo() throws CannotUndoException {
        super.undo();
        model.removePrimitive(primitive);
    }

    public void redo() throws CannotRedoException {
        super.redo();
        model.addPrimitive(primitive);
    }
}
