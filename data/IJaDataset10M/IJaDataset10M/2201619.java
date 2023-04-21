package edu.uah.elearning.qti.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.security.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.uah.elearning.qti.service.security.SecurityUser;
import edu.uah.elearning.qti.service.UserService;
import edu.uah.elearning.qti.dao.RoleDAO;
import edu.uah.elearning.qti.dao.UserDAO;
import edu.uah.elearning.qti.dao.jpa.HibernateUtil;
import edu.uah.elearning.qti.model.Trole;
import edu.uah.elearning.qti.model.Tuser;
import edu.uah.elearning.qti.model.TuserTrole;

/**
 * Clase de servicio de usuarios.<br>
 * Implementa las operaciones sobre los usuarios del sistema.
 * 
 * @author emoriana
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    private final Log log = LogFactory.getLog(UserServiceImpl.class);

    private UserDAO userDAO;

    private Md5PasswordEncoder passwordEncoder;

    private RoleDAO roleDAO = null;

    private List<Tuser> inMemoryTusers;

    public void setInMemoryTusers(List<Tuser> inMemoryTuser) {
        for (Tuser tuser : inMemoryTuser) tuser.setPassword(passwordEncoder.encodePassword(tuser.getPassword(), null));
        this.inMemoryTusers = inMemoryTuser;
    }

    @Transactional(readOnly = true)
    public Tuser getUser(String login) {
        Tuser user = null;
        if (inMemoryTusers != null) user = getUserInMemory(login);
        if (user == null) user = getUserDAO().getUser(login);
        return user;
    }

    private Tuser getUserInMemory(String login) {
        Tuser tuser = null;
        for (Tuser tuserVar : inMemoryTusers) {
            if (tuserVar.getLogin().equals(login)) {
                tuser = tuserVar;
                break;
            }
        }
        return tuser;
    }

    @Transactional(readOnly = true)
    public Tuser getUserById(int userId) {
        Tuser user = getUserDAO().getUserById(userId);
        if (user == null) {
            throw new ObjectRetrievalFailureException(Tuser.class, userId);
        }
        return user;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    @Autowired
    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Md5PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(Md5PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void createStudent(Tuser tuser) {
        Collection<TuserTrole> collection = new ArrayList<TuserTrole>();
        Trole trole = new Trole(TROLE_STUDENT_ID);
        TuserTrole tuserTrole = new TuserTrole();
        tuserTrole.setRoleId(trole);
        tuserTrole.setUserId(tuser);
        collection.add(tuserTrole);
        tuser.setTuserTroleCollection(collection);
        createUser(tuser);
    }

    public void createUserMD5(Tuser user) {
        String passwordMD5 = passwordEncoder.encodePassword(user.getPassword(), null);
        user.setPassword(passwordMD5);
        createUser(user);
    }

    @Transactional
    public void createRole(Trole trole) {
        roleDAO.persist(trole);
    }

    @Transactional
    public void createUser(Tuser user) {
        Date now = Calendar.getInstance().getTime();
        user.setCreationDate(now);
        user.setActive(true);
        userDAO.persist(user);
    }

    @Transactional
    public void initDataBase() {
        log.warn("Testing Database.");
        Trole studentRole = new Trole();
        studentRole.setLkey("ROLE_USER");
        studentRole.setRoleId(2);
        roleDAO.persist(studentRole);
        Trole teacherRole = new Trole();
        teacherRole.setLkey("ROLE_ADMIN");
        teacherRole.setRoleId(1);
        roleDAO.persist(teacherRole);
        Tuser adminUser = new Tuser();
        adminUser.setLogin("admin");
        adminUser.setPassword("admin");
        adminUser.setEmail("");
        adminUser.setCreationDate(new Date());
        Collection<TuserTrole> roles = new ArrayList<TuserTrole>();
        TuserTrole tuserTrole = new TuserTrole();
        tuserTrole.setRoleId(studentRole);
        tuserTrole.setUserId(adminUser);
        roles.add(tuserTrole);
        TuserTrole tuserTrole2 = new TuserTrole();
        tuserTrole2.setRoleId(teacherRole);
        tuserTrole2.setUserId(adminUser);
        roles.add(tuserTrole2);
        adminUser.setTuserTroleCollection(roles);
        createUser(adminUser);
    }

    @Transactional(readOnly = true)
    public List<Tuser> getListUsers() {
        return userDAO.loadAll();
    }

    public void updateUser(Tuser user) {
        userDAO.update(user);
    }

    public void updateUserMD5(Tuser user) {
        String passwordMD5 = passwordEncoder.encodePassword(user.getPassword(), null);
        user.setPassword(passwordMD5);
        userDAO.update(user);
    }

    public RoleDAO getRoleDAO() {
        return roleDAO;
    }

    @Autowired
    public void setRoleDAO(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    public void removeUser(String login) {
        userDAO.delete(getUser(login));
    }

    public List<Trole> getListRoles() {
        return roleDAO.loadAll();
    }

    public Trole loadById(Integer id) {
        return roleDAO.loadById(id);
    }

    public Tuser getUserInSession() {
        Tuser usuario = null;
        User u = getSecurityUser();
        if (u != null) {
            usuario = userDAO.getUser(u.getUsername());
        } else {
        }
        return usuario;
    }

    private SecurityUser getSecurityUser() {
        SecurityUser securityUser = null;
        if ((SecurityContextHolder.getContext() == null) || !(SecurityContextHolder.getContext() instanceof org.springframework.security.context.SecurityContext) || (SecurityContextHolder.getContext().getAuthentication() == null) || !(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof SecurityUser)) return securityUser; else securityUser = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return securityUser;
    }

    public void setSessionProperty(String key, Object value) {
        SecurityUser securityUser = getSecurityUser();
        if (securityUser != null) securityUser.addProperty(key, value); else {
        }
    }

    public Object getSessionProperty(String key) {
        SecurityUser securityUser = getSecurityUser();
        if (securityUser != null) return securityUser.getProperty(key); else {
            return null;
        }
    }

    public void clearSessionProperty(String key) {
        SecurityUser securityUser = getSecurityUser();
        if (securityUser != null) securityUser.clearProperty(key); else {
        }
    }

    public List<Tuser> getAlumnos() {
        List<Tuser> users = getListUsers();
        Trole trole = new Trole();
        trole.setRoleId(TROLE_STUDENT_ID);
        for (Tuser tuser : users) {
            if (tuser.getTuserTroleCollection().contains(trole)) users.remove(tuser);
        }
        return users;
    }
}
