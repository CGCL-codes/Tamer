package cantro.entity;

import java.awt.*;
import java.util.*;
import cantro.*;
import cantro.util.*;
import cantro.graphics.Status;
import cantro.input.*;
import cantro.networking.*;

public class Player {

    public int xShootOff = 2;

    public int yShootOff = 25;

    public static int ylim = 1;

    public String name;

    public int UID;

    public int height;

    public int width;

    public int x;

    public int y;

    public int health;

    public int maxHealth;

    public int xspeed;

    public int yspeed;

    public int xaccel;

    public int yaccel;

    public int jumpTimeLim;

    public boolean isOnGround;

    public boolean isCloseToBlack;

    public int isFacingRight;

    public int isMoving;

    public double gravity;

    public double aimTheta;

    public double bulletChaos;

    public int grabRadius;

    public int score;

    public int lastHorizontalMoveInPixels;

    public long lastJumpTime;

    public Color color;

    public ArrayList<GameItem> items;

    public ArrayList<Bullet> bullets;

    public Cantro owner;

    public Input input;

    public static ArrayList<Player> players;

    public Player(String n, int uid, boolean add, Cantro o) {
        name = n;
        UID = uid;
        height = 50;
        width = 30;
        x = 100;
        y = 415;
        health = 100;
        maxHealth = 100;
        xspeed = 0;
        yspeed = -10;
        xaccel = 15;
        yaccel = 35;
        jumpTimeLim = 750;
        isOnGround = true;
        isCloseToBlack = false;
        isFacingRight = 0;
        isMoving = 0;
        gravity = .5;
        aimTheta = 0.0;
        bulletChaos = .04;
        grabRadius = 20;
        score = 0;
        lastHorizontalMoveInPixels = 0;
        lastJumpTime = 0;
        color = GraphicsUtil.randColor();
        items = new ArrayList<GameItem>();
        bullets = new ArrayList<Bullet>();
        owner = o;
        input = new Input();
        if (add) {
            synchronized (players) {
                players.add(this);
            }
        }
    }

    public static Player playerByID(int ID) {
        for (Player p : players) {
            if (p.UID == ID) return p;
        }
        System.out.println("Null found with ID " + ID);
        for (Player p : players) {
            System.out.println(p.UID);
        }
        return null;
    }

    public void applyInput(MouseInput mouseInput) {
        applyInput();
        if (mouseInput.down) shoot();
    }

    public void applyInput() {
        if (input.down[Input.UP]) jump();
        if (input.down[Input.DOWN]) goDown();
        if (input.down[Input.LEFT]) goLeft();
        if (input.down[Input.RIGHT]) goRight();
    }

    public void applyNetworkInputUpdate(String in) {
        input.clear();
        int num = (int) in.charAt(0) - 40;
        if ((num & 16) == 16) input.addKey(Input.SHOOT);
        if ((num & 8) == 8) input.addKey(Input.UP);
        if ((num & 4) == 4) input.addKey(Input.DOWN);
        if ((num & 2) == 2) input.addKey(Input.LEFT);
        if ((num & 1) == 1) input.addKey(Input.RIGHT);
    }

    public void applyNetworkPosUpdate(int xx, int yy) {
        x = xx;
        y = yy;
    }

    public void damage(int num) {
        health -= num;
        new Particle(0, x, y, num);
    }

    public void shoot() {
        if (owner.gameScreen.status == Status.MULTIPLAYERGAME) owner.connection.send("" + Commands.SPAWNBULLET.val + ":" + UID);
        synchronized (bullets) {
            bullets.add(new Bullet(this, aimTheta + (Math.random() * 2 * bulletChaos - bulletChaos)));
        }
        synchronized (Particle.particles) {
            new Particle(1, x + width / 2, y + height / 2, 5);
        }
    }

    public String generateInputUpdateToSend() {
        byte b = 0;
        for (int i = 0; i < input.down.length; i++) {
            if (input.down[i]) b += Math.pow(2, i);
        }
        b += 40;
        char c = (char) b;
        if (input.down[Input.DOWN]) input.remKey(Input.DOWN);
        return StringUtil.CombineWithColons(Commands.INPUTUPDATE.val, UID, c);
    }

    public void goRight() {
        if (isOnGround || xspeed > 10) xspeed = xaccel; else xspeed = xaccel / 2;
    }

    public void goLeft() {
        if (isOnGround || xspeed < -10) xspeed = -xaccel; else xspeed = -(xaccel / 2);
    }

