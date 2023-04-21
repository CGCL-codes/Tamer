package com.cci.bmc.validation;

import com.cci.bmc.domain.StaticAddress;
import com.cci.bmc.service.AccountService;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class CrvLookupValidator extends FieldValidatorSupport {

    public void validate(Object object) throws ValidationException {
        String fieldName = getFieldName();
        String fieldValue = (String) getFieldValue(fieldName, object);
        String ipAddress = accountService.lookupIpAddress(fieldValue);
        if (ipAddress != null) {
            StaticAddress address = accountService.getStaticAddressByIp(ipAddress);
            if (address == null) {
                addFieldError(fieldName, object);
            }
        } else {
            addFieldError(fieldName, object);
        }
    }

    private AccountService accountService;

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
