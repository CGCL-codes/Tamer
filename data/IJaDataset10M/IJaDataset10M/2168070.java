package net.joindesk.let;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import net.joindesk.api.WebApp;
import net.joindesk.api.exception.DeskletNotFoundException;
import net.joindesk.api.exception.SysException;
import net.joindesk.app.DeskletLoader;
import net.joindesk.let.config.DeskletConfig;
import net.joindesk.request.WebRequest;
import net.joindesk.response.WebResponse;
import net.joindesk.response.error.ErrorResponse;

public class DeskletFactoryImpl implements DeskletFactory {

    private static final String DEFAULT_REQUEST_TYPE = "default";

    /**
	 * Map<String, DeskletConfig>
	 */
    private Map pathConfigMap;

    /**
	 * Map<String, DeskletConfig>
	 */
    private Map nameConfigMap;

    /**
	 * Map<String, Class>
	 */
    private Map requestClassMap;

    /**
	 * Map<String, Class>
	 */
    private Map errorClassMap;

    /**
	 * Map<String, Class>
	 */
    private Map responseClassMap;

    private DeskletLoader deskletLoader;

    public DeskletProxy createDeskletProxyByPath(String deskletPath) throws DeskletNotFoundException {
        if (pathConfigMap == null) throw new SysException("Factory not init.");
        DeskletConfig config = (DeskletConfig) pathConfigMap.get(deskletPath);
        if (config == null) throw new DeskletNotFoundException(deskletPath);
        DeskletProxy proxy = new DeskletProxy(config);
        return proxy;
    }

    public WebRequest createRequest(String requestType) {
        if (requestClassMap == null) throw new SysException("Must set requestClasses property in DeskletFactory. ");
        try {
            if (requestType == null) requestType = DEFAULT_REQUEST_TYPE;
            Class theClass = (Class) requestClassMap.get(requestType);
            if (theClass == null) throw new SysException("Request class for type " + requestType + " not found.");
            WebRequest webRequest = (WebRequest) theClass.newInstance();
            return webRequest;
        } catch (Exception e) {
            throw new SysException("Create request error. " + e.getMessage(), e);
        }
    }

    public ErrorResponse createErrorResponse(String requestType) {
        if (errorClassMap == null) throw new SysException("Must set errorClasses property in DeskletFactory. ");
        try {
            if (requestType == null) requestType = DEFAULT_REQUEST_TYPE;
            Class theClass = (Class) errorClassMap.get(requestType);
            if (theClass == null) throw new SysException("Error response class for type " + requestType + " not found.");
            ErrorResponse error = (ErrorResponse) theClass.newInstance();
            return error;
        } catch (Exception e) {
            throw new SysException("Create Error response error. " + e.getMessage(), e);
        }
    }

    public WebResponse createResponse(String responseType) {
        if (responseClassMap == null) throw new SysException("Must set responseClasses property in DeskletFactory. ");
        Class responseClass = (Class) responseClassMap.get(responseType);
        if (responseClass == null) throw new SysException("Not found reponse class for type " + responseType + ".");
        WebResponse response = null;
        try {
            response = (WebResponse) responseClass.newInstance();
        } catch (Exception e) {
            throw new SysException("Create response error. " + e.getMessage(), e);
        }
        return response;
    }

    public void init(WebApp app) {
        pathConfigMap = deskletLoader.load(app);
        if (pathConfigMap != null) {
            nameConfigMap = new HashMap();
            Iterator iter = pathConfigMap.entrySet().iterator();
            while (iter.hasNext()) {
                java.util.Map.Entry entry = (java.util.Map.Entry) iter.next();
                DeskletConfig config = (DeskletConfig) entry.getValue();
                nameConfigMap.put(config.getName(), config);
            }
        }
    }

    public void setRequestClasses(Properties requestClasses) {
        requestClassMap = getClassesMap(requestClasses, "Request");
    }

    public void setErrorClasses(Properties requestClasses) {
        errorClassMap = getClassesMap(requestClasses, "Error");
    }

    public void setResponseClasses(Properties responseClasses) {
        responseClassMap = getClassesMap(responseClasses, "Response");
    }

    private Map getClassesMap(Properties classes, String error) {
        if (classes == null) return null;
        Map map = new HashMap();
        Iterator iter = classes.entrySet().iterator();
        while (iter.hasNext()) {
            Entry entry = (Entry) iter.next();
            try {
                map.put((String) entry.getKey(), Class.forName((String) entry.getValue()));
            } catch (ClassNotFoundException e) {
                throw new SysException(error + " [" + entry.getKey() + "] class " + entry.getValue() + " not found.", e);
            }
        }
        return map;
    }

    public void setDeskletLoader(DeskletLoader deskletLoader) {
        this.deskletLoader = deskletLoader;
    }

    public List getDeskletConfigs() {
        List list = new ArrayList();
        if (pathConfigMap == null) return list;
        list.addAll(pathConfigMap.values());
        return list;
    }
}
