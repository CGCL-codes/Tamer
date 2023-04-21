package org.kc7bfi.jflac.apps;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.kc7bfi.jflac.FrameListener;
import org.kc7bfi.jflac.FLACDecoder;
import org.kc7bfi.jflac.frame.Frame;
import org.kc7bfi.jflac.metadata.Metadata;

/**
 * Analyze FLAC file application.
 * @author kc7bfi
 */
public class Analyser implements FrameListener {

    private int frameNum = 0;

    /**
     * Analyse an input FLAC file.
     * @param inFileName The input file name
     * @throws IOException thrown if error reading file
     */
    public void analyse(String inFileName) throws IOException {
        System.out.println("FLAX Analysis for " + inFileName);
        FileInputStream is = new FileInputStream(inFileName);
        FLACDecoder decoder = new FLACDecoder(is);
        decoder.addFrameListener(this);
        decoder.decode();
    }

    /**
     * Process metadata records.
     * @param metadata the metadata block
     * @see org.kc7bfi.jflac.FrameListener#processMetadata(org.kc7bfi.jflac.metadata.MetadataBase)
     */
    public void processMetadata(Metadata metadata) {
        System.out.println(metadata.toString());
    }

    /**
     * Process data frames.
     * @param frame the data frame
     * @see org.kc7bfi.jflac.FrameListener#processFrame(org.kc7bfi.jflac.frame.Frame)
     */
    public void processFrame(Frame frame) {
        frameNum++;
        System.out.println(frameNum + " " + frame.toString());
    }

    /**
     * Called for each frame error detected.
     * @param msg   The error message
     * @see org.kc7bfi.jflac.FrameListener#processError(java.lang.String)
     */
    public void processError(String msg) {
        System.out.println("Frame Error: " + msg);
    }

    /**
     * Main routine.
     * <p>args[0] is the FLAC file name to analyse
     * @param args  Command arguments
     */
    public static void main(String[] args) {
        try {
            Analyser analyser = new Analyser();
            analyser.analyse(args[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
