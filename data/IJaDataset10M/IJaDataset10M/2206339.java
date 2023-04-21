package mp3;

import java.util.Arrays;

public class Quantize {

    BitStream bs;

    Reservoir rv;

    QuantizePVT qupvt;

    VBRQuantize vbr = new VBRQuantize();

    Takehiro tk;

    public final void setModules(BitStream bs, Reservoir rv, QuantizePVT qupvt, Takehiro tk) {
        this.bs = bs;
        this.rv = rv;
        this.qupvt = qupvt;
        this.tk = tk;
        this.vbr.setModules(qupvt, tk);
    }

    /**
	 * convert from L/R <. Mid/Side
	 */
    public final void ms_convert(final IIISideInfo l3_side, final int gr) {
        for (int i = 0; i < 576; ++i) {
            float l = l3_side.tt[gr][0].xr[i];
            float r = l3_side.tt[gr][1].xr[i];
            l3_side.tt[gr][0].xr[i] = (l + r) * (float) (Util.SQRT2 * 0.5);
            l3_side.tt[gr][1].xr[i] = (l - r) * (float) (Util.SQRT2 * 0.5);
        }
    }

    /**
	 * mt 6/99
	 * 
	 * initializes cod_info, scalefac and xrpow
	 * 
	 * returns 0 if all energies in xr are zero, else 1
	 */
    private float init_xrpow_core(final GrInfo cod_info, float xrpow[], int upper, float sum) {
        sum = 0;
        for (int i = 0; i <= upper; ++i) {
            float tmp = Math.abs(cod_info.xr[i]);
            sum += tmp;
            xrpow[i] = (float) Math.sqrt(tmp * Math.sqrt(tmp));
            if (xrpow[i] > cod_info.xrpow_max) cod_info.xrpow_max = xrpow[i];
        }
        return sum;
    }

    public final boolean init_xrpow(final LameInternalFlags gfc, final GrInfo cod_info, float xrpow[]) {
        float sum = 0;
        final int upper = cod_info.max_nonzero_coeff;
        assert (xrpow != null);
        cod_info.xrpow_max = 0;
        assert (0 <= upper && upper <= 575);
        Arrays.fill(xrpow, upper, 576, 0);
        sum = init_xrpow_core(cod_info, xrpow, upper, sum);
        if (sum > 1E-20f) {
            int j = 0;
            if ((gfc.substep_shaping & 2) != 0) j = 1;
            for (int i = 0; i < cod_info.psymax; i++) gfc.pseudohalf[i] = j;
            return true;
        }
        Arrays.fill(cod_info.l3_enc, 0, 576, 0);
        return false;
    }

    /**
	 * Gabriel Bouvigne feb/apr 2003<BR>
	 * Analog silence detection in partitionned sfb21 or sfb12 for short blocks
	 * 
	 * From top to bottom of sfb, changes to 0 coeffs which are below ath. It
	 * stops on the first coeff higher than ath.
	 */
    private void psfb21_analogsilence(final LameInternalFlags gfc, final GrInfo cod_info) {
        final ATH ath = gfc.ATH;
        final float[] xr = cod_info.xr;
        if (cod_info.block_type != Encoder.SHORT_TYPE) {
            boolean stop = false;
            for (int gsfb = Encoder.PSFB21 - 1; gsfb >= 0 && !stop; gsfb--) {
                final int start = gfc.scalefac_band.psfb21[gsfb];
                final int end = gfc.scalefac_band.psfb21[gsfb + 1];
                float ath21 = qupvt.athAdjust(ath.adjust, ath.psfb21[gsfb], ath.floor);
                if (gfc.nsPsy.longfact[21] > 1e-12f) ath21 *= gfc.nsPsy.longfact[21];
                for (int j = end - 1; j >= start; j--) {
                    if (Math.abs(xr[j]) < ath21) xr[j] = 0; else {
                        stop = true;
                        break;
                    }
                }
            }
        } else {
            for (int block = 0; block < 3; block++) {
                boolean stop = false;
                for (int gsfb = Encoder.PSFB12 - 1; gsfb >= 0 && !stop; gsfb--) {
                    final int start = gfc.scalefac_band.s[12] * 3 + (gfc.scalefac_band.s[13] - gfc.scalefac_band.s[12]) * block + (gfc.scalefac_band.psfb12[gsfb] - gfc.scalefac_band.psfb12[0]);
                    final int end = start + (gfc.scalefac_band.psfb12[gsfb + 1] - gfc.scalefac_band.psfb12[gsfb]);
                    float ath12 = qupvt.athAdjust(ath.adjust, ath.psfb12[gsfb], ath.floor);
                    if (gfc.nsPsy.shortfact[12] > 1e-12f) ath12 *= gfc.nsPsy.shortfact[12];
                    for (int j = end - 1; j >= start; j--) {
                        if (Math.abs(xr[j]) < ath12) xr[j] = 0; else {
                            stop = true;
                            break;
                        }
                    }
                }
            }
        }
    }

