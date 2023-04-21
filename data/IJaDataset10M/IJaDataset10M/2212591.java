package f;

import v.DoomVideoRenderer;
import doom.DoomMain;

public class Wiper extends AbstractWiper {

    static final String rcsid = "$Id: Wiper.java,v 1.17 2011/11/01 19:02:57 velktron Exp $";

    protected wipefun[] wipes;

    public Wiper(DoomMain DC) {
        this.updateStatus(DC);
        wipes = new wipefun[] { new wipe_initColorXForm(), new wipe_doColorXForm(), new wipe_exitColorXForm(), new wipe_initMelt(), new wipe_doMelt(), new wipe_exitMelt() };
    }

    /** They sure have an obsession with shit...this is supposed to do some
     * lame-ass transpose.
     * 
     * @param array
     * @param width
     * @param height
     */
    public void shittyColMajorXform(short[] array, int width, int height) {
        int x;
        int y;
        short[] dest;
        dest = new short[width * height];
        for (y = 0; y < height; y++) for (x = 0; x < width; x++) dest[y * width + x] = array[x * height + y];
        System.arraycopy(dest, 0, array, 0, width * height);
    }

    /** Those guys sure have an obsession with shit...this is supposed to do some
     * lame-ass transpose.
     * 
     * @param array
     * @param width
     * @param height
     */
    public void shittyColMajorXform(byte[] array, int width, int height) {
        int x;
        int y;
        byte[] dest;
        dest = new byte[width * height];
        for (y = 0; y < height; y++) for (x = 0; x < width; x++) {
            dest[x * height + y] = array[y * width + x];
        }
        System.arraycopy(dest, 0, array, 0, width * height);
    }

    class wipe_initColorXForm implements wipefun {

        public boolean invoke(int width, int height, int ticks) {
            System.arraycopy(wipe_scr_start, 0, wipe_scr, 0, width * height);
            return false;
        }
    }

    class wipe_doColorXForm implements wipefun {

        public boolean invoke(int width, int height, int ticks) {
            boolean changed;
            byte[] w = wipe_scr;
            byte[] e = wipe_scr_end;
            int newval;
            changed = false;
            int pw = 0;
            int pe = 0;
            while (pw != width * height) {
                if (w[pw] != e[pe]) {
                    if (w[pw] > e[pe]) {
                        newval = w[pw] - ticks;
                        if (newval < e[pe]) w[pw] = e[pe]; else w[pw] = (byte) newval;
                        changed = true;
                    } else if (w[pw] < e[pe]) {
                        newval = w[pw] + ticks;
                        if (newval > e[pe]) w[pw] = e[pe]; else w[pw] = (byte) newval;
                        changed = true;
                    }
                }
                pw++;
                pe++;
            }
            return !changed;
        }
    }

    class wipe_exitColorXForm implements wipefun {

        public boolean invoke(int width, int height, int ticks) {
            return false;
        }
    }

    protected int[] y;

    class wipe_initMelt implements wipefun {

        public boolean invoke(int width, int height, int ticks) {
            int i, r;
            System.arraycopy(wipe_scr_start, 0, wipe_scr, 0, width * height);
            shittyColMajorXform(wipe_scr_start, width, height);
            shittyColMajorXform(wipe_scr_end, width, height);
            y = new int[width];
            y[0] = -(RND.M_Random() % 16);
            for (int j = 1; j < Y_SCALE; j++) {
                y[j] = y[j - 1];
            }
            for (i = Y_SCALE; i < width; i += Y_SCALE) {
                r = (RND.M_Random() % 3) - 1;
                y[i] = y[i - 1] + r;
                if (y[i] > 0) y[i] = 0; else if (y[i] == -16) y[i] = -15;
                for (int j = 1; j < Y_SCALE; j++) {
                    y[i + j] = y[i];
                }
            }
            return false;
        }
    }

    class wipe_doMelt implements wipefun {

        public boolean invoke(int width, int height, int ticks) {
            int dy;
            int idx;
            int ps;
            int pd;
            byte[] s;
            byte[] d = wipe_scr;
            boolean done = true;
            while (ticks-- > 0) {
                for (int i = 0; i < width; i++) {
                    if (y[i] < 0) {
                        y[i]++;
                        done = false;
                    } else if (y[i] < height) {
                        dy = (y[i] < 16 * Y_SCALE) ? y[i] + Y_SCALE : 8 * Y_SCALE;
                        if (y[i] + dy >= height) dy = height - y[i];
                        ps = i * height + y[i];
                        pd = y[i] * width + i;
                        idx = 0;
                        s = wipe_scr_end;
                        for (int j = dy; j > 0; j--) {
                            d[pd + idx] = s[ps++];
                            idx += width;
                        }
                        y[i] += dy;
                        s = wipe_scr_start;
                        ps = i * height;
                        pd = y[i] * width + i;
                        idx = 0;
                        for (int j = height - y[i]; j > 0; j--) {
                            d[pd + idx] = s[ps++];
                            idx += width;
                        }
                        done = false;
                    }
                }
            }
            return done;
        }
    }

    class wipe_exitMelt implements wipefun {

        public boolean invoke(int width, int height, int ticks) {
            y = null;
            return false;
        }
    }

    /** Sets "from" screen and stores it in "screen 2"*/
    @Override
    public boolean StartScreen(int x, int y, int width, int height) {
        wipe_scr_start = (byte[]) V.getScreen(DoomVideoRenderer.SCREEN_WS);
        VI.ReadScreen(wipe_scr_start);
        return false;
    }

    /** Sets "to" screen and stores it to "screen 3" */
    @Override
    public boolean EndScreen(int x, int y, int width, int height) {
        wipe_scr_end = (byte[]) V.getScreen(DoomVideoRenderer.SCREEN_WE);
        VI.ReadScreen(wipe_scr_end);
        byte[] screen_zero = (byte[]) V.getScreen(DoomVideoRenderer.SCREEN_FG);
        System.arraycopy(wipe_scr_start, 0, screen_zero, 0, SCREENWIDTH * SCREENHEIGHT);
        return false;
    }

    @Override
    public boolean ScreenWipe(int wipeno, int x, int y, int width, int height, int ticks) {
        boolean rc;
        if (!go) {
            go = true;
            wipe_scr = (byte[]) V.getScreen(DoomVideoRenderer.SCREEN_FG);
            (wipes[wipeno * 3]).invoke(width, height, ticks);
        }
        V.MarkRect(0, 0, width, height);
        rc = (wipes[wipeno * 3 + 1]).invoke(width, height, ticks);
        if (rc) {
            go = false;
            (wipes[wipeno * 3 + 2]).invoke(width, height, ticks);
        }
        return !go;
    }

    /** Interface for ASS-WIPING functions */
    interface wipefun {

        public boolean invoke(int width, int height, int ticks);
    }
}
