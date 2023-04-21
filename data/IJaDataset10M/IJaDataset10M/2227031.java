package gameEngine.entityClasses.onDragActions;

import gameEngine.entityClasses.Entity;
import gameEngine.gameMath.Point;

public class MoveToMousePositionOnDrag implements OnDragAction {

    private final Entity entity;

    public MoveToMousePositionOnDrag(Entity entity) {
        this.entity = entity;
    }

    public void onDrag(final double x, final double y) {
        entity.goToRelativePosition(x, y);
    }

    public void onDrag(final Point deltaPoint) {
        onDrag(deltaPoint.getX(), deltaPoint.getY());
    }

    public boolean actionEnded() {
        return false;
    }

    @Override
    public boolean canBeDeleted() {
        return false;
    }
}