    public final void init_outer_loop(final LameInternalFlags gfc, final GrInfo cod_info) {
        cod_info.part2_3_length = 0;
        cod_info.big_values = 0;
        cod_info.count1 = 0;
        cod_info.global_gain = 210;
        cod_info.scalefac_compress = 0;
        cod_info.table_select[0] = 0;
        cod_info.table_select[1] = 0;
        cod_info.table_select[2] = 0;
        cod_info.subblock_gain[0] = 0;
        cod_info.subblock_gain[1] = 0;
        cod_info.subblock_gain[2] = 0;
        cod_info.subblock_gain[3] = 0;
        cod_info.region0_count = 0;
        cod_info.region1_count = 0;
        cod_info.preflag = 0;
        cod_info.scalefac_scale = 0;
        cod_info.count1table_select = 0;
        cod_info.part2_length = 0;
        cod_info.sfb_lmax = Encoder.SBPSY_l;
        cod_info.sfb_smin = Encoder.SBPSY_s;
        cod_info.psy_lmax = gfc.sfb21_extra ? Encoder.SBMAX_l : Encoder.SBPSY_l;
        cod_info.psymax = cod_info.psy_lmax;
        cod_info.sfbmax = cod_info.sfb_lmax;
        cod_info.sfbdivide = 11;
        for (int sfb = 0; sfb < Encoder.SBMAX_l; sfb++) {
            cod_info.width[sfb] = gfc.scalefac_band.l[sfb + 1] - gfc.scalefac_band.l[sfb];
            cod_info.window[sfb] = 3;
        }
        if (cod_info.block_type == Encoder.SHORT_TYPE) {
            float ixwork[] = new float[576];
            cod_info.sfb_smin = 0;
            cod_info.sfb_lmax = 0;
            if (cod_info.mixed_block_flag != 0) {
                cod_info.sfb_smin = 3;
                cod_info.sfb_lmax = gfc.mode_gr * 2 + 4;
            }
            cod_info.psymax = cod_info.sfb_lmax + 3 * ((gfc.sfb21_extra ? Encoder.SBMAX_s : Encoder.SBPSY_s) - cod_info.sfb_smin);
            cod_info.sfbmax = cod_info.sfb_lmax + 3 * (Encoder.SBPSY_s - cod_info.sfb_smin);
            cod_info.sfbdivide = cod_info.sfbmax - 18;
            cod_info.psy_lmax = cod_info.sfb_lmax;
            int ix = gfc.scalefac_band.l[cod_info.sfb_lmax];
            System.arraycopy(cod_info.xr, 0, ixwork, 0, 576);
            for (int sfb = cod_info.sfb_smin; sfb < Encoder.SBMAX_s; sfb++) {
                final int start = gfc.scalefac_band.s[sfb];
                final int end = gfc.scalefac_band.s[sfb + 1];
                for (int window = 0; window < 3; window++) {
                    for (int l = start; l < end; l++) {
                        cod_info.xr[ix++] = ixwork[3 * l + window];
                    }
                }
            }
            int j = cod_info.sfb_lmax;
            for (int sfb = cod_info.sfb_smin; sfb < Encoder.SBMAX_s; sfb++) {
                cod_info.width[j] = cod_info.width[j + 1] = cod_info.width[j + 2] = gfc.scalefac_band.s[sfb + 1] - gfc.scalefac_band.s[sfb];
                cod_info.window[j] = 0;
                cod_info.window[j + 1] = 1;
                cod_info.window[j + 2] = 2;
                j += 3;
            }
        }
        cod_info.count1bits = 0;
        cod_info.sfb_partition_table = qupvt.nr_of_sfb_block[0][0];
        cod_info.slen[0] = 0;
        cod_info.slen[1] = 0;
        cod_info.slen[2] = 0;
        cod_info.slen[3] = 0;
        cod_info.max_nonzero_coeff = 575;
        Arrays.fill(cod_info.scalefac, 0);
        psfb21_analogsilence(gfc, cod_info);
    }

    private enum BinSearchDirection {

        BINSEARCH_NONE, BINSEARCH_UP, BINSEARCH_DOWN
    }

    /**
	 * author/date??
	 * 
	 * binary step size search used by outer_loop to get a quantizer step size
	 * to start with
	 */
    private int bin_search_StepSize(final LameInternalFlags gfc, final GrInfo cod_info, int desired_rate, final int ch, final float xrpow[]) {
        int nBits;
        int CurrentStep = gfc.CurrentStep[ch];
        boolean flagGoneOver = false;
        final int start = gfc.OldValue[ch];
        BinSearchDirection Direction = BinSearchDirection.BINSEARCH_NONE;
        cod_info.global_gain = start;
        desired_rate -= cod_info.part2_length;
        assert (CurrentStep != 0);
        for (; ; ) {
            int step;
            nBits = tk.count_bits(gfc, xrpow, cod_info, null);
            if (CurrentStep == 1 || nBits == desired_rate) break;
            if (nBits > desired_rate) {
                if (Direction == BinSearchDirection.BINSEARCH_DOWN) flagGoneOver = true;
                if (flagGoneOver) CurrentStep /= 2;
                Direction = BinSearchDirection.BINSEARCH_UP;
                step = CurrentStep;
            } else {
                if (Direction == BinSearchDirection.BINSEARCH_UP) flagGoneOver = true;
                if (flagGoneOver) CurrentStep /= 2;
                Direction = BinSearchDirection.BINSEARCH_DOWN;
                step = -CurrentStep;
            }
            cod_info.global_gain += step;
            if (cod_info.global_gain < 0) {
                cod_info.global_gain = 0;
                flagGoneOver = true;
            }
            if (cod_info.global_gain > 255) {
                cod_info.global_gain = 255;
                flagGoneOver = true;
            }
        }
        assert (cod_info.global_gain >= 0);
        assert (cod_info.global_gain < 256);
        while (nBits > desired_rate && cod_info.global_gain < 255) {
            cod_info.global_gain++;
            nBits = tk.count_bits(gfc, xrpow, cod_info, null);
        }
        gfc.CurrentStep[ch] = (start - cod_info.global_gain >= 4) ? 4 : 2;
        gfc.OldValue[ch] = cod_info.global_gain;
        cod_info.part2_3_length = nBits;
        return nBits;
    }

