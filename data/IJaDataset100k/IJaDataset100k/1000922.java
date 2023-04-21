package com.tll.client.mvc.view.account;

import com.tll.client.mvc.view.AbstractDynamicViewInitializer;
import com.tll.common.model.ModelKey;

/**
 * MerchantListingViewInitializer - MerchantListingView specific view request.
 * @author jpk
 */
public final class MerchantListingViewInitializer extends AbstractDynamicViewInitializer {

    /**
	 * The parent Isp ref.
	 */
    final ModelKey ispRef;

    /**
	 * Constructor
	 * @param ispRef The required parent isp ref
	 */
    public MerchantListingViewInitializer(ModelKey ispRef) {
        super(MerchantListingView.klas);
        this.ispRef = ispRef;
    }

    @Override
    protected int getViewId() {
        return ispRef.hashCode();
    }

    public ModelKey getIspRef() {
        return ispRef;
    }
}
