package com.finavi.ejb;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.finavi.ejb.eao.UserEAO;
import com.finavi.model.Role;
import com.finavi.model.User;

/**
 * @author matus
 *
 */
@Local(value = UserServiceLocal.class)
@Remote(value = UserServiceRemote.class)
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UserServiceBean implements UserServiceLocal {

    @PersistenceContext
    EntityManager em;

    UserEAO eao;

    public UserServiceBean() {
    }

    private UserEAO getEAO() {
        if (this.eao == null) {
            this.eao = new UserEAO(this.em);
        }
        return eao;
    }

    @Override
    public List<User> getAll() {
        return getEAO().getAll();
    }

    @Override
    public User getById(long id) {
        return getEAO().getById(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public User add(User user) {
        return getEAO().save(user);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public User update(User user) {
        return getEAO().save(user);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public User delete(User user) {
        return getEAO().delete(user);
    }

    @Override
    public User getByName(String name) {
        List<User> list = getEAO().findByName(name);
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public User getByEmail(String email) {
        List<User> list = getEAO().findByEmail(email);
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public User register(User user) {
        Set<Role> defRoles = new HashSet<Role>();
        defRoles.add(em.find(Role.class, 1l));
        user.setRoles(defRoles);
        Object object = add(user);
        if (object != null) {
            return (User) object;
        }
        return null;
    }

    @Override
    public User login(String email, String password) {
        return getEAO().findByEmailAndPassword(email, password);
    }

    @Override
    public List<User> search(String name, String surname) {
        System.out.println(name + " " + surname);
        if (name == null) {
            name = "";
        }
        if (surname == null) {
            surname = "";
        }
        Query q = em.createQuery("from User u where u.name like :name and u.surname like :surname");
        q.setParameter("name", name + "%");
        q.setParameter("surname", surname + "%");
        return (List<User>) q.getResultList();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public User switchToAgent(User user) {
        user.getRoles().clear();
        Role r = em.find(Role.class, 2l);
        user.getRoles().add(r);
        return eao.save(user);
    }
}
