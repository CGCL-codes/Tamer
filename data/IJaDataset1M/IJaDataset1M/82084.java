package resid;

import static resid.SID.ANTTI_LANKILA_PATCH;
import resid.ISIDDefs.chip_model;

/**
 * The SID filter is modeled with a two-integrator-loop biquadratic filter,
 * which has been confirmed by Bob Yannes to be the actual circuit used in the
 * SID chip.
 * <P>
 * Measurements show that excellent emulation of the SID filter is achieved,
 * except when high resonance is combined with high sustain levels. In this case
 * the SID op-amps are performing less than ideally and are causing some
 * peculiar behavior of the SID filter. This however seems to have more effect
 * on the overall amplitude than on the color of the sound.
 * <P>
 * The theory for the filter circuit can be found in "Microelectric Circuits" by
 * Adel S. Sedra and Kenneth C. Smith. The circuit is modeled based on the
 * explanation found there except that an additional inverter is used in the
 * feedback from the bandpass output, allowing the summer op-amp to operate in
 * single-ended mode. This yields inverted filter outputs with levels
 * independent of Q, which corresponds with the results obtained from a real
 * SID.
 * <P>
 * We have been able to model the summer and the two integrators of the circuit
 * to form components of an IIR filter. Vhp is the output of the summer, Vbp is
 * the output of the first integrator, and Vlp is the output of the second
 * integrator in the filter circuit.
 * <P>
 * According to Bob Yannes, the active stages of the SID filter are not really
 * op-amps. Rather, simple NMOS inverters are used. By biasing an inverter into
 * its region of quasi-linear operation using a feedback resistor from input to
 * output, a MOS inverter can be made to act like an op-amp for small signals
 * centered around the switching threshold.
 * <P>
 * Qualified guesses at SID filter schematics are depicted below.
 * 
 * <pre>
 * SID filter
 * ----------
 * 
 *     -----------------------------------------------
 *    |                                               |
 *    |            ---Rq--                            |
 *    |           |       |                           |
 *    |  ------------&lt;A]-----R1---------              |
 *    | |                               |             |
 *    | |                        ---C---|      ---C---|
 *    | |                       |       |     |       |
 *    |  --R1--    ---R1--      |---Rs--|     |---Rs--| 
 *    |        |  |       |     |       |     |       |
 *     ----R1--|-----[A&gt;--|--R-----[A&gt;--|--R-----[A&gt;--|
 *             |          |             |             |
 * vi -----R1--           |             |             |
 * 
 *                       vhp           vbp           vlp
 * 
 * 
 * vi  - input voltage
 * vhp - highpass output
 * vbp - bandpass output
 * vlp - lowpass output
 * [A&gt; - op-amp
 * R1  - summer resistor
 * Rq  - resistor array controlling resonance (4 resistors)
 * R   - NMOS FET voltage controlled resistor controlling cutoff frequency
 * Rs  - shunt resitor
 * C   - capacitor
 * 
 * 
 * 
 * SID integrator
 * --------------
 * 
 *                                   V+
 * 
 *                                   |
 *                                   |
 *                              -----|
 *                             |     |
 *                             | ||--
 *                              -||
 *                   ---C---     ||-&gt;
 *                  |       |        |
 *                  |---Rs-----------|---- vo
 *                  |                |
 *                  |            ||--
 * vi ----     -----|------------||
 *        |   &circ;     |            ||-&gt;
 *        |___|     |                |
 *        -----     |                |
 *          |       |                |
 *          |---R2--                 |
 *          |
 *          R1                       V-
 *          |
 *          |
 * 
 *          Vw
 * ----------------------------------------------------------------------------
 * </pre>
 * 
 * @author Ken H�ndel
 */
public class Filter {

    /**
	 * #define SPLINE_BRUTE_FORCE false
	 */
    public static final boolean SPLINE_BRUTE_FORCE = false;

    /**
	 * Filter enabled.
	 */
    protected boolean enabled;

    /**
	 * Filter cutoff frequency.
	 */
    protected int fc;

    /**
	 * Filter resonance.
	 */
    protected int res;

