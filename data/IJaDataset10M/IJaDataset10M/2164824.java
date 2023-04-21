package com.landak.ipod.gain;

import java.util.Arrays;

/**
 * 
 * Ported from mp3gain
 * 
 * NOTICE: if you use this code please let me know personally
 * NOTICE: consider buying me an ipod nano
 * 
 * 
 * 
 * 
 * @author khad (khad@users.sourceforge.net)
 *
 */
class Analyzer {

    public static final int GAIN_NOT_ENOUGH_SAMPLES = -24601;

    public static final int GAIN_ANALYSIS_ERROR = 0;

    public static final int GAIN_ANALYSIS_OK = 1;

    public static final int INIT_GAIN_ANALYSIS_ERROR = 0;

    public static final int INIT_GAIN_ANALYSIS_OK = 1;

    private static final int YULE_ORDER = 10;

    private static final int BUTTER_ORDER = 2;

    private static final double RMS_PERCENTILE = 0.95;

    private static final double MAX_SAMP_FREQ = 48000.;

    private static final double RMS_WINDOW_TIME = 0.050;

    private static final double STEPS_per_dB = 100.;

    private static final double MAX_dB = 120.;

    private static final int MAX_ORDER = (BUTTER_ORDER > YULE_ORDER ? BUTTER_ORDER : YULE_ORDER);

    private static final double MAX_SAMPLES_PER_WINDOW = (MAX_SAMP_FREQ * RMS_WINDOW_TIME);

    private static final double PINK_REF = 64.82;

    private static double ABYule[][] = { { 0.03857599435200, -3.84664617118067, -0.02160367184185, 7.81501653005538, -0.00123395316851, -11.34170355132042, -0.00009291677959, 13.05504219327545, -0.01655260341619, -12.28759895145294, 0.02161526843274, 9.48293806319790, -0.02074045215285, -5.87257861775999, 0.00594298065125, 2.75465861874613, 0.00306428023191, -0.86984376593551, 0.00012025322027, 0.13919314567432, 0.00288463683916 }, { 0.05418656406430, -3.47845948550071, -0.02911007808948, 6.36317777566148, -0.00848709379851, -8.54751527471874, -0.00851165645469, 9.47693607801280, -0.00834990904936, -8.81498681370155, 0.02245293253339, 6.85401540936998, -0.02596338512915, -4.39470996079559, 0.01624864962975, 2.19611684890774, -0.00240879051584, -0.75104302451432, 0.00674613682247, 0.13149317958808, -0.00187763777362 }, { 0.15457299681924, -2.37898834973084, -0.09331049056315, 2.84868151156327, -0.06247880153653, -2.64577170229825, 0.02163541888798, 2.23697657451713, -0.05588393329856, -1.67148153367602, 0.04781476674921, 1.00595954808547, 0.00222312597743, -0.45953458054983, 0.03174092540049, 0.16378164858596, -0.01390589421898, -0.05032077717131, 0.00651420667831, 0.02347897407020, -0.00881362733839 }, { 0.30296907319327, -1.61273165137247, -0.22613988682123, 1.07977492259970, -0.08587323730772, -0.25656257754070, 0.03282930172664, -0.16276719120440, -0.00915702933434, -0.22638893773906, -0.02364141202522, 0.39120800788284, -0.00584456039913, -0.22138138954925, 0.06276101321749, 0.04500235387352, -0.00000828086748, 0.02005851806501, 0.00205861885564, 0.00302439095741, -0.02950134983287 }, { 0.33642304856132, -1.49858979367799, -0.25572241425570, 0.87350271418188, -0.11828570177555, 0.12205022308084, 0.11921148675203, -0.80774944671438, -0.07834489609479, 0.47854794562326, -0.00469977914380, -0.12453458140019, -0.00589500224440, -0.04067510197014, 0.05724228140351, 0.08333755284107, 0.00832043980773, -0.04237348025746, -0.01635381384540, 0.02977207319925, -0.01760176568150 }, { 0.44915256608450, -0.62820619233671, -0.14351757464547, 0.29661783706366, -0.22784394429749, -0.37256372942400, -0.01419140100551, 0.00213767857124, 0.04078262797139, -0.42029820170918, -0.12398163381748, 0.22199650564824, 0.04097565135648, 0.00613424350682, 0.10478503600251, 0.06747620744683, -0.01863887810927, 0.05784820375801, -0.03193428438915, 0.03222754072173, 0.00541907748707 }, { 0.56619470757641, -1.04800335126349, -0.75464456939302, 0.29156311971249, 0.16242137742230, -0.26806001042947, 0.16744243493672, 0.00819999645858, -0.18901604199609, 0.45054734505008, 0.30931782841830, -0.33032403314006, -0.27562961986224, 0.06739368333110, 0.00647310677246, -0.04784254229033, 0.08647503780351, 0.01639907836189, -0.03788984554840, 0.01807364323573, -0.00588215443421 }, { 0.58100494960553, -0.51035327095184, -0.53174909058578, -0.31863563325245, -0.14289799034253, -0.20256413484477, 0.17520704835522, 0.14728154134330, 0.02377945217615, 0.38952639978999, 0.15558449135573, -0.23313271880868, -0.25344790059353, -0.05246019024463, 0.01628462406333, -0.02505961724053, 0.06920467763959, 0.02442357316099, -0.03721611395801, 0.01818801111503, -0.00749618797172 }, { 0.53648789255105, -0.25049871956020, -0.42163034350696, -0.43193942311114, -0.00275953611929, -0.03424681017675, 0.04267842219415, -0.04678328784242, -0.10214864179676, 0.26408300200955, 0.14590772289388, 0.15113130533216, -0.02459864859345, -0.17556493366449, -0.11202315195388, -0.18823009262115, -0.04060034127000, 0.05477720428674, 0.04788665548180, 0.04704409688120, -0.02217936801134 } };

