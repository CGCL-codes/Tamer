package de.ios.kontor.sv.order.co;

import java.util.*;
import java.math.BigDecimal;
import de.ios.framework.basic.*;
import de.ios.kontor.sv.basic.co.*;
import de.ios.kontor.sv.main.co.*;

/**
 * Datacarrier for the main Infos about a ShipmentKind 
 *
 * @author js (Joachim Schaaf).
 * @version $Id: ShipmentKindDC.java,v 1.1.1.1 2004/03/24 23:03:07 nanneb Exp $.
 */
public class ShipmentKindDC extends BasicDC {

    /** Name of Shipment kind */
    public String name;

    /** Description */
    public String description;

    /** Suggested price */
    public BigDecimal price;

    /**
   * Build the RowVector.
   *
   * @return the Vector containing the ListRow.
   */
    public Vector getRowVector() {
        Vector v = new Vector(20);
        v.addElement(toString(name));
        v.addElement(toString(description));
        v.addElement(toString(price));
        return v;
    }
}

;
