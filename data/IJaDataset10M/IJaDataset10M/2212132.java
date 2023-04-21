package org.ddljen;

import java.util.Properties;

public class BaseTypeMapper implements TypeMapper {

    private Properties typeMap = new Properties();

    public DataType map(DataType dataType) {
        String sourceType = dataType.getType();
        String destType = typeMap.getProperty(sourceType, sourceType);
        dataType.setType(destType);
        return dataType;
    }

    public Properties getTypeMap() {
        return typeMap;
    }

    public void setTypeMap(Properties types) {
        this.typeMap = types;
    }
}
