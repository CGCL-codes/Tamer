package org.wynnit.minows;

import javax.ejb.EJBLocalObject;

/**
 *
 * @author steve
 */
public interface ActionConditionsLocal extends EJBLocalObject {

    java.lang.Long getId();

    java.lang.Long getConditionid();

    void setConditionid(java.lang.Long condition);

    java.lang.String getCondition();

    void setCondition(java.lang.String condition);

    String getOutcome();

    void setOutcome(String outcome);

    String getName();

    void setName(String name);

    String getArgument();

    void setArgument(String argument);

    TicketActionsStoreLocal getTicketactionid();

    void setTicketactionid(TicketActionsStoreLocal tickactionid);
}
