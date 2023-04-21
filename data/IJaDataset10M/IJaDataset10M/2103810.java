package mirrormonkey.state.member.accessor;

import java.lang.reflect.Field;
import mirrormonkey.framework.entity.SyncEntity;

public class FieldWriteAccessor implements ValueWriteAccessor {

    public final Field f;

    public FieldWriteAccessor(Field f) {
        this.f = f;
    }

    @Override
    public void writeValue(SyncEntity entity, Object value) {
        try {
            f.set(entity, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
