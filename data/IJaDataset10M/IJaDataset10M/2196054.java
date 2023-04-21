package org;

import org.entities.Item;
import org.entities.LevelObject;
import org.entities.Block;
import org.entities.Door;
import org.entities.Marine;
import org.entities.Player;
import org.entities.Sas;
import org.entities.Ship;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import java.util.ArrayList;

public class BlockMap {

    public static int mapWidth;

    public static int mapHeight;

    public static TiledMap tmap;

    public static LevelObject[][] blockAt;

    public ArrayList<LevelObject> tiles;

    public static ArrayList<LevelObject> entities;

    public static String leftNu, leftUpNu;

    public static String rightNu, rightUpNu;

    public Door doorLD, doorLU;

    public Door doorRD, doorRU;

    public static int thisLevelNu;

    private boolean noMoreItemInThisLevel;

    public int leftDX, leftDY, rightDX, rightDY, doorRightUpX, doorRightUpY, doorLeftUpX, doorLeftUpY;

    public static Music music;

    public boolean elevUp, elevDown;

    public Block startSignal, stopSignal;

    private int dUp1[] = { 0, 16, 16, 8, 16, 16, 0, 16 };

    private int dUp2[] = { 0, 8, 16, 1, 16, 16, 0, 16 };

    private int dDown1[] = { 0, 1, 16, 8, 16, 16, 0, 16 };

    private int dDown2[] = { 0, 8, 16, 16, 0, 16, 0, 8 };

    private int hDiag1[] = { 0, 16, 16, 0, 16, 16, 0, 16 };

    private int hDiag2[] = { 0, 0, 16, 16, 0, 16, 0, 0 };

    private int square[] = { 1, 1, 15, 1, 15, 15, 1, 15 };

    private int sDiag1[] = { 0, 0, 16, 0, 16, 8, 0, 0 };

    private int sDiag2[] = { 0, 0, 16, 0, 16, 16, 0, 8 };

    private int sDiag3[] = { 0, 0, 16, 0, 16, 8, 0, 17 };

    private int sDiag4[] = { 0, 0, 16, 0, 0, 8, 0, 0 };

    private int hDiag3[] = { 0, 0, 16, 0, 16, 16, 0, 0 };

    private int hDiag4[] = { 0, 0, 16, 0, 0, 16, 0, 0 };

    private int elevS1[] = { 0, 0, 16, 0, 16, 14, 0, 14 };

    private int sas[] = { -16, 0, 64, 0, 64, 64, -16, 64 };

    private int lvlScan;

    public static Player Samus;

