package cunei.corpus;

public class SampledCorpusReader implements CorpusReader {

    private CorpusReader reader;

    private float freq;

    private int window;

    private int n;

    public SampledCorpusReader(CorpusReader reader, float freq, int window) {
        this.reader = reader;
        this.freq = freq;
        this.window = window;
        n = 0;
    }

    public MultiSentence next() {
        for (MultiSentence result = reader.next(); result != null; result = reader.next()) {
            int sentenceId = result.getSentenceId() + 1;
            if (n / window <= freq * sentenceId / window) {
                n++;
                return result;
            }
        }
        return null;
    }
}
