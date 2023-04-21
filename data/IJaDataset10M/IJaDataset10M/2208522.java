package de.ios.kontor.sv.address.co;

import java.util.*;
import de.ios.framework.basic.*;
import de.ios.kontor.sv.basic.co.*;

/**
 * KindOfBusinessPDC is a ListRowDataCarrier carrying the data for a business partner kind.
 * 
 * @author js (Joachim Schaaf)
 * @version $Id: KindOfBusinessPDC.java,v 1.1.1.1 2004/03/24 23:02:38 nanneb Exp $
 */
public class KindOfBusinessPDC extends BasicDC {

    public String kind;

    /**
   * Build the RowVector.
   * @return the Vector containing the ListRow.
   */
    public Vector getRowVector() {
        Vector v = new Vector(1);
        v.addElement(toString(kind));
        return v;
    }
}
