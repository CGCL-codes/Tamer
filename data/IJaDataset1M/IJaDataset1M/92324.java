package com.xtech.xerp.inventory;

import com.xtech.common.Filter;
import com.xtech.common.entities.BillingItem;
import com.xtech.common.entities.Entity;

/**
 * @author jscruz
 * @since XERP
 */
public class BillingItemProposalFilter extends Filter {

    String proposal;

    public BillingItemProposalFilter() {
        setAllowMultiple(false);
    }

    public BillingItemProposalFilter(String proposal) {
        this();
        setProposal(proposal);
    }

    public boolean fliterEntity(Entity e) {
        if (e != null && e instanceof BillingItem) {
            BillingItem i = (BillingItem) e;
            return proposal.equals(i.getProposal());
        } else {
            return false;
        }
    }

    /**
	 * @return
	 * @author jscruz
	 * @since XERP
	 */
    public String getProposal() {
        return proposal;
    }

    /**
	 * @param string
	 * @author jscruz
	 * @since XERP
	 */
    public void setProposal(String string) {
        proposal = string;
    }
}
