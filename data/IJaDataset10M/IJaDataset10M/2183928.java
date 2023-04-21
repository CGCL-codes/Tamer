package net.narusas.game.pushpush.ui;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import net.narusas.game.pushpush.Map;

public class GameClearScene implements Scene {

    private boolean isEnd;

    int count = 0;

    private BufferedImage img;

    int imgCount = 0;

    boolean memorial = false;

    public void render(PushPushGame game, Map map, Graphics2D g) {
        if (memorial) {
            g.drawImage(img, 0, 0, null);
        } else {
            g.drawImage(game.imageManager.gameClearImg, 0, 0, null);
        }
    }

    @Override
    public void update(PushPushGame game, Map map, long elapsedTime) {
        if (memorial) {
            count += elapsedTime;
            if (count > 1000) {
                count = 0;
                imgCount++;
                BufferedImage tempImg = null;
                try {
                    tempImg = game.getImage("/images/" + imgCount + ".jpg");
                } catch (RuntimeException e) {
                }
                if (tempImg == null) {
                    game.stage = 1;
                    game.loadStage();
                    isEnd = true;
                    return;
                }
                img = tempImg;
            }
        }
        if (game.keyPressed(KeyEvent.VK_SPACE)) {
            memorial = true;
        }
        if (game.keyPressed(KeyEvent.VK_ESCAPE)) {
            System.exit(0);
        }
    }

    @Override
    public boolean isSceneOver() {
        return isEnd;
    }

    @Override
    public void init(PushPushGame game) {
        img = game.imageManager.gameClearImg;
    }
}
