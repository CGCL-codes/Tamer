package opennlp.tools.formats;

import opennlp.tools.namefind.NameSample;
import opennlp.tools.tokenize.Detokenizer;
import opennlp.tools.util.ObjectStream;

/**
 * <b>Note:</b> Do not use this class, internal use only!
 */
public class NameToSentenceSampleStream extends AbstractToSentenceSampleStream<NameSample> {

    public NameToSentenceSampleStream(Detokenizer detokenizer, ObjectStream<NameSample> samples, int chunkSize) {
        super(detokenizer, samples, chunkSize);
    }

    @Override
    protected String[] toSentence(NameSample sample) {
        return sample.getSentence();
    }
}