    /**
	 * Selects which inputs to route through filter.
	 */
    protected int filt;

    /**
	 * Switch voice 3 off.
	 */
    protected int voice3off;

    /**
	 * Highpass, bandpass, and lowpass filter modes.
	 */
    protected int hp_bp_lp;

    /**
	 * Output master volume.
	 */
    protected int vol;

    /**
	 * Mixer DC offset.
	 */
    protected int mixer_DC;

    /**
	 * State of filter. highpass
	 */
    protected int Vhp;

    /**
	 * State of filter. bandpass
	 */
    protected int Vbp;

    /**
	 * State of filter. lowpass
	 */
    protected int Vlp;

    /**
	 * State of filter. not filtered
	 */
    protected int Vnf;

    /**
	 * when to begin, how fast it grows
	 */
    int DLthreshold, DLsteepness;

    int DHthreshold, DHsteepness;

    int DLlp, DLbp, DLhp;

    int DHlp, DHbp, DHhp;

    /**
	 * Cutoff frequency, resonance.
	 */
    protected int w0, w0_ceil_1, w0_ceil_dt;

    protected int _1024_div_Q;

    /**
	 * Cutoff frequency tables. FC is an 11 bit register.
	 */
    protected int f0_6581[] = new int[2048];

    /**
	 * Cutoff frequency tables. FC is an 11 bit register.
	 */
    protected int f0_8580[] = new int[2048];

    protected int f0[];

    /**
	 * 
	 * Maximum cutoff frequency is specified as FCmax = 2.6e-5/C =
	 * 2.6e-5/2200e-12 = 11818.
	 * <P>
	 * Measurements indicate a cutoff frequency range of approximately 220Hz -
	 * 18kHz on a MOS6581 fitted with 470pF capacitors. The function mapping FC
	 * to cutoff frequency has the shape of the tanh function, with a
	 * discontinuity at FCHI = 0x80. In contrast, the MOS8580 almost perfectly
	 * corresponds with the specification of a linear mapping from 30Hz to
	 * 12kHz.
	 * <P>
	 * The mappings have been measured by feeding the SID with an external
	 * signal since the chip itself is incapable of generating waveforms of
	 * higher fundamental frequency than 4kHz. It is best to use the bandpass
	 * output at full resonance to pick out the cutoff frequency at any given FC
	 * setting.
	 * <P>
	 * The mapping function is specified with spline interpolation points and
	 * the function values are retrieved via table lookup.
	 * <P>
	 * NB! Cutoff frequency characteristics may vary, we have modeled two
	 * particular Commodore 64s.
	 */
    protected static int[] f0_points_6581[] = { { 0, 220 }, { 0, 220 }, { 128, 230 }, { 256, 250 }, { 384, 300 }, { 512, 420 }, { 640, 780 }, { 768, 1600 }, { 832, 2300 }, { 896, 3200 }, { 960, 4300 }, { 992, 5000 }, { 1008, 5400 }, { 1016, 5700 }, { 1023, 6000 }, { 1023, 6000 }, { 1024, 4600 }, { 1024, 4600 }, { 1032, 4800 }, { 1056, 5300 }, { 1088, 6000 }, { 1120, 6600 }, { 1152, 7200 }, { 1280, 9500 }, { 1408, 12000 }, { 1536, 14500 }, { 1664, 16000 }, { 1792, 17100 }, { 1920, 17700 }, { 2047, 18000 }, { 2047, 18000 } };

