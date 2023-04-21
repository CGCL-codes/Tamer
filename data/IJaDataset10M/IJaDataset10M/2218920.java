package vrml.node;

import vrml.*;

/**
This is the general Node class
*/
public class Node extends BaseNode {

    protected Node() {
    }

    ;

    public Node(Browser browser, vrml.external.Node node) {
        super(browser, node);
    }

    /** 
   Get an EventIn by name. Return value is write-only.
   Throws an InvalidEventInException if eventInName isn't a valid
   eventIn name for a node of this type.
   */
    public final Field getEventIn(String eventInName) {
        return null;
    }

    /**
   Get an EventOut by name. Return value is read-only.
   Throws an InvalidEventOutException if eventOutName isn't a valid
   eventOut name for a node of this type.
   */
    public final ConstField getEventOut(String eventOutName) {
        return null;
    }

    /**
   Get an exposed field by name. 
   Throws an InvalidExposedFieldException if exposedFieldName isn't a valid
   exposedField name for a node of this type.
   */
    public final Field getExposedField(String exposedFieldName) {
        return null;
    }
}
