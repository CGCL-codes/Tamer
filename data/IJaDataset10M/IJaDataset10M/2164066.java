package scorch.weapons;

import scorch.*;
import java.awt.*;

public class FireExplosion extends Explosion {

    protected FireColorModel pallete;

    protected int width;

    protected int height;

    protected int pixels = 0;

    protected float intensity = 0;

    protected int data[];

    protected int buffer[][];

    protected int state = 0;

    protected ExplosionInfo ie;

    protected long duration = 50;

    private int frameNum = 0;

    private static int sndFIRE;

    /***************************************************************/
    public FireExplosion(Bitmap bitmap, int width, int height) {
        super(bitmap);
        this.width = width;
        this.height = height;
        pallete = new FireColorModel(32);
        data = new int[width * height];
        buffer = new int[height][width];
        ie = new ExplosionInfo();
    }

    /***************************************************************/
    public FireExplosion(Bitmap bitmap, int width, int height, int x, int y) {
        super(bitmap);
        this.width = width;
        this.height = height;
        setPosition(x, y);
        pallete = new FireColorModel(32);
        data = new int[width * height];
        buffer = new int[height][width];
    }

    /***************************************************************/
    public void setArgument(int arg) {
    }

    public void setPosition(int x, int y) {
        this.x = x - width / 2;
        this.y = y - height;
    }

    /***************************************************************/
    public void setDuration(long time) {
        duration = time;
    }

    public static void loadSounds(ScorchApplet owner) {
        sndFIRE = addSound(owner.getAudioClip(owner.getCodeBase(), "Sound/fire.au"));
    }

    /***************************************************************/
    protected static long Random(long min, long max) {
        double rnd = Math.random();
        long returnValue = Math.round((rnd * (double) (max - min) + (double) min));
        return returnValue;
    }

    /***************************************************************/
    protected static int random(int max) {
        int returnValue;
        double rnd = Math.random();
        returnValue = (int) Math.round(rnd * ((double) max));
        if (returnValue < 0) System.err.println("PANIC");
        return returnValue;
    }

    /***************************************************************/
    public boolean drawNextFrame(boolean update) {
        pixels = 0;
        int color = 0;
        frameNum++;
        switch(state) {
            case 0:
                {
                    if (intensity == 0) loopSound(sndFIRE);
                    intensity += 0.009f;
                    if (intensity > 0.2f) {
                        state = 1;
                    }
                }
                break;
            case 1:
                if (frameNum >= duration) {
                    state = 2;
                }
                break;
            case 2:
                intensity -= 0.007f;
                if (intensity <= 0) {
                    stopSound(sndFIRE);
                    state = 4;
                    bitmap.setSandMode(true);
                    bitmap.setClipping(false);
                    bitmap.setDensity(1);
                    bitmap.setColor(null);
                    bitmap.fillEllipse(x + width / 2, y + height / 2 + 20, width / 2, 40);
                    bitmap.setSandMode(false);
                    return false;
                }
                break;
            default:
                return false;
        }
        try {
            for (int y = 1; y < height - 4; y += 2) {
                int pixel = pixels + y * width;
                for (int x = 0; x < width; x++) {
                    int p = pixel + (width << 1);
                    int top = data[p];
                    top += data[p - 1];
                    top += data[p + 1];
                    int bottom = data[pixel + (width << 2)];
                    int c1 = (top + bottom) >> 2;
                    if (c1 > 1) c1--;
                    int c2 = (c1 + bottom) >> 1;
                    data[pixel] = (byte) c1;
                    data[pixel + width] = (byte) c2;
                    pixel++;
                }
            }
            int generator = pixels + width * (height - 4);
            for (int x = 0; x < width; x += 4) {
                color = random((int) (255.0f * intensity));
                data[generator] = color;
                data[generator + 1] = color;
                data[generator + 2] = color;
                data[generator + 3] = color;
                data[generator + width] = color;
                data[generator + width + 1] = color;
                data[generator + width + 2] = color;
                data[generator + width + 3] = color;
                data[generator + width * 2] = color;
                data[generator + width * 2 + 1] = color;
                data[generator + width * 2 + 2] = color;
                data[generator + width * 2 + 3] = color;
                data[generator + width * 3] = color;
                data[generator + width * 3 + 1] = color;
                data[generator + width * 3 + 2] = color;
                data[generator + width * 3 + 3] = color;
                generator += 4;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        bitmap.setSandMode(true);
        bitmap.setDensity(1);
        bitmap.setColor(Color.black.getRGB());
        bitmap.setClipping(true);
        bitmap.fillEllipse(x + width / 2, y + height / 2 + 20, width / 2, 40);
        bitmap.setClipping(false);
        bitmap.drawSpriteCl(x, y, data, width, 0, pallete);
        bitmap.newPixels(x, y, width, height);
        bitmap.setSandMode(false);
        return true;
    }

    /***************************************************************/
    public Rectangle getUpdatedArea() {
        return new Rectangle(x, y, width, height);
    }

    /***************************************************************/
    public ExplosionInfo getExplosionInfo() {
        ExplosionInfo ie = new ExplosionInfo();
        ie.explosionArea = new Rectangle(x, y, width, height);
        return ie;
    }
}

class FireColorModel extends ScorchColorModel {

    public FireColorModel(int bits) {
        super();
        int i = 0;
        byte c = 0;
        while (i < 64) {
            r[i] = c;
            g[i] = 0;
            b[i] = 0;
            c += 4;
            i++;
        }
        c = 0;
        while (i < 128) {
            r[i] = (byte) 255;
            g[i] = c;
            b[i] = 0;
            c += 4;
            i++;
        }
        c = 0;
        while (i < 192) {
            r[i] = (byte) 255;
            g[i] = (byte) 255;
            b[i] = c;
            c += 4;
            i++;
        }
        while (i < 256) {
            r[i] = b[i] = g[i] = (byte) 255;
            i++;
        }
    }

    public int getRGB(int pixel) {
        if (pixel < 20) return 0; else return ((((int) 255) << 24) | (r[pixel] << 16) | (g[pixel] << 8) | b[pixel]);
    }
}
