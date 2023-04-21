package com.objectcode.time4u.server.ejb;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.ejb.RemoteBinding;
import com.objectcode.time4u.server.api.ILoginService;
import com.objectcode.time4u.server.api.local.LoginLocal;
import com.objectcode.time4u.server.api.local.LoginServiceLocal;
import com.objectcode.time4u.server.ejb.entities.PermissionType;
import com.objectcode.time4u.server.ejb.entities.PersonEntity;
import com.objectcode.time4u.server.ejb.entities.PersonPermission;
import com.objectcode.time4u.server.util.PasswordEncoder;

@LocalBinding(jndiBinding = "time4u-server/LoginServiceBean/local")
@RemoteBinding(jndiBinding = "time4u-server/LoginServiceBean/remote")
@Local(LoginServiceLocal.class)
@Remote(ILoginService.class)
@Stateless
public class LoginServiceBean implements ILoginService, LoginServiceLocal {

    @PersistenceContext
    private EntityManager m_manager;

    public int getApiVersion() {
        return CURRENT_VERSION;
    }

    public LoginLocal findLogin(String userId) {
        Query query = m_manager.createQuery("from " + PersonEntity.class.getName() + " p where p.userId = :userId");
        query.setParameter("userId", userId);
        try {
            PersonEntity person = (PersonEntity) query.getSingleResult();
            return new LoginLocal(person.getId(), person.getUserId(), person.getHashedPassword());
        } catch (NoResultException e) {
            if ("admin".equals(userId)) {
                PersonEntity person = new PersonEntity();
                person.setUserId("admin");
                person.setHashedPassword(PasswordEncoder.encrypt("admin"));
                person.setName("admin");
                m_manager.persist(person);
                PersonPermission permission = new PersonPermission();
                permission.setType(PermissionType.ADMIN);
                permission.setCanAdmin(true);
                permission.setPerson(person);
                m_manager.persist(permission);
                return new LoginLocal(person.getId(), person.getUserId(), person.getHashedPassword());
            }
            return null;
        }
    }

    public boolean registerLogin(String userId, String password) {
        Query query = m_manager.createQuery("from " + PersonEntity.class.getName() + " p where p.userId = :userId");
        query.setParameter("userId", userId);
        try {
            query.getSingleResult();
            return false;
        } catch (NoResultException e) {
            PersonEntity person = new PersonEntity();
            person.setUserId(userId);
            person.setHashedPassword(PasswordEncoder.encrypt(password));
            person.setName(userId);
            m_manager.persist(person);
            return true;
        }
    }
}
