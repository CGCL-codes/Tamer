package apps;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import jflac.PCMProcessor;
import jflac.FLACDecoder;
import metadata.StreamInfo;
import util.ByteData;

/**
 * Play a FLAC file application.
 * @author kc7bfi
 */
public class Player implements PCMProcessor {

    private AudioFormat fmt;

    private DataLine.Info info;

    private SourceDataLine line;

    private Vector listeners = new Vector();

    public void addListener(LineListener listener) {
        listeners.add(listener);
    }

    /**
     * Decode and play an input FLAC file.
     * @param inFileName    The input FLAC file name
     * @throws IOException  Thrown if error reading file
     * @throws LineUnavailableException Thrown if error playing file
     */
    public void decode(String inFileName) throws IOException, LineUnavailableException {
        System.out.println("Play [" + inFileName + "]");
        FileInputStream is = new FileInputStream(inFileName);
        FLACDecoder decoder = new FLACDecoder(is);
        decoder.addPCMProcessor(this);
        try {
            decoder.decode();
        } catch (EOFException e) {
        }
        line.drain();
        line.close();
        listeners.clear();
    }

    /**
     * Process the StreamInfo block.
     * @param streamInfo the StreamInfo block
     * @see org.kc7bfi.jflac.PCMProcessor#processStreamInfo(org.kc7bfi.jflac.metadata.StreamInfo)
     */
    public void processStreamInfo(StreamInfo streamInfo) {
        try {
            fmt = streamInfo.getAudioFormat();
            info = new DataLine.Info(SourceDataLine.class, fmt, AudioSystem.NOT_SPECIFIED);
            line = (SourceDataLine) AudioSystem.getLine(info);
            int size = listeners.size();
            for (int index = 0; index < size; index++) line.addLineListener((LineListener) listeners.get(index));
            line.open(fmt, AudioSystem.NOT_SPECIFIED);
            line.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Process the decoded PCM bytes.
     * @param pcm The decoded PCM data
     * @see org.kc7bfi.jflac.PCMProcessor#processPCM(org.kc7bfi.jflac.util.ByteSpace)
     */
    public void processPCM(ByteData pcm) {
        line.write(pcm.getData(), 0, pcm.getLen());
    }

    public void removeListener(LineListener listener) {
        listeners.removeElement(listener);
    }

    /**
     * The main routine.
     * <p>args[0] is the input file name
     * @param args  Command line arguments
     */
    public static void main(String[] args) {
        try {
            Player decoder = new Player();
            for (int i = 0; i < args.length; i++) decoder.decode(args[i]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
