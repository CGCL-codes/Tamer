package fr.inria.zvtm.fits.filters;

import java.awt.Color;
import java.awt.image.RGBImageFilter;
import java.awt.LinearGradientPaint;

public class LightFilter extends RGBImageFilter implements ColorGradient {

    private static final Color[] map = new Color[128];

    static {
        map[0] = new Color(.00000f, .00390f, .00000f);
        map[1] = new Color(.00000f, .01180f, .05490f);
        map[2] = new Color(.00000f, .01960f, .10980f);
        map[3] = new Color(.00000f, .02750f, .15690f);
        map[4] = new Color(.00000f, .03530f, .20000f);
        map[5] = new Color(.00000f, .04310f, .23530f);
        map[6] = new Color(.00000f, .05100f, .26270f);
        map[7] = new Color(.00000f, .05880f, .29410f);
        map[8] = new Color(.00000f, .06670f, .31760f);
        map[9] = new Color(.00000f, .07450f, .34120f);
        map[10] = new Color(.00000f, .08240f, .36080f);
        map[11] = new Color(.00000f, .09020f, .38430f);
        map[12] = new Color(.00390f, .09800f, .40000f);
        map[13] = new Color(.01180f, .10590f, .41960f);
        map[14] = new Color(.01570f, .11370f, .43530f);
        map[15] = new Color(.02350f, .12160f, .45100f);
        map[16] = new Color(.02750f, .12940f, .46670f);
        map[17] = new Color(.03530f, .13730f, .48240f);
        map[18] = new Color(.04710f, .14510f, .49410f);
        map[19] = new Color(.05880f, .15290f, .50590f);
        map[20] = new Color(.07450f, .16080f, .51760f);
        map[21] = new Color(.09020f, .16860f, .53330f);
        map[22] = new Color(.10590f, .17650f, .54120f);
        map[23] = new Color(.12940f, .18430f, .55290f);
        map[24] = new Color(.15290f, .19220f, .56080f);
        map[25] = new Color(.18040f, .20000f, .57250f);
        map[26] = new Color(.20000f, .20780f, .58430f);
        map[27] = new Color(.23140f, .21570f, .59220f);
        map[28] = new Color(.26270f, .22350f, .60000f);
        map[29] = new Color(.29410f, .23140f, .60780f);
        map[30] = new Color(.32940f, .23920f, .61570f);
        map[31] = new Color(.36860f, .24710f, .62750f);
        map[32] = new Color(.40390f, .25490f, .63530f);
        map[33] = new Color(.43530f, .26270f, .64310f);
        map[34] = new Color(.47060f, .27060f, .65100f);
        map[35] = new Color(.50200f, .27840f, .65490f);
        map[36] = new Color(.52940f, .28630f, .66270f);
        map[37] = new Color(.56470f, .29410f, .67060f);
        map[38] = new Color(.59220f, .30200f, .67840f);
        map[39] = new Color(.61960f, .30980f, .68630f);
        map[40] = new Color(.63920f, .31760f, .69020f);
        map[41] = new Color(.65880f, .32550f, .69800f);
        map[42] = new Color(.67840f, .33330f, .70200f);
        map[43] = new Color(.69800f, .34120f, .70980f);
        map[44] = new Color(.71370f, .34900f, .71370f);
        map[45] = new Color(.72940f, .35690f, .72160f);
        map[46] = new Color(.74510f, .36470f, .72940f);
        map[47] = new Color(.76080f, .37250f, .73330f);
        map[48] = new Color(.77650f, .38040f, .74120f);
        map[49] = new Color(.78820f, .38820f, .74510f);
        map[50] = new Color(.80390f, .39610f, .74900f);
        map[51] = new Color(.81180f, .40390f, .75290f);
        map[52] = new Color(.82350f, .41180f, .76080f);
        map[53] = new Color(.83530f, .41960f, .76470f);
        map[54] = new Color(.84310f, .42750f, .76860f);
        map[55] = new Color(.85100f, .43530f, .77250f);
        map[56] = new Color(.85880f, .44310f, .78040f);
        map[57] = new Color(.86670f, .45100f, .78430f);
        map[58] = new Color(.87450f, .45880f, .78820f);
        map[59] = new Color(.88240f, .46670f, .79220f);
        map[60] = new Color(.89020f, .47450f, .79610f);
        map[61] = new Color(.89800f, .48240f, .80000f);
        map[62] = new Color(.90200f, .49020f, .80390f);
        map[63] = new Color(.90980f, .49800f, .80780f);
        map[64] = new Color(.91370f, .50590f, .81180f);
        map[65] = new Color(.92160f, .51370f, .81570f);
        map[66] = new Color(.92550f, .52160f, .81960f);
        map[67] = new Color(.92940f, .52940f, .82750f);
        map[68] = new Color(.93730f, .53730f, .83140f);
        map[69] = new Color(.93730f, .54510f, .83530f);
        map[70] = new Color(.94120f, .55290f, .83920f);
        map[71] = new Color(.94510f, .56080f, .84310f);
        map[72] = new Color(.94900f, .56860f, .84710f);
        map[73] = new Color(.95290f, .57650f, .85100f);
        map[74] = new Color(.95690f, .58430f, .85490f);
        map[75] = new Color(.96080f, .59220f, .85490f);
        map[76] = new Color(.96080f, .60000f, .85880f);
        map[77] = new Color(.96470f, .60780f, .86270f);
        map[78] = new Color(.96860f, .61570f, .86670f);
        map[79] = new Color(.97250f, .62350f, .87060f);
        map[80] = new Color(.97250f, .63140f, .87060f);
        map[81] = new Color(.97650f, .63920f, .87840f);
        map[82] = new Color(.98040f, .64710f, .88240f);
        map[83] = new Color(.98430f, .65490f, .88630f);
        map[84] = new Color(.98430f, .66270f, .89020f);
        map[85] = new Color(.98820f, .67060f, .89020f);
        map[86] = new Color(.99220f, .67840f, .89410f);
        map[87] = new Color(.99220f, .68630f, .89800f);
        map[88] = new Color(.99220f, .69410f, .89800f);
        map[89] = new Color(.99610f, .70200f, .90200f);
        map[90] = new Color(.99610f, .70980f, .90590f);
        map[91] = new Color(.99610f, .71760f, .90980f);
        map[92] = new Color(.99610f, .72550f, .90980f);
        map[93] = new Color(.99610f, .73330f, .91370f);
        map[94] = new Color(.99610f, .74120f, .91760f);
        map[95] = new Color(.99610f, .74900f, .91760f);
        map[96] = new Color(.99610f, .75690f, .92160f);
        map[97] = new Color(.99610f, .76470f, .92940f);
        map[98] = new Color(.99610f, .77250f, .92940f);
        map[99] = new Color(.99610f, .78040f, .93330f);
        map[100] = new Color(.99610f, .78820f, .93730f);
        map[101] = new Color(1.00000f, .79610f, .93730f);
        map[102] = new Color(1.00000f, .80390f, .94120f);
        map[103] = new Color(1.00000f, .81180f, .94120f);
        map[104] = new Color(1.00000f, .81960f, .94510f);
        map[105] = new Color(1.00000f, .82750f, .94900f);
        map[106] = new Color(1.00000f, .83530f, .94900f);
        map[107] = new Color(1.00000f, .84310f, .95290f);
        map[108] = new Color(1.00000f, .85100f, .95690f);
        map[109] = new Color(1.00000f, .85880f, .95690f);
        map[110] = new Color(1.00000f, .86670f, .96080f);
        map[111] = new Color(1.00000f, .87450f, .96080f);
        map[112] = new Color(1.00000f, .88240f, .96470f);
        map[113] = new Color(1.00000f, .89020f, .96470f);
        map[114] = new Color(1.00000f, .89800f, .96860f);
        map[115] = new Color(1.00000f, .90590f, .97250f);
        map[116] = new Color(1.00000f, .91370f, .97250f);
        map[117] = new Color(1.00000f, .92160f, .98040f);
        map[118] = new Color(1.00000f, .92940f, .98040f);
        map[119] = new Color(1.00000f, .93730f, .98430f);
        map[120] = new Color(1.00000f, .94510f, .98430f);
        map[121] = new Color(1.00000f, .95290f, .98820f);
        map[122] = new Color(1.00000f, .96080f, .98820f);
        map[123] = new Color(1.00000f, .96860f, .99220f);
        map[124] = new Color(1.00000f, .97650f, .99220f);
        map[125] = new Color(1.00000f, .98430f, .99610f);
        map[126] = new Color(1.00000f, .99220f, .99610f);
        map[127] = new Color(1.00000f, 1.00000f, 1.00000f);
    }

    public LightFilter() {
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