    private static double ABButter[][] = { { 0.98621192462708, -1.97223372919527, -1.97242384925416, 0.97261396931306, 0.98621192462708 }, { 0.98500175787242, -1.96977855582618, -1.97000351574484, 0.97022847566350, 0.98500175787242 }, { 0.97938932735214, -1.95835380975398, -1.95877865470428, 0.95920349965459, 0.97938932735214 }, { 0.97531843204928, -1.95002759149878, -1.95063686409857, 0.95124613669835, 0.97531843204928 }, { 0.97316523498161, -1.94561023566527, -1.94633046996323, 0.94705070426118, 0.97316523498161 }, { 0.96454515552826, -1.92783286977036, -1.92909031105652, 0.93034775234268, 0.96454515552826 }, { 0.96009142950541, -1.91858953033784, -1.92018285901082, 0.92177618768381, 0.96009142950541 }, { 0.95856916599601, -1.91542108074780, -1.91713833199203, 0.91885558323625, 0.95856916599601 }, { 0.94597685600279, -1.88903307939452, -1.89195371200558, 0.89487434461664, 0.94597685600279 } };

    private double linprebuf[] = new double[MAX_ORDER * 2];

    private int linpre;

    private double lstepbuf[] = new double[(int) (MAX_SAMPLES_PER_WINDOW + MAX_ORDER)];

    private int lstep;

    private double loutbuf[] = new double[(int) (MAX_SAMPLES_PER_WINDOW + MAX_ORDER)];

    private int lout;

    private double rinprebuf[] = new double[MAX_ORDER * 2];

    private int rinpre;

    private double rstepbuf[] = new double[(int) (MAX_SAMPLES_PER_WINDOW + MAX_ORDER)];

    private int rstep;

    private double routbuf[] = new double[(int) (MAX_SAMPLES_PER_WINDOW + MAX_ORDER)];

    private int rout;

    private int sampleWindow;

    private int totsamp;

    private double lsum;

    private double rsum;

    private int freqindex;

    private int first;

    private int A[] = new int[(int) (STEPS_per_dB * MAX_dB)];

    public Analyzer(long samplefreq) {
        if (ResetSampleFrequency(samplefreq) != INIT_GAIN_ANALYSIS_OK) {
            System.err.println("INIT_GAIN_ANALYSIS_ERROR");
        }
        linpre = MAX_ORDER;
        rinpre = MAX_ORDER;
        lstep = MAX_ORDER;
        rstep = MAX_ORDER;
        lout = MAX_ORDER;
        rout = MAX_ORDER;
    }

    private int ResetSampleFrequency(long samplefreq) {
        int i;
        switch((int) (samplefreq)) {
            case 48000:
                freqindex = 0;
                break;
            case 44100:
                freqindex = 1;
                break;
            case 32000:
                freqindex = 2;
                break;
            case 24000:
                freqindex = 3;
                break;
            case 22050:
                freqindex = 4;
                break;
            case 16000:
                freqindex = 5;
                break;
            case 12000:
                freqindex = 6;
                break;
            case 11025:
                freqindex = 7;
                break;
            case 8000:
                freqindex = 8;
                break;
            default:
                return INIT_GAIN_ANALYSIS_ERROR;
        }
        sampleWindow = (int) Math.ceil(samplefreq * RMS_WINDOW_TIME);
        lsum = 0.;
        rsum = 0.;
        totsamp = 0;
        return INIT_GAIN_ANALYSIS_OK;
    }

