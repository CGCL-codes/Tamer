package name.huzhenbo.java.patterns.factorymethod;

class BombedMazeGame implements MazeGame {

    public Maze createMaze() {
        return new BombedMaze();
    }
}
