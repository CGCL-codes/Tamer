package client.ui;

import java.awt.*;
import java.io.*;
import javax.imageio.*;
import common.data.*;

/** Contain all images of the game */
public class Images {

    public static Image[][][] unitImages;

    public static Image[] buff;

    public static Image[] bButton;

    public static Image unit;

    public static Image[] aButton;

    public static Image loginBG;

    public static Image[] obstacles;

    public static Image[] itemImages;

    public static Image startHuman, startErothaur;

    public static Image dmg, heal;

    public static Image fullscreen, tutorial, logout, exit, player;

    public static Image scrollUp, scrollDown;

    public static Image errorImg;

    /** Load all images */
    public static void load(Database db) {
        errorImg = loadImage("Images/error.png");
        unit = loadImage("Images/Einheit.png");
        scrollUp = loadImage("Images/scrollUp.png");
        scrollDown = loadImage("Images/scrollDown.png");
        dmg = loadImage("Images/damage.png");
        heal = loadImage("Images/heal.png");
        fullscreen = loadImage("Images/fullscreen.png");
        tutorial = loadImage("Images/tutorial.png");
        logout = loadImage("Images/logout.png");
        exit = loadImage("Images/exit.png");
        player = loadImage("Images/player.png");
        buff = new Image[db.buffs];
        for (int i = 0; i < db.buffs; i++) buff[i] = loadImage("Images/buff" + i + ".png");
        bButton = new Image[db.buffs];
        for (int i = 0; i < db.buffs; i++) bButton[i] = loadImage("Images/bButton" + i + ".png");
        startHuman = loadImage("Images/startHuman.png");
        startErothaur = loadImage("Images/startErothaur.png");
        unitImages = new Image[db.units][8][4];
        for (int i = 0; i < db.units; i++) for (int dir = 0; dir < 8; dir++) for (int state = 0; state < 4; state++) unitImages[i][dir][state] = loadImage("Images/unit" + i + "state" + state + "dir" + dir + ".png");
        aButton = new Image[db.abilities];
        for (int i = 0; i < db.abilities; i++) aButton[i] = loadImage("Images/aButton" + i + ".png");
        itemImages = new Image[db.items];
        for (int i = 0; i < db.items; i++) itemImages[i] = loadImage("Images/item" + i + ".png");
        obstacles = new Image[db.obstacles];
        for (int i = 0; i < db.obstacles; i++) obstacles[i] = loadImage("Images/obstacle" + i + ".png");
        loginBG = loadImage("Images/loginbg.png");
    }

    /** Load an image */
    static Image loadImage(String file) {
        try {
            FileInputStream is = new FileInputStream(file);
            Image i = ImageIO.read(is);
            is.close();
            return i;
        } catch (IOException e) {
            System.out.println("Image (" + file + ") not found!");
            return errorImg;
        }
    }
}
