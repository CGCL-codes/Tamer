package strutter.config;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.ServletException;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ControllerConfig;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import strutter.config.tags.ConfigAuthorityInterface;
import strutter.config.tags.ConfigAutorunInterface;
import strutter.config.tags.ConfigInterface;
import strutter.config.tags.ConfigRemotingInterface;
import strutter.config.tags.ConfigWSInterface;
import strutter.config.tags.ConfigZeroInterface;
import strutter.controller.RequestProcessorProxy;
import strutter.helper.ActionHelper;
import strutter.helper.SqlHolder;

public class ActionPlugin implements PlugIn {

    ActionServlet servlet;

    ModuleConfig module;

    private String packageroot = "";

    private String aliasaction = "Action";

    private String aliasform = "ActionForm";

    private String pathformat = "{PATH}";

    private String pathlower = "1";

    private String parameter = "action";

    private String scope = ActionConfig.SCOPE_SESSION;

    private String packageby = ActionConfig.PACKAGEBY_LAYER;

    private String aliasview = "view,list,update,create,success";

    private String views = "/views";

    private String encoding = "UTF-8";

    private String viewer = "1";

    private String keepalive = "1";

    private String script = "1";

    private String template = "1";

    private String doctype = "1";

    private String cookiecheck = "1";

    private String sessioncheck = "1";

    private String compression = "1";

    private String nocache = "1";

    private static ArrayList actionclasses = new ArrayList();

    private static ArrayList dwrpojos = new ArrayList();

