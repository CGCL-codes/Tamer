package com.fdv.template.service.impl;

import com.fdv.template.domain.persistence.UserDao;
import com.fdv.template.domain.user.User;
import com.fdv.template.service.MailService;

/**
 * @author Mariano Simone (mariano.simone@fdvsolutions.com)
 *
 */
public interface ForgottenPasswordStrategy {

    void manageForgottenPassword(final User user);

    void setDao(UserDao dao);

    void setMailService(MailService mailService);
}
