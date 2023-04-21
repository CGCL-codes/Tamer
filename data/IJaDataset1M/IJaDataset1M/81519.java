package org.musicnotation.gef.editpolicies;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.musicnotation.gef.Engraver;
import org.musicnotation.gef.commands.MusicElementCreateCommand;
import org.musicnotation.gef.editparts.score.MovementEditPart;
import org.musicnotation.gef.figures.ColumnFigure;
import org.musicnotation.model.ChordNote;
import org.musicnotation.model.Mob;
import org.musicnotation.model.MusicElement;
import org.musicnotation.model.MusicNotationFactory;
import org.musicnotation.model.Part;
import org.musicnotation.model.Pitch;
import org.musicnotation.model.Staff;
import org.musicnotation.model.helpers.PartHelper;

public class MovementLayoutEditPolicy extends XYLayoutEditPolicy {

    @Override
    protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
        return null;
    }

    @Override
    protected Command getCreateCommand(CreateRequest request) {
        Object newObject = request.getNewObject();
        if (MusicElement.class.isInstance(newObject)) {
            MusicElementCreateCommand result = new MusicElementCreateCommand();
            MusicElement newMusicElement = (MusicElement) newObject;
            Point location = request.getLocation();
            MovementEditPart movementEditPart = (MovementEditPart) getHost();
            for (Object child : movementEditPart.getMovementFigure().getColumns().getChildren()) {
                ColumnFigure column = (ColumnFigure) child;
                double left = column.getBounds().preciseX();
                double right = left + column.getBounds().preciseWidth();
                if ((location.preciseX() > left) && (location.preciseX() < right)) {
                    newMusicElement.setTime(column.getDescriptor().getTime());
                }
            }
            if (newMusicElement instanceof Mob) {
                Mob newMob = (Mob) newMusicElement;
                if (!newMob.getNotes().isEmpty()) {
                    ChordNote chordNote = newMob.getNotes().get(0);
                    Pitch pitch = MusicNotationFactory.eINSTANCE.createPitch();
                    pitch.setDiatonic(32);
                    chordNote.setPitch(pitch);
                }
            }
            int currentIndex = 0;
            int index = (int) (location.preciseY() / Engraver.STAFF_DISTANCE);
            for (Part part : PartHelper.getDescendantsAndSelf(movementEditPart.getMovement())) {
                if (part instanceof Staff) {
                    if (index == currentIndex) {
                        result.setParent(part);
                        break;
                    }
                    currentIndex++;
                }
            }
            result.setChild(newMusicElement);
            return result;
        } else {
            return null;
        }
    }
}
