package org.chernovia.games.arcade.funkball;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import org.chernovia.lib.graphics.lib3d.P2D;
import org.chernovia.lib.sims.ca.fishbowl.FishBowl2D;

public class Arena extends Canvas implements KeyListener {

    private static final long serialVersionUID = -6881342753819756435L;

    static int DOT_PTS = 1, BLUE_DOT_PTS = 10, MOB_PTS = 100;

    int xDim, yDim;

    Player player;

    Mob[] mobs;

    Dot[] dots;

    FishBowl2D bowl;

    Image[] dotImgs, mobImgs, avatImgs;

    BufferedImage buffImg;

    Graphics2D BG;

    TextField statFld;

    int magStr = 32;

    private boolean SAFE, BLUE;

    public Arena(int w, int h, int d, FishBowl2D b) {
        bowl = b;
        xDim = w;
        yDim = h;
        setSize(w, h);
        dots = new Dot[d];
        mobs = new Mob[bowl.getNumFish()];
        int dot_rad = ((xDim + yDim) / 2) / 50;
        int mob_x = xDim / 25;
        int mob_y = yDim / 25;
        int play_x = xDim / 20;
        int play_y = yDim / 25;
        dotImgs = new Image[2];
        for (int i = 1; i < 3; i++) dotImgs[i - 1] = Toolkit.getDefaultToolkit().getImage(Arena.class.getResource(FunkBallApplet.RES_DIR + FunkBallApplet.DOT_IMG + i + FunkBallApplet.IMG_SFX));
        for (int i = 0; i < dots.length; i++) {
            int rx = (int) (Math.random() * xDim);
            int ry = (int) (Math.random() * yDim);
            boolean blue = Math.random() < .1;
            dots[i] = new Dot(rx, ry, dot_rad, blue, dotImgs);
        }
        mobImgs = new Image[2];
        for (int i = 1; i < 3; i++) mobImgs[i - 1] = Toolkit.getDefaultToolkit().getImage(Arena.class.getResource(FunkBallApplet.RES_DIR + FunkBallApplet.MOB_IMG + i + ".gif"));
        for (int i = 0; i < mobs.length; i++) {
            mobs[i] = new Mob(bxax(bowl.getFish(i).x), byay(bowl.getFish(i).y), mob_x, mob_y, mobImgs);
        }
        avatImgs = new Image[9];
        for (int i = 1; i < 10; i++) avatImgs[i - 1] = Toolkit.getDefaultToolkit().getImage(Arena.class.getResource(FunkBallApplet.RES_DIR + FunkBallApplet.AVAT_IMG + i + FunkBallApplet.IMG_SFX));
        player = new Player(xDim / 2, yDim / 2, play_x, play_y, avatImgs);
        player.speed = 2;
        buffImg = new BufferedImage(xDim, yDim, BufferedImage.TYPE_INT_RGB);
        BG = buffImg.createGraphics();
        drawDots();
        drawPlayer();
        statFld = new TextField("Score: " + player.score + ", Lives: " + player.lives + "     ");
        bowl.MAGNET = true;
        bowl.setMagStr(magStr);
    }

    public void setBlue(boolean b) {
        if (BLUE == b) return; else BLUE = b;
        if (BLUE) {
            bowl.setMagStr(-magStr);
            new FunkTime(FunkTime.BLUE, this).start();
        } else {
            bowl.setMagStr(magStr);
            for (int i = 0; i < mobs.length; i++) mobs[i].vis = true;
            repaint();
        }
    }

    public void setSafe(boolean b) {
        if (SAFE == b) return; else SAFE = b;
        if (SAFE) new FunkTime(FunkTime.SAFE, this).start(); else repaint();
    }

    private void updateStats() {
        statFld.setText("Score: " + player.score + ", Lives: " + player.lives);
    }

    public void moveMobs() {
        for (int i = 0; i < mobs.length; i++) {
            mobs[i].setLoc(bxax(bowl.getFish(i).x), byay(bowl.getFish(i).y));
        }
    }

    public void movePlayer() {
        player.xLoc += Player.DX[player.dir] * player.speed;
        if (player.xLoc > xDim) player.xLoc -= xDim;
        if (player.xLoc < 0) player.xLoc += xDim;
        player.yLoc += Player.DY[player.dir] * player.speed;
        if (player.yLoc > yDim) player.yLoc -= yDim;
        if (player.yLoc < 0) player.yLoc += yDim;
        if (!SAFE) {
            chkDots();
            chkMobs();
        }
        bowl.getMagnet().setLoc(new P2D(axbx(player.xLoc), ayby(player.yLoc)));
    }

    private void chkDots() {
        for (int i = 0; i < dots.length; i++) {
            if (!dots[i].eaten && player.detectCollision(dots[i])) {
                dots[i].eaten = true;
                if (dots[i].blue) {
                    setBlue(true);
                    player.score += BLUE_DOT_PTS;
                } else player.score += DOT_PTS;
                updateStats();
            }
        }
    }

    private void chkMobs() {
        for (int i = 0; i < mobs.length; i++) {
            if (mobs[i].vis && player.detectCollision(mobs[i])) {
                if (BLUE) {
                    player.score += MOB_PTS;
                    mobs[i].vis = false;
                    updateStats();
                    bowl.getFish(i).setLoc(new P2D(0, 0));
                } else {
                    whoops();
                    updateStats();
                }
            }
        }
    }

    private void whoops() {
        player.lives--;
        if (player.lives <= 0) System.exit(-1);
        setSafe(true);
    }

    private void drawDots() {
        for (int i = 0; i < dots.length; i++) {
            if (!dots[i].eaten) BG.drawImage(dots[i].imgs[dots[i].blue ? 1 : 0], dots[i].xLoc, dots[i].yLoc, dots[i].width, dots[i].height, this);
        }
    }

    private void drawMobs() {
        for (int i = 0; i < mobs.length; i++) {
            BG.drawImage(bowl.getFish(i).getVec().x < 0 ? mobs[i].imgs[0] : mobs[i].imgs[1], mobs[i].xLoc, mobs[i].yLoc, mobs[i].width, mobs[i].height, !mobs[i].vis || SAFE ? Color.black : BLUE ? Color.blue : Mob.colors[i], this);
        }
    }

    private void drawPlayer() {
        BG.drawImage(player.imgs[player.dir], player.xLoc, player.yLoc, player.width, player.height, this);
    }

    @Override
    public void paint(Graphics g) {
        BG.clearRect(0, 0, xDim, yDim);
        drawMobs();
        drawDots();
        drawPlayer();
        g.drawImage(buffImg, 0, 0, this);
    }

    private int bxax(double x) {
        return (xDim / 2) + (int) (x * (xDim / (bowl.getBowlSize() * 2)));
    }

    private int byay(double y) {
        return (yDim / 2) + (int) (y * (yDim / (bowl.getBowlSize() * 2)));
    }

    private double axbx(int x) {
        return -bowl.getBowlSize() + (x * ((bowl.getBowlSize() * 2) / xDim));
    }

    private double ayby(int y) {
        return -bowl.getBowlSize() + (y * ((bowl.getBowlSize() * 2) / yDim));
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode();
        for (int i = 0; i < Player.DIR_KEYS.length; i++) if (c == Player.DIR_KEYS[i]) player.dir = i;
    }
}
