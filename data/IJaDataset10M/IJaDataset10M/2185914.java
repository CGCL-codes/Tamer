package br.com.jnfe.connect.service;

import org.helianto.core.User;
import org.helianto.core.security.PublicUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 
 * @author mauriciofernandesdecastro
 */
public class UserExtractor {

    /**
	 * 
	 * @return
	 */
    public static User getUserFromSecurityContext() {
        PublicUserDetails currentUser = (PublicUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = currentUser.getUser();
        logger.info("Usu�rio autorizado: {}.", user);
        logger.info("Empresa autorizada: {}.", user.getEntity());
        return user;
    }

    private static final Logger logger = LoggerFactory.getLogger(UserExtractor.class);
}
