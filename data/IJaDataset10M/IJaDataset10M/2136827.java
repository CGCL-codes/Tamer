package com.webmotix.facade;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.webmotix.dao.UserDAO;
import com.webmotix.exception.FacadeException;

/**
 * Regra de neg�cio para inserir usu�rio.
 * @author wsouza
 */
public class MotixUserInsert extends MotixFacadeInsert {

    private static final Logger log = LoggerFactory.getLogger(MotixUserInsert.class);

    /**
	 * Criptografa a senha antes de inserir o usuario e verifica se j� existe 
	 * um usu�rio com mesmo login.
	 */
    @Override
    public void beforeExecute(final MotixParameter param) throws Throwable {
        final String login = param.getValue("login");
        if (UserDAO.existsUser(login, param.getSession())) {
            throw new FacadeException(FacadeException.WARN, "J� existe outro usu�rio com o login {0}.", login);
        }
        final String password = param.getValue("password");
        if (password.length() < 5) {
            throw new FacadeException(FacadeException.WARN, "A senha do usu�rio deve conter no m�nimo 5 caracteres.");
        }
        param.setValue("password", UserDAO.cryptPassword(password));
        param.setDefault("changePassword", String.valueOf(Boolean.FALSE));
        param.setDefault("passwordNeverExpires", String.valueOf(Boolean.FALSE));
        param.setDefault("blocked", String.valueOf(Boolean.FALSE));
    }
}
