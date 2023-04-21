package org.fosterapet.dao;

import java.util.*;
import org.greatlogic.gae.dao.DAOAnnotations.EntityProperty;
import org.greatlogic.gae.dao.*;
import org.greatlogic.gae.dao.DAOEnums.EDAOAction;
import org.json.*;

public class Role extends DAOBase {

    @EntityProperty(maxLength = 30)
    private String desc;

    @EntityProperty(maxLength = 10, nullable = false, unique = true)
    private String shortDesc;

    public Role(final EDAOAction action, final Object propertyValues, final String... foreignKeyNames) throws Exception {
        super(action, propertyValues, foreignKeyNames);
    }

    public Role(final EDAOAction action, final JSONObject jsonObject) throws Exception {
        super(action, jsonObject);
    }

    public Role(final EDAOAction action, final Map<String, String[]> paramMap) throws Exception {
        super(action, paramMap);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }
}
