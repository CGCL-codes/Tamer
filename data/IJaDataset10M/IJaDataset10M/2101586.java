package org.eledge.pages;

import static org.eledge.Eledge.checkParameters;
import static org.eledge.Eledge.currentCourse;
import static org.eledge.Eledge.currentUser;
import static org.eledge.Eledge.dataContext;
import static org.eledge.Eledge.dbcommit;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.cayenne.DataObjectUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.tapestry.IExternalPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.link.ILinkRenderer;
import org.eledge.BadPageLockException;
import org.eledge.EledgeGlobal;
import org.eledge.components.EledgeLinkRenderer;
import org.eledge.domain.BaseWebPage;
import org.eledge.domain.Course;
import org.eledge.domain.CourseUploadedResource;
import org.eledge.domain.DynamicWebPage;
import org.eledge.domain.ResourceWebPage;
import org.eledge.domain.User;
import org.eledge.domain.WebPageParagraph;
import org.eledge.domain.permissions.PermissionDeterminer;
import org.rz.resultmessage.MessageWrapper;
import org.rz.tapestryutils.ListPropertySelectionModel;

/**
 * @author robertz
 * 
 */
public abstract class EditWebPage extends EledgeSecureBasePage implements PageValidateListener, IExternalPage {

    public abstract BaseWebPage getPageToEdit();

    public abstract void setPageToEdit(BaseWebPage page);

    public abstract WebPageParagraph getNewParagraph();

    public abstract void setNewParagraph(WebPageParagraph p);

    public abstract int getListIndex();

    public abstract void setListIndex(int value);

    public abstract String getNewSection();

    public abstract void setNewSection(String s);

    public abstract WebPageParagraph getCurrPar();

    public abstract void setCurrPar(WebPageParagraph para);

    private Logger log = Logger.getLogger(EditWebPage.class);

    private boolean allowOtherSectionEdit = false;

    public String getNewSectionWrapper() {
        setNewSection(Integer.toString(getListIndex() + 1));
        return getNewSection();
    }

    public void setNewSectionWrapper(String section) {
        setNewSection(section);
    }

    public boolean getCanUserAddParagraphs() {
        return PermissionDeterminer.hasEditPermission(currentUser(), getPageToEdit());
    }

    public boolean isLocked() {
        EledgeGlobal eg = (EledgeGlobal) getGlobal();
        boolean ret = eg.isPageLocked(getPageToEdit(), currentUser());
        System.out.println("Page locked status: " + ret);
        if (!ret) {
            System.out.println("I'm locking the " + getPageToEdit().getName() + " page.");
            eg.lockPage(getPageToEdit(), currentUser());
        }
        return ret;
    }

    public Date getLockEndTime() {
        Calendar c = Calendar.getInstance();
        Date time = ((EledgeGlobal) getGlobal()).getLockTime(getPageToEdit());
        if (time == null) {
            return new Date();
        }
        c.setTime(time);
        c.add(Calendar.MINUTE, 1);
        return c.getTime();
    }

    public boolean isParagraphEditable() {
        WebPageParagraph paragraph = getCurrPar();
        return PermissionDeterminer.hasEditPermission(currentUser(), paragraph);
    }

    public boolean getAreParticipantsEditableByUser() {
        WebPageParagraph paragraph = getCurrPar();
        return PermissionDeterminer.hasEditParticipantsPermission(currentUser(), paragraph);
    }

    @Override
    public void pageValidate(PageEvent event) {
        super.pageValidate(event);
        BaseWebPage page = getPageToEdit();
        if (page != null) {
            if (!PermissionDeterminer.hasEditPermission(currentUser(), page)) {
                ErrorPage errorPage = (ErrorPage) event.getRequestCycle().getPage("ErrorPage");
                errorPage.setErrors(new MessageWrapper(getMessage("str_invalid_permission")));
                throw new PageRedirectException(errorPage);
            }
        }
    }

    public void saveEditsListener(IRequestCycle cycle) {
        EditWebPageHelper helper = new EditWebPageHelper((DynamicWebPage) getPageToEdit(), getNewParagraph(), getNewSection());
        helper.savePage();
        dbcommit(cycle);
        setNewParagraph(new WebPageParagraph());
        ViewPage page = (ViewPage) cycle.getPage("ViewPage");
        page.setPageToView(getPageToEdit());
        ((EledgeGlobal) getGlobal()).unlockPage(getPageToEdit());
        cycle.activate(page);
    }

    public void removeParagraph(IRequestCycle cycle) {
        Object[] params = cycle.getServiceParameters();
        if (!checkParameters(params, DynamicWebPage.class, WebPageParagraph.class)) {
            doErrorPage("invalid_delete_request", cycle);
        }
        DynamicWebPage page = (DynamicWebPage) params[0];
        WebPageParagraph paragraph = (WebPageParagraph) params[1];
        page.removeFromParagraphs(paragraph);
        this.setPageToEdit(page);
        dbcommit(cycle);
        return;
    }

