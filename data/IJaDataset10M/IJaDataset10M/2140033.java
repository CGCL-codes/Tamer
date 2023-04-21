package org.corrib.jonto.wordnet;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.DictionaryElementType;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Pointer;
import net.didion.jwnl.data.PointerTarget;
import net.didion.jwnl.data.PointerType;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.data.Word;
import net.didion.jwnl.dictionary.Dictionary;
import org.corrib.jonto.beans.Sha1sum;
import org.corrib.jonto.db.rdf.SesameWrapper;
import org.openrdf.model.Graph;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.sesame.config.AccessDeniedException;
import org.openrdf.sesame.repository.local.LocalRepository;
import org.openrdf.sesame.sail.StatementIterator;
import org.openrdf.sesame.sailimpl.memory.LiteralNode;

/**
 * @author skruk
 *
 */
public class LocalizedWord implements AbstractMeaning {

    /**
	 * Default logger for this package 
	 */
    private static final Logger logger = Logger.getLogger("org.corrib.jonto.beans");

    /**
	 * Word wrapped by this local word
	 */
    String word;

    /**
	 * Actual meaning of the word
	 */
    SynsetWord meaning = null;

    /**
	 * In WordNet we have offset, but for generic vocabulary HASH function will do better
	 */
    String hashValue;

    /**
	 * Language in which this word is localized
	 */
    Locale lang;

    /**
	 * URI of this resources in the database
	 */
    String uri = null;

    static LocalRepository repository = SesameWrapper.getInstance().getRepository();

    /**
	 * 
	 */
    protected LocalizedWord(String _word, Locale _lang, SynsetWord _meaning) {
        this(_word, _lang);
        this.meaning = _meaning;
    }

    protected LocalizedWord(String _word, Locale _lang) {
        this.word = _word;
        this.lang = _lang;
        this.hashValue = Sha1sum.getInstance().calc(_word + "@" + _lang);
    }

    /**
	 * Creates new localized word or retrieves on from RDF store
	 * 
	 * @param _word
	 * @param _lang
	 * @param _meaning
	 * @return
	 * @throws AccessDeniedException 
	 * @throws NotRecognizedSynsetException 
	 */
    public static AbstractMeaning createLocalizedWord(String _word, Locale _lang, Synset _meaning) {
        String hash = Sha1sum.getInstance().calc(_word + "@" + _lang);
        String uri = JOntoWordNetOntology.WNO_NS_I18N_WORDNET + hash;
        AbstractMeaning result = null;
        try {
            result = LocalizedWord.createLocalizedWord(uri);
        } catch (NotRecognizedSynsetException e) {
            logger.fine("Getting WordNet from URI not successfull" + e);
        } catch (AccessDeniedException e) {
            logger.fine("Getting WordNet from URI not successfull" + e);
        }
        if (result == null) {
            result = (_meaning != null) ? new LocalizedWord(_word, _lang, new SynsetWord(_meaning)) : new LocalizedWord(_word, _lang);
            SesameWrapper sw = SesameWrapper.getInstance();
            String word = "\"" + _word + "\"" + "@" + _lang;
            sw.addStatement(uri, JOntoWordNetOntology.WNO_HAS_WORD.toString(), word, repository);
            if (_meaning != null) {
                String uriSynset = JOntoWordNetOntology.WNO_NS_WN_OFFSET + PartOfSpeech.getByPOS(_meaning.getPOS()).getEnc() + _meaning.getOffset();
                sw.addStatement(uri, JOntoWordNetOntology.WNO_HAS_MEANING.toString(), uriSynset, repository);
            }
        }
        return result;
    }

