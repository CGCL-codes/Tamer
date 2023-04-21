package net.sf.telescope.communications.client.audio;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class AudioPlayer {

    SourceDataLine line;

    DatagramSocket socket;

    private boolean running;

    public AudioPlayer(DatagramSocket s) {
        socket = s;
        running = false;
        try {
            AudioFormat aFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 8000.0f, 16, 2, 4, 8000.0f, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, aFormat);
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(aFormat);
        } catch (Exception e) {
            System.out.println("Line Exception " + e);
        }
    }

    public void stopPlayer() {
        line.close();
        running = false;
    }

    public void playFile() {
        byte[] data = new byte[MicReader.BUFFER_SIZE];
        running = true;
        try {
            while (running) {
                DatagramPacket packet = new DatagramPacket(data, data.length);
                socket.receive(packet);
                line.start();
                line.write(packet.getData(), 0, MicReader.BUFFER_SIZE);
            }
        } catch (Exception e) {
            System.out.println("Line Exception " + e);
        }
    }
}
