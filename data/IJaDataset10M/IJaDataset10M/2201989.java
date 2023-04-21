package my.usm.cs.utmk.blexisma2.struct;

/**
 * @author didier
 * 
 */
public final class Acception extends LexicalObject {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * @param id
	 *            the id of the lexical object.
	 * @param form
	 *            the word form
	 * @param hits
	 *            the number of hits of the lexical object
	 * @param morphology
	 *            the morphology of the lexical object
	 * @param etymology
	 *            the etymology of the lexical object
	 * @param CV
	 *            the conceptual vector of the lexical object
	 * @param source
	 *            the source
	 * @param glose
	 *            the glose of the lexical object
	 * 
	 * @see LexicalObject
	 */
    public Acception(int id, String form, int hits, String morphology, String etymology, ConceptualVector CV, String source, String glose, boolean randomCV) {
        super(id, form, hits, morphology, etymology, CV, source, glose, randomCV);
    }

    /**
	 * Alphanumeric representation method of an acception.
	 * 
	 * @return a string representation of the acception.
	 * 
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        return " ________________\n|                |\n|    Acception   |\n|________________|\n" + super.toString() + "\n ________________";
    }
}
