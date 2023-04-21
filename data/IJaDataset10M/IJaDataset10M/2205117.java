package org.broadleafcommerce.offer.dao;

import java.util.List;
import org.broadleafcommerce.offer.domain.CustomerOffer;
import org.broadleafcommerce.profile.domain.Customer;

public interface CustomerOfferDao {

    public CustomerOffer readCustomerOfferById(Long customerOfferId);

    public List<CustomerOffer> readCustomerOffersByCustomer(Customer customer);

    public CustomerOffer save(CustomerOffer customerOffer);

    public void delete(CustomerOffer customerOffer);

    public CustomerOffer create();
}
