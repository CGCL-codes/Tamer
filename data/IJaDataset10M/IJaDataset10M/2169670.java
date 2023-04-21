package net.sf.sit.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.sf.sit.support.Candidate;
import net.sf.sit.support.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Proxy a facade interface by delegating calls on facade methods to
 * corresponding methods on provider objects.
 * <p>
 * This is the core of SIT, this class implements the core algorithm and binds
 * everything together.
 */
public class ServiceInvocationProxy implements InvocationHandler {

    private final Logger log = LoggerFactory.getLogger(ServiceInvocationProxy.class);

    private Configuration config;

    /**
	 * Default constructor.
	 */
    public ServiceInvocationProxy() {
    }

    /**
	 * Full constructor.
	 * 
	 * @param config
	 *            SIT configuration
	 */
    public ServiceInvocationProxy(Configuration config) {
        this.config = config;
    }

    /**
	 * Return SIT configuration.
	 * 
	 * @return SIT configuration
	 */
    public Configuration getConfig() {
        return config;
    }

    /**
	 * Set SIT configuration.
	 * 
	 * @param config
	 *            SIT configuration
	 */
    public void setConfig(Configuration config) {
        this.config = config;
    }

    public Object invoke(Object proxy, Method facadeMethod, Object[] arguments) throws Exception {
        log.info("Proxying invocation of facade method {}", facadeMethod);
        log.debug("With arguments {}", (Object) arguments);
        validate(facadeMethod, arguments);
        Collection<Candidate> candidates = selectCandidates(facadeMethod);
        if (candidates.size() == 0) {
            log.warn("No candidate was found, " + "returning null from facade method {}", facadeMethod);
            return null;
        }
        List<Object> resultValues = new ArrayList<Object>();
        for (Candidate candidate : candidates) {
            processCandidate(candidate, facadeMethod, arguments, resultValues);
        }
        if ("void".equals(facadeMethod.getReturnType().getName())) {
            log.debug("Facade method is void, returning void from {}", facadeMethod);
            return null;
        }
        Object mappedResult = mapResults(facadeMethod, resultValues);
        Object convertedResult = convertResult(facadeMethod, mappedResult);
        log.info("Returning from facade method {}", facadeMethod);
        log.debug("With result value {}", convertedResult);
        return convertedResult;
    }

    /**
	 * Validate arguments of the facade method, handling any exception using
	 * exception handler.
	 * 
	 * @param facadeMethod
	 *            Facade method
	 * @param arguments
	 *            Arguments passed into facade method
	 * @throws Exception
	 *             When exceptionHandler re-throws (wrapped) exception from
	 *             validation
	 */
    private void validate(Method facadeMethod, Object[] arguments) throws Exception {
        log.debug("Validating method arguments {}", (Object) arguments);
        try {
            getConfig().getValidator().validate(facadeMethod, arguments);
        } catch (Exception e) {
            handleException(facadeMethod, e);
        }
    }

    /**
	 * Select candidates for a facade method using candidate selector.
	 * 
	 * @param facadeMethod
	 *            Facade method
	 * @return Candidates
	 */
    private Collection<Candidate> selectCandidates(Method facadeMethod) {
        log.debug("Selecting candidates for facade method {}", facadeMethod);
        Collection<Candidate> candidates = getConfig().getSelector().select(getConfig().getProviders(), facadeMethod);
        return candidates;
    }

    /**
	 * Process a candidate by mapping and converting the arguments and invoking
	 * the provider, storing any result value.
	 * 
	 * @param candidate
	 *            Candidate to process
	 * @param facadeMethod
	 *            Facade method being proxied
	 * @param arguments
	 *            Arguments passed into facade method
	 * @param resultValues
	 *            List storing result values from candidates
	 * @throws Exception
	 *             Thrown from the invocation of the provider method
	 */
    private void processCandidate(Candidate candidate, Method facadeMethod, Object[] arguments, List<Object> resultValues) throws Exception {
        log.debug("Processing candidate {}", candidate);
        try {
            Method providerMethod = candidate.getMethod();
            Object[] convertedArgs;
            if (0 == providerMethod.getParameterTypes().length) {
                log.debug("Provider method has no arguments, " + "skipping processing of arguments");
                convertedArgs = new Object[0];
            } else {
                Object[] mappedArgs = mapArguments(arguments, providerMethod);
                convertedArgs = convertArguments(providerMethod, mappedArgs);
            }
            invokeProvider(facadeMethod, candidate, convertedArgs, providerMethod, resultValues);
        } catch (Exception e) {
            handleException(facadeMethod, e);
        }
        log.debug("Candidate processed {}", candidate);
    }

