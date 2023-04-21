package rino.sudoku.model.generators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import rino.sudoku.model.Cell;
import rino.sudoku.model.Cube;
import rino.sudoku.model.GameBoard;

/**
 * @author rino
 *
 */
public class CubeBasedGenerator extends PuzzleGenerator {

    private int takeAways;

    private Random rand;

    private HashMap<Cube, ArrayList<Location>> points;

    /**
	 * @param board
	 */
    public CubeBasedGenerator(GameBoard board) {
        super(board);
        rand = new Random();
        points = new HashMap<Cube, ArrayList<Location>>();
    }

    @Override
    protected void characterizePuzzle(int difficulty) {
        switch(difficulty) {
            case GameBoard.HARD:
                {
                    takeAways = 7;
                    break;
                }
            case GameBoard.MEDIUM:
                {
                    takeAways = 6;
                    break;
                }
            default:
                {
                    takeAways = 5;
                }
        }
        List<List<Cube>> cuberows = getBoard().getRowsOfCubes();
        for (int i = 0; i < 3; i++) {
            List<Cube> cubes = cuberows.get(i);
            for (int j = 0; j < 3; j++) {
                Cube cube = cubes.get(j);
                if (i == 2) {
                    ArrayList<Location> locs = copyList(points.get(cuberows.get(0).get(j)));
                    points.put(cube, locs);
                    if (j == 2) {
                        ArrayList<Location> tmp = points.get(cube);
                        points.put(cube, points.get(cuberows.get(2).get(0)));
                        points.put(cuberows.get(2).get(0), tmp);
                        for (Cube c : cuberows.get(i)) {
                            tmp = points.get(c);
                            for (Location l : tmp) {
                                l.row = 2 - l.row;
                                l.column = 2 - l.column;
                            }
                            points.put(c, tmp);
                        }
                    }
                } else {
                    points.put(cube, new ArrayList<Location>());
                    int takeAwaysAug = takeAways - rand.nextInt(2);
                    while (takeAwaysAug > 0) {
                        Location point = new Location(rand.nextInt(3), rand.nextInt(3));
                        while (points.get(cube).contains(point)) {
                            point.set(rand.nextInt(3), rand.nextInt(3));
                        }
                        points.get(cube).add(point);
                        takeAwaysAug--;
                    }
                }
            }
        }
        for (Cube cube : points.keySet()) {
            List<Location> locs = points.get(cube);
            for (Location loc : locs) {
                Cell cell = cube.getCell(loc.row, loc.column);
                cell.setLocked(false);
            }
        }
    }

    private ArrayList<Location> copyList(ArrayList<Location> list) {
        ArrayList<Location> result = new ArrayList<Location>();
        for (Location l : list) {
            result.add(new Location(l.row, l.column));
        }
        return result;
    }

    private class Location {

        int column;

        int row;

        Location(int row, int column) {
            this.row = row;
            this.column = column;
        }

        void set(int row, int column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + column;
            result = prime * result + row;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            final Location other = (Location) obj;
            if (column != other.column) return false;
            if (row != other.row) return false;
            return true;
        }
    }
}
