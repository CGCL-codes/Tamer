package purej.config.handler;

import purej.exception.NestedRuntimeException;

/**
 * LSF ���� ����
 * 
 * @author Administrator
 * 
 */
public class ConfigurationException extends NestedRuntimeException {

    private static final long serialVersionUID = 2917050304913971181L;

    public ConfigurationException(String msg) {
        super(msg);
    }

    public ConfigurationException(String msg, Throwable ex) {
        super(msg, ex);
    }
}
