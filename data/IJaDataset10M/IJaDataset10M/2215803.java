package de.meylirh.test.service.impl;

import java.util.Date;
import java.util.List;
import de.meylirh.test.dao.LoginDAO;
import de.meylirh.test.domain.Login;
import de.meylirh.test.service.LoginService;

public class LoginServiceImpl implements LoginService {

    private LoginDAO dao;

    public List<Login> getAllLogins() {
        return dao.list();
    }

    public void login(String name) {
        assert (name != null);
        Login l = new Login();
        l.setLoginTimeStamp(new Date());
        l.setName(name);
        dao.store(l);
    }

    public void setLoginDAO(LoginDAO dao) {
        this.dao = dao;
    }
}
