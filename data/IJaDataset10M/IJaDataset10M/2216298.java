package is.iclt.icenlp.core.tritagger;

import is.iclt.icenlp.core.tokenizer.TokenTags;
import is.iclt.icenlp.core.tokenizer.Segmentizer;

/**
 * Generates the output for TriTagger.
 * @author Hrafn Loftsson
 */
public class TriTaggerOutput {

    protected int outputFormat = Segmentizer.tokenPerLine;

    private static final String unknownStr = "<UNKNOWN>";

    public TriTaggerOutput(int outFormat) {
        outputFormat = outFormat;
    }

    public String buildOutput(TokenTags tok, int index, int numTokens) {
        String str;
        if (outputFormat == Segmentizer.tokenPerLine) str = tok.lexeme + " " + tok.getFirstTagStr(); else {
            str = tok.lexeme + " " + tok.getFirstTagStr();
            if (index < numTokens - 1) str = str + " ";
        }
        if (tok.isUnknown()) {
            if (outputFormat == Segmentizer.tokenPerLine) str = str + " " + unknownStr;
        }
        return str;
    }
}
