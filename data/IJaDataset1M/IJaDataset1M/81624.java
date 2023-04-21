package org.helianto.web.action.impl;

import java.util.List;
import javax.annotation.Resource;
import org.helianto.core.Identity;
import org.helianto.core.filter.Filter;
import org.helianto.core.filter.IdentityFilterAdapter;
import org.helianto.core.security.PublicUserDetails;
import org.helianto.core.service.IdentityMgr;
import org.helianto.web.action.AbstractFilterAction;
import org.helianto.web.action.SimpleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.webflow.core.collection.MutableAttributeMap;

/**
 * Presentation logic to find or create an identity.
 * 
 * @author mauriciofernandesdecastro
 */
@Component("identityAction")
public class IdentityActionImpl extends AbstractFilterAction<Identity> {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    @Override
    protected Filter doCreateFilter(MutableAttributeMap attributes, PublicUserDetails userDetails) {
        return new IdentityFilterAdapter(((SimpleModel<Identity>) getModel(attributes)).getForm());
    }

    @Override
    protected List<Identity> doFilter(Filter filter) {
        return identityMgr.findIdentities(filter, null);
    }

    /**
	 * Overriden to route "create" only if the principal does not exist.
	 * 
	 * @param attributes
	 * @param itemList
	 */
    protected String internalFilter(MutableAttributeMap attributes, List<Identity> itemList) {
        if (itemList != null && itemList.size() == 0) {
            return "create";
        }
        logger.debug("Selected: {}.", itemList.get(0));
        return "use";
    }

    @Override
    protected Identity doCreate(MutableAttributeMap attributes, PublicUserDetails userDetails) {
        return new Identity("");
    }

    @Override
    protected Identity doStore(Identity target) {
        return identityMgr.storeIdentity(target);
    }

    private IdentityMgr identityMgr;

    @Resource
    public void setIdentityMgr(IdentityMgr identityMgr) {
        this.identityMgr = identityMgr;
    }

    private static final Logger logger = LoggerFactory.getLogger(IdentityActionImpl.class);
}
