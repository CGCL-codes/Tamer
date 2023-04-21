package drk.game;

import java.awt.event.KeyEvent;
import java.io.*;
import drk.KarnaughLog;
import drk.*;
import drk.maze.*;
import drk.circuit.*;
import drk.sound.*;
import drk.menu.*;
import javax.media.opengl.*;
import java.awt.event.*;
import java.util.*;

public class KarnaughGame extends MazeGame implements Updatable, MouseListener {

    public int currentOutput = 0;

    public int Score = 0;

    public int tempScore = 0;

    protected long Time;

    public boolean paused;

    long cycleTime = 0;

    private long lastUpdate = 0;

    public MazeItem inputSource = null;

    public KarnaughOverlays overlays;

    protected int songID = 0;

    public boolean hasWire = false;

    protected String mapName = "map01.kar";

    protected static String tempmapName = "map01.kar";

    private long longest = 0;

    Bunny bunneh = null;

    public static final int GAME_WIDTH = 1024;

    public static final int GAME_HEIGHT = 768;

    public void discardWire() {
        inputSource = null;
        hasWire = false;
        doubleClickLeft = doubleClickRight = leftClick = rightClick = false;
        overlays.currentCursor = overlays.cursor;
    }

    public void initialize(GL gl) {
        super.initialize(gl);
        overlays = new KarnaughOverlays(this);
        overlays.initialize(gl);
    }

    public KarnaughMaze getMaze() {
        return (KarnaughMaze) m;
    }

    public KarnaughGame() {
        Score = 0;
        Time = 0;
        paused = false;
        hasWire = false;
        songID = 0;
        inputSource = null;
        lastUpdate = 0;
        cycleTime = 0;
        currentOutput = 0;
    }

    public void setSingleMapCampaign(KarnaughGame m, String map) {
        KarnaughLog.clearLog();
        KarnaughLog.log("Starting Dr. Karnaugh's Lab");
        KarnaughLog.log("" + map);
        m.loadMap(map);
        m.camera.fovy = 30;
        m.doMain(GAME_WIDTH, GAME_HEIGHT, null, true);
    }

    public void render(GL gl) {
        super.render(gl);
        if (bunneh != null) bunneh.render(gl);
        overlays.render(gl);
    }

    public void gameOver() {
        if (Menu.userName == null) {
        } else {
            int asdkjghskdjgh = new Random().nextInt(255);
            if (asdkjghskdjgh < 0) {
                asdkjghskdjgh = -asdkjghskdjgh;
            }
            String newHash = RankedGame.hash(Score, Menu.userName, asdkjghskdjgh);
            String postScore = RankedGame.postHighScore(Score, Menu.userName, asdkjghskdjgh, newHash);
            System.out.println("Hash key is " + newHash);
            System.out.println("" + postScore);
            String[][] getScore = RankedGame.getHighScores();
        }
    }

    public void die() {
        frameClose();
        SoundStreamer.stopPlayImmediately(songID);
        GameOver.gameisOver();
        gameOver();
    }

    public void winMap() {
        if (minutesLeft() == 0 && secondsLeft() < 30) {
            Score += 30;
        }
        Score += secondsLeft();
        Score += minutesLeft() * 65;
        if (!((KarnaughMaze) this.m).nextmap.equals("LAST_LEVEL")) {
            loadMap(((KarnaughMaze) this.m).mapDirectory + ((KarnaughMaze) this.m).nextmap);
            mapName = ((KarnaughMaze) this.m).nextmap;
            tempmapName = ((KarnaughMaze) this.m).nextmap;
        } else {
            SoundStreamer.stopPlayImmediately(songID);
            frameClose();
            WinMenu.WinGame();
            Menu mainMenu = new Menu();
            mainMenu.GameGUI();
            gameOver();
        }
    }

    public boolean loadMap(String m) {
        LogicInput.inputNumber = 0;
        hasWire = false;
        cycleTime = 0;
        currentOutput = 0;
        inputSource = null;
        bunneh = null;
        if (overlays != null) overlays.currentCursor = overlays.cursor;
        this.m = (KarnaughMaze) KarnaughMaze.loadMaze(m);
        if (this.m == null) return false;
        int rm = 0;
        KarnaughMaze km = (KarnaughMaze) this.m;
        for (rm = 0; rm < this.m.getWidth() * this.m.getHeight(); rm++) {
            if (this.m.getRoom(rm).getItem() instanceof Entrance) break;
        }
        ec.Position = this.m.getRoomMiddle(this.m.getRoom(rm));
        ec.setHeight(1.5);
        Time = km.timelimit * 1000;
        lastUpdate = System.currentTimeMillis();
        this.m.setDeltaTimer(this.frameTimer);
        final File f = new File("drk/sound/music/" + km.songfile + ".mp3");
        SoundStreamer.stopPlayImmediately(songID);
        if (f != null) {
            songID = SoundStreamer.playThreadedStreamedLooped(f);
        } else {
            KarnaughLog.log("Could not open song file");
        }
        return true;
    }

