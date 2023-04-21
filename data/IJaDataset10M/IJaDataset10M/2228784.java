package de.joergjahnke.gameboy.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Implements the Gameboy's sound channel 3, which offers white noise with an envelope function
 * 
 * @author  Joerg Jahnke (joergjahnke@users.sourceforge.net)
 */
public class WhiteNoiseChannel extends SoundChannel {

    /**
     * pre-calculated random values
     */
    private static final boolean[] randoms = new boolean[1 << 15];

    static {
        final Random rand = new Random();
        for (int i = 0; i < randoms.length; ++i) {
            randoms[i] = (rand.nextInt() & 1) == 1;
        }
    }

    /**
     * number of envelope steps
     */
    private int envelopeSweeps;

    /**
     * envelope steps left
     */
    private int envelopeSweepsLeft;

    /**
     * multiplier giving the direction of the envelope: -1 for decrease, +1 for increase
     */
    private int envelopeDirection = -1;

    /**
     * use 7 bit counter steps instead of 15 bit?
     */
    private boolean isSmallStepWidth;

    /**
     * Create a new white noise channel for the given sound chip
     * 
     * @param   sound   the sound chip we work for
     */
    public WhiteNoiseChannel(final SoundChip sound) {
        super(sound);
    }

    /**
     * Set envelope parameters
     * 
     * @param   initialVolume   initial sound volume (0-15)
     * @param   increase    true to increase the frequency on envelope sweeps, false to decrease
     * @param   envelopeSweeps  number of envelope sweeps
     */
    public void setVolumeEnvelope(final int initialVolume, final boolean increase, final int envelopeSweeps) {
        this.volume = initialVolume;
        this.envelopeSweeps = this.envelopeSweepsLeft = (envelopeSweeps * SoundChip.UPDATES_PER_SECOND) >> 6;
        this.envelopeDirection = increase ? 1 : -1;
    }

    /**
     * Set polynomial counter parameters
     * 
     * @param   shift   shift clock frequency
     * @param   isSmallStepWidth    use 7 bit counter steps instead of 15 bit?
     * @param   dividingRatio   dividing ratio of frequencies
     */
    public void setPolynomialCounter(final int shift, final boolean isSmallStepWidth, final int dividingRatio) {
        this.isSmallStepWidth = isSmallStepWidth;
        final double divider = dividingRatio == 0 ? 0.5 : (double) dividingRatio;
        this.frequency = ((int) (524288 / divider)) >> (shift + 1);
    }

    public void update() {
        --this.length;
        if (this.envelopeSweeps > 0) {
            --this.envelopeSweepsLeft;
            if (this.envelopeSweepsLeft <= 0) {
                this.volume = Math.min(MAX_VOLUME, Math.max(0, this.volume + this.envelopeDirection));
                this.envelopeSweepsLeft = this.envelopeSweeps;
            }
        }
    }

    public void mix(final byte[] buffer) {
        final int sampleRate = this.sound.getSampleRate();
        final boolean isLeftActive = isTerminalActive(LEFT);
        final boolean isRightActive = isTerminalActive(RIGHT);
        final int mod = sampleRate * (this.isSmallStepWidth ? 1 << 7 : 1 << 15);
        for (int i = 0, to = buffer.length; i < to; i += 2) {
            final int index = this.audioIndex / sampleRate;
            final int sample = randoms[index] ? this.volume << 1 : -this.volume << 1;
            if (isLeftActive) {
                buffer[i + LEFT] += sample;
            }
            if (isRightActive) {
                buffer[i + RIGHT] += sample;
            }
            this.audioIndex += this.frequency;
            this.audioIndex %= mod;
        }
    }

    public final int getFrequency() {
        return this.startFrequency % 12000;
    }

    public final int getVolume() {
        return hasSound() && this.length > 0 ? this.startVolume * 100 / MAX_VOLUME : 0;
    }

    public final int getType() {
        return 127;
    }

    public void serialize(final DataOutputStream out) throws IOException {
        out.writeInt(this.envelopeSweeps);
        out.writeInt(this.envelopeSweepsLeft);
        out.writeInt(this.envelopeDirection);
        out.writeBoolean(this.isSmallStepWidth);
    }

    public void deserialize(final DataInputStream in) throws IOException {
        this.envelopeSweeps = in.readInt();
        this.envelopeSweepsLeft = in.readInt();
        this.envelopeDirection = in.readInt();
        this.isSmallStepWidth = in.readBoolean();
    }
}
