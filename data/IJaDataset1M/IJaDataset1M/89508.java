package gal.metier.service.common;

import gal.metier.service.ISubscriptionsService;

/**
 * @author Benjamin
 * 
 */
public interface IServiceFactory {

    /**
     * @return :
     */
    ISubscriptionsService getSubscriptionService();

    /**
     * @param subscriptionService :
     */
    void setSubscriptionService(ISubscriptionsService subscriptionService);
}
