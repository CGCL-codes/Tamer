package de.ios.kontor.sv.order.impl;

import de.ios.framework.db2.*;
import de.ios.framework.basic.*;
import de.ios.kontor.sv.order.co.*;
import de.ios.kontor.sv.basic.impl.*;

/**
 *
 *
 * @author js (Joachim Schaaf).
 * @version $Id: PriceCategoryDBO.java,v 1.1.1.1 2004/03/24 23:03:32 nanneb Exp $.
 */
public class PriceCategoryDBO extends BasicDBO {

    /**
   * constructor
   */
    public PriceCategoryDBO() {
        super();
    }

    /**
   * constructor
   *
   * @param _description The description for this price category.
   */
    public PriceCategoryDBO(String _description) {
        this();
        setAll(_description, false);
        resetModified();
    }

    /**
   * set all items in one call
   *
   * @param _description The description for this price category.
   * @param setNull Set null values only if true
   */
    public void setAll(String _description, boolean setNull) {
        if (setNull || (_description != null)) description.set(_description);
    }

    /** The description for this price category */
    public DBStringAttr description;
}