    public IPropertySelectionModel getResourceModel() {
        return new ListPropertySelectionModel(CourseUploadedResource.lookupAvailableResources(getCurrentCourse()));
    }

    public void saveResourcePage(IRequestCycle cycle) {
        dbcommit(cycle);
        ViewPage page = (ViewPage) cycle.getPage("ViewPage");
        page.setPageToView(getPageToEdit());
        ((EledgeGlobal) getGlobal()).unlockPage(getPageToEdit());
        cycle.activate(page);
    }

    public void updateLock(IRequestCycle cycle) {
        String pid = (String) cycle.getServiceParameters()[0];
        BaseWebPage p = (BaseWebPage) DataObjectUtils.objectForPK(dataContext(), BaseWebPage.class, Integer.parseInt(pid));
        User u = currentUser();
        if (!PermissionDeterminer.hasEditPermission(u, p)) {
            log.debug("Error: no permission to edit page " + p.getName() + " (course: " + p.getCourse().getName() + ") for user: " + currentUser().getUserId() + "; NOT updating lock.");
            return;
        }
        log.debug("updating lock for " + p.getName());
        EledgeGlobal global = (EledgeGlobal) getGlobal();
        try {
            global.updateLock(p, u);
        } catch (BadPageLockException e) {
            log.debug("hit a badpagelockexception", e);
            if (e.getDidLockExist()) {
                throw new RuntimeException(e);
            } else {
                global.lockPage(p, u);
            }
        }
    }

    public boolean isResourcePage() {
        return getPageToEdit() instanceof ResourceWebPage;
    }

    public List<?> getPgVarList() {
        return getCurrentCourse().getTemplate() == null ? null : getCurrentCourse().getTemplate().getPageVariables();
    }

    public CourseUploadedResource getResource() {
        return ((ResourceWebPage) getPageToEdit()).getResource();
    }

    public void setResource(CourseUploadedResource res) {
        getRPage().setResource(res);
    }

    private ResourceWebPage getRPage() {
        return (ResourceWebPage) getPageToEdit();
    }

    public Integer getWidth() {
        return getRPage().getWidth();
    }

    public void setWidth(Integer i) {
        getRPage().setWidth(i);
    }

    public Integer getHeight() {
        return getRPage().getHeight();
    }

    public void setHeight(Integer i) {
        getRPage().setHeight(i);
    }

    public List<?> getParVarList() {
        Course c = currentCourse();
        return c.getTemplate() == null ? null : c.getTemplate().getParagraphVariables();
    }

    public ILinkRenderer getErenderer() {
        return new EledgeLinkRenderer();
    }

    public Object[] getRmParParams() {
        return new Object[] { getPageToEdit(), getCurrPar() };
    }

    public String getLockTxt() {
        return format("str_locked", getPageToEdit().getName(), getLockEndTime());
    }

    public String getPgtitle() {
        return format("str_title_edit_page", getPageToEdit().getName());
    }

    public String getHTMLText() {
        return getCurrPar().getHTMLText();
    }

    public void setHTMLText(String t) {
        getCurrPar().setHTMLText(t);
    }

    public String getHeading() {
        return getCurrPar().getHeading();
    }

    public void setHeading(String s) {
        getCurrPar().setHeading(s);
    }

    public String getPnum() {
        return format("str_paragraph", getCurrPar().getSection());
    }

    public List<WebPageParagraph> getParagraphs() {
        DynamicWebPage dwp = (DynamicWebPage) getPageToEdit();
        return PermissionDeterminer.editFilter(dwp.getParagraphs(), currentUser());
    }

    public String getExplainCreate() {
        return format("str_explain_create", getPageToEdit().getName());
    }

    public String getName() {
        return getPageToEdit().getName();
    }

    public String getPid() {
        if (getPageToEdit().getObjectId().isTemporary()) {
            dbcommit(getRequestCycle());
        }
        return Integer.toString(DataObjectUtils.intPKForObject(getPageToEdit()));
    }

    public void activateExternalPage(Object[] parameters, IRequestCycle cycle) {
        log.setLevel(Level.DEBUG);
        log.debug("activating external page");
        Object[] params = cycle.getServiceParameters();
        log.debug("checking parameters...");
        if (!checkParameters(params, String.class)) {
            log.debug("parameter check failed; redirecting to login page");
            doErrorPage("invalidpage", cycle);
            return;
        }
        String pgname = (String) params[0];
        log.debug("pgname is: " + pgname);
        BaseWebPage pageToEdit = BaseWebPage.lookupWebPage(pgname, getCurrentCourse());
        log.debug("page is: " + pageToEdit);
        if (PermissionDeterminer.hasEditPermission(currentUser(), pageToEdit)) {
            log.debug("setting page to edit to " + pgname);
            this.setPageToEdit(pageToEdit);
        } else {
            log.debug("no perm to edit " + pgname + "; redirecting to error page");
            doErrorPage(format("noperm", pgname), cycle, false);
        }
    }
}
