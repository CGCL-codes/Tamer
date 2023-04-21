package tiled.mapeditor.undo;

import java.awt.Point;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import tiled.core.MapObject;
import tiled.mapeditor.Resources;

/**
 * Moves an object.
 */
public class MoveObjectEdit extends AbstractUndoableEdit {

    private final MapObject mapObject;

    private final Point moveDist;

    public MoveObjectEdit(MapObject mapObject, Point moveDist) {
        this.mapObject = mapObject;
        this.moveDist = moveDist;
    }

    public void undo() throws CannotUndoException {
        super.undo();
        mapObject.translate(-moveDist.x, -moveDist.y);
    }

    public void redo() throws CannotRedoException {
        super.redo();
        mapObject.translate(moveDist.x, moveDist.y);
    }

    public String getPresentationName() {
        return Resources.getString("action.object.move.name");
    }
}
