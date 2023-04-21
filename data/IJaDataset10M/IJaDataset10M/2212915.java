package ToolSystem;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;
import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;

public class MusicClip {

    private String file;

    private String prefix;

    private String suffix;

    private String destroy;

    private Converter converter;

    private byte[] bayman;

    private InputStream in;

    private InputStream in2;

    private Clip soundClip;

    private AudioSystem asys;

    private final int BUFSIZE = 4096 * 2;

    private int convsize = BUFSIZE * 2;

    private byte[] buffer = null;

    private Thread player = null;

    private BufferedInputStream bitStream = null;

    private BufferedInputStream bitStream2 = null;

    private int bytes = 0;

    private int RETRY = 3;

    private int retry = RETRY;

    private SyncState oy;

    private StreamState os;

    private Page og;

    private Packet op;

    private Info vi;

    private Comment vc;

    private DspState vd;

    private Block vb;

    private SourceDataLine outputLine;

    private int rate;

    private int channels;

    private int frameSizeInBytes;

    private int bufferLengthInBytes;

    private int currentFrame;

    private int startFrame;

    private int loopFrame;

    private int endFrame;

    private void initializeOGG() throws InternalException, IOException {
        in = new FileInputStream(prefix + ".ogg");
        if (in == null) {
            throw new IOException("Couldn't find input source");
        }
        bitStream = new BufferedInputStream(in);
        int omg = 0;
        int bomg = 0;
        int last_channels = -1;
        int last_rate = -1;
        boolean chained = false;
        initJOrbis();
        retry = RETRY;
        loop: while (true) {
            int eos = 0;
            int index = oy.buffer(BUFSIZE);
            buffer = oy.data;
            try {
                bytes = bitStream.read(buffer, index, BUFSIZE);
            } catch (Exception e) {
                throw new InternalException(e);
            }
            oy.wrote(bytes);
            if (chained) {
                chained = false;
            } else {
                if (oy.pageout(og) != 1) {
                    if (bytes < BUFSIZE) break;
                    throw new InternalException("Input does not appear to be an Ogg bitstream.");
                }
            }
            os.init(og.serialno());
            os.reset();
            vi.init();
            vc.init();
            if (os.pagein(og) < 0) {
                throw new InternalException("Error reading first page of Ogg bitstream data.");
            }
            retry = RETRY;
            if (os.packetout(op) != 1) {
                throw new InternalException("Error reading initial header packet.");
            }
            if (vi.synthesis_headerin(vc, op) < 0) {
                throw new InternalException("This Ogg bitstream does not contain Vorbis audio data.");
            }
            int i = 0;
            while (i < 2) {
                oggAssist(i, index);
            }
            convsize = BUFSIZE / vi.channels;
            vd.synthesis_init(vi);
            vb.init(vd);
            double[][][] _pcm = new double[1][][];
            float[][][] _pcmf = new float[1][][];
            int[] _index = new int[vi.channels];
            getOutputLine(vi.channels, vi.rate);
            while (eos == 0) {
                pageDecipherAssist(chained, eos, bomg, _pcm, _pcmf, _index, index);
            }
            os.clear();
            vb.clear();
            vd.clear();
            vi.clear();
        }
        oy.clear();
        rate = 44100;
        channels = 2;
        AudioFormat audioFormat = new AudioFormat((float) rate, 16, channels, true, false);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Clip not Supported:" + info);
        }
        try {
            soundClip = AudioSystem.getClip();
            soundClip.open(audioFormat, bayman, 0, bayman.length);
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    private void oggAssist(int i, int index) throws InternalException {
        while (i < 2) {
            int result = oy.pageout(og);
            if (result == 0) break;
            if (result == 1) {
                oggSplit(i, result);
            }
        }
        index = oy.buffer(BUFSIZE);
        buffer = oy.data;
        try {
            bytes = bitStream.read(buffer, index, BUFSIZE);
        } catch (Exception e) {
            throw new InternalException(e);
        }
        if (bytes == 0 && i < 2) {
            throw new InternalException("End of file before finding all Vorbis headers!");
        }
        oy.wrote(bytes);
    }

    private void oggSplit(int i, int result) throws InternalException {
        os.pagein(og);
        while (i < 2) {
            result = os.packetout(op);
            if (result == 0) break;
            if (result == -1) {
                throw new InternalException("" + "Corrupt secondary header.  Exiting.");
            }
            vi.synthesis_headerin(vc, op);
            i++;
        }
    }

    private void pageDecipherAssist(boolean chained, int eos, int bomg, double[][][] _pcm, float[][][] _pcmf, int[] _index, int index) throws InternalException {
        while (eos == 0) {
            int result = oy.pageout(og);
            if (result == 0) break;
            if (result == -1) {
            } else {
                pageDecipher(chained, result, eos, bomg, _pcm, _pcmf, _index);
            }
        }
        if (eos == 0) {
            index = oy.buffer(BUFSIZE);
            buffer = oy.data;
            try {
                bytes = bitStream.read(buffer, index, BUFSIZE);
            } catch (Exception e) {
                throw new InternalException(e);
            }
            if (bytes == -1) {
                return;
            }
            oy.wrote(bytes);
            if (bytes == 0) eos = 1;
        }
    }

    private void pageDecipher(boolean chained, int result, int eos, int bomg, double[][][] _pcm, float[][][] _pcmf, int[] _index) {
        os.pagein(og);
        if (og.granulepos() == 0) {
            chained = true;
            eos = 1;
            return;
        }
        while (true) {
            result = os.packetout(op);
            if (result == 0) break;
            if (result == -1) {
            } else {
                packetDecipher(bomg, _pcm, _pcmf, _index);
            }
        }
        if (og.eos() != 0) eos = 1;
    }

    private void packetDecipher(int bomg, double[][][] _pcm, float[][][] _pcmf, int[] _index) {
        int samples;
        if (vb.synthesis(op) == 0) {
            vd.synthesis_blockin(vb);
        }
        while ((samples = vd.synthesis_pcmout(_pcmf, _index)) > 0) {
            double[][] pcm = _pcm[0];
            float[][] pcmf = _pcmf[0];
            boolean clipflag = false;
            int bout = (samples < convsize ? samples : convsize);
            for (int i = 0; i < vi.channels; i++) byteDecipher(i, clipflag, bout, bomg, pcmf, _index[i], i * 2);
            System.out.println(bomg + ":LOOP ITER");
            vd.synthesis_read(bout);
        }
    }

    private void byteDecipher(int i, boolean clipflag, int bout, int bomg, float[][] pcmf, int mono, int ptr) {
        for (int j = 0; j < bout; j++) {
            int val = (int) (pcmf[i][mono + j] * 32767.);
            if (val > 32767) {
                val = 32767;
                clipflag = true;
            }
            if (val < -32768) {
                val = -32768;
                clipflag = true;
            }
            if (val < 0) val = val | 0x8000;
            bayman[bomg + 0] = (byte) (val);
            bayman[bomg + 1] = (byte) (val >>> 8);
            bayman[bomg + 2] = 0;
            bayman[bomg + 3] = 0;
            bomg = bomg + 4;
            ptr += 2 * (vi.channels);
        }
    }

    private void initJOrbis() {
        oy = new SyncState();
        os = new StreamState();
        og = new Page();
        op = new Packet();
        vi = new Info();
        vc = new Comment();
        vd = new DspState();
        vb = new Block(vd);
        buffer = null;
        bytes = 0;
        oy.init();
    }

    private SourceDataLine getOutputLine(int channels, int rate) throws InternalException {
        if (outputLine == null || this.rate != rate || this.channels != channels) {
            if (outputLine != null) {
                outputLine.drain();
                outputLine.stop();
                outputLine.close();
            }
            initJavaSound(channels, rate);
            outputLine.start();
        }
        return outputLine;
    }

    private void initJavaSound(int channels, int rate) throws InternalException {
        try {
            AudioFormat audioFormat = new AudioFormat((float) rate, 16, channels, true, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
            if (!AudioSystem.isLineSupported(info)) {
                throw new Exception("Line " + info + " not supported.");
            }
            try {
                outputLine = (SourceDataLine) AudioSystem.getLine(info);
                outputLine.open(audioFormat);
            } catch (LineUnavailableException ex) {
                throw new Exception("Unable to open the sourceDataLine: " + ex);
            } catch (IllegalArgumentException ex) {
                throw new Exception("Illegal Argument: " + ex);
            }
            frameSizeInBytes = audioFormat.getFrameSize();
            int bufferLengthInFrames = outputLine.getBufferSize() / frameSizeInBytes / 2;
            bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
            this.rate = rate;
            this.channels = channels;
        } catch (Exception ee) {
            System.out.println(ee);
        }
    }

    private int grabBytes() throws InternalException {
        int omg = 0;
        int bomg = 0;
        int last_channels = -1;
        int last_rate = -1;
        boolean chained = false;
        initJOrbis();
        retry = RETRY;
        loop: while (true) {
            int eos = 0;
            int index = oy.buffer(BUFSIZE);
            buffer = oy.data;
            try {
                bytes = bitStream2.read(buffer, index, BUFSIZE);
            } catch (Exception e) {
                throw new InternalException(e);
            }
            oy.wrote(bytes);
            if (chained) {
                chained = false;
            } else {
                if (oy.pageout(og) != 1) {
                    if (bytes < BUFSIZE) break;
                    throw new InternalException("Input does not appear to be an Ogg bitstream.");
                }
            }
            os.init(og.serialno());
            os.reset();
            vi.init();
            vc.init();
            if (os.pagein(og) < 0) {
                throw new InternalException("Error reading first page of Ogg bitstream data.");
            }
            retry = RETRY;
            if (os.packetout(op) != 1) {
                throw new InternalException("Error reading initial header packet.");
            }
            if (vi.synthesis_headerin(vc, op) < 0) {
                throw new InternalException("This Ogg bitstream does not contain Vorbis audio data.");
            }
            int i = 0;
            while (i < 2) {
                while (i < 2) {
                    int result = oy.pageout(og);
                    if (result == 0) break;
                    if (result == 1) {
                        os.pagein(og);
                        while (i < 2) {
                            result = os.packetout(op);
                            if (result == 0) break;
                            if (result == -1) {
                                throw new InternalException("Corrupt secondary header.  Exiting.");
                            }
                            vi.synthesis_headerin(vc, op);
                            i++;
                        }
                    }
                }
                index = oy.buffer(BUFSIZE);
                buffer = oy.data;
                try {
                    bytes = bitStream2.read(buffer, index, BUFSIZE);
                } catch (Exception e) {
                    throw new InternalException(e);
                }
                if (bytes == 0 && i < 2) {
                    throw new InternalException("End of file before finding all Vorbis headers!");
                }
                oy.wrote(bytes);
            }
            convsize = BUFSIZE / vi.channels;
            vd.synthesis_init(vi);
            vb.init(vd);
            double[][][] _pcm = new double[1][][];
            float[][][] _pcmf = new float[1][][];
            int[] _index = new int[vi.channels];
            getOutputLine(vi.channels, vi.rate);
            while (eos == 0) {
                while (eos == 0) {
                    int result = oy.pageout(og);
                    if (result == 0) break;
                    if (result == -1) {
                    } else {
                        os.pagein(og);
                        if (og.granulepos() == 0) {
                            chained = true;
                            eos = 1;
                            break;
                        }
                        while (true) {
                            result = os.packetout(op);
                            if (result == 0) break;
                            if (result == -1) {
                            } else {
                                int samples;
                                if (vb.synthesis(op) == 0) {
                                    vd.synthesis_blockin(vb);
                                }
                                while ((samples = vd.synthesis_pcmout(_pcmf, _index)) > 0) {
                                    double[][] pcm = _pcm[0];
                                    float[][] pcmf = _pcmf[0];
                                    boolean clipflag = false;
                                    int bout = (samples < convsize ? samples : convsize);
                                    for (i = 0; i < vi.channels; i++) {
                                        int ptr = i * 2;
                                        int mono = _index[i];
                                        for (int j = 0; j < bout; j++) {
                                            int val = (int) (pcmf[i][mono + j] * 32767.);
                                            if (val > 32767) {
                                                val = 32767;
                                                clipflag = true;
                                            }
                                            if (val < -32768) {
                                                val = -32768;
                                                clipflag = true;
                                            }
                                            if (val < 0) val = val | 0x8000;
                                            bomg = bomg + 4;
                                            ptr += 2 * (vi.channels);
                                        }
                                    }
                                    System.out.println(bomg + ":LOOP ITER");
                                    vd.synthesis_read(bout);
                                }
                            }
                        }
                        if (og.eos() != 0) eos = 1;
                    }
                }
                if (eos == 0) {
                    index = oy.buffer(BUFSIZE);
                    buffer = oy.data;
                    try {
                        bytes = bitStream2.read(buffer, index, BUFSIZE);
                    } catch (Exception e) {
                        throw new InternalException(e);
                    }
                    if (bytes == -1) {
                        break;
                    }
                    oy.wrote(bytes);
                    if (bytes == 0) eos = 1;
                }
            }
            os.clear();
            vb.clear();
            vd.clear();
            vi.clear();
        }
        oy.clear();
        return (bomg);
    }

    public class InternalException extends Exception {

        public InternalException(Exception e) {
            super(e);
        }

        public InternalException(String msg) {
            super(msg);
        }
    }
}