    /**
	 * 
	 * Maximum cutoff frequency is specified as FCmax = 2.6e-5/C =
	 * 2.6e-5/2200e-12 = 11818.
	 * 
	 * Measurements indicate a cutoff frequency range of approximately 220Hz -
	 * 18kHz on a MOS6581 fitted with 470pF capacitors. The function mapping FC
	 * to cutoff frequency has the shape of the tanh function, with a
	 * discontinuity at FCHI = 0x80. In contrast, the MOS8580 almost perfectly
	 * corresponds with the specification of a linear mapping from 30Hz to
	 * 12kHz.
	 * 
	 * The mappings have been measured by feeding the SID with an external
	 * signal since the chip itself is incapable of generating waveforms of
	 * higher fundamental frequency than 4kHz. It is best to use the bandpass
	 * output at full resonance to pick out the cutoff frequency at any given FC
	 * setting.
	 * 
	 * The mapping function is specified with spline interpolation points and
	 * the function values are retrieved via table lookup.
	 * 
	 * NB! Cutoff frequency characteristics may vary, we have modeled two
	 * particular Commodore 64s.
	 */
    protected static int[] f0_points_8580[] = { { 0, 0 }, { 0, 0 }, { 128, 800 }, { 256, 1600 }, { 384, 2500 }, { 512, 3300 }, { 640, 4100 }, { 768, 4800 }, { 896, 5600 }, { 1024, 6500 }, { 1152, 7500 }, { 1280, 8400 }, { 1408, 9200 }, { 1536, 9800 }, { 1664, 10500 }, { 1792, 11000 }, { 1920, 11700 }, { 2047, 12500 }, { 2047, 12500 } };

    protected int[] f0_points[];

    protected int f0_count;

    /**
	 * SID clocking - 1 cycle
	 * 
	 * @param voice1
	 * @param voice2
	 * @param voice3
	 * @param ext_in
	 */
    public void clock(int voice1, int voice2, int voice3, int ext_in) {
        voice1 >>= 7;
        voice2 >>= 7;
        if ((voice3off != 0) && ((filt & 0x04) == 0)) {
            voice3 = 0;
        } else {
            voice3 >>= 7;
        }
        ext_in >>= 7;
        if (!enabled) {
            Vnf = voice1 + voice2 + voice3 + ext_in;
            Vhp = Vbp = Vlp = 0;
            return;
        }
        int Vi = Vnf = 0;
        if (ANTTI_LANKILA_PATCH) {
            if ((filt & 1) != 0) Vi += voice1; else Vnf += voice1;
            if ((filt & 2) != 0) Vi += voice2; else Vnf += voice2;
            if ((filt & 4) != 0) Vi += voice3; else Vnf += voice3;
            if ((filt & 8) != 0) Vi += ext_in; else Vnf += ext_in;
        } else {
            switch(filt) {
                default:
                case 0x0:
                    Vi = 0;
                    Vnf = voice1 + voice2 + voice3 + ext_in;
                    break;
                case 0x1:
                    Vi = voice1;
                    Vnf = voice2 + voice3 + ext_in;
                    break;
                case 0x2:
                    Vi = voice2;
                    Vnf = voice1 + voice3 + ext_in;
                    break;
                case 0x3:
                    Vi = voice1 + voice2;
                    Vnf = voice3 + ext_in;
                    break;
                case 0x4:
                    Vi = voice3;
                    Vnf = voice1 + voice2 + ext_in;
                    break;
                case 0x5:
                    Vi = voice1 + voice3;
                    Vnf = voice2 + ext_in;
                    break;
                case 0x6:
                    Vi = voice2 + voice3;
                    Vnf = voice1 + ext_in;
                    break;
                case 0x7:
                    Vi = voice1 + voice2 + voice3;
                    Vnf = ext_in;
                    break;
                case 0x8:
                    Vi = ext_in;
                    Vnf = voice1 + voice2 + voice3;
                    break;
                case 0x9:
                    Vi = voice1 + ext_in;
                    Vnf = voice2 + voice3;
                    break;
                case 0xa:
                    Vi = voice2 + ext_in;
                    Vnf = voice1 + voice3;
                    break;
                case 0xb:
                    Vi = voice1 + voice2 + ext_in;
                    Vnf = voice3;
                    break;
                case 0xc:
                    Vi = voice3 + ext_in;
                    Vnf = voice1 + voice2;
                    break;
                case 0xd:
                    Vi = voice1 + voice3 + ext_in;
                    Vnf = voice2;
                    break;
                case 0xe:
                    Vi = voice2 + voice3 + ext_in;
                    Vnf = voice1;
                    break;
                case 0xf:
                    Vi = voice1 + voice2 + voice3 + ext_in;
                    Vnf = 0;
                    break;
            }
        }
        if (ANTTI_LANKILA_PATCH) {
            int Vi_peak_bp = ((Vlp * DHlp + Vbp * DHbp + Vhp * DHhp) >> 8) + Vi;
            if (Vi_peak_bp < DHthreshold) Vi_peak_bp = DHthreshold;
            int Vi_peak_lp = ((Vlp * DLlp + Vbp * DLbp + Vhp * DLhp) >> 8) + Vi;
            if (Vi_peak_lp < DLthreshold) Vi_peak_lp = DLthreshold;
            int w0_eff_bp = w0 + w0 * ((Vi_peak_bp - DHthreshold) >> 4) / DHsteepness;
            int w0_eff_lp = w0 + w0 * ((Vi_peak_lp - DLthreshold) >> 4) / DLsteepness;
            if (w0_eff_bp > w0_ceil_1) w0_eff_bp = w0_ceil_1;
            if (w0_eff_lp > w0_ceil_1) w0_eff_lp = w0_ceil_1;
            Vhp = (Vbp * _1024_div_Q >> 10) - Vlp - Vi;
            Vlp -= w0_eff_lp * Vbp >> 20;
            Vbp -= w0_eff_bp * Vhp >> 20;
        } else {
            int dVbp = (w0_ceil_1 * Vhp >> 20);
            int dVlp = (w0_ceil_1 * Vbp >> 20);
            Vbp -= dVbp;
            Vlp -= dVlp;
            Vhp = (Vbp * _1024_div_Q >> 10) - Vlp - Vi;
        }
    }

