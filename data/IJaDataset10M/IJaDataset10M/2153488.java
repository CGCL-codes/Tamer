package cu.edu.cujae.biowh.parser.protein.xml.tags;

/**
 * This Class storage the XML Interactant tags on Uniprot
 *
 * @author rvera
 * @version 0.1
 * @since Oct 2, 2010
 * @see
 */
public class InteractantTags {

    private final String INTERACTANTFLAGS = "interactant";

    private final String INTACTIDFLAGS = "intactId";

    private final String IDFLAGS = "id";

    private final String LABELFLAGS = "label";

    public String getIDFLAGS() {
        return IDFLAGS;
    }

    public String getINTACTIDFLAGS() {
        return INTACTIDFLAGS;
    }

    public String getINTERACTANTFLAGS() {
        return INTERACTANTFLAGS;
    }

    public String getLABELFLAGS() {
        return LABELFLAGS;
    }
}
