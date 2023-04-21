package com.pricerunner.aop.model;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Autowire;
import org.apache.commons.lang.StringUtils;
import com.pricerunner.aop.repository.RetailerDAO;
import com.sun.org.apache.xerces.internal.util.MessageFormatter;
import java.text.MessageFormat;

/**
 * @author: gmola, giulio.mola@gmail.com
 */
@Configurable(autowire = Autowire.BY_NAME, dependencyCheck = true)
public class Retailer {

    private RetailerDAO retailerDAO;

    private String name;

    private String mailAddress;

    /**
     * this is the setter that will be used for AOP
     * dependency injection weaving
     *
     * @param retailerDAO retailer repository
     */
    public void setRetailerDAO(RetailerDAO retailerDAO) {
        this.retailerDAO = retailerDAO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMailAddress() {
        return mailAddress;
    }

    public void setMailAddress(String mailAddress) {
        this.mailAddress = mailAddress;
    }

    public boolean save() {
        assert (StringUtils.isNotEmpty(this.name) && StringUtils.isNotEmpty(this.mailAddress)) : "invalid name or mail address: " + this;
        return retailerDAO.persist(this);
    }

    public String toString() {
        return MessageFormat.format("[name:${0}, mail:${1}]", this.name, this.mailAddress);
    }
}
