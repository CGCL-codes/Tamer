package halo.web.action.actionmapping;

import java.lang.reflect.Method;

/**
 * Action类的匹配对象
 * 
 * @author akwei
 */
public class ActionMapping {

    /**
	 * Action中要运行的方法名称
	 */
    private String methodName;

    /**
	 * Action对象
	 */
    private Object action;

    /**
	 * Action匹配名称
	 */
    private String actionName;

    /**
	 * Action中要运行的方法对象
	 */
    private Method actionMethod;

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setAction(Object action) {
        this.action = action;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public void setActionMethod(Method actionMethod) {
        this.actionMethod = actionMethod;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

    public String getActionName() {
        return actionName;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object getAction() {
        return action;
    }
}
