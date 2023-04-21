package com.germinus.portlet.book_viewer.action;

import com.germinus.xpression.cms.CMSRuntimeException;
import com.germinus.xpression.cms.action.CMSPortletAction;
import com.germinus.xpression.groupware.action.GroupwareHelper;
import com.germinus.xpression.cms.contents.Content;
import com.germinus.xpression.cms.contents.ContentManager;
import com.germinus.xpression.cms.contents.ContentNotFoundException;
import com.germinus.xpression.cms.contents.DynaCMSData;
import com.germinus.xpression.cms.contents.MalformedContentException;
import com.germinus.xpression.cms.util.ManagerRegistry;
import com.germinus.xpression.content_editor.action.ShowChapterContentAction;
import com.germinus.xpression.menu.Organizations;
import com.germinus.xpression.menu.ToolMenu;
import com.germinus.xpression.menu.ToolMenuItem;
import java.io.IOException;
import java.io.PrintWriter;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * Retrieves the link of the following chapter.
 *
 * @author acheca
 */
public class GetLastChapterAction extends CMSPortletAction {

    private static final Log log = LogFactory.getLog(GetNextChapterAction.class);

    private static ContentManager contentManager = ManagerRegistry.getContentManager();

    public void processAction(ActionMapping mapping, ActionForm form, PortletConfig config, ActionRequest req, ActionResponse res) throws IOException {
        String contentId = req.getParameter("contentId");
        String chapterId = req.getParameter("chapterId");
        String fieldId = req.getParameter("fieldId");
        HttpServletResponse httpResponse = GroupwareHelper.getLiferayHelper().convertToHttpServletResponse(res);
        PrintWriter writer = httpResponse.getWriter();
        try {
            Content content = contentManager.getContentById(contentId);
            Organizations orgs = null;
            try {
                orgs = (Organizations) PropertyUtils.getNestedProperty(content, fieldId);
                ToolMenu chaptersMenu = orgs.getDefaultOrganization();
                ToolMenuItem chapter = null;
                if (chaptersMenu.getChildren().size() > 0) chapter = (ToolMenuItem) chaptersMenu.getChildren().get(chaptersMenu.getChildren().size() - 1);
                if (chapter == null) {
                    writer.write("null:true:false");
                    req.setAttribute("prevChapters", "true");
                    req.setAttribute("nextChapter", "false");
                } else {
                    writer.write(chapter.getLink() + ":true:false");
                    req.setAttribute("prevChapters", "true");
                    req.setAttribute("nextChapter", "false");
                }
            } catch (Throwable e) {
                log.error("Error obtaning Organizations property called: " + fieldId, e);
                writer.write("" + null);
            }
        } catch (ContentNotFoundException ex) {
            log.warn("Error retrieving content " + contentId, ex);
        } catch (MalformedContentException ex) {
            log.warn("Error retrieving content " + contentId + ".Malformed content", ex);
        }
    }
}
