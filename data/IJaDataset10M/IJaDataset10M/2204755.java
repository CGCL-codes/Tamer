package name.huzhenbo.java.patterns.factorymethod;

import org.junit.Assert;
import org.junit.Test;

/**
 * Like other creational patterns, it deals with the problem of creating objects (products) without specifying the exact
 * class of object that will be created. The factory method design pattern handles this problem by defining a separate
 * method for creating the objects, which subclasses can then override to specify the derived type of product that will
 * be created.
 */
public class FactoryMethodTest {

    @Test
    public void should_work_like_this() {
        MazeGame game = new StandardMazeGame();
        MazeGame bombedGame = new BombedMazeGame();
        Assert.assertTrue(game.createMaze() instanceof StandardMaze);
        Assert.assertTrue(bombedGame.createMaze() instanceof BombedMaze);
    }
}
