package org.helianto.partner.filter.classic;

import java.io.Serializable;
import org.helianto.core.Entity;
import org.helianto.core.User;
import org.helianto.core.criteria.OrmCriteriaBuilder;
import org.helianto.core.filter.classic.AbstractUserBackedCriteriaFilter;

/**
 * Partner registry filter.
 * 
 * @author Maur�cio Fernandes de Castro
 * @deprecated see PartnerEntityFilterAdapter
 */
public class PrivateEntityFilter extends AbstractUserBackedCriteriaFilter implements Serializable {

    private static final long serialVersionUID = 1L;

    private String partnerAlias;

    private String partnerNameLike;

    /**
	 * Default constructor.
	 */
    public PrivateEntityFilter() {
        setPartnerAlias("");
        setPartnerNameLike("");
    }

    /**
	 * Entity constructor.
	 * 
	 * @param entity
	 */
    public PrivateEntityFilter(Entity entity) {
        this();
        setEntity(entity);
    }

    /**
	 * User constructor.
	 * 
	 * @param user
	 */
    public PrivateEntityFilter(User user) {
        this(user.getEntity());
        setUser(user);
    }

    /**
	 * Key constructor.
	 * 
	 * @param entity
	 * @param partnerAlias
	 */
    public PrivateEntityFilter(Entity entity, String partnerAlias) {
        this(entity);
        setPartnerAlias(partnerAlias);
    }

    /**
	 * Reset method.
	 */
    public void reset() {
        setPartnerAlias("");
        setPartnerNameLike("");
    }

    public boolean isSelection() {
        return getPartnerAlias().length() > 0;
    }

    @Override
    public void doFilter(OrmCriteriaBuilder mainCriteriaBuilder) {
        appendLikeFilter("entityName", getPartnerNameLike(), mainCriteriaBuilder);
    }

    @Override
    protected void doSelect(OrmCriteriaBuilder mainCriteriaBuilder) {
        appendEqualFilter("entityAlias", getPartnerAlias(), mainCriteriaBuilder);
    }

    /**
     * PartnerAlias.
     */
    public String getPartnerAlias() {
        return this.partnerAlias;
    }

    public void setPartnerAlias(String partnerAlias) {
        this.partnerAlias = partnerAlias;
    }

    /**
	 * Name like.
	 */
    public String getPartnerNameLike() {
        return partnerNameLike;
    }

    public void setPartnerNameLike(String partnerNameLike) {
        this.partnerNameLike = partnerNameLike;
    }
}
