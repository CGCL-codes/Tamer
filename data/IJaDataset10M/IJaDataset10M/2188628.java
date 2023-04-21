package net.ad.adsp.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;
import net.ad.adsp.entity.Permission;
import net.ad.adsp.entity.Role;
import net.ad.adsp.entity.User;
import net.ad.adsp.exception.ExceptionAction;
import net.ad.adsp.exception.ExceptionManager;
import net.ad.adsp.session.PermissionList;
import net.ad.adsp.utility.Item;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.FlushModeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.security.Delete;
import org.jboss.seam.annotations.security.Insert;
import org.jboss.seam.annotations.security.Update;
import org.jboss.seam.core.ResourceBundle;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.jboss.seam.security.Identity;
import org.jboss.seam.security.management.action.RoleAction;
import org.jboss.seam.security.management.action.UserAction;

@Name("roleActionBean")
@Scope(ScopeType.CONVERSATION)
public class RoleActionBean implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    @Logger
    private Log log;

    @In(value = "entityManager", create = true)
    private EntityManager em;

    @In(value = "permissionList", required = false)
    PermissionList permissionList;

    public Role role;

    public boolean inModifica;

    public Integer roleId;

    public Integer groupId;

    String rolename;

    public List<Role> availableRoles;

    @Out(value = "usersAssigned", required = false)
    public List<User> usersAssigned;

    @Out(value = "usersNotAssigned", required = false)
    public List<User> usersNotAssigned;

    @In(value = "permissionActionBean", required = false, create = true)
    PermissionActionBean permissionActionBean;

    @In(value = "menuActionBean", required = false, create = true)
    MenuActionBean menuActionBean;

    @In(value = "exceptionManager", create = true, required = false)
    ExceptionManager exceptionManager;

    @In(value = "exceptionAction", create = true, required = false)
    ExceptionAction exceptionAction;

    List<Permission> permissionRole;

    String oldRoleName;

    private boolean existUser;

    String existUserMsg;

    String existFigliMsg;

    String operation;

    public RoleActionBean() {
        this.role = new Role();
        this.inModifica = false;
        this.availableRoles = new ArrayList<Role>();
        this.usersAssigned = new ArrayList<User>();
        this.usersNotAssigned = new ArrayList<User>();
        this.permissionRole = null;
    }

    public void eseguiVerifiche(String action) throws Exception {
        try {
            if (action.equals("delete")) {
                verificaExistFigli();
                verificaExistUser();
                operation = "delete";
            } else if (action.equals("create") || action.equals("update")) {
                existUserMsg = null;
                existFigliMsg = null;
                operation = action;
            } else {
                throw new Exception("Wrong action");
            }
        } catch (Exception e) {
            log.error("ERRORE in RoleActionBean.eseguiVerifiche(), lanciata eccezione '#0'", e.toString());
            exceptionManager.manageException(e.toString());
            exceptionAction.execute(null);
        }
    }

    public void memorizeRole() throws Exception {
        try {
            groupId = null;
            if (roleId != 0) {
                this.role = em.find(Role.class, roleId);
                if (role.getRole() != null) {
                    groupId = role.getRole().getRoleId();
                }
                rolename = role.getRoleName();
                oldRoleName = role.getRoleName();
                inModifica = true;
            } else {
                this.role = new Role();
                rolename = null;
                oldRoleName = null;
                inModifica = false;
            }
            populateAvlRoles();
        } catch (Exception e) {
            log.error("ERRORE in RoleActionBean.memorizeRole(), lanciata eccezione '#0'", e.toString());
            exceptionManager.manageException(e.toString());
            exceptionAction.execute(null);
        }
    }

    public void populateAvlRoles() throws Exception {
        try {
            availableRoles = new ArrayList<Role>();
            StringBuilder query = new StringBuilder();
            query.append("SELECT role ");
            query.append("FROM Role role ");
            query.append("WHERE role.roleName IS NOT '" + role.getRoleName() + "'");
            List<Role> tempRoles = new ArrayList<Role>(em.createQuery(query.toString()).getResultList());
            List<Role> discendenti = new ArrayList<Role>();
            popolaDiscendenti(role, discendenti);
            for (Role r : tempRoles) {
                if (!discendenti.contains(r) && verNoFigliInRoleGrp(r, role)) {
                    availableRoles.add(r);
                }
            }
            Collections.sort(availableRoles);
        } catch (Exception e) {
            log.error("ERRORE in RoleActionBean.populateAvlRoles(), lanciata eccezione '#0'", e.toString());
            throw e;
        }
    }

    public void popolaDiscendenti(Role group, List<Role> discendenti) throws Exception {
        try {
            if (inModifica) {
                em.refresh(group);
            }
            if (!group.getRoles().isEmpty()) {
                for (Role r : group.getRoles()) {
                    discendenti.add(r);
                    popolaDiscendenti(r, discendenti);
                }
            }
        } catch (Exception e) {
            log.error("Errore RoleActionBean.popolaDiscendenti(...): lanciata eccezione '" + e.toString() + "'");
            throw e;
        }
    }

    public void populateListUsers() throws Exception {
        try {
            usersAssigned = new ArrayList<User>(em.createQuery("SELECT user FROM User user JOIN user.roles role WHERE role.roleId IS " + role.getRoleId()).getResultList());
            usersNotAssigned = new ArrayList<User>();
            List<User> tempUsers = new ArrayList<User>(em.createQuery("SELECT user FROM User user WHERE user.username IS NOT 'superuser'").getResultList());
            boolean trovato;
            for (User allUser : tempUsers) {
                trovato = false;
                for (User assUser : usersAssigned) {
                    if (allUser.getUserId() == assUser.getUserId()) {
                        trovato = true;
                        break;
                    }
                }
                if (!trovato) {
                    usersNotAssigned.add(allUser);
                }
            }
        } catch (Exception e) {
            log.error("ERRORE in RoleActionBean.populateListUsers(), lanciata eccezione '#0'", e.toString());
            exceptionManager.manageException(e.toString());
            exceptionAction.execute(null);
        }
    }

    public void recursiveExistUser(Role role) throws Exception {
        try {
            boolean continuaRicerca = true;
            List<User> users = em.createQuery("SELECT u FROM User u JOIN u.roles role WHERE role.roleId = " + role.getRoleId()).getResultList();
            if (!users.isEmpty()) {
                existUser = true;
                continuaRicerca = false;
            }
            if (continuaRicerca) {
                for (Role r : role.getRoles()) {
                    recursiveExistUser(r);
                }
            }
        } catch (Exception e) {
            log.error("ERRORE in RoleActionBean.recursiveExistUser(...), lanciata eccezione '#0'", e.toString());
            throw e;
        }
    }

    @Begin(join = true, flushMode = FlushModeType.MANUAL)
    public void recursiveSaveMenu(Item item) throws Exception {
        try {
            if (item.getIsEnabled()) {
                Permission p = new Permission(role.getRoleName(), item.getTarget().getNameMenu(), "read", "role", true);
                em.persist(p);
            }
            for (Item itm : item.getNodes()) {
                recursiveSaveMenu(itm);
            }
        } catch (Exception e) {
            log.error("ERRORE in RoleActionBean.recursiveSaveMenu(...), lanciata eccezione '#0'", e.toString());
            throw e;
        }
    }

    @Begin(join = true, flushMode = FlushModeType.MANUAL)
    public void recursiveUpdateMenu(Item item, String oldRecipient, String newRecipient) throws Exception {
        try {
            if (item.getIsEnabled()) {
                if (!permissionActionBean.hasPermission("read", oldRecipient, "role", item.getTarget().getNameMenu(), true)) {
                    Permission p = new Permission(newRecipient, item.getTarget().getNameMenu(), "read", "role", true);
                    em.persist(p);
                }
            } else {
                if (permissionActionBean.hasPermission("read", oldRecipient, "role", item.getTarget().getNameMenu(), true)) {
                    Permission perm = permissionActionBean.returnPermission("read", oldRecipient, "role", item.getTarget().getNameMenu(), true);
                    em.lock(perm, LockModeType.READ);
                    em.remove(perm);
                }
            }
            for (Item itm : item.getNodes()) {
                recursiveUpdateMenu(itm, oldRecipient, newRecipient);
            }
        } catch (OptimisticLockException opLockExc) {
            log.error("ERRORE in RoleActionBean.recursiveUpdateMenu(...), lanciata eccezione '#0'", opLockExc.toString());
            exceptionManager.manageException(opLockExc.toString());
            exceptionAction.execute(null);
        } catch (Exception e) {
            log.error("ERRORE in RoleActionBean.recursiveUpdateMenu(...), lanciata eccezione '#0'", e.toString());
            throw e;
        }
    }

    @Begin(join = true, flushMode = FlushModeType.MANUAL)
    public void removeAssWithUser(User user, Role role) throws Exception {
        try {
            boolean trovato = false;
            Role roleToRemove = new Role();
            for (Role r : user.getRoles()) {
                if (r.getRoleId() == role.getRoleId()) {
                    roleToRemove = r;
                    trovato = true;
                    break;
                }
            }
            if (trovato) {
                em.lock(user, LockModeType.READ);
                user.getRoles().remove(roleToRemove);
                em.persist(user);
            }
        } catch (OptimisticLockException opLockExc) {
            log.error("ERRORE in RoleActionBean.removeAssWithUser(...), lanciata eccezione '#0'", opLockExc.toString());
            exceptionManager.manageException(opLockExc.toString());
            exceptionAction.execute(null);
        } catch (Exception e) {
            log.error("ERRORE in RoleActionBean.removeAssWithUser(...), lanciata eccezione '#0'", e.toString());
            throw e;
        }
    }

    @Delete(Role.class)
    @Begin(join = true, flushMode = FlushModeType.MANUAL)
    public void removeRoleAndPermission(Role role) throws Exception {
        try {
            for (Role r : role.getRoles()) {
                removeRoleAndPermission(r);
            }
            List<User> users = em.createQuery("SELECT u FROM User u JOIN u.roles role WHERE role.roleId = " + role.getRoleId()).getResultList();
            for (User u : users) {
                removeAssWithUser(u, role);
            }
            em.lock(role, LockModeType.READ);
            em.remove(role);
            for (Permission p : permissionActionBean.listPermissions(role.getRoleName(), "role")) {
                em.lock(p, LockModeType.READ);
                em.refresh(p);
                em.remove(p);
            }
        } catch (OptimisticLockException opLockExc) {
            log.error("ERRORE in RoleActionBean.removeRoleAndPermission(...), lanciata eccezione '#0'", opLockExc.toString());
            exceptionManager.manageException(opLockExc.toString());
            exceptionAction.execute(null);
        } catch (Exception e) {
            log.error("ERRORE in RoleActionBean.removeRoleAndPermission(...), lanciata eccezione '#0'", e.toString());
            throw e;
        }
    }

    @Update(User.class)
    @Begin(join = true, flushMode = FlushModeType.MANUAL)
    public void updateUsers() throws Exception {
        try {
            for (User user : usersAssigned) {
                em.lock(user, LockModeType.READ);
                user.getRoles().add(role);
                em.persist(user);
            }
            for (User user : usersNotAssigned) {
                if (user.getRoles().contains(role)) {
                    user.getRoles().remove(role);
                    em.persist(user);
                }
            }
        } catch (OptimisticLockException opLockExc) {
            log.error("ERRORE in RoleActionBean.updateUsers(), lanciata eccezione '#0'", opLockExc.toString());
            exceptionManager.manageException(opLockExc.toString());
            exceptionAction.execute(null);
        } catch (Exception e) {
            em.clear();
            log.error("ERRORE in RoleActionBean.updateUsers(), lanciata eccezione '#0'", e.toString());
            throw e;
        }
    }

    public void verificaExistUser() throws Exception {
        try {
            existUser = false;
            recursiveExistUser(role);
            if (!existUser) {
                existUserMsg = null;
            } else {
                existUserMsg = ResourceBundle.instance().getString("existUser");
            }
        } catch (Exception e) {
            log.error("ERRORE in RoleActionBean.verificaExistUser(), lanciata eccezione '#0'", e.toString());
            throw e;
        }
    }

    public void verificaExistFigli() throws Exception {
        try {
            if (role.getRoles().isEmpty()) {
                existFigliMsg = null;
            } else {
                existFigliMsg = ResourceBundle.instance().getString("existFigli");
            }
        } catch (Exception e) {
            log.error("ERRORE in RoleActionBean.verificaExistFigli(), lanciata eccezione '#0'", e.toString());
            throw e;
        }
    }

    public boolean verifyUnicoPermesso(Permission permission) throws Exception {
        try {
            permissionList.getPermission().setAction(permission.getAction());
            permissionList.getPermission().setTarget(permission.getTarget());
            if (permissionList.getResultList().size() <= 1) {
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("ERRORE in RoleActionBean.verifyUnicoPermesso(...), lanciata eccezione '#0'", e.toString());
            exceptionManager.manageException(e.toString());
            exceptionAction.execute(null);
            throw e;
        }
    }

    public boolean verNoFigliInRoleGrp(Role role, Role toModify) throws Exception {
        try {
            if (toModify.getRoleId() == role.getRoleId()) {
                return false;
            }
            List<Role> discendenti = new ArrayList<Role>();
            popolaDiscendenti(toModify, discendenti);
            if (discendenti.contains(role)) {
                return false;
            }
            return true;
        } catch (Exception e) {
            log.error("ERRORE in RoleActionBean.verNoFigliInRoleGrp(...), lanciata eccezione '#0'", e.toString());
            throw e;
        }
    }

    @Insert(Role.class)
    @Begin(join = true, flushMode = FlushModeType.MANUAL)
    public String createRole() {
        try {
            log.debug("L'utente '#0' ha iniziato la procedura di creazione di un nuovo Ruolo...", Identity.instance().getCredentials().getUsername());
            role.setRoleName(rolename);
            role.setConditional(false);
            if (groupId != null) {
                Role roleGroup = em.find(Role.class, groupId);
                em.lock(roleGroup, LockModeType.READ);
                role.setRole(roleGroup);
            }
            em.persist(role);
            roleId = role.getRoleId();
            if (Identity.instance().hasPermission(Permission.class, "insert")) {
                for (Permission perm : permissionActionBean.getPermList()) {
                    perm.setRecipient(role.getRoleName());
                    em.persist(perm);
                }
                for (Item item : menuActionBean.getItemList()) {
                    recursiveSaveMenu(item);
                }
            } else {
                FacesMessages.instance().addFromResourceBundle(Severity.WARN, "warn_roleNoPermI", Identity.instance().getCredentials().getUsername(), role.getRoleName());
                log.warn("WARNING: Utente '#0', creazione del nuovo Ruolo '#1' parziale! Non si ha l'autorizzazione ad inserire nuovi permessi: i permessi e il menu' non verrano creati!", Identity.instance().getCredentials().getUsername(), role.getRoleName());
            }
            if (Identity.instance().hasPermission(User.class, "update")) {
                updateUsers();
            } else {
                FacesMessages.instance().addFromResourceBundle(Severity.WARN, "warn_createRole", Identity.instance().getCredentials().getUsername(), role.getRoleName());
                log.warn("WARNING: Utente '#0', creazione del nuovo Ruolo '#1' parziale! Non si hanno i permessi per poter aggiornare gli Utenti: il nuovo ruolo non è stato associato!", Identity.instance().getCredentials().getUsername(), role.getRoleName());
            }
            em.flush();
            FacesMessages.instance().addFromResourceBundle(Severity.INFO, "success_createRole", Identity.instance().getCredentials().getUsername(), role.getRoleName());
            log.debug("Utente '#0': creazione del nuovo Ruolo '#1' completata con successo!", Identity.instance().getCredentials().getUsername(), role.getRoleName());
            return "true";
        } catch (OptimisticLockException opLockExc) {
            FacesMessages.instance().addFromResourceBundle(Severity.ERROR, "error_opLock");
            log.error("ERRORE: Utente '#0', creazione del nuovo Ruolo '#1' fallita! Un altro utente ha modificato i dati!", Identity.instance().getCredentials().getUsername(), role.getRoleName());
            return "opLockExc";
        } catch (Exception e) {
            em.clear();
            FacesMessages.instance().addFromResourceBundle(Severity.ERROR, "error_createRole", Identity.instance().getCredentials().getUsername(), role.getRoleName());
            log.error("ERRORE: Utente '#0', creazione del nuovo Ruolo '#1' fallita! Lanciata eccezione: '#2'", Identity.instance().getCredentials().getUsername(), role.getRoleName(), e.toString());
            return "false";
        }
    }

    @Delete(Role.class)
    @Begin(join = true, flushMode = FlushModeType.MANUAL)
    public String deleteRole() {
        try {
            log.debug("L'utente '#0' ha iniziato la procedura di cancellazione del Ruolo...", Identity.instance().getCredentials().getUsername());
            em.lock(role, LockModeType.READ);
            for (User u : usersAssigned) {
                removeAssWithUser(u, role);
            }
            removeRoleAndPermission(role);
            em.flush();
            FacesMessages.instance().addFromResourceBundle(Severity.INFO, "success_deleteRole", Identity.instance().getCredentials().getUsername(), role.getRoleName());
            log.debug("Utente '#0': cancellazione Ruolo '#1' completata con successo!", Identity.instance().getCredentials().getUsername(), role.getRoleName());
            return "true";
        } catch (OptimisticLockException opLockExc) {
            FacesMessages.instance().addFromResourceBundle(Severity.ERROR, "error_opLock");
            log.error("ERRORE: Utente '#0', cancellazione Ruolo '#1' fallita! Un altro utente ha modificato i dati!", Identity.instance().getCredentials().getUsername(), role.getRoleName());
            return "opLockExc";
        } catch (Exception e) {
            em.clear();
            FacesMessages.instance().addFromResourceBundle(Severity.ERROR, "error_deleteRole", Identity.instance().getCredentials().getUsername(), role.getRoleName());
            log.error("ERRORE: Utente '#0', cancellazione Ruolo '#1' fallita! Lanciata eccezione: '#2'", Identity.instance().getCredentials().getUsername(), role.getRoleName(), e.toString());
            return "false";
        }
    }

    @Update(Role.class)
    @Begin(join = true, flushMode = FlushModeType.MANUAL)
    public String updateRole() throws Exception {
        try {
            log.debug("L'utente '#0' ha iniziato la procedura di aggiornamento di un Ruolo...", Identity.instance().getCredentials().getUsername());
            em.lock(role, LockModeType.READ);
            role.setRoleName(rolename);
            if (groupId != null) {
                Role roleGroup = em.find(Role.class, groupId);
                em.lock(roleGroup, LockModeType.READ);
                role.setRole(roleGroup);
            } else {
                role.setRole(null);
            }
            em.persist(role);
            if (Identity.instance().hasPermission(User.class, "update")) {
                updateUsers();
            } else {
                FacesMessages.instance().addFromResourceBundle(Severity.INFO, "warn_createRole", Identity.instance().getCredentials().getUsername(), role.getRoleName());
                log.warn("WARNING: Utente '#0', modifica Ruolo '#1' parziale! Non si hanno i permessi per poter aggiornare gli Utenti: il ruolo non è stato associato!", Identity.instance().getCredentials().getUsername(), role.getRoleName());
            }
            if (Identity.instance().hasPermission(Permission.class, "insert") && Identity.instance().hasPermission(Permission.class, "delete")) {
                for (Permission perm : permissionActionBean.getPermList()) {
                    perm.setRecipient(role.getRoleName());
                    em.merge(perm);
                }
                for (Permission perm : permissionActionBean.getPermissionToRemove()) {
                    if (perm.getPermissionId() != null) {
                        em.lock(perm, LockModeType.READ);
                        em.remove(perm);
                    }
                }
                for (Item item : menuActionBean.getItemList()) {
                    recursiveUpdateMenu(item, oldRoleName, role.getRoleName());
                }
            } else {
                FacesMessages.instance().addFromResourceBundle(Severity.WARN, "warn_roleNoPermID", Identity.instance().getCredentials().getUsername(), role.getRoleName());
                log.warn("WARNING: Utente '#0', aggiornamento Ruolo '#1' parziale! Non si ha l'autorizzazione ad operare sui permessi: i permessi e il menu' non verrano aggiornati!", Identity.instance().getCredentials().getUsername(), role.getRoleName());
            }
            if (!oldRoleName.equals(role.getRoleName())) {
                for (Permission perm : permissionActionBean.listPermissions(oldRoleName, "role", false)) {
                    if (em.contains(perm)) {
                        perm.setRecipient(role.getRoleName());
                        em.merge(perm);
                    }
                }
            }
            em.flush();
            permissionRole = null;
            FacesMessages.instance().addFromResourceBundle(Severity.INFO, "success_updateRole", Identity.instance().getCredentials().getUsername(), role.getRoleName());
            log.debug("Utente '#0': aggiornamento Ruolo '#1' completato con successo!", Identity.instance().getCredentials().getUsername(), role.getRoleName());
            return "true";
        } catch (OptimisticLockException opLockExc) {
            FacesMessages.instance().addFromResourceBundle(Severity.ERROR, "error_opLock");
            log.error("ERRORE: Utente '#0', aggiornamento Ruolo '#1' fallito! Un altro utente ha modificato i dati!", Identity.instance().getCredentials().getUsername(), role.getRoleName());
            return "opLockExc";
        } catch (Exception e) {
            em.clear();
            FacesMessages.instance().addFromResourceBundle(Severity.ERROR, "error_updateRole", Identity.instance().getCredentials().getUsername(), role.getRoleName());
            log.error("ERRORE: Utente '#0', aggiornamento Ruolo '#1' fallito! Lanciata eccezione: '#2'", Identity.instance().getCredentials().getUsername(), role.getRoleName(), e.toString());
            return "false";
        }
    }

    /******************************************************************************/
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isInModifica() {
        return inModifica;
    }

    public void setInModifica(boolean inModifica) {
        this.inModifica = inModifica;
    }

    public List<User> getUsersAssigned() {
        return usersAssigned;
    }

    public void setUsersAssigned(List<User> usersAssigned) {
        this.usersAssigned = usersAssigned;
    }

    public List<User> getUsersNotAssigned() {
        return usersNotAssigned;
    }

    public void setUsersNotAssigned(List<User> usersNotAssigned) {
        this.usersNotAssigned = usersNotAssigned;
    }

    public List<Role> getAvailableRoles() {
        return availableRoles;
    }

    public void setAvailableRoles(List<Role> availableRoles) {
        this.availableRoles = availableRoles;
    }

    public List<Permission> getPermissionRole() throws Exception {
        try {
            permissionRole = new ArrayList<Permission>(permissionActionBean.listPermissions(role.getRoleName(), "role", false));
            permissionActionBean.orderByField(permissionRole, "target");
        } catch (Exception e) {
            log.error("ERRORE in RoleActionBean.getPermissionRole(), lanciata eccezione '#0'", e.toString());
            exceptionManager.manageException(e.toString());
            exceptionAction.execute(null);
        }
        return permissionRole;
    }

    public void setPermissionRole(List<Permission> permissionRole) {
        this.permissionRole = permissionRole;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getExistUserMsg() {
        return existUserMsg;
    }

    public void setExistUserMsg(String existUserMsg) {
        this.existUserMsg = existUserMsg;
    }

    public String getExistFigliMsg() {
        return existFigliMsg;
    }

    public void setExistFigliMsg(String existFigliMsg) {
        this.existFigliMsg = existFigliMsg;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }
}
