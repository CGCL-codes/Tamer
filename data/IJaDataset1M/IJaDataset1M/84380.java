package net.sourceforge.configured.rules.standard.typeresolver.annotation;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sourceforge.configured.annotation.ServiceProvider;
import net.sourceforge.configured.rules.standard.typeresolver.ServiceProviderInfo;
import net.sourceforge.configured.utils.classpath.ClasspathResourceHandler;
import net.sourceforge.configured.utils.classpath.ClasspathScanner;
import net.sourceforge.configured.utils.classpath.ClasspathUtils;
import org.apache.commons.lang.StringUtils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.EmptyVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author dboyce
 *
 */
public class ClasspathScanningServiceProviderLocator {

    protected Logger logger = LoggerFactory.getLogger(ClasspathScanningServiceProviderLocator.class);

    protected String basePackage;

    protected ClasspathScanner classpathScanner = new ClasspathScanner();

    protected static final String SERVICE_PROVIDER_ANNOTATION_NAME = "L" + ClasspathUtils.packageNameToResourcePath(ServiceProvider.class.getName()) + ";";

    public Map<ServiceProviderInfo, Class<?>> locateServiceProviders(List<String> basePackages) {
        final Map<ServiceProviderInfo, Class<?>> ret = new HashMap<ServiceProviderInfo, Class<?>>();
        for (String basePackage : basePackages) {
            classpathScanner.scanClasspath(basePackage, new ClasspathResourceHandler() {

                @Override
                public boolean filterResource(String fileName) {
                    return fileName.endsWith(".class");
                }

                @Override
                public void handleClasspathResource(String fileName, InputStream is) {
                    try {
                        processClassFile(is, ret);
                    } catch (Exception e) {
                        logger.error("error processing " + fileName + ": " + e.getMessage(), e);
                    }
                }
            });
        }
        return ret;
    }

    protected void processClassFile(final InputStream is, final Map<ServiceProviderInfo, Class<?>> providerMap) throws IOException {
        final class Context {

            String className;

            Class<?> providerClass;

            boolean multiProvider = false;

            Class<?> serviceInterface;

            String providerName;

            List<Class<?>> serviceInterfaces = new ArrayList<Class<?>>();

            List<String> providerNames = new ArrayList<String>();

            String currentArrayName;

            boolean testImpl = false;

            public void endOfAnnotation() {
                serviceInterface = null;
                providerName = null;
                multiProvider = false;
                serviceInterfaces.clear();
                providerNames.clear();
                currentArrayName = null;
                testImpl = false;
            }

            public void endOfClass() {
                endOfAnnotation();
                className = null;
                providerClass = null;
            }
        }
        final Context context = new Context();
        ClassReader classReader = new ClassReader(is);
        final EmptyVisitor emptyVisitor = new EmptyVisitor();
        classReader.accept(new EmptyVisitor() {

            @Override
            public void visit(int arg0, int arg1, String arg2, String arg3, String arg4, String[] arg5) {
                context.className = ClasspathUtils.getClassNameFromInternalName(arg2);
                try {
                    context.providerClass = Class.forName(context.className);
                } catch (Exception e) {
                    throw new RuntimeException("error loading class: " + context.className, e);
                }
            }

            @Override
            public void visitEnd() {
                context.endOfClass();
            }

            @Override
            public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
                if (!StringUtils.equals(SERVICE_PROVIDER_ANNOTATION_NAME, arg0)) {
                    return emptyVisitor;
                } else {
                    return new AnnotationVisitor() {

                        @Override
                        public void visit(String name, Object value) {
                            if (!context.multiProvider) {
                                if ("service".equals(name)) {
                                    if (!(value instanceof Type)) {
                                        throw new RuntimeException("unexpected value " + value + "type for service attribute of ServiceProvider annotation on class: " + context.className);
                                    }
                                    Type tVal = (Type) value;
                                    try {
                                        context.serviceInterface = Class.forName(tVal.getClassName());
                                    } catch (ClassNotFoundException e) {
                                        logger.error("error processing ServiceProvider annotation for class " + context.className, e);
                                    }
                                } else if ("name".equals(name)) {
                                    context.providerName = String.valueOf(value);
                                } else if ("testServiceProvider".equals(name)) {
                                    context.testImpl = Boolean.parseBoolean(String.valueOf(value));
                                }
                            } else {
                                if ("services".equals(context.currentArrayName)) {
                                    if (!(value instanceof Type)) {
                                        throw new RuntimeException("Couldn't process value class: " + value.getClass());
                                    }
                                    Type tVal = (Type) value;
                                    try {
                                        context.serviceInterfaces.add(Class.forName(tVal.getClassName()));
                                    } catch (ClassNotFoundException e) {
                                        logger.error("error processing ServiceProvider annotation for class " + context.className, e);
                                    }
                                } else if ("names".equals(context.currentArrayName)) {
                                    if (!(value instanceof String)) {
                                        throw new RuntimeException("Couldn't process value class: " + value.getClass());
                                    }
                                    context.providerNames.add(String.valueOf(value));
                                } else {
                                    logger.error("unexpected array name: " + context.currentArrayName);
                                }
                            }
                        }

                        @Override
                        public AnnotationVisitor visitAnnotation(String name, String desc) {
                            return emptyVisitor;
                        }

                        @Override
                        public AnnotationVisitor visitArray(String name) {
                            context.multiProvider = true;
                            context.currentArrayName = name;
                            return this;
                        }

                        @Override
                        public void visitEnd() {
                            if (!context.multiProvider) {
                                Class<?> serviceInterface = context.serviceInterface;
                                String providerName = context.providerName == null ? ServiceProviderInfo.DEFAULT_PROVIDER : context.providerName;
                                ServiceProviderInfo providerInfo = new ServiceProviderInfo(serviceInterface, providerName);
                                providerInfo.setTestServiceProvider(context.testImpl);
                                registerServiceProvider(providerMap, providerInfo, context.providerClass);
                            } else {
                                for (int i = 0; i < context.serviceInterfaces.size(); i++) {
                                    Class<?> serviceInterface = context.serviceInterfaces.get(i);
                                    String providerName = context.providerNames.get(i) == null ? ServiceProviderInfo.DEFAULT_PROVIDER : context.providerNames.get(i);
                                    ServiceProviderInfo providerInfo = new ServiceProviderInfo(serviceInterface, providerName);
                                    registerServiceProvider(providerMap, providerInfo, context.providerClass);
                                }
                            }
                        }

                        @Override
                        public void visitEnum(String name, String desc, String value) {
                        }
                    };
                }
            }
        }, true);
    }

    protected void registerServiceProvider(Map<ServiceProviderInfo, Class<?>> providerMap, ServiceProviderInfo providerInfo, Class<?> providerClass) {
        Class<?> currentProvider = providerMap.get(providerInfo);
        if (currentProvider == null) {
            logger.info("binding service {} to provider {}", providerInfo, providerClass);
            providerMap.put(providerInfo, providerClass);
        } else if (!currentProvider.equals(providerClass)) {
            logger.warn("Found conflicting service provider definitions for service {}: {} and {}, using {}", new Object[] { providerInfo, currentProvider, providerClass, currentProvider });
        }
    }
}
