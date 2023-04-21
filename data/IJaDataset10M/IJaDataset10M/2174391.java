package org.jgentleframework.services.eventservices;

import org.jgentleframework.services.eventservices.objectmeta.Subscription;

/**
 * Chỉ định thực thể filter của một subscription chỉ định.
 * 
 * @author LE QUOC CHUNG - mailto: <a
 *         href="mailto:skydunkpro@yahoo.com">skydunkpro@yahoo.com</a>
 * @date Oct 27, 2007
 */
public interface ISubscriptionFilter {

    /**
	 * @param eventClassImpl
	 * @param subscription
	 * @param args
	 * @return {@link Subscription}
	 * @throws EventServicesException
	 */
    Subscription filter(EventClass eventClassImpl, Subscription subscription, Object... args) throws EventServicesException;
}
