package com.apelon.beans.dts.plugin.transferable;

import com.apelon.dts.client.concept.DTSConcept;
import java.awt.datatransfer.*;

/**
 * For transfer of DTS Concept through Drag and Drop.
 *
 * <p>It supports these data flavors:
 * <ul>
 * <li>DTS Concept Data Flavor ({@link DTSDataFlavor#conceptFlavor})
 * <li>String Data Flavor ({@link DataFlavor#stringFlavor})
 * </ul>
 * <p>
 * Copyright:    Copyright (c) 2003<P>
 * Company:      Apelon<P>
 * @author Apelon
 * @version 1.0
 */
public class ConceptTransferable extends AbstractTransferable {

    private DTSConcept fConcept = null;

    /**
   * Initializes with given {@link DTSConcept}. The supported DataFlavors
   * are set.
	 */
    public ConceptTransferable(DTSConcept con) {
        super();
        setTransferDataFlavors(new DataFlavor[] { DTSDataFlavor.conceptFlavor, DataFlavor.stringFlavor });
        fConcept = con;
    }

    /**
   * Returns DataFlavor of this Transferable
	 *
	 * @return java.awt.datatransfer.DataFlavor
   * @deprecated Use <code>DTSDataFlavor.conceptFlavor</code> instead
	 */
    public static DataFlavor getDataFlavor() {
        return DTSDataFlavor.conceptFlavor;
    }

    /**
   * Returns the data for a given DataFlavor.
   * <p>For Concept Data Flavor, the concept ({@link DTSConcept}) of this Transferable is returned.
   * <p>For String Data Flavor, the name of the concept is retuned.
   * <p>For other Data Flavor, the returned value is null
	 *
	 * @return java.lang.Object
	 * @param dataflavor java.awt.datatransfer.DataFlavor
	 */
    public Object getTransferData(DataFlavor dataflavor) {
        if (dataflavor.equals(DTSDataFlavor.conceptFlavor)) {
            return fConcept;
        } else if (dataflavor.equals(DataFlavor.stringFlavor)) {
            return fConcept.toString();
        } else {
            return null;
        }
    }
}
