package ch.unisi.inf.pfii.teamred.pacman.model;

import java.util.ArrayList;

/**
 * @author luca.vignola@lu.unisi.ch
 * 
 */
public abstract class GhostController extends Controller {

    public GhostController() {
        super();
    }

    protected final Direction directionToPacmanUsingNeighbors(final int depth) {
        Direction newDirection = computeDirectionToPacmanUsingNeighbors(depth);
        if (newDirection != null) {
            if (!isAiControlledCreatureVulnerable()) {
                if (canGoStraight(newDirection)) {
                    return newDirection;
                } else {
                    return null;
                }
            } else {
                Direction oppositeDirection = Direction.getOppositeDirection(newDirection);
                if (canGoStraight(oppositeDirection)) {
                    return oppositeDirection;
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    protected final Direction computeDirectionToPacmanUsingNeighbors(final int depth) {
        for (final Direction direction : getDirections()) {
            ArrayList<Block> blocks = getNeighbors().get(direction);
            int min = Math.min(depth, blocks.size());
            for (int i = 0; i < min; i++) {
                Block block = blocks.get(i);
                if (block.isTraversable()) {
                    TraversableBlock traversableBlock = (TraversableBlock) block;
                    if (traversableBlock.containsPacman()) {
                        return direction;
                    }
                }
            }
        }
        return null;
    }

    protected final Direction directionToPacmanUsingPosition() {
        Direction newDirection = computeDirectionToPacmanUsingPosition();
        if (newDirection != null) {
            if (!isAiControlledCreatureVulnerable()) {
                return newDirection;
            } else {
                if (canGoStraight(Direction.getOppositeDirection(newDirection))) {
                    return Direction.getOppositeDirection(newDirection);
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    protected final Direction computeDirectionToPacmanUsingPosition() {
        int monsterColumn = getAiControlledCreaturePosition().getColumn();
        int monsterRow = getAiControlledCreaturePosition().getRow();
        int pacmanColumn = getPlayerControlledCreaturePosition().getColumn();
        int pacmanRow = getPlayerControlledCreaturePosition().getRow();
        if (pacmanColumn > monsterColumn && getNeighbors().get(Direction.RIGHT).get(0).isTraversable()) {
            return Direction.RIGHT;
        } else if (pacmanColumn < monsterColumn && getNeighbors().get(Direction.LEFT).get(0).isTraversable()) {
            return Direction.LEFT;
        } else if (pacmanRow > monsterRow && getNeighbors().get(Direction.DOWN).get(0).isTraversable()) {
            return Direction.DOWN;
        } else if (pacmanRow < monsterRow && getNeighbors().get(Direction.UP).get(0).isTraversable()) {
            return Direction.UP;
        } else {
            return null;
        }
    }

    protected final Direction getRandomDirection() {
        do {
            int randomInt = getRandom().nextInt(getDirections().size());
            Direction randomDirection = getDirections().get(randomInt);
            if (getNeighbors().get(randomDirection).get(0) != null && getNeighbors().get(randomDirection).get(0).isTraversable() && randomDirection != Direction.getOppositeDirection(getAiControlledCreatureDirection())) {
                return randomDirection;
            }
        } while (true);
    }
}