    /**
	 * SID clocking - delta_t cycles.
	 * 
	 * @param delta_t
	 * @param voice1
	 * @param voice2
	 * @param voice3
	 * @param ext_in
	 */
    public void clock(int delta_t, int voice1, int voice2, int voice3, int ext_in) {
        voice1 >>= 7;
        voice2 >>= 7;
        if ((voice3off != 0) && ((filt & 0x04) == 0)) {
            voice3 = 0;
        } else {
            voice3 >>= 7;
        }
        ext_in >>= 7;
        if (!enabled) {
            Vnf = voice1 + voice2 + voice3 + ext_in;
            Vhp = Vbp = Vlp = 0;
            return;
        }
        int Vi = Vnf = 0;
        if (!ANTTI_LANKILA_PATCH) {
            switch(filt) {
                default:
                case 0x0:
                    Vi = 0;
                    Vnf = voice1 + voice2 + voice3 + ext_in;
                    break;
                case 0x1:
                    Vi = voice1;
                    Vnf = voice2 + voice3 + ext_in;
                    break;
                case 0x2:
                    Vi = voice2;
                    Vnf = voice1 + voice3 + ext_in;
                    break;
                case 0x3:
                    Vi = voice1 + voice2;
                    Vnf = voice3 + ext_in;
                    break;
                case 0x4:
                    Vi = voice3;
                    Vnf = voice1 + voice2 + ext_in;
                    break;
                case 0x5:
                    Vi = voice1 + voice3;
                    Vnf = voice2 + ext_in;
                    break;
                case 0x6:
                    Vi = voice2 + voice3;
                    Vnf = voice1 + ext_in;
                    break;
                case 0x7:
                    Vi = voice1 + voice2 + voice3;
                    Vnf = ext_in;
                    break;
                case 0x8:
                    Vi = ext_in;
                    Vnf = voice1 + voice2 + voice3;
                    break;
                case 0x9:
                    Vi = voice1 + ext_in;
                    Vnf = voice2 + voice3;
                    break;
                case 0xa:
                    Vi = voice2 + ext_in;
                    Vnf = voice1 + voice3;
                    break;
                case 0xb:
                    Vi = voice1 + voice2 + ext_in;
                    Vnf = voice3;
                    break;
                case 0xc:
                    Vi = voice3 + ext_in;
                    Vnf = voice1 + voice2;
                    break;
                case 0xd:
                    Vi = voice1 + voice3 + ext_in;
                    Vnf = voice2;
                    break;
                case 0xe:
                    Vi = voice2 + voice3 + ext_in;
                    Vnf = voice1;
                    break;
                case 0xf:
                    Vi = voice1 + voice2 + voice3 + ext_in;
                    Vnf = 0;
                    break;
            }
        } else {
            if ((filt & 1) != 0) Vi += voice1; else Vnf += voice1;
            if ((filt & 2) != 0) Vi += voice2; else Vnf += voice2;
            if ((filt & 4) != 0) Vi += voice3; else Vnf += voice3;
            if ((filt & 8) != 0) Vi += ext_in; else Vnf += ext_in;
        }
        int delta_t_flt = 8;
        while (delta_t != 0) {
            if (delta_t < delta_t_flt) {
                delta_t_flt = delta_t;
            }
            int w0_delta_t = w0_ceil_dt * delta_t_flt >> 6;
            int dVbp = (w0_delta_t * Vhp >> 14);
            int dVlp = (w0_delta_t * Vbp >> 14);
            Vbp -= dVbp;
            Vlp -= dVlp;
            Vhp = (Vbp * _1024_div_Q >> 10) - Vlp - Vi;
            delta_t -= delta_t_flt;
        }
    }

