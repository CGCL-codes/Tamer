package cn.myapps.core.resource.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import cn.myapps.core.resource.ejb.ResourceProcess;
import cn.myapps.core.resource.ejb.ResourceVO;
import cn.myapps.util.ProcessFactory;
import cn.myapps.util.web.DWRHtmlUtils;
import com.opensymphony.xwork.config.Configuration;
import com.opensymphony.xwork.config.ConfigurationManager;
import com.opensymphony.xwork.config.entities.ActionConfig;
import com.opensymphony.xwork.config.entities.PackageConfig;

public class WebWorkConfigUtil {

    /**
	 * Get the web work action config list.
	 * @return The web work action config list.
	 */
    public Collection getActionConfig() {
        ArrayList actionClasses = new ArrayList();
        Configuration conf = ConfigurationManager.getConfiguration();
        Set names = conf.getPackageConfigNames();
        Iterator iter = names.iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            PackageConfig pc = conf.getPackageConfig(name);
            Map actions = pc.getAllActionConfigs();
            Iterator iter2 = actions.values().iterator();
            while (iter2.hasNext()) {
                ActionConfig actionConfig = (ActionConfig) iter2.next();
                actionClasses.add(actionConfig);
            }
        }
        return actionClasses;
    }

    /**
	 * Get the web work action class list.
	 * @return The web work action class list.
	 */
    public Map getActionClasses() {
        Collection actionConfigs = getActionConfig();
        LinkedHashMap actionClasses = new LinkedHashMap();
        actionClasses.put("none", "Select");
        Iterator iter = actionConfigs.iterator();
        while (iter.hasNext()) {
            ActionConfig actionConfig = (ActionConfig) iter.next();
            actionClasses.put(actionConfig.getClassName(), actionConfig.getClassName());
        }
        return actionClasses;
    }

    public String createActionClassesOptionFunc(String selectFieldName, String def) {
        Map map = getActionClasses();
        return DWRHtmlUtils.createOptions(map, selectFieldName, def);
    }

    public String createActionMethodsOptionFunc(String selectFieldName, String actionClass, String def) {
        Map map = getActionMethods(actionClass);
        return DWRHtmlUtils.createOptions(map, selectFieldName, def);
    }

    public String createActionUrlsOptionFunc(String selectFieldName, String actionClass, String actionMethod, String def) {
        Map map = getActionUrls(actionClass, actionMethod);
        return DWRHtmlUtils.createOptions(map, selectFieldName, def);
    }

    /**
	 * Get the web work action method list.
	 * @param className The action class name.
	 * @return the web work action method list.
	 */
    public Map getActionMethods(String className) {
        Map actionMethods = new LinkedHashMap();
        actionMethods.put("none", "Select");
        if (className == null) {
            return actionMethods;
        }
        Collection actionConfigs = getActionConfig();
        Iterator iter = actionConfigs.iterator();
        while (iter.hasNext()) {
            ActionConfig actionConfig = (ActionConfig) iter.next();
            if (actionConfig.getClassName().equals(className)) {
                actionMethods.put(actionConfig.getMethodName(), actionConfig.getMethodName());
            }
        }
        return actionMethods;
    }

    /**
	 * the web work forward url list according the class and the method.
	 * @param className The action class name.
	 * @param methodName The action method name.
	 * @return The forward url list according the class and the method.
	 */
    public static Map getActionUrls(String className, String methodName) {
        Map urls = new LinkedHashMap();
        urls.put("none", "Select");
        if (className == null || methodName == null) {
            return urls;
        }
        Configuration conf = ConfigurationManager.getConfiguration();
        Set names = conf.getPackageConfigNames();
        Iterator iter = names.iterator();
        while (iter.hasNext()) {
            String name = (String) iter.next();
            PackageConfig pc = conf.getPackageConfig(name);
            String namespace = pc.getNamespace();
            Map actions = pc.getAllActionConfigs();
            Iterator iter2 = actions.keySet().iterator();
            while (iter2.hasNext()) {
                String actionName = (String) iter2.next();
                ActionConfig actionConfig = (ActionConfig) actions.get(actionName);
                if (actionConfig.getClassName().equals(className) && actionConfig.getMethodName().equals(methodName)) {
                    String tmp = namespace + "/" + actionName + ".action";
                    urls.put(tmp, tmp);
                }
            }
        }
        return urls;
    }

    /**
	 * Get the menu tree.
	 * @return The menu tree.
	 */
    public Map getMenuTree(String application) {
        Map map = new TreeMap();
        map.put("", "Select");
        try {
            ResourceProcess process = (ResourceProcess) ProcessFactory.createProcess(ResourceProcess.class);
            Collection rc = process.doSimpleQuery(null, application);
            ResourceVO node = null;
            Iterator iter = rc.iterator();
            while (iter.hasNext()) {
                node = (ResourceVO) iter.next();
                if (node.getSuperior() == null) {
                    map.putAll(getSubTree(rc, node, 0));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
	 * Get the menu tree.
	 * @param startNodeId The parent menu tree.
	 * @return The menu tree.
	 */
    public Map getMenuTree(String startNodeId, String application) {
        Map map = null;
        try {
            ResourceProcess process = (ResourceProcess) ProcessFactory.createProcess(ResourceProcess.class);
            Collection rc = process.doSimpleQuery(null, application);
            ResourceVO node = null;
            Iterator iter = rc.iterator();
            while (iter.hasNext()) {
                node = (ResourceVO) iter.next();
                if (node.getId() == null || node.getId().trim().equals(startNodeId)) break;
            }
            if (node != null) {
                map = new TreeMap();
                map.put("", "Select");
                map.putAll(getSubTree(rc, node, 0));
            } else {
                map = new TreeMap();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
	 * Get the menu tree expect the current menu.
	 * @param startNodeId The parent menu.
	 * @param currResourceId The current menu.
	 * @return  The menu tree expect the parent menu.
	 */
    public Map getMenuTreeExpCurrResource(String startNodeId, String currResourceId, String application) {
        Map tree = getMenuTree(startNodeId, application);
        tree.remove(currResourceId);
        return tree;
    }

    /**
	 * Get the menu tree expect the parent menu.
	 * @param currResourceId  The current menu.
	 * @return The menu tree expect the parent menu.
	 */
    public Map getMenuTreeExpCurrResource(String currResourceId, String application) {
        Map tree = getMenuTree(application);
        if (currResourceId != null) tree.remove(currResourceId);
        return tree;
    }

    /**
	 * Get the sub menu tree tree.
	 * @param rc The resouce collection
	 * @param node The parent node.
	 * @param deep The tree height
	 * @return The sub menu tree.
	 */
    private Map getSubTree(Collection rc, ResourceVO node, int deep) {
        String prefix = "|---------------------------------";
        TreeMap rtn = new TreeMap();
        rtn.put(node.getId(), prefix.substring(0, deep * 2) + node.getDescription());
        Iterator iter = rc.iterator();
        while (iter.hasNext()) {
            ResourceVO sub = (ResourceVO) iter.next();
            if (node.getId().equals(sub.getSuperior().getId()) && !node.getId().equals(sub.getId())) {
                Map subTree = getSubTree(rc, sub, deep + 1);
                rtn.putAll(subTree);
            }
        }
        return rtn;
    }

    public static void main(String[] args) {
        WebWorkConfigUtil test = new WebWorkConfigUtil();
        ResourceVO root;
        ArrayList list = new ArrayList();
        {
            root = new ResourceVO();
            root.setId("1");
            root.setDescription("root");
            list.add(root);
        }
        Map tree = test.getSubTree(list, root, 0);
        Iterator iter = tree.keySet().iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            System.out.println(tree.get(key));
        }
    }
}
