package net.sf.joafip.store.entity.conversion;

import net.sf.joafip.store.entity.classinfo.FieldInfo;
import net.sf.joafip.store.entity.objectio.ObjectAndPersistInfo;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class ValuedField {

    private final FieldInfo fieldInfo;

    private final ObjectAndPersistInfo value;

    public ValuedField(final FieldInfo fieldInfo, final ObjectAndPersistInfo value) {
        super();
        this.fieldInfo = fieldInfo;
        this.value = value;
    }

    public FieldInfo getFieldInfo() {
        return fieldInfo;
    }

    public ObjectAndPersistInfo getValue() {
        return value;
    }

    public boolean fieldIsBasicType() {
        return fieldInfo.isBasicType();
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("ValuedField [fieldInfo=");
        builder.append(fieldInfo);
        builder.append(", value=");
        builder.append(value);
        builder.append("]");
        return builder.toString();
    }
}
