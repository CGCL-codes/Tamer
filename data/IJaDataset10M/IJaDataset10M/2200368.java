package org.wynnit.minows;

import javax.ejb.*;

/**
 * This is the bean class for the CapabilityParametersStoreBean enterprise bean.
 * Created Jan 31, 2008 8:59:06 AM
 * @author steve
 */
public abstract class CapabilityParametersStoreBean implements EntityBean, CapabilityParametersStoreLocalBusiness {

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

    public abstract Long getId();

    public abstract void setId(Long id);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract byte[] getValue();

    public abstract void setValue(byte[] value);

    public abstract String getFormat();

    public abstract void setFormat(String format);

    public abstract org.wynnit.minows.CapabilityStoreLocal getCapabilityid();

    public abstract void setCapabilityid(org.wynnit.minows.CapabilityStoreLocal capabilityid);

    public Long ejbCreate(Long id, String name, String format, byte[] value, org.wynnit.minows.CapabilityStoreLocal capabilityid) throws CreateException {
        if (id == null) {
            throw new CreateException("The field \"id\" must not be null");
        }
        if (name == null) {
            throw new CreateException("The field \"name\" must not be null");
        }
        if (capabilityid == null) {
            throw new CreateException("The field \"capabilityid\" must not be null");
        }
        setId(id);
        setName(name);
        setValue(value);
        return null;
    }

    public void ejbPostCreate(Long id, String name, String format, byte[] value, org.wynnit.minows.CapabilityStoreLocal capabilityid) {
        setCapabilityid(capabilityid);
    }
}