    public final void trancate_smallspectrums(final LameInternalFlags gfc, final GrInfo gi, final float[] l3_xmin, final float[] work) {
        float distort[] = new float[L3Side.SFBMAX];
        if ((0 == (gfc.substep_shaping & 4) && gi.block_type == Encoder.SHORT_TYPE) || (gfc.substep_shaping & 0x80) != 0) return;
        qupvt.calc_noise(gi, l3_xmin, distort, new CalcNoiseResult(), null);
        for (int j = 0; j < 576; j++) {
            float xr = 0.0f;
            if (gi.l3_enc[j] != 0) xr = Math.abs(gi.xr[j]);
            work[j] = xr;
        }
        int j = 0;
        int sfb = 8;
        if (gi.block_type == Encoder.SHORT_TYPE) sfb = 6;
        do {
            float allowedNoise, trancateThreshold;
            int nsame, start;
            int width = gi.width[sfb];
            j += width;
            if (distort[sfb] >= 1.0) continue;
            Arrays.sort(work, j - width, width);
            if (BitStream.EQ(work[j - 1], 0.0f)) continue;
            allowedNoise = (1.0f - distort[sfb]) * l3_xmin[sfb];
            trancateThreshold = 0.0f;
            start = 0;
            do {
                float noise;
                for (nsame = 1; start + nsame < width; nsame++) if (BitStream.NEQ(work[start + j - width], work[start + j + nsame - width])) break;
                noise = work[start + j - width] * work[start + j - width] * nsame;
                if (allowedNoise < noise) {
                    if (start != 0) trancateThreshold = work[start + j - width - 1];
                    break;
                }
                allowedNoise -= noise;
                start += nsame;
            } while (start < width);
            if (BitStream.EQ(trancateThreshold, 0.0f)) continue;
            do {
                if (Math.abs(gi.xr[j - width]) <= trancateThreshold) gi.l3_enc[j - width] = 0;
            } while (--width > 0);
        } while (++sfb < gi.psymax);
        gi.part2_3_length = tk.noquant_count_bits(gfc, gi, null);
    }

    /**
	 * author/date??
	 * 
	 * Function: Returns zero if there is a scalefac which has not been
	 * amplified. Otherwise it returns one.
	 */
    private boolean loop_break(final GrInfo cod_info) {
        for (int sfb = 0; sfb < cod_info.sfbmax; sfb++) if (cod_info.scalefac[sfb] + cod_info.subblock_gain[cod_info.window[sfb]] == 0) return false;
        return true;
    }

    private double penalties(final double noise) {
        return Util.FAST_LOG10((float) (0.368 + 0.632 * noise * noise * noise));
    }

    /**
	 * author/date??
	 * 
	 * several different codes to decide which quantization is better
	 */
    private double get_klemm_noise(final float[] distort, final GrInfo gi) {
        double klemm_noise = 1E-37;
        for (int sfb = 0; sfb < gi.psymax; sfb++) klemm_noise += penalties(distort[sfb]);
        return Math.max(1e-20, klemm_noise);
    }

    private boolean quant_compare(final int quant_comp, final CalcNoiseResult best, final CalcNoiseResult calc, final GrInfo gi, final float[] distort) {
        boolean better;
        switch(quant_comp) {
            default:
            case 9:
                {
                    if (best.over_count > 0) {
                        better = calc.over_SSD <= best.over_SSD;
                        if (calc.over_SSD == best.over_SSD) better = calc.bits < best.bits;
                    } else {
                        better = ((calc.max_noise < 0) && ((calc.max_noise * 10 + calc.bits) <= (best.max_noise * 10 + best.bits)));
                    }
                    break;
                }
            case 0:
                better = calc.over_count < best.over_count || (calc.over_count == best.over_count && calc.over_noise < best.over_noise) || (calc.over_count == best.over_count && BitStream.EQ(calc.over_noise, best.over_noise) && calc.tot_noise < best.tot_noise);
                break;
            case 8:
                calc.max_noise = (float) get_klemm_noise(distort, gi);
            case 1:
                better = calc.max_noise < best.max_noise;
                break;
            case 2:
                better = calc.tot_noise < best.tot_noise;
                break;
            case 3:
                better = (calc.tot_noise < best.tot_noise) && (calc.max_noise < best.max_noise);
                break;
            case 4:
                better = (calc.max_noise <= 0.0 && best.max_noise > 0.2) || (calc.max_noise <= 0.0 && best.max_noise < 0.0 && best.max_noise > calc.max_noise - 0.2 && calc.tot_noise < best.tot_noise) || (calc.max_noise <= 0.0 && best.max_noise > 0.0 && best.max_noise > calc.max_noise - 0.2 && calc.tot_noise < best.tot_noise + best.over_noise) || (calc.max_noise > 0.0 && best.max_noise > -0.05 && best.max_noise > calc.max_noise - 0.1 && calc.tot_noise + calc.over_noise < best.tot_noise + best.over_noise) || (calc.max_noise > 0.0 && best.max_noise > -0.1 && best.max_noise > calc.max_noise - 0.15 && calc.tot_noise + calc.over_noise + calc.over_noise < best.tot_noise + best.over_noise + best.over_noise);
                break;
            case 5:
                better = calc.over_noise < best.over_noise || (BitStream.EQ(calc.over_noise, best.over_noise) && calc.tot_noise < best.tot_noise);
                break;
            case 6:
                better = calc.over_noise < best.over_noise || (BitStream.EQ(calc.over_noise, best.over_noise) && (calc.max_noise < best.max_noise || (BitStream.EQ(calc.max_noise, best.max_noise) && calc.tot_noise <= best.tot_noise)));
                break;
            case 7:
                better = calc.over_count < best.over_count || calc.over_noise < best.over_noise;
                break;
        }
        if (best.over_count == 0) {
            better = better && calc.bits < best.bits;
        }
        return better;
    }

