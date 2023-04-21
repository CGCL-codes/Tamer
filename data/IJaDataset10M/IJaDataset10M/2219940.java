package net.martinimix.service.account;

import java.util.List;
import net.martinimix.domain.account.Address;
import net.martinimix.domain.account.Customer;
import net.martinimix.domain.account.CustomerNotFoundException;
import net.martinimix.domain.account.InvalidLoginException;
import net.martinimix.domain.payment.Account;
import net.martinimix.domain.payment.BillMeLaterAccount;
import net.martinimix.domain.payment.CreditCard;
import net.martinimix.domain.payment.PaymentMethod;
import net.martinimix.service.payment.CreditCardNotFoundException;

/**
 * Provides a customer account service interface.
 * 
 * @author Scott Rossillo
 * @since 1.0
 */
public interface CustomerService {

    /**
	 * Creates a new guest customer.
	 * 
	 * @return a new guest <code>Customer</code>
	 */
    public Customer createGuestCustomer();

    /**
	 * Returns the customer identified by the given login credentials.
	 * 
	 * @param userId the user identifier for the user to login
	 * @param password the password for the user to login
	 * 
	 * @return an authenticated <code>Customer</code> if the given
	 * <code>userId</code> and <code>password</code> match the credentials
	 * on file
	 * 
	 * @throws InvalidLoginException if the given
	 * <code>userId</code> and <code>password</code> do not match the user's
	 * known login credentials
	 * 
	 */
    public Customer login(String userId, String password) throws InvalidLoginException;

    /**
	 * Returns the customer identified by the given customer id.
	 * 
	 * @param customerId the identifier for the <code>Customer</code>
	 * to be returned
	 * 
	 * @return the <code>Customer</code> identified by the given 
	 * <code>customerId</code> if found; <code>null</code> otherwise
	 * 
	 * @throws CustomerNotFoundException if no <code>Customer</code> exists with 
	 * the given <code>customerId</code>
	 */
    public Customer findCustomer(long customerId) throws CustomerNotFoundException;

    /**
	 * Returns the customer identified by the given user name.
	 * 
	 * @param username the user name for the <code>Customer</code>
	 * to be returned
	 * 
	 * @return the <code>Customer</code> identified by the given 
	 * <code>username</code> if found; <code>null</code> otherwise
	 * 
	 * @throws CustomerNotFoundException if no <code>Customer</code> exists with 
	 * the given <code>username</code>
	 * 
	 */
    public Customer findCustomer(String username) throws CustomerNotFoundException;

    /**
	 * Returns the address identified by the given identifier.
	 * 
	 * @param addressId the unique identifier for the address to be returned
	 * 
	 * @return the address identified by the given <code>addressId</code>
	 * 
	 * @throws AddressNotFoundException if the requested address could not be found
	 */
    public Address findAddress(long addressId) throws AddressNotFoundException;

    /**
	 * Returns the credit card with the given card number.
	 * 
	 * @param customer the <code>Customer</code> owning the card to be returned
	 * 
	 * @param cardNumber the card number for the credit card to find
	 *  
	 * @return the <code>CreditCard</code> with the given <code>cardNumber</code>
	 * 
	 * @throws CreditCardNotFoundException if no credit card exists with the given
	 * <code>cardNumber</code>
	 * 
	 * @throws IllegalArgumentException if the given <code>cardNumber</code> is null
	 */
    public CreditCard findCreditCard(Customer customer, String cardNumber) throws CreditCardNotFoundException;

    /**
	 * Returns all billing addresses for the given customer.
	 *  
	 * @param customerId the id for the customer whose billing addresses
	 * should be returned
	 * 
	 * @return a <code>List</code> of billing <code>Address</code>es for
	 * the given <code>customerId</code>
	 */
    public List getBillingAddresses(long customerId);