    /**
	 * Map facade arguments to the provider method arguments using arguments
	 * mapper.
	 * 
	 * @param arguments
	 *            Facade arguments
	 * @param providerMethod
	 *            Provider method
	 * @return Mapped arguments
	 */
    private Object[] mapArguments(Object[] arguments, Method providerMethod) {
        int targetLength = providerMethod.getParameterTypes().length;
        log.debug("Mapping source arguments {} to target length {}", arguments, targetLength);
        Object[] mappedArgs = getConfig().getArgumentsMapper().map(arguments, targetLength, providerMethod);
        assert mappedArgs.length == targetLength;
        return mappedArgs;
    }

    /**
	 * Convert mapped facade arguments to target types of provider method
	 * arguments using arguments converter.
	 * 
	 * @param providerMethod
	 *            Provider method
	 * @param mappedArgs
	 *            Mapped facade arguments
	 * @return Converted arguments
	 */
    private Object[] convertArguments(Method providerMethod, Object[] mappedArgs) {
        Object[] convertedArgs = new Object[mappedArgs.length];
        for (int i = 0; i < mappedArgs.length; i++) {
            Class<?> targetType = providerMethod.getParameterTypes()[i];
            Class<?> targetComponentType = getParameterComponentTypeHint(providerMethod, i);
            log.debug("Converting argument {} to target type {} " + "with target component type {}", new Object[] { mappedArgs[i], targetType, targetComponentType });
            convertedArgs[i] = getConfig().getArgumentsConverter().convert(targetType, mappedArgs[i], targetComponentType);
        }
        return convertedArgs;
    }

    /**
	 * Invoke a provider method using invoker, storing result value, handling
	 * any exception using exception handler.
	 * 
	 * @param facadeMethod
	 *            Facade method being proxied
	 * @param candidate
	 *            Candidate
	 * @param convertedArgs
	 *            Converted arguments to provider method
	 * @param providerMethod
	 *            Provider method to invoke
	 * @param resultValues
	 *            List storing result values from candidates
	 * @throws Exception
	 *             When exceptionHandler re-throws (wrapped) exception from
	 *             invocation of the provider method
	 */
    private void invokeProvider(Method facadeMethod, Candidate candidate, Object[] convertedArgs, Method providerMethod, List<Object> resultValues) throws Exception {
        log.debug("Invoking candidate {} with arguments {}", candidate, convertedArgs);
        Object resultValue = getConfig().getInvoker().invoke(providerMethod, candidate.getProvider(), convertedArgs);
        resultValues.add(resultValue);
    }

    /**
	 * Handle exception from provider method invocation using exception handler.
	 * 
	 * @param facadeMethod
	 *            Facade method being proxied
	 * @param e
	 *            Exception from provider method invocation
	 * @throws Exception
	 *             Processed exception to be thrown up
	 */
    private void handleException(Method facadeMethod, Exception e) throws Exception {
        if (e instanceof InvocationTargetException && e.getCause() instanceof Exception) {
            e = (Exception) e.getCause();
        }
        log.debug("Handling exception {}, \"{}\" on method {}", new Object[] { e.getClass().getName(), e.getMessage(), facadeMethod });
        getConfig().getExceptionHandler().handleException(e, facadeMethod);
    }

    /**
	 * Map result values to a single value using result mapper.
	 * 
	 * @param facadeMethod
	 *            Facade method being proxied
	 * @param resultValues
	 *            Result values to map
	 * @return Mapped single result value
	 */
    private Object mapResults(Method facadeMethod, List<Object> resultValues) {
        Object[] resultArray = resultValues.toArray();
        log.debug("Mapping results {} to target length 1", resultArray);
        Object[] mappedResults = getConfig().getResultsMapper().map(resultArray, 1, facadeMethod);
        assert mappedResults.length == 1;
        Object mappedResult = mappedResults[0];
        return mappedResult;
    }

    /**
	 * Convert mapped result value to facade method return type using result
	 * converter.
	 * 
	 * @param facadeMethod
	 *            Facade method being proxied
	 * @param mappedResult
	 *            Mapped result value
	 * @return Result value converted to return type of the facade method
	 */
    private Object convertResult(Method facadeMethod, Object mappedResult) {
        Class<?> targetType = facadeMethod.getReturnType();
        Class<?> targetComponentType = getReturnComponentTypeHint(facadeMethod);
        log.debug("Converting result {} to target type {} " + "with target component type {}", new Object[] { mappedResult, targetType, targetComponentType });
        Object convertedResult = getConfig().getResultConverter().convert(targetType, mappedResult, targetComponentType);
        return convertedResult;
    }

    private Class<?> getReturnComponentTypeHint(Method method) {
        Type genericReturnType = method.getGenericReturnType();
        return getComponentTypeHint(genericReturnType);
    }

    private Class<?> getParameterComponentTypeHint(Method method, int parameterIndex) {
        Type genericParameterType = method.getGenericParameterTypes()[parameterIndex];
        return getComponentTypeHint(genericParameterType);
    }

    private Class<?> getComponentTypeHint(Type genericType) {
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Class<?> targetComponentType = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            return targetComponentType;
        }
        return null;
    }
}