    /**
	 * Allows to create a localized word based on the given URI, word and lang. If it is possible recreates synset information from WordNet
	 * @param uri
	 * @param word
	 * @param lang
	 * @param called
	 * @return
	 * @throws AccessDeniedException
	 * @throws NotRecognizedSynsetException 
	 */
    public static AbstractMeaning createLocalizedWord(String uri, String word, Locale lang) throws AccessDeniedException, NotRecognizedSynsetException {
        SesameWrapper sw = SesameWrapper.getInstance();
        AbstractMeaning result = null;
        Graph g = sw.loadPureRdf(uri, repository);
        if (g == null || g.getStatements().hasNext()) {
            Synset synset = null;
            try {
                int offlen = JOntoWordNetOntology.WNO_NS_WN_OFFSET.toString().length();
                String enc = uri.substring(offlen, offlen + PartOfSpeech.getEncLength());
                String offsite = uri.substring(offlen + PartOfSpeech.getEncLength());
                synset = Dictionary.getInstance().getSynsetAt(PartOfSpeech.getByEnc(enc).getPos(), Long.valueOf(offsite));
            } catch (NumberFormatException e) {
                logger.log(Level.SEVERE, "Error with translating URI=" + uri, e);
            } catch (JWNLException e) {
                logger.log(Level.SEVERE, "Error with translating URI=" + uri, e);
            }
            ValueFactory vf = repository.getGraph().getValueFactory();
            Resource subject = vf.createURI(uri);
            URI hasWord = vf.createURI(JOntoWordNetOntology.WNO_HAS_WORD.toString());
            Value vword = vf.createLiteral(word, lang.toString());
            sw.addStatement(subject, hasWord, vword, repository);
            result = (synset != null) ? new LocalizedWord(word, lang, new SynsetWord(synset)) : new LocalizedWord(word, lang);
        } else result = createLocalizedWord(uri);
        return result;
    }

    /**
	 * Attempts to retrive word from RDF storage
	 * 
	 * @param uri
	 * @return
	 * @throws NotRecognizedSynsetException 
	 * @throws AccessDeniedException 
	 */
    public static AbstractMeaning createLocalizedWord(String uri) throws NotRecognizedSynsetException, AccessDeniedException {
        SesameWrapper sw = SesameWrapper.getInstance();
        AbstractMeaning result = null;
        Graph g = sw.loadPureRdf(uri, repository);
        if (g != null && g.getStatements().hasNext()) {
            Resource resource = g.getValueFactory().createURI(uri);
            URI uriWord = g.getValueFactory().createURI(JOntoWordNetOntology.WNO_HAS_WORD.toString());
            String word = null;
            Locale lang = null;
            Synset synset = null;
            StatementIterator stmtit = g.getStatements(resource, uriWord, null);
            if (stmtit.hasNext()) {
                Value vword = stmtit.next().getObject();
                if (vword != null && vword instanceof LiteralNode) {
                    word = ((LiteralNode) vword).getLabel();
                    lang = new Locale(((LiteralNode) vword).getLanguage());
                }
            }
            int offlen = JOntoWordNetOntology.WNO_NS_WN_OFFSET.toString().length();
            String enc = uri.substring(offlen, offlen + PartOfSpeech.getEncLength());
            String offsite = uri.substring(offlen + PartOfSpeech.getEncLength());
            try {
                synset = Dictionary.getInstance().getSynsetAt(PartOfSpeech.getByEnc(enc).getPos(), Long.valueOf(offsite));
            } catch (NumberFormatException e) {
                logger.fine("Error with translating URI=" + uri);
            } catch (JWNLException e) {
                logger.fine("Error with translating URI=" + uri);
            } catch (NullPointerException npe) {
                logger.fine("Error with translating URI=" + uri);
            }
            result = (synset != null) ? new LocalizedWord(word, lang, new SynsetWord(synset)) : new LocalizedWord(word, lang);
        } else {
            Synset synset = null;
            try {
                int offlen = JOntoWordNetOntology.WNO_NS_WN_OFFSET.toString().length();
                String enc = uri.substring(offlen, offlen + PartOfSpeech.getEncLength());
                String offsite = uri.substring(offlen + PartOfSpeech.getEncLength());
                synset = Dictionary.getInstance().getSynsetAt(PartOfSpeech.getByEnc(enc).getPos(), Long.valueOf(offsite));
            } catch (NumberFormatException e) {
                logger.fine("Error with translating URI=" + uri);
            } catch (JWNLException e) {
                logger.fine("Error with translating URI=" + uri);
            } catch (NullPointerException npe) {
                logger.fine("Error with translating URI=" + uri);
            }
            if (synset != null) {
                ValueFactory vf = repository.getGraph().getValueFactory();
                Resource subject = vf.createURI(uri);
                URI hasWord = vf.createURI(JOntoWordNetOntology.WNO_HAS_WORD.toString());
                Value vword = vf.createLiteral(synset.getWord(0).getLemma(), Locale.ENGLISH.toString());
                sw.addStatement(subject, hasWord, vword, repository);
                result = new SynsetWord(synset);
            } else throw new NotRecognizedSynsetException(uri);
        }
        return result;
    }

    public String getName() {
        return this.word;
    }

