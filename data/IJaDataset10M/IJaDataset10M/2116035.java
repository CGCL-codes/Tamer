package org.dashz.web.service.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;

/**
*
*
* @author <a href="mailto:lcor1979 AT gmail DOT com">Laurent Corn�lis</a>
* @author <a href="mailto:yanngeek AT gmail DOT com">Yannick Lemin</a>
* @version @Version@
*/
public class JSONInputObjectMapper implements InputObjectMapper {

    public static final String DEFAULT_PARAMETER_NAME = "jsoninput";

    private Class objectClass;

    private String parameterName;

    public JSONInputObjectMapper() {
        this(DEFAULT_PARAMETER_NAME, null);
    }

    public JSONInputObjectMapper(Class objectClass) {
        this(DEFAULT_PARAMETER_NAME, objectClass);
    }

    public JSONInputObjectMapper(String parameterName, Class objectClass) {
        this.parameterName = parameterName;
        this.objectClass = objectClass;
    }

    public Object mapObject(Map<String, String> restParameters, HttpServletRequest request) throws Exception {
        JSONObject jsonObject = JSONObject.fromObject(request.getParameter(getParameterName()));
        return JSONObject.toBean(jsonObject, getObjectClass());
    }

    public Class getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(Class objectClass) {
        this.objectClass = objectClass;
    }

    public String getParameterName() {
        return parameterName;
    }

    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }
}
