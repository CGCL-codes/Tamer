package com.centraview.sale.opportunity;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface OpportunityLocalHome extends EJBLocalHome {

    public OpportunityLocal create() throws CreateException;
}
