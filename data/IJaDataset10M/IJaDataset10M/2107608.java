package edu.princeton.wordnet.pojos.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import edu.princeton.wordnet.pojos.Antonym;
import edu.princeton.wordnet.pojos.CasedWord;
import edu.princeton.wordnet.pojos.Hypernym;
import edu.princeton.wordnet.pojos.Hyponym;
import edu.princeton.wordnet.pojos.LexLink;
import edu.princeton.wordnet.pojos.Morph;
import edu.princeton.wordnet.pojos.PosTaggedMorph;
import edu.princeton.wordnet.pojos.PosTaggedWord;
import edu.princeton.wordnet.pojos.Sample;
import edu.princeton.wordnet.pojos.SemLink;
import edu.princeton.wordnet.pojos.Sense;
import edu.princeton.wordnet.pojos.Synset;
import edu.princeton.wordnet.pojos.VerbFrame;
import edu.princeton.wordnet.pojos.VerbFrameSentence;
import edu.princeton.wordnet.pojos.Word;

/**
 * Default processor
 * 
 * @author <a href="mailto:bbou@ac-toulouse.fr">Bernard Bou</a>
 */
public class DefaultProcessorSet implements ProcessorSet {

    public Map<Class<?>, Processor<?>> theProcessorMap;

    public DefaultProcessorSet() {
        this.theProcessorMap = new HashMap<Class<?>, Processor<?>>();
        this.theProcessorMap.put(Word.class, new Processor<Word>() {

            @Override
            public void process(final Word thisWord) {
                DefaultProcessorSet.process(thisWord);
            }
        });
        this.theProcessorMap.put(Sense.class, new Processor<Sense>() {

            @Override
            public void process(final Sense thisSense) {
                DefaultProcessorSet.process(thisSense);
            }
        });
        this.theProcessorMap.put(Synset.class, new Processor<Synset>() {

            @Override
            public void process(final Synset thisSynset) {
                DefaultProcessorSet.process(thisSynset);
            }
        });
        this.theProcessorMap.put(Morph.class, new Processor<Morph>() {

            @Override
            public void process(final Morph thisMorph) {
                DefaultProcessorSet.process(thisMorph);
            }
        });
    }

    @Override
    public Processor<?> getProcessor(final Class<?> thisClass) {
        if (!this.theProcessorMap.containsKey(thisClass)) {
            System.err.println(thisClass);
            throw new NoSuchElementException(thisClass.getName());
        }
        return this.theProcessorMap.get(thisClass);
    }

    static class Utils {

        /**
		 * Get words in synset
		 * 
		 * @param thisSynset
		 *            synset
		 * @return array of lemma strings
		 */
        static String[] getWords(final Synset thisSynset) {
            final List<String> theseWords = new ArrayList<String>();
            for (final Sense thisSense : thisSynset.getSenses()) {
                theseWords.add(thisSense.getWord().getLemma());
            }
            return theseWords.toArray(new String[theseWords.size()]);
        }

        /**
		 * Get morphological forms for word
		 * 
		 * @param thisWord
		 *            word
		 * @return array of morphological strings
		 */
        static String[] getMorphs(final Word thisWord) {
            final List<String> theseMorphs = new ArrayList<String>();
            for (final PosTaggedMorph thisMorph : thisWord.getPosTaggedMorphs()) {
                final String m = String.format("%s(%s)", thisMorph.getMorph().getMorphString(), thisMorph.getPos().getName());
                theseMorphs.add(m);
            }
            return theseMorphs.toArray(new String[theseMorphs.size()]);
        }

        /**
		 * Flatten array of strings to one string
		 * 
		 * @param theseStrings
		 *            array of strings
		 * @return string
		 */
        static String array2String(final String[] theseStrings) {
            final StringBuffer thisBuffer = new StringBuffer();
            int i = 0;
            for (final String thisString : theseStrings) {
                if (i++ > 0) {
                    thisBuffer.append(' ');
                }
                thisBuffer.append(thisString);
            }
            return thisBuffer.toString();
        }

