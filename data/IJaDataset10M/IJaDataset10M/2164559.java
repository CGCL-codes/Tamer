package br.rmpestano.finantial.service;

import br.rmpestano.finantial.model.User;
import br.rmpestano.finantial.service.generic.CrudService;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author rmpestano
 */
@Named("userService")
@RequestScoped
public class UserService implements Serializable {

    private static final String FIND_BY_LOGIN = "User.findByLogin";

    private static final String FIND_ALL = "User.findAll";

    @EJB
    CrudService<User> crudService;

    public User findByLogin(String username) {
        HashMap hash = new HashMap();
        hash.put("username", username);
        User resultList = crudService.findWithTypedQuery(User.class, "username", username);
        if (resultList != null) {
            return resultList;
        } else {
            return null;
        }
    }

    public User findById(String id) {
        User u = crudService.findById(Long.parseLong(id), User.class);
        if (u != null) {
            return u;
        } else {
            return null;
        }
    }

    public List<User> findAll() {
        return crudService.findWithNamedQuery(FIND_ALL);
    }

    public void incluir(User usuario) throws Exception {
        crudService.create(usuario);
    }

    public User atualizar(User usuario) throws Exception {
        return crudService.update(usuario);
    }

    public void removeUser(User user) {
        crudService.delete(user.getId(), User.class);
    }
}
