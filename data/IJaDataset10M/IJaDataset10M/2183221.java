package com.golden.lemmings;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import com.golden.gamedev.GameEngine;
import com.golden.gamedev.GameObject;
import com.golden.gamedev.object.GameFont;
import com.golden.gamedev.object.Timer;

public class MenuStart extends GameObject {

    GameFont font;

    BufferedImage title;

    BufferedImage arrow;

    int option;

    boolean blink;

    Timer blinkTimer = new Timer(400);

    public MenuStart(GameEngine parent) {
        super(parent);
    }

    public void initResources() {
        Settings.getInstance().loadSettings(bsIO);
        Settings.getInstance().loadHighscore(bsIO);
        title = getImage("resources/titel2.png", false);
        arrow = getImage("resources/Arrow.png");
        font = fontManager.getFont(getImages("resources/202.gif", 8, 5), "ABCDEFGHIJKLMNOPQRSTUVWXYZ.!:\"\" ()");
    }

    public void update(long elapsedTime) {
        if (blinkTimer.action(elapsedTime)) {
            blink = !blink;
        }
        switch(bsInput.getKeyPressed()) {
            case KeyEvent.VK_ENTER:
                if (option == 0) {
                    parent.nextGameID = Constants.MENU_LEVEL_MODE;
                    finish();
                } else if (option == 1) {
                    parent.nextGameID = Constants.MENU_HIGHSCORE_MODE;
                    finish();
                } else if (option == 2) {
                    parent.nextGameID = Constants.MENU_SETTINGS_MODE;
                    finish();
                } else if (option == 3) {
                    parent.nextGameID = Constants.MENU_ANLEITUNG_MODE;
                    finish();
                } else if (option == 4) {
                    finish();
                }
                break;
            case KeyEvent.VK_UP:
                option--;
                if (option < 0) option = 4;
                break;
            case KeyEvent.VK_DOWN:
                option++;
                if (option > 4) option = 0;
                break;
            case KeyEvent.VK_ESCAPE:
                finish();
                break;
        }
    }

    public void render(Graphics2D g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(title, 0, 0, null);
        font.drawString(g, "START", 300, 220);
        font.drawString(g, "HIGHSCORE", 300, 270);
        font.drawString(g, "SETTINGS", 300, 320);
        font.drawString(g, "ANLEITUNG", 300, 370);
        font.drawString(g, "END", 300, 420);
        if (!blink) {
            switch(option) {
                case 0:
                    g.drawImage(arrow, 220, 220, null);
                    break;
                case 1:
                    g.drawImage(arrow, 220, 270, null);
                    break;
                case 2:
                    g.drawImage(arrow, 220, 320, null);
                    break;
                case 3:
                    g.drawImage(arrow, 220, 370, null);
                    break;
                case 4:
                    g.drawImage(arrow, 220, 420, null);
                    break;
            }
        }
    }
}
