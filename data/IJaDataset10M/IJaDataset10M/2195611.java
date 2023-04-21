package au.gov.nla.aons.rest.handler;

import javax.servlet.http.HttpServletRequest;
import au.gov.nla.aons.rest.exceptions.ParameterInvalidException;
import au.gov.nla.aons.rest.exceptions.ParameterMissingException;

public class HandlerUtil {

    public Long retrieveParameterAsLong(HttpServletRequest request, String name, boolean required) {
        String value = request.getParameter(name);
        if (value == null) {
            if (required) {
                throw new ParameterMissingException(name);
            } else {
                return null;
            }
        }
        try {
            return Long.decode(value);
        } catch (NumberFormatException nfe) {
            throw new ParameterInvalidException(name, value, nfe.getMessage(), nfe);
        }
    }

    public String retrieveParameter(HttpServletRequest request, String name, boolean required) {
        String value = request.getParameter(name);
        if (value == null && required) {
            throw new ParameterMissingException(name);
        }
        return value;
    }
}
