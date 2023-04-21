package org.cmsuite2.business.validator;

import it.ec.commons.security.spring.jpa.model.account.Account;
import it.ec.commons.web.ValidateBean;
import it.ec.commons.web.ValidateException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.cmsuite2.model.country.Country;
import org.cmsuite2.model.customer.Customer;

public class CustomerValidator {

    private Logger logger = Logger.getLogger(CustomerValidator.class);

    private Pattern mailPattern = Pattern.compile("^([-+\\w\\.]*)@([\\w\\-]+\\.)+[A-Z_a-z]{2,4}$");

    private Pattern usernamePattern = Pattern.compile("^[a-z_A-Z][\\w\\- ]*?[\\w\\-]$");

    private Pattern passwordPattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z_A-Z]).{6,64}$");

    public void validateStep1(Customer customer) throws ValidateException {
        if (logger.isDebugEnabled()) logger.debug("validateStep1[" + customer + "]");
        List<ValidateBean> errors = new ArrayList<ValidateBean>();
        if (customer != null) {
            String name = customer.getName();
            if (StringUtils.isNotEmpty(name) && name.length() > 255) errors.add(new ValidateBean("customer.name", "message.name.nv"));
            String firstName = customer.getFirstName();
            if (StringUtils.isEmpty(firstName)) errors.add(new ValidateBean("customer.firstName", "message.firstName.em")); else if (firstName.length() > 100) errors.add(new ValidateBean("customer.firstName", "message.firstName.nv"));
            String lastName = customer.getLastName();
            if (StringUtils.isEmpty(lastName)) errors.add(new ValidateBean("customer.lastName", "message.lastName.em")); else if (lastName.length() > 100) errors.add(new ValidateBean("customer.lastName", "message.lastName.nv"));
            String middleName = customer.getMiddleName();
            if (StringUtils.isNotEmpty(middleName) && middleName.length() > 100) errors.add(new ValidateBean("customer.middleName", "message.middleName.nv"));
            String vatCode = customer.getVatCode();
            if (StringUtils.isNotEmpty(vatCode) && vatCode.length() > 50) errors.add(new ValidateBean("customer.vatCode", "message.vatCode.nv"));
            String address = customer.getAddress();
            if (StringUtils.isEmpty(address)) errors.add(new ValidateBean("customer.address", "message.address.em")); else if (address.length() > 255) errors.add(new ValidateBean("customer.address", "message.address.nv"));
            String city = customer.getCity();
            if (StringUtils.isEmpty(city)) errors.add(new ValidateBean("customer.city", "message.city.em")); else if (city.length() > 100) errors.add(new ValidateBean("customer.city", "message.city.nv"));
            Country country = customer.getCountry();
            if (country == null) errors.add(new ValidateBean("countryId", "message.country.em"));
            String zipCode = customer.getZipCode();
            if (StringUtils.isEmpty(zipCode)) errors.add(new ValidateBean("customer.zipCode", "message.zipCode.em")); else if (zipCode.length() > 50) errors.add(new ValidateBean("customer.zipCode", "message.zipCode.nv"));
            String mail = customer.getMail();
            if (StringUtils.isNotEmpty(mail) && (!mailPattern.matcher(mail).matches() || mail.length() > 255)) errors.add(new ValidateBean("customer.mail", "message.mail.nv"));
            String phone = customer.getPhone();
            if (StringUtils.isNotEmpty(phone) && phone.length() > 16) errors.add(new ValidateBean("customer.phone", "message.phone.nv"));
            Account account = customer.getAccount();
            if (account != null) {
                String username = account.getUsername();
                if (StringUtils.isEmpty(username)) errors.add(new ValidateBean("account.username", "message.username.em")); else if (!usernamePattern.matcher(username).matches() || username.length() < 4 || username.length() > 64) errors.add(new ValidateBean("account.username", "message.username.nv"));
                String password = account.getPassword();
                if (StringUtils.isEmpty(password)) errors.add(new ValidateBean("account.password", "message.password.em")); else if (!passwordPattern.matcher(password).matches() || password.length() < 6 || password.length() > 64) errors.add(new ValidateBean("account.password", "message.password.nv"));
                String passwordConfirm = account.getPasswordConfirm();
                if (StringUtils.isEmpty(passwordConfirm)) errors.add(new ValidateBean("account.passwordConfirm", "message.passwordConfirm.em")); else if (!passwordConfirm.equals(password)) errors.add(new ValidateBean("account.passwordConfirm", "message.passwordConfirm.nv"));
            } else errors.add(new ValidateBean("account.username", "message.username.em"));
        } else {
            throw new ValidateException();
        }
        if (errors.size() > 0) throw new ValidateException(errors);
    }
}
