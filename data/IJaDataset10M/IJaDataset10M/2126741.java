package cu.edu.cujae.biowh.parser.mif25.xml.tags;

/**
 * This Class storage the XML Interactor Tags on MIF25
 *
 * @author rvera
 * @version 0.1
 * @since Oct 21, 2010
 * @see
 */
public class InteractorTags {

    private final String INTERACTORFLAGS = "interactor";

    private final String IDFLAGS = "id";

    private final String INTERACTORTYPEFLAGS = "interactorType";

    private final String ORGANISMFLAGS = "organism";

    private final String SEQUENCEFLAGS = "sequence";

    public String getIDFLAGS() {
        return IDFLAGS;
    }

    public String getINTERACTORFLAGS() {
        return INTERACTORFLAGS;
    }

    public String getINTERACTORTYPEFLAGS() {
        return INTERACTORTYPEFLAGS;
    }

    public String getORGANISMFLAGS() {
        return ORGANISMFLAGS;
    }

    public String getSEQUENCEFLAGS() {
        return SEQUENCEFLAGS;
    }
}
