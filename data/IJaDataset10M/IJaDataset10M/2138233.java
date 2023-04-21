package org.apache.lucene.analysis;

import org.apache.commons.codec.Encoder;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.codec.language.Metaphone;
import org.apache.commons.codec.language.RefinedSoundex;
import org.apache.commons.codec.language.Soundex;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.util.Version;
import java.io.IOException;
import java.io.Reader;
import lia.analysis.LuceneTagger;

/**
 * Create tokens for phonetic matches. See:
 * http://jakarta.apache.org/commons/codec
 * /api-release/org/apache/commons/codec/language/package-summary.html
 * 
 * @version $Id: PhoneticFilter.java 804726 2009-08-16 17:28:58Z yonik $
 */
public class PhoneticFilter extends TokenFilter {

    /** Terme courant */
    private final CharTermAttribute term = addAttribute(CharTermAttribute.class);

    /** Position */
    private final PositionIncrementAttribute pos = addAttribute(PositionIncrementAttribute.class);

    protected boolean inject = true;

    protected Encoder encoder = null;

    protected String name = null;

    protected State save = null;

    public PhoneticFilter(TokenStream in, Encoder encoder, boolean inject) {
        super(in);
        this.encoder = encoder;
        this.name = encoder.getClass().getName();
        this.inject = inject;
    }

    @Override
    public boolean incrementToken() throws IOException {
        if (save != null) {
            restoreState(save);
            save = null;
            return true;
        }
        if (!input.incrementToken()) return false;
        if (term.length() == 0) return true;
        String value = term.toString();
        String phonetic = null;
        try {
            String v;
            v = encoder.encode(value).toString().replace(' ', 'a');
            if (v.length() > 0 && !value.equals(v)) phonetic = v;
        } catch (Exception ignored) {
        }
        if (phonetic == null) return true;
        if (!inject) {
            term.setEmpty();
            term.append(phonetic);
            return true;
        }
        int origOffset = pos.getPositionIncrement();
        pos.setPositionIncrement(0);
        save = captureState();
        pos.setPositionIncrement(origOffset);
        term.setEmpty();
        term.append(phonetic);
        return true;
    }

    @Override
    public void reset() throws IOException {
        input.reset();
        save = null;
    }

    public static void main(String[] args) throws IOException {
        LuceneTagger util = new LuceneTagger();
        final String text = "Tester la phonétisation, décidément pas faîte pour le français. Cayassé, caillasser.";
        Analyzer analyzer = new Analyzer() {

            public TokenStream tokenStream(String fieldName, Reader reader) {
                return new PhoneticFilter(new LetterTokenizer(Version.LUCENE_35, reader), new DoubleMetaphone(), true);
            }
        };
        util.displayTokensWithPositions(analyzer, text);
        analyzer = new Analyzer() {

            public TokenStream tokenStream(String fieldName, Reader reader) {
                return new PhoneticFilter(new LetterTokenizer(Version.LUCENE_35, reader), new Metaphone(), true);
            }
        };
        util.displayTokensWithPositions(analyzer, text);
        analyzer = new Analyzer() {

            public TokenStream tokenStream(String fieldName, Reader reader) {
                return new PhoneticFilter(new ASCIIFoldingFilter(new PunctuationTokenizer(reader)), new Soundex(), true);
            }
        };
        util.displayTokensWithPositions(analyzer, text);
        analyzer = new Analyzer() {

            public TokenStream tokenStream(String fieldName, Reader reader) {
                return new PhoneticFilter(new ASCIIFoldingFilter(new PunctuationTokenizer(reader)), new RefinedSoundex(), false);
            }
        };
        util.displayTokensWithPositions(analyzer, text);
    }
}
