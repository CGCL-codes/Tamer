package eu.irreality.dai.world.gen.strategy;

import java.util.Iterator;
import eu.irreality.dai.gameplay.entities.Stairs;
import eu.irreality.dai.gameplay.world.LevelDescriptor;
import eu.irreality.dai.util.Position;
import eu.irreality.dai.util.Roll;
import eu.irreality.dai.world.gen.LevelGenerator;
import eu.irreality.dai.world.level.Level;

public class SimpleStairGenerationStrategy implements StairGenerationStrategy {

    private boolean isEmpty(Level l, Position p) {
        return (l.get(p).size() == 1 && l.get(p).get(0).equals(LevelGenerator.GROUND));
    }

    private boolean isValid(Level l, Position p) {
        if (!isEmpty(l, p)) return false;
        for (Iterator<Position> iterator = p.adjacentPositions(); iterator.hasNext(); ) {
            Position p2 = (Position) iterator.next();
            if (p2.inRange(l.getRows(), l.getCols())) if (!isEmpty(l, p2)) return false;
        }
        return true;
    }

    private void addOneStairs(Level l, boolean up, LevelDescriptor destination) {
        int rows = l.getRows();
        int cols = l.getCols();
        int seedRow = Roll.randomInt(0, l.getRows() - 1);
        int seedCol = Roll.randomInt(0, l.getCols() - 1);
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int row = Math.abs((seedRow + r) % rows);
                int col = Math.abs((seedCol + c) % cols);
                if (isValid(l, new Position(row, col))) {
                    Stairs s = new Stairs(up, l.getDescriptor(), destination);
                    l.addEntity(s, row, col);
                    return;
                }
            }
        }
    }

    @Override
    public void addStairs(Level l) {
        LevelDescriptor ld = l.getDescriptor();
        for (LevelDescriptor upDesc : ld.getUpstairsConnections()) {
            addOneStairs(l, true, upDesc);
        }
        for (LevelDescriptor downDesc : ld.getDownstairsConnections()) {
            addOneStairs(l, false, downDesc);
        }
    }
}
