package org.mobicents.media.server.impl.resource.cnf;

/**
 * Approximate loudest speaker mixer, all other speakers are removed
 * 
 * @author Oleg Kulikov
 * @author Vladimir Ralev
 */
public class MaxPowerAudioMixer extends AudioMixer {

    private int lastChannel = 0;

    private static int takeoverRatio = 2;

    /**
     * Creates a new instance of AudioMixer.
     * 
     * @param packetPeriod
     *            packetization period in milliseconds.
     * @param fmt
     *            format of the output stream.
     */
    public MaxPowerAudioMixer(String name) {
        super(name);
    }

    @Override
    public byte[] mix(byte[][] input) {
        int numChannels = input.length;
        short[][] inputs = new short[input.length][];
        long[] powerPerChannel = new long[numChannels];
        long[] sumPerChannel = new long[numChannels];
        for (int q = 0; q < numChannels; q++) {
            inputs[q] = byteToShortArray(input[q]);
            for (int w = 0; w < inputs[q].length; w++) {
                powerPerChannel[q] += Math.abs(inputs[q][w]);
                sumPerChannel[q] += inputs[q][w];
            }
        }
        int maxPowerChannel = 0;
        long maxPower = 0;
        for (int q = 0; q < numChannels; q++) {
            powerPerChannel[q] -= sumPerChannel[q];
        }
        for (int q = 0; q < numChannels; q++) {
            if (powerPerChannel[q] > maxPower) {
                maxPowerChannel = q;
                maxPower = powerPerChannel[q];
            }
        }
        if (maxPower / takeoverRatio < powerPerChannel[lastChannel]) {
            maxPowerChannel = lastChannel;
        }
        return input[maxPowerChannel];
    }
}
