package br.com.yaw.struts2.action;

import br.com.yaw.entity.User;
import br.com.yaw.exception.ServiceException;
import br.com.yaw.ioc.ServiceFactory;
import br.com.yaw.service.UserService;

public class LoginAction extends BaseAction {

    private String username;

    private String password;

    private UserService userService;

    public String login() {
        User user;
        String retorno = INPUT;
        try {
            if (username == null || username.length() <= 0) {
                throw new IllegalArgumentException("Usu�rio/Senha inv�lido.");
            }
            userService = ServiceFactory.getService(UserService.class);
            user = userService.authenticate(username, password);
            if (user != null) {
                getSession().put("loggedUser", user);
                addActionMessage("Login com sucesso!");
                retorno = SUCCESS;
            } else {
                addActionError("Usu�rio/Senha inv�lido.");
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            addActionError(e.getMessage());
        } catch (IllegalArgumentException ie) {
            addActionError(ie.getMessage());
        }
        return retorno;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
