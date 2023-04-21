package net.disy.ogc.wps.v_1_0_0.model;

import static net.disy.ogc.wps.v_1_0_0.util.Validate.notContains;
import static org.apache.commons.lang.Validate.notNull;
import java.util.HashMap;
import java.util.Map;

public class DefaultLiteralTypeRegistry implements LiteralTypeRegistry {

    private final Map<DataType<?>, LiteralType<?>> literalTypes = new HashMap<DataType<?>, LiteralType<?>>();

    public void add(LiteralType<?> literalType) {
        notNull(literalType);
        notContains(literalTypes, literalType.getDataType());
        literalTypes.put(literalType.getDataType(), literalType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> LiteralType<T> getLiteralType(DataType<T> dataType) {
        notNull(dataType);
        if (!literalTypes.containsKey(dataType)) {
            throw new IllegalArgumentException("dataType for schemaDesignator '" + dataType.getSchemaDesignator() + "' is no literal type.");
        }
        return (LiteralType<T>) literalTypes.get(dataType);
    }
}