    /**
	 * author/date??
	 * 
	 * <PRE>
	 *  Amplify the scalefactor bands that violate the masking threshold.
	 *  See ISO 11172-3 Section C.1.5.4.3.5
	 * 
	 *  distort[] = noise/masking
	 *  distort[] > 1   ==> noise is not masked
	 *  distort[] < 1   ==> noise is masked
	 *  max_dist = maximum value of distort[]
	 * 
	 *  Three algorithms:
	 *  noise_shaping_amp
	 *        0             Amplify all bands with distort[]>1.
	 * 
	 *        1             Amplify all bands with distort[] >= max_dist^(.5);
	 *                     ( 50% in the db scale)
	 * 
	 *        2             Amplify first band with distort[] >= max_dist;
	 * 
	 * 
	 *  For algorithms 0 and 1, if max_dist < 1, then amplify all bands
	 *  with distort[] >= .95*max_dist.  This is to make sure we always
	 *  amplify at least one band.
	 * </PRE>
	 */
    private void amp_scalefac_bands(final LameGlobalFlags gfp, final GrInfo cod_info, final float[] distort, final float xrpow[], final boolean bRefine) {
        final LameInternalFlags gfc = gfp.internal_flags;
        float ifqstep34;
        if (cod_info.scalefac_scale == 0) {
            ifqstep34 = 1.29683955465100964055f;
        } else {
            ifqstep34 = 1.68179283050742922612f;
        }
        float trigger = 0;
        for (int sfb = 0; sfb < cod_info.sfbmax; sfb++) {
            if (trigger < distort[sfb]) trigger = distort[sfb];
        }
        int noise_shaping_amp = gfc.noise_shaping_amp;
        if (noise_shaping_amp == 3) {
            if (bRefine) noise_shaping_amp = 2; else noise_shaping_amp = 1;
        }
        switch(noise_shaping_amp) {
            case 2:
                break;
            case 1:
                if (trigger > 1.0) trigger = (float) Math.pow(trigger, .5); else trigger *= .95;
                break;
            case 0:
            default:
                if (trigger > 1.0) trigger = 1.0f; else trigger *= .95;
                break;
        }
        int j = 0;
        for (int sfb = 0; sfb < cod_info.sfbmax; sfb++) {
            final int width = cod_info.width[sfb];
            int l;
            j += width;
            if (distort[sfb] < trigger) continue;
            if ((gfc.substep_shaping & 2) != 0) {
                gfc.pseudohalf[sfb] = (0 == gfc.pseudohalf[sfb]) ? 1 : 0;
                if (0 == gfc.pseudohalf[sfb] && gfc.noise_shaping_amp == 2) return;
            }
            cod_info.scalefac[sfb]++;
            for (l = -width; l < 0; l++) {
                xrpow[j + l] *= ifqstep34;
                if (xrpow[j + l] > cod_info.xrpow_max) cod_info.xrpow_max = xrpow[j + l];
            }
            if (gfc.noise_shaping_amp == 2) return;
        }
    }

    /**
	 * Takehiro Tominaga 2000-xx-xx
	 * 
	 * turns on scalefac scale and adjusts scalefactors
	 */
    private void inc_scalefac_scale(final GrInfo cod_info, float xrpow[]) {
        final float ifqstep34 = 1.29683955465100964055f;
        int j = 0;
        for (int sfb = 0; sfb < cod_info.sfbmax; sfb++) {
            final int width = cod_info.width[sfb];
            int s = cod_info.scalefac[sfb];
            if (cod_info.preflag != 0) s += qupvt.pretab[sfb];
            j += width;
            if ((s & 1) != 0) {
                s++;
                for (int l = -width; l < 0; l++) {
                    xrpow[j + l] *= ifqstep34;
                    if (xrpow[j + l] > cod_info.xrpow_max) cod_info.xrpow_max = xrpow[j + l];
                }
            }
            cod_info.scalefac[sfb] = s >> 1;
        }
        cod_info.preflag = 0;
        cod_info.scalefac_scale = 1;
    }

    /**
	 * Takehiro Tominaga 2000-xx-xx
	 * 
	 * increases the subblock gain and adjusts scalefactors
	 */
    private boolean inc_subblock_gain(final LameInternalFlags gfc, final GrInfo cod_info, float xrpow[]) {
        int sfb;
        final int[] scalefac = cod_info.scalefac;
        for (sfb = 0; sfb < cod_info.sfb_lmax; sfb++) {
            if (scalefac[sfb] >= 16) return true;
        }
        for (int window = 0; window < 3; window++) {
            int s1 = 0;
            int s2 = 0;
            for (sfb = cod_info.sfb_lmax + window; sfb < cod_info.sfbdivide; sfb += 3) {
                if (s1 < scalefac[sfb]) s1 = scalefac[sfb];
            }
            for (; sfb < cod_info.sfbmax; sfb += 3) {
                if (s2 < scalefac[sfb]) s2 = scalefac[sfb];
            }
            if (s1 < 16 && s2 < 8) continue;
            if (cod_info.subblock_gain[window] >= 7) return true;
            cod_info.subblock_gain[window]++;
            int j = gfc.scalefac_band.l[cod_info.sfb_lmax];
            for (sfb = cod_info.sfb_lmax + window; sfb < cod_info.sfbmax; sfb += 3) {
                float amp;
                final int width = cod_info.width[sfb];
                int s = scalefac[sfb];
                assert (s >= 0);
                s = s - (4 >> cod_info.scalefac_scale);
                if (s >= 0) {
                    scalefac[sfb] = s;
                    j += width * 3;
                    continue;
                }
                scalefac[sfb] = 0;
                {
                    final int gain = 210 + (s << (cod_info.scalefac_scale + 1));
                    amp = qupvt.IPOW20(gain);
                }
                j += width * (window + 1);
                for (int l = -width; l < 0; l++) {
                    xrpow[j + l] *= amp;
                    if (xrpow[j + l] > cod_info.xrpow_max) cod_info.xrpow_max = xrpow[j + l];
                }
                j += width * (3 - window - 1);
            }
            {
                final float amp = qupvt.IPOW20(202);
                j += cod_info.width[sfb] * (window + 1);
                for (int l = -cod_info.width[sfb]; l < 0; l++) {
                    xrpow[j + l] *= amp;
                    if (xrpow[j + l] > cod_info.xrpow_max) cod_info.xrpow_max = xrpow[j + l];
                }
            }
        }
        return false;
    }