    public void frameVisible(int x) {
        super.frameVisible(x);
    }

    public void unPause() {
        lastUpdate = System.currentTimeMillis();
        paused = false;
    }

    public void update() {
        tempScore = Score;
        if (paused) return;
        super.update();
        if (bunneh != null) bunneh.update();
        if (!hasWire) {
            overlays.currentCursor = overlays.cursor;
            if (Time > 0) updateInfo(""); else updateInfo("RUN! The Bunny is after you!");
        } else {
            if ((doubleClickLeft || doubleClickRight)) discardWire();
            overlays.currentCursor = overlays.wireHand;
            updateInfo("Double Click to discard wire");
        }
        if (isKeyPressed(KeyEvent.VK_ESCAPE)) {
            SoundStreamer.stopPlayImmediately(songID);
            frameClose();
            Menu mainMenu = new Menu();
            mainMenu.GameGUI();
        }
        if (isKeyPressed(KeyEvent.VK_R)) {
            SoundStreamer.stopPlayImmediately(songID);
            if (tempScore == 0) {
                if (Score == 0) tempScore = Score; else tempScore = Score - 10;
            } else tempScore = tempScore - 10;
            frameClose();
            KarnaughGame m = new KarnaughGame();
            m.Score = this.tempScore;
            m.loadMap(mapName);
            m.camera.fovy = 30;
            m.doMain(GAME_WIDTH, GAME_HEIGHT, null, true);
        }
        MazeItem x = ((KarnaughMaze) m).getCurrentRoom().getItem();
        if (x != null) {
            if (x.isMazeItemHighlighted(this)) {
                x.onMazeItemHighlighted(this);
            }
        }
        leftClick = false;
        rightClick = false;
        doubleClickLeft = false;
        doubleClickRight = false;
        if (System.currentTimeMillis() - cycleTime >= 2000) {
            cycleTime = System.currentTimeMillis();
            currentOutput = (currentOutput + 1) % truthTableSize();
            updateTT(currentOutput, ((KarnaughMaze) m).numInputs, ((KarnaughMaze) m).solution[currentOutput]);
        }
        if (Time > 0) {
            long frametime;
            Time -= (frametime = System.currentTimeMillis() - lastUpdate);
            if (frametime > longest && frametime < 1000) {
                longest = frametime;
                KarnaughLog.log("longest frametime: " + longest + "ms");
            }
            if (Time <= 0) {
                bunneh = new Bunny(this);
                System.out.println("new bunny");
                Time = 0;
                final File f = new File("drk/sound/music/mission.mp3");
                SoundStreamer.stopPlayImmediately(songID);
                if (f != null) {
                    songID = SoundStreamer.playThreadedStreamedLooped(f);
                } else {
                    KarnaughLog.log("Could not open song file");
                }
            }
        }
        lastUpdate = System.currentTimeMillis();
    }

    public long minutesLeft() {
        return (Time / 1000) / 60;
    }

    public long secondsLeft() {
        return (Time / 1000) % 60;
    }

    public static void mainGame() {
        KarnaughLog.clearLog();
        KarnaughLog.log("Starting Dr. Karnaugh's Lab");
        KarnaughGame m = new KarnaughGame();
        m.loadMap("map01.kar");
        m.camera.fovy = 30;
        m.doMain(GAME_WIDTH, GAME_HEIGHT, null, true);
        Menu.story.dispose();
    }

    public boolean getCurrentSolution() {
        return ((KarnaughMaze) m).solution[currentOutput];
    }

    public int truthTableSize() {
        return ((KarnaughMaze) m).solution.length;
    }

    public static void main(String args[]) {
        KarnaughLog.clearLog();
        KarnaughLog.log("Starting Dr. Karnaugh's Lab");
        KarnaughGame m = new KarnaughGame();
        m.camera.fovy = 30;
        m.loadMap("map03.kar");
        m.doMain(GAME_WIDTH, GAME_HEIGHT, null, true);
        for (boolean b : ((KarnaughMaze) m.m).solution) {
            System.out.println(b);
        }
    }
}
