package fr.inria.zvtm.fits.filters;

import java.awt.Color;
import java.awt.image.RGBImageFilter;
import java.awt.LinearGradientPaint;

public class StaircaseFilter extends RGBImageFilter implements ColorGradient {

    private static final Color[] map = new Color[128];

    static {
        map[0] = new Color(.00390f, .00390f, .31370f);
        map[1] = new Color(.01180f, .01180f, .31370f);
        map[2] = new Color(.01960f, .01960f, .31370f);
        map[3] = new Color(.02750f, .02750f, .31370f);
        map[4] = new Color(.03530f, .03530f, .31370f);
        map[5] = new Color(.04310f, .04310f, .31370f);
        map[6] = new Color(.05100f, .05100f, .31370f);
        map[7] = new Color(.05880f, .05880f, .31370f);
        map[8] = new Color(.06670f, .06670f, .47060f);
        map[9] = new Color(.07450f, .07450f, .47060f);
        map[10] = new Color(.08240f, .08240f, .47060f);
        map[11] = new Color(.09020f, .09020f, .47060f);
        map[12] = new Color(.09800f, .09800f, .47060f);
        map[13] = new Color(.10590f, .10590f, .47060f);
        map[14] = new Color(.11370f, .11370f, .47060f);
        map[15] = new Color(.12160f, .12160f, .47060f);
        map[16] = new Color(.12940f, .12940f, .62750f);
        map[17] = new Color(.13730f, .13730f, .62750f);
        map[18] = new Color(.14510f, .14510f, .62750f);
        map[19] = new Color(.15290f, .15290f, .62750f);
        map[20] = new Color(.16080f, .16080f, .62750f);
        map[21] = new Color(.16860f, .16860f, .62750f);
        map[22] = new Color(.17650f, .17650f, .62750f);
        map[23] = new Color(.18430f, .18430f, .62750f);
        map[24] = new Color(.19220f, .19220f, .78430f);
        map[25] = new Color(.20000f, .20000f, .78430f);
        map[26] = new Color(.20780f, .20780f, .78430f);
        map[27] = new Color(.21570f, .21570f, .78430f);
        map[28] = new Color(.22350f, .22350f, .78430f);
        map[29] = new Color(.23140f, .23140f, .78430f);
        map[30] = new Color(.23920f, .23920f, .78430f);
        map[31] = new Color(.24710f, .24710f, .78430f);
        map[32] = new Color(.25490f, .25490f, .94120f);
        map[33] = new Color(.26270f, .26270f, .94120f);
        map[34] = new Color(.27060f, .27060f, .94120f);
        map[35] = new Color(.27840f, .27840f, .94120f);
        map[36] = new Color(.28630f, .28630f, .94120f);
        map[37] = new Color(.29410f, .29410f, .94120f);
        map[38] = new Color(.30200f, .30200f, .94120f);
        map[39] = new Color(.30980f, .30980f, .94120f);
        map[40] = new Color(.31760f, .31760f, .95290f);
        map[41] = new Color(.32550f, .32550f, .97650f);
        map[42] = new Color(.33330f, .33330f, 1.00000f);
        map[43] = new Color(.00780f, .31370f, .00780f);
        map[44] = new Color(.01570f, .31370f, .01570f);
        map[45] = new Color(.02350f, .31370f, .02350f);
        map[46] = new Color(.03140f, .31370f, .03140f);
        map[47] = new Color(.03920f, .31370f, .03920f);
        map[48] = new Color(.04710f, .31370f, .04710f);
        map[49] = new Color(.05490f, .31370f, .05490f);
        map[50] = new Color(.06270f, .31370f, .06270f);
        map[51] = new Color(.07060f, .47060f, .07060f);
        map[52] = new Color(.07840f, .47060f, .07840f);
        map[53] = new Color(.08630f, .47060f, .08630f);
        map[54] = new Color(.09410f, .47060f, .09410f);
        map[55] = new Color(.10200f, .47060f, .10200f);
        map[56] = new Color(.10980f, .47060f, .10980f);
        map[57] = new Color(.11760f, .47060f, .11760f);
        map[58] = new Color(.12550f, .47060f, .12550f);
        map[59] = new Color(.13330f, .62750f, .13330f);
        map[60] = new Color(.14120f, .62750f, .14120f);
        map[61] = new Color(.14900f, .62750f, .14900f);
        map[62] = new Color(.15690f, .62750f, .15690f);
        map[63] = new Color(.16470f, .62750f, .16470f);
        map[64] = new Color(.17250f, .62750f, .17250f);
        map[65] = new Color(.18040f, .62750f, .18040f);
        map[66] = new Color(.18820f, .62750f, .18820f);
        map[67] = new Color(.19610f, .78430f, .19610f);
        map[68] = new Color(.20390f, .78430f, .20390f);
        map[69] = new Color(.21180f, .78430f, .21180f);
        map[70] = new Color(.21960f, .78430f, .21960f);
        map[71] = new Color(.22750f, .78430f, .22750f);
        map[72] = new Color(.23530f, .78430f, .23530f);
        map[73] = new Color(.24310f, .78430f, .24310f);
        map[74] = new Color(.25100f, .78430f, .25100f);
        map[75] = new Color(.25880f, .94120f, .25880f);
        map[76] = new Color(.26670f, .94120f, .26670f);
        map[77] = new Color(.27450f, .94120f, .27450f);
        map[78] = new Color(.28240f, .94120f, .28240f);
        map[79] = new Color(.29020f, .94120f, .29020f);
        map[80] = new Color(.29800f, .94120f, .29800f);
        map[81] = new Color(.30590f, .94120f, .30590f);
        map[82] = new Color(.31370f, .94120f, .31370f);
        map[83] = new Color(.32160f, .96470f, .32160f);
        map[84] = new Color(.32940f, .98820f, .32940f);
        map[85] = new Color(.31370f, .00390f, .00390f);
        map[86] = new Color(.31370f, .01180f, .01180f);
        map[87] = new Color(.31370f, .01960f, .01960f);
        map[88] = new Color(.31370f, .02750f, .02750f);
        map[89] = new Color(.31370f, .03530f, .03530f);
        map[90] = new Color(.31370f, .04310f, .04310f);
        map[91] = new Color(.31370f, .05100f, .05100f);
        map[92] = new Color(.31370f, .05880f, .05880f);
        map[93] = new Color(.47060f, .06670f, .06670f);
        map[94] = new Color(.47060f, .07450f, .07450f);
        map[95] = new Color(.47060f, .08240f, .08240f);
        map[96] = new Color(.47060f, .09020f, .09020f);
        map[97] = new Color(.47060f, .09800f, .09800f);
        map[98] = new Color(.47060f, .10590f, .10590f);
        map[99] = new Color(.47060f, .11370f, .11370f);
        map[100] = new Color(.47060f, .12160f, .12160f);
        map[101] = new Color(.62750f, .12940f, .12940f);
        map[102] = new Color(.62750f, .13730f, .13730f);
        map[103] = new Color(.62750f, .14510f, .14510f);
        map[104] = new Color(.62750f, .15290f, .15290f);
        map[105] = new Color(.62750f, .16080f, .16080f);
        map[106] = new Color(.62750f, .16860f, .16860f);
        map[107] = new Color(.62750f, .17650f, .17650f);
        map[108] = new Color(.62750f, .18430f, .18430f);
        map[109] = new Color(.78430f, .19220f, .19220f);
        map[110] = new Color(.78430f, .20000f, .20000f);
        map[111] = new Color(.78430f, .20780f, .20780f);
        map[112] = new Color(.78430f, .21570f, .21570f);
        map[113] = new Color(.78430f, .22350f, .22350f);
        map[114] = new Color(.78430f, .23140f, .23140f);
        map[115] = new Color(.78430f, .23920f, .23920f);
        map[116] = new Color(.78430f, .24710f, .24710f);
        map[117] = new Color(.94120f, .25490f, .25490f);
        map[118] = new Color(.94120f, .26270f, .26270f);
        map[119] = new Color(.94120f, .27060f, .27060f);
        map[120] = new Color(.94120f, .27840f, .27840f);
        map[121] = new Color(.94120f, .28630f, .28630f);
        map[122] = new Color(.94120f, .29410f, .29410f);
        map[123] = new Color(.94120f, .30200f, .30200f);
        map[124] = new Color(.94120f, .30980f, .30980f);
        map[125] = new Color(.94900f, .39220f, .39220f);
        map[126] = new Color(.97250f, .66670f, .66670f);
        map[127] = new Color(.99220f, .80000f, .80000f);
    }

    public StaircaseFilter() {
    }

    public int filterRGB(int x, int y, int rgb) {
        return map[(rgb & 0xff) / 2].getRGB();
    }

    public LinearGradientPaint getGradient(float w) {
        return getGradientS(w);
    }

    public static LinearGradientPaint getGradientS(float w) {
        float[] fractions = new float[map.length];
        for (int i = 0; i < fractions.length; i++) {
            fractions[i] = i / (float) fractions.length;
        }
        return new LinearGradientPaint(0, 0, w, 0, fractions, map);
    }
}