    /**
	 * <PRE>
	 *  Takehiro Tominaga /date??
	 *  Robert Hegemann 2000-09-06: made a function of it
	 * 
	 *  amplifies scalefactor bands,
	 *   - if all are already amplified returns 0
	 *   - if some bands are amplified too much:
	 *      * try to increase scalefac_scale
	 *      * if already scalefac_scale was set
	 *          try on short blocks to increase subblock gain
	 * </PRE>
	 */
    private boolean balance_noise(final LameGlobalFlags gfp, final GrInfo cod_info, final float[] distort, float xrpow[], final boolean bRefine) {
        final LameInternalFlags gfc = gfp.internal_flags;
        amp_scalefac_bands(gfp, cod_info, distort, xrpow, bRefine);
        boolean status = loop_break(cod_info);
        if (status) return false;
        if (gfc.mode_gr == 2) status = tk.scale_bitcount(cod_info); else status = tk.scale_bitcount_lsf(gfc, cod_info);
        if (!status) return true;
        if (gfc.noise_shaping > 1) {
            Arrays.fill(gfc.pseudohalf, 0);
            if (0 == cod_info.scalefac_scale) {
                inc_scalefac_scale(cod_info, xrpow);
                status = false;
            } else {
                if (cod_info.block_type == Encoder.SHORT_TYPE && gfc.subblock_gain > 0) {
                    status = (inc_subblock_gain(gfc, cod_info, xrpow) || loop_break(cod_info));
                }
            }
        }
        if (!status) {
            if (gfc.mode_gr == 2) status = tk.scale_bitcount(cod_info); else status = tk.scale_bitcount_lsf(gfc, cod_info);
        }
        return !status;
    }

    /**
	 * <PRE>
	 *  Function: The outer iteration loop controls the masking conditions
	 *  of all scalefactorbands. It computes the best scalefac and
	 *  global gain. This module calls the inner iteration loop
	 * 
	 *  mt 5/99 completely rewritten to allow for bit reservoir control,
	 *  mid/side channels with L/R or mid/side masking thresholds,
	 *  and chooses best quantization instead of last quantization when
	 *  no distortion free quantization can be found.
	 * 
	 *  added VBR support mt 5/99
	 * 
	 *  some code shuffle rh 9/00
	 * </PRE>
	 * 
	 * @param l3_xmin
	 *            allowed distortion
	 * @param xrpow
	 *            coloured magnitudes of spectral
	 * @param targ_bits
	 *            maximum allowed bits
	 */
    public final int outer_loop(final LameGlobalFlags gfp, final GrInfo cod_info, final float[] l3_xmin, float xrpow[], final int ch, final int targ_bits) {
        final LameInternalFlags gfc = gfp.internal_flags;
        GrInfo cod_info_w = new GrInfo();
        float save_xrpow[] = new float[576];
        float distort[] = new float[L3Side.SFBMAX];
        CalcNoiseResult best_noise_info = new CalcNoiseResult();
        int better;
        CalcNoiseData prev_noise = new CalcNoiseData();
        int best_part2_3_length = 9999999;
        boolean bEndOfSearch = false;
        boolean bRefine = false;
        int best_ggain_pass1 = 0;
        bin_search_StepSize(gfc, cod_info, targ_bits, ch, xrpow);
        if (0 == gfc.noise_shaping) return 100;
        qupvt.calc_noise(cod_info, l3_xmin, distort, best_noise_info, prev_noise);
        best_noise_info.bits = cod_info.part2_3_length;
        cod_info_w.assign(cod_info);
        int age = 0;
        System.arraycopy(xrpow, 0, save_xrpow, 0, 576);
        while (!bEndOfSearch) {
            do {
                CalcNoiseResult noise_info = new CalcNoiseResult();
                int search_limit;
                int maxggain = 255;
                if ((gfc.substep_shaping & 2) != 0) {
                    search_limit = 20;
                } else {
                    search_limit = 3;
                }
                if (gfc.sfb21_extra) {
                    if (distort[cod_info_w.sfbmax] > 1.0) break;
                    if (cod_info_w.block_type == Encoder.SHORT_TYPE && (distort[cod_info_w.sfbmax + 1] > 1.0 || distort[cod_info_w.sfbmax + 2] > 1.0)) break;
                }
                if (!balance_noise(gfp, cod_info_w, distort, xrpow, bRefine)) break;
                if (cod_info_w.scalefac_scale != 0) maxggain = 254;
                int huff_bits = targ_bits - cod_info_w.part2_length;
                if (huff_bits <= 0) break;
                while ((cod_info_w.part2_3_length = tk.count_bits(gfc, xrpow, cod_info_w, prev_noise)) > huff_bits && cod_info_w.global_gain <= maxggain) cod_info_w.global_gain++;
                if (cod_info_w.global_gain > maxggain) break;
                if (best_noise_info.over_count == 0) {
                    while ((cod_info_w.part2_3_length = tk.count_bits(gfc, xrpow, cod_info_w, prev_noise)) > best_part2_3_length && cod_info_w.global_gain <= maxggain) cod_info_w.global_gain++;
                    if (cod_info_w.global_gain > maxggain) break;
                }
                qupvt.calc_noise(cod_info_w, l3_xmin, distort, noise_info, prev_noise);
                noise_info.bits = cod_info_w.part2_3_length;
                if (cod_info.block_type != Encoder.SHORT_TYPE) {
                    better = gfp.quant_comp;
                } else better = gfp.quant_comp_short;
                better = quant_compare(better, best_noise_info, noise_info, cod_info_w, distort) ? 1 : 0;
                if (better != 0) {
                    best_part2_3_length = cod_info.part2_3_length;
                    best_noise_info = noise_info;
                    cod_info.assign(cod_info_w);
                    age = 0;
                    System.arraycopy(xrpow, 0, save_xrpow, 0, 576);
                } else {
                    if (gfc.full_outer_loop == 0) {
                        if (++age > search_limit && best_noise_info.over_count == 0) break;
                        if ((gfc.noise_shaping_amp == 3) && bRefine && age > 30) break;
                        if ((gfc.noise_shaping_amp == 3) && bRefine && (cod_info_w.global_gain - best_ggain_pass1) > 15) break;
                    }
                }
            } while ((cod_info_w.global_gain + cod_info_w.scalefac_scale) < 255);
            if (gfc.noise_shaping_amp == 3) {
                if (!bRefine) {
                    cod_info_w.assign(cod_info);
                    System.arraycopy(save_xrpow, 0, xrpow, 0, 576);
                    age = 0;
                    best_ggain_pass1 = cod_info_w.global_gain;
                    bRefine = true;
                } else {
                    bEndOfSearch = true;
                }
            } else {
                bEndOfSearch = true;
            }
        }
        assert ((cod_info.global_gain + cod_info.scalefac_scale) <= 255);
        if (gfp.VBR == VbrMode.vbr_rh || gfp.VBR == VbrMode.vbr_mtrh) System.arraycopy(save_xrpow, 0, xrpow, 0, 576); else if ((gfc.substep_shaping & 1) != 0) trancate_smallspectrums(gfc, cod_info, l3_xmin, xrpow);
        return best_noise_info.over_count;
    }

