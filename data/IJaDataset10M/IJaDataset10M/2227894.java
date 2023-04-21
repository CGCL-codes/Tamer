package atunit.core;

import java.lang.reflect.Field;
import java.util.Map;

public interface Container {

    Object createTest(Class<?> testClass, Map<Field, Object> fieldValues) throws Exception;
}
