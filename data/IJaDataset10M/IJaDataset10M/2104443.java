package net.sf.brightside.mobilestock.service.api.main;

import java.util.List;
import net.sf.brightside.mobilestock.metamodel.api.BuyingOffer;
import net.sf.brightside.mobilestock.metamodel.api.ShareTransfer;

public interface ITryToBuyShare {

    /**
	 * @param offer
	 * @return
	 */
    List<ShareTransfer> tryToBuy(BuyingOffer offer);
}
