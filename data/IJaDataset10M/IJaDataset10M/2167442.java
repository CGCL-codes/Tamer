package purej.web.servlet;

import java.util.Map;
import purej.web.domain.ControllerMapping;
import purej.web.domain.ExceptionMap;
import purej.web.domain.SecurityMap;

/**
 * Ŀ�ǵ� �� �ڵ� ��
 * 
 * @author Administrator
 * 
 */
class ControllerHandle {

    private Class<?> controllerClass;

    private String processName;

    private Map<String, ExceptionMap> exceptionMap;

    private Map<String, SecurityMap> securityMap;

    public ControllerHandle(ControllerMapping controllerMapping) {
        this.controllerClass = controllerMapping.getControllerClass();
        this.processName = controllerMapping.getProcessMethod();
        this.exceptionMap = controllerMapping.getExceptionMap();
        this.securityMap = controllerMapping.getSecurityMap();
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public Map<String, ExceptionMap> getExceptionMap() {
        return exceptionMap;
    }

    public void setExceptionMap(Map<String, ExceptionMap> exceptionMap) {
        this.exceptionMap = exceptionMap;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public Map<String, SecurityMap> getSecurityMap() {
        return securityMap;
    }

    public void setSecurityMap(Map<String, SecurityMap> securityMap) {
        this.securityMap = securityMap;
    }
}
