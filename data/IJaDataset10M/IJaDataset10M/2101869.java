package halo.web.action;

import java.lang.reflect.Method;

/**
 * Action类的匹配对象
 * 
 * @author akwei
 */
public class ActionMapping {

    private String mappingUri;

    /**
	 * Action中要运行的方法名称
	 */
    private String methodName;

    /**
	 * Action对象
	 */
    private Action action;

    /**
	 * Action匹配名称
	 */
    private String actionName;

    /**
	 * Action中要运行的方法对象
	 */
    private Method actionMethod;

    /**
	 * 通过asm创建的对象
	 */
    private Action asmAction;

    public void setActionMethod(Method actionMethod) {
        this.actionMethod = actionMethod;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public boolean hasMethod() {
        if (this.methodName == null) {
            return false;
        }
        return true;
    }

    public void setAsmAction(Action asmAction) {
        this.asmAction = asmAction;
    }

    public Action getAsmAction() {
        return asmAction;
    }

    public String getMappingUri() {
        return mappingUri;
    }

    public void setMappingUri(String mappingUri) {
        this.mappingUri = mappingUri;
    }
}
