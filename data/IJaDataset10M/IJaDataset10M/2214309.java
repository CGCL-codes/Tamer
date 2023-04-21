package net.sf.log2web.web.common.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author Guillermo Manzato (manzato@gmail.com)
 * @date Jan 22, 2008
 */
public abstract class BaseJsonAction extends BaseTextAction {

    private final Object jsonConfigMutex = new Object();

    private JsonConfig jsonConfig;

    public final String executeTextAction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        final Object result = executeJsonAction(mapping, actionForm, request, response);
        final JsonConfig jsonConfig = getOrCreateJSonConfig(result);
        final JSON jsonResult = JSONSerializer.toJSON(result, jsonConfig);
        return jsonResult.toString(0);
    }

    /**
	 * @param result
	 * @return
	 */
    private JsonConfig getOrCreateJSonConfig(Object result) {
        synchronized (jsonConfigMutex) {
            if (jsonConfig != null) {
                return jsonConfig;
            }
            return buildDefaultConfig(result);
        }
    }

    public abstract Object executeJsonAction(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response);

    protected String[] getExcludeProperties() {
        return new String[] {};
    }

    protected final JsonConfig getJSonConfig() {
        synchronized (jsonConfigMutex) {
            return jsonConfig;
        }
    }

    protected final void setJSonConfig(JsonConfig config) {
        synchronized (jsonConfigMutex) {
            this.jsonConfig = config;
        }
    }

    protected JsonConfig buildDefaultConfig(Object obj) {
        JsonConfig config = new JsonConfig();
        config.setRootClass(obj.getClass());
        return config;
    }
}
