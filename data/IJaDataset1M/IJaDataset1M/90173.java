package org.bionote.webapp.action.page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.bionote.om.IPage;
import org.bionote.om.service.PageService;
import org.bionote.page.type.PageType;
import org.bionote.page.type.PageTypeUtil;
import org.bionote.service.UserPermissions;
import org.bionote.webapp.action.BaseStrutsAction;

/**
 * @author mbreese
 *
 */
public class MetaLink extends BaseStrutsAction {

    public ActionForward bionoteExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (isCancelled(request)) {
            return mapping.findForward("Success");
        }
        PageService pageService = (PageService) springContext.getBean("pageService");
        UserPermissions userPermissions = (UserPermissions) springContext.getBean("permissions");
        if (container.getUser() == null || !userPermissions.isAllowed(container.getUser(), container.getSpace())) {
            return mapping.findForward("NotAllowed");
        }
        String typeClass = org.bionote.page.type.MetaLink.class.getName();
        Boolean isPrivate = getParameterBoolean(request, "private");
        Long fromPageId = getParameterLong(request, "fromId");
        Long linkPageId = getParameterLong(request, "linkId");
        IPage fromPage = pageService.findPage(fromPageId);
        IPage linkPage = pageService.findPage(linkPageId);
        String pageName = fromPage.getName() + "_" + linkPage.getName() + " Interaction";
        log.debug("Looking for meta page: *" + pageName + "*");
        IPage tmp = pageService.findPage(pageName, container.getSpace());
        if (tmp != null) {
            log.debug("found page:" + tmp.getName());
            return mapping.findForward("Exists");
        }
        PageType type = PageTypeUtil.getInstance(typeClass);
        boolean spaceAdmin = userPermissions.isAdmin(container.getUser(), container.getSpace());
        if (!spaceAdmin && fromPage != null) {
            isPrivate = new Boolean(fromPage.isPrivate());
        } else if (!spaceAdmin) {
            isPrivate = Boolean.FALSE;
        }
        if (isPrivate == null) {
            isPrivate = Boolean.FALSE;
        }
        IPage newPage = pageService.addPage(pageName, container.getSpace(), container.getUser(), type, isPrivate.booleanValue());
        request.setAttribute("forward_page", newPage);
        request.setAttribute("newSource", newPage.getPageType().getInitialContent(newPage));
        request.setAttribute("pageId", new Long(newPage.getId()));
        return mapping.findForward("Success");
    }
}
