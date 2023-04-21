package rpg.entities;

import java.awt.image.BufferedImage;
import java.util.Random;
import rpg.Game;
import rpg.Location;
import rpg.Maps.Map;
import rpg.gfx.Bitmap;
import rpg.util.ImageUtil;

/**
 * rothens.tumblr.com
 * @author Rothens
 */
public class Rooster extends LivingEntity {

    private Bitmap bitmaps[];

    private int animState = 0;

    int animDelay = 5;

    int nextRotate = 60;

    Random random = new Random();

    private int dir = 0;

    private Location target;

    public Rooster(Map map, Location loc) {
        super(50, loc);
        this.map = map;
        target = loc;
        bitmaps = new Bitmap[12];
        BufferedImage image = ImageUtil.createImageIcon("/res/rooster.png");
        for (int i = 0; i < 12; i++) {
            int col = i % 3;
            int row = i / 3;
            bitmaps[i] = new Bitmap(64, 64);
            image.getSubimage(col * 64, row * 64, 64, 64).getRGB(0, 0, bitmaps[i].width, bitmaps[i].height, bitmaps[i].pixels, 0, bitmaps[i].width);
        }
        Game.eh.addToActive(this);
    }

    @Override
    public boolean isPassable() {
        return false;
    }

    @Override
    public boolean tick() {
        animDelay--;
        if (animDelay <= 0) {
            animDelay = 5;
            animState++;
            animState %= 3;
        }
        int moveDir = loc.getDir(target);
        if (moveDir != -1) {
            dir = moveDir;
            if (!loc.move(moveDir, 1, map)) {
                target = loc.getPossibleLocation(map);
            }
        } else {
            Random rand = new Random();
            if (rand.nextBoolean()) {
                target = loc.getPossibleLocation(map);
            }
        }
        return true;
    }

    @Override
    public Bitmap getDrawGraphics() {
        return bitmaps[animState % 3 + dir * 3];
    }
}
