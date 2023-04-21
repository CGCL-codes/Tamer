package edu.cmu.sphinx.demo.lattice;

import edu.cmu.sphinx.frontend.util.AudioFileDataSource;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Lattice;
import edu.cmu.sphinx.result.LatticeOptimizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/** A simple Lattice demo showing a simple speech application that generates a Lattice from a recognition result. */
public class LatticeDemo {

    /** Main method for running the Lattice demo. */
    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {
        URL audioURL, url;
        if (args.length > 0) {
            audioURL = new File(args[0]).toURI().toURL();
        } else {
            audioURL = LatticeDemo.class.getResource("10001-90210-01803.wav");
        }
        if (args.length > 1) {
            url = new File(args[1]).toURI().toURL();
        } else {
            url = LatticeDemo.class.getResource("config.xml");
        }
        System.out.println("Loading...");
        ConfigurationManager cm = new ConfigurationManager(url);
        Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
        recognizer.allocate();
        AudioFileDataSource dataSource = (AudioFileDataSource) cm.lookup("audioFileDataSource");
        dataSource.setAudioFile(audioURL, null);
        boolean done = false;
        while (!done) {
            Result result = recognizer.recognize();
            if (result != null) {
                Lattice lattice = new Lattice(result);
                LatticeOptimizer optimizer = new LatticeOptimizer(lattice);
                optimizer.optimize();
                lattice.dumpAllPaths();
                String resultText = result.getBestResultNoFiller();
                System.out.println("I heard: " + resultText + '\n');
            } else {
                done = true;
            }
        }
    }
}
