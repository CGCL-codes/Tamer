package org.wynnit.minows;

import javax.ejb.*;

/**
 * This is the bean class for the TicketAccessRightsBean enterprise bean.
 * Created Jan 4, 2008 1:11:38 PM
 * @author steve
 */
public abstract class TicketAccessRightsBean implements EntityBean, TicketAccessRightsLocalBusiness {

    private EntityContext context;

    /**
     * @see javax.ejb.EntityBean#setEntityContext(javax.ejb.EntityContext)
     */
    public void setEntityContext(EntityContext aContext) {
        context = aContext;
    }

    /**
     * @see javax.ejb.EntityBean#ejbActivate()
     */
    public void ejbActivate() {
    }

    /**
     * @see javax.ejb.EntityBean#ejbPassivate()
     */
    public void ejbPassivate() {
    }

    /**
     * @see javax.ejb.EntityBean#ejbRemove()
     */
    public void ejbRemove() {
    }

    /**
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() {
        context = null;
    }

    /**
     * @see javax.ejb.EntityBean#ejbLoad()
     */
    public void ejbLoad() {
    }

    /**
     * @see javax.ejb.EntityBean#ejbStore()
     */
    public void ejbStore() {
    }

    public abstract Long getAccessid();

    public abstract void setAccessid(Long accessid);

    public abstract String getRequestor();

    public abstract void setRequestor(String requestor);

    public abstract String getType();

    public abstract void setType(String type);

    public abstract Integer getRef();

    public abstract void setRef(Integer ref);

    public abstract TicketStoreLocal getTicketid();

    public abstract void setTicketid(TicketStoreLocal ticketid);

    public Long ejbCreate(Long accessid, String requestor, String type, Integer ref, TicketStoreLocal ticketid) throws CreateException {
        if (accessid == null) {
            throw new CreateException("The field \"accessid\" must not be null");
        }
        if (requestor == null) {
            throw new CreateException("The field \"requestor\" must not be null");
        }
        if (type == null) {
            throw new CreateException("The field \"type\" must not be null");
        }
        if (ref == null) {
            throw new CreateException("The field \"ref\" must not be null");
        }
        if (ticketid == null) {
            throw new CreateException("The field \"ticketid\" must not be null");
        }
        setAccessid(accessid);
        setRequestor(requestor);
        setType(type);
        setRef(ref);
        return null;
    }

    public void ejbPostCreate(Long accessid, String requestor, String type, Integer ref, TicketStoreLocal ticketid) {
        setTicketid(ticketid);
    }
}