    /**
	 * Returns all shipping addresses for the given customer.
	 *  
	 * @param customerId the id for the customer whose shipping addresses
	 * should be returned
	 * 
	 * @return a <code>List</code> of shipping <code>Address</code>es for
	 * the given <code>customerId</code>
	 */
    public List getShippingAddresses(long customerId);

    /**
	 * Returns the credit card identified by the given credit card id.
	 * 
	 * @param creditCardId the unique identifier for the credit card to
	 * be returned 
	 * 
	 * @return the <code>CreditCard</code> identified by the given
	 * <code>creditCardId</code>
	 */
    public CreditCard getCreditCard(long creditCardId);

    /**
	 * Returns a list of credit cards for the given customer id.
	 *  
	 * @param customer the <code>Customer</code> whose credit
	 * cards should be returned
	 * 
	 * @return a <code>List</code> of <code>CreditCard</code>s for the
	 * given <code>customer</code>
	 * 
	 * @throws CreditCardNotFoundException if no credit cards could be found
	 */
    public List getCreditCards(Customer customer) throws CreditCardNotFoundException;

    /**
	 * Saves the given customer as a registered user.
	 * 
	 * @param customer the <code>Customer</code> to register
	 */
    public void registerCustomer(Customer customer);

    /**
	 * Saves the given account.
	 * 
	 * @param account the <code>Account</code> to save
	 * @param customer the <code>Customer</code> owning the given <code>account</code>
	 */
    public void saveAccount(Account account, Customer customer);

    /**
	 * Saves the given address.
	 * 
	 * @param customer the <code>Customer</code> whose <code>address</code>
	 * should be saved
	 * 
	 * @param address the <code>Address</code> to save
	 * 
	 * @return the saved <code>Address</code>
	 */
    public Address saveAddress(Customer customer, Address address);

    /**
	 * Saves the given address as a billing address.
	 * 
	 * @param customer the <code>Customer</code> whose <code>address</code>
	 * should be saved
	 * 
	 * @param address the <code>Address</code> to save
	 * 
	 * @return the saved <code>Address</code>
	 */
    public Address saveBillingAddress(Customer customer, Address address);

    /**
	 * Saves the given address as a shipping address.
	 * 
	 * @param customer the <code>Customer</code> whose <code>address</code>
	 * should be saved
	 * 
	 * @param address the <code>Address</code> to save
	 * 
	 * @return the saved <code>Address</code>
	 */
    public Address saveShippingAddress(Customer customer, Address address);

    /**
	 * Saves the given customer.
	 * 
	 * @param customer the <code>Customer</code> to save
	 */
    public void saveCustomer(Customer customer);

    /**
	 * Saves the given Bill Me Later account.
	 * 
	 * @param billMeLaterAccount the <code>BillMeLaterAccount</code> to save
	 * 
	 * @param customer the <code>Customer</code> whose <code>billMeLaterAccount</code>
	 * is to be saved
	 */
    public void saveBillMeLaterAccount(BillMeLaterAccount billMeLaterAccount, Customer customer);

    /**
	 * Saves and updates the given credit card.
	 * 
	 * @param customer the <code>Customer</code> for whom to save
	 * the given <code>creditCard</code>
	 * 
	 * @param creditCard the <code>CreditCard</code> to be saved
	 */
    public void saveCreditCard(CreditCard creditCard, Customer customer);

    public void savePaymentMethod(PaymentMethod paymentMethod, Customer customer);

    /**
	 * Updates the billing address for the given credit card.
	 * 
	 * @param creditCard the <code>CreditCard</code> to udate (required)
	 * @param billingAddress the new billing <code>Address</code> (required)
	 * @param customer the <code>Customer</code> whose card billing address is being updated
	 * 
	 * @return the updated <code>CreditCard</code>
	 * 
	 * @throws CreditCardNotFoundException if the given <code>creditCard</code> does not exist
	 * in the <code>customer</code>'s wallet
	 */
    public CreditCard updateCreditCardBillingAddress(CreditCard creditCard, Address billingAddress, final Customer customer) throws CreditCardNotFoundException;
}
