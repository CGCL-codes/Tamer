package org.broadleafcommerce.offer.dao;

import java.util.List;
import org.broadleafcommerce.offer.domain.CandidateFulfillmentGroupOffer;
import org.broadleafcommerce.offer.domain.CandidateItemOffer;
import org.broadleafcommerce.offer.domain.CandidateOrderOffer;
import org.broadleafcommerce.offer.domain.FulfillmentGroupAdjustment;
import org.broadleafcommerce.offer.domain.Offer;
import org.broadleafcommerce.offer.domain.OfferInfo;
import org.broadleafcommerce.offer.domain.OrderAdjustment;
import org.broadleafcommerce.offer.domain.OrderItemAdjustment;

public interface OfferDao {

    public List<Offer> readAllOffers();

    public Offer readOfferById(Long offerId);

    public List<Offer> readOffersByAutomaticDeliveryType();

    public Offer save(Offer offer);

    public void delete(Offer offer);

    public Offer create();

    public CandidateOrderOffer createCandidateOrderOffer();

    public CandidateItemOffer createCandidateItemOffer();

    public CandidateFulfillmentGroupOffer createCandidateFulfillmentGroupOffer();

    public OrderItemAdjustment createOrderItemAdjustment();

    public OrderAdjustment createOrderAdjustment();

    public FulfillmentGroupAdjustment createFulfillmentGroupAdjustment();

    public OfferInfo createOfferInfo();

    public OfferInfo save(OfferInfo offerInfo);

    public void delete(OfferInfo offerInfo);
}
