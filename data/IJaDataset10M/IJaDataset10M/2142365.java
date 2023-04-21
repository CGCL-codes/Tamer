package net.sf.easyweb4j.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;
import net.sf.easyweb4j.controller.ActionResult;
import net.sf.easyweb4j.controller.Controller;
import net.sf.easyweb4j.controller.SpringManager;
import net.sf.easyweb4j.controller.ActionResult.NavigationType;
import net.sf.easyweb4j.exceptions.ControllerException;
import net.sf.easyweb4j.exceptions.DispatcherException;
import net.sf.easyweb4j.model.ModelELResolver;
import net.sf.easyweb4j.repository.RepositoryManager;
import net.sf.easyweb4j.util.FlashMap;
import net.sf.easyweb4j.util.ReflectionUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class DispatcherServlet extends HttpServlet {

    private static final String CONTROLLERS_PACKAGE_PARAM = "CONTROLLERS_PACKAGE";

    private static final String ROOT_CONTROLLER_PARAM = "ROOT_CONTROLLER";

    private static final String DEFAULT_ACTION = "index";

    private static final String LAYOUTS_DIRECTORY = "/WEB-INF/layouts/";

    private static final String VIEWS_DIRECTORY = "/WEB-INF/views/";

    private static final String VIEW_PAGE_ATTRIBUTE = "view";

    private static final String SPRING_UTIL_CLASS = "org.springframework.web.context.support.WebApplicationContextUtils";

    public static final String CONTEXT_ROOT_ATTRIBUTE = "contextRoot";

    public static final String DISPATCHER_ROOT_ATTRIBUTE = "dispatcherRoot";

    public static final String ID_PARAM_ATTRIBUTE = "easyweb4j.id";

    public static final String FLASH_MAP_ATTRIBUTE = "flash";

    private String controllersPackage, rootControllerName;

    private RepositoryManager repositoryManager;

    private SpringManager springManager;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        addELResolver(config.getServletContext());
        repositoryManager = new RepositoryManager();
        try {
            Class.forName(SPRING_UTIL_CLASS, false, this.getClass().getClassLoader());
            springManager = new SpringManager(getServletContext());
        } catch (ClassNotFoundException e) {
            springManager = null;
        }
        controllersPackage = config.getInitParameter(CONTROLLERS_PACKAGE_PARAM);
        rootControllerName = config.getInitParameter(ROOT_CONTROLLER_PARAM);
    }

    @Override
    public void destroy() {
        repositoryManager.closeFactories();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestMethod = request.getMethod();
        if (requestMethod.equals("OPTIONS") || requestMethod.equals("TRACE")) {
            super.service(request, response);
        } else {
            String pathInfo = request.getPathInfo();
            pathInfo = (pathInfo == null) ? "" : pathInfo;
            String[] pathComponents = pathInfo.split("/", 4);
            prepareFlashMap(request);
            disableCache(response);
            setRequestAttributes(pathComponents, request, response);
            try {
                Controller controller = createController(pathComponents, request, response);
                Method actionMethod = determineActionMethod(pathComponents, controller.getClass());
                ActionResult result;
                try {
                    repositoryManager.beginTransaction();
                    invokeAction(controller, actionMethod, request, response);
                    repositoryManager.commit();
                    result = controller.getActionResult();
                } catch (Exception e) {
                    repositoryManager.rollback();
                    controller.handleActionException(e);
                    result = controller.getActionResult();
                }
                if (!response.isCommitted()) {
                    try {
                        repositoryManager.markAsReadOnly();
                        repositoryManager.beginTransaction();
                        if (result.getNavigationType().equals(NavigationType.REDIRECT)) redirectView(result, request, response); else renderView(result, request, response);
                        repositoryManager.commit();
                    } catch (Exception e) {
                        repositoryManager.rollback();
                        throw e;
                    }
                }
                cleanup(controller);
            } catch (Exception e) {
                throw new DispatcherException("Servicing request failed.", e);
            } finally {
                repositoryManager.closePersistence();
            }
        }
    }

    private void disableCache(HttpServletResponse response) {
        response.setHeader("Pragma", "No-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-cache");
    }

    private void setRequestAttributes(String[] pathComponents, HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(ID_PARAM_ATTRIBUTE, getIdParam(pathComponents));
        request.setAttribute(CONTEXT_ROOT_ATTRIBUTE, request.getContextPath());
        request.setAttribute(DISPATCHER_ROOT_ATTRIBUTE, request.getContextPath() + request.getServletPath());
    }

    private Controller invokeAction(Controller controller, Method actionMethod, HttpServletRequest request, HttpServletResponse response) {
        try {
            controller.filterRequest();
            if (actionMethod != null && !response.isCommitted() && !controller.isNavigationInitiated()) actionMethod.invoke(controller);
            return controller;
        } catch (Exception e) {
            throw new DispatcherException("Invocation of action failed.", e);
        }
    }

    private void redirectView(ActionResult actionResult, HttpServletRequest request, HttpServletResponse response) {
        String controller = actionResult.getController();
        String action = actionResult.getView();
        String id = actionResult.getId();
        final StringBuilder redirectPath = new StringBuilder((String) request.getAttribute(DispatcherServlet.DISPATCHER_ROOT_ATTRIBUTE));
        if (controller != null) redirectPath.append("/" + controller);
        if (action != null) redirectPath.append("/" + action);
        if (id != null) redirectPath.append("/" + id);
        try {
            response.sendRedirect(redirectPath.toString());
        } catch (Exception e) {
            throw new ControllerException("Redirection Failed.", e);
        }
    }

    private void renderView(ActionResult actionResult, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute(VIEW_PAGE_ATTRIBUTE, VIEWS_DIRECTORY + actionResult.getController() + "/" + actionResult.getView() + ".jsp");
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(determineLayoutFile(actionResult, request, response));
        requestDispatcher.forward(request, response);
    }

    private String determineLayoutFile(ActionResult result, HttpServletRequest request, HttpServletResponse response) {
        try {
            String controllerLayoutFile;
            if (!result.isRenderLayout()) {
                controllerLayoutFile = (String) request.getAttribute(VIEW_PAGE_ATTRIBUTE);
                request.removeAttribute(VIEW_PAGE_ATTRIBUTE);
            } else {
                controllerLayoutFile = LAYOUTS_DIRECTORY + "Application.jsp";
                URL layoutURL = getServletContext().getResource(controllerLayoutFile);
                if (layoutURL == null) {
                    controllerLayoutFile = (String) request.getAttribute(VIEW_PAGE_ATTRIBUTE);
                    request.removeAttribute(VIEW_PAGE_ATTRIBUTE);
                }
            }
            return controllerLayoutFile;
        } catch (Exception e) {
            throw new DispatcherException("Redering layout failed.", e);
        }
    }

    private Controller createController(String[] pathComponents, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String unqualifiedControllerName = getControllerName(pathComponents);
        String controllerName = qualifyControllerName(unqualifiedControllerName);
        String actionName = getActionName(pathComponents);
        ActionResult defaultActionResult = new ActionResult(NavigationType.RENDER, unqualifiedControllerName, actionName, null, true);
        Controller controllerInstance = (Controller) Class.forName(controllerName).newInstance();
        injectDependencies(request, response, actionName, defaultActionResult, controllerInstance);
        return controllerInstance;
    }

    private void injectDependencies(HttpServletRequest request, HttpServletResponse response, String actionName, ActionResult defaultActionResult, Controller controller) throws IllegalAccessException, NoSuchFieldException, FileUploadException, IOException {
        ReflectionUtil.setField(Controller.REQUEST_FIELD, controller, request);
        ReflectionUtil.setField(Controller.RESPONSE_FIELD, controller, response);
        ReflectionUtil.setField(Controller.SERVLET_CONTEXT_FIELD, controller, getServletContext());
        ReflectionUtil.setField(Controller.ACTION_INVOKED_FIELD, controller, actionName);
        ReflectionUtil.setField(Controller.ACTION_RESULT_FIELD, controller, defaultActionResult);
        if (ServletFileUpload.isMultipartContent(request)) {
            MultiPartParser parser = new MultiPartParser(request, controller);
            ReflectionUtil.setField(Controller.PARAMETERS_FIELD, controller, parser.getParameters());
            ReflectionUtil.setField(Controller.FILE_ITEMS_FIELD, controller, parser.getFileItems());
        }
        repositoryManager.injectRepositories(controller);
        if (springManager != null) springManager.injectSpringBeans(controller);
    }

    private Method determineActionMethod(String[] pathComponents, Class<?> controllerClass) {
        try {
            String actionMethodName = getActionName(pathComponents);
            return controllerClass.getMethod(actionMethodName);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private String getControllerName(String[] pathComponents) {
        String controllerName = null;
        if (pathComponents.length >= 2) {
            controllerName = pathComponents[1];
        }
        if (controllerName == null) {
            controllerName = rootControllerName;
        }
        return controllerName;
    }

    private String getActionName(String[] pathComponents) {
        String actionName = null;
        if (pathComponents.length >= 3) {
            actionName = pathComponents[2];
        }
        if (actionName == null || actionName.length() == 0) {
            actionName = DEFAULT_ACTION;
        }
        return actionName;
    }

    private String getIdParam(String[] pathComponents) {
        String idParam = null;
        if (pathComponents.length >= 4) {
            idParam = pathComponents[3];
        }
        return idParam;
    }

    private String qualifyControllerName(String controllerName) {
        return controllersPackage + "." + controllerName + "Controller";
    }

    private void addELResolver(ServletContext servletContext) {
        JspApplicationContext jspContext = JspFactory.getDefaultFactory().getJspApplicationContext(servletContext);
        jspContext.addELResolver(new ModelELResolver());
    }

    private void prepareFlashMap(HttpServletRequest request) {
        HttpSession session = request.getSession();
        FlashMap currentFlashMap = (FlashMap) session.getAttribute(FLASH_MAP_ATTRIBUTE);
        if (currentFlashMap == null) session.setAttribute(FLASH_MAP_ATTRIBUTE, new FlashMap()); else currentFlashMap.rotateFlashes();
    }

    @SuppressWarnings("unchecked")
    private void cleanup(Controller controller) throws SecurityException, NoSuchFieldException, IllegalAccessException {
        Map<String, FileItem[]> fileItemsMap = (Map<String, FileItem[]>) ReflectionUtil.getField(Controller.FILE_ITEMS_FIELD, controller);
        if (fileItemsMap != null) for (FileItem[] fileItems : fileItemsMap.values()) for (FileItem fileItem : fileItems) fileItem.delete();
    }
}
