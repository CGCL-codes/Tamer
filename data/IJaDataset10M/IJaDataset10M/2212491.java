package whf.framework.validate;

import java.util.Map;
import whf.framework.i18n.ApplicationResource;
import whf.framework.util.Utils;

/**
 * @author wanghaifeng
 *
 */
public class ValidateErrors {

    private Map<String, String> errors = Utils.newHashMap();

    public boolean isEmpty() {
        return this.errors.isEmpty();
    }

    public void add(String propertyName, String msgKey) {
        errors.put(propertyName, ApplicationResource.get(msgKey));
    }

    public void add(String propertyName, String msgKey, Object[] params) {
        errors.put(propertyName, ApplicationResource.get(msgKey, params));
    }

    public void put(String propertyName, String msg) {
        errors.put(propertyName, msg);
    }

    public String get(String propertyName) {
        String msg = errors.get(propertyName);
        if (msg == null) msg = "";
        return msg;
    }

    public void merge(ValidateErrors validateErrors) {
        if (validateErrors == null) return;
        Map<String, String> errors = validateErrors.errors;
        this.errors.putAll(errors);
    }

    public Map<String, String> getErrors() {
        return this.errors;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String propName : this.errors.keySet()) {
            sb.append(propName).append(" : ").append(this.errors.get(propName)).append("\t");
        }
        return sb.toString();
    }
}
