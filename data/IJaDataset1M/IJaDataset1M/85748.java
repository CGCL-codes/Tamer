package fast4j.ejbbridge;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Map;

/**
 * Result after calling a service method.
 * @author alexandre.roman
 */
public class ServiceResult implements Externalizable {

    private static final long serialVersionUID = 1L;

    private Object value;

    private Map context;

    /**
     * This constructor is required for proper serialization. Don't use it!
     */
    public ServiceResult() {
    }

    /**
     * Construct a new instance.
     * @param value returned by service method
     * @param serviceContext attached context
     */
    public ServiceResult(final Object value, final Map context) {
        this.value = value;
        this.context = context == null ? new HashMap() : context;
    }

    public Object getValue() {
        return value;
    }

    public Map getContext() {
        return context;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((context == null) ? 0 : context.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final ServiceResult other = (ServiceResult) obj;
        if (context == null) {
            if (other.context != null) return false;
        } else if (!context.equals(other.context)) return false;
        if (value == null) {
            if (other.value != null) return false;
        } else if (!value.equals(other.value)) return false;
        return true;
    }

    public String toString() {
        return "ServiceResult[value=" + value + ", context=" + context + "]";
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        value = in.readObject();
        context = (Map) in.readObject();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(value);
        out.writeObject(context);
    }
}
