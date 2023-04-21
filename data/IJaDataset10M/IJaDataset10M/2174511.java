package purej.web.servlet;

import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import purej.logging.Logger;
import purej.logging.LoggerFactory;
import purej.security.JAASKey;
import purej.util.PatternMatchUtils;
import purej.util.StringUtils;
import purej.web.context.WebAppContext;
import purej.web.context.WebAppContextFactory;
import purej.web.domain.ControllerMapping;
import purej.web.domain.ExceptionMap;
import purej.web.domain.SecurityMap;
import purej.web.servlet.auth.UserAuthManager;

/**
 * ���? ���μ��� �۾���
 * 
 * @author SangBoo Lee
 * 
 */
abstract class ServletProcessWorker {

    private static final Logger log = LoggerFactory.getLogger(ServletProcessWorker.class, Logger.FRAMEWORK);

    /**
     * ������ ó���Ѵ�.
     * 
     * @param httpModel
     * @throws Exception
     */
    public void processConfigure(HTTPModel httpModel) throws Exception {
        WebAppContextFactory webAppContextFactory = WebAppContextFactory.getInstance();
        WebAppContext webAppContext = webAppContextFactory.getWebAppContext();
        ControllerConfigManager configManager = ControllerConfigManager.getInstance();
        configManager.checkedReload(webAppContext.getWebAppPath(), webAppContext.getControllerConfigPaths());
        String url = httpModel.getRequest().getServletPath();
        ControllerMappingFactory controllerMappingFactory = ControllerMappingFactory.getInstance();
        ControllerMapping controllerMapping = controllerMappingFactory.getControllerMapping(url);
        httpModel.setControllerMapping(controllerMapping);
        WebUtils webUtils = new WebUtils(httpModel.getRequestContext(), httpModel.getResponseContext());
        httpModel.setWebUtils(webUtils);
        httpModel.setInput(new Input(webUtils));
        httpModel.setOutput(new Output(webUtils));
        httpModel.setAuthentication(new Authentication(webUtils));
        httpModel.setMessage(new Message(webUtils));
    }

