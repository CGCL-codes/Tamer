package ar.com.temporis.system.service;

import ar.com.temporis.system.domain.User;

/**
 * 
 * @author matias.sulik
 * 
 */
public interface LoginService {

    public User autenticate(String username, String password);
}
