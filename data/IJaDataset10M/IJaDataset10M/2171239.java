package eu.irreality.dai.gameplay.controller.actions.movement;

import eu.irreality.dai.gameplay.entities.AliveEntity;
import eu.irreality.dai.gameplay.world.GameWorld;
import eu.irreality.dai.ui.keyboard.KeyProcessor;
import eu.irreality.dai.util.Position;

public class MoveSouthEastAction extends MovementAction {

    public MoveSouthEastAction(KeyProcessor kp, GameWorld world, AliveEntity entity) {
        super(kp, world, entity);
    }

    protected Position getDestinationPosition() {
        Position pos = entity.getPosition();
        Position destPos = new Position(pos.getRow() + 1, pos.getCol() + 1);
        return destPos;
    }
}
