package com.catarak.uwhscoretime;

import java.io.*;
import javax.sound.sampled.*;
import java.net.*;
import java.applet.*;

/*************************************************************************
 *  Compilation:  javac StdAudio.java
 *  Execution:    java StdAudio
 *
 *  Simple library for reading, writing, and manipulationg .wav files.

 *
 *  Limitations
 *  -----------
 *    - Does not seem to work properly when reading .wav files from a .jar file.
 *    - Assumes the audio is monaural, and 44,100 samples per second.
 *
 * A slight revision of StdAudio by Robert Sedgewick and Kevin Wayne
 * of Computer Science, Princeton.
 *
 *************************************************************************/
public class StdAudio {

    private static final int FPS = 44100;

    private StdAudio() {
    }

    /** = the values in file filename, with values
        scaled to be between -1 and 1
        */
    public static double[] read(String filename) {
        byte[] data = readByte(filename);
        int N = data.length;
        double[] d = new double[N / 2];
        for (int i = 0; i < N / 2; i++) {
            d[i] = ((short) (((data[2 * i + 1] & 0xFF) << 8) + (data[2 * i] & 0xFF))) / 32768.0;
        }
        return d;
    }

    /** Play array input.
        Precondition. It's a .wav file, 44,100 samples per second,
        16-bit audio, mono, signed PCM, little Endian,
        with array values between -1 and 1
    */
    public static void play(double[] input) {
        AudioFormat format = new AudioFormat(FPS, 16, 1, true, false);
        byte[] data = new byte[2 * input.length];
        for (int i = 0; i < input.length; i++) {
            int temp = (int) (input[i] * 32768.0);
            data[2 * i + 0] = (byte) temp;
            data[2 * i + 1] = (byte) (temp >> 8);
        }
        play(format, data);
    }

    /** play the .wav or .midi sound in filename in the background */
    public static void play(String filename) {
        URL url = null;
        try {
            File fil = new File(filename);
            if (fil.canRead()) url = fil.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (url == null) throw new RuntimeException("audio " + filename + " not found");
        AudioClip clip = Applet.newAudioClip(url);
        clip.play();
    }

    /** Save array input as a .wav or .au file in file filename.
        Precondition: filename ends in .wav or .au.
        The file has 44,100 samples per second
        and uses 16-bit audio, mono, signed PCM, little Endian
     */
    public static void save(double[] input, String filename) {
        AudioFormat format = new AudioFormat(FPS, 16, 1, true, false);
        byte[] data = new byte[2 * input.length];
        for (int i = 0; i < input.length; i++) {
            int temp = (int) (input[i] * 32768.0);
            data[2 * i + 0] = (byte) temp;
            data[2 * i + 1] = (byte) (temp >> 8);
        }
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            AudioInputStream ais = new AudioInputStream(bais, format, input.length);
            if (filename.endsWith(".wav") || filename.endsWith(".WAV")) {
                AudioSystem.write(ais, AudioFileFormat.Type.WAVE, new File(filename));
            } else if (filename.endsWith(".au") || filename.endsWith(".AU")) {
                AudioSystem.write(ais, AudioFileFormat.Type.AU, new File(filename));
            } else {
                throw new RuntimeException("File format not supported: " + filename);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /** play the sound represented by filename */
    private static void play(AudioFormat format, byte[] data) {
        SourceDataLine line = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        try {
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        line.start();
        line.write(data, 0, data.length);
        line.drain();
        line.close();
    }

    /** = the audio format of file filename */
    private static AudioFormat format(String filename) {
        AudioInputStream ais = ais(filename);
        AudioFormat format = ais.getFormat();
        return format;
    }

    /** = audio input stream of file filename */
    private static AudioInputStream ais(String filename) {
        AudioInputStream ais = null;
        try {
            URL url = StdAudio.class.getResource(filename);
            ais = AudioSystem.getAudioInputStream(url);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        return ais;
    }

    /** = data in file filename as a byte array */
    private static byte[] readByte(String filename) {
        AudioInputStream ais = ais(filename);
        byte[] data = null;
        try {
            data = new byte[ais.available()];
            ais.read(data);
        } catch (IOException e) {
            throw new RuntimeException("Could not read " + filename);
        }
        return data;
    }

    public static double[] note(double hz, double duration, double amplitude) {
        int N = (int) (FPS * duration);
        double[] tone = new double[N];
        for (int i = 0; i < N; i++) tone[i] = amplitude * Math.sin(2 * Math.PI * i * hz / FPS);
        return tone;
    }

    /** = Play a major scale */
    public static void playScale() {
        int[] steps = { 0, 2, 4, 5, 7, 9, 11, 12 };
        for (int i = 0; i < steps.length; i++) {
            double hz = 440.0 * Math.pow(2, steps[i] / 12.0);
            StdAudio.play(note(hz, 1.0, 0.5));
        }
        System.exit(0);
    }
}