    /**
	 * Robert Hegemann 2000-09-06
	 * 
	 * update reservoir status after FINAL quantization/bitrate
	 */
    public final void iteration_finish_one(final LameInternalFlags gfc, final int gr, final int ch) {
        final IIISideInfo l3_side = gfc.l3_side;
        final GrInfo cod_info = l3_side.tt[gr][ch];
        tk.best_scalefac_store(gfc, gr, ch, l3_side);
        if (gfc.use_best_huffman == 1) tk.best_huffman_divide(gfc, cod_info);
        rv.ResvAdjust(gfc, cod_info);
    }

    /**
	 * 
	 * 2000-09-04 Robert Hegemann
	 * 
	 * @param l3_xmin
	 *            allowed distortion of the scalefactor
	 * @param xrpow
	 *            coloured magnitudes of spectral values
	 */
    public final void VBR_encode_granule(final LameGlobalFlags gfp, GrInfo cod_info, final float[] l3_xmin, float xrpow[], final int ch, int min_bits, int max_bits) {
        final LameInternalFlags gfc = gfp.internal_flags;
        GrInfo bst_cod_info = new GrInfo();
        float bst_xrpow[] = new float[576];
        final int Max_bits = max_bits;
        int real_bits = max_bits + 1;
        int this_bits = (max_bits + min_bits) / 2;
        int dbits, over, found = 0;
        final boolean sfb21_extra = gfc.sfb21_extra;
        assert (Max_bits <= LameInternalFlags.MAX_BITS_PER_CHANNEL);
        Arrays.fill(bst_cod_info.l3_enc, 0);
        do {
            assert (this_bits >= min_bits);
            assert (this_bits <= max_bits);
            assert (min_bits <= max_bits);
            if (this_bits > Max_bits - 42) gfc.sfb21_extra = false; else gfc.sfb21_extra = sfb21_extra;
            over = outer_loop(gfp, cod_info, l3_xmin, xrpow, ch, this_bits);
            if (over <= 0) {
                found = 1;
                real_bits = cod_info.part2_3_length;
                bst_cod_info.assign(cod_info);
                System.arraycopy(xrpow, 0, bst_xrpow, 0, 576);
                max_bits = real_bits - 32;
                dbits = max_bits - min_bits;
                this_bits = (max_bits + min_bits) / 2;
            } else {
                min_bits = this_bits + 32;
                dbits = max_bits - min_bits;
                this_bits = (max_bits + min_bits) / 2;
                if (found != 0) {
                    found = 2;
                    cod_info.assign(bst_cod_info);
                    System.arraycopy(bst_xrpow, 0, xrpow, 0, 576);
                }
            }
        } while (dbits > 12);
        gfc.sfb21_extra = sfb21_extra;
        if (found == 2) {
            System.arraycopy(bst_cod_info.l3_enc, 0, cod_info.l3_enc, 0, 576);
        }
        assert (cod_info.part2_3_length <= Max_bits);
    }

    /**
	 * Robert Hegemann 2000-09-05
	 * 
	 * calculates * how many bits are available for analog silent granules * how
	 * many bits to use for the lowest allowed bitrate * how many bits each
	 * bitrate would provide
	 */
    public final void get_framebits(final LameGlobalFlags gfp, final int frameBits[]) {
        final LameInternalFlags gfc = gfp.internal_flags;
        gfc.bitrate_index = gfc.VBR_min_bitrate;
        int bitsPerFrame = bs.getframebits(gfp);
        gfc.bitrate_index = 1;
        bitsPerFrame = bs.getframebits(gfp);
        for (int i = 1; i <= gfc.VBR_max_bitrate; i++) {
            gfc.bitrate_index = i;
            MeanBits mb = new MeanBits(bitsPerFrame);
            frameBits[i] = rv.ResvFrameBegin(gfp, mb);
            bitsPerFrame = mb.bits;
        }
    }

