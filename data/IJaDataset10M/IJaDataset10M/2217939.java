package org.kablink.teaming.web.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kablink.teaming.UncheckedIOException;
import org.kablink.teaming.context.request.HttpSessionContext;
import org.kablink.teaming.context.request.RequestContext;
import org.kablink.teaming.context.request.RequestContextHolder;
import org.kablink.teaming.context.request.SessionContext;
import org.kablink.teaming.dao.ProfileDao;
import org.kablink.teaming.domain.Binder;
import org.kablink.teaming.domain.Folder;
import org.kablink.teaming.domain.NoUserByTheNameException;
import org.kablink.teaming.domain.User;
import org.kablink.teaming.domain.Workspace;
import org.kablink.teaming.module.binder.BinderModule;
import org.kablink.teaming.module.ldap.LdapModule;
import org.kablink.teaming.module.zone.ZoneModule;
import org.kablink.teaming.portletadapter.MultipartFileSupport;
import org.kablink.teaming.portletadapter.portlet.HttpServletRequestReachable;
import org.kablink.teaming.runas.RunasCallback;
import org.kablink.teaming.runas.RunasTemplate;
import org.kablink.teaming.security.accesstoken.AccessTokenManager;
import org.kablink.teaming.util.SPropsUtil;
import org.kablink.teaming.util.SZoneConfig;
import org.kablink.teaming.util.SimpleMultipartFile;
import org.kablink.teaming.util.SpringContextUtil;
import org.kablink.teaming.util.TempFileUtil;
import org.kablink.teaming.util.WindowsUtil;
import org.kablink.teaming.web.WebKeys;
import org.kablink.teaming.web.portlet.ParamsWrappedActionRequest;
import org.kablink.util.Html;
import org.kablink.util.PortalDetector;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

public class WebHelper {

    protected static Log logger = LogFactory.getLog(WebHelper.class);

    protected static Pattern htmlEscapes = Pattern.compile("(\\&[^;]*;)");

