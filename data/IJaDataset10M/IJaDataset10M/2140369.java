package kotan.util;

import kotan.datastore.api.Blob;
import kotan.datastore.api.Text;

public class PropertyUtil {

    public static Object getDefaultValue(Class<?> type) {
        if (type == null) return null;
        if (type == String.class) return "";
        if (type == Boolean.class) return Boolean.FALSE;
        if (type == Integer.class) return 0;
        if (type == Long.class) return 0L;
        if (type == Float.class) return 0f;
        if (type == Double.class) return 0d;
        if (type == Blob.class) return new Blob(new byte[0]);
        if (type == Text.class) return new Text("");
        return null;
    }
}
