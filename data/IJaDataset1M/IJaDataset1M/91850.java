package net.sourceforge.iwii.db.dev.logic.fascade.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.iwii.db.dev.common.exceptions.FascadeProcessException;
import net.sourceforge.iwii.db.dev.common.localization.LocalizationKeysRepository;
import net.sourceforge.iwii.db.dev.common.utils.ServiceInjector;
import net.sourceforge.iwii.db.dev.logic.fascade.api.ISecurityFascade;
import net.sourceforge.iwii.db.dev.security.api.ISecurityProvider;

/**
 * Implements ISecurityFascade
 * 
 * @author Grzegorz 'Gregor736' Wolszczak
 * @version 1.00
 */
public class SecurityFascade implements ISecurityFascade {

    private static final Logger logger = Logger.getLogger(SecurityFascade.class.getName());

    private ISecurityProvider securityProvider;

    public SecurityFascade() {
        long start = System.currentTimeMillis();
        SecurityFascade.logger.log(Level.INFO, "Initializating...");
        this.securityProvider = ServiceInjector.injectSingletonService(ISecurityProvider.class);
        SecurityFascade.logger.log(Level.INFO, "Initialization done in " + String.valueOf(System.currentTimeMillis() - start) + " [ms]");
    }

    @Override
    public void loginUser(String userLogin, String password) throws FascadeProcessException {
        SecurityFascade.logger.log(Level.INFO, "Performing login operation...");
        if (userLogin == null || userLogin.trim().equals("")) {
            throw new FascadeProcessException(LocalizationKeysRepository.Login.ERROR_INPUT_NO_USER);
        }
        if (password == null || password.trim().equals("")) {
            throw new FascadeProcessException(LocalizationKeysRepository.Login.ERROR_INPUT_NO_PASSWORD);
        }
        String hashedPassword = this.securityProvider.hashString(password);
        Boolean isUserCorrect = this.securityProvider.isLoginInformationCorrect(userLogin, hashedPassword);
        this.securityProvider.logLoginAttemp(userLogin, isUserCorrect);
        if (!isUserCorrect) {
            throw new FascadeProcessException(LocalizationKeysRepository.Login.ERROR_WRONG_USER_PASSWORD);
        }
        this.securityProvider.performLoginOperation(userLogin);
    }

    public boolean isUserLogged() {
        return this.securityProvider.isUserLogged();
    }
}
