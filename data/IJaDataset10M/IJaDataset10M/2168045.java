package purej.service.builder;

import purej.exception.NestedRuntimeException;

/**
 * ���� ���� ����
 * 
 * @author Administrator
 * 
 */
public class ServiceBuildException extends NestedRuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -415937314715892436L;

    public ServiceBuildException(String msg) {
        super(msg);
    }

    public ServiceBuildException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
