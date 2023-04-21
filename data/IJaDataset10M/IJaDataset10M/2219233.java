package ge.forms.etx.controllers;

import ge.forms.etx.model.Address;
import ge.forms.etx.model.Constants;
import ge.forms.etx.model.Group;
import ge.forms.etx.model.User;
import ge.forms.etx.model.UserGroup;
import ge.forms.etx.model.UserOrganization;
import java.sql.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

/**
 * User organization service.
 * 
 * @author dimitri
 */
public class UserOrganizationService {

    private EntityManager em;

    private DatabaseService databaseService;

    /**
	 * Creates instance of this manager.
	 */
    public UserOrganizationService(DatabaseService databaseService) {
        this.em = databaseService.em;
        this.databaseService = databaseService;
    }

    public void setLastOrganization(User user, long userOrgId) {
        User dbUser = databaseService.getSecurityService().getUser(user.getUserName(), user.getPassword());
        dbUser.setLastOrganization(getUserOrganization(userOrgId));
    }

    /**
	 * Get user organizations list.
	 */
    @SuppressWarnings("unchecked")
    public List<UserOrganization> getUserOrganizations(User user) {
        User dbUser = databaseService.getSecurityService().getUser(user.getUserName(), user.getPassword());
        Query q = em.createQuery("SELECT uo.organization FROM UserGroup uo " + "WHERE uo.user.id = :userId AND uo.statusId = :statusId AND " + "uo.organization.statusId = :orgStatusId " + "ORDER BY uo.organization.name");
        q.setParameter("userId", dbUser.getId());
        q.setParameter("statusId", Constants.STATUS_ACTIVE);
        q.setParameter("orgStatusId", Constants.STATUS_ACTIVE);
        return q.getResultList();
    }

    /**
	 * Looks up organization by it's id.
	 */
    public UserOrganization getUserOrganization(long userOrgId) {
        Query q = em.createQuery("SELECT org FROM UserOrganization org WHERE org.id = :orgId");
        q.setParameter("orgId", userOrgId);
        try {
            return (UserOrganization) q.getSingleResult();
        } catch (NoResultException ex) {
            throw new IllegalArgumentException("MyOrganizationService.MyorgNotFound");
        }
    }

    /**
	 * Create new organization.
	 */
    public UserOrganization create(User user, UserOrganization userOrg) {
        User dbUser = databaseService.getSecurityService().getUser(user.getUserName(), user.getPassword());
        validate(userOrg);
        userOrg.setId(null);
        userOrg.setCreationDate(new Date(System.currentTimeMillis()));
        userOrg.setStatusId(Constants.STATUS_ACTIVE);
        userOrg.setCreator(dbUser);
        if (userOrg.getOfficialAddress() != null) {
            em.persist(userOrg.getOfficialAddress());
        }
        if (userOrg.getActualAddress() != null) {
            em.persist(userOrg.getActualAddress());
        }
        em.persist(userOrg);
        UserGroup group = new UserGroup();
        group.setOrganization(userOrg);
        group.setUser(dbUser);
        Group ownerGroup = databaseService.getSecurityService().getGroup(Constants.GROUP_OWNERS_ID);
        group.setGroup(ownerGroup);
        em.persist(group);
        return userOrg;
    }

    /**
	 * Update user organization.
	 */
    public UserOrganization update(User user, UserOrganization userOrg) {
        User dbUser = databaseService.getSecurityService().getUser(user.getUserName(), user.getPassword());
        boolean permission = databaseService.getSecurityService().hasPermission(dbUser, userOrg.getId(), Constants.ACT_USERORG_UPDATE);
        if (!permission) {
            throw new IllegalArgumentException("MyOrganizationService.MyorgUpdate.NotPermitted");
        }
        validate(userOrg);
        UserOrganization oldUserOrg = getUserOrganization(userOrg.getId());
        if (oldUserOrg.getStatusId() != Constants.STATUS_ACTIVE) {
            throw new IllegalArgumentException("MyOrganizationService.MyOrgUpdate.Deleted");
        }
        if (oldUserOrg.getOfficialAddress() == null && userOrg.getOfficialAddress() != null) {
            Address newAddress = userOrg.getOfficialAddress();
            em.persist(newAddress);
            oldUserOrg.setOfficialAddress(newAddress);
        } else if (oldUserOrg.getOfficialAddress() != null && userOrg.getOfficialAddress() == null) {
            em.remove(oldUserOrg.getOfficialAddress());
            oldUserOrg.setOfficialAddress(null);
        } else if (oldUserOrg.getOfficialAddress() != null && userOrg.getOfficialAddress() != null) {
            oldUserOrg.getOfficialAddress().setCity(userOrg.getOfficialAddress().getCity());
            oldUserOrg.getOfficialAddress().setAddressLine(userOrg.getOfficialAddress().getAddressLine());
            oldUserOrg.getOfficialAddress().setCountry(userOrg.getOfficialAddress().getCountry());
            oldUserOrg.getOfficialAddress().setPostalIndex(userOrg.getOfficialAddress().getPostalIndex());
        }
        if (oldUserOrg.getActualAddress() == null && userOrg.getActualAddress() != null) {
            Address newAddress = userOrg.getActualAddress();
            em.persist(newAddress);
            oldUserOrg.setActualAddress(newAddress);
        } else if (oldUserOrg.getActualAddress() != null && userOrg.getActualAddress() == null) {
            em.remove(oldUserOrg.getActualAddress());
            oldUserOrg.setActualAddress(null);
        } else if (oldUserOrg.getActualAddress() != null && userOrg.getActualAddress() != null) {
            oldUserOrg.getActualAddress().setCity(userOrg.getActualAddress().getCity());
            oldUserOrg.getActualAddress().setAddressLine(userOrg.getActualAddress().getAddressLine());
            oldUserOrg.getActualAddress().setCountry(userOrg.getActualAddress().getCountry());
            oldUserOrg.getActualAddress().setPostalIndex(userOrg.getActualAddress().getPostalIndex());
        }
        oldUserOrg.setBossName(userOrg.getBossName());
        oldUserOrg.setCategoryId(userOrg.getCategoryId());
        oldUserOrg.setComercialId(userOrg.getComercialId());
        oldUserOrg.setIndividualId(userOrg.getIndividualId());
        oldUserOrg.setInspectionName(userOrg.getInspectionName());
        oldUserOrg.setNaceId(userOrg.getNaceId());
        oldUserOrg.setName(userOrg.getName());
        oldUserOrg.setVatSertificate(userOrg.getVatSertificate());
        oldUserOrg.setVatSertificateDate(userOrg.getVatSertificateDate());
        oldUserOrg.setPhone(userOrg.getPhone());
        oldUserOrg.setAqciziPayer(userOrg.getAqciziPayer());
        return oldUserOrg;
    }