    public BlockMap(String ref) throws SlickException {
        leftNu = "reset";
        rightNu = "reset";
        if (tiles != null) tiles.clear();
        if (entities != null) {
            entities.clear();
        }
        tmap = null;
        tmap = new TiledMap(ref, "data");
        thisLevelNu = Integer.parseInt(ref.substring(16, ref.length() - 4));
        System.out.printf("thisLevelNu=[%d]\n", thisLevelNu);
        System.out.printf("Current map is =[%s]\n", ref);
        music = new Music(Progress.setMusic());
        music.setVolume(Progress.musicVol / 20);
        mapWidth = tmap.getWidth() * tmap.getTileWidth();
        mapHeight = tmap.getHeight() * tmap.getTileHeight();
        tiles = new ArrayList<LevelObject>();
        entities = new ArrayList<LevelObject>();
        blockAt = new LevelObject[tmap.getWidth()][tmap.getHeight()];
        for (lvlScan = 0; lvlScan < 200; lvlScan++) {
            if (getBlockAt(lvlScan) != null && lvlScan == thisLevelNu) {
                System.out.printf("lvlScan and thisLevelNu are equal\n");
                noMoreItemInThisLevel = true;
            }
        }
        for (int x = 0; x < tmap.getWidth(); x++) {
            for (int y = 0; y < tmap.getHeight(); y++) {
                int tileID = tmap.getTileId(x, y, 0);
                int tileID2 = tmap.getTileId(x, y, 5);
                String finder = tmap.getTileProperty(tileID, "manual", "false");
                if ("true".equals(finder)) {
                    System.out.printf("tile=[%d]\n", tileID);
                }
                if (tileID == 1) {
                    entities.add(blockAt[x][y] = new Block(x * 16, y * 16, dUp1, "dUp"));
                }
                if (tileID == 2) {
                    entities.add(blockAt[x][y] = new Block(x * 16, y * 16, dUp2, "dUp"));
                }
                if (tileID == 3) {
                    entities.add(blockAt[x][y] = new Block(x * 16, y * 16, dDown1, "dDown"));
                }
                if (tileID == 4) {
                    entities.add(blockAt[x][y] = new Block(x * 16, y * 16, dDown2, "dDown"));
                }
                if (tileID == 5) {
                    entities.add(blockAt[x][y] = new Block(x * 16, y * 16, hDiag1, "hDiag1"));
                }
                if (tileID == 6) {
                    entities.add(blockAt[x][y] = new Block(x * 16, y * 16, hDiag2, "hDiag2"));
                }
                if (tileID == 7) {
                    entities.add(blockAt[x][y] = new Block(x * 16, y * 16, square, "square"));
                }
                if (tileID == 17) {
                    entities.add(blockAt[x][y] = new Block(x * 16, y * 16, sDiag1, "sDiag1"));
                }
                if (tileID == 18) {
                    entities.add(blockAt[x][y] = new Block(x * 16, y * 16, sDiag2, "sDiag2"));
                }
                if (tileID == 19) {
                    entities.add(blockAt[x][y] = new Block(x * 16, y * 16, sDiag3, "sDiag3"));
                }
                if (tileID == 20) {
                    entities.add(blockAt[x][y] = new Block(x * 16, y * 16, sDiag4, "sDiag4"));
                }
                if (tileID == 21) {
                    entities.add(blockAt[x][y] = new Block(x * 16, y * 16, hDiag3, "hdiag3"));
                }
                if (tileID == 22) {
                    entities.add(blockAt[x][y] = new Block(x * 16, y * 16, hDiag4, "hDiag4"));
                }
                if (tileID == 8) {
                    entities.add(new Door(x * 16, y * 16, square, "doorLD"));
                    entities.add(new Sas((x + 2) * 16, (y - 3) * 16, sas, "sasLeft"));
                    leftDX = x;
                    leftDY = y;
                    String doorLD = tmap.getTileProperty(tileID, "doorNu", "false");
                    leftNu = doorLD;
                }
                if (tileID == 9) {
                    entities.add(new Door(x * 16, y * 16, square, "doorRD"));
                    entities.add(new Sas((x - 4) * 16, (y - 3) * 16, sas, "sasRight"));
                    rightDX = x;
                    rightDY = y;
                    String doorRD = tmap.getTileProperty(tileID, "doorNu", "false");
                    rightNu = doorRD;
                }
                if (tileID == 10) {
                    entities.add(new Door(x * 16, y * 16, square, "doorLU"));
                    entities.add(new Sas((x + 2) * 16, (y - 3) * 16, sas, "sasLeft"));
                    doorLeftUpX = x;
                    doorLeftUpY = y;
                    String doorLU = tmap.getTileProperty(tileID, "doorNu", "false");
                    leftUpNu = doorLU;
                }
                if (tileID == 11) {
                    entities.add(new Door(x * 16, y * 16, square, "doorRU"));
                    entities.add(new Sas((x - 4) * 16, (y - 3) * 16, sas, "sasRight"));
                    doorRightUpX = x;
                    doorRightUpY = y;
                    String doorRU = tmap.getTileProperty(tileID, "doorNu", "false");
                    rightUpNu = doorRU;
                }
                if (tileID == 14) {
                    entities.add(new Block(x * 16, y * 16, elevS1, "elevStop"));
                }
                if (tileID == 15) {
                    entities.add(new Block(x * 16, y * 16, square, "elevKill"));
                }
                if (tileID == 30) {
                    entities.add(new Block(x * 16, y * 16, elevS1, "elevStart"));
                }
                if (tileID == 113) {
                    if (!noMoreItemInThisLevel) {
                        entities.add(new Item(x * 16, y * 16, square, "powerup"));
                    }
                }
                if (tileID == 114) {
                    if (!noMoreItemInThisLevel) {
                        entities.add(new Item(x * 16, y * 16, square, "quest"));
                    }
                }
                if (tileID == 115) {
                    if (!noMoreItemInThisLevel) {
                        entities.add(new Item(x * 16, y * 16, square, "manual"));
                    }
                }
                if (tileID2 == 51) {
                    entities.add(new Marine(x * 16, y * 16, "marine"));
                }
                if (Progress.initial) {
                    if (tileID2 == 50) {
                        System.out.println("ship creation");
                        entities.add(new Ship(x * 16, y * 16, "ship"));
                    }
                }
                if (!Player.appearLeftDown && !Player.appearRightDown && !Player.appearLeftUp && !Player.appearRightUp) {
                    if (tileID2 == 49) {
                        entities.add(Samus = new Player((x - 0.5f) * 16, (y - 1.5f) * 16, "samus"));
                    }
                }
            }
        }
        if (Player.appearLeftDown) {
            entities.add(Samus = new Player((leftDX + 1) * 16, (leftDY - 2.2f) * 16, "samus"));
        }
        if (Player.appearLeftUp) {
            entities.add(Samus = new Player((doorLeftUpX + 1) * 16, (doorLeftUpY - 2.2f) * 16, "samus"));
        }
        if (Player.appearRightDown) {
            entities.add(Samus = new Player((rightDX - 3) * 16, (rightDY - 2.2f) * 16, "samus"));
        }
        if (Player.appearRightUp) {
            entities.add(Samus = new Player((doorRightUpX - 3) * 16, (doorRightUpY - 2.2f) * 16, "samus"));
        }
        GameState.mapX = 200 - Samus.x;
        GameState.mapY = 150 - Samus.y;
        if (GameState.mapX > 0) {
            GameState.mapX = 0;
        }
        if (GameState.mapY > 0) {
            GameState.mapY = 0;
        }
        if (GameState.mapX < -(mapWidth - GameState.viewWidth)) {
            GameState.mapX = -(mapWidth - GameState.viewWidth);
        }
        if (GameState.mapY < -(mapHeight - GameState.viewHeight)) {
            GameState.mapY = -(mapHeight - GameState.viewHeight);
        }
        Progress.initial = false;
        Player.appearLeftDown = false;
        Player.appearLeftUp = false;
        Player.appearRightDown = false;
        Player.appearRightUp = false;
        Player.doorLD = false;
        Player.doorLU = false;
        Player.doorRU = false;
        Player.doorLD = false;
        MapState.registerRoom();
    }

    public static String getBlockAt(int x) {
        return Progress.gotItemInLevel[x];
    }

    public static LevelObject getBlockAt(int x, int y) {
        return blockAt[x][y];
    }
}
