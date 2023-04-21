package jgrail.lexicon;

public class Lexeme {

    private String word;

    private SemanticClause sem;

    private SyntacticClause syn;

    public Lexeme(String lexeme) {
        parseLexemeString(lexeme);
    }

    private void parseLexemeString(String lexeme) {
    }

    /**
	 * @param word
	 * @param syn
	 * @param sem
	 */
    public Lexeme(String word, SyntacticClause syn, SemanticClause sem) {
        this.word = word;
        this.sem = sem;
        this.syn = syn;
    }

    public String name() {
        return getWord();
    }

    /**
	 * @return the word
	 */
    public String getWord() {
        return word;
    }

    /**
	 * @return the sem
	 */
    public SemanticClause getSem() {
        return sem;
    }

    /**
	 * @return the syn
	 */
    public SyntacticClause getSyn() {
        return syn;
    }

    @Override
    public String toString() {
        return "lex(" + word + "," + syn.getTerm() + "," + sem.getTerm() + ").\n";
    }
}
