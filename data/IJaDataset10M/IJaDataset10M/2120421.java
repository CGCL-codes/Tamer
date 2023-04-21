package com.director.example.action;

import com.director.core.annotation.DirectMethod;
import com.director.core.annotation.RemotingProviderConfig;
import org.apache.struts2.convention.annotation.Action;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Simone Ricciardi
 * Date: 2-giu-2010
 * Time: 11.11.14
 */
public class ProfileAction {

    /**
    * Handler for client side form sumbit
    *
    * @param formPacket Collection of form items along with direct data
    * @return Array response packet
    */
    @Action("updateBasicInfo")
    @DirectMethod
    @RemotingProviderConfig(formHandler = true)
    public Map<String, Object> updateBasicInfo(Map<String, String> formPacket) {
        boolean success;
        Map<String, Object> response = new HashMap<String, Object>();
        String email = formPacket.get("email");
        if (email.equals("aaron@extjs.com")) {
            success = false;
            Map<String, String> errors = new HashMap<String, String>();
            errors.put("email", "already taken");
            response.put("errors", errors);
        } else {
            success = true;
        }
        response.put("success", success);
        response.put("debug_formPacket", formPacket);
        return response;
    }

    /**
    * put your comment there...
    * This method configured with len=2, so 2 arguments will be sent
    * in the order according to the client side specified paramOrder
    *
    * @param  userId
    * @param  foo
    * @return Array response packet
    */
    @Action("getBasicInfo")
    @DirectMethod
    public Map<String, Object> getBasicInfo(String userId, String foo) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        result.put("success", true);
        result.put("data", data);
        data.put("foo", foo);
        data.put("name", "Aaron Conran");
        data.put("company", "Ext JS, LLC");
        data.put("email", "aaron@extjs.com");
        return result;
    }

    @Action("getPhoneInfo")
    @DirectMethod
    public Map<String, Object> getPhoneInfo(String userId) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        result.put("success", true);
        result.put("data", data);
        data.put("cell", "443-555-1234");
        data.put("office", "1-800-CALLEXT");
        data.put("home", "");
        return result;
    }

    @Action("getLocationInfo")
    @DirectMethod
    public Map<String, Object> getLocationInfo(String userId) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        result.put("success", true);
        result.put("data", data);
        data.put("street", "1234 Red Dog Rd.");
        data.put("city", "Seminole");
        data.put("state", "FL");
        data.put("zip", 33776);
        return result;
    }
}
