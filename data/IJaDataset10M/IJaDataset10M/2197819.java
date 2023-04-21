package werkzeugkasten.twowaysql.dao.base.binder;

import java.sql.Blob;
import werkzeugkasten.twowaysql.dao.Binder;
import werkzeugkasten.twowaysql.dao.BinderFactory;

public class BlobBinderFactory implements BinderFactory {

    @Override
    public Binder create(Object value) throws IllegalArgumentException {
        if (value instanceof Blob) {
            Blob v = (Blob) value;
            return new BlobBinder(v);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Class<?> targetType() {
        return Blob.class;
    }

    @Override
    public String bindingTypeName() {
        return "Blob";
    }
}
