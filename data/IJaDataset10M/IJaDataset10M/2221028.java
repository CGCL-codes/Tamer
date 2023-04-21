package my.usm.cs.utmk.blexisma2.struct;

/**
 * @author didier
 * 
 */
public final class Lexie extends LexicalObject {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String local_id;

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
	 * @see LexicalObject
	 */
    public Lexie(int id, String form, int hits, String morphology, String etymology, ConceptualVector CV, String source, String glose, boolean randomCV) {
        super(id, form, hits, morphology, etymology, CV, source, glose, randomCV);
    }

    /**
     * @return the local_id
     */
    public final String getLocalID() {
        return local_id;
    }

    /**
     * @param local_id the local_id to set
     */
    public final void setLocalID(String local_id) {
        this.local_id = local_id;
    }

    /**
	 * Alphanumeric representation method of a lexie.
	 * 
	 * @return a string representation of the lexie.
	 * 
	 * @see java.lang.Object#toString()
	 */
    public String toString() {
        return " ________________\n|                |\n|     Lexie      |\n|________________|\n" + super.toString() + "\n ________________";
    }
}