    /**
     * ��Ʈ�ѷ� ���� ������ üũ�Ѵ�.
     * 
     * @param httpModel
     * @throws Exception
     */
    protected void processAuthentication(HTTPModel httpModel) throws Exception {
        boolean hasAccessPermission = false;
        boolean isCheckObject = false;
        String targetObject = httpModel.getWebUtils().getRequest().getServletPath();
        Map<String, SecurityMap> securityMap = httpModel.getControllerMapping().getSecurityMap();
        Object[] keys = securityMap.keySet().toArray();
        for (int i = 0; i < keys.length; i++) {
            SecurityMap map = ((SecurityMap) securityMap.get(keys[i]));
            String urlPattern = map.getUrlPattern();
            if (PatternMatchUtils.simpleMatch(urlPattern, targetObject)) {
                log.info("Global security authentication url pattern check : targetObject=[" + targetObject + "], url-pattern=[" + urlPattern + "], roles=[" + StringUtils.arrayToCommaDelimitedString(map.getRoles()) + "]");
                isCheckObject = true;
                break;
            }
        }
        if (isCheckObject) {
            String userID = (String) httpModel.getWebUtils().getSessionValue(JAASKey.J_USER_ID_KEY);
            log.debug("userid = " + userID);
            if (StringUtils.isEmpty(userID)) throw new AuthenticationException("User not login.");
            UserAuthManager userAuthManager = new UserAuthManager(httpModel.getWebUtils().getSession());
            securityMap = httpModel.getControllerMapping().getSecurityMap();
            keys = securityMap.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                SecurityMap map = ((SecurityMap) securityMap.get(keys[i]));
                String urlPattern = map.getUrlPattern();
                if (PatternMatchUtils.simpleMatch(urlPattern, targetObject)) {
                    hasAccessPermission = userAuthManager.hasRoles(map.getRoles());
                    if (hasAccessPermission) break;
                }
            }
            if (hasAccessPermission) return;
            if (!hasAccessPermission) log.info("User role not found.");
            hasAccessPermission = userAuthManager.hasAccessPermission(targetObject);
            if (hasAccessPermission) return;
            if (!hasAccessPermission) log.info("User permission not found.");
            if (!hasAccessPermission) {
                throw new AuthenticationException("Not authenticated user. Can't access this object [" + targetObject + "]");
            }
        }
    }

    protected void processParameterBiding(HTTPModel httpModel) throws Exception {
    }

    /**
     * ��Ʈ�ѷ��� ó���Ѵ�.
     * 
     * @param commandFactory
     * @param commandProcessor
     * @param httpModel
     */
    protected void processController(ControllerCommandFactory commandFactory, ControllerCommandProcessor commandProcessor, HTTPModel httpModel) throws Exception {
        ControllerCommand command = (ControllerCommand) httpModel.getControllerMapping().getControllerClass().newInstance();
        String mehtod = httpModel.getControllerMapping().getProcessMethod();
        httpModel = commandProcessor.invoke(command, mehtod, httpModel);
    }

    /**
     * ��Ʈ�ѷ� ���� ó��
     * 
     * <br>
     * ��Ʈ�ѷ� Ŀ�ǵ��� ���� ó���Ѵ�. ���� Ŀ���� ��Ʈ�ѷ����� error �޼ҵ带 �������� ���� �۷ι� ���� ������ �����ϰ�
     * ��Ʈ�ѷ��� error �޼ҵ常 ���÷����Ͽ� �����Ѵ�.
     * 
     * @param httpModel
     * @param e
     */
    protected void processError(ControllerCommandFactory commandFactory, ControllerCommandProcessor commandProcessor, HTTPModel httpModel, Exception e) {
        try {
            log.info("Error handle process for exception : class=[" + e.getClass() + "], message=[" + e.getMessage() + "]");
            log.info("Global Error handler processing...");
            java.util.Map<String, ExceptionMap> exceptions = httpModel.getControllerMapping().getExceptionMap();
            if (exceptions != null) {
                Object[] keys = exceptions.keySet().toArray();
                for (int i = 0; i < keys.length; i++) {
                    ExceptionMap exceptioMap = exceptions.get(keys[i]);
                    if (e.getClass().equals(exceptioMap.getExceptionClass())) {
                        log.debug("Match exception handle : " + e.getClass() + ":" + exceptioMap.getExceptionClass());
                        if (exceptioMap.getHandlerClass() != null) {
                            try {
                                log.info("Find exception handler class : " + exceptioMap.getHandlerClass());
                                ExceptionHandler handler = exceptioMap.getHandlerClass().newInstance();
                                handler.handleException(httpModel, e);
                            } catch (Exception ex2) {
                                log.error("Global error handler failed : " + ex2.getMessage());
                                ex2.printStackTrace();
                            }
                        }
                        httpModel.setView(new FowardView(exceptioMap.getView()));
                    }
                }
            }
        } catch (Exception spex) {
            log.error("Controller error handle exception : " + spex.getMessage(), e);
            spex.printStackTrace();
        }
    }

    /**
     * �並 ó���Ѵ�.
     * 
     * @param httpModel
     */
    protected void processView(HTTPModel httpModel) {
        try {
            View view = httpModel.getView();
            view.rendering(httpModel);
        } catch (Exception ex) {
            log.error("View redering exception : " + ex.getMessage(), ex);
        }
    }

    /**
     * HTML����� ��ĳ���� �����Ѵ�.
     * 
     * @param cache
     * @param response
     */
    protected void processNoCache(HTTPModel httpModel) {
        HttpServletResponse response = httpModel.getResponse();
        boolean cache = httpModel.getControllerMapping().isCache();
        if (!cache) {
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache,no-store,max-age=0");
            response.setDateHeader("Expires", 1);
            log.debug("Response NO cache enabled.");
        } else {
            log.debug("Response cache enabled.");
        }
    }

    /**
     * ��Ű�� ó���Ѵ�.
     * 
     * @param cookises
     * @param response
     */
    protected void processCookies(HTTPModel httpModel) {
        javax.servlet.http.Cookie[] cookises = httpModel.getWebUtils().getCookies();
        HttpServletResponse response = httpModel.getResponse();
        if (cookises != null) {
            for (int c = 0; c < cookises.length; c++) {
                response.addCookie(cookises[c]);
            }
        }
    }

    /**
     * ������ �����̷�Ʈ
     * 
     * @param response
     * @param page
     * @throws java.io.IOException
     */
    protected void redirect(HttpServletResponse response, String page) throws java.io.IOException {
        response.sendRedirect(page);
    }
}
