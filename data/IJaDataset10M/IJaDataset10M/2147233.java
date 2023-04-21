package org.jabusuite.webclient.address.supplier;

import org.jabusuite.address.supplier.Supplier;
import org.jabusuite.address.supplier.session.SuppliersRemote;
import org.jabusuite.client.address.PlSuppliers;
import org.jabusuite.client.datalist.PageableJbsObjectList;
import org.jabusuite.client.utils.ClientTools;
import org.jabusuite.core.utils.JbsBaseObject;
import org.jabusuite.webclient.address.PnAddressList;
import org.jabusuite.webclient.dataediting.FmEditJbsBaseObject;
import org.jabusuite.webclient.main.ClientGlobals;
import org.jabusuite.logging.Logger;
import org.jabusuite.webclient.address.contact.SelContactType.ContactType;

/**
 * List-Panel for suppliers
 * @author hilwers
 */
public class PnSupplierList extends PnAddressList {

    Logger logger = Logger.getLogger(PnSupplierList.class);

    @Override
    protected void initPanel() throws Exception {
        super.initPanel();
        this.setContactType(ContactType.CT_SUPPLIER);
    }

    @Override
    protected FmEditJbsBaseObject createEditForm() {
        return new FmSupplierEdit();
    }

    @Override
    protected PageableJbsObjectList createJbsObjects() {
        return new PlSuppliers(ClientGlobals.getUser(), ClientGlobals.getCompany());
    }

    @Override
    public void doDeleteJbsObject(JbsBaseObject jbsObject) throws Exception {
        SuppliersRemote custManagement = (SuppliersRemote) ClientTools.getRemoteBean(SuppliersRemote.class);
        custManagement.deleteDataset((Supplier) jbsObject, ClientGlobals.getUser());
    }
}
