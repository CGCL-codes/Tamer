package org.phramer.v1.decoder.extensionifs.impl;

import org.phramer.v1.decoder.extensionifs.*;
import org.phramer.v1.decoder.search.*;
import org.phramer.v1.decoder.*;

public class CustomProbabilityPharaohWordPenality extends CustomProbability {

    public CustomProbabilityPharaohWordPenality(PhramerConfig config, String param[]) {
        super(config, param);
    }

    public int getNoProbabilities() {
        return 1;
    }

    public synchronized int getProbability(double[] bufferOut, int offset, PhramerInput input, HypothesisState hyp, PhraseTranslationVariant tv, Object constraintObject, int pos, int len) {
        bufferOut[offset] = -tv.getTranslation().length;
        return 1;
    }

    public int getEstimatedProbability(double[] bufferOut, int offset, boolean[] foreignCovered, final int lastForeign) {
        bufferOut[offset] = 0;
        return 1;
    }

    public int getEstimatedProbability(double[] bufferOut, int offset, PhraseTranslationVariant tVar) {
        bufferOut[offset] = -tVar.getTranslation().length;
        return 1;
    }
}