    /**
	 * <PRE>
	 *  2000-09-04 Robert Hegemann
	 * 
	 *  * converts LR to MS coding when necessary
	 *  * calculates allowed/adjusted quantization noise amounts
	 *  * detects analog silent frames
	 * 
	 *  some remarks:
	 *  - lower masking depending on Quality setting
	 *  - quality control together with adjusted ATH MDCT scaling
	 *    on lower quality setting allocate more noise from
	 *    ATH masking, and on higher quality setting allocate
	 *    less noise from ATH masking.
	 *  - experiments show that going more than 2dB over GPSYCHO's
	 *    limits ends up in very annoying artefacts
	 * </PRE>
	 */
    public final int VBR_old_prepare(final LameGlobalFlags gfp, float pe[][], final float ms_ener_ratio[], final III_psy_ratio ratio[][], float l3_xmin[][][], final int frameBits[], final int min_bits[][], final int max_bits[][], final int bands[][]) {
        final LameInternalFlags gfc = gfp.internal_flags;
        float masking_lower_db, adjust = 0.0f;
        int analog_silence = 1;
        int bits = 0;
        gfc.bitrate_index = gfc.VBR_max_bitrate;
        int avg = rv.ResvFrameBegin(gfp, new MeanBits(0)) / gfc.mode_gr;
        get_framebits(gfp, frameBits);
        for (int gr = 0; gr < gfc.mode_gr; gr++) {
            int mxb = qupvt.on_pe(gfp, pe, max_bits[gr], avg, gr, 0);
            if (gfc.mode_ext == Encoder.MPG_MD_MS_LR) {
                ms_convert(gfc.l3_side, gr);
                qupvt.reduce_side(max_bits[gr], ms_ener_ratio[gr], avg, mxb);
            }
            for (int ch = 0; ch < gfc.channels_out; ++ch) {
                final GrInfo cod_info = gfc.l3_side.tt[gr][ch];
                if (cod_info.block_type != Encoder.SHORT_TYPE) {
                    adjust = 1.28f / (1 + (float) Math.exp(3.5 - pe[gr][ch] / 300.)) - 0.05f;
                    masking_lower_db = gfc.PSY.mask_adjust - adjust;
                } else {
                    adjust = 2.56f / (1 + (float) Math.exp(3.5 - pe[gr][ch] / 300.)) - 0.14f;
                    masking_lower_db = gfc.PSY.mask_adjust_short - adjust;
                }
                gfc.masking_lower = (float) Math.pow(10.0, masking_lower_db * 0.1);
                init_outer_loop(gfc, cod_info);
                bands[gr][ch] = qupvt.calc_xmin(gfp, ratio[gr][ch], cod_info, l3_xmin[gr][ch]);
                if (bands[gr][ch] != 0) analog_silence = 0;
                min_bits[gr][ch] = 126;
                bits += max_bits[gr][ch];
            }
        }
        for (int gr = 0; gr < gfc.mode_gr; gr++) {
            for (int ch = 0; ch < gfc.channels_out; ch++) {
                if (bits > frameBits[gfc.VBR_max_bitrate]) {
                    max_bits[gr][ch] *= frameBits[gfc.VBR_max_bitrate];
                    max_bits[gr][ch] /= bits;
                }
                if (min_bits[gr][ch] > max_bits[gr][ch]) min_bits[gr][ch] = max_bits[gr][ch];
            }
        }
        return analog_silence;
    }

    public final void bitpressure_strategy(final LameInternalFlags gfc, float l3_xmin[][][], final int min_bits[][], final int max_bits[][]) {
        for (int gr = 0; gr < gfc.mode_gr; gr++) {
            for (int ch = 0; ch < gfc.channels_out; ch++) {
                final GrInfo gi = gfc.l3_side.tt[gr][ch];
                float[] pxmin = l3_xmin[gr][ch];
                int pxminPos = 0;
                for (int sfb = 0; sfb < gi.psy_lmax; sfb++) pxmin[pxminPos++] *= 1. + .029 * sfb * sfb / Encoder.SBMAX_l / Encoder.SBMAX_l;
                if (gi.block_type == Encoder.SHORT_TYPE) {
                    for (int sfb = gi.sfb_smin; sfb < Encoder.SBMAX_s; sfb++) {
                        pxmin[pxminPos++] *= 1. + .029 * sfb * sfb / Encoder.SBMAX_s / Encoder.SBMAX_s;
                        pxmin[pxminPos++] *= 1. + .029 * sfb * sfb / Encoder.SBMAX_s / Encoder.SBMAX_s;
                        pxmin[pxminPos++] *= 1. + .029 * sfb * sfb / Encoder.SBMAX_s / Encoder.SBMAX_s;
                    }
                }
                max_bits[gr][ch] = (int) Math.max(min_bits[gr][ch], 0.9 * max_bits[gr][ch]);
            }
        }
    }

