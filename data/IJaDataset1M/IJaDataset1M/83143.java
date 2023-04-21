package net.sourceforge.jwakeup;

import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

/**
 * Model object for carrying data between Controller and View.
 *
 * @author Matthias Heidenreich - 05.10.2007
 */
public class ControllerResult {

    public static final Logger LOGGER = Logger.getLogger(ControllerResult.class);

    private Map<String, Object> _attributes = new HashMap<String, Object>();

    private Map<String, String> _errors = new HashMap<String, String>();

    private String _successMessage;

    private boolean _handled;

    public ControllerResult(boolean requestHandled) {
        _handled = requestHandled;
    }

    public boolean isHandled() {
        return _handled;
    }

    public Map getAttributes() {
        return Collections.unmodifiableMap(_attributes);
    }

    public Object getAttribute(String name) {
        return _attributes.get(name);
    }

    public void setAttribute(String name, Object value) {
        _attributes.put(name, value);
    }

    public Map<String, String> getErrors() {
        return Collections.unmodifiableMap(_errors);
    }

    public void addErrors(Map<String, String> errors) {
        for (Object o : errors.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            _errors.put((String) entry.getKey(), (String) entry.getValue());
        }
    }

    public void addError(String key, String value) {
        _errors.put(key, value);
    }

    public String getSuccessMessage() {
        return _successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        _successMessage = successMessage;
    }

    public boolean hasErrors() {
        return !_errors.isEmpty();
    }
}