    /**
	 * SID audio output (16 bits). SID audio output (20 bits).
	 * 
	 * @return
	 */
    public int output() {
        if (!enabled) {
            return (Vnf + mixer_DC) * (vol);
        }
        if (!ANTTI_LANKILA_PATCH) {
            int Vf;
            switch(hp_bp_lp) {
                default:
                case 0x0:
                    Vf = 0;
                    break;
                case 0x1:
                    Vf = Vlp;
                    break;
                case 0x2:
                    Vf = Vbp;
                    break;
                case 0x3:
                    Vf = Vlp + Vbp;
                    break;
                case 0x4:
                    Vf = Vhp;
                    break;
                case 0x5:
                    Vf = Vlp + Vhp;
                    break;
                case 0x6:
                    Vf = Vbp + Vhp;
                    break;
                case 0x7:
                    Vf = Vlp + Vbp + Vhp;
                    break;
            }
            return (Vnf + Vf + mixer_DC) * (vol);
        } else {
            int Vf = 0;
            if ((hp_bp_lp & 1) != 0) Vf += Vlp;
            if ((hp_bp_lp & 2) != 0) Vf += Vbp;
            if ((hp_bp_lp & 4) != 0) Vf += Vhp;
            return (Vnf + Vf + mixer_DC) * (vol);
        }
    }

    /**
	 * Constructor.
	 */
    public Filter() {
        fc = 0;
        res = 0;
        filt = 0;
        voice3off = 0;
        hp_bp_lp = 0;
        vol = 0;
        Vhp = 0;
        Vbp = 0;
        Vlp = 0;
        Vnf = 0;
        enable_filter(true);
        interpolate(f0_points_6581, 0, f0_points_6581.length - 1, new PointPlotter(f0_6581), 1.0);
        interpolate(f0_points_8580, 0, f0_points_8580.length - 1, new PointPlotter(f0_8580), 1.0);
        set_chip_model(chip_model.MOS6581);
        set_distortion_properties(999999, 999999, 0, 0, 0, 999999, 999999, 0, 0, 0);
    }

    /**
	 * Enable filter.
	 * 
	 * @param enable
	 */
    public void enable_filter(boolean enable) {
        enabled = enable;
    }

    /**
	 * Set chip model.
	 * 
	 * @param model
	 */
    public void set_chip_model(chip_model model) {
        if (model == chip_model.MOS6581) {
            mixer_DC = -0xfff * 0xff / 18 >> 7;
            f0 = f0_6581;
            f0_points = f0_points_6581;
            f0_count = f0_points_6581.length;
        } else {
            mixer_DC = 0;
            f0 = f0_8580;
            f0_points = f0_points_8580;
            f0_count = f0_points_8580.length;
        }
        set_w0();
        set_Q();
    }

