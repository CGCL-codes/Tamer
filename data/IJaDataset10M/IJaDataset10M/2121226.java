package com.versant.core.jdbc;

import com.versant.core.jdbc.metadata.JdbcColumn;
import com.versant.core.jdbc.sql.conv.TypeAsStringConverter;
import com.versant.core.jdbc.sql.conv.TypeAsBytesConverter;
import com.versant.core.util.classhelper.ClassHelper;
import com.versant.core.common.BindingSupportImpl;
import java.util.HashMap;
import java.util.Map;

/**
 * This manages and creates JdbcConverterFactory instances from the names
 * of the factory classes. It makes sure that a given factory is only
 * created once.
 */
public class JdbcConverterFactoryRegistry {

    private ClassLoader loader;

    private Map map = new HashMap();

    /**
     * Placeholder converter used to indicate that no converter is needed.
     * It returns null when asked to create a converter.
     */
    public static final JdbcConverterFactory NULL_CONVERTER = new JdbcConverterFactory() {

        public Object createArgsBean() {
            return null;
        }

        public JdbcConverter createJdbcConverter(JdbcColumn col, Object args, JdbcTypeRegistry jdbcTypeRegistry) throws IllegalArgumentException {
            return null;
        }
    };

    public static final String NULL_CONVERTER_NAME = "{none}";

    public static final String STRING_CONVERTER_NAME = "STRING";

    public static final String BYTES_CONVERTER_NAME = "BYTES";

    public JdbcConverterFactoryRegistry(ClassLoader loader) {
        this.loader = loader;
        map.put(NULL_CONVERTER_NAME, NULL_CONVERTER);
        map.put(STRING_CONVERTER_NAME, new TypeAsStringConverter.Factory());
        map.put(BYTES_CONVERTER_NAME, new TypeAsBytesConverter.Factory());
    }

    public ClassLoader getLoader() {
        return loader;
    }

    /**
     * Get the factory with class name.
     */
    public JdbcConverterFactory getFactory(String name) {
        JdbcConverterFactory ans = (JdbcConverterFactory) map.get(name);
        if (ans == null) {
            Class t = null;
            try {
                t = ClassHelper.get().classForName(name, true, loader);
            } catch (ClassNotFoundException e) {
                throw BindingSupportImpl.getInstance().runtime("Unable to load JdbcConverterFactory class " + name, e);
            }
            try {
                ans = (JdbcConverterFactory) t.newInstance();
            } catch (Exception x) {
                throw BindingSupportImpl.getInstance().runtime("Unable to create JdbcConverterFactory instance " + t.getName(), x);
            }
            map.put(name, ans);
        }
        return ans;
    }
}
