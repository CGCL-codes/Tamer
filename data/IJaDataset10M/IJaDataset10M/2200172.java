package org.xdoclet.plugin.web.qtags;

import com.thoughtworks.qdox.model.AbstractJavaEntity;
import java.util.*;
import org.xdoclet.XDocletTag;

public class WebSecurityRoleRefTagImpl extends XDocletTag implements WebSecurityRoleRefTag {

    public WebSecurityRoleRefTagImpl(String name, String value, AbstractJavaEntity entity, int lineNumber) {
        super(name, value, entity, lineNumber);
    }

    public String getRoleName() {
        boolean required = true;
        String result = getNamedParameter("role-name");
        if (required && result == null) bomb("role-name=\"???\" must be specified.");
        String retVal = null;
        if (result != null) retVal = result;
        return retVal;
    }

    public String getRoleLink() {
        boolean required = true;
        String result = getNamedParameter("role-link");
        if (required && result == null) bomb("role-link=\"???\" must be specified.");
        String retVal = null;
        if (result != null) retVal = result;
        return retVal;
    }

    public String getDescription() {
        boolean required = false;
        String result = getNamedParameter("description");
        if (required && result == null) bomb("description=\"???\" must be specified.");
        String retVal = null;
        if (result != null) retVal = result;
        return retVal;
    }

    protected void validateLocation() {
        if (ALLOWED_VALUES.size() > 1 && !ALLOWED_VALUES.contains(getValue())) bomb("\"" + getValue() + "\" is not a valid value. Allowed values are ");
        Collection parameterNames = getNamedParameterMap().keySet();
        Iterator iterator = parameterNames.iterator();
        do {
            if (!iterator.hasNext()) break;
            String parameterName = (String) iterator.next();
            if (!ALLOWED_PARAMETERS.contains(parameterName)) bomb(parameterName + " is an invalid parameter name.");
        } while (true);
        getRoleName();
        getRoleLink();
        getDescription();
    }

    public static final String NAME = "web.security-role-ref";

    private static final List ALLOWED_PARAMETERS = Arrays.asList(new String[] { "role-name", "role-link", "description", "" });

    private static final List ALLOWED_VALUES = Arrays.asList(new String[] { "" });

    public void validateModel() {
    }
}
