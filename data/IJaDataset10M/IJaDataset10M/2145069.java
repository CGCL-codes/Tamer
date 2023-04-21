package com.lb.trac.service;

import java.security.GeneralSecurityException;
import java.util.Iterator;
import java.util.Set;
import com.lb.trac.dao.DAOService;
import com.lb.trac.pojo.Progetti;
import com.lb.trac.pojo.Utenti;
import com.lb.trac.util.TracSetupUtil;

public class AdminService {

    private DAOService daoService;

    private TracSetupUtil tracSetupUtil;

    public Utenti insertUtente(Utenti u) throws GeneralSecurityException {
        try {
            u.setPassword(getTracSetupUtil().md5(u.getUsername() + u.getPassword()));
            u.setPrimoAccesso(true);
            getDaoService().insert(u);
            return u;
        } catch (GeneralSecurityException e) {
            throw e;
        }
    }

    public DAOService getDaoService() {
        return daoService;
    }

    public void setDaoService(DAOService daoService) {
        this.daoService = daoService;
    }

    public TracSetupUtil getTracSetupUtil() {
        return tracSetupUtil;
    }

    public void setTracSetupUtil(TracSetupUtil tracSetupUtil) {
        this.tracSetupUtil = tracSetupUtil;
    }

    public void updateUser(Utenti user) {
        daoService.update(user);
    }
}
