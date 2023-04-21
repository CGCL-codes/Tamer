package uk.ac.ed.ph.jqtiplus.node.content.xhtml.table;

import uk.ac.ed.ph.jqtiplus.node.XmlObject;

/**
 * td
 * 
 * @author Jonathon Hare
 *
 */
public class Td extends TableCell {

    private static final long serialVersionUID = 1L;

    /** Name of this class in xml schema. */
    public static final String CLASS_TAG = "td";

    /**
     * Constructs object.
     *
     * @param parent parent of constructed object
     */
    public Td(XmlObject parent) {
        super(parent);
    }

    @Override
    public String getClassTag() {
        return CLASS_TAG;
    }
}
