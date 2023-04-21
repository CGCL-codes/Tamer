package su.mvc.solo.service;

import org.springframework.orm.jpa.JpaTemplate;
import org.springframework.transaction.annotation.Transactional;
import su.mvc.solo.model.DomainEntity;
import su.mvc.solo.model.User;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Rustam Ismailov
 * Date: Nov 15, 2009 : 3:30:30 PM
 */
@Transactional
public class UserService extends ServiceSupport {

    public void createEntity(DomainEntity domainEntity) {
        jpaTemplate.persist(domainEntity);
    }

    public void removeEntity(DomainEntity domainEntity) {
        jpaTemplate.remove(domainEntity);
    }

    public DomainEntity getEntity(Class clazz, Long id) {
        return (DomainEntity) jpaTemplate.find(clazz, id);
    }

    public List findAllEntities(Class clazz) {
        return jpaTemplate.find("Select de from " + clazz.getName() + " de");
    }

    public void updateEntity(User user) {
        User u = jpaTemplate.find(User.class, user.getId());
        u.setFirstName(user.getFirstName());
        u.setLastName(user.getLastName());
        u.setEmail(user.getEmail());
        u.setVersion(user.getVersion());
    }

    public List<User> findAllUsers() {
        return findAllEntities(User.class);
    }

    public User getUser(Long id) {
        return (User) getEntity(User.class, id);
    }
}
