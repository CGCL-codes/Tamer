package cunei.decode;

import java.util.Collection;
import cunei.confusion.ConfusionLattice;
import cunei.document.BufferedDocuments;
import cunei.document.Phrase;
import cunei.document.Sentence;
import cunei.hypothesis.Hypothesis;
import cunei.translate.TranslationLattice;
import cunei.util.Log;
import cunei.util.Time;

public class TranslateAndDecode implements cunei.translate.Operation {

    private Operation op;

    private Decoder decoder;

    private BufferedDocuments<Phrase> references;

    public TranslateAndDecode(cunei.decode.Operation op) {
        this.op = op;
        decoder = new Decoder();
    }

    public void complete() {
        op.complete();
    }

    public void run(Sentence<ConfusionLattice> sentence, TranslationLattice lattice) {
        Sentence<Phrase>[] reference = references == null ? null : references.getSentence(sentence.getSentenceId());
        Time timer = new Time();
        Collection<Hypothesis> hypotheses = decoder.decode(lattice, reference);
        Log.getInstance().info("Decoded translations: " + timer);
        op.run(sentence, lattice, hypotheses, reference);
    }

    public void setReferences(BufferedDocuments<Phrase> references) {
        this.references = references;
    }
}