    private void memcpy(double dst[], int dstofs, double src[], int srcofs, int len) {
        for (int a = 0; a < len; a++) {
            dst[dstofs++] = src[srcofs++];
        }
    }

    private void memmove(double dst[], int dstofs, double src[], int srcofs, int len) {
        memcpy(dst, dstofs, src, srcofs, len);
    }

    private static void filterYule(final double input[], int iofs, double output[], int oofs, long nSamples, double kernel[]) {
        while (nSamples-- > 0) {
            output[(int) oofs] = input[iofs + 0] * kernel[0] - output[oofs - 1] * kernel[1] + input[iofs - 1] * kernel[2] - output[oofs - 2] * kernel[3] + input[iofs - 2] * kernel[4] - output[oofs - 3] * kernel[5] + input[iofs - 3] * kernel[6] - output[oofs - 4] * kernel[7] + input[iofs - 4] * kernel[8] - output[oofs - 5] * kernel[9] + input[iofs - 5] * kernel[10] - output[oofs - 6] * kernel[11] + input[iofs - 6] * kernel[12] - output[oofs - 7] * kernel[13] + input[iofs - 7] * kernel[14] - output[oofs - 8] * kernel[15] + input[iofs - 8] * kernel[16] - output[oofs - 9] * kernel[17] + input[iofs - 9] * kernel[18] - output[oofs - 10] * kernel[19] + input[iofs - 10] * kernel[20];
            oofs++;
            iofs++;
        }
    }

    private static void filterButter(final double input[], int iofs, double output[], int oofs, long nSamples, double kernel[]) {
        while (nSamples-- > 0) {
            output[oofs] = input[iofs + 0] * kernel[0] - output[oofs - 1] * kernel[1] + input[iofs - 1] * kernel[2] - output[oofs - 2] * kernel[3] + input[iofs - 2] * kernel[4];
            oofs++;
            iofs++;
        }
    }