    void set_distortion_properties(int Lthreshold, int Lsteepness, int Llp, int Lbp, int Lhp, int Hthreshold, int Hsteepness, int Hlp, int Hbp, int Hhp) {
        DLthreshold = Lthreshold;
        if (Lsteepness < 16) Lsteepness = 16;
        DLsteepness = Lsteepness >> 4;
        DLlp = Llp;
        DLbp = Lbp;
        DLhp = Lhp;
        DHthreshold = Hthreshold;
        if (Hsteepness < 16) Hsteepness = 16;
        DHsteepness = Hsteepness >> 4;
        DHlp = Hlp;
        DHbp = Hbp;
        DHhp = Hhp;
    }

    /**
	 * SID reset.
	 */
    public void reset() {
        fc = 0;
        res = 0;
        filt = 0;
        voice3off = 0;
        hp_bp_lp = 0;
        vol = 0;
        Vhp = 0;
        Vbp = 0;
        Vlp = 0;
        Vnf = 0;
        set_w0();
        set_Q();
    }

    /**
	 * Register functions.
	 * 
	 * @param fc_lo
	 */
    public void writeFC_LO(int fc_lo) {
        fc = fc & 0x7f8 | fc_lo & 0x007;
        set_w0();
    }

    /**
	 * Register functions.
	 * 
	 * @param fc_hi
	 */
    public void writeFC_HI(int fc_hi) {
        fc = (fc_hi << 3) & 0x7f8 | fc & 0x007;
        set_w0();
    }

    /**
	 * Register functions.
	 * 
	 * @param res_filt
	 */
    public void writeRES_FILT(int res_filt) {
        res = (res_filt >> 4) & 0x0f;
        set_Q();
        filt = res_filt & 0x0f;
    }

    /**
	 * Register functions.
	 * 
	 * @param mode_vol
	 */
    public void writeMODE_VOL(int mode_vol) {
        voice3off = mode_vol & 0x80;
        hp_bp_lp = (mode_vol >> 4) & 0x07;
        vol = mode_vol & 0x0f;
    }

    protected void set_w0() {
        final double pi = 3.1415926535897932385;
        w0 = (int) (2 * pi * f0[fc] * 1.048576);
        if (ANTTI_LANKILA_PATCH) {
            w0_ceil_1 = (int) (2 * pi * 18000 * 1.048576);
        } else {
            final int w0_max_1 = (int) (2 * pi * 16000 * 1.048576);
            w0_ceil_1 = w0 <= w0_max_1 ? w0 : w0_max_1;
        }
        final int w0_max_dt = (int) (2 * pi * 4000 * 1.048576);
        w0_ceil_dt = w0 <= w0_max_dt ? w0 : w0_max_dt;
    }

    /**
	 * Set filter resonance.
	 */
    protected void set_Q() {
        _1024_div_Q = (int) (1024.0 / (0.707 + 1.0 * res / 0x0f));
    }

    /**
	 * Return the array of spline interpolation points used to map the FC
	 * register to filter cutoff frequency.
	 * 
	 * @param fcp
	 *            IN/OUT parameter points and count
	 */
    public void fc_default(SID.FCPoints fcp) {
        fcp.points = f0_points;
        fcp.count = f0_count;
    }

    public PointPlotter fc_plotter() {
        return new PointPlotter(f0);
    }

    public class Coefficients {

        public double a;

        public double b;

        public double c;

        public double d;
    }

    /**
	 * Calculation of coefficients.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param k1
	 * @param k2
	 * @param coeff
	 */
    protected void cubic_coefficients(double x1, double y1, double x2, double y2, double k1, double k2, Coefficients coeff) {
        double dx = x2 - x1, dy = y2 - y1;
        coeff.a = ((k1 + k2) - 2 * dy / dx) / (dx * dx);
        coeff.b = ((k2 - k1) / dx - 3 * (x1 + x2) * coeff.a) / 2;
        coeff.c = k1 - (3 * x1 * coeff.a + 2 * coeff.b) * x1;
        coeff.d = y1 - ((x1 * coeff.a + coeff.b) * x1 + coeff.c) * x1;
    }

