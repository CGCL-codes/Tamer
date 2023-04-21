package com.googlecode.avgas.test.appengine;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.Set;
import com.google.appengine.tools.development.ApiProxyLocalImpl;
import com.google.apphosting.api.ApiProxy;
import com.googlecode.avgas.test.appengine.annotation.AppEngineEnvironment;
import com.googlecode.avgas.test.appengine.annotation.AppEngineService;
import com.googlecode.avgas.test.stripes.annotation.StripesActionBean;
import org.unitils.core.Module;
import org.unitils.core.TestListener;
import org.unitils.core.UnitilsException;
import org.unitils.spring.annotation.SpringBean;
import org.unitils.util.AnnotationUtils;
import static org.unitils.util.AnnotationUtils.getFieldsAnnotatedWith;
import static org.unitils.util.AnnotationUtils.getMethodsAnnotatedWith;
import static org.unitils.util.ReflectionUtils.invokeMethod;
import static org.unitils.util.ReflectionUtils.isSetter;
import static org.unitils.util.ReflectionUtils.setFieldValue;

/**
 * Create Date: 2009/4/23
 *
 * @author Alan She
 */
public class AppEngineServiceModule implements Module {

    public void init(Properties properties) {
    }

    public void afterInit() {
    }

    public TestListener getTestListener() {
        return new AppEngineServiceTestListener();
    }

    public void setupEnvironment(Object testObject) {
        AppEngineTestEnvironment environment = new AppEngineTestEnvironment();
        ApiProxy.setEnvironmentForCurrentThread(environment);
        ApiProxy.setDelegate(new ApiProxyLocalImpl(new File(".")) {
        });
        Set<Field> fields = getFieldsAnnotatedWith(testObject.getClass(), AppEngineEnvironment.class);
        for (Field field : fields) {
            try {
                setFieldValue(testObject, field, environment);
            } catch (UnitilsException e) {
                throw new UnitilsException("Unable to assign the Spring bean value to field annotated with @" + SpringBean.class.getSimpleName(), e);
            }
        }
        Set<Method> methods = getMethodsAnnotatedWith(testObject.getClass(), AppEngineEnvironment.class);
        for (Method method : methods) {
            try {
                if (!isSetter(method)) {
                    throw new UnitilsException("Unable to assign the Stripes action bean value to method annotated with @" + StripesActionBean.class.getSimpleName() + ". Method " + method.getName() + " is not a setter method.");
                }
                invokeMethod(testObject, method, environment);
            } catch (UnitilsException e) {
                throw new UnitilsException("Unable to assign the Stripes action bean value to method annotated with @" + StripesActionBean.class.getSimpleName(), e);
            } catch (InvocationTargetException e) {
                throw new UnitilsException("Unable to assign the Stripes action bean value to method annotated with @" + StripesActionBean.class.getSimpleName() + ". Method " + "has thrown an exception.", e.getCause());
            }
        }
    }

    public void tearDownEnvironment() {
        ApiProxy.setDelegate(null);
        ApiProxy.setEnvironmentForCurrentThread(null);
    }

    protected class AppEngineServiceTestListener extends TestListener {

        @Override
        public void beforeTestSetUp(Object testObject, Method testMethod) {
            AppEngineService annotation = AnnotationUtils.getMethodOrClassLevelAnnotation(AppEngineService.class, testMethod, testObject.getClass());
            if (annotation != null) {
                setupEnvironment(testObject);
            }
        }

        @Override
        public void afterTestTearDown(Object testObject, Method testMethod) {
            AppEngineService annotation = AnnotationUtils.getMethodOrClassLevelAnnotation(AppEngineService.class, testMethod, testObject.getClass());
            if (annotation != null) {
                tearDownEnvironment();
            }
        }
    }
}
