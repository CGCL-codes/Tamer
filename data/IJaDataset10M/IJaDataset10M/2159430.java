package org.jabusuite.webclient.address.customer;

import javax.naming.NamingException;
import org.jabusuite.address.customer.Customer;
import org.jabusuite.address.customer.HierarchyCustomer;
import org.jabusuite.address.customer.session.HierarchyCustomersRemote;
import org.jabusuite.client.utils.ClientTools;
import org.jabusuite.core.utils.EJbsObject;
import org.jabusuite.logging.Logger;
import org.jabusuite.webclient.dataediting.DlgState;
import org.jabusuite.webclient.main.ClientGlobals;

/**
 *
 * @author hilwers
 */
public class PnHierarchyCustomerEdit extends PnCustomerEdit {

    private Logger logger = Logger.getLogger(PnHierarchyCustomerEdit.class);

    private HierarchyCustomer parentCustomer;

    public PnHierarchyCustomerEdit(DlgState state, Customer customer) {
        super(state, customer);
    }

    public PnHierarchyCustomerEdit() {
        super();
    }

    @Override
    protected void initPanel() {
        super.initPanel();
        this.setParentCustomer(null);
    }

    @Override
    public void doSave() throws EJbsObject {
        if (logger.isDebugEnabled()) {
            logger.debug("Saving data...");
        }
        try {
            HierarchyCustomersRemote customers = (HierarchyCustomersRemote) ClientTools.getRemoteBean(HierarchyCustomersRemote.class);
            this.getControlData();
            this.checkData();
            logger.debug(getCustomer().isDeleted());
            if (this.getDlgState() == DlgState.dsInsert) {
                System.out.println("Adding new entity");
                this.getCustomer().setParentCustomer(this.getParentCustomer());
                customers.createDataset(this.getCustomer(), ClientGlobals.getUser(), ClientGlobals.getCompany());
            } else if (this.getDlgState() == DlgState.dsEdit) {
                System.out.println("Saving exisiting entity.");
                customers.updateDataset(this.getCustomer(), ClientGlobals.getUser());
            }
            System.out.println("Entity saved.");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public HierarchyCustomer getCustomer() {
        return (HierarchyCustomer) this.getJbsBaseObject();
    }

    public void setCustomer(HierarchyCustomer customer) {
        this.setJbsBaseObject(customer);
    }

    public HierarchyCustomer getParentCustomer() {
        return parentCustomer;
    }

    public void setParentCustomer(HierarchyCustomer parentCustomer) {
        this.parentCustomer = parentCustomer;
    }
}
