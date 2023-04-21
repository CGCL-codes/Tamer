package org.freelords.map.paths;

import java.util.Collection;
import java.util.Collections;
import org.junit.Test;
import org.freelords.game.generator.RandomWalkMapGenerator;
import org.freelords.map.GameMap;
import org.freelords.map.TileType;
import org.freelords.util.Rand;
import org.freelords.util.geom.Direction;
import org.freelords.util.geom.Line;
import org.freelords.util.geom.Point;

/** Tests the various simple path calculators.
  *
  * We do not test the StackPathCalculator because this takes too much
  * preparation; After all, we need to set up a complete game with various valid
  * data.
  *
  * However, there are two simpler path calculators, the SimplePathCalculator,
  * and the RoadPathCalculator that just try to calculate a straight line or a
  * path hopefully suited for building a road. These two only require a map
  * which is relatively easy to generate.
  *
  * So the plan is: Generate a random map, put some roads through it (to make a
  * challenge for the RoadPathCalculator that should try to recycle roads and
  * intersections), calculate paths between random points, and output the result
  * for the tester to judge.
  *
  * @author Ulf Lorenz
  */
public class PathCalculatorTest {

    @Test
    public void testCalculators() {
        int width = 50;
        int height = 30;
        RandomWalkMapGenerator gen = new RandomWalkMapGenerator();
        gen.setWidth(width);
        gen.setHeight(height);
        GameMap map = gen.generateMap();
        for (int road = 0; road < 10; road++) {
            Point pos = new Point(Rand.GEN.nextInt(width), Rand.GEN.nextInt(height));
            Direction dir = Direction.values()[Rand.GEN.nextInt(8)];
            for (int i = 0; i < Math.max(width, height); i++) {
                if (pos.getX() >= width || pos.getX() < 0 || pos.getY() >= height || pos.getY() < 0) {
                    break;
                }
                if (map.getTile(pos.getX(), pos.getY()).getTileType() != TileType.WATER) {
                    map.getTile(pos.getX(), pos.getY()).setRoad(true);
                }
                pos = dir.shift(pos);
                if (Rand.GEN.nextInt(10) == 0) {
                    dir = dir.rotate(Rand.GEN.nextInt(3) - 2);
                }
            }
        }
        System.out.println("Generated map: \n\n");
        outputMap(map, null);
        Path p = null;
        Point start = null;
        Point end = null;
        while (p == null) {
            start = new Point(Rand.GEN.nextInt(width), Rand.GEN.nextInt(height));
            end = new Point(Rand.GEN.nextInt(width), Rand.GEN.nextInt(height));
            Line l = new Line(start, end);
            if (l.getLength() < Math.max(width, height) / 2) {
                continue;
            }
            SimplePathCalculator scalc = new SimplePathCalculator(start, map, Collections.singleton(TileType.WATER));
            p = scalc.calculatePath(end);
        }
        System.out.println("Path from " + start + " to " + end + "\n\n");
        System.out.println("SimplePathCalculator: \n\n");
        outputMap(map, p.getPointSequence());
        RoadPathCalculator rcalc = new RoadPathCalculator(start, map);
        System.out.println("RoadPathCalculator: \n\n");
        outputMap(map, rcalc.calculatePath(end).getPointSequence());
    }

    /** Prints a representation of the map with or withour path.
	  *
	  * We use excessive spacing to give the isometric feeling when looking at the map.
	  */
    private void outputMap(GameMap map, Collection<Point> path) {
        for (int y = 0; y < map.getHeight(); y++) {
            String line = "";
            if (y % 2 == 1) {
                line += " ";
            }
            for (int x = 0; x < map.getWidth(); x++) {
                Point pos = new Point(x, y);
                if (path != null && path.contains(pos)) {
                    line += "* ";
                } else if (map.getTile(x, y).hasRoad()) {
                    line += "+ ";
                } else {
                    line += map.getTile(x, y).getTileType().getCharCode() + " ";
                }
            }
            System.out.println(line);
        }
    }
}