    /**
	 * Evaluation of cubic polynomial by brute force.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param k1
	 * @param k2
	 * @param plotter
	 * @param res
	 */
    protected void interpolate_brute_force(double x1, double y1, double x2, double y2, double k1, double k2, PointPlotter plotter, double res) {
        Coefficients coeff = new Coefficients();
        cubic_coefficients(x1, y1, x2, y2, k1, k2, coeff);
        for (double x = x1; x <= x2; x += res) {
            double y = ((coeff.a * x + coeff.b) * x + coeff.c) * x + coeff.d;
            plotter.plot(x, y);
        }
    }

    /**
	 * Evaluation of cubic polynomial by forward differencing.
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param k1
	 * @param k2
	 * @param plotter
	 * @param res
	 */
    protected void interpolate_forward_difference(double x1, double y1, double x2, double y2, double k1, double k2, PointPlotter plotter, double res) {
        Coefficients coeff = new Coefficients();
        cubic_coefficients(x1, y1, x2, y2, k1, k2, coeff);
        double y = ((coeff.a * x1 + coeff.b) * x1 + coeff.c) * x1 + coeff.d;
        double dy = (3 * coeff.a * (x1 + res) + 2 * coeff.b) * x1 * res + ((coeff.a * res + coeff.b) * res + coeff.c) * res;
        double d2y = (6 * coeff.a * (x1 + res) + 2 * coeff.b) * res * res;
        double d3y = 6 * coeff.a * res * res * res;
        for (double x = x1; x <= x2; x += res) {
            plotter.plot(x, y);
            y += dy;
            dy += d2y;
            d2y += d3y;
        }
    }

    protected double x(int[] f0_base[], int p) {
        return (f0_base[p])[0];
    }

    protected double y(int[] f0_base[], int p) {
        return (f0_base[p])[1];
    }

    /**
	 * Evaluation of complete interpolating function. Note that since each curve
	 * segment is controlled by four points, the end points will not be
	 * interpolated. If extra control points are not desirable, the end points
	 * can simply be repeated to ensure interpolation. Note also that points of
	 * non-differentiability and discontinuity can be introduced by repeating
	 * points.
	 * 
	 * @param p0
	 * @param pn
	 * @param plotter
	 * @param res
	 */
    public void interpolate(int[] f0_base[], int p0, int pn, PointPlotter plotter, double res) {
        double k1, k2;
        int p1 = p0;
        ++p1;
        int p2 = p1;
        ++p2;
        int p3 = p2;
        ++p3;
        for (; p2 != pn; ++p0, ++p1, ++p2, ++p3) {
            if (x(f0_base, p1) == x(f0_base, p2)) {
                continue;
            }
            if (x(f0_base, p0) == x(f0_base, p1) && x(f0_base, p2) == x(f0_base, p3)) {
                k1 = k2 = (y(f0_base, p2) - y(f0_base, p1)) / (x(f0_base, p2) - x(f0_base, p1));
            } else if (x(f0_base, p0) == x(f0_base, p1)) {
                k2 = (y(f0_base, p3) - y(f0_base, p1)) / (x(f0_base, p3) - x(f0_base, p1));
                k1 = (3 * (y(f0_base, p2) - y(f0_base, p1)) / (x(f0_base, p2) - x(f0_base, p1)) - k2) / 2;
            } else if (x(f0_base, p2) == x(f0_base, p3)) {
                k1 = (y(f0_base, p2) - y(f0_base, p0)) / (x(f0_base, p2) - x(f0_base, p0));
                k2 = (3 * (y(f0_base, p2) - y(f0_base, p1)) / (x(f0_base, p2) - x(f0_base, p1)) - k1) / 2;
            } else {
                k1 = (y(f0_base, p2) - y(f0_base, p0)) / (x(f0_base, p2) - x(f0_base, p0));
                k2 = (y(f0_base, p3) - y(f0_base, p1)) / (x(f0_base, p3) - x(f0_base, p1));
            }
            if (SPLINE_BRUTE_FORCE) {
                interpolate_brute_force(x(f0_base, p1), y(f0_base, p1), x(f0_base, p2), y(f0_base, p2), k1, k2, plotter, res);
            } else {
                interpolate_forward_difference(x(f0_base, p1), y(f0_base, p1), x(f0_base, p2), y(f0_base, p2), k1, k2, plotter, res);
            }
        }
    }
}
