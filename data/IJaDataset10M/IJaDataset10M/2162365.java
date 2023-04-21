package edu.cmu.sphinx.tools.audio;

import javax.sound.sampled.AudioFormat;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Reads a raw audio file (i.e., a file that is just plain raw
 * samples and nothing else) and converts it to signed data.
 */
public class RawReader {

    /**
     * Reads raw bytes from the given audioStream and returns
     * big endian signed samples based on the audioFormat.  Only
     * PCM_SIGNED or PCM_UNSIGNED encodings are allowed.
     *
     * @param audioStream the stream containing the raw bytes
     * @param audioFormat a hint of what to expect from the stream
     * @return big endian signed samples, one sample per array element
     */
    public static short[] readAudioData(InputStream audioStream, AudioFormat audioFormat) throws IOException {
        int bytesPerSample;
        boolean signedData = true;
        boolean bigEndian;
        AudioFormat.Encoding encoding = audioFormat.getEncoding();
        bytesPerSample = audioFormat.getSampleSizeInBits() / 8;
        if (encoding == AudioFormat.Encoding.PCM_SIGNED) {
            signedData = true;
        } else if (encoding == AudioFormat.Encoding.PCM_UNSIGNED) {
            signedData = false;
        } else {
            System.err.println("Unsupported audio encoding: " + encoding);
            System.exit(-1);
        }
        bigEndian = audioFormat.isBigEndian();
        byte[] buffer = new byte[bytesPerSample];
        ArrayList samples = new ArrayList();
        int read = 0;
        int totalRead = 0;
        boolean done = false;
        while (!done) {
            totalRead = read = audioStream.read(buffer, 0, bytesPerSample);
            while (totalRead < bytesPerSample) {
                if (read == -1) {
                    done = true;
                    break;
                } else {
                    read = audioStream.read(buffer, totalRead, bytesPerSample - totalRead);
                    totalRead += read;
                }
            }
            if (!done) {
                int val = 0;
                if (bigEndian) {
                    val = (int) buffer[0];
                    if (!signedData) {
                        val &= 0xff;
                    }
                    for (int i = 1; i < bytesPerSample; i++) {
                        int temp = (int) buffer[i] & 0xff;
                        val = (val << 8) + temp;
                    }
                } else {
                    val = (int) buffer[bytesPerSample - 1];
                    if (!signedData) {
                        val &= 0xff;
                    }
                    for (int i = bytesPerSample - 2; i >= 0; i--) {
                        int temp = (int) buffer[i] & 0xff;
                        val = (val << 8) + temp;
                    }
                }
                if (!signedData) {
                    val = val - (1 << ((bytesPerSample * 8) - 1));
                }
                samples.add(new Short((short) val));
            }
        }
        short[] audioData = new short[samples.size()];
        for (int i = 0; i < audioData.length; i++) {
            audioData[i] = ((Short) samples.get(i)).shortValue();
        }
        return audioData;
    }
}
