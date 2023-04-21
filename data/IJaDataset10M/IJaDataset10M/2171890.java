package edu.upenn.cis.taggers.gene;

import java.io.Serializable;
import java.util.regex.Pattern;
import edu.umass.cs.mallet.base.pipe.Pipe;
import edu.umass.cs.mallet.base.types.Instance;
import edu.umass.cs.mallet.base.types.LabelAlphabet;
import edu.umass.cs.mallet.base.types.LabelSequence;
import edu.umass.cs.mallet.base.types.Token;
import edu.umass.cs.mallet.base.types.TokenSequence;
import edu.upenn.cis.taggers.Constants;

public class GeneSentence2TokenSequence extends Pipe implements Serializable {

    private static final long serialVersionUID = Constants.SVUID_GENE_BIO_CREATIVE_SENTENCE_2_TOKEN_SEQUENCE;

    public GeneSentence2TokenSequence() {
        super(null, LabelAlphabet.class);
    }

    public GeneSentence2TokenSequence(boolean extraFeatures) {
        super(null, LabelAlphabet.class);
    }

    public Instance pipe(Instance carrier) {
        String sentenceLines = (String) carrier.getData();
        String[] tokens = sentenceLines.split("\n");
        TokenSequence data = new TokenSequence(tokens.length);
        LabelSequence target = new LabelSequence((LabelAlphabet) getTargetAlphabet(), tokens.length);
        String word, label, pos, chunk;
        for (int i = 0; i < tokens.length; i++) {
            if (tokens[i].length() != 0) {
                String[] features = tokens[i].split("\t");
                if (features.length != 2) throw new IllegalStateException("Line \"" + tokens[i] + "\" doesn't have 2 elements");
                word = features[0];
                label = features[1];
            } else {
                word = "-<S>-";
                label = "O";
            }
            Token token = new Token(word);
            data.add(token);
            target.add(label);
        }
        carrier.setData(data);
        carrier.setTarget(target);
        return carrier;
    }
}
