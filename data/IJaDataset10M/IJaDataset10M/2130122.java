package au.org.tpac.portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ApplicationObjectSupport;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gwtwidgets.server.spring.ServletUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import au.org.tpac.portal.domain.Category;
import au.org.tpac.portal.domain.Dataset;
import au.org.tpac.portal.domain.DlpUser;
import au.org.tpac.portal.gwt.client.data.UserCreationResponse;
import au.org.tpac.portal.gwt.client.data.UserInformation;
import au.org.tpac.portal.gwt.client.service.AnonymousConsentException;
import au.org.tpac.portal.gwt.client.service.UserService;
import au.org.tpac.portal.manager.CategoryManager;
import au.org.tpac.portal.manager.UserManager;
import au.org.tpac.portal.security.DlpPermission;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.NotFoundException;

/**
 * The Class UserServiceImpl.
 * See @{link au.org.tpac.portal.gwt.client.service.UserService} for details of methods
 */
@Transactional
public class UserServiceImpl extends WebApplicationObjectSupport implements UserService {

    /** The user manager. */
    private UserManager userManager;

    private CategoryManager catManager;

    private MutableAclService mutableAclService;

    /**
     * Sets the user manager. 
     * @param fManager the new user manager
     */
    public final void setUserManager(final UserManager fManager) {
        this.userManager = fManager;
    }

    /**
     * Set the category manager.
     * @param fManager the new category manager
     */
    public final void setCategoryManager(final CategoryManager fManager) {
        this.catManager = fManager;
    }

    /** Logger for this class and subclasses. */
    protected final Log logger = LogFactory.getLog(getClass());

    /** The service helper. */
    private ServiceHelper serviceHelper;

    /** The Namespace URI. */
    private static String NS_XSD = "http://portal.tpac.org.au/schemas";

    /**
     * @see  au.org.tpac.portal.gwt.client.service.UserService#fetchUserDatasets()
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public Map<DlpUser, Map<String, List<Dataset>>> fetchUserDatasets() {
        Map<DlpUser, Map<String, List<Dataset>>> info = new HashMap<DlpUser, Map<String, List<Dataset>>>();
        List<DlpUser> list = userManager.findUsers();
        for (DlpUser user : list) {
            Map<String, List<Dataset>> data = new HashMap<String, List<Dataset>>();
            data.put("READ", userManager.findReadOnlyDatasets(user));
            data.put("ADMIN", userManager.findAdminDatasets(user));
            info.put(user, data);
        }
        return info;
    }

    /**
     * @see  au.org.tpac.portal.gwt.client.service.UserService#fetchUserDetail(String)
     */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public Map<String, Map<String, Set<Integer>>> fetchUserDetail(String id) {
        HashMap<String, Set<Integer>> d = new HashMap<String, Set<Integer>>();
        d.put("ADMIN", userManager.findAdminDatasetIds(id));
        d.put("READ", userManager.findReadOnlyDatasetIds(id));
        HashMap<String, Set<Integer>> c = new HashMap<String, Set<Integer>>();
        c.put("ADMIN", userManager.findAdminCategoryIds(id));
        c.put("READ", userManager.findReadOnlyCategoryIds(id));
        HashMap<String, Map<String, Set<Integer>>> h = new HashMap<String, Map<String, Set<Integer>>>();
        h.put("DATASET", d);
        h.put("CATEGORY", c);
        return h;
    }

    private static final String DEFAULT_ROLE = "ROLE_EVERYONE";

    /**
	 * @see  au.org.tpac.portal.gwt.client.service.UserService#listDatasets()
	 */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public List<Dataset> listDatasets() {
        return userManager.listDatasets();
    }

