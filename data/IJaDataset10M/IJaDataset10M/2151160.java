package trb.trials4k;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author admin
 */
public class LevelCreator {

    public static void main(String[] args) {
        Level mainLevel = new Level();
        {
            LineArray lineArray = new LineArray();
            lineArray.points.add(new Tuple(0, 490));
            lineArray.points.add(new Tuple(10, 500));
            lineArray.points.add(new Tuple(770, 500));
            lineArray.points.add(new Tuple(770, 380));
            lineArray.points.add(new Tuple(1000, 380));
            lineArray.points.add(new Tuple(1200, 340));
            lineArray.points.add(new Tuple(1400, 250));
            lineArray.points.add(new Tuple(1400, 500));
            Level level = new Level();
            level.lineArrays.add(lineArray);
            level.checkpoints.add(new Checkpoint(200, 500));
            level.checkpoints.add(new Checkpoint(900, 380));
            level.checkpoints.add(new Checkpoint(2000, 250));
            level.circles.add(new Circle(500, 450, 50));
            level.circles.add(new Circle(2000, 500, 250));
            mainLevel.addLevel(level, 0, 0);
        }
        {
            LineArray lineArray = new LineArray();
            lineArray.points.add(new Tuple(0, 1000));
            lineArray.points.add(new Tuple(300, 1000));
            lineArray.points.add(new Tuple(500, 950));
            lineArray.points.add(new Tuple(700, 800));
            lineArray.points.add(new Tuple(700, 1000));
            lineArray.points.add(new Tuple(1000, 1000));
            lineArray.points.add(new Tuple(1000, 800));
            lineArray.points.add(new Tuple(1300, 950));
            lineArray.points.add(new Tuple(1500, 1000));
            lineArray.points.add(new Tuple(2000, 1000));
            Level level = new Level();
            level.lineArrays.add(lineArray);
            level.checkpoints.add(new Checkpoint(100, 1000));
            level.checkpoints.add(new Checkpoint(1700, 1000));
            mainLevel.addLevel(level, 2700, -200);
        }
        {
            LineArray lineArray = new LineArray();
            lineArray.points.add(new Tuple(0, 1000));
            lineArray.points.add(new Tuple(300, 1000));
            lineArray.points.add(new Tuple(300, 1300));
            lineArray.points.add(new Tuple(600, 1000));
            lineArray.points.add(new Tuple(600, 1300));
            lineArray.points.add(new Tuple(900, 1000));
            lineArray.points.add(new Tuple(900, 1300));
            lineArray.points.add(new Tuple(1200, 1000));
            lineArray.points.add(new Tuple(1200, 1300));
            lineArray.points.add(new Tuple(1500, 1000));
            lineArray.points.add(new Tuple(1500, 1300));
            lineArray.points.add(new Tuple(1800, 1000));
            Level level = new Level();
            level.lineArrays.add(lineArray);
            level.checkpoints.add(new Checkpoint(100, 1000));
            level.checkpoints.add(new Checkpoint(2000, 1000));
            mainLevel.addLevel(level, 5000, 0);
        }
        {
            LineArray lineArray = new LineArray();
            lineArray.points.add(new Tuple(0, 2000));
            lineArray.points.add(new Tuple(300, 2000));
            lineArray.points.add(new Tuple(600, 1500));
            lineArray.points.add(new Tuple(1000, 1500));
            lineArray.points.add(new Tuple(1250, 1000));
            lineArray.points.add(new Tuple(1500, 1000));
            Level level = new Level();
            level.lineArrays.add(lineArray);
            level.checkpoints.add(new Checkpoint(100, 2000));
            level.checkpoints.add(new Checkpoint(1500, 1000));
            mainLevel.addLevel(level, 7000, -1000);
        }
        {
            LineArray lineArray = new LineArray();
            lineArray.points.add(new Tuple(0, 2000));
            lineArray.points.add(new Tuple(300, 2000));
            lineArray.points.add(new Tuple(500, 1750));
            lineArray.points.add(new Tuple(750, 1800));
            lineArray.points.add(new Tuple(1000, 1750));
            lineArray.points.add(new Tuple(1250, 2000));
            lineArray.points.add(new Tuple(1500, 2000));
            Level level = new Level();
            level.lineArrays.add(lineArray);
            level.checkpoints.add(new Checkpoint(100, 2000));
            level.checkpoints.add(new Checkpoint(1500, 2000));
            level.circles.add(new Circle(750, 1650, 50));
            mainLevel.addLevel(level, 8500, -1500);
        }
        {
            LineArray lineArray = new LineArray();
            lineArray.points.add(new Tuple(0, 2000));
            lineArray.points.add(new Tuple(500, 2000));
            lineArray.points.add(new Tuple(700, 1800));
            lineArray.points.add(new Tuple(700, 1600));
            lineArray.points.add(new Tuple(600, 1500));
            LineArray lineArray2 = new LineArray();
            lineArray.points.add(new Tuple(0, 1500));
            lineArray.points.add(new Tuple(300, 1500));
            Level level = new Level();
            level.lineArrays.add(lineArray);
            level.lineArrays.add(lineArray2);
            level.checkpoints.add(new Checkpoint(100, 2000));
            level.checkpoints.add(new Checkpoint(1500, 2000));
            level.circles.add(new Circle(750, 1650, 50));
            mainLevel.addLevel(level, 10000, -1000);
        }
        try {
            FileOutputStream byteOut = new FileOutputStream(new File("/vrdev/fps/data.bin"));
            LevelIO.writeLevel(mainLevel, new DataOutputStream(byteOut));
            byteOut.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
