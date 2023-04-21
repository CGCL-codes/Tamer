package org.corrib.jonto.wordnet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.dictionary.Dictionary;
import org.corrib.jonto.Classification;
import org.openrdf.sesame.config.AccessDeniedException;

/**
 * Represents the WordNet classification item
 * 
 * @author Tomek Woroniecki, Sebastian Ryszard Kruk,
 * @created 16.08.2005
 */
public class WordNetClassification implements Classification {

    /**
	 * Default logger for this package 
	 */
    private static final Logger logger = Logger.getLogger("org.corrib.jonto.beans");

    /**
	 * Represents the unique sense of the word net classification entry
	 */
    AbstractMeaning wordSense = null;

    /**
	 * URI representing this classification
	 */
    String uri = null;

    /**
	 * Single lemma describing this classification
	 */
    String name = null;

    /**
	 * 
	 */
    public WordNetClassification(Synset _wordSense) {
        this.wordSense = new SynsetWord(_wordSense);
    }

    public WordNetClassification(AbstractMeaning _meaning) {
        this.wordSense = _meaning;
    }

    public WordNetClassification(String _word, Locale lang) throws NotRecognizedSynsetException, AccessDeniedException {
        this.wordSense = LocalizedWord.createLocalizedWord(_word, lang, null);
    }

    public String getURI() {
        synchronized (this) {
            if (uri == null) {
                uri = JOntoWordNetOntology.WNO_NS_WN_OFFSET.toString() + PartOfSpeech.getByPOS(this.wordSense.getPOS()).getEnc() + this.wordSense.getOffset();
            }
        }
        return uri;
    }

    public String getName() {
        synchronized (this) {
            if (name == null) {
                name = this.wordSense.getName();
            }
        }
        return name;
    }

    /**
	 * @return Description of the classification
	 */
    public String getDescription() {
        return this.wordSense.getGloss();
    }

    /**
	 * Factory for creating objects of this class.
	 * @throws AccessDeniedException 
	 * @throws NotRecognizedSynsetException 
	 */
    public static WordNetClassification getWordNet(String uri) throws NotRecognizedSynsetException, AccessDeniedException {
        JOntoWord.initWN();
        WordNetClassification result = null;
        String enc = uri.substring(JOntoWordNetOntology.WNO_NS_WN_OFFSET.toString().length(), JOntoWordNetOntology.WNO_NS_WN_OFFSET.toString().length() + PartOfSpeech.getEncLength());
        String offsite = uri.substring(JOntoWordNetOntology.WNO_NS_WN_OFFSET.toString().length() + PartOfSpeech.getEncLength());
        Synset synset = null;
        try {
            synset = Dictionary.getInstance().getSynsetAt(PartOfSpeech.getByEnc(enc).getPos(), Long.valueOf(offsite));
            result = new WordNetClassification(synset);
        } catch (NumberFormatException e) {
            result = new WordNetClassification(LocalizedWord.createLocalizedWord(uri));
        } catch (JWNLException e) {
            result = new WordNetClassification(LocalizedWord.createLocalizedWord(uri));
        }
        return result;
    }

    /**
	 * Returns a list of possible word net classfications for the given word
	 * 
	 * @param _word
	 * @return
	 */
    public static Collection<WordNetClassification> getWordNetClassifications(String _word) {
        Collection<WordNetClassification> result = new ArrayList<WordNetClassification>();
        JOntoWord w = new JOntoWord(_word);
        for (PartOfSpeech pos : PartOfSpeech.values()) {
            IndexWord iw = w.getIndexWord(pos);
            if (iw != null) {
                try {
                    for (Synset sens : iw.getSenses()) {
                        WordNetClassification wnc = new WordNetClassification(sens);
                        result.add(wnc);
                    }
                } catch (JWNLException e) {
                    logger.log(Level.SEVERE, "Error with getting senses for " + iw, e);
                }
            }
        }
        return result;
    }

    /**
	 * @return Returns the wordSense.
	 */
    public Synset getWordSense() {
        return wordSense.getSynset();
    }
}
