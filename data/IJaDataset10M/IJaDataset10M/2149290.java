package jfun.yan;

import java.util.LinkedHashMap;
import java.util.Map;
import jfun.yan.function.Signature;

/**
 * Helper class to get meta information
 * about a component. 
 * Including expected parameters, expected properties, etc.
 * <p>
 * Zephyr Business Solution
 *
 * @author Michelle Lei
 *
 */
public class Introspector {

    /**
   * Get the meta info about a component object.
   * @param c the component to introspect.
   * @return the ComponentInfo object containing the introspection result.
   */
    public static ComponentInfo getComponentInfo(Component c) {
        final class Result {

            private final Map props = new LinkedHashMap();

            private final Map params = new LinkedHashMap();

            private int max_param = -1;

            int getParamCount() {
                return max_param + 1;
            }

            Map getParams() {
                return params;
            }

            Map getProps() {
                return props;
            }

            void addProp(Object key, Class type) {
                props.put(key, type);
            }

            void addParam(int ind, Class type) {
                if (ind > max_param) {
                    max_param = ind;
                }
                params.put(new Integer(ind), type);
            }
        }
        final Result result = new Result();
        final Component checker = c;
        final Container container = new jfun.yan.containers.DefaultContainer() {

            public Dependency getDependency(Object key, ComponentMap cmap) {
                if (key == null) {
                    return new IntrospectionDependency(cmap, key) {

                        public Class verifyArgument(Signature source, int ind, Class type) throws IrresolveableArgumentException, ParameterTypeMismatchException, AmbiguousComponentResolutionException, YanException {
                            result.addParam(ind, type);
                            return type;
                        }

                        public Class verifyProperty(Class component_type, Object k, Class type) throws IrresolveablePropertyException, PropertyTypeMismatchException, AmbiguousComponentResolutionException, YanException {
                            result.addProp(k, type);
                            return type;
                        }
                    };
                } else return new IntrospectionDependency(cmap, key);
            }

            public Dependency getDependencyOfType(Class type, ComponentMap cmap) {
                return new IntrospectionDependency(cmap, type);
            }
        };
        final Class type = container.verifyComponent(checker);
        return new ComponentInfo(result.getParamCount(), result.getParams(), result.getProps(), type);
    }
}
