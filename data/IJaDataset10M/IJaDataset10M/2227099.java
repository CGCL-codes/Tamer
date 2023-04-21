package org.broadleafcommerce.core.offer.service.discount.domain;

import org.broadleafcommerce.core.offer.domain.CandidateItemOffer;
import org.broadleafcommerce.core.offer.domain.Offer;
import org.broadleafcommerce.core.offer.domain.OfferItemCriteria;
import org.broadleafcommerce.core.offer.service.type.OfferDiscountType;
import org.broadleafcommerce.money.Money;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PromotableCandidateItemOfferImpl implements PromotableCandidateItemOffer {

    private static final long serialVersionUID = 1L;

    protected Money potentialSavings;

    protected HashMap<OfferItemCriteria, List<PromotableOrderItem>> candidateQualifiersMap = new HashMap<OfferItemCriteria, List<PromotableOrderItem>>();

    protected List<PromotableOrderItem> candidateTargets = new ArrayList<PromotableOrderItem>();

    protected CandidateItemOffer delegate;

    protected PromotableOrderItem orderItem;

    protected int uses = 0;

    public PromotableCandidateItemOfferImpl(CandidateItemOffer candidateItemOffer) {
        this.delegate = candidateItemOffer;
    }

    public HashMap<OfferItemCriteria, List<PromotableOrderItem>> getCandidateQualifiersMap() {
        return candidateQualifiersMap;
    }

    public void setCandidateQualifiersMap(HashMap<OfferItemCriteria, List<PromotableOrderItem>> candidateItemsMap) {
        this.candidateQualifiersMap = candidateItemsMap;
    }

    public List<PromotableOrderItem> getCandidateTargets() {
        return candidateTargets;
    }

    public void setCandidateTargets(List<PromotableOrderItem> candidateTargets) {
        this.candidateTargets = candidateTargets;
    }

    public Money calculateSavingsForOrderItem(PromotableOrderItem orderItem, int qtyToReceiveSavings) {
        Money savings = new Money(0);
        Money salesPrice = orderItem.getPriceBeforeAdjustments(getOffer().getApplyDiscountToSalePrice());
        if (getOffer().getDiscountType().equals(OfferDiscountType.AMOUNT_OFF)) {
            savings = savings.add(new Money(getOffer().getValue()).multiply(qtyToReceiveSavings));
        } else if (getOffer().getDiscountType().equals(OfferDiscountType.PERCENT_OFF)) {
            BigDecimal savingsPercent = getOffer().getValue().divide(new BigDecimal(100));
            savings = savings.add(salesPrice.multiply(savingsPercent).multiply(qtyToReceiveSavings));
        } else {
            savings = savings.add(salesPrice.multiply(qtyToReceiveSavings).subtract(new Money(getOffer().getValue()).multiply(qtyToReceiveSavings)));
        }
        return savings;
    }

    public Money getPotentialSavings() {
        if (potentialSavings == null) {
            potentialSavings = calculatePotentialSavings();
        }
        return potentialSavings;
    }

    /**
	 * This method determines how much the customer might save using this promotion for the
	 * purpose of sorting promotions with the same priority. The assumption is that any possible
	 * target specified for BOGO style offers are of equal or lesser value. We are using
	 * a calculation based on the qualifiers here strictly for rough comparative purposes.
	 *  
	 * If two promotions have the same priority, the one with the highest potential savings
	 * will be used as the tie-breaker to determine the order to apply promotions.
	 * 
	 * This method makes a good approximation of the promotion value as determining the exact value
	 * would require all permutations of promotions to be run resulting in a costly 
	 * operation.
	 * 
	 * @return
	 */
    public Money calculatePotentialSavings() {
        Money savings = new Money(0);
        int maxUses = calculateMaximumNumberOfUses();
        int appliedCount = 0;
        for (PromotableOrderItem chgItem : candidateTargets) {
            int qtyToReceiveSavings = Math.min(chgItem.getQuantity(), maxUses);
            savings = calculateSavingsForOrderItem(chgItem, qtyToReceiveSavings);
            appliedCount = appliedCount + qtyToReceiveSavings;
            if (appliedCount >= maxUses) {
                return savings;
            }
        }
        return savings;
    }

    /**
	 * Determines the maximum number of times this promotion can be used based on the
	 * ItemCriteria and promotion's maxQty setting.
	 */
    public int calculateMaximumNumberOfUses() {
        int maxMatchesFound = 9999;
        int numberOfUsesForThisItemCriteria = calculateMaxUsesForItemCriteria(delegate.getOffer().getTargetItemCriteria(), getOffer());
        maxMatchesFound = Math.min(maxMatchesFound, numberOfUsesForThisItemCriteria);
        int offerMaxUses = getOffer().getMaxUses() == 0 ? maxMatchesFound : getOffer().getMaxUses();
        return Math.min(maxMatchesFound, offerMaxUses);
    }

    public int calculateMaxUsesForItemCriteria(OfferItemCriteria itemCriteria, Offer promotion) {
        int numberOfTargets = 0;
        int numberOfUsesForThisItemCriteria = 9999;
        if (candidateTargets != null && itemCriteria != null) {
            for (PromotableOrderItem potentialTarget : candidateTargets) {
                numberOfTargets += potentialTarget.getQuantityAvailableToBeUsedAsTarget(promotion);
            }
            numberOfUsesForThisItemCriteria = numberOfTargets / itemCriteria.getQuantity();
        }
        return numberOfUsesForThisItemCriteria;
    }

    public CandidateItemOffer getDelegate() {
        return delegate;
    }

    public void reset() {
        delegate = null;
    }

    public void setOrderItem(PromotableOrderItem orderItem) {
        if (orderItem != null) {
            this.orderItem = (PromotableOrderItem) orderItem;
            delegate.setOrderItem(this.orderItem.getDelegate());
        }
    }

    public PromotableCandidateItemOffer clone() {
        PromotableCandidateItemOfferImpl copy = new PromotableCandidateItemOfferImpl(delegate.clone());
        copy.orderItem = this.orderItem;
        return copy;
    }

    public int getPriority() {
        return delegate.getPriority();
    }

    public Offer getOffer() {
        return delegate.getOffer();
    }

    public void setOffer(Offer offer) {
        delegate.setOffer(offer);
    }

    public PromotableOrderItem getOrderItem() {
        return orderItem;
    }

    public int getUses() {
        return uses;
    }

    public void addUse() {
        uses++;
    }
}
