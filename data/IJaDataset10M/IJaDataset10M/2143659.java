package org.travelfusion.xmlclient.ri.xobject.misc;

import java.util.Date;
import org.travelfusion.xmlclient.ri.xobject.misc.XFindAlternativeFlightsItem.CodeType;
import org.travelfusion.xmlclient.xobject.XRequestBean;

/**
 * The request for supplier routes information.
 */
public class XGetSupplierRoutesRequest extends XRequestBean {

    private String supplier;

    /**
	 * Returns the supplier.
	 * 
	 * @return the supplier
	 */
    public String getSupplier() {
        return supplier;
    }

    /**
	 * Sets the supplier.
	 * 
	 * @param supplier the supplier
	 */
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
}
