package mp3;

/**
 * ResvFrameBegin:<BR>
 * Called (repeatedly) at the beginning of a frame. Updates the maximum size of
 * the reservoir, and checks to make sure main_data_begin was set properly by
 * the formatter<BR>
 * Background information:
 * 
 * This is the original text from the ISO standard. Because of sooo many bugs
 * and irritations correcting comments are added in brackets []. A '^W' means
 * you should remove the last word.
 * 
 * <PRE>
 *  1. The following rule can be used to calculate the maximum
 *     number of bits used for one granule [^W frame]:<BR>
 *     At the highest possible bitrate of Layer III (320 kbps
 *     per stereo signal [^W^W^W], 48 kHz) the frames must be of
 *     [^W^W^W are designed to have] constant length, i.e.
 *     one buffer [^W^W the frame] length is:<BR>
 * 
 *         320 kbps * 1152/48 kHz = 7680 bit = 960 byte
 * 
 *     This value is used as the maximum buffer per channel [^W^W] at
 *     lower bitrates [than 320 kbps]. At 64 kbps mono or 128 kbps
 *     stereo the main granule length is 64 kbps * 576/48 kHz = 768 bit
 *     [per granule and channel] at 48 kHz sampling frequency.
 *     This means that there is a maximum deviation (short time buffer
 *     [= reservoir]) of 7680 - 2*2*768 = 4608 bits is allowed at 64 kbps.
 *     The actual deviation is equal to the number of bytes [with the
 *     meaning of octets] denoted by the main_data_end offset pointer.
 *     The actual maximum deviation is (2^9-1)*8 bit = 4088 bits
 *     [for MPEG-1 and (2^8-1)*8 bit for MPEG-2, both are hard limits].
 *     ... The xchange of buffer bits between the left and right channel
 *     is allowed without restrictions [exception: dual channel].
 *     Because of the [constructed] constraint on the buffer size
 *     main_data_end is always set to 0 in the case of bit_rate_index==14,
 *     i.e. data rate 320 kbps per stereo signal [^W^W^W]. In this case
 *     all data are allocated between adjacent header [^W sync] words
 *     [, i.e. there is no buffering at all].
 * </PRE>
 */
public class Reservoir {

    BitStream bs;

    public final void setModules(BitStream bs) {
        this.bs = bs;
    }

    public final int ResvFrameBegin(final LameGlobalFlags gfp, final MeanBits mean_bits) {
        final LameInternalFlags gfc = gfp.internal_flags;
        int maxmp3buf;
        final IIISideInfo l3_side = gfc.l3_side;
        int frameLength = bs.getframebits(gfp);
        mean_bits.bits = (frameLength - gfc.sideinfo_len * 8) / gfc.mode_gr;
        int resvLimit = (8 * 256) * gfc.mode_gr - 8;
        if (gfp.brate > 320) {
            maxmp3buf = 8 * ((int) ((gfp.brate * 1000) / (gfp.out_samplerate / 1152f) / 8 + .5));
        } else {
            maxmp3buf = 8 * 1440;
            if (gfp.strict_ISO) {
                maxmp3buf = 8 * ((int) (320000 / (gfp.out_samplerate / 1152f) / 8 + .5));
            }
        }
        gfc.ResvMax = maxmp3buf - frameLength;
        if (gfc.ResvMax > resvLimit) gfc.ResvMax = resvLimit;
        if (gfc.ResvMax < 0 || gfp.disable_reservoir) gfc.ResvMax = 0;
        int fullFrameBits = mean_bits.bits * gfc.mode_gr + Math.min(gfc.ResvSize, gfc.ResvMax);
        if (fullFrameBits > maxmp3buf) fullFrameBits = maxmp3buf;
        assert (0 == gfc.ResvMax % 8);
        assert (gfc.ResvMax >= 0);
        l3_side.resvDrain_pre = 0;
        if (gfc.pinfo != null) {
            gfc.pinfo.mean_bits = mean_bits.bits / 2;
            gfc.pinfo.resvsize = gfc.ResvSize;
        }
        return fullFrameBits;
    }

    /**
	 * returns targ_bits: target number of bits to use for 1 granule<BR>
	 * extra_bits: amount extra available from reservoir<BR>
	 * Mark Taylor 4/99
	 */
    public final int ResvMaxBits(final LameGlobalFlags gfp, final int mean_bits, final MeanBits targ_bits, final int cbr) {
        final LameInternalFlags gfc = gfp.internal_flags;
        int add_bits;
        int ResvSize = gfc.ResvSize, ResvMax = gfc.ResvMax;
        if (cbr != 0) ResvSize += mean_bits;
        if ((gfc.substep_shaping & 1) != 0) ResvMax *= 0.9;
        targ_bits.bits = mean_bits;
        if (ResvSize * 10 > ResvMax * 9) {
            add_bits = ResvSize - (ResvMax * 9) / 10;
            targ_bits.bits += add_bits;
            gfc.substep_shaping |= 0x80;
        } else {
            add_bits = 0;
            gfc.substep_shaping &= 0x7f;
            if (!gfp.disable_reservoir && 0 == (gfc.substep_shaping & 1)) targ_bits.bits -= .1 * mean_bits;
        }
        int extra_bits = (ResvSize < (gfc.ResvMax * 6) / 10 ? ResvSize : (gfc.ResvMax * 6) / 10);
        extra_bits -= add_bits;
        if (extra_bits < 0) extra_bits = 0;
        return extra_bits;
    }

    /**
	 * Called after a granule's bit allocation. Readjusts the size of the
	 * reservoir to reflect the granule's usage.
	 */
    public final void ResvAdjust(final LameInternalFlags gfc, final GrInfo gi) {
        gfc.ResvSize -= gi.part2_3_length + gi.part2_length;
    }

    /**
	 * Called after all granules in a frame have been allocated. Makes sure that
	 * the reservoir size is within limits, possibly by adding stuffing bits.
	 */
    public final void ResvFrameEnd(final LameInternalFlags gfc, final int mean_bits) {
        int over_bits;
        final IIISideInfo l3_side = gfc.l3_side;
        gfc.ResvSize += mean_bits * gfc.mode_gr;
        int stuffingBits = 0;
        l3_side.resvDrain_post = 0;
        l3_side.resvDrain_pre = 0;
        if ((over_bits = gfc.ResvSize % 8) != 0) stuffingBits += over_bits;
        over_bits = (gfc.ResvSize - stuffingBits) - gfc.ResvMax;
        if (over_bits > 0) {
            assert (0 == over_bits % 8);
            assert (over_bits >= 0);
            stuffingBits += over_bits;
        }
        {
            int mdb_bytes = Math.min(l3_side.main_data_begin * 8, stuffingBits) / 8;
            l3_side.resvDrain_pre += 8 * mdb_bytes;
            stuffingBits -= 8 * mdb_bytes;
            gfc.ResvSize -= 8 * mdb_bytes;
            l3_side.main_data_begin -= mdb_bytes;
        }
        l3_side.resvDrain_post += stuffingBits;
        gfc.ResvSize -= stuffingBits;
    }
}
