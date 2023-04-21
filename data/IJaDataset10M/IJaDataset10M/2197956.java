package de.crysandt.audio.mpeg7audio;

import de.crysandt.audio.mpeg7audio.msgs.*;
import de.crysandt.math.Function;

/**
 * This code has been verified against the Matlab-XM reference implementation
 *  dated 08-2003.<p/>
 *
 * @author <a href="mailto:crysandt@ient.rwth-aachen.de">Holger Crysandt</a>
 */
class AudioSpectrumEnvelope extends MsgSpeaker implements MsgListener {

    private static final float HAMMING_ENERGIE = 0.3974f;

    static final int NORMALIZE_OFF = 0;

    static final int NORMALIZE_NORM2 = 1;

    static final int NORMALIZE_POWER = 2;

    private final float RESOLUTION;

    private final float LO_EDGE;

    private final float HI_EDGE;

    private final boolean DB_SCALE;

    private final int normalize;

    public AudioSpectrumEnvelope(float resolution, float lo_edge, float hi_edge, boolean db_scale, String normalize) {
        this.RESOLUTION = resolution;
        this.LO_EDGE = lo_edge;
        this.HI_EDGE = hi_edge;
        this.DB_SCALE = db_scale;
        if ("off".equals(normalize)) this.normalize = NORMALIZE_OFF; else if ("norm2".equals(normalize)) this.normalize = NORMALIZE_NORM2; else if ("power".equals(normalize)) this.normalize = NORMALIZE_POWER; else throw new IllegalArgumentException("Parameter nomalize must be \"off\", \"norm2\" or \"power\". " + "( is: " + normalize + ")");
    }

    public void receivedMsg(Msg msg) {
        MsgAudioSpectrum m = (MsgAudioSpectrum) msg;
        double band_factor = Math.pow(2.0, RESOLUTION);
        float DF = m.deltaF;
        float[] spectrum = m.getAudioSpectrum();
        float scal = m.lengthFFT * m.lengthWindow * HAMMING_ENERGIE;
        for (int i = 0; i < spectrum.length; ++i) spectrum[i] /= scal;
        int num = 2 + (int) (Math.floor(0.5 + Function.log2(HI_EDGE / LO_EDGE)) / RESOLUTION);
        float[] envelope = new float[num];
        float freq = 0;
        int i = 0;
        int index = 0;
        float band_edge_lo = -Float.MAX_VALUE;
        float band_edge_hi = LO_EDGE;
        while (i < spectrum.length) {
            for (; (freq < band_edge_hi) && (i < spectrum.length); ++i, freq += DF) {
                float alpha_lo = Math.max(0.0f, 0.5f - (freq - band_edge_lo) / DF);
                float alpha_hi = Math.max(0.0f, 0.5f - (band_edge_hi - freq) / DF);
                if ((alpha_lo != 0.0f) || (alpha_hi != 0.0f)) {
                    if (alpha_lo > 0.0) envelope[index - 1] += spectrum[i] * alpha_lo;
                    if (alpha_hi > 0.0) envelope[index + 1] += spectrum[i] * alpha_hi;
                    envelope[index] += spectrum[i] * (1 - alpha_lo - alpha_hi);
                } else {
                    envelope[index] += spectrum[i];
                }
            }
            if (index < envelope.length - 1) ++index;
            if (index < envelope.length - 1) {
                band_edge_lo = band_edge_hi;
                band_edge_hi *= band_factor;
            } else {
                band_edge_lo = HI_EDGE;
                band_edge_hi = Float.MAX_VALUE;
            }
        }
        if (this.normalize == NORMALIZE_POWER) {
            float power = Function.sum(envelope);
            for (int n = 0; n < envelope.length; ++n) envelope[n] /= power;
            envelope[0] = power;
        }
        if (DB_SCALE) {
            for (int n = 0; n < envelope.length; ++n) envelope[n] = 10.0f / (float) Function.LOG10 * (float) Math.log(envelope[n] + Float.MIN_VALUE);
        }
        if (this.normalize == NORMALIZE_NORM2) {
            double norm2 = 0.0;
            for (int n = 0; n < envelope.length; ++n) norm2 += envelope[n] * envelope[n];
            norm2 = (float) Math.sqrt(norm2);
            for (int n = 0; n < envelope.length; ++n) envelope[n] /= norm2;
        }
        send(new MsgAudioSpectrumEnvelope(m.time, m.duration, m.hopsize, envelope, LO_EDGE, HI_EDGE, RESOLUTION, DB_SCALE, normalize));
    }
}
