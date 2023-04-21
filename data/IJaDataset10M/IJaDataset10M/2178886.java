package org.magicbox.admin.service;

import org.magicbox.admin.dao.RolesDao;

/**
 * Implementazione Interfaccia di gestione dei ruoli
 * 
 * @author Massimiliano Dess&igrave; (desmax74@yahoo.it)
 * @since jdk 1.6.0
 * @version 3.0
 */
public class RolesServiceImpl implements RolesService {

    public boolean deleteFromRoles(String username) {
        return rolesDao.deleteFromRoles(username);
    }

    public boolean insertInRoles(String username, String role) {
        return rolesDao.insertInRoles(username, role);
    }

    public void setRolesDao(RolesDao rolesDao) {
        this.rolesDao = rolesDao;
    }

    private RolesDao rolesDao;
}
