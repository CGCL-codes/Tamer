package org.medbeans.modules.search.types;

import org.openide.loaders.DataObject;
import org.openide.util.HelpCtx;

/**
 * Test DataObject name. Reuse TextType.
 *
 * @author  Petr Kuzel
 */
public class ObjectNameType extends TextType {

    /** Serial verions UID. */
    public static final long serialVersionUID = 1L;

    /**
     * @return true if object passes the test.
     */
    public boolean testDataObject(DataObject dobj) {
        return match(dobj.getName());
    }

    /** Get help context for this search type.
     * Implements superclass abstract method. */
    public HelpCtx getHelpCtx() {
        return new HelpCtx(ObjectNameType.class);
    }
}
