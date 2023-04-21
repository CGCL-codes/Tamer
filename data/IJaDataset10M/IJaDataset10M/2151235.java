package au.edu.monash.merc.capture.struts2.action;

import java.util.GregorianCalendar;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import au.edu.monash.merc.capture.domain.PermissionRequest;
import au.edu.monash.merc.capture.domain.User;

@Scope("prototype")
@Controller("perm.permReqAction")
public class PermissionRequestAction extends DMCoreAction {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    private PermissionRequest permReq;

    private List<PermissionRequest> permRequests;

    public String applyForPerms() {
        try {
            user = retrieveLoggedInUser();
            collection = this.dmService.getCollectionById(collection.getId());
            User owner = collection.getOwner();
            if (user.getId() == owner.getId()) {
                addActionError(getText("collection.owner.does.not.need.apply.perms"));
                setNavAfterExc();
                return INPUT;
            }
            permReq = this.dmService.getCoPermissionRequestByReqUser(collection.getId(), user.getId());
            if (permReq == null) {
                permReq = new PermissionRequest();
            }
            setNavAfterSuccess();
        } catch (Exception e) {
            logger.error(e);
            addActionError(getText("failed.to.show.apply.collection.perms.page"));
            setNavAfterExc();
            return INPUT;
        }
        return SUCCESS;
    }

    public String sendPermsReq() {
        try {
            if (checkPermsReqError()) {
                setNavAfterSuccess();
                return INPUT;
            }
            collection = this.dmService.getCollectionById(collection.getId());
            User owner = collection.getOwner();
            user = retrieveLoggedInUser();
            if (user.getId() == owner.getId()) {
                addActionError(getText("collection.owner.does.not.need.apply.perms"));
                setNavAfterExc();
                return INPUT;
            }
            permReq.setOwner(owner);
            permReq.setRequestUser(user);
            permReq.setCollection(collection);
            permReq.setRequestTime(GregorianCalendar.getInstance().getTime());
            if (permReq.getId() == 0) {
                this.dmService.savePermissionRequest(permReq);
            }
            if (permReq.getId() > 0) {
                this.dmService.updatePermissionRequest(permReq);
            }
            setActionSuccessMsg(getText("apply.for.collection.permissions.successfully", new String[] { collection.getName() }));
            setNavAfterSuccess();
        } catch (Exception e) {
            logger.error(e);
            addActionError("failed.to.apply.collection.permissions");
            setNavAfterExc();
            return INPUT;
        }
        return SUCCESS;
    }

    private boolean checkPermsReqError() {
        if (!permReq.isViewAllowed() && !permReq.isUpdateAllowed() && !permReq.isImportAllowed() && !permReq.isExportAllowed() && !permReq.isDeleteAllowed() && !permReq.isChangePermAllowed()) {
            addFieldError("perms", getText("at.least.selected.permission.required"));
            return true;
        }
        return false;
    }

    private void setNavAfterExc() {
        String startNav = getText("allcollection.nav.label.name");
        String startNavLink = ActConstants.LIST_ALL_COLLECTIONS_ACTION;
        String secondNav = getText("apply.collection.permission.nav.label.name");
        setPageTitle(startNav, secondNav + " Error");
        navigationBar = generateNavLabel(startNav, startNavLink, secondNav, null, null, null);
    }

    private void setNavAfterSuccess() {
        String pageTitle = null;
        String startNav = getText("allcollection.nav.label.name");
        String startNavLink = ActConstants.LIST_ALL_COLLECTIONS_ACTION;
        String secondNav = collection.getName();
        String secondNavLink = ActConstants.VIEW_COLLECTION_DETAILS_ACTION + "?collection.id=" + collection.getId() + "&collection.owner.id=" + collection.getOwner().getId() + "&viewType=all";
        String thirdNav = getText("apply.collection.permission.nav.label.name");
        pageTitle = startNav + " - " + secondNav + " - " + thirdNav;
        setPageTitle(pageTitle);
        navigationBar = generateNavLabel(startNav, startNavLink, secondNav, secondNavLink, thirdNav, null);
    }

    public PermissionRequest getPermReq() {
        return permReq;
    }

    public void setPermReq(PermissionRequest permReq) {
        this.permReq = permReq;
    }

    public List<PermissionRequest> getPermRequests() {
        return permRequests;
    }

    public void setPermRequests(List<PermissionRequest> permRequests) {
        this.permRequests = permRequests;
    }
}