        /**
		 * Represent word as string
		 * 
		 * @param thisWord
		 *            word
		 * @return string
		 */
        @SuppressWarnings("boxing")
        static String word2String(final Word thisWord) {
            return String.format("%s #%d [%s]", thisWord.getLemma(), thisWord.getId(), Utils.array2String(Utils.getMorphs(thisWord)));
        }

        /**
		 * Represent synset as string
		 * 
		 * @param thisSynset
		 *            synset
		 * @return string
		 */
        @SuppressWarnings("boxing")
        static String synset2String(final Synset thisSynset) {
            return String.format("%s %s #%d {%s} '%s'", thisSynset.getPos().getName(), thisSynset.getLexDomain().getName(), thisSynset.getId(), Utils.array2String(Utils.getWords(thisSynset)), thisSynset.getDefinition());
        }

        /**
		 * Utils semantic link (x R y)
		 * 
		 * @param thisSemlink
		 *            semantic link R
		 * @return string representing link
		 */
        @SuppressWarnings("boxing")
        static String semLink2String(final SemLink thisSemlink) {
            final Synset thisSynset = thisSemlink.getDestSynset();
            return String.format("%s [%d]- %s", thisSemlink.getClass().getSimpleName(), thisSemlink.getId().getLinkId(), Utils.synset2String(thisSynset));
        }

        /**
		 * Utils lexical link (x R y)
		 * 
		 * @param thisLexlink
		 *            lexical link R
		 * @return string representing link
		 */
        @SuppressWarnings("boxing")
        static String lexLink2String(final LexLink thisLexlink) {
            final Synset thisSynset = thisLexlink.getDestSynset();
            final Word thisWord = thisLexlink.getDestWord();
            return String.format("%s %s - %s %s", thisLexlink.getClass().getSimpleName(), thisLexlink.getId().getLinkId(), thisWord.getLemma(), Utils.synset2String(thisSynset));
        }

        /**
		 * Utils semantic link (x R y)
		 * 
		 * @param thisSemlink
		 *            semantic link R
		 * @param processTarget
		 *            whether to process link's target (y) or source (x)
		 * @return string representing link
		 */
        @SuppressWarnings("boxing")
        static String semLink2String(final SemLink thisSemlink, final boolean processTarget) {
            final Synset thisSynset = processTarget ? thisSemlink.getDestSynset() : thisSemlink.getSrcSynset();
            return String.format("%s %s - %s (%s %c %c)", (processTarget ? "<" : ">"), thisSemlink.getLink().getName(), Utils.synset2String(thisSynset), thisSemlink.getClass().getSimpleName(), thisSemlink instanceof Hypernym ? 'H' : '-', thisSemlink instanceof Hyponym ? 'h' : '-');
        }

        /**
		 * Utils lexical link (x R y)
		 * 
		 * @param thisLexlink
		 *            lexical link R
		 * @param processTarget
		 *            whether to process link's target (y) or source (x)
		 * @return string representing link
		 */
        static String lexLink2String(final LexLink thisLexlink, final boolean processTarget) {
            final Synset thisSynset = processTarget ? thisLexlink.getDestSynset() : thisLexlink.getSrcSynset();
            final Word thisWord = processTarget ? thisLexlink.getDestWord() : thisLexlink.getSrcWord();
            return String.format("%s %s - %s %s", (processTarget ? "<" : ">"), thisLexlink.getLink().getName(), thisWord.getLemma(), Utils.synset2String(thisSynset));
        }
    }

    public static void process(final Word thisWord) {
        System.out.printf("%s\n", Utils.word2String(thisWord));
        final Set<CasedWord> cws = thisWord.getCasedWords();
        for (final CasedWord cw : cws) {
            System.out.printf("cased=%s uncased=%s\n", cw.getCasedString(), cw.getWord().getLemma());
        }
        final Set<Sense> theseSenses = thisWord.getSenses();
        if (theseSenses != null) {
            for (final Sense thisSense : theseSenses) {
                DefaultProcessorSet.process(thisSense);
            }
        }
    }

