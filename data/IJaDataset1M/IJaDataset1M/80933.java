package com.maicuole.user.login;

/**
 * 
 * 
 * @author kui.lijk
 * @version $Id: UserRegisterService.java, v 0.1 2009-2-25 下午 10:11:42 kui.lijk Exp $
 */
public interface UserRegisterService {

    UserRegisterResult doRegister(final UserRegisterDTO input);

    String getEmail(final String testEmail);
}
