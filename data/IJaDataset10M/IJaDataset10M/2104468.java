package teste;

import java.io.File;
import java.util.Vector;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;

public class ReadSound {

    AudioInputStream audioInputStream;

    private int PACKET_SIZE = 5000;

    int nBytesRead = 0;

    Vector[] bufferChannelFile;

    byte[] abData;

    int quantPacket;

    int rest;

    int channels;

    AudioFormat audioFormat;

    public ReadSound(String filename) throws Exception {
        File soundFile = new File(filename);
        audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        audioFormat = audioInputStream.getFormat();
        channels = audioFormat.getChannels();
        bufferChannelFile = new Vector[this.channels];
        for (int i = 0; i < this.channels; i++) bufferChannelFile[i] = new Vector<Byte>();
        abData = new byte[channels * PACKET_SIZE];
    }

    public void fillBuffer() {
        int nBytesRead = 0;
        while (nBytesRead != -1) {
            try {
                nBytesRead = this.getNextFrame();
                if (nBytesRead != -1) {
                    for (int i = 0, n = 0; i < nBytesRead; i += this.channels, n++) {
                        for (int j = 0; j < this.channels; j++) {
                            bufferChannelFile[j].add(n, abData[j + i]);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int qtdeBytesChannel = bufferChannelFile[0].size() * abData.length;
        this.quantPacket = qtdeBytesChannel / PACKET_SIZE;
    }

    public int getNextFrame() throws Exception {
        return audioInputStream.read(abData, 0, abData.length);
    }

    public AudioFormat getAudioFormat() {
        return audioFormat;
    }

    public void setAudioFormat(AudioFormat audioFormat) {
        this.audioFormat = audioFormat;
    }

    public int getQuantPacket() {
        return quantPacket;
    }

    public void setQuantPacket(int quantPacket) {
        this.quantPacket = quantPacket;
    }

    public int getRest() {
        return rest;
    }

    public void setRest(int rest) {
        this.rest = rest;
    }

    public Vector getChannel(int n) {
        return bufferChannelFile[n - 1];
    }

    public int getNumChannel() {
        return this.channels;
    }
}
