package city.model.architectures.roads;

import java.util.ArrayList;
import city.shapes.Shape;
import city.shapes.basic.Cuboid;

public class NormalRoad extends Road {

    private static final double roadWidth = 7;

    private static final double markingWidth = 0.2;

    public NormalRoad() {
        super(1, 1);
    }

    @Override
    public void roadSegments() {
        double tarmacHeight = 0.1;
        double markingsHeight = 1;
        ArrayList<Shape> roadShapes = new ArrayList<Shape>();
        ArrayList<Shape> markingsShapes = new ArrayList<Shape>();
        double halfCellSize = cellSize / 2;
        double mid = halfCellSize - (roadWidth / 2);
        double markingsMid = halfCellSize - (markingWidth / 2);
        double markingLength = halfCellSize / 3;
        roadShapes.add(new Cuboid(mid, mid, -tarmacHeight, roadWidth, roadWidth, tarmacHeight));
        if (isDirection(Direction.WEST)) {
            roadShapes.add(new Cuboid(mid, 0, -tarmacHeight, roadWidth, halfCellSize, tarmacHeight));
            markingsShapes.add(new Cuboid(markingsMid, 0, -markingsHeight, markingWidth, markingLength / 2, markingsHeight));
            markingsShapes.add(new Cuboid(markingsMid, markingLength, -markingsHeight, markingWidth, markingLength, markingsHeight));
            markingsShapes.add(new Cuboid(markingsMid, 2.5 * markingLength, -markingsHeight, markingWidth, markingLength / 2, markingsHeight));
        }
        if (isDirection(Direction.SOUTH)) {
            roadShapes.add(new Cuboid(halfCellSize, mid, -tarmacHeight, halfCellSize, roadWidth, tarmacHeight));
            markingsShapes.add(new Cuboid(halfCellSize, markingsMid, -markingsHeight, markingLength / 2, markingWidth, markingsHeight));
            markingsShapes.add(new Cuboid(halfCellSize + markingLength, markingsMid, -markingsHeight, markingLength, markingWidth, markingsHeight));
            markingsShapes.add(new Cuboid(halfCellSize + 2.5 * markingLength, markingsMid, -markingsHeight, markingLength / 2, markingWidth, markingsHeight));
        }
        if (isDirection(Direction.EAST)) {
            roadShapes.add(new Cuboid(mid, halfCellSize, -tarmacHeight, roadWidth, halfCellSize, tarmacHeight));
            markingsShapes.add(new Cuboid(markingsMid, halfCellSize, -markingsHeight, markingWidth, markingLength / 2, markingsHeight));
            markingsShapes.add(new Cuboid(markingsMid, halfCellSize + markingLength, -markingsHeight, markingWidth, markingLength, markingsHeight));
            markingsShapes.add(new Cuboid(markingsMid, halfCellSize + 2.5 * markingLength, -markingsHeight, markingWidth, markingLength / 2, markingsHeight));
        }
        if (isDirection(Direction.NORTH)) {
            roadShapes.add(new Cuboid(0, mid, -tarmacHeight, halfCellSize, roadWidth, tarmacHeight));
            markingsShapes.add(new Cuboid(0, markingsMid, -markingsHeight, markingLength / 2, markingWidth, markingsHeight));
            markingsShapes.add(new Cuboid(markingLength, markingsMid, -markingsHeight, markingLength, markingWidth, markingsHeight));
            markingsShapes.add(new Cuboid(2.5 * markingLength, markingsMid, -markingsHeight, markingLength / 2, markingWidth, markingsHeight));
        }
        for (Shape shape : roadShapes) shape.setColour(0.1f, 0.1f, 0.1f, 1);
        for (Shape shape : markingsShapes) shape.setColour(1f, 1f, 1f, 1);
        roadShapes.addAll(markingsShapes);
        Shape[] shapes = roadShapes.toArray(new Shape[] {});
        setShapes(shapes);
    }
}
