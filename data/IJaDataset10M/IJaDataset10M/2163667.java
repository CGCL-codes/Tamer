package com.prossys.facade;

import com.prossys.dao.CustomerDAO;
import com.prossys.dao.PersistenceException;
import com.prossys.model.Customer;
import com.prossys.model.Price;
import com.prossys.model.Product;
import com.prossys.model.Rate;
import com.prossys.model.Type;
import com.prossys.util.Encryption;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author Victor
 */
public class CustomerFacade extends BaseFacade {

    Logger logger = Logger.getLogger(this.getClass().getName());

    public void updateCustomerData(Customer customer) throws FacadeException {
        logger.info("Updating customer data...");
        try {
            String encryptedPassword = Encryption.encrypt(customer.getPassword());
            customer.setPassword(encryptedPassword);
            new CustomerDAO().updateCustomer(customer);
        } catch (PersistenceException ex) {
            throw new FacadeException(ex);
        }
        logger.info("Company data updated successfully.");
    }

    public List<Price> getPriceListByName(String name) throws FacadeException {
        logger.info("Listing prices...");
        List<Price> listPrices = null;
        try {
            listPrices = new CustomerDAO().searchProductByName(name);
            Iterator<Price> it = listPrices.iterator();
            while (it.hasNext()) {
                Price price = (Price) it.next();
                DecimalFormat df = new DecimalFormat("#,##0.00");
                price.setPriceAsString(df.format(price.getPrice()));
            }
        } catch (PersistenceException ex) {
            throw new FacadeException(ex);
        }
        logger.info("Getting list of prices complete.");
        return listPrices;
    }

    public void orderPricesByPrice(List<Price> list) throws FacadeException {
        logger.info("Listing prices ordered by price...");
        try {
            Collections.sort(list, new Comparator<Price>() {

                public int compare(Price price1, Price price2) {
                    if (price1.getPrice() > price2.getPrice()) return 1;
                    if (price1.getPrice() < price2.getPrice()) return -1;
                    return 0;
                }
            });
        } catch (Exception ex) {
            throw new FacadeException(ex);
        }
        logger.info("Getting list of prices ordered by price complete.");
    }

    public void orderPricesByCity(List<Price> list) throws FacadeException {
        logger.info("Listing prices ordered by city...");
        try {
            Collections.sort(list, new Comparator<Price>() {

                public int compare(Price price1, Price price2) {
                    return price1.getId().getStore().getCity().compareToIgnoreCase(price2.getId().getStore().getCity());
                }
            });
        } catch (Exception ex) {
            throw new FacadeException(ex);
        }
        logger.info("Getting list of prices ordered by city complete.");
    }

    public void orderPricesByStore(List<Price> list) throws FacadeException {
        logger.info("Listing prices ordered by store...");
        try {
            Collections.sort(list, new Comparator<Price>() {

                public int compare(Price price1, Price price2) {
                    return price1.getId().getStore().getTradingName().compareToIgnoreCase(price2.getId().getStore().getTradingName());
                }
            });
        } catch (Exception ex) {
            throw new FacadeException(ex);
        }
        logger.info("Getting list of prices ordered by store complete.");
    }

    public void orderPricesByCompany(List<Price> list) throws FacadeException {
        logger.info("Listing prices ordered by company...");
        try {
            Collections.sort(list, new Comparator<Price>() {

                public int compare(Price price1, Price price2) {
                    return price1.getId().getStore().getCompany().getTradingName().compareToIgnoreCase(price2.getId().getStore().getCompany().getTradingName());
                }
            });
        } catch (Exception ex) {
            throw new FacadeException(ex);
        }
        logger.info("Getting list of prices ordered by store complete.");
    }

    public void insertRate(Rate rate) throws FacadeException {
        logger.info("Inserting rate...");
        try {
            new CustomerDAO().insertRate(rate);
        } catch (PersistenceException ex) {
            throw new FacadeException(ex);
        }
        logger.info("Rate included successfully.");
    }

    public List<Rate> getRateListByProduct(Product product) throws FacadeException {
        logger.info("Listing rates...");
        List<Rate> listRate = null;
        try {
            listRate = new CustomerDAO().listCustomerRate(product);
        } catch (PersistenceException ex) {
            throw new FacadeException(ex);
        }
        logger.info("Getting list of prices complete.");
        return listRate;
    }

    public void insertCustomer(Customer customer) throws FacadeException {
        logger.info("Inserting customer...");
        try {
            String encryptedPassword = Encryption.encrypt(customer.getPassword());
            customer.setPassword(encryptedPassword);
            customer.setRegistrationDate(new Date(System.currentTimeMillis()));
            new CustomerDAO().insertCustomer(customer);
        } catch (PersistenceException ex) {
            throw new FacadeException(ex);
        }
        logger.info("Customer included successfully.");
    }

    public boolean isProductRatedbyCustomer(Product product, Customer customer) throws FacadeException {
        logger.info("Is product rated by customer function inicialized...");
        try {
            return new CustomerDAO().isProductRatedbyCustomer(product, customer);
        } catch (PersistenceException ex) {
            throw new FacadeException(ex);
        }
    }

    public Float getAverageGrade(List<Rate> list) throws FacadeException {
        logger.info("Getting average grade...");
        Float sum = 0F;
        int count = 0;
        Iterator<Rate> it = list.iterator();
        while (it.hasNext()) {
            Rate rate = (Rate) it.next();
            sum += rate.getGrade();
            count++;
        }
        if (count == 0) return null;
        return sum / count;
    }
}