    @SuppressWarnings("boxing")
    public static void process(final Sense thisSense) {
        System.out.printf("*sense=%d,%d ", thisSense.getWord().getId(), thisSense.getSynset().getId());
        System.out.printf("senseid=%d lexid=%d sensekey=%s sensenum=%s tagcount=%s", thisSense.getSenseId(), thisSense.getLexId(), thisSense.getSenseKey(), thisSense.getSenseNum(), thisSense.getTagCount());
        final CasedWord cw = thisSense.getCasedWord();
        if (cw != null) {
            System.out.printf("cased=%s ", cw.getCasedString());
        }
        if (thisSense.getAdjPosition() != null) {
            System.out.printf("adjposition=%s ", thisSense.getAdjPosition().getName());
        }
        System.out.println();
        final Synset sy = thisSense.getSynset();
        DefaultProcessorSet.process(sy);
        for (final LexLink thisAntonym : thisSense.getLexLinksAsSource()) {
            System.out.printf('\t' + "antonym=%s %s\n", thisAntonym.getDestWord().getLemma(), Utils.lexLink2String(thisAntonym));
        }
        System.out.println("-source (from sense)-");
        final Set<LexLink> lls1 = thisSense.getLexLinksAsSource();
        for (final LexLink ll1 : lls1) {
            System.out.println('\t' + Utils.lexLink2String(ll1, true));
        }
        System.out.println("-dest (from sense)-");
        final Set<LexLink> lls2 = thisSense.getLexLinksAsDest();
        for (final LexLink ll2 : lls2) {
            System.out.println('\t' + Utils.lexLink2String(ll2, false));
        }
        for (final Antonym thisAntonym : thisSense.getAntonyms()) {
            System.out.printf('\t' + "antonym=%s %s\n", thisAntonym.getDestWord().getLemma(), Utils.lexLink2String(thisAntonym));
        }
        for (final VerbFrame vf : thisSense.getVerbFrames()) {
            System.out.printf('\t' + "verbframe=[%d] %s\n", vf.getId(), vf.getFrame());
        }
        for (final VerbFrameSentence vf : thisSense.getVerbFrameSentences()) {
            System.out.printf('\t' + "sentence [%d] %s\n", vf.getId(), vf.getSentence());
        }
    }

    public static void process(final Synset thisSynset) {
        System.out.println(Utils.synset2String(thisSynset));
        for (final Sample thisSample : thisSynset.getSamples()) {
            System.out.printf("-eg:%s\n", thisSample.getSampleString());
        }
        System.out.println("-hyper-");
        final Set<Hypernym> hypers = thisSynset.getHypernyms();
        for (final Hypernym sl1 : hypers) {
            System.out.println('\t' + Utils.semLink2String(sl1));
        }
        System.out.println("-hypo-");
        final Set<Hyponym> hypos = thisSynset.getHyponyms();
        for (final Hyponym sl1 : hypos) {
            System.out.println('\t' + Utils.semLink2String(sl1));
        }
        System.out.println("-source-");
        final Set<SemLink> sls1 = thisSynset.getSemLinksAsSource();
        for (final SemLink sl1 : sls1) {
            System.out.println('\t' + Utils.semLink2String(sl1, true));
        }
        final Set<LexLink> lls1 = thisSynset.getLexLinksAsSource();
        for (final LexLink ll1 : lls1) {
            System.out.println('\t' + Utils.lexLink2String(ll1, true));
        }
        System.out.println("-dest-");
        final Set<SemLink> sls2 = thisSynset.getSemLinksAsDest();
        for (final SemLink sl2 : sls2) {
            System.out.println('\t' + Utils.semLink2String(sl2, false));
        }
        final Set<LexLink> lls2 = thisSynset.getLexLinksAsDest();
        for (final LexLink ll2 : lls2) {
            System.out.println('\t' + Utils.lexLink2String(ll2, false));
        }
    }

    public static void process(final Morph thisMorph) {
        if (thisMorph == null) {
            System.out.printf("morph=null\n");
            return;
        }
        System.out.printf("morph=%s\n", thisMorph.getMorphString());
        final Set<PosTaggedWord> theseWords = thisMorph.getPosTaggedWords();
        for (final PosTaggedWord thisWord : theseWords) {
            System.out.printf(" %s %s\n", thisWord.getWord().getLemma(), thisWord.getPos().getName());
        }
    }
}
