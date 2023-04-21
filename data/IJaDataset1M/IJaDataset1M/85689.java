package com.google.code.magja.service.cart;

import com.google.code.magja.model.cart.Cart;
import com.google.code.magja.model.product.Product;
import com.google.code.magja.service.GeneralService;
import com.google.code.magja.service.ServiceException;

/**
 * @author schneider
 * 
 */
public interface CartRemoteService extends GeneralService<Cart> {

    /**
	 * create new cart for store
	 * 
	 * @param storeid
	 * @return
	 * @throws ServiceException
	 */
    public abstract Cart create(Integer store) throws ServiceException;

    /**
	 * set customer
	 * 
	 * @param cart
	 * @param customer
	 * @return
	 * @throws ServiceException
	 */
    public abstract void setCustomer(Cart cart) throws ServiceException;

    /**
	 * get cart license agreements
	 * 
	 * @param cart
	 * @throws ServiceException
	 */
    public abstract void getLicenseAgreements(Cart cart) throws ServiceException;

    /**
	 * retrieve cart totals
	 * 
	 * @param cart
	 * @throws ServiceException
	 */
    public abstract void getTotals(Cart cart) throws ServiceException;

    /**
	 * get cart by quote id
	 * 
	 * @param id
	 * @param storeId
	 * @return
	 * @throws ServiceException
	 */
    public abstract Cart getById(Integer id, Integer storeId) throws ServiceException;

    /**
	 * create an order from cart
	 * 
	 * @param cart
	 * @throws ServiceException
	 */
    public abstract void order(Cart cart) throws ServiceException;

    /**
	 * set shipping and billing addresses
	 * 
	 * @param cart
	 * @throws ServiceException
	 */
    public abstract void setAddresses(Cart cart) throws ServiceException;

    /**
	 * add product to shopping cart
	 * 
	 * @param cart
	 * @param product
	 * @param quantity
	 * @throws ServiceException
	 */
    public abstract void addProduct(Cart cart, Product product, double quantity) throws ServiceException;
}
