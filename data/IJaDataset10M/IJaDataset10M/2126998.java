package org.broadleafcommerce.offer.service;

import java.util.List;
import javax.annotation.Resource;
import org.broadleafcommerce.offer.OfferDataProvider;
import org.broadleafcommerce.offer.domain.Offer;
import org.broadleafcommerce.order.service.OrderService;
import org.broadleafcommerce.profile.service.CustomerService;
import org.broadleafcommerce.test.BaseTest;
import org.testng.annotations.Test;

public class OfferServiceTest extends BaseTest {

    @Resource
    OfferService offerService;

    @Resource
    OrderService orderService;

    @Resource
    CustomerService customerService;

    @Test(groups = { "applyOffersToOrder" }, dataProvider = "offerDataProvider", dataProviderClass = OfferDataProvider.class, dependsOnGroups = { "findCurrentCartForCustomer", "getItemsForOrder" })
    public void applyOffersToOrder(List<Offer> allOffers) {
    }
}
