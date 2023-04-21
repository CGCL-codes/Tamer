package fi.arcusys.commons.j2ee.resolver;

import java.io.Serializable;
import org.hibernate.Session;

/**
 * An abstract implementation of interface {@link ReferringEntity} that
 * provides default implementation for every method but 
 * {@link ReferringEntity#getEntityId()}.
 * 
 * @author mikko
 * @version 1.0 $Rev$
 */
public abstract class AbstractReferringEntity implements ReferringEntity, Serializable {

    private Object entity;

    private CascadeOp cascadeOp;

    private ReferringEntity parent;

    public AbstractReferringEntity(Object entity, CascadeOp cascadeOp) {
        this(entity, cascadeOp, null);
    }

    public AbstractReferringEntity(Object entity, CascadeOp cascadeOp, ReferringEntity parentReferringEntity) {
        this.entity = entity;
        this.cascadeOp = cascadeOp;
        this.parent = parentReferringEntity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ReferringEntity {entity = ");
        sb.append(getEntity());
        sb.append(", cascadeOp = ");
        sb.append(getCascadeOp());
        sb.append("}");
        return sb.toString();
    }

    public ReferringEntity getParent() {
        return parent;
    }

    public int getNestingLevel() {
        ReferringEntity p = getParent();
        return (null == p) ? 0 : p.getNestingLevel() + 1;
    }

    public CascadeOp getCascadeOp() {
        return cascadeOp;
    }

    public Object getEntity() {
        return entity;
    }

    /**
	 * Called to do the specified {@link CascadeOp} operation before the
	 * referred entity is deleted.
	 * 
	 * <p>This default implementation does nothing but calls
	 * <code>ReferredEntity.doCascade()</code>.</p>
	 * 
	 * @param referredEntity the referred entity
	 * @param session the current <code>Session</code>
	 */
    public void doCascade(ReferredEntity referredEntity, Session session) {
        referredEntity.doCascade(this, session);
    }
}
