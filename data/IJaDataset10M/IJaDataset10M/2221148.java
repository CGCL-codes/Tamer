package com.ohioedge.j2ee.api.org.vendor;

import com.ohioedge.j2ee.api.org.vendor.VendorBean;
import com.ohioedge.j2ee.api.org.vendor.ejb.VendorShipmentMethodPK;
import org.j2eebuilder.view.BusinessDelegateException;
import java.util.Collection;
import org.j2eebuilder.util.LogManager;

/**
* VendorShipmentMethodBean is a java bean used for communication between JSPs and
* VendorShipmentMethod EJB.
* @author Sandeep Dixit
* @version 1.3.1
* 04/06/2001: Modified to show changed title selection
*/
public class VendorShipmentMethodDelegate extends org.j2eebuilder.view.DefaultSharedOrganizationBusinessDelegate {

    private static transient LogManager log = new LogManager(VendorShipmentMethodDelegate.class);

    public static VendorShipmentMethodBean getVendorShipmentMethodVO(Integer shipmentMethodID, Integer vendorID) throws BusinessDelegateException {
        try {
            return (VendorShipmentMethodBean) org.j2eebuilder.InstanceLocator.getCurrentInstance().queryByUniqueKey("VendorShipmentMethod", new VendorShipmentMethodPK(shipmentMethodID, vendorID));
        } catch (Exception e) {
            throw new BusinessDelegateException("getVendorShipmentMethodVO(shipmentMethodID, vendorID):" + e.toString());
        }
    }

    public VendorShipmentMethodDelegate() {
    }
}
