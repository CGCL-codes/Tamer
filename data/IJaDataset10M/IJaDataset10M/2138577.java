package name.huzhenbo.java.patterns.builder;

interface MazeBuilder {

    void createDoor();

    void createCeil();

    void createWall();

    Maze getMaze();
}
