package org.pdfbox.pdmodel.interactive.action.type;

import java.io.IOException;
import org.pdfbox.cos.COSDictionary;
import org.pdfbox.pdmodel.interactive.documentnavigation.destination.PDDestination;

/**
 * This represents a go-to action that can be executed in a PDF document.
 *
 * @author <a href="mailto:ben@benlitchfield.com">Ben Litchfield</a>
 * @author Panagiotis Toumasis (ptoumasis@mail.gr)
 * @version $Revision: 1.2 $
 */
public class PDActionGoTo extends PDAction {

    /**
     * This type of action this object represents.
     */
    public static final String SUB_TYPE = "GoTo";

    /**
     * Default constructor.
     */
    public PDActionGoTo() {
        super();
        setSubType(SUB_TYPE);
    }

    /**
     * Constructor.
     *
     * @param a The action dictionary.
     */
    public PDActionGoTo(COSDictionary a) {
        super(a);
    }

    /**
     * This will get the destination to jump to.
     *
     * @return The D entry of the specific go-to action dictionary.
     * 
     * @throws IOException If there is an error creating the destination.
     */
    public PDDestination getDestination() throws IOException {
        return PDDestination.create(getCOSDictionary().getDictionaryObject("D"));
    }

    /**
     * This will set the destination to jump to.
     *
     * @param d The destination.
     */
    public void setDestination(PDDestination d) {
        getCOSDictionary().setItem("D", d);
    }
}