    public void goDown() {
        int[] pixels = new int[width];
        owner.gameScreen.getRGBofSkeleton(x, y + height + 1, width, 1, pixels, 0, width);
        boolean goDown = true;
        for (int i = 0; i < pixels.length; i++) {
            if (pixels[i] == -16777216 || pixels[i] == -65281) {
                goDown = false;
                i = pixels.length;
            }
        }
        if (goDown) y += 5;
    }

    public void jump() {
        if (isOnGround && (System.currentTimeMillis() - lastJumpTime) > jumpTimeLim) {
            lastJumpTime = System.currentTimeMillis();
            yspeed = yaccel;
        }
    }

    public void momentum() {
        int deductFromY = (yspeed / 2);
        isCloseToBlack = false;
        if (yspeed < 0) {
            int[] pixels = new int[Math.abs(deductFromY) * width];
            owner.gameScreen.getRGBofSkeleton(x, y + height, width, Math.abs(deductFromY), pixels, 0, width);
            for (int i = 0; i < pixels.length; i++) {
                if (pixels[i] == -16777216 || pixels[i] == -65281) {
                    isCloseToBlack = true;
                    deductFromY = -(i / width);
                    break;
                }
            }
        } else if (yspeed > 0) {
            int[] pixels = new int[Math.abs(deductFromY) * width];
            owner.gameScreen.getRGBofSkeleton(x, y - Math.abs(deductFromY), width, Math.abs(deductFromY), pixels, 0, width);
            for (int i = 0; i < pixels.length; i++) {
                if (pixels[i] == -65281) {
                    isCloseToBlack = true;
                    deductFromY = (i / width);
                    yspeed = 0;
                    break;
                }
            }
        }
        if (isOnGround) {
            int[] pixels = new int[width];
            owner.gameScreen.getRGBofSkeleton(x, y + height - 1, width, 1, pixels, 0, width);
            for (int i = 0; i < pixels.length; i++) {
                if (pixels[i] == -16777216 || pixels[i] == -65281) {
                    y -= 3;
                    break;
                }
            }
        }
        if (xspeed == 0) isMoving = 1; else isMoving = 0;
        if (deductFromY == 0 && isCloseToBlack) isOnGround = true; else {
            y -= deductFromY;
            isOnGround = false;
        }
        if (yspeed > -10 / gravity) yspeed -= 1 / gravity + 1;
        if (y > ylim) {
            y = ylim;
        }
        int actualTravel = xspeed / 4;
        lastHorizontalMoveInPixels = actualTravel;
        if (actualTravel < 0) {
            int absXSpeedDivBy4 = Math.abs(xspeed / 4);
            int[] pixels = new int[absXSpeedDivBy4 * height];
            owner.gameScreen.getRGBofSkeleton(x - absXSpeedDivBy4, y, absXSpeedDivBy4, height, pixels, 0, absXSpeedDivBy4);
            for (int i = absXSpeedDivBy4 - 1; i < pixels.length - 1; i += absXSpeedDivBy4) {
                if (pixels[i] == -65281) {
                    actualTravel = 0;
                    i = pixels.length;
                }
            }
            if (actualTravel == xspeed / 4) {
                for (int x = 0; x < absXSpeedDivBy4; x++) {
                    for (int y = 0; y < height; y++) {
                        int rgb = pixels[y * absXSpeedDivBy4 + x];
                        if (rgb == -65281) {
                            actualTravel = -x;
                            x = absXSpeedDivBy4;
                            y = height;
                        }
                    }
                }
            }
        } else if (actualTravel > 0) {
            int absXSpeedDivBy4 = Math.abs(xspeed / 4);
            int[] pixels = new int[absXSpeedDivBy4 * height];
            owner.gameScreen.getRGBofSkeleton(x + width, y, absXSpeedDivBy4, height, pixels, 0, absXSpeedDivBy4);
            for (int i = 0; i < pixels.length; i += absXSpeedDivBy4) {
                if (pixels[i] == -65281) {
                    actualTravel = 0;
                    i = pixels.length;
                }
            }
            if (actualTravel == xspeed / 4) {
                for (int x = 0; x < absXSpeedDivBy4; x++) {
                    for (int y = 0; y < height; y++) {
                        int rgb = pixels[y * absXSpeedDivBy4 + x];
                        if (rgb == -65281) {
                            actualTravel = x;
                            x = absXSpeedDivBy4;
                            y = height;
                        }
                    }
                }
            }
        }
        x += actualTravel;
        if (isOnGround) {
            if (xspeed < 0) ++xspeed;
            if (xspeed > 0) --xspeed;
        }
    }

    public String toString() {
        return x + ":" + y + ":" + xspeed + ":" + yspeed + ":" + isFacingRight;
    }
}