    /**
	 * @see  au.org.tpac.portal.gwt.client.service.UserService#listDatasetAccess()
	 */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public LinkedHashMap<Dataset, Boolean> listDatasetAccess() {
        LinkedHashMap<Dataset, Boolean> result = new LinkedHashMap<Dataset, Boolean>();
        List<Dataset> ds = userManager.listDatasets();
        for (Dataset d : ds) {
            result.put(d, hasPermission(Dataset.class, d.getId(), new GrantedAuthoritySid(DEFAULT_ROLE), BasePermission.READ));
        }
        return result;
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public LinkedHashMap<Dataset, Boolean> listDatasetHarvestAccess() {
        LinkedHashMap<Dataset, Boolean> result = new LinkedHashMap<Dataset, Boolean>();
        List<Dataset> ds = userManager.listDatasets();
        for (Dataset d : ds) {
            result.put(d, hasPermission(Dataset.class, d.getId(), new GrantedAuthoritySid(DEFAULT_ROLE), DlpPermission.HARVEST));
        }
        return result;
    }

    /**
	 * @see  au.org.tpac.portal.gwt.client.service.UserService#listCategoryAccess()
	 */
    @Override
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public LinkedHashMap<Category, Boolean> listCategoryAccess() {
        LinkedHashMap<Category, Boolean> result = new LinkedHashMap<Category, Boolean>();
        List<Category> ds = catManager.findCategories();
        for (Category d : ds) {
            result.put(d, hasPermission(Category.class, d.getId(), new GrantedAuthoritySid(DEFAULT_ROLE), BasePermission.READ));
        }
        return result;
    }

    /**
     * Set the service responsible for managing the ACLs
     * @param mutableAclService
     */
    public void setMutableAclService(MutableAclService mutableAclService) {
        this.mutableAclService = mutableAclService;
    }

    /**
     * @see  au.org.tpac.portal.gwt.client.service.UserService#setPublicAccessForDataset(int, boolean) 
     */
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public void setPublicAccessForDataset(int datasetId, boolean on) {
        if (on) this.addPermission(Dataset.class, datasetId, new GrantedAuthoritySid(DEFAULT_ROLE), BasePermission.READ); else this.deletePermission(Dataset.class, datasetId, new GrantedAuthoritySid(DEFAULT_ROLE), BasePermission.READ);
    }

    /**
     * @see  au.org.tpac.portal.gwt.client.service.UserService#setPublicAccessForDataset(int, boolean) 
     */
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public void setPublicHarvestForDataset(int datasetId, boolean on) {
        if (on) this.addPermission(Dataset.class, datasetId, new GrantedAuthoritySid(DEFAULT_ROLE), DlpPermission.HARVEST); else this.deletePermission(Dataset.class, datasetId, new GrantedAuthoritySid(DEFAULT_ROLE), DlpPermission.HARVEST);
    }

    /**
     * @see {@link au.org.tpac.portal.gwt.client.service.UserService#setPublicAccessForCategory(int, boolean)}
     */
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public void setPublicAccessForCategory(int datasetId, boolean on) {
        if (on) this.addPermission(Category.class, datasetId, new GrantedAuthoritySid(DEFAULT_ROLE), BasePermission.READ); else this.deletePermission(Category.class, datasetId, new GrantedAuthoritySid(DEFAULT_ROLE), BasePermission.READ);
    }

    /**
     * @see  au.org.tpac.portal.gwt.client.service.UserService#setUserEnabled(DlpUser, boolean)
     */
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public void setUserEnabled(DlpUser user, boolean on) {
        this.userManager.setUserEnabled(user, on);
    }

    /**
     * @see  au.org.tpac.portal.gwt.client.service.UserService#setUserAdmin(DlpUser, boolean)
     */
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public void setUserAdmin(DlpUser user, boolean on) {
        this.userManager.setUserAdmin(user, on);
    }

    /**
     * @see  au.org.tpac.portal.gwt.client.service.UserService#setReadAccessForDataset(DlpUser, int, boolean)
     */
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public void setReadAccessForDataset(DlpUser user, int datasetId, boolean on) {
        if (on) this.addPermission(Dataset.class, datasetId, new PrincipalSid(user.getSid()), BasePermission.READ); else this.deletePermission(Dataset.class, datasetId, new PrincipalSid(user.getSid()), BasePermission.READ);
    }

    /**
     * @see  au.org.tpac.portal.gwt.client.service.UserService#setAdminAccessForDataset(DlpUser, int, boolean)
     */
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public void setAdminAccessForDataset(DlpUser user, int datasetId, boolean on) {
        if (on) this.addPermission(Dataset.class, datasetId, new PrincipalSid(user.getSid()), BasePermission.WRITE); else this.deletePermission(Dataset.class, datasetId, new PrincipalSid(user.getSid()), BasePermission.WRITE);
    }

    /**
     * @see  au.org.tpac.portal.gwt.client.service.UserService#setReadAccessForCategory(DlpUser, int, boolean)
     */
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public void setReadAccessForCategory(DlpUser user, int categoryId, boolean on) {
        if (on) this.addPermission(Category.class, categoryId, new PrincipalSid(user.getSid()), BasePermission.READ); else this.deletePermission(Category.class, categoryId, new PrincipalSid(user.getSid()), BasePermission.READ);
    }

    /**
     * @see  au.org.tpac.portal.gwt.client.service.UserService#setAdminAccessForCategory(DlpUser, int, boolean)   
     */
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public void setAdminAccessForCategory(DlpUser user, int categoryId, boolean on) {
        if (on) this.addPermission(Category.class, categoryId, new PrincipalSid(user.getSid()), BasePermission.WRITE); else this.deletePermission(Category.class, categoryId, new PrincipalSid(user.getSid()), BasePermission.WRITE);
    }

    /**
     * Adds a permission to the ACL for an object, and creates an ACL if one doesn't already exist.
     * @param classReference Type of the target object
     * @param objectId Id of the target object
     * @param recipient User who's permission we are adding
     * @param permission Permission that is being added (eg. Read, write, admin, etc.)
     */
    private void addPermission(Class<?> classReference, int objectId, Sid recipient, Permission permission) {
        MutableAcl acl;
        ObjectIdentity oid = new ObjectIdentityImpl(classReference, objectId);
        try {
            acl = (MutableAcl) mutableAclService.readAclById(oid);
        } catch (NotFoundException nfe) {
            acl = mutableAclService.createAcl(oid);
        }
        acl.insertAce(acl.getEntries().size(), permission, recipient, true);
        mutableAclService.updateAcl(acl);
        logger.debug("Added permission " + permission + " for Sid " + recipient + " contact " + classReference.getName());
    }

    /**
     * Checks to see if user has a permission for a particular object
     * Uses code from SpringSecurity ContactManager example.
     * @param classReference Type of the target object
     * @param id Id of the target object
     * @param recipient User who's permission we are adding
     * @param permission Permission that is being sought (eg. Read, write, admin, etc.)
     * @return True if user has the specified permission for the specified object, false otherwise.
     */
    private boolean hasPermission(Class<?> classReference, int id, Sid recipient, Permission permission) {
        ObjectIdentity oid = new ObjectIdentityImpl(classReference, id);
        MutableAcl acl;
        try {
            acl = (MutableAcl) mutableAclService.readAclById(oid);
        } catch (NotFoundException nfe) {
            return false;
        }
        List<AccessControlEntry> entries = acl.getEntries();
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getSid().equals(recipient) && entries.get(i).getPermission().equals(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Delete a particular permission a user has for an object.
     * Uses code from SpringSecurity ContactManager example.
     * @param classReference Type of the target object
     * @param objectId Id of the target object
     * @param recipient User who's permission we are adding
     * @param permission Permission that is being removed
     */
    private void deletePermission(Class<?> classReference, int objectId, Sid recipient, Permission permission) {
        ObjectIdentity oid = new ObjectIdentityImpl(classReference, objectId);
        MutableAcl acl;
        try {
            acl = (MutableAcl) mutableAclService.readAclById(oid);
        } catch (NotFoundException nfe) {
            acl = mutableAclService.createAcl(oid);
        }
        List<AccessControlEntry> entries = acl.getEntries();
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getSid().equals(recipient) && entries.get(i).getPermission().equals(permission)) {
                acl.deleteAce(i);
            }
        }
        mutableAclService.updateAcl(acl);
        if (logger.isDebugEnabled()) {
            logger.debug("Deleted ACL permissions on " + classReference.getName() + " for recipient " + recipient);
        }
    }

    @Override
    @PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
    public void deleteUser(DlpUser user) {
        this.userManager.deleteUser(user);
    }

    @Override
    public boolean isLoggedIn() {
        return this.userManager.isLoggedIn();
    }

    @Override
    public boolean isUserAdmin() {
        return this.userManager.isUserAdmin();
    }

    @Override
    public UserInformation getUserInformation() {
        UserInformation info = new UserInformation();
        info.setDisplayName(this.userManager.getUserDisplayName());
        info.setProvidedConsent(this.userManager.getProvidedConsent());
        return info;
    }

    @Override
    public UserCreationResponse registerUser(String email, String password, String givenName, String lastName, String organisation, boolean consent) {
        return this.userManager.createUser(email, password, givenName, lastName, organisation, consent);
    }

    @Override
    public void provideConsent() throws AnonymousConsentException {
        try {
            this.userManager.setProvidedConsent(true, ServletUtils.getRequest());
        } catch (IllegalAccessException e) {
            throw new AnonymousConsentException();
        }
    }
}
