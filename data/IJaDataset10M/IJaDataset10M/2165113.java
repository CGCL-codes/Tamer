package com.inet.qlcbcc.converter;

import org.json.JSONException;
import org.json.JSONObject;
import org.webos.core.json.convert.ObjectToJsonObjectConverter;
import com.inet.qlcbcc.domain.Role;

/**
 * RoleToJsonConverter.
 *
 * @author Dung Nguyen
 * @version $Id: RoleToJsonConverter.java 2011-05-13 16:50:08z nguyen_dv $
 *
 * @since 1.0
 */
public class RoleToJsonConverter implements ObjectToJsonObjectConverter<Role> {

    public JSONObject convert(Role source) {
        try {
            return new JSONObject().accumulate("id", source.getId()).accumulate("name", source.getName()).accumulate("desc", source.getDescription()).accumulate("system", source.getSystem().booleanValue());
        } catch (JSONException ex) {
            throw new IllegalStateException("Could not convert the role to JSON", ex);
        }
    }
}
