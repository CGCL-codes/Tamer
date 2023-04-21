package org.travelfusion.xmlclient.ri.xobject.misc;

import java.util.Map;
import org.travelfusion.xmlclient.xobject.XResponseBean;

/**
 * @author Ana Henneberke (ana@travelfusion.com)
 */
public class XGetProductTypesResponse extends XResponseBean {

    private static final long serialVersionUID = 1L;

    private Map<String, String> supplierTypes;

    public Map<String, String> getSupplierTypes() {
        return supplierTypes;
    }

    public void setSupplierTypes(Map<String, String> supplierTypes) {
        this.supplierTypes = supplierTypes;
    }
}
