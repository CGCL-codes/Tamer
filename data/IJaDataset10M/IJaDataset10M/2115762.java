package edu.cmu.sphinx.frontend;

import edu.cmu.sphinx.util.SphinxProperties;
import edu.cmu.sphinx.util.ExtendedStreamTokenizer;
import java.io.IOException;

/**
 * Produces MelCepstrum data from a file.
 */
public class MelCepstrumFileProducer extends DataProcessor implements CepstrumSource {

    private static final String PROP_CEPSTRUM_FILE = FrontEnd.PROP_PREFIX + "MelCepstrumFileProducer.file";

    private String path;

    private ExtendedStreamTokenizer est;

    private int numFrames;

    private int curFrame;

    private int cepstrumLength;

    /**
     * Constructs a MelCepstrumFileProducer that reads
     * MelCepstrum data from the given path.
     *
     * @param context the context for the producer
     *
     * @throws IOException if an error occurs while reading the data
     */
    public MelCepstrumFileProducer(String name, String context) throws IOException {
        super(name, context);
        initSphinxProperties();
        est = new ExtendedStreamTokenizer(path);
        numFrames = est.getInt("num_frames");
        est.expectString("frames");
    }

    /**
     * Reads the parameters needed from the static SphinxProperties object.
     */
    private void initSphinxProperties() {
        SphinxProperties properties = getSphinxProperties();
        cepstrumLength = properties.getInt(FrontEnd.PROP_CEPSTRUM_SIZE, 13);
        path = properties.getString(PROP_CEPSTRUM_FILE, "file");
        System.out.println("File is " + path);
    }

    /**
     * Returns the next Cepstrum object, which is the mel cepstrum of the
     * input frame. However, it can also be other Cepstrum objects
     * like a EndPointSignal.
     *
     * @return the next available Cepstrum object, returns null if no
     *     Cepstrum object is available
     */
    public Cepstrum getCepstrum() throws IOException {
        Cepstrum data = null;
        if (curFrame == 0) {
            data = new Cepstrum(Signal.UTTERANCE_START);
        } else if (curFrame == numFrames) {
            data = new Cepstrum(Signal.UTTERANCE_END);
        } else if (curFrame > numFrames) {
            data = null;
        } else {
            float[] vectorCepstrum = new float[cepstrumLength];
            for (int i = 0; i < cepstrumLength; i++) {
                vectorCepstrum[i] = est.getFloat("cepstrum data");
            }
            data = new Cepstrum(vectorCepstrum);
            System.out.println("CP " + data);
        }
        curFrame++;
        return data;
    }
}
