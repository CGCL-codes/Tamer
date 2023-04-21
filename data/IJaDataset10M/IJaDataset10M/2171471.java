package net.sourceforge.aidungeon.common.map.path;

import java.util.ArrayList;
import java.util.List;
import net.sourceforge.aidungeon.common.map.CharacterSimulationMap;
import net.sourceforge.aidungeon.common.map.mapCoordinate.InvalidMapCoordinateException;
import net.sourceforge.aidungeon.common.map.mapCoordinate.MapCoordinate;
import net.sourceforge.aidungeon.common.map.mapCoordinate.MapCoordinateNotVisitedByPlayerException;
import net.sourceforge.aidungeon.common.map.mapObject.terrain.TerrainMapObject;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.PathFinder;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

public class PathSolver implements TileBasedMap {

    private final PathFinder pathFinder;

    private final CharacterSimulationMap characterSimulationMap;

    public PathSolver(CharacterSimulationMap characterSimulationMap) {
        this.characterSimulationMap = characterSimulationMap;
        this.pathFinder = new AStarPathFinder(this, this.characterSimulationMap.getHeight() * this.characterSimulationMap.getWidth(), false);
    }

    public List<MapCoordinate> findPath(MapCoordinate playerCoordinate, MapCoordinate targetCoordinate) throws MapCoordinateNotVisitedByPlayerException, NoPathToMapCoordinateException {
        List<MapCoordinate> pathCoordinates = new ArrayList<MapCoordinate>();
        if (this.characterSimulationMap.isVisited(targetCoordinate) == false) {
            throw new MapCoordinateNotVisitedByPlayerException(this.characterSimulationMap.getCharacter(), targetCoordinate);
        }
        if (playerCoordinate.equals(targetCoordinate)) {
            return pathCoordinates;
        } else if (playerCoordinate.isNextTo(targetCoordinate, false)) {
            pathCoordinates.add(targetCoordinate);
            return pathCoordinates;
        }
        MapCoordinate originalTargerCoordinate = targetCoordinate;
        if (this.characterSimulationMap.isObstacle(targetCoordinate)) {
            if (this.characterSimulationMap.isVisited(targetCoordinate.getNorth()) && !this.characterSimulationMap.isObstacle(targetCoordinate.getNorth())) {
                targetCoordinate = targetCoordinate.getNorth();
            } else if (this.characterSimulationMap.isVisited(targetCoordinate.getEast()) && !this.characterSimulationMap.isObstacle(targetCoordinate.getEast())) {
                targetCoordinate = targetCoordinate.getEast();
            } else if (this.characterSimulationMap.isVisited(targetCoordinate.getSouth()) && !this.characterSimulationMap.isObstacle(targetCoordinate.getSouth())) {
                targetCoordinate = targetCoordinate.getSouth();
            } else if (this.characterSimulationMap.isVisited(targetCoordinate.getWest()) && !this.characterSimulationMap.isObstacle(targetCoordinate.getWest())) {
                targetCoordinate = targetCoordinate.getWest();
            }
        }
        Mover mover = new Mover() {
        };
        Path path = this.pathFinder.findPath(mover, playerCoordinate.getX(), playerCoordinate.getY(), targetCoordinate.getX(), targetCoordinate.getY());
        if (path != null) {
            try {
                for (int i = 1; i < path.getLength(); i++) {
                    pathCoordinates.add(this.characterSimulationMap.getMapCoordinate(path.getX(i), path.getY(i)));
                }
                if (originalTargerCoordinate != targetCoordinate) {
                    pathCoordinates.add(originalTargerCoordinate);
                }
            } catch (InvalidMapCoordinateException e) {
            }
        } else {
            throw new NoPathToMapCoordinateException(targetCoordinate);
        }
        return pathCoordinates;
    }

    @Override
    public boolean blocked(PathFindingContext context, int tx, int ty) {
        try {
            MapCoordinate coordinate = this.characterSimulationMap.getMapCoordinate(tx, ty);
            if (this.characterSimulationMap.isVisible(coordinate)) {
                return this.characterSimulationMap.isObstacle(coordinate);
            } else {
                if (this.characterSimulationMap.isVisited(coordinate) == false) {
                    return true;
                } else {
                    TerrainMapObject terrain = this.characterSimulationMap.getMapObject(coordinate, TerrainMapObject.class);
                    return terrain.isObstacle();
                }
            }
        } catch (InvalidMapCoordinateException e) {
            return true;
        }
    }

    @Override
    public float getCost(PathFindingContext context, int tx, int ty) {
        return 1;
    }

    @Override
    public int getHeightInTiles() {
        return this.characterSimulationMap.getHeight();
    }

    @Override
    public int getWidthInTiles() {
        return this.characterSimulationMap.getWidth();
    }

    @Override
    public void pathFinderVisited(int x, int y) {
    }
}