    public final int VBR_new_prepare(final LameGlobalFlags gfp, float pe[][], III_psy_ratio ratio[][], float l3_xmin[][][], final int frameBits[], final int max_bits[][]) {
        final LameInternalFlags gfc = gfp.internal_flags;
        int analog_silence = 1;
        int avg = 0, bits = 0;
        int maximum_framebits;
        if (!gfp.free_format) {
            gfc.bitrate_index = gfc.VBR_max_bitrate;
            MeanBits mb = new MeanBits(avg);
            rv.ResvFrameBegin(gfp, mb);
            avg = mb.bits;
            get_framebits(gfp, frameBits);
            maximum_framebits = frameBits[gfc.VBR_max_bitrate];
        } else {
            gfc.bitrate_index = 0;
            MeanBits mb = new MeanBits(avg);
            maximum_framebits = rv.ResvFrameBegin(gfp, mb);
            avg = mb.bits;
            frameBits[0] = maximum_framebits;
        }
        for (int gr = 0; gr < gfc.mode_gr; gr++) {
            qupvt.on_pe(gfp, pe, max_bits[gr], avg, gr, 0);
            if (gfc.mode_ext == Encoder.MPG_MD_MS_LR) {
                ms_convert(gfc.l3_side, gr);
            }
            for (int ch = 0; ch < gfc.channels_out; ++ch) {
                final GrInfo cod_info = gfc.l3_side.tt[gr][ch];
                gfc.masking_lower = (float) Math.pow(10.0, gfc.PSY.mask_adjust * 0.1);
                init_outer_loop(gfc, cod_info);
                if (0 != qupvt.calc_xmin(gfp, ratio[gr][ch], cod_info, l3_xmin[gr][ch])) analog_silence = 0;
                bits += max_bits[gr][ch];
            }
        }
        for (int gr = 0; gr < gfc.mode_gr; gr++) {
            for (int ch = 0; ch < gfc.channels_out; ch++) {
                if (bits > maximum_framebits) {
                    max_bits[gr][ch] *= maximum_framebits;
                    max_bits[gr][ch] /= bits;
                }
            }
        }
        return analog_silence;
    }

    /**
	 * calculates target bits for ABR encoding
	 * 
	 * mt 2000/05/31
	 */
    public final void calc_target_bits(final LameGlobalFlags gfp, float pe[][], final float ms_ener_ratio[], final int targ_bits[][], final int[] analog_silence_bits, final int[] max_frame_bits) {
        final LameInternalFlags gfc = gfp.internal_flags;
        final IIISideInfo l3_side = gfc.l3_side;
        float res_factor;
        int gr, ch, totbits, mean_bits = 0;
        gfc.bitrate_index = gfc.VBR_max_bitrate;
        MeanBits mb = new MeanBits(mean_bits);
        max_frame_bits[0] = rv.ResvFrameBegin(gfp, mb);
        mean_bits = mb.bits;
        gfc.bitrate_index = 1;
        mean_bits = bs.getframebits(gfp) - gfc.sideinfo_len * 8;
        analog_silence_bits[0] = mean_bits / (gfc.mode_gr * gfc.channels_out);
        mean_bits = gfp.VBR_mean_bitrate_kbps * gfp.framesize * 1000;
        if ((gfc.substep_shaping & 1) != 0) mean_bits *= 1.09;
        mean_bits /= gfp.out_samplerate;
        mean_bits -= gfc.sideinfo_len * 8;
        mean_bits /= (gfc.mode_gr * gfc.channels_out);
        res_factor = .93f + .07f * (11.0f - gfp.compression_ratio) / (11.0f - 5.5f);
        if (res_factor < .90) res_factor = .90f;
        if (res_factor > 1.00) res_factor = 1.00f;
        for (gr = 0; gr < gfc.mode_gr; gr++) {
            int sum = 0;
            for (ch = 0; ch < gfc.channels_out; ch++) {
                targ_bits[gr][ch] = (int) (res_factor * mean_bits);
                if (pe[gr][ch] > 700) {
                    int add_bits = (int) ((pe[gr][ch] - 700) / 1.4);
                    final GrInfo cod_info = l3_side.tt[gr][ch];
                    targ_bits[gr][ch] = (int) (res_factor * mean_bits);
                    if (cod_info.block_type == Encoder.SHORT_TYPE) {
                        if (add_bits < mean_bits / 2) add_bits = mean_bits / 2;
                    }
                    if (add_bits > mean_bits * 3 / 2) add_bits = mean_bits * 3 / 2; else if (add_bits < 0) add_bits = 0;
                    targ_bits[gr][ch] += add_bits;
                }
                if (targ_bits[gr][ch] > LameInternalFlags.MAX_BITS_PER_CHANNEL) {
                    targ_bits[gr][ch] = LameInternalFlags.MAX_BITS_PER_CHANNEL;
                }
                sum += targ_bits[gr][ch];
            }
            if (sum > LameInternalFlags.MAX_BITS_PER_GRANULE) {
                for (ch = 0; ch < gfc.channels_out; ++ch) {
                    targ_bits[gr][ch] *= LameInternalFlags.MAX_BITS_PER_GRANULE;
                    targ_bits[gr][ch] /= sum;
                }
            }
        }
        if (gfc.mode_ext == Encoder.MPG_MD_MS_LR) for (gr = 0; gr < gfc.mode_gr; gr++) {
            qupvt.reduce_side(targ_bits[gr], ms_ener_ratio[gr], mean_bits * gfc.channels_out, LameInternalFlags.MAX_BITS_PER_GRANULE);
        }
        totbits = 0;
        for (gr = 0; gr < gfc.mode_gr; gr++) {
            for (ch = 0; ch < gfc.channels_out; ch++) {
                if (targ_bits[gr][ch] > LameInternalFlags.MAX_BITS_PER_CHANNEL) targ_bits[gr][ch] = LameInternalFlags.MAX_BITS_PER_CHANNEL;
                totbits += targ_bits[gr][ch];
            }
        }
        if (totbits > max_frame_bits[0]) {
            for (gr = 0; gr < gfc.mode_gr; gr++) {
                for (ch = 0; ch < gfc.channels_out; ch++) {
                    targ_bits[gr][ch] *= max_frame_bits[0];
                    targ_bits[gr][ch] /= totbits;
                }
            }
        }
    }
}
