package net.davidrobles.games.pacsim.maze;

public class MazeItem {

    private AbstractMaze maze;

    private MazeDifficulty difficulty;

    public MazeItem(AbstractMaze maze, MazeDifficulty difficulty) {
        this.maze = maze;
        this.difficulty = difficulty;
    }

    public MazeDifficulty getDifficulty() {
        return difficulty;
    }

    public AbstractMaze getLevel() {
        return maze;
    }

    public void setDifficulty(MazeDifficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setLevel(AbstractMaze maze) {
        this.maze = maze;
    }
}
