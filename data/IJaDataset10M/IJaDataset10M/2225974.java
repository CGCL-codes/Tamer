package an.xacml.engine.ctx;

import static an.util.PackageUtil.findClassesByPackage;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import an.log.LogFactory;
import an.log.Logger;
import an.xacml.engine.BuiltInFunctionExistsException;
import an.xacml.engine.BuiltInFunctionNotFoundException;
import an.xacml.function.BuiltInFunction;
import an.xacml.function.XACMLFunction;
import an.xacml.function.XACMLFunctionProvider;
import an.xml.XMLGeneralException;

/**
 * Before a function could be use, it should be registered using its signature.
 */
public class FunctionRegistry {

    public static String XACML_FUNCTION_PACKAGE = "an.xacml.policy.function";

    private static FunctionRegistry functionReg;

    private Map<String, BuiltInFunction> functions = new HashMap<String, BuiltInFunction>();

    private Logger logger;

    private FunctionRegistry() throws IOException, ClassNotFoundException, BuiltInFunctionExistsException {
        logger = LogFactory.getLogger();
        initialize();
    }

    /**
     * Get a FunctionRegistry instance by given PDP.
     * @param pdp
     * @return
     * @throws XMLGeneralException 
     * @throws ClassNotFoundException 
     * @throws IOException 
     * @throws BuiltInFunctionExistsException 
     */
    public static synchronized FunctionRegistry getInstance() throws IOException, ClassNotFoundException, BuiltInFunctionExistsException {
        if (functionReg == null) {
            functionReg = new FunctionRegistry();
        }
        return functionReg;
    }

    /**
     * Load all configured functions to the registry using current PDP's configuration.
     * @throws ClassNotFoundException 
     * @throws IOException 
     * @throws BuiltInFunctionExistsException 
     */
    protected void initialize() throws IOException, ClassNotFoundException, BuiltInFunctionExistsException {
        Set<Class<?>> providerClasses = new HashSet<Class<?>>();
        findClassesByPackage(XACML_FUNCTION_PACKAGE, true, providerClasses);
        for (Class<?> provider : providerClasses) {
            try {
                if (provider.isAnnotationPresent(XACMLFunctionProvider.class)) {
                    Method[] methods = provider.getMethods();
                    for (final Method current : methods) {
                        XACMLFunction funcAnn = current.getAnnotation(XACMLFunction.class);
                        if (funcAnn != null) {
                            if (!Modifier.isStatic(current.getModifiers())) {
                                throw new IllegalArgumentException("We expected method '" + provider.getSimpleName() + ":" + current.getName() + "' is static, but it isn't, we can't load a non-static function.");
                            }
                            String[] funcNames = funcAnn.value();
                            if (funcNames.length == 0) {
                                funcNames = new String[] { current.getName() };
                            }
                            final Map<Class<?>, Annotation> funcAttrs = new HashMap<Class<?>, Annotation>();
                            Annotation[] anns = current.getDeclaredAnnotations();
                            for (Annotation ann : anns) {
                                if (!ann.annotationType().equals(XACMLFunction.class)) {
                                    funcAttrs.put(ann.annotationType(), ann);
                                }
                            }
                            for (final String funcName : funcNames) {
                                if (funcName != null && funcName.trim().length() > 0) {
                                    if (functions.get(funcName) != null) {
                                        throw new BuiltInFunctionExistsException("The built-in function '" + funcName + "' has been registered.");
                                    }
                                    BuiltInFunction function = new BuiltInFunction() {

                                        public String getFunctionId() {
                                            return funcName;
                                        }

                                        public Object invoke(EvaluationContext ctx, Object[] params) throws Exception {
                                            return current.invoke(null, ctx, params);
                                        }

                                        public Object[] getAllAttributes() {
                                            return funcAttrs.values().toArray();
                                        }

                                        public Object getAttribute(Object key) {
                                            return funcAttrs.get(key);
                                        }
                                    };
                                    register(function);
                                }
                            }
                        }
                    }
                }
            } catch (BuiltInFunctionExistsException existEx) {
                throw existEx;
            } catch (Exception e) {
                logger.error("Error occurs when loading function, will skip current one and continue with next", e);
            }
        }
    }

    public void register(BuiltInFunction function) {
        functions.put(function.getFunctionId(), function);
    }

    public BuiltInFunction unregister(BuiltInFunction function) throws BuiltInFunctionNotFoundException {
        return unregister(function.getFunctionId());
    }

    public BuiltInFunction unregister(String funcId) throws BuiltInFunctionNotFoundException {
        BuiltInFunction willRemove = lookup(funcId);
        if (willRemove != null) {
            functions.remove(funcId);
        }
        return willRemove;
    }

    protected void unregisterAll() {
        functions.clear();
    }

    public BuiltInFunction lookup(String funcId) throws BuiltInFunctionNotFoundException {
        BuiltInFunction func = functions.get(funcId);
        if (func == null) {
            throw new BuiltInFunctionNotFoundException("The built-in function " + funcId + " could not be found in registry.");
        }
        return func;
    }
}