    public static boolean isUserLoggedIn(HttpServletRequest request) {
        try {
            getRequiredUserName(request);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public static boolean isUserLoggedIn(PortletRequest request) {
        try {
            getRequiredUserName(request);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public static boolean isGuestLoggedIn(HttpServletRequest request) {
        try {
            String username = getRequiredUserName(request);
            if (username.equals(SZoneConfig.getGuestUserName(getZoneNameByVirtualHost(request)))) return true; else return false;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public static boolean isGuestLoggedIn(PortletRequest request) {
        try {
            String username = getRequiredUserName(request);
            if (username.equals(SZoneConfig.getGuestUserName(getZoneNameByVirtualHost(request)))) return true; else return false;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    public static boolean isMethodPost(PortletRequest request) {
        HttpServletRequest req = null;
        ActionRequest actionRequest;
        if (request instanceof ParamsWrappedActionRequest) {
            actionRequest = ((ParamsWrappedActionRequest) request).getActionRequest();
            if (actionRequest instanceof HttpServletRequestReachable) {
                req = ((HttpServletRequestReachable) actionRequest).getHttpServletRequest();
            }
        }
        if (req != null) return "post".equals(req.getMethod().toLowerCase());
        return false;
    }

    public static boolean isBinderPreDeleted(Long binderId) {
        BinderModule bm = ((BinderModule) SpringContextUtil.getBean("binderModule"));
        Binder binder = bm.getBinder(binderId);
        boolean reply = false;
        if (binder instanceof Workspace) {
            reply = ((Workspace) binder).isPreDeleted();
        } else if (binder instanceof Folder) {
            reply = ((Folder) binder).isPreDeleted();
        }
        return reply;
    }

    public static HttpServletRequest getHttpServletRequest(PortletRequest request) {
        HttpServletRequest req = null;
        ActionRequest actionRequest;
        if (request instanceof HttpServletRequestReachable) {
            req = ((HttpServletRequestReachable) request).getHttpServletRequest();
        }
        return req;
    }

    public static String getRequiredUserName(HttpServletRequest request) throws IllegalStateException {
        HttpSession ses = getRequiredSession(request);
        return (String) ses.getAttribute(WebKeys.USER_NAME);
    }

    public static String getRequiredUserName(PortletRequest request) throws IllegalStateException {
        PortletSession ses = getRequiredPortletSession(request);
        return (String) ses.getAttribute(WebKeys.USER_NAME, PortletSession.APPLICATION_SCOPE);
    }

    public static Long getRequiredUserId(HttpServletRequest request) throws IllegalStateException {
        HttpSession ses = getRequiredSession(request);
        return (Long) ses.getAttribute(WebKeys.USER_ID);
    }

    public static Long getRequiredUserId(PortletRequest request) throws IllegalStateException {
        PortletSession ses = getRequiredPortletSession(request);
        return (Long) ses.getAttribute(WebKeys.USER_ID, PortletSession.APPLICATION_SCOPE);
    }

    public static String getRequiredZoneName(HttpServletRequest request) throws IllegalStateException {
        HttpSession ses = getRequiredSession(request);
        return (String) ses.getAttribute(WebKeys.ZONE_NAME);
    }

    public static String getRequiredZoneName(PortletRequest request) throws IllegalStateException {
        PortletSession ses = getRequiredPortletSession(request);
        return (String) ses.getAttribute(WebKeys.ZONE_NAME, PortletSession.APPLICATION_SCOPE);
    }

    public static Long getRequiredZoneId(HttpServletRequest request) throws IllegalStateException {
        HttpSession ses = getRequiredSession(request);
        return (Long) ses.getAttribute(WebKeys.ZONE_ID);
    }

    public static Long getRequiredZoneId(PortletRequest request) throws IllegalStateException {
        PortletSession ses = getRequiredPortletSession(request);
        return (Long) ses.getAttribute(WebKeys.ZONE_ID, PortletSession.APPLICATION_SCOPE);
    }

    public static PortletSession getRequiredPortletSession(PortletRequest request) throws IllegalStateException {
        if (null == request) {
            return null;
        }
        PortletSession ses = request.getPortletSession(false);
        if (ses != null) {
            if (ses.getAttribute(WebKeys.USER_NAME, PortletSession.APPLICATION_SCOPE) == null) {
                String username = getRemoteUserName(request);
                if (username != null) {
                    putContext(ses, getProfileDao().findUserByName(username, getZoneIdByVirtualHost(request)));
                } else {
                    throw new IllegalStateException("No valid session - Illegal request sequence.");
                }
            }
        } else {
            String username = getRemoteUserName(request);
            if (username != null) {
                ses = request.getPortletSession();
                putContext(ses, getProfileDao().findUserByName(username, getZoneIdByVirtualHost(request)));
            } else {
                throw new IllegalStateException("No valid session - Illegal request sequence");
            }
        }
        return ses;
    }

    public static HttpSession getRequiredSession(HttpServletRequest request) throws IllegalStateException {
        if (null == request) {
            return null;
        }
        final HttpSession ses = request.getSession();
        final String infoId = (String) ses.getAttribute(WebKeys.TOKEN_INFO_ID);
        if (infoId == null) {
            String ldapGuid;
            LdapModule ldapModule;
            final User user;
            User tmpUser = null;
            String username = (String) ses.getAttribute(WebKeys.USER_NAME);
            if (username == null) {
                username = getRemoteUserName(request);
            }
            if (username == null) {
                username = SZoneConfig.getGuestUserName(getZoneNameByVirtualHost(request));
            }
            ldapModule = getLdapModule();
            ldapGuid = ldapModule.readLdapGuidFromDirectory(username, getZoneIdByVirtualHost(request));
            if (ldapGuid != null && ldapGuid.length() > 0) {
                try {
                    tmpUser = getProfileDao().findUserByLdapGuid(ldapGuid, getZoneIdByVirtualHost(request));
                } catch (NoUserByTheNameException ex) {
                }
            }
            if (tmpUser == null) {
                tmpUser = getProfileDao().findUserByName(username, getZoneIdByVirtualHost(request));
            }
            user = tmpUser;
            putContext(ses, user);
            if (!user.isShared() || SPropsUtil.getBoolean("remoteapp.interactive.token.support.guest", true)) {
                final HttpSession session = ses;
                RunasTemplate.runas(new RunasCallback() {

                    public Object doAs() {
                        String infoId = getAccessTokenManager().createTokenInfoSession(user.getId(), ses.getId());
                        session.setAttribute(WebKeys.TOKEN_INFO_ID, infoId);
                        return null;
                    }
                }, user);
            }
        }
        return ses;
    }

    public static void putContext(HttpSession ses, User user) {
        ses.setAttribute(WebKeys.ZONE_NAME, user.getParentBinder().getRoot().getName());
        ses.setAttribute(WebKeys.ZONE_ID, user.getZoneId());
        ses.setAttribute(WebKeys.USER_NAME, user.getName());
        ses.setAttribute(WebKeys.USER_ID, user.getId());
    }

    private static void putContext(PortletSession ses, User user) {
        ses.setAttribute(WebKeys.ZONE_NAME, user.getParentBinder().getRoot().getName(), PortletSession.APPLICATION_SCOPE);
        ses.setAttribute(WebKeys.ZONE_ID, user.getZoneId(), PortletSession.APPLICATION_SCOPE);
        ses.setAttribute(WebKeys.USER_NAME, user.getName(), PortletSession.APPLICATION_SCOPE);
        ses.setAttribute(WebKeys.USER_ID, user.getId(), PortletSession.APPLICATION_SCOPE);
    }

    /**
	 * Returns a handle on the uploaded file. This handle is guaranteed to be
	 * valid only during the current server session. In other words, the handle
	 * is not persistent and will be lost once the server shuts down.
	 * It returns <code>null</code> if there is no uploaded file.
	 * 
	 * @param request
	 * @return
	 */
    public static String getFileHandleOnUploadedFile(ActionRequest request) throws IOException {
        Map fileMap = null;
        if (request instanceof MultipartFileSupport) fileMap = ((MultipartFileSupport) request).getFileMap();
        if (fileMap == null || fileMap.size() == 0) return null;
        MultipartFile file = (MultipartFile) fileMap.values().iterator().next();
        String fileName = file.getOriginalFilename();
        if (!validateFilenameForSafeLeaf(fileName)) throw new UncheckedIOException(new IOException("Illegal file name [" + fileName + "]"));
        String prefix = String.valueOf(fileName.length()) + "-" + fileName + "_";
        File destFile = TempFileUtil.createTempFile(prefix);
        file.transferTo(destFile);
        return destFile.getName();
    }

    public static boolean validateFilenameForSafeLeaf(String filename) throws UnsupportedEncodingException {
        byte[] bytes = filename.getBytes("UTF-8");
        for (int b : bytes) {
            if (b == 0x00) return false;
        }
        if (filename.contains("/")) return false;
        if (filename.contains("\\")) return false;
        return true;
    }

    /**
	 * Returns a handle on the uploaded iCal file. This handle is guaranteed to be
	 * valid only during the current server session. In other words, the handle
	 * is not persistent and will be lost once the server shuts down.
	 * It returns <code>null</code> if there is no uploaded file.
	 * 
	 * @param request
	 * @return
	 */
    public static String getFileHandleOnUploadedCalendarFile(ActionRequest request) throws IOException {
        Map fileMap = null;
        if (request instanceof MultipartFileSupport) fileMap = ((MultipartFileSupport) request).getFileMap();
        if (fileMap == null || fileMap.size() == 0) return null;
        MultipartFile mpfile = (MultipartFile) fileMap.values().iterator().next();
        String fileName = mpfile.getOriginalFilename();
        if (!validateFilenameForSafeLeaf(fileName)) throw new UncheckedIOException(new IOException("Illegal file name [" + fileName + "]"));
        BufferedReader breader = new BufferedReader(new InputStreamReader(mpfile.getInputStream()));
        String prefix = String.valueOf(fileName.length()) + "-" + fileName + "_";
        File destFile = TempFileUtil.createTempFile(prefix);
        BufferedWriter bwriter = new BufferedWriter(new FileWriter(destFile));
        while (breader.ready()) {
            String line = breader.readLine();
            if (line.endsWith("=")) {
                while (line.endsWith("=") && breader.ready()) {
                    String temp = line.substring(0, line.length() - 1);
                    bwriter.write(temp);
                    line = breader.readLine();
                }
                bwriter.write(line);
            } else {
                bwriter.write(line);
            }
            bwriter.newLine();
        }
        breader.close();
        bwriter.close();
        return destFile.getName();
    }

    /**
	 * Wraps the file handle in a MultipartFile datastructure. This is used
	 * primarily to put the data in an argument format compatible with some
	 * of the module methods. 
	 * 
	 * @param fileHandle
	 * @return
	 * @throws IOException
	 */
    public static SimpleMultipartFile wrapFileHandleInMultipartFile(String fileHandle) throws IOException {
        int idx = fileHandle.indexOf("-");
        int fileNameLength = Integer.parseInt(fileHandle.substring(0, idx));
        String fileName = fileHandle.substring(idx + 1, idx + 1 + fileNameLength);
        File file = TempFileUtil.getTempFileByName(fileHandle);
        SimpleMultipartFile mf = new SimpleMultipartFile(fileName, file, true);
        return mf;
    }

    /**
	 * Returns the file name of the uploaded file
	 * 
	 * @param fileHandle
	 * @return fileName
	 */
    public static String getFileName(String fileHandle) {
        int idx = fileHandle.indexOf("-");
        if (idx < 0) return null;
        int fileNameLength = Integer.parseInt(fileHandle.substring(0, idx));
        return fileHandle.substring(idx + 1, idx + 1 + fileNameLength);
    }

    public static void readFileHandleContent(String fileHandle, OutputStream out) throws IOException {
        FileCopyUtils.copy(new BufferedInputStream(TempFileUtil.openTempFile(fileHandle)), out);
    }

    /**
	 * Clean up resources associated with the file handle. Must be called
	 * when the application is done with the file handle.
	 * 
	 * @param fileHandle
	 */
    public static void releaseFileHandle(String fileHandle) {
        File file = TempFileUtil.getTempFileByName(fileHandle);
        file.delete();
    }

    public static String getNormalizedTitle(String title) {
        if (title == null) return null;
        String titleTrimmed = Html.stripHtml(title.trim());
        if (titleTrimmed.equals("")) return null;
        Matcher m1 = htmlEscapes.matcher(titleTrimmed);
        int loopDetector = 0;
        while (m1.find()) {
            if (loopDetector > 2000) {
                logger.error("Error processing markup: " + titleTrimmed);
                break;
            }
            titleTrimmed = m1.replaceFirst(" ");
            m1 = htmlEscapes.matcher(titleTrimmed);
        }
        String normalTitle = titleTrimmed.replaceAll("[\\P{L}&&\\P{N}]", " ");
        normalTitle = normalTitle.replaceAll(" ++", "_");
        normalTitle = normalTitle.toLowerCase();
        return normalTitle;
    }

    public static boolean isUnauthenticatedRequest(HttpServletRequest request) {
        Boolean unathenticatedRequest = (Boolean) request.getAttribute(WebKeys.UNAUTHENTICATED_REQUEST);
        return Boolean.TRUE.equals(unathenticatedRequest);
    }

    public static boolean isUnauthenticatedRequest(PortletRequest request) {
        Boolean unathenticatedRequest = (Boolean) request.getAttribute(WebKeys.UNAUTHENTICATED_REQUEST);
        return Boolean.TRUE.equals(unathenticatedRequest);
    }

    public static String getRemoteUserName(HttpServletRequest request) {
        String name = request.getRemoteUser();
        if (name != null && name.length() == 0) {
            name = null;
        }
        if (name != null) name = WindowsUtil.getSamaccountname(name);
        return name;
    }

    private static String getRemoteUserName(PortletRequest request) {
        if (PortalDetector.isLiferay()) {
            return null;
        } else {
            String name = request.getRemoteUser();
            if (name != null && name.length() > 0) name = WindowsUtil.getSamaccountname(name);
            return name;
        }
    }

    public static String getZoneNameByVirtualHost(ServletRequest request) {
        return getZoneModule().getZoneNameByVirtualHost(request.getServerName().toLowerCase());
    }

    public static Long getZoneIdByVirtualHost(ServletRequest request) {
        return getZoneModule().getZoneIdByVirtualHost(request.getServerName().toLowerCase());
    }

    public static String getZoneNameByVirtualHost(PortletRequest request) {
        return getZoneModule().getZoneNameByVirtualHost(request.getServerName().toLowerCase());
    }

    public static Long getZoneIdByVirtualHost(PortletRequest request) {
        return getZoneModule().getZoneIdByVirtualHost(request.getServerName().toLowerCase());
    }

    private static ZoneModule getZoneModule() {
        return (ZoneModule) SpringContextUtil.getBean("zoneModule");
    }

    /**
	 * 
	 * @return
	 */
    private static LdapModule getLdapModule() {
        return (LdapModule) SpringContextUtil.getBean("ldapModule");
    }

    private static ProfileDao getProfileDao() {
        return (ProfileDao) SpringContextUtil.getBean("profileDao");
    }

    private static AccessTokenManager getAccessTokenManager() {
        return (AccessTokenManager) SpringContextUtil.getBean("accessTokenManager");
    }

    public static String getTokenInfoId(HttpServletRequest request) {
        HttpSession ses = request.getSession(false);
        if (ses == null) return null;
        return (String) ses.getAttribute(WebKeys.TOKEN_INFO_ID);
    }

    public static HttpSession getCurrentHttpSession() {
        HttpSession reply = null;
        RequestContext rc = RequestContextHolder.getRequestContext();
        if (rc != null) {
            SessionContext sc = rc.getSessionContext();
            if (sc != null && sc instanceof HttpSessionContext) {
                reply = ((HttpSessionContext) sc).getHttpSession();
            }
        }
        return reply;
    }
}
