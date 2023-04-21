package rpg.gfx;

/**
 * rothens.tarhely.biz
 * @author Rothens
 */
public class Bar extends Bitmap {

    int maxVal;

    int val;

    int color;

    int bgcolor;

    public Bar(int width, int height, int color, int bgcolor, int maxVal, int val) {
        super(width, height);
        this.color = (0xff << 24) | color;
        this.bgcolor = 0x000000;
        this.maxVal = maxVal;
        this.val = val;
        for (int i = 0; i < width * height; i++) {
            pixels[i] = this.bgcolor;
        }
        draw(calculateBar(), 1, 1);
    }

    public void setVal(int val) {
        if (val < 0) val = 0;
        if (val > maxVal) val = maxVal;
        this.val = val;
        draw(calculateBar(), 1, 1);
    }

    private Bitmap calculateBar() {
        Bitmap ret = new Bitmap(width - 2, height - 2);
        for (int i = 0; i < ret.width * ret.height; i++) {
            ret.pixels[i] = 0xff000001;
        }
        double percent = val / (double) maxVal;
        int w = (int) (ret.width * percent);
        Bitmap bar = new Bitmap(w, ret.height);
        for (int i = 0; i < bar.width * bar.height; i++) {
            bar.pixels[i] = color;
        }
        ret.draw(bar, 0, 0);
        return ret;
    }
}
