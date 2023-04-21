package org.didicero.base.service;

import java.net.URI;
import java.util.Date;
import org.didicero.base.entity.ModuleRole;
import org.didicero.base.entity.ModuleRoleDaoException;
import org.didicero.base.entity.Role;
import org.didicero.base.entity.RoleDaoException;
import org.didicero.base.entity.SystemRole;
import org.didicero.base.entity.SystemRoleDaoException;
import org.didicero.base.entity.User;
import org.didicero.base.entity.UserDaoException;
import org.didicero.base.types.ModuleRoleType;
import org.didicero.base.types.SystemRoleType;

public class IntegrityServiceBean extends org.didicero.base.service.IntegrityServiceBase implements org.didicero.base.service.IntegrityServiceRemote {

    /**
     * Default constructor extending base class default constructor
     */
    public IntegrityServiceBean() {
        super();
    }

    /**
     * @throws UserDaoException 
     * @throws RoleDaoException 
     * @see org.didicero.base.service.IntegrityServiceBase#checkDatabaseDefaults()
     */
    protected void handleCheckDatabaseDefaults() throws UserDaoException, RoleDaoException {
        SystemRole adminRole;
        try {
            adminRole = this.getSystemRoleDao().findSystemRoleByRolename("SysAdmin");
        } catch (SystemRoleDaoException e) {
            adminRole = new SystemRole();
            adminRole.setDescription("System administrator");
            adminRole.setRolename("SysAdmin");
            adminRole.setRolegroup("SysAdmin");
            adminRole.setRole(SystemRoleType.ADMIN);
            try {
                this.getSystemRoleDao().create(adminRole);
            } catch (SystemRoleDaoException e1) {
                throw new RoleDaoException("Cannot create role SysAdmin");
            }
        }
        ModuleRole readerRole;
        try {
            readerRole = this.getModuleRoleDao().findModuleRoleByRolename("Guest");
        } catch (ModuleRoleDaoException e) {
            readerRole = new ModuleRole();
            readerRole.setDescription("User/guest with read access only");
            readerRole.setRolename("Reader");
            readerRole.setRolegroup("Reader");
            readerRole.setRole(ModuleRoleType.GUEST);
            try {
                this.getModuleRoleDao().create(readerRole);
            } catch (ModuleRoleDaoException e1) {
                throw new RoleDaoException("Cannot create role Guest");
            }
        }
        ModuleRole authorRole;
        try {
            authorRole = this.getModuleRoleDao().findModuleRoleByRolename("Author");
        } catch (ModuleRoleDaoException e) {
            authorRole = new ModuleRole();
            authorRole.setDescription("User with read and write access for his modules");
            authorRole.setRolename("Author");
            authorRole.setRolegroup("Author");
            authorRole.setRole(ModuleRoleType.AUTHOR);
            try {
                this.getModuleRoleDao().create(authorRole);
            } catch (ModuleRoleDaoException e1) {
                throw new RoleDaoException("Cannot create role Author");
            }
        }
        ModuleRole learnerRole;
        try {
            learnerRole = this.getModuleRoleDao().findModuleRoleByRolename("Learner");
        } catch (ModuleRoleDaoException e) {
            learnerRole = new ModuleRole();
            learnerRole.setDescription("User with read  access for his modules");
            learnerRole.setRolename("Learner");
            learnerRole.setRolegroup("Learner");
            learnerRole.setRole(ModuleRoleType.LEARNER);
            try {
                this.getModuleRoleDao().create(learnerRole);
            } catch (ModuleRoleDaoException e1) {
                throw new RoleDaoException("Cannot create role Learner");
            }
        }
        ModuleRole publishRole;
        try {
            publishRole = this.getModuleRoleDao().findModuleRoleByRolename("Publisher");
        } catch (ModuleRoleDaoException e) {
            publishRole = new ModuleRole();
            publishRole.setDescription("User with right to publish his modules");
            publishRole.setRolename("Publisher");
            publishRole.setRolegroup("Publisher");
            publishRole.setRole(ModuleRoleType.PUBLISHER);
            try {
                this.getModuleRoleDao().create(publishRole);
            } catch (ModuleRoleDaoException e1) {
                throw new RoleDaoException("Cannot create role Publisher");
            }
        }
        User tmp = new User();
        Date now = new Date();
        Long millis = now.getTime();
        Long pass = new Long(tmp.hashCode() + millis.hashCode());
        tmp.setLastname("system");
        tmp.setFirstname("admin");
        tmp.setUri("didicero://" + this.didiceroInstance + "/su/" + tmp.getLastname() + "/" + tmp.getFirstname());
        User su;
        try {
            su = this.getUserDao().findUserByUri(tmp.getUri());
        } catch (UserDaoException e) {
            tmp.setPassword("p" + pass.toString());
            tmp.setCreatedate(now);
            tmp.setChangedate(now);
            tmp.setChangeuser("System");
            tmp.setCreateuser("System");
            su = this.getUserDao().create(tmp);
        }
        tmp = new User();
        now = new Date();
        tmp.setLastname("system");
        tmp.setFirstname("annonymous");
        tmp.setUri("didicero://" + this.didiceroInstance + "/user/" + tmp.getLastname() + "/" + tmp.getFirstname());
        User annonymous;
        try {
            annonymous = this.getUserDao().findUserByUri(tmp.getUri());
        } catch (UserDaoException e) {
            tmp.setPassword("annonymous");
            tmp.setCreatedate(now);
            tmp.setChangedate(now);
            tmp.setChangeuser("System");
            tmp.setCreateuser("System");
            annonymous = this.getUserDao().create(tmp);
        }
    }

    /**
     * @see org.didicero.base.service.IntegrityServiceBase#recreateDatabaseDefaults()
     */
    protected void handleRecreateDatabaseDefaults() throws java.lang.Exception {
        throw new java.lang.UnsupportedOperationException("org.didicero.base.service.IntegrityServiceBean.handleRecreateDatabaseDefaults() Not implemented!");
    }
}
