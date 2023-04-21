package tiled.mapeditor.undo;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import tiled.core.MapObject;
import tiled.core.ObjectGroup;
import tiled.mapeditor.Resources;

/**
 * Removes an object from an object group.
 *
 * @version $Id$
 */
public class RemoveObjectEdit extends AbstractUndoableEdit {

    private final ObjectGroup objectGroup;

    private final MapObject mapObject;

    public RemoveObjectEdit(ObjectGroup objectGroup, MapObject mapObject) {
        this.objectGroup = objectGroup;
        this.mapObject = mapObject;
    }

    public void undo() throws CannotUndoException {
        super.undo();
        objectGroup.addObject(mapObject);
    }

    public void redo() throws CannotRedoException {
        super.redo();
        objectGroup.removeObject(mapObject);
    }

    public String getPresentationName() {
        return Resources.getString("action.object.remove.name");
    }
}