    /**
	 * @return Returns the meaning.
	 */
    public SynsetWord getMeaning() {
        return meaning;
    }

    /**
	 * @param meaning The meaning to set.
	 */
    public void setMeaning(SynsetWord _meaning) {
        this.meaning = _meaning;
    }

    /**
	 * @return Returns the word.
	 */
    public String getWord() {
        return word;
    }

    /**
	 * @param word The word to set.
	 */
    public void setWord(String _word) {
        this.word = _word;
    }

    public boolean containsWord(String arg0) {
        return (meaning != null) ? meaning.containsWord(arg0) : false;
    }

    public boolean equals(Object arg0) {
        boolean result = false;
        if (arg0 instanceof String && arg0.equals(this.word)) return true; else if (!result && this.meaning != null) {
            return meaning.equals(arg0);
        } else return false;
    }

    public String getGloss() {
        return (meaning != null) ? meaning.getGloss() : this.word;
    }

    public Object getKey() {
        return (meaning != null) ? meaning.getKey() : null;
    }

    public String getOffset() {
        return (meaning != null) ? String.valueOf(meaning.getOffset()) : this.hashValue;
    }

    public Pointer[] getPointers() {
        return (meaning == null) ? null : meaning.getPointers();
    }

    public Pointer[] getPointers(PointerType arg0) {
        return (meaning == null) ? null : meaning.getPointers(arg0);
    }

    public POS getPOS() {
        return (meaning == null) ? POS.NOUN : meaning.getPOS();
    }

    public Synset getSynset() {
        return (meaning == null) ? null : meaning.getSynset();
    }

    public PointerTarget[] getTargets() throws JWNLException {
        return (meaning == null) ? null : meaning.getTargets();
    }

    public PointerTarget[] getTargets(PointerType arg0) throws JWNLException {
        return (meaning == null) ? null : meaning.getTargets(arg0);
    }

    public DictionaryElementType getType() {
        return (meaning == null) ? null : meaning.getType();
    }

    public BitSet getVerbFrameFlags() {
        return (meaning == null) ? null : meaning.getVerbFrameFlags();
    }

    public int[] getVerbFrameIndicies() {
        return (meaning == null) ? null : meaning.getVerbFrameIndicies();
    }

    public String[] getVerbFrames() {
        return (meaning == null) ? null : meaning.getVerbFrames();
    }

    public Word getWord(int arg0) {
        return (meaning == null) ? null : meaning.getWord(arg0);
    }

    public Word[] getWords() {
        return (meaning == null) ? null : meaning.getWords();
    }

    public int getWordsSize() {
        return (meaning == null) ? 0 : meaning.getWordsSize();
    }

    public boolean isAdjectiveCluster() {
        return (meaning == null) ? false : meaning.isAdjectiveCluster();
    }

    public void setSynset(Synset _synset) {
        if (meaning == null) meaning = new SynsetWord(_synset);
    }

    public String toString() {
        return this.word + "@" + this.lang;
    }

    public static void main(String[] args) {
        AbstractMeaning lw = null;
        AbstractMeaning lw2 = null;
        AbstractMeaning wn1 = null;
        AbstractMeaning wn2 = null;
        try {
            List<SynsetWord> words = new ArrayList<SynsetWord>(JOntoWord.findMatchingWords("house"));
            System.out.println(LocalizedWord.createLocalizedWord("http://www.cogsci.princeton.edu/~wn/concept#NOUN2906118"));
            lw = LocalizedWord.createLocalizedWord("test", new Locale("pl"), null);
            lw2 = LocalizedWord.createLocalizedWord(JOntoWordNetOntology.WNO_NS_I18N_WORDNET + "3d7e104f86b5893a9450c929e6143c5a097c3474");
            System.out.println(">" + lw.toString());
            System.out.println(">" + lw2.toString());
        } catch (NotRecognizedSynsetException e) {
            e.printStackTrace();
        } catch (AccessDeniedException e) {
            e.printStackTrace();
        } catch (JWNLException e) {
            e.printStackTrace();
        }
    }

    public String getURI() {
        synchronized (this) {
            if (uri == null) if (this.meaning == null) uri = JOntoWordNetOntology.WNO_NS_I18N_WORDNET.toString() + this.hashValue; else uri = this.meaning.getURI();
        }
        return uri;
    }

    public Locale getLang() {
        return lang;
    }

    /**
	 * Initializing WordNet
	 */
    static {
        JOntoWord.initWN();
    }
}