    int AnalyzeSamples(double left_samples[], double right_samples[], int num_samples, int num_channels) {
        double curleft[];
        double curright[];
        int curleftp;
        int currightp;
        int batchsamples;
        int cursamples;
        int cursamplepos;
        int i;
        if (num_samples == 0) return GAIN_ANALYSIS_OK;
        cursamplepos = 0;
        batchsamples = num_samples;
        switch(num_channels) {
            case 1:
                right_samples = left_samples;
            case 2:
                break;
            default:
                return GAIN_ANALYSIS_ERROR;
        }
        if (num_samples < MAX_ORDER) {
            memcpy(linprebuf, MAX_ORDER, left_samples, 0, num_samples);
            memcpy(rinprebuf, MAX_ORDER, right_samples, 0, num_samples);
        } else {
            memcpy(linprebuf, MAX_ORDER, left_samples, 0, MAX_ORDER);
            memcpy(rinprebuf, MAX_ORDER, right_samples, 0, MAX_ORDER);
        }
        while (batchsamples > 0) {
            cursamples = batchsamples > sampleWindow - totsamp ? sampleWindow - totsamp : batchsamples;
            if (cursamplepos < MAX_ORDER) {
                curleft = linprebuf;
                curright = rinprebuf;
                curleftp = linpre + cursamplepos;
                currightp = rinpre + cursamplepos;
                if (cursamples > MAX_ORDER - cursamplepos) cursamples = MAX_ORDER - cursamplepos;
            } else {
                curleft = left_samples;
                curright = right_samples;
                curleftp = cursamplepos;
                currightp = cursamplepos;
            }
            filterYule(left_samples, curleftp, lstepbuf, lstep + totsamp, cursamples, ABYule[freqindex]);
            filterYule(right_samples, currightp, rstepbuf, rstep + totsamp, cursamples, ABYule[freqindex]);
            filterButter(lstepbuf, lstep + totsamp, loutbuf, lout + totsamp, cursamples, ABButter[freqindex]);
            filterButter(rstepbuf, rstep + totsamp, routbuf, rout + totsamp, cursamples, ABButter[freqindex]);
            curleft = loutbuf;
            curright = routbuf;
            curleftp = lout + totsamp;
            currightp = rout + totsamp;
            i = cursamples % 16;
            while (i-- > 0) {
                lsum += curleft[curleftp] * curleft[curleftp];
                rsum += curright[currightp] * curright[currightp];
                curleftp++;
                currightp++;
            }
            i = cursamples / 16;
            while (i-- > 0) {
                lsum += curleft[curleftp + 0] * curleft[curleftp + 0] + curleft[curleftp + 1] * curleft[curleftp + 1] + curleft[curleftp + 2] * curleft[curleftp + 2] + curleft[curleftp + 3] * curleft[curleftp + 3] + curleft[curleftp + 4] * curleft[curleftp + 4] + curleft[curleftp + 5] * curleft[curleftp + 5] + curleft[curleftp + 6] * curleft[curleftp + 6] + curleft[curleftp + 7] * curleft[curleftp + 7] + curleft[curleftp + 8] * curleft[curleftp + 8] + curleft[curleftp + 9] * curleft[curleftp + 9] + curleft[curleftp + 10] * curleft[curleftp + 10] + curleft[curleftp + 11] * curleft[curleftp + 11] + curleft[curleftp + 12] * curleft[curleftp + 12] + curleft[curleftp + 13] * curleft[curleftp + 13] + curleft[curleftp + 14] * curleft[curleftp + 14] + curleft[curleftp + 15] * curleft[curleftp + 15];
                curleftp += 16;
                rsum += curright[currightp + 0] * curright[currightp + 0] + curright[currightp + 1] * curright[currightp + 1] + curright[currightp + 2] * curright[currightp + 2] + curright[currightp + 3] * curright[currightp + 3] + curright[currightp + 4] * curright[currightp + 4] + curright[currightp + 5] * curright[currightp + 5] + curright[currightp + 6] * curright[currightp + 6] + curright[currightp + 7] * curright[currightp + 7] + curright[currightp + 8] * curright[currightp + 8] + curright[currightp + 9] * curright[currightp + 9] + curright[currightp + 10] * curright[currightp + 10] + curright[currightp + 11] * curright[currightp + 11] + curright[currightp + 12] * curright[currightp + 12] + curright[currightp + 13] * curright[currightp + 13] + curright[currightp + 14] * curright[currightp + 14] + curright[currightp + 15] * curright[currightp + 15];
                currightp += 16;
            }
            batchsamples -= cursamples;
            cursamplepos += cursamples;
            totsamp += cursamples;
            if (totsamp == sampleWindow) {
                double val = STEPS_per_dB * 10. * log10((lsum + rsum) / totsamp * 0.5 + 1.e-37);
                int ival = (int) val;
                if (ival < 0) ival = 0;
                if (ival >= A.length) ival = A.length - 1;
                A[ival]++;
                lsum = rsum = 0.;
                memmove(loutbuf, 0, loutbuf, totsamp, MAX_ORDER);
                memmove(routbuf, 0, routbuf, totsamp, MAX_ORDER);
                memmove(lstepbuf, 0, lstepbuf, totsamp, MAX_ORDER);
                memmove(rstepbuf, 0, rstepbuf, totsamp, MAX_ORDER);
                totsamp = 0;
            }
            if (totsamp > sampleWindow) return GAIN_ANALYSIS_ERROR;
        }
        if (num_samples < MAX_ORDER) {
            memmove(linprebuf, 0, linprebuf, num_samples, (MAX_ORDER - num_samples));
            memmove(rinprebuf, 0, rinprebuf, num_samples, (MAX_ORDER - num_samples));
            memcpy(linprebuf, MAX_ORDER - num_samples, left_samples, 0, num_samples);
            memcpy(rinprebuf, MAX_ORDER - num_samples, right_samples, 0, num_samples);
        } else {
            memcpy(linprebuf, 0, left_samples, num_samples - MAX_ORDER, MAX_ORDER);
            memcpy(rinprebuf, 0, right_samples, num_samples - MAX_ORDER, MAX_ORDER);
        }
        return GAIN_ANALYSIS_OK;
    }

    private double log10(double d) {
        return Math.log(d) / Math.log(10);
    }

    private static double analyzeResult(int Array[], int len) {
        int elems;
        int upper;
        int i;
        elems = 0;
        for (i = 0; i < len; i++) elems += Array[i];
        if (elems == 0) return GAIN_NOT_ENOUGH_SAMPLES;
        upper = (int) Math.ceil(elems * (1. - RMS_PERCENTILE));
        for (i = len; i-- > 0; ) {
            if ((upper -= Array[i]) <= 0) break;
        }
        return PINK_REF - i / STEPS_per_dB;
    }

    public double GetTitleGain() {
        double retval;
        int i;
        retval = analyzeResult(A, A.length);
        for (i = 0; i < A.length; i++) {
            A[i] = 0;
        }
        for (i = 0; i < MAX_ORDER; i++) linprebuf[i] = lstepbuf[i] = loutbuf[i] = rinprebuf[i] = rstepbuf[i] = routbuf[i] = 0.;
        totsamp = 0;
        lsum = rsum = 0.;
        return retval;
    }
}
