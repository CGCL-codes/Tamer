package de.spec.recognition;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.UnsupportedAudioFileException;
import edu.cmu.sphinx.frontend.util.AudioFileDataSource;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;

public class Transcriber {

    public static List<Result> unitTestBuffer = new ArrayList<Result>();

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {
        URL audioURL;
        if (args.length > 0) {
            audioURL = new File(args[0]).toURI().toURL();
        } else {
            audioURL = Transcriber.class.getResource("10001-90210-01803.wav");
        }
        URL configURL = Transcriber.class.getResource("config.xml");
        ConfigurationManager cm = new ConfigurationManager(configURL);
        Recognizer recognizer = (Recognizer) cm.lookup("recognizer");
        recognizer.allocate();
        AudioFileDataSource dataSource = (AudioFileDataSource) cm.lookup("audioFileDataSource");
        dataSource.setAudioFile(audioURL, null);
        Result result;
        while ((result = recognizer.recognize()) != null) {
            String resultText = result.getBestResultNoFiller();
            System.out.println(resultText);
            unitTestBuffer.add(result);
        }
    }
}
