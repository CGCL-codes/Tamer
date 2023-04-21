package sabokan.mazes;

import java.util.Arrays;
import java.util.Collections;

/**
 * Generates a random maze
 * @author anaka
 */
public class MazeGenerator {

    /**
     * Ugly but fast, represent up, down, left right
     */
    private static final Integer[] directions = new Integer[] { 1, 2, 3, 4 };

    /**
     * Generates a Maze with a default size 100 and prints the output to the System.out
     * Not very useful method
     */
    public void generateMaze() {
        final Maze maze = new Maze(101);
        Collections.shuffle(Arrays.asList(directions));
        for (Integer dir : directions) {
            generateMaze(maze.getStartX(), maze.getStartY(), dir, maze);
        }
        System.out.println(maze);
    }

    /**
     * Generates a Maze of the given size
     * @param dimension
     * @return 
     */
    public Maze generateSaveMaze(final int dimension) {
        final Maze maze = new Maze(dimension);
        Collections.shuffle(Arrays.asList(directions));
        for (Integer dir : directions) {
            generateMaze(maze.getStartX(), maze.getStartY(), dir, maze);
        }
        return maze;
    }

    /**
     * Private method that does actually BFS to generate the output maze 
     * @param x
     * @param y
     * @param direction
     * @param maze 
     */
    private void generateMaze(int x, int y, int direction, final Maze maze) {
        Collections.shuffle(Arrays.asList(directions));
        if (maze.move(x, y, direction)) {
            x = maze.calcX(x, direction);
            y = maze.calcY(y, direction);
            maze.clearWall(x, y, direction);
            for (Integer dir : directions) {
                generateMaze(x, y, dir, maze);
            }
        }
    }
}