    /**
	 *  Initialisieren
	 */
    public void init(ActionServlet servlet, ModuleConfig module) throws ServletException {
        this.servlet = servlet;
        this.module = module;
        System.out.println("### Strutter start ###");
        ControllerConfig controller = module.getControllerConfig();
        RequestProcessorProxy.setProxyname(controller.getProcessorClass());
        RequestProcessorProxy.setPlugin(this);
        controller.setProcessorClass(RequestProcessorProxy.class.getName());
        try {
            String[] packages = packageroot.split(",");
            ArrayList list = new ArrayList();
            for (int i = 0; i < packages.length; i++) {
                String packagepath = packages[i];
                packagepath = normalizePath(packagepath.replace('.', '/'));
                String path = servlet.getServletContext().getRealPath("/WEB-INF/classes");
                list.addAll(ActionPlugin.getClasses(path, packages[i]));
            }
            for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
                String cname = (String) iterator.next();
                try {
                    registAutorun(Class.forName(cname, false, this.getClass().getClassLoader()));
                } catch (Exception e) {
                    System.out.println("### Strutter Problem Autorun: " + e + "###");
                }
            }
            for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
                String cname = (String) iterator.next();
                try {
                    registAction(module, Class.forName(cname, false, this.getClass().getClassLoader()));
                } catch (Exception e) {
                    System.out.println("### Strutter Problem: " + e + "###");
                }
                try {
                    registAuthority(Class.forName(cname, false, this.getClass().getClassLoader()));
                } catch (Exception e) {
                    System.out.println("### Strutter Problem Authority: " + e + "###");
                }
            }
        } catch (Exception e) {
            System.out.println("Strutter Init Exception: " + e);
        }
        System.out.println("### Strutter initialized ###");
    }

    public void destroy() {
    }

    static ArrayList getClasses(String path) {
        return getClasses(path, "");
    }

    public static ArrayList getClasses(String path, String mainpackage) {
        ArrayList list = new ArrayList();
        listFolder(list, new File(path + "/" + mainpackage.replace('.', '/')), mainpackage);
        return list;
    }

    static void listFolder(ArrayList list, File f, String pack) {
        if (!f.isDirectory()) return;
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            File sub = files[i];
            String name = pack + (pack.equals("") ? "" : ".") + sub.getName();
            if (sub.isDirectory()) {
                listFolder(list, sub, name);
            } else {
                if (name.endsWith(".class")) {
                    String cname = name.substring(0, name.length() - ".class".length());
                    list.add(cname);
                }
            }
        }
    }

    boolean registAutorun(Class actionClass) throws Exception {
        if (actionClass.isInterface()) return false;
        if (!isInterfaceImplementer(actionClass, ConfigAutorunInterface.class)) return false;
        ConfigAutorunInterface autorun = (ConfigAutorunInterface) actionClass.newInstance();
        autorun.init(servlet, module);
        return true;
    }

    boolean registAuthority(Class actionClass) throws Exception {
        if (actionClass.isInterface()) return false;
        if (!isInterfaceImplementer(actionClass, ConfigAuthorityInterface.class)) return false;
        ConfigAuthorityInterface authority = (ConfigAuthorityInterface) actionClass.newInstance();
        ActionHelper.setAuthority(authority);
        return true;
    }

    /**
	 *  Aktion registrieren
	 */
    boolean registAction(ModuleConfig moduleConfig, Class actionClass) throws Exception {
        if (actionClass.isInterface()) return false;
        if ((actionClass.getModifiers() & Modifier.ABSTRACT) != 0) return false;
        if (!isSubclass(actionClass, Action.class) && !isInterfaceImplementer(actionClass, ConfigZeroInterface.class)) return false;
        ActionConfig conf = null;
        try {
            if (isInterfaceImplementer(actionClass, ConfigZeroInterface.class)) {
                conf = new ActionConfig();
                if (isInterfaceImplementer(actionClass, ConfigInterface.class)) {
                    try {
                        ConfigInterface ci = (ConfigInterface) actionClass.newInstance();
                        ci.config(conf);
                    } catch (Exception e) {
                        System.out.println("#INSTANCE PROBLEM: " + e);
                    }
                }
            } else {
                conf = (ActionConfig) actionClass.getField("struts").get(actionClass);
            }
            SqlHolder.addSqlHolder(actionClass);
        } catch (Exception e) {
            return false;
        }
        ActionConfig action = (ActionConfig) conf;
        ActionMappingExtended actionmapping = new ActionMappingExtended();
        dwrpojos.addAll(action.getRemoting());
        String fqn = actionClass.getName();
        if (isInterfaceImplementer(actionClass, ConfigRemotingInterface.class)) {
            actionclasses.add(fqn);
            actionmapping.setRemoteaction(true);
        }
        actionmapping.setWsaction(isInterfaceImplementer(actionClass, ConfigWSInterface.class));
        actionmapping.setType(fqn);
        actionmapping.setUnknown(action.isUnknown());
        if (action.getParameter() != null) actionmapping.setParameter(action.getParameter()); else if (getParameter() != null) actionmapping.setParameter(getParameter()); else actionmapping.setParameter("action");
        if (action.getScope() != null) actionmapping.setScope(action.getScope()); else if (getParameter() != null) actionmapping.setScope(getScope()); else actionmapping.setScope("session");
        String action_views = views;
        if ((action.getPackageby() != null && action.getPackageby().equals(ActionConfig.PACKAGEBY_FEATURE)) || getPackageby().equals(ActionConfig.PACKAGEBY_FEATURE)) action_views = "/WEB-INF/classes/" + getPackageName(fqn).replace('.', '/');
        actionmapping.setInput(action_views + action.getInput());
        String path = action.getPath();
        if (isEmpty(path)) path = getPathFromClassName(fqn, getAliasaction());
        actionmapping.setPath(path);
        Class form = action.getForm();
        String formName = null;
        if (form == null) {
            String formClass = getFormFromAction(fqn);
            try {
                form = Class.forName(formClass, false, this.getClass().getClassLoader());
                formName = registForm(moduleConfig, form);
            } catch (ClassNotFoundException e) {
            }
        } else {
            formName = registForm(moduleConfig, form);
        }
        if (!isEmpty(formName)) {
            actionmapping.setName(formName);
            actionmapping.setValidate(action.isValidate());
        } else {
            actionmapping.setAttribute(getClassName(actionClass.getName()));
        }
        if (!isEmpty(action.getRoles())) actionmapping.setRoles(action.getRoles());
        for (String key : action.getProperties().keySet()) {
            actionmapping.setProperty(key, action.getProperties().get(key));
        }
        moduleConfig.addActionConfig(actionmapping);
        System.out.println(fqn + " -> " + path + (actionmapping.getName() == null ? " [NO FORM]" : ""));
        Forward[] forwards = action.getForwards();
        for (int i = 0; i < forwards.length; i++) {
            Forward forward = forwards[i];
            ActionForward aforward = new ActionForward();
            aforward.setName(forward.getName());
            if (forward.getName().startsWith("#")) {
                String targetclass = forward.getPath();
                if (targetclass == null) targetclass = actionmapping.getPath(); else targetclass = getPathFromClassName(targetclass, getAliasaction());
                String fpath = targetclass + ".do?" + actionmapping.getParameter() + "=" + forward.getName().substring(1);
                aforward.setPath(fpath);
            } else {
                if (forward.getPath().indexOf(".do") > 0 || forward.getPath().startsWith("~")) aforward.setPath(forward.getPath().replaceAll("~", "")); else aforward.setPath(action_views + forward.getPath());
            }
            aforward.setRedirect(forward.isRedirect());
            if (forward.isGlobal()) moduleConfig.addForwardConfig(aforward); else actionmapping.addForwardConfig(aforward);
            printForward(aforward);
        }
        if (forwards.length == 0) {
            findForwards(action_views, path, actionmapping);
            String packageviews = "/WEB-INF/classes/" + getPackageName(fqn).replace('.', '/');
            if (!action_views.equals(packageviews)) findForwards(packageviews, path, actionmapping);
        }
        actionmapping.addInterceptors(action.getInterceptors());
        actionmapping.setUnspecified(action.getUnspecified());
        return true;
    }

    private void findForwards(String viewsfolder, String actionpath, ActionMappingExtended actionmapping) {
        String[] defaultpages = aliasview.split(",");
        for (int i = 0; i < defaultpages.length; i++) {
            String file = viewsfolder + actionpath + "_" + defaultpages[i] + ".jsp";
            if (!new File(servlet.getServletContext().getRealPath(file)).exists()) continue;
            ActionForward aforward = new ActionForward();
            aforward.setName(defaultpages[i]);
            aforward.setPath(file);
            aforward.setRedirect(false);
            actionmapping.addForwardConfig(aforward);
            printForward(aforward);
        }
    }

    private void printForward(ActionForward aforward) {
        System.out.println("  " + aforward.getPath() + " [" + aforward.getName() + "]");
    }

    /**
	 *  Form registieren
	 */
    String registForm(ModuleConfig moduleConfig, Class formClass) {
        if (formClass.isInterface()) return null;
        if ((formClass.getModifiers() & Modifier.ABSTRACT) != 0) return null;
        FormBeanConfig formBean = new FormBeanConfig();
        String fqn = formClass.getName();
        String formName = getClassName(fqn);
        formBean.setName(formName);
        formBean.setType(fqn);
        moduleConfig.addFormBeanConfig(formBean);
        return formName;
    }

    final String cutAlias(String classname, String patternstr) {
        int pos = classname.indexOf(patternstr);
        if (pos > -1) return classname.substring(0, classname.indexOf(patternstr)); else return classname;
    }

    /**
	 *  Path aus Klassennamen ermitteln
	 */
    String getPathFromClassName(String fqn, String alias) {
        String className = getClassName(fqn);
        className = cutAlias(className, alias);
        if ("1".equals(this.getPathlower())) className = className.toLowerCase();
        className = getPathformat().replaceAll("\\{PATH\\}", className);
        String path = normalizePath(className);
        return path;
    }

    /**
	 * Form-Klassenname aus Action-Klassennamen ermitteln
	 */
    private String getFormFromAction(String actionClass) {
        String className = getClassName(actionClass);
        String packageName = getPackageName(actionClass);
        String formclass = className.substring(0, className.length() - getAliasaction().length());
        formclass = packageName + "." + formclass + getAliasform();
        try {
            Class.forName(formclass, false, this.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            formclass = packageName + "." + findPattern2(className, getAliasaction()) + getAliasform();
        }
        return formclass;
    }

    public String findPattern2(String action, String ActionAlias) {
        String pat = "[A-Z][a-z0-9]*" + ActionAlias + "$";
        return action.replaceFirst(pat, "");
    }

    /**
	 * Subclass checker
	 */
    boolean isSubclass(Class target, Class superclass) {
        for (Class s = target; s != Object.class; s = s.getSuperclass()) {
            if (s == superclass) return true;
        }
        return false;
    }

    boolean isInterfaceImplementer(Class target, Class superclass) throws Exception {
        for (Class s = target; s != Object.class; s = s.getSuperclass()) {
            Class[] interfaces = s.getInterfaces();
            if (inInterfaceList(interfaces, superclass)) return true;
        }
        return false;
    }

    boolean inInterfaceList(Class[] interfaces, Class superclass) throws Exception {
        for (int i = 0; i < interfaces.length; i++) {
            if (interfaces[i] == superclass) return true;
            if (inInterfaceList(interfaces[i].getInterfaces(), superclass)) return true;
        }
        return false;
    }

    /**
	 * FQN Klassennamen ermitteln
	 */
    public static String getClassName(String fqn) {
        String className = fqn;
        if (className.indexOf('.') > 0) {
            className = className.substring(className.lastIndexOf('.') + 1);
        }
        return className;
    }

    /**
	 * FQN Package ermitteln
	 */
    private String getPackageName(String fqn) {
        if (fqn.indexOf('.') < 0) return "";
        return fqn.substring(0, fqn.lastIndexOf('.'));
    }

    private boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    private String normalizePath(String path) {
        String ret = path;
        if (ret == null) return "";
        if (!ret.startsWith("/")) ret = "/" + ret;
        if (ret.endsWith("/")) ret = ret.substring(0, ret.length() - 1);
        if (ret.equals("/")) ret = "";
        return ret;
    }

    public void setPackageroot(String packageroot) {
        this.packageroot = packageroot;
    }

    public String getAliasaction() {
        return aliasaction;
    }

    public void setAliasaction(String action) {
        aliasaction = action;
    }

    public String getAliasform() {
        return aliasform;
    }

    public void setAliasform(String actionform) {
        aliasform = actionform;
    }

    public String getPathformat() {
        return pathformat;
    }

    public void setPathformat(String pathformat) {
        this.pathformat = pathformat;
    }

    public String getPathlower() {
        return pathlower;
    }

    public void setPathlower(String pathlower) {
        this.pathlower = pathlower;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getAliasview() {
        return aliasview;
    }

    public void setAliasview(String defaultforwards) {
        this.aliasview = defaultforwards;
    }

    public String getViews() {
        return views;
    }

    public void setViews(String views) {
        if (!views.startsWith("/")) this.views = "/" + views; else this.views = views;
        if (views.endsWith("/")) views = views.substring(0, views.length() - 1);
        this.views = views;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getViewer() {
        return viewer;
    }

    public void setViewer(String viewer) {
        this.viewer = viewer;
    }

    public String getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(String keepalive) {
        this.keepalive = keepalive;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getCookiecheck() {
        return cookiecheck;
    }

    public void setCookiecheck(String cookiecheck) {
        this.cookiecheck = cookiecheck;
    }

    public String getSessioncheck() {
        return sessioncheck;
    }

    public void setSessioncheck(String sessioncheck) {
        this.sessioncheck = sessioncheck;
    }

    public String getDoctype() {
        return doctype;
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }

    public String getCompression() {
        return compression;
    }

    public void setCompression(String compression) {
        this.compression = compression;
    }

    public String getNocache() {
        return nocache;
    }

    public void setNocache(String nocache) {
        this.nocache = nocache;
    }

    public String getPackageby() {
        return packageby;
    }

    public void setPackageby(String packageby) {
        this.packageby = packageby;
    }

    public static ArrayList getActionclasses() {
        return actionclasses;
    }

    public static ArrayList getDWRPOJOs() {
        return dwrpojos;
    }
}
