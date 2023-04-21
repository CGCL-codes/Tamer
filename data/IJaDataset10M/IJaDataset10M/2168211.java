package com.koylu.caffein.model.clazz.id;

import java.sql.PreparedStatement;
import com.koylu.caffein.model.caffein.CaffeinConfig;
import com.koylu.caffein.model.clazz.Property;

public class AssignedId extends Property implements Id {

    public String toUniqueId(CaffeinConfig caffeinConfig, Object id) throws Exception {
        return getConverter(caffeinConfig).objectToString(id);
    }

    public String[] getGeneratedColumnNames() {
        return null;
    }

    public void setGeneratedKeys(CaffeinConfig caffeinConfig, Object object, PreparedStatement preparedStatement) throws Exception {
    }

    public boolean isColumnNullable(CaffeinConfig caffeinConfig) throws Exception {
        return false;
    }
}
