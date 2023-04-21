package jfun.yan;

import java.io.Serializable;

/**
 * <p>
 * Represents the logic that pulls property values
 * from a Dependency object and inject them into
 * the appropriate properties.
 * </p>
 * <p>
 * A typical implementation may use java.beans package to do introspection.
 * Yet, it is also possible to use any other naming convention, such as "injectAge"
 * for property "age", or use the "age" field directly.
 * </p>
 * @author Ben Yu
 * Dec 16, 2005 1:08:50 PM
 */
public interface PropertiesInjector extends Serializable {

    /**
   * Pull property values and inject to the object.
   * @param obj the object to inject properties to.
   * @param dep the dependency to pull property values form.
   */
    void injectProperties(Object obj, Dependency dep);

    /**
   * Verify that all required properties are resolveable.
   * @param type the type that has the properties.
   * @param dep the dependency to resolve properties.
   */
    void verifyProperties(Class type, Dependency dep);
}
