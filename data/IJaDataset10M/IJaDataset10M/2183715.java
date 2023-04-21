package br.ufmg.catustec.arangiSecurity.controller;

import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.apache.log4j.Logger;
import br.ufmg.catustec.arangi.commons.BasicException;
import br.ufmg.catustec.arangi.controller.IApplicationContext;
import br.ufmg.catustec.arangi.controller.StandardControllerHelper;
import br.ufmg.catustec.arangi.controller.bean.ControllerBean;
import br.ufmg.catustec.arangi.controller.bean.SelectList;
import br.ufmg.catustec.arangi.dto.BasicDTO;
import br.ufmg.catustec.arangi.model.IFacade;
import br.ufmg.catustec.arangi.model.ModelService;
import br.ufmg.catustec.arangiSecurity.dto.Profile;
import br.ufmg.catustec.arangiSecurity.dto.ProfileRole;
import br.ufmg.catustec.arangiSecurity.dto.Role;

@ManagedBean
@ApplicationScoped
public class ProfileController extends AppController {

    protected static Logger log = Logger.getLogger(ProfileController.class);

    @Override
    protected ControllerBean afterEdit(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        controllerBean = super.afterEdit(context, controllerBean);
        organizeRoles(controllerBean);
        decodeRoleType(controllerBean);
        return controllerBean;
    }

    private void decodeRoleType(ControllerBean controllerBean) throws BasicException {
        Profile profile = (Profile) controllerBean.getDto();
        SelectList selectList = (SelectList) controllerBean.getSelectListsMap().get("typeRoleList");
        StandardControllerHelper.localizeListLabels(controllerBean, profile.getAllRoles(), selectList, "type", "typeDescription");
    }

    @Override
    protected ControllerBean afterNewObject(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        controllerBean = super.afterNewObject(context, controllerBean);
        organizeRoles(controllerBean);
        return controllerBean;
    }

    /**
	 * Build roles list to be shown and mark the roles of the profile
	 * 
	 * @param controllerBean
	 */
    private void organizeRoles(ControllerBean controllerBean) throws BasicException {
        List<Role> allRoles = searchRoles(controllerBean);
        Profile profile = (Profile) controllerBean.getDto();
        profile.setAllRoles(allRoles);
        List<ProfileRole> profileRoles = profile.getProfileRoles();
        if (allRoles != null) {
            for (Role role : allRoles) {
                role.setCheckDelete(false);
                if (profileRoles != null) {
                    for (ProfileRole profileRole : profileRoles) {
                        if (role.equals(profileRole.getRole())) {
                            role.setCheckDelete(true);
                        }
                    }
                }
            }
        }
    }

    /**
	 * Configure the user loged in.
	 * 
	 * @param userContext
	 * @param request
	 */
    private List<Role> searchRoles(ControllerBean controllerBean) throws BasicException {
        IFacade facade = ModelService.getFacade();
        List<Role> listRoles = null;
        Role role = new Role();
        role.setLocale(controllerBean.getLocale());
        listRoles = facade.loadAll(role, Role.class.getName());
        return listRoles;
    }

    @Override
    protected ControllerBean beforeSave(IApplicationContext context, ControllerBean controllerBean, BasicDTO dto) throws BasicException {
        verifyRolesToAddDelete(context, controllerBean);
        return controllerBean;
    }

    @Override
    public ControllerBean save(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        verifyRolesToAddDelete(context, controllerBean);
        return super.save(context, controllerBean);
    }

    /**
	 * Verify the roles that been added and the roles that been excluded. After
	 * that, build the list to be shown.
	 * 
	 * @param context
	 * @param controllerBean
	 */
    private void verifyRolesToAddDelete(IApplicationContext context, ControllerBean controllerBean) {
        Profile profile = (Profile) controllerBean.getDto();
        List<Role> allRoles = profile.getAllRoles();
        List<ProfileRole> profileRoles = profile.getProfileRoles();
        if (allRoles != null) {
            for (Role role : allRoles) {
                boolean found = false;
                if (profileRoles != null) {
                    for (ProfileRole profileRole : profileRoles) {
                        if (role.equals(profileRole.getRole())) {
                            found = true;
                            if (role.isCheckDelete() == false) {
                                profileRole.setCheckDelete(true);
                            }
                            break;
                        }
                    }
                }
                if (!found && role.isCheckDelete()) {
                    ProfileRole profileRole = new ProfileRole();
                    profileRole.setRole(role);
                    profileRoles.add(profileRole);
                }
            }
        }
        if (profileRoles != null) {
            for (ProfileRole profileRole : profileRoles) {
                StandardControllerHelper.populateAudit(profileRole, context);
                profileRole.setMaster(profile);
            }
        }
    }

    @Override
    protected ControllerBean afterCancel(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        controllerBean.setForward("/auth/ProfileSearch.xhtml?event=new&viewID=POPUP");
        return controllerBean;
    }

    @Override
    protected ControllerBean afterOpen(IApplicationContext context, ControllerBean controllerBean) throws BasicException {
        controllerBean.setForward("/auth/ProfileSearch.xhtml?event=new&viewID=POPUP");
        return controllerBean;
    }
}
