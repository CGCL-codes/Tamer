package Game;

import java.io.*;
import java.util.ArrayList;

public class MapLoader {

    Platform platform1, platform2, platform3, platform4, platform5, platform6, platform7, platform8;

    Platform platform9, platform10, platform11, platform12, platform13, platform14, platform15, platform16, platform17, platform18, platform19;

    Wall wall1, wall2, wall3, wall4, wall5, wall6, wall7, wall8, wall9, wall10, wall11, wall12, wall13, wall14;

    int screenNum;

    NextScreen screen1, screen2, screen3;

    public void MapNewGame(int level, int currentScreen, GamePanel mainPanel) {
        screenNum = currentScreen;
        screen1 = new NextScreen(638, 288, currentScreen);
        screen2 = new NextScreen(637, 384, currentScreen);
        screen3 = new NextScreen(637, 0, currentScreen);
        platform1 = new Platform(0, 475, 20);
        platform2 = new Platform(128, 436, 1);
        platform3 = new MovingPlatform(100, 300, 1, mainPanel, true, 640, 5, 352, 58);
        platform4 = new MovingPlatform(50, 300, 1, mainPanel, true, 640, 5, 448, 192);
        platform5 = new Platform(180, 384, 8);
        platform7 = new Platform(5, 64, 1);
        platform8 = new Platform(256, 128, 5);
        platform9 = new Platform(385, 288, 3);
        platform10 = new Platform(128, 192, 7);
        platform11 = new Platform(0, 100, 5);
        platform12 = new MovingPlatform(512, 100, 1, mainPanel, true, 640, 5, 150, 50);
        platform13 = new MovingPlatform(448, 75, 1, mainPanel, false, 450, 300, 1, 1);
        platform14 = new MovingPlatform(50, 300, 1, mainPanel, true, 640, 5, 448, 154);
        platform15 = new Platform(192, 96, 6);
        platform16 = new Platform(556, 160, 1);
        platform17 = new Platform(320, 318, 5);
        platform18 = new Platform(536, 222, 1);
        platform19 = new Platform(385, 264, 1);
        wall2 = new Wall(256, 196, 2);
        wall1 = new Wall(0, 0, 5);
        wall3 = new Wall(637, 384, 1);
        wall4 = new Wall(637, 0, 3);
        wall5 = new Wall(572, 0, 3);
        wall6 = new Wall(128, 192, 3);
        wall7 = new Wall(0, 0, 3);
        wall8 = new Wall(0, 384, 1);
        wall9 = new Wall(576, 100, 1);
        wall10 = new Wall(637, 288, 1);
        wall11 = new Wall(128, 190, 3);
        wall12 = new Wall(572, -92, 2);
        wall13 = new Wall(0, 384, 1);
        wall14 = new Wall(637, 0, 5);
        mainPanel.platforms.clear();
        mainPanel.walls.clear();
        mainPanel.screens.clear();
        switch(level) {
            case 0:
                switch(currentScreen) {
                    case 0:
                        mainPanel.platforms.add(platform1);
                        mainPanel.platforms.add(platform2);
                        mainPanel.platforms.add(platform3);
                        mainPanel.platforms.add(platform5);
                        mainPanel.platforms.add(platform7);
                        mainPanel.platforms.add(platform8);
                        mainPanel.platforms.add(platform9);
                        mainPanel.walls.add(wall1);
                        mainPanel.walls.add(wall2);
                        mainPanel.walls.add(wall3);
                        mainPanel.walls.add(wall4);
                        mainPanel.walls.add(wall5);
                        mainPanel.screens.add(screen1);
                        mainPanel.repaint();
                        break;
                    case 1:
                        mainPanel.screens.add(screen2);
                        mainPanel.platforms.add(platform1);
                        mainPanel.platforms.add(platform4);
                        mainPanel.platforms.add(platform10);
                        mainPanel.platforms.add(platform11);
                        mainPanel.platforms.add(platform12);
                        mainPanel.platforms.add(platform13);
                        mainPanel.platforms.add(platform5);
                        mainPanel.walls.add(wall4);
                        mainPanel.walls.add(wall6);
                        mainPanel.walls.add(wall7);
                        mainPanel.walls.add(wall8);
                        mainPanel.walls.add(wall9);
                        mainPanel.walls.add(wall10);
                        mainPanel.repaint();
                        break;
                    case 2:
                        mainPanel.platforms.add(platform1);
                        mainPanel.platforms.add(platform14);
                        mainPanel.platforms.add(platform15);
                        mainPanel.platforms.add(platform19);
                        mainPanel.platforms.add(platform16);
                        mainPanel.platforms.add(platform17);
                        mainPanel.platforms.add(platform18);
                        mainPanel.screens.add(screen3);
                        mainPanel.walls.add(wall7);
                        mainPanel.walls.add(wall11);
                        mainPanel.walls.add(wall12);
                        mainPanel.walls.add(wall13);
                        mainPanel.walls.add(wall14);
                        break;
                    case 3:
                        mainPanel.platforms.add(platform1);
                        break;
                    case 4:
                        mainPanel.platforms.add(platform1);
                        break;
                    case 5:
                        mainPanel.platforms.add(platform1);
                        break;
                }
        }
    }
}
