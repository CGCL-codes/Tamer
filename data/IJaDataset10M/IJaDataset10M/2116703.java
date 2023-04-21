package org.openedc.web.showcase.service.bmp;

import org.openedc.core.domain.service.UserEntityService;
import java.util.List;
import javax.inject.Named;
import org.openedc.core.domain.model.User;
import org.openedc.core.domain.model.support.UserEntity;
import org.openedc.core.domain.service.exceptions.NonexistentEntityException;

/**
 *
 * @author openedc
 */
@Named
public class UserEntityJpaController implements UserEntityService {

    DefaultJpaController entityService = new DefaultJpaController() {

        @Override
        public Class getEntityType() {
            return UserEntity.class;
        }
    };

    @Override
    public void create(User user) throws Exception {
        entityService.create(user);
    }

    @Override
    public void destroy(Long id) throws NonexistentEntityException {
        entityService.destroy(id);
    }

    @Override
    public void edit(User user) throws NonexistentEntityException, Exception {
        entityService.edit(user);
    }

    @Override
    public User find(Long id) throws Exception {
        return (User) entityService.find(id);
    }

    @Override
    public List<User> findAll() throws Exception {
        return entityService.findAll();
    }

    @Override
    public List<User> findAll(int maxResults, int firstResult) throws Exception {
        return entityService.findAll(maxResults, firstResult);
    }

    @Override
    public int getCount() throws Exception {
        return entityService.getCount();
    }

    @Override
    public User getInstance() throws Exception {
        return (User) entityService.getInstance();
    }

    public void store(User user) throws Exception {
        entityService.store(user);
    }
}
