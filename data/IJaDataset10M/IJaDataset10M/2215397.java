package unbbs.persistence.model;

/**
 * Bean implementation class for Enterprise Bean: Domain
 */
public abstract class DomainBean implements javax.ejb.EntityBean {

    private javax.ejb.EntityContext myEntityCtx;

    /**
	 * setEntityContext
	 */
    public void setEntityContext(javax.ejb.EntityContext ctx) {
        myEntityCtx = ctx;
    }

    /**
	 * getEntityContext
	 */
    public javax.ejb.EntityContext getEntityContext() {
        return myEntityCtx;
    }

    /**
	 * unsetEntityContext
	 */
    public void unsetEntityContext() {
        myEntityCtx = null;
    }

    /**
	 * ejbCreate
	 */
    public unbbs.persistence.model.DomainKey ejbCreate(int id, String name) throws javax.ejb.CreateException {
        setId(id);
        setName(name);
        return null;
    }

    /**
	 * ejbPostCreate
	 */
    public void ejbPostCreate(int id, String name) throws javax.ejb.CreateException {
    }

    /**
	 * ejbActivate
	 */
    public void ejbActivate() {
    }

    /**
	 * ejbLoad
	 */
    public void ejbLoad() {
    }

    /**
	 * ejbPassivate
	 */
    public void ejbPassivate() {
    }

    /**
	 * ejbRemove
	 */
    public void ejbRemove() throws javax.ejb.RemoveException {
    }

    /**
	 * ejbStore
	 */
    public void ejbStore() {
    }

    /**
	 * Get accessor for persistent attribute: id
	 */
    public abstract int getId();

    /**
	 * Set accessor for persistent attribute: id
	 */
    public abstract void setId(int newId);

    /**
	 * Get accessor for persistent attribute: name
	 */
    public abstract java.lang.String getName();

    /**
	 * Set accessor for persistent attribute: name
	 */
    public abstract void setName(java.lang.String newName);

    /**
	 * This method was generated for supporting the relationship role named models.
	 * It will be deleted/edited when the relationship is deleted/edited.
	 */
    public abstract java.util.Collection getModels();

    /**
	 * This method was generated for supporting the relationship role named models.
	 * It will be deleted/edited when the relationship is deleted/edited.
	 */
    public abstract void setModels(java.util.Collection aModels);
}
