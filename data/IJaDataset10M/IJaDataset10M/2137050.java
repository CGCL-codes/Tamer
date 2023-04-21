package de.uni_leipzig.lots.jsp.tag;

import de.uni_leipzig.lots.webfrontend.Constants;
import de.uni_leipzig.lots.webfrontend.utils.HttpSessionUtils;
import de.uni_leipzig.lots.webfrontend.utils.ActionHistory;
import de.uni_leipzig.lots.webfrontend.container.UserContainer;
import de.uni_leipzig.lots.webfrontend.views.PageSetting;
import org.jetbrains.annotations.NotNull;
import javax.servlet.jsp.PageContext;
import javax.servlet.http.HttpSession;

/**
 * Static utility methods for tags.
 *
 * @author Alexander Kiel
 * @version $Id: TagUtil.java,v 1.5 2007/10/23 06:29:25 mai99bxd Exp $
 */
public class TagUtil implements Constants {

    @NotNull
    public static UserContainer getUserContainer(@NotNull PageContext pageContext) {
        HttpSession session = pageContext.getSession();
        return HttpSessionUtils.getUserContainer(session);
    }

    @NotNull
    public static PageSetting getPageSetting(@NotNull PageContext pageContext) {
        ActionHistory actionHistory = getActionHistory(pageContext);
        UserContainer userContainer = getUserContainer(pageContext);
        String path = actionHistory.getActual().getMapping().getPath();
        PageSetting pageSetting = userContainer.getPageSetting(path);
        if (pageSetting == null) {
            pageSetting = new PageSetting();
            pageSetting.setUserContainer(userContainer);
            pageSetting.setPageName(path);
            userContainer.putPageSetting(path, pageSetting);
        }
        return pageSetting;
    }

    @NotNull
    public static ActionHistory getActionHistory(@NotNull PageContext pageContext) {
        ActionHistory actionHistory = (ActionHistory) pageContext.getSession().getAttribute(ACTION_HISTORY);
        if (actionHistory == null) {
            throw new IllegalStateException("No action history");
        }
        return actionHistory;
    }

    public static Object getAttributeInAllScopes(PageContext pageContext, String name) {
        Object attribute = null;
        try {
            attribute = pageContext.getAttribute(name, PageContext.PAGE_SCOPE);
        } catch (IllegalStateException e) {
        }
        if (attribute == null) {
            try {
                attribute = pageContext.getAttribute(name, PageContext.REQUEST_SCOPE);
            } catch (IllegalStateException e) {
            }
            if (attribute == null) {
                try {
                    attribute = pageContext.getAttribute(name, PageContext.SESSION_SCOPE);
                } catch (IllegalStateException e) {
                }
                if (attribute == null) {
                    try {
                        attribute = pageContext.getAttribute(name, PageContext.APPLICATION_SCOPE);
                    } catch (IllegalStateException e) {
                    }
                }
            }
        }
        return attribute;
    }
}
