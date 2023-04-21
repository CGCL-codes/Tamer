package org.powermock.api.mockito.mockpolicies;

import org.mockito.Mockito;
import org.powermock.core.spi.PowerMockPolicy;
import org.powermock.mockpolicies.MockPolicyClassLoadingSettings;
import org.powermock.mockpolicies.MockPolicyInterceptionSettings;
import org.powermock.mockpolicies.support.LogPolicySupport;
import java.lang.reflect.Method;

/**
 * Sfl4j mock policy that injects a Mockito-created mock to be returned on calls to getLogger factory methods.
 * The implementation returns a single mock instance per thread but it doesn't return a different mock instance based
 * on the actual value passed to getLogger. This limitation is acceptable in most real uses cases.
 * <p/>
 * Tests that want to do verifications on the mocked logger can do so by getting the mocked instance as production code
 * does: {@link org.slf4j.LoggerFactory#getLogger(Class)}. However, it is critical that the mocked logger is
 * reset after each test in order to avoid crosstalk between test cases.
 * <p/>
 *
 * @author Alexandre Normand <alexandre.normand@gmail.com>
 */
public class Slf4jMockPolicy implements PowerMockPolicy {

    private static final String LOGGER_FACTORY_CLASS_NAME = "org.slf4j.LoggerFactory";

    private static final String LOGGER_FACTORY_METHOD_NAME = "getLogger";

    private static final String FRAMEWORK_NAME = "sfl4j";

    private static final String LOGGER_CLASS_NAME = "org.slf4j.Logger";

    private static ThreadLocal<Object> threadLogger = new ThreadLocal<Object>();

    public void applyClassLoadingPolicy(MockPolicyClassLoadingSettings mockPolicyClassLoadingSettings) {
        mockPolicyClassLoadingSettings.addFullyQualifiedNamesOfClassesToLoadByMockClassloader(LOGGER_FACTORY_CLASS_NAME, "org.apache.log4j.Appender", "org.apache.log4j.xml.DOMConfigurator");
    }

    public void applyInterceptionPolicy(MockPolicyInterceptionSettings mockPolicyInterceptionSettings) {
        LogPolicySupport logPolicySupport = new LogPolicySupport();
        Method[] loggerFactoryMethods = logPolicySupport.getLoggerMethods(LOGGER_FACTORY_CLASS_NAME, LOGGER_FACTORY_METHOD_NAME, FRAMEWORK_NAME);
        initializeMockForThread(logPolicySupport);
        for (Method loggerFactoryMethod : loggerFactoryMethods) {
            mockPolicyInterceptionSettings.stubMethod(loggerFactoryMethod, threadLogger.get());
        }
    }

    private void initializeMockForThread(LogPolicySupport logPolicySupport) {
        Class<?> loggerClass = getLoggerClass(logPolicySupport);
        if (threadLogger.get() == null) {
            threadLogger.set(Mockito.mock(loggerClass));
        }
    }

    private Class<?> getLoggerClass(LogPolicySupport logPolicySupport) {
        Class<?> loggerType;
        try {
            loggerType = logPolicySupport.getType(LOGGER_CLASS_NAME, FRAMEWORK_NAME);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return loggerType;
    }
}
