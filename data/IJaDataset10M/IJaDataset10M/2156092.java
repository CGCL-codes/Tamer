package ch.idsia.benchmark.mario.engine.level;

/**
 * Created by IntelliJ IDEA.
 * User: Sergey Karakovskiy, sergey.karakovskiy@gmail.com
 * Date: 4/16/11
 * Time: 4:12 PM
 * Package: ch.idsia.benchmark.mario.engine.level
 */
public class NewLevelGenerator implements ILevelGenerator {

    public Level generateLevel() {
        final int height = 15;
        final int length = 320;
        Level level = new Level(length, height);
        return level;
    }

    public int[] giveLevelComponents() {
        return new int[0];
    }

    public void buildLevelPart() {
    }
}
