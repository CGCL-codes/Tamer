package org.jaffa.qm.apis.data;

import org.jaffa.soa.graph.ServiceError;

/**
 * Encapsulates the response to a Message administration operation.
 *
 * @author GautamJ
 */
public class MessageAdminResponse {

    private MessageGraph source;

    private ServiceError[] errors;

    public MessageAdminResponse() {
    }

    public MessageAdminResponse(MessageGraph source, ServiceError[] errors) {
        this.source = source;
        this.errors = errors;
    }

    /**
     * Getter for property source. This is the source GraphDataObject that the error occurred on.
     * @return Value of property source.
     */
    public MessageGraph getSource() {
        return source;
    }

    /**
     * Setter for property source.
     * @param source New value of property source.
     */
    public void setSource(MessageGraph source) {
        this.source = source;
    }

    /**
     * Getter for property errors.
     * @return Value of property errors.
     */
    public ServiceError[] getErrors() {
        return this.errors;
    }

    /**
     * Setter for property errors.
     * @param errors New value of property errors.
     */
    public void setErrors(ServiceError[] errors) {
        this.errors = errors;
    }

    /**
     * Returns diagnostic information.
     * @return diagnostic information.
     */
    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("<MessageAdminResponse>");
        if (getSource() != null) buf.append(getSource());
        if (getErrors() != null) {
            for (Object o : getErrors()) buf.append(o);
        }
        buf.append("</MessageAdminResponse>");
        return buf.toString();
    }
}
