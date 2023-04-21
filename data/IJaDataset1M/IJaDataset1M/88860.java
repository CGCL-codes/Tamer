package relex.parser;

import org.linkgrammar.LinkGrammar;

/**
 * This LinkGrammar.class serves as a wrapper to the C linkparser library.
 * The LinkGrammar.class is not internally organized in a very good OOP way.
 * This should be fixed.
 *
 * This class is for use with link-grammar versions 4.2.6 and later
 */
public class LinkParserJNINewClient extends LinkParserClient {

    private static final int verbosity = 1;

    private static LinkParserJNINewClient singletonInstance = null;

    private LinkParserJNINewClient() {
        super();
    }

    static {
        System.loadLibrary("link-grammar");
        System.loadLibrary("link-grammar-java");
        singletonInstance = new LinkParserJNINewClient();
    }

    public static LinkParserJNINewClient getSingletonInstance() {
        return singletonInstance;
    }

    public void setMaxParseSeconds(int maxParseSeconds) {
        super.setMaxParseSeconds(maxParseSeconds);
        LinkGrammar.setMaxParseSeconds(maxParseSeconds);
    }

    public void setMaxCost(int maxCost) {
        super.setMaxCost(maxCost);
        LinkGrammar.setMaxCost(maxCost);
    }

    public boolean isPastTenseForm(String word) {
        return LinkGrammar.isPastTenseForm(word);
    }

    public boolean isEntity(String word) {
        return LinkGrammar.isEntity(word);
    }

    public void close() {
        LinkGrammar.close();
    }

    public void init(String pathname) {
        super.init(pathname);
        if (verbosity > 3) System.out.println("LinkParserJNINewClient: initializing.");
        LinkGrammar.init();
    }

    void execParse(String sentence) {
        if (verbosity > 3) System.out.println("parsing:" + sentence + "[end_sentence]");
        LinkGrammar.parse(sentence);
        if (verbosity > 3) System.out.println("parsing LinkGrammar.completed.");
    }

    int getNumLinkages() {
        return LinkGrammar.getNumLinkages();
    }

    void makeLinkage(int i) {
        LinkGrammar.makeLinkage(i);
    }

    String getConstituentString() {
        return LinkGrammar.getConstituentString();
    }

    int getNumSkippedWords() {
        return LinkGrammar.getNumSkippedWords();
    }

    int getNumWords() {
        return LinkGrammar.getNumWords();
    }

    String getWord(int w) {
        return LinkGrammar.getWord(w);
    }

    int getLinkageDisjunctCost() {
        return LinkGrammar.getLinkageDisjunctCost();
    }

    int getLinkageNumViolations() {
        return LinkGrammar.getLinkageNumViolations();
    }

    int getNumLinks() {
        return LinkGrammar.getNumLinks();
    }

    String getLinkString() {
        return LinkGrammar.getLinkString();
    }

    int getLinkLWord(int i) {
        return LinkGrammar.getLinkLWord(i);
    }

    int getLinkRWord(int i) {
        return LinkGrammar.getLinkRWord(i);
    }

    String getLinkLabel(int i) {
        return LinkGrammar.getLinkLabel(i);
    }

    String getLinkLLabel(int i) {
        return LinkGrammar.getLinkLLabel(i);
    }

    String getLinkRLabel(int i) {
        return LinkGrammar.getLinkRLabel(i);
    }
}
