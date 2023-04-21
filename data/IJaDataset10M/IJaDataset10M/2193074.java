package net.stickycode.mockwire.guice2;

import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.MembersInjector;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import net.stickycode.mockwire.Controlled;
import net.stickycode.mockwire.IsolatedTestManifest;
import net.stickycode.mockwire.MissingBeanException;
import net.stickycode.mockwire.UnderTest;
import net.stickycode.reflector.AnnotatedFieldSettingProcessor;
import net.stickycode.reflector.Reflector;

public class GuiceIsolatedTestManifest implements IsolatedTestManifest {

    private Logger log = LoggerFactory.getLogger(getClass());

    public static class Bean {

        private Object instance;

        private Class<?> type;

        private String name;

        public Bean(String name, Object bean, Class<?> type) {
            this.instance = bean;
            this.type = type;
            this.name = name;
        }

        public Class<?> getType() {
            return type;
        }

        public Object getInstance() {
            return instance;
        }

        public String getName() {
            return name;
        }
    }

    public static class Manifest {

        private List<Class<?>> types = new LinkedList<Class<?>>();

        private List<Bean> beans = new LinkedList<Bean>();

        public boolean hasRegisteredType(Class<?> type) {
            for (Class<?> t : types) if (t.isAssignableFrom(type)) return true;
            for (Bean b : beans) if (b.getType().isAssignableFrom(type)) return true;
            return false;
        }

        public void register(String beanName, Object bean, Class<?> type) {
            beans.add(new Bean(beanName, bean, type));
        }

        public void register(String beanName, Class<?> type) {
            types.add(type);
        }

        public List<Class<?>> getTypes() {
            return types;
        }

        public List<Bean> getBeans() {
            return beans;
        }

        public Object getBean(Class<?> type) {
            for (Bean b : beans) {
                if (b.getType().equals(type)) return b.getInstance();
            }
            return null;
        }
    }

    private Manifest manifest = new Manifest();

    private Injector injector;

    public class IsolatedTestModule extends AbstractModule {

        private Manifest manifest;

        private Class<?> testClass;

        public IsolatedTestModule(Class<?> testClass, Manifest manifest) {
            this.manifest = manifest;
            this.testClass = testClass;
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        protected void configure() {
            bindListener(Matchers.any(), new TypeListener() {

                @Override
                public <I> void hear(TypeLiteral<I> type, final TypeEncounter<I> encounter) {
                    Class<? super I> rawType = type.getRawType();
                    if (rawType.equals(testClass)) {
                        final Values valueCollector = new Values(encounter);
                        new Reflector().forEachField(valueCollector).process(rawType);
                        encounter.register(new MembersInjector<Object>() {

                            @Override
                            public void injectMembers(Object instance) {
                                new Reflector().forEachField(new AnnotatedFieldSettingProcessor(valueCollector, UnderTest.class, Controlled.class)).process(instance);
                            }
                        });
                    }
                }
            });
            for (final Bean b : manifest.getBeans()) {
                TypeLiteral type = TypeLiteral.get(b.getType());
                log.debug("binding type {} to instance {}", type, b.getInstance());
                bind(type).toInstance(b.getInstance());
            }
            for (Class<?> type : manifest.getTypes()) {
                log.debug("binding type {}", type);
                bind(type);
            }
        }
    }

    public GuiceIsolatedTestManifest() {
        manifest.register("manifest", this, IsolatedTestManifest.class);
    }

    @Override
    public boolean hasRegisteredType(Class<?> type) {
        return manifest.hasRegisteredType(type);
    }

    @Override
    public void registerBean(String beanName, Object bean, Class<?> type) {
        log.debug("register bean {} to instance of {}", beanName, type.getName());
        manifest.register(beanName, bean, type);
    }

    @Override
    public void registerType(String beanName, Class<?> type) {
        log.debug("register name {} to type {}", beanName, type.getName());
        manifest.register(beanName, type);
    }

    @Override
    public Object getBeanOfType(Class<?> type) {
        return manifest.getBean(type);
    }

    @Override
    public void scanPackages(String[] scanRoots) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void registerConfigurationSystem(String name, Object configurationSystem, Class<?> type) {
        manifest.register(name, configurationSystem, type);
    }

    @Override
    public void prepareTest(Object testInstance) throws MissingBeanException {
        injector = Guice.createInjector(Stage.PRODUCTION, new IsolatedTestModule(testClass, manifest));
        injector.injectMembers(testInstance);
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void startup(Class<?> testClass) {
    }
}
