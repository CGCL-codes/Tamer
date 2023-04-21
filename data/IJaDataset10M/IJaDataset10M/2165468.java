package org.javason.jsonrpc.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.javason.util.ClassUtils;

public class JsonRpcService {

    private Object _serviceBean = null;

    private Class _serviceClass = null;

    private Map<String, List<JsonRpcOperation>> _operations = new HashMap<String, List<JsonRpcOperation>>();

    private static final Logger LOG = Logger.getLogger(JsonRpcService.class);

    public static final String VERSION = "$Rev: 41030 $";

    public JsonRpcService(Object serviceBean, Map<String, List<JsonRpcOperation>> operations) {
        _serviceBean = serviceBean;
        _operations = operations;
    }

    public JsonRpcService(Object serviceBean) {
        _serviceBean = serviceBean;
        initializeOperations();
    }

    public JsonRpcService(Object serviceBean, Class serviceClass) {
        _serviceBean = serviceBean;
        _serviceClass = serviceClass;
        initializeOperations();
    }

    /**
	 * @return Returns the operations.
	 */
    public Map<String, List<JsonRpcOperation>> getOperations() {
        return _operations;
    }

    /**
	 * @param operations The operations to set.
	 */
    public void setOperations(Map<String, List<JsonRpcOperation>> operations) {
        _operations = operations;
    }

    /**
	 * @return Returns the serviceBean.
	 */
    public Object getServiceBean() {
        return _serviceBean;
    }

    /**
	 * @param serviceBean The serviceBean to set.
	 */
    public void setServiceBean(Object serviceBean) {
        _serviceBean = serviceBean;
    }

    /**
	 * @return Returns the serviceClass.
	 */
    public Class getServiceClass() {
        if (_serviceClass == null) {
            _serviceClass = _serviceBean.getClass();
        }
        return _serviceClass;
    }

    private void initializeOperations() {
        Method[] methods = ClassUtils.getMethods(getServiceClass());
        for (Method method : methods) {
            JsonRpcOperation op = new JsonRpcOperation(method);
            List<JsonRpcOperation> JsonRpcOps = _operations.get(method.getName());
            if (JsonRpcOps == null) {
                JsonRpcOps = new ArrayList<JsonRpcOperation>();
            }
            JsonRpcOps.add(op);
            _operations.put(method.getName(), JsonRpcOps);
        }
    }
}
