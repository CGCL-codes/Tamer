package de.ios.kontor.sv.order.impl;

import java.math.*;
import de.ios.framework.db2.*;
import de.ios.framework.basic.*;
import de.ios.kontor.sv.order.co.*;
import de.ios.kontor.sv.basic.impl.*;

/**
 * DBObject for a VAT (value added tax rate).
 *
 * @author js (Joachim Schaaf).
 * @version $Id: VATDBO.java,v 1.1.1.1 2004/03/24 23:03:36 nanneb Exp $.
 */
public class VATDBO extends BasicDBO {

    /**
   * constructor
   */
    public VATDBO() {
        super();
    }

    /**
   * constructor
   *
   * @param _vatlevel Value added tax.
   * @param _orderby Order for sorting.
   */
    public VATDBO(BigDecimal _vatlevel, Integer _orderby) {
        this();
        setAll(_vatlevel, _orderby, false);
        resetModified();
    }

    /**
   * set all items in one call
   *
   * @param _vatlevel Value added tax.
   * @param _orderby Order for sorting.
   * @param setNull Set null values only if true
   */
    public void setAll(BigDecimal _vatlevel, Integer _orderby, boolean setNull) {
        if (setNull || (_vatlevel != null)) vatlevel.set(_vatlevel);
        if (setNull || (_orderby != null)) orderby.set(_orderby);
    }

    /** Value added tax */
    public DBFixedAttr vatlevel;

    /** Order for sorting */
    public DBIntAttr orderby;
}
