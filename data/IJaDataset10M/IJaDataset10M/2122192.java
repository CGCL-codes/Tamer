package org.az.paccman.services;

import org.az.model.Account;
import org.az.tb.common.vo.client.UserProfileVo;
import org.az.tb.common.vo.client.exceptions.AuthenticationException;
import org.az.tb.common.vo.client.exceptions.TechException;
import org.az.tb.common.vo.client.exceptions.ValidationException;

public interface RegistrationService {

    /**
     * receives request from the registration form.
     */
    public void register(UserProfileVo form) throws AuthenticationException, TechException, ValidationException;

    public abstract String createConfirmationKey(Account account);
}
