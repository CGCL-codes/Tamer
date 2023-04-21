package gdi.ws0809.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
* Public tests for the 2nd extended Sokoban implementation level
* 
* @author Steven Arzt, Oren Avni
* @version 1.0
*/
public class SokobanTestExtended2 {

    /**
		 * the tested Sokoban implementation
		 */
    private SokobanTest testee = null;

    private static File validLevel;

    private static String validLevelString = "####\n# .#\n#  ###\n#*@  #\n#  $ #\n#  ###\n####\n";

    private static String validLevelMovedString = "####\n# .#\n#  ###\n#* @ #\n#  $ #\n#  ###\n####\n";

    private static File highScoreFile;

    private static File levelDir;

    private static File saveFile;

    @BeforeClass
    public static void init() {
        try {
            validLevel = new File(ClassLoader.getSystemClassLoader().getResource("valid.txt").toURI());
            levelDir = new File(ClassLoader.getSystemClassLoader().getResource("testlevel").toURI());
            saveFile = new File(levelDir, "save.sok");
            highScoreFile = new File(levelDir, "highscore.txt");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        SokobanTestAdapter.init();
    }

    @Before
    public void before() {
        testee = new SokobanTestAdapter();
    }

    /**
		 * Requirement "undo/redo"
		 */
    @Test
    public void undo() throws Exception {
        testee.loadLevel(validLevel);
        testee.moveWorker('R');
        testee.undoLastMove();
        assertEquals("Level should be in initial state after undo.", validLevelString, testee.currentLevelToString());
    }

    @Test(expected = Exception.class)
    public void undoFail() throws Exception {
        testee.loadLevel(validLevel);
        testee.undoLastMove();
    }

    @Test
    public void redo() throws Exception {
        testee.loadLevel(validLevel);
        testee.moveWorker('R');
        testee.undoLastMove();
        testee.redoLastUndoneMove();
        assertEquals("Move should be performed after undo / redo.", validLevelMovedString, testee.currentLevelToString());
    }

    private String textFileToString(File file) {
        BufferedReader rdr = null;
        try {
            try {
                rdr = new BufferedReader(new FileReader(file));
                String s = "", p = "";
                while ((p = rdr.readLine()) != null) s += p + "\n";
                return s;
            } finally {
                if (rdr != null) rdr.close();
            }
        } catch (IOException ex) {
            fail("Could not read text file");
            return null;
        }
    }

    /**
		 * Requirement "saveGame" & "loadGame"
		 * @throws Exception 
		 */
    @Test
    public void saveGame() throws Exception {
        testee.setLevelDir(levelDir);
        testee.startNextLevel();
        testee.saveGame(saveFile);
        testee.moveWorker('R');
        long timestamp = saveFile.lastModified();
        String data = textFileToString(saveFile);
        testee.saveGame(saveFile);
        assertFalse("Nothing has been written to save file", timestamp == 0L);
        assertFalse("Saved level data not changed", textFileToString(saveFile).equals(data));
    }

    @Test
    public void loadGame() throws Exception {
        testee.setLevelDir(levelDir);
        testee.startNextLevel();
        testee.moveWorker('R');
        testee.setPlayerName("playername");
        testee.saveGame(saveFile);
        testee.loadLevel(validLevel);
        testee.loadGame(saveFile);
        assertEquals("Grid state properly loaded.", validLevelMovedString, testee.currentLevelToString());
        assertEquals("Step count not properly loaded.", 1, testee.getStepsInCurrentLevel());
    }

    /**
		 * Requirement "highscoreTime"
		 */
    @Test
    public void solutionTime() throws Exception {
        if (highScoreFile.exists()) highScoreFile.delete();
        testee.setLevelDir(levelDir);
        testee.startNextLevel();
        testee.setPlayerName("testplayer");
        testee.startNextLevel();
        testee.startNextLevel();
        long timestamp = System.currentTimeMillis();
        Thread.sleep(1500);
        testee.moveWorker('R');
        long delta = System.currentTimeMillis() - timestamp;
        try {
            testee.startNextLevel();
        } catch (Exception e) {
        }
        testee.writeHighScoreFile();
        assertTrue("No highscore file written", highScoreFile.exists());
        BufferedReader br = new BufferedReader(new FileReader(highScoreFile));
        String line = br.readLine();
        assertFalse("Empty highscore file", line == null);
        assertFalse("Empty line in highscore file", line.equals(""));
        String[] parts = line.split("\t");
        br.close();
        int timeTaken = Integer.parseInt(parts[3]);
        float diff = (float) Math.abs((timeTaken * 1000) - delta);
        assertTrue("Measured time differs more than 1s (" + diff + "ms) from actual passed time.", diff < 1000.0);
    }

    @Test
    public void moveSequence() throws Exception {
        testee.setLevelDir(levelDir);
        testee.startNextLevel();
        testee.moveWorkerSequence("U");
    }
}
