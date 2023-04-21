package com.cell.bms.oal;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.ByteBuffer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import net.java.games.joal.AL;
import net.java.games.joal.util.ALut;
import com.cell.CIO;
import com.cell.CObject;
import com.cell.bms.BMSFile;
import com.cell.bms.IDefineSound;
import com.cell.j2se.CAppBridge;
import de.jarnbjo.ogg.CachedUrlStream;
import de.jarnbjo.ogg.EndOfOggStreamException;
import de.jarnbjo.ogg.FileStream;
import de.jarnbjo.ogg.LogicalOggStream;
import de.jarnbjo.ogg.PhysicalOggStream;
import de.jarnbjo.ogg.UncachedUrlStream;
import de.jarnbjo.vorbis.IdentificationHeader;
import de.jarnbjo.vorbis.VorbisStream;

public class JALSound implements IDefineSound {

    final JALNoteFactory factory;

    final AL al;

    String sound_name;

    int[] buffer;

    int[] format = new int[1];

    int[] size = new int[1];

    ByteBuffer[] data = new ByteBuffer[1];

    int[] freq = new int[1];

    int[] loop = new int[1];

    public JALSound(JALNoteFactory factory, BMSFile bms, String sound) {
        this.factory = factory;
        this.al = factory.al;
        this.sound_name = sound;
        synchronized (al) {
            if (sound.toLowerCase().endsWith(".wav")) {
                initWav(bms.bms_dir + "/" + sound);
            } else if (sound.toLowerCase().endsWith(".ogg")) {
                initOgg(bms.bms_dir + "/" + sound);
            }
        }
    }

    private void initWav(String file) {
        InputStream is = CIO.loadStream(file);
        if (is != null) {
            try {
                {
                    ALut.alutLoadWAVFile(is, format, data, size, freq, loop);
                    if (data[0] == null) {
                        System.err.println("Error loading WAV file : " + file);
                        return;
                    }
                }
                {
                    int[] buffer = new int[1];
                    al.alGenBuffers(1, buffer, 0);
                    if (al.alGetError() != AL.AL_NO_ERROR) {
                        System.err.println("Error generating OpenAL buffers : " + file);
                        return;
                    }
                    al.alBufferData(buffer[0], format[0], data[0], size[0], freq[0]);
                    if (al.alGetError() != AL.AL_NO_ERROR) {
                        System.err.println("Error bind WAV file : " + file);
                        return;
                    }
                    this.buffer = buffer;
                }
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }

    private void initOgg(String file) {
        try {
            {
                PhysicalOggStream os = null;
                AudioFormat audioFormat = null;
                ByteArrayOutputStream baos = new ByteArrayOutputStream(10240);
                URL url = CIO.getResourceURL(file);
                if (url != null) {
                    os = new UncachedUrlStream(url);
                } else {
                    File of = new File(file);
                    if (of.exists()) {
                        os = new FileStream(new RandomAccessFile(of, "r"));
                    }
                }
                if (os == null) {
                    System.err.println("can not create sound : " + file);
                }
                for (Object los : os.getLogicalStreams()) {
                    LogicalOggStream loStream = (LogicalOggStream) los;
                    VorbisStream vStream = new VorbisStream(loStream);
                    IdentificationHeader vStreamHdr = vStream.getIdentificationHeader();
                    audioFormat = new AudioFormat((float) vStreamHdr.getSampleRate(), 16, vStreamHdr.getChannels(), true, true);
                    try {
                        byte t = 0;
                        byte[] data = new byte[2];
                        while (true) {
                            vStream.readPcm(data, 0, data.length);
                            t = data[0];
                            data[0] = data[1];
                            data[1] = t;
                            baos.write(data);
                        }
                    } catch (EndOfOggStreamException e) {
                    }
                    vStream.close();
                    loStream.close();
                }
                os.close();
                if (audioFormat.getChannels() == 1) format[0] = AL.AL_FORMAT_MONO16; else format[0] = AL.AL_FORMAT_STEREO16;
                data[0] = ByteBuffer.wrap(baos.toByteArray());
                size[0] = baos.size();
                freq[0] = (int) audioFormat.getFrameRate();
                System.out.println(audioFormat + " size = " + baos.size() + " : " + file);
            }
            {
                int[] buffer = new int[1];
                al.alGenBuffers(1, buffer, 0);
                if (al.alGetError() != AL.AL_NO_ERROR) {
                    System.err.println("Error generating OpenAL buffers : " + file);
                    return;
                }
                al.alBufferData(buffer[0], format[0], data[0], size[0], freq[0]);
                if (al.alGetError() != AL.AL_NO_ERROR) {
                    System.err.println("Error bind WAV file : " + file);
                    return;
                }
                this.buffer = buffer;
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    @Override
    public void play() {
        if (buffer != null) {
            JALPlayer player = factory.getFreePlayer();
            if (player != null) {
                player.bindBuffer(this);
                player.play();
            } else {
                System.err.println("Can not play, no free source ! " + sound_name);
            }
        }
    }

    @Override
    public void stop() {
        if (buffer != null) {
            JALPlayer player = factory.getActivePlayer(this);
            if (player != null) {
                player.stop();
            }
        }
    }

    @Override
    public void dispose() {
        if (buffer != null) {
            al.alDeleteBuffers(1, buffer, 0);
            System.out.println("alDeleteBuffers : ");
        }
    }

    @Override
    protected void finalize() throws Throwable {
        dispose();
    }

    public static void swapBytes(byte[] b, int off, int len) {
        byte tempByte;
        for (int i = off; i < (off + len); i += 2) {
            tempByte = b[i];
            b[i] = b[i + 1];
            b[i + 1] = tempByte;
        }
    }

    public static void main(String[] args) {
        CAppBridge.init();
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(10240);
            UncachedUrlStream os = new UncachedUrlStream(CIO.getResourceURL("broken_glass.ogg"));
            for (Object los : os.getLogicalStreams()) {
                LogicalOggStream loStream = (LogicalOggStream) los;
                VorbisStream vStream = new VorbisStream(loStream);
                IdentificationHeader vStreamHdr = vStream.getIdentificationHeader();
                AudioFormat audioFormat = new AudioFormat((float) vStreamHdr.getSampleRate(), 16, vStreamHdr.getChannels(), true, true);
                System.out.println(audioFormat);
                try {
                    byte[] data = new byte[1];
                    while (true) {
                        vStream.readPcm(data, 0, 1);
                        baos.write(data);
                    }
                } catch (EndOfOggStreamException e) {
                }
                vStream.close();
                loStream.close();
            }
            os.close();
            System.out.println("pcm data size = " + baos.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
