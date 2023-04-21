package levels;

import gameobjects.*;
import java.awt.Image;
import java.awt.image.PixelGrabber;
import resources.Music;
import resources.Sprite;
import tools.GameApplet;
import abstracts.Level;
import land.Land;

public class PngLevel extends Level {

    private final Sprite SPRITE;

    public PngLevel(GameApplet applet, Sprite sprite) {
        super(applet);
        SPRITE = sprite;
    }

    @Override
    public final void load() {
        Image img = SPRITE.getImage();
        int[] px = new int[24 * 18];
        PixelGrabber pg = new PixelGrabber(img, 0, 0, 24, 18, px, 0, 24);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int row = 0; row < 18; row++) {
            for (int col = 0; col < 24; col++) {
                switch(px[row * 24 + col]) {
                    case -16777216:
                        getCollisionGrid().set(row, col, true, true, true);
                        break;
                    case -1:
                        getCollisionGrid().set(row, col, true, false, false);
                        break;
                    case -4347000:
                        add(new EyeballSpawn(this, row, col));
                        getCollisionGrid().set(row, col, true, false, false);
                        break;
                    case -6360080:
                        add(new Treasure(this, row, col, "Diamond"));
                        getCollisionGrid().set(row, col, true, false, false);
                        break;
                    case -65536:
                        add(new Treasure(this, row, col, "Ruby"));
                        getCollisionGrid().set(row, col, true, false, false);
                        break;
                    case -15945681:
                        add(new Treasure(this, row, col, "Emerald"));
                        getCollisionGrid().set(row, col, true, false, false);
                        break;
                    case -15962381:
                        add(new Treasure(this, row, col, "Sapphire"));
                        getCollisionGrid().set(row, col, true, false, false);
                        break;
                    case -4250405:
                        add(new Mage(this, row, col));
                        getCollisionGrid().set(row, col, true, false, false);
                        break;
                    default:
                        System.out.println("Unknown level pixel: " + px[row * 24 + col]);
                        break;
                }
            }
        }
        add(new Teleporter(this, 3, 1, 5, 14));
        getCollisionGrid().set(3, 1, true, false, true);
        add(new Teleporter(this, 6, 15, 2, 2));
        getCollisionGrid().set(6, 15, true, false, true);
        new Land(getCollisionGrid(), this);
        Music.set(Music.BACKGROUND_1);
    }
}
