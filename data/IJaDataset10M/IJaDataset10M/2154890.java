package org.apache.lucene.analysis;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import lia.analysis.LuceneTagger;
import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import fr.crim.lucene.hunspell.Lexicon;

/**
 * TokenFilter that uses hunspell affix rules and words to stem tokens.  Since hunspell supports a word having multiple
 * stems, this filter can emit multiple tokens for each consumed token
 * 
 * TODO, tester en lucene 3.5
 * 
 * see http://svn.apache.org/repos/asf/lucene/dev/trunk/modules/analysis/common/src/java/org/apache/lucene/analysis/synonym/SynonymFilter.java
 */
public final class HunspellFilter extends TokenFilter {

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

    private final PositionIncrementAttribute posIncAtt = addAttribute(PositionIncrementAttribute.class);

    /** le type du token en cours */
    private TypeAttribute typeAtt = (TypeAttribute) addAttribute(TypeAttribute.class);

    /** Garder mémoire de la ponctuation de fin de phrase, commencer comme début de phrase  */
    boolean phrase = true;

    /** lexique capable de lemmatiser */
    private final Lexicon lexicon;

    /** forme en cours */
    private StringBuffer form = new StringBuffer();

    /** lemmes à insérer */
    private String[] lemmas;

    /** stem en cours à insérer */
    String stem;

    /** Iterateur sur le tableau */
    private int i;

    /** État */
    private State savedState;

    /** mode */
    private int mode;

    /** Mode 1 seule forme (pour clé de tri) */
    public static final int UNIQ = 1;

    /** Clé phonétique d'un mot */
    public static final int PHONE = 2;

    /**
	 * {@inheritDoc}
	 */
    public HunspellFilter(TokenStream input, Lexicon lexicon, int mode) {
        super(input);
        this.lexicon = lexicon;
        this.mode = mode;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public boolean incrementToken() throws IOException {
        if (lemmas != null && this.i < lemmas.length) {
            restoreState(savedState);
            posIncAtt.setPositionIncrement(0);
            termAtt.append(lemmas[i]);
            i++;
            return true;
        }
        if (!input.incrementToken()) {
            return false;
        }
        if (!typeAtt.type().equals("word")) {
            if (typeAtt.type().equals("S")) phrase = true;
            return true;
        }
        form.setLength(0);
        form.append(termAtt.buffer());
        form.setLength(termAtt.length());
        if (this.mode == PHONE) {
            stem = lexicon.oconv(lexicon.phone(lexicon.iconv(form.toString())));
            termAtt.setEmpty();
            termAtt.append(stem);
            return true;
        }
        lemmas = lexicon.lemmas(form.toString());
        this.i = 0;
        if (this.mode == UNIQ) {
            if (lemmas.length < 1) {
            } else {
                stem = lemmas[0];
                termAtt.setEmpty();
                termAtt.append(stem);
                typeAtt.setType("DIC");
            }
            lemmas = null;
            return true;
        }
        if (lemmas.length < 1) {
            lemmas = new String[] { lexicon.stem(form.toString()) };
        }
        savedState = captureState();
        return true;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void reset() throws IOException {
        super.reset();
        form.setLength(0);
        lemmas = null;
    }

    /** Tests */
    public static void main(String[] args) throws Exception {
        String text = "Karolus etc., universis justiciariis nostris aut eorum locatenentibus, salutem. Si, vocatis evocandis, vobis aut vestrum alteri constiterit quod major pars creditorum Talis in numero creditorum et cumulo debitorum ad dandum ei quinquennalem dilacionem de suis debitis solvendis pro evitenda miserabili cessione bonorum suorum consenserit sine fraude, mandamus vobis et vestrum cuilibet, prout ad eum pertinuerit, quatinus minorem partem creditorum suorum, habita consideracione et respectu ad premissa, ad dandum ei dilacionem consimilem et ejus fidejussoribus quomodolibet obligatis de debitis hujusmodi solvendis prout justum fuerit compellatis seu compelli faciatis ; et si quid in contrarium factum vel attemptatum aut de bonis suis captum vel arrestatum fuerit, id ad statum pristinum et debitum reducatis seu reduci faciatis indilate. Obligacionibus et renunciacionibus fide et juramento mediantibus vallatis, dum tamen a prelato suo aut alio super hoc potestatem habente de hujusmodi fide et juramento dispensacionem obtinuerit, ac litteris surrepticiis in contrarium impetratis vel impetrandis non obstantibus quibuscumque, nostris ac nundinarum Campanie et Briea debitis dumtaxat exceptis. Datum etc.";
        final Lexicon lexicon = new Lexicon(new File("rsc/lat.aff"), new File("rsc/lat.dic"));
        Analyzer analyzer = new Analyzer() {

            public TokenStream tokenStream(String fieldName, Reader reader) {
                return new HunspellFilter(new PunctuationTokenizer(reader), lexicon, HunspellFilter.UNIQ);
            }
        };
        System.out.println(text);
        new LuceneTagger().ponctuer(analyzer, text, null);
        new LuceneTagger().displayTokensWithFullDetails(analyzer, text);
    }
}
