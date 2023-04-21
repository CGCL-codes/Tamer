package juegos;

import java.awt.Color;
import java.awt.image.ImageObserver;
import java.util.ArrayList;

public interface Stage extends ImageObserver {

    public static final int WIDTH = 1024;

    public static final int HEIGHT = 768;

    public static final int PLAY_HEIGHT = 608;

    public static final int CELL = 32;

    public static final char GOLD = 'G';

    public static final char LIFE = 'L';

    public static final char LADDER = 'H';

    public static final char FLOOR = 'x';

    public static final char SUELO = 'X';

    public static final char HOLE = '0';

    public static final char HOLE1 = '1';

    public static final char HOLE2 = '2';

    public static final char HOLE3 = '3';

    public static final char P_RESPAWN = 'p';

    public static final char BGROUND = 'o';

    public static final char DOOR = 'D';

    public static final char M_RESPAWN = 'm';

    public static final int FLOOR_HOLETIME = 4;

    public static final int MONSTER_CLIMBTIME = 7;

    public static final Color[] colorPlayer = { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW };

    public void addChanges(int x, int y, char HOLE);

    public void getJewel();

    public ArrayList getMonsters();

    public int getNumPlayers();

    public SpriteCache getSpriteCache();

    public SoundCache getSoundCache();

    public char[][] getLevel();

    public ArrayList getPlayers();

    public boolean transitable(int x, int y);

    public void gameOver();

    public void removeHole(int x, int y);

    public void addHole(int x, int y);
}
