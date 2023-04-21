package com.once;

import org.apache.log4j.Logger;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.once.log.PerformanceLogger;
import com.once.server.data.DataAccessException;
import com.once.server.security.IAuthenticator;
import com.once.server.security.ISecurityManager;
import com.once.server.security.SecurityFactory;

public class ActionCliPreferences extends Action {

    private static final Logger m_logger = Logger.getLogger(ActionCliPreferences.class);

    private static final String HEADER_AUTO_COMMIT = "autocommit";

    private static final String HEADER_INSERT_RETURN_STYLE = "insertreturnstyle";

    private static final String HEADER_LARGE_SUBBLOCK_ICONS = "largesubblockicons";

    private static final String HEADER_WILDCARD_SEARCH = "wildcardsearch";

    private static final String HEADER_MENU_BAR_X = "menubarx";

    private static final String HEADER_SESSION = "session";

    private static final String OPERATION_GET_PREFERENCES = "Get oCLI preferences";

    private static final long serialVersionUID = -2655471940842889276L;

    protected void serve(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        IAuthenticator authentication;
        ISecurityManager security;
        Map<String, String> preference;
        PerformanceLogger perform;
        PrintWriter page;
        String autoCommit;
        String insertReturnStyle;
        String largeSubblockIcons;
        String wildcardSearch;
        String menuBarX;
        String session;
        String username;
        boolean isSaveOperation;
        int userId;
        resetStartTime();
        request.getSession();
        response.setContentType(CLIENT_FORMAT);
        response.setCharacterEncoding(CLIENT_ENCODING);
        page = getOutputPage(response);
        session = decodeHeader(HEADER_SESSION, request, true);
        perform = PerformanceLogger.getLogger(session, this.getClass().getName());
        autoCommit = decodeHeader(HEADER_AUTO_COMMIT, request, false);
        menuBarX = decodeHeader(HEADER_MENU_BAR_X, request, false);
        insertReturnStyle = decodeHeader(HEADER_INSERT_RETURN_STYLE, request, false);
        largeSubblockIcons = decodeHeader(HEADER_LARGE_SUBBLOCK_ICONS, request, false);
        wildcardSearch = decodeHeader(HEADER_WILDCARD_SEARCH, request, false);
        perform.logStart(OPERATION_GET_PREFERENCES);
        authentication = SecurityFactory.getInstance().getAuthenticator();
        security = SecurityFactory.getInstance().getSecurityManager();
        isSaveOperation = autoCommit != null || menuBarX != null || insertReturnStyle != null || wildcardSearch != null && largeSubblockIcons != null;
        if (m_logger.isInfoEnabled() == true) {
            try {
                username = authentication.getUserLogin(session);
            } catch (DataAccessException except) {
                username = "<unknown>";
            }
            m_logger.info((isSaveOperation == true ? "Save" : "Restore") + " oCLI preferences. (User: " + username + " / IP: " + getClientIP(request) + ")");
        }
        try {
            if (authentication.isUserLoggedIn(session) == true) {
                userId = authentication.getUserId(session);
                try {
                    if (isSaveOperation == true) {
                        preference = new HashMap<String, String>();
                        preference.put(ISecurityManager.USER_PREFERENCES_AUTO_COMMIT, autoCommit);
                        preference.put(ISecurityManager.USER_PREFERENCES_MENU_BAR_X, menuBarX);
                        preference.put(ISecurityManager.USER_PREFERENCES_INSERT_RETURN_STYLE, insertReturnStyle);
                        preference.put(ISecurityManager.USER_PREFERENCES_LARGE_SUBBLOCK_ICONS, largeSubblockIcons);
                        preference.put(ISecurityManager.USER_PREFERENCES_WILDCARD_SEARCH, wildcardSearch);
                        security.setUserPreferences(userId, preference);
                    } else {
                        preference = security.getUserPreferences(userId);
                        page.println("t".equals(preference.get(ISecurityManager.USER_PREFERENCES_AUTO_COMMIT)) == true ? "true" : "false");
                        menuBarX = preference.get(ISecurityManager.USER_PREFERENCES_MENU_BAR_X);
                        page.println(menuBarX == null ? "50" : menuBarX);
                        insertReturnStyle = preference.get(ISecurityManager.USER_PREFERENCES_INSERT_RETURN_STYLE);
                        page.println(insertReturnStyle == null ? "0" : insertReturnStyle);
                        page.println("t".equals(preference.get(ISecurityManager.USER_PREFERENCES_LARGE_SUBBLOCK_ICONS)) == true ? "true" : "false");
                        page.println("t".equals(preference.get(ISecurityManager.USER_PREFERENCES_WILDCARD_SEARCH)) == true ? "true" : "false");
                    }
                } catch (DataAccessException except) {
                    m_logger.error("serve(HttpServletRequest, HttpServletResponse)", except);
                    page.print("false\nERROR: " + except.getMessage() + " (last query was: \"" + security.getGeneratedSQL() + "\")");
                }
            } else page.print("false\nERROR: User not logged in.");
        } catch (DataAccessException except) {
            m_logger.error("serve(HttpServletRequest, HttpServletResponse)", except);
            page.print("false\nERROR: " + except.getMessage());
        } finally {
            page.close();
            perform.logFinish();
        }
        return;
    }
}
