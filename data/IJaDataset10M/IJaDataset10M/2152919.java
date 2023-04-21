package org.fao.fenix.persistence.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.fao.fenix.domain.security.FenixAclObjectIdentity;
import org.fao.fenix.domain.security.FenixAclPermission;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.acl.basic.AclObjectIdentity;
import org.springframework.security.acl.basic.BasicAclEntry;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * used this site to implement a first version of the ACL versions
 * http://toubsen.de/appfuse/acls/start
 *
 * This Dao is not used yet in Fenix. 
 *
 *
 */
@Repository
@Transactional
public class PermissionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public void save(FenixAclPermission fenixAclPermission) {
        entityManager.persist(fenixAclPermission);
    }

    public void delete(FenixAclPermission fenixAclPermission) {
        entityManager.remove(fenixAclPermission);
    }

    public FenixAclPermission findById(long id) {
        return entityManager.find(FenixAclPermission.class, id);
    }

    public Set<FenixAclPermission> getFenixAclPermissions(String objectIdentity, Authentication auth) {
        Set<FenixAclPermission> toReturn = new HashSet<FenixAclPermission>();
        String username = "";
        if (auth.getPrincipal() instanceof String) {
            username = (String) auth.getPrincipal();
        } else if (auth.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails) auth.getPrincipal()).getUsername();
        }
        List<FenixAclPermission> userpermissions = getFenixAclPermissions(objectIdentity, username);
        toReturn.addAll(userpermissions);
        List<FenixAclPermission> authorityPermissions = getFenixAclPermissions(objectIdentity, auth.getAuthorities());
        toReturn.addAll(authorityPermissions);
        return toReturn;
    }

    @SuppressWarnings("unchecked")
    public List<FenixAclPermission> getFenixAclPermissions(String objectIdentity, String username) {
        Query query = entityManager.createQuery("from FenixAclPermission as p WHERE p.fenixAclObjectIdentity.objectIdentity = :identity " + " AND p.objRecipient = :username");
        query.setParameter("identity", objectIdentity);
        query.setParameter("username", username);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<FenixAclPermission> getFenixAclPermissions(String objectIdentity, GrantedAuthority[] authorities) {
        if (authorities.length < 1) {
            return new ArrayList<FenixAclPermission>();
        }
        StringBuffer sb = new StringBuffer(50);
        for (int i = 0; i < authorities.length; i++) {
            sb.append(":rolename");
            sb.append(i);
            sb.append(", ");
        }
        sb.setLength(sb.length() - 2);
        String queryString = "from FenixAclPermission as p WHERE p.basicAclObjectIdentity.objectIdentity = :identity " + " AND p.objRecipient in (" + sb.toString() + ")";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("identity", objectIdentity);
        for (int i = 0; i < authorities.length; i++) {
            query.setParameter("rolename" + i, authorities[i].getAuthority());
        }
        List<FenixAclPermission> toReturn = query.getResultList();
        return toReturn;
    }

    @SuppressWarnings("unchecked")
    public List<FenixAclPermission> getFenixAclPermissions(String objectIdentity) {
        String queryString = "from FenixAclPermission as p where p.basicAclObjectIdentity.objectIdentity = :identity";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("identity", objectIdentity);
        List<FenixAclPermission> results = query.getResultList();
        return results;
    }

    @SuppressWarnings("unchecked")
    public List<FenixAclPermission> getFenixAclPermissionsByUsername(String username) {
        String queryString = "select permission from FenixAclPermission as permission where permission.objRecipient = :username";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("username", username);
        List<FenixAclPermission> results = query.getResultList();
        return results;
    }

    public BasicAclEntry[] getAcls(AclObjectIdentity aclObjectIdentity) {
        if (aclObjectIdentity instanceof FenixAclObjectIdentity) {
            FenixAclObjectIdentity oid = (FenixAclObjectIdentity) aclObjectIdentity;
            List<FenixAclPermission> entries = getFenixAclPermissions(oid.toString());
            return (BasicAclEntry[]) entries.toArray(new BasicAclEntry[] {});
        }
        return null;
    }
}
