package supaplex;

import java.io.*;
import java.net.URL;
import javax.swing.JFrame;
import supaplex.enums.ActionType;

/**
 * 
 * @author DutchRemco
 * 
 * Creation date: 27-06-2009
 * 
 * History
 * -------
 * 
 * 03-07-2009 DR: For testing: Load a level from 63 format
 * 10-07-2009 DR: Started on reading demofiles
 * 
 */
public class Main extends JFrame {

    private static final long serialVersionUID = -3517095118544972608L;

    public Main() {
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) throws FileNotFoundException, IOException, Exception {
        int levelIndex = 23;
        URL levelsFileUrlLevels = Main.class.getResource("/resources/LEVELS.DAT");
        URL levelsFileUrlReplay = Main.class.getResource("/resources/JENS0009.SP");
        Level levelFromLevelSet = Level.getLevelFromLevelSet(levelsFileUrlLevels.getFile(), levelIndex);
        Level levelFromReplay = Level.getLevelFromReplay(levelsFileUrlReplay.getFile());
        Field field = new Field(levelFromReplay);
        field.Next(ActionType.Right);
    }
}
