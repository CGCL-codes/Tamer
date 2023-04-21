package org.fb4j.impl;

import java.util.HashSet;
import java.util.Set;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.fb4j.ApplicationProperties;

/**
 * @author Mino Togna
 * 
 */
public class GetApplicationPropertiesMethodCall extends JsonMethodCallBase<ApplicationProperties> {

    protected GetApplicationPropertiesMethodCall(String[] properties) {
        super("admin.getAppProperties");
        JSONArray array = JSONArray.fromObject(properties);
        setParameter("properties", array.toString());
    }

    @Override
    protected ApplicationProperties processJsonResponse(String responseData) throws InvalidResponseException {
        Set<String> initializedFields = new HashSet<String>();
        JSONArray array = JSONArray.fromObject(getParameter("properties"));
        for (Object object : array) {
            initializedFields.add(object.toString());
        }
        JSONObject jsonObject = JSONObject.fromObject(responseData);
        ApplicationPropertiesImpl applicationProperties = new ApplicationPropertiesImpl();
        applicationProperties.initialize(jsonObject, initializedFields);
        return applicationProperties;
    }
}