    /**
	 * Delete user organization.
	 */
    public void delete(User user, long userOrgId) {
        User dbUser = databaseService.getSecurityService().getUser(user.getUserName(), user.getPassword());
        boolean permission = databaseService.getSecurityService().hasPermission(dbUser, userOrgId, Constants.ACT_USERORG_DELETE);
        if (!permission) {
            throw new IllegalArgumentException("MyOrganizationService.MyDelete.NotPermitted");
        }
        UserOrganization oldMyorg = getUserOrganization(userOrgId);
        oldMyorg.setStatusId(Constants.STATUS_DELETED);
    }

    /**
	 * Validate user organization object.
	 */
    private void validate(UserOrganization userOrg) {
        int catId = userOrg.getCategoryId();
        if (catId != Constants.USERORG_LOCAL_ORGANIZATION && catId != Constants.USERORG_FOREIGN_ORGANIZATION && catId != Constants.USERORG_LOCAL_INDIVIDUAL && catId != Constants.USERORG_OTHER) {
            throw new IllegalArgumentException("MyOrganizationService.MyorgValidation.IllegalCategory");
        }
        if (Utils.isEmptyString(userOrg.getName())) {
            throw new IllegalArgumentException("MyOrganizationService.MyorgValidation.EmptyName");
        }
        if (Utils.isEmptyString(userOrg.getComercialId()) && Utils.isEmptyString(userOrg.getIndividualId())) {
            throw new IllegalArgumentException("MyOrganizationService.MyorgValidation.EmptyOrgId");
        }
        if (Utils.isEmptyString(userOrg.getPhone())) {
            throw new IllegalArgumentException("MyOrganizationService.MyorgValidation.EmptyPhone");
        }
        if (Utils.isEmptyString(userOrg.getInspectionName())) {
            throw new IllegalArgumentException("MyOrganizationService.MyorgValidation.EmptyInspection");
        }
        if (userOrg.getOfficialAddress() == null) {
            throw new IllegalArgumentException("MyOrganizationService.MyorgValidation.EmptyOfficialAddress");
        }
        String msg;
        msg = Utils.validateLength(userOrg.getName(), 0, 100, "MyOrganizationService.Field.Name");
        if (msg != null) {
            throw new IllegalArgumentException(msg);
        }
        msg = Utils.validateLength(userOrg.getComercialId(), 0, 25, "MyOrganizationService.Field.OrgId");
        if (msg != null) {
            throw new IllegalArgumentException(msg);
        }
        msg = Utils.validateLength(userOrg.getIndividualId(), 0, 25, "MyOrganizationService.Field.IndId");
        if (msg != null) {
            throw new IllegalArgumentException(msg);
        }
        msg = Utils.validateLength(userOrg.getNaceId(), 0, 25, "MyOrganizationService.Field.NACE");
        if (msg != null) {
            throw new IllegalArgumentException(msg);
        }
        msg = Utils.validateLength(userOrg.getBossName(), 0, 100, "MyOrganizationService.Field.BossName");
        if (msg != null) {
            throw new IllegalArgumentException(msg);
        }
        msg = Utils.validateLength(userOrg.getVatSertificate(), 0, 25, "MyOrganizationService.Field.VatCertificateNumber");
        if (msg != null) {
            throw new IllegalArgumentException(msg);
        }
        if (userOrg.getOfficialAddress() != null) {
            msg = Utils.validateAddress(userOrg.getOfficialAddress());
            if (msg != null) {
                throw new IllegalArgumentException(msg);
            }
        }
        if (userOrg.getActualAddress() != null) {
            msg = Utils.validateAddress(userOrg.getActualAddress());
            if (msg != null) {
                throw new IllegalArgumentException(msg);
            }
        }
    }
}
