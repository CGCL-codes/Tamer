package com.google.inject.tools.suite;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import com.google.inject.Inject;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.tools.suite.GuiceToolsModule;
import com.google.inject.tools.suite.JavaManager;
import com.google.inject.tools.suite.Messenger;
import com.google.inject.tools.suite.ProblemsHandler;
import com.google.inject.tools.suite.code.CodeRunner;
import com.google.inject.tools.suite.code.CodeRunnerFactory;
import com.google.inject.tools.suite.code.CodeRunnerModule;
import com.google.inject.tools.suite.module.ModuleManager;
import com.google.inject.tools.suite.module.ModuleManagerFactory;
import com.google.inject.tools.suite.module.ModuleManagerModule;

/**
 * Implementation of the {@link GuiceToolsModule} that injects mock objects.
 * 
 * {@inheritDoc GuiceToolsModule}
 * 
 * @author Darren Creutz (dcreutz@gmail.com)
 */
public class MockingGuiceToolsModule extends GuiceToolsModule {

    private boolean useRealModuleManager = false;

    private ModuleManager moduleManager = null;

    private Messenger messenger = null;

    private CodeRunner codeRunner = null;

    private ProblemsHandler problemsHandler = null;

    public MockingGuiceToolsModule useRealModuleManager() {
        useRealModuleManager = true;
        return this;
    }

    public MockingGuiceToolsModule useModuleManager(ModuleManager moduleManager) {
        this.moduleManager = moduleManager;
        return this;
    }

    public MockingGuiceToolsModule useProblemsHandler(ProblemsHandler problemsHandler) {
        this.problemsHandler = problemsHandler;
        return this;
    }

    public MockingGuiceToolsModule useMessenger(Messenger messenger) {
        this.messenger = messenger;
        return this;
    }

    public MockingGuiceToolsModule useCodeRunner(CodeRunner codeRunner) {
        this.codeRunner = codeRunner;
        return this;
    }

    class MockingModuleManagerModule extends ModuleManagerModule {

        @Override
        protected void bindModuleManagerFactory(AnnotatedBindingBuilder<ModuleManagerFactory> builder) {
            if (useRealModuleManager) {
                super.bindModuleManagerFactory(builder);
            } else if (moduleManager != null) {
                builder.toInstance(new ModuleManagerInstanceFactory(moduleManager));
            } else {
                builder.to(ModuleManagerMockFactory.class);
            }
        }

        @Override
        protected void bindModuleManager(AnnotatedBindingBuilder<ModuleManager> builder) {
            if (moduleManager != null) {
                bindToInstance(builder, moduleManager);
            } else if (useRealModuleManager) {
                super.bindModuleManager(builder);
            } else {
                bindToMockInstance(builder, ModuleManager.class);
            }
        }
    }

    @Override
    protected ModuleManagerModule moduleManagerModule() {
        return new MockingModuleManagerModule();
    }

    @Override
    protected void bindProblemsHandler(AnnotatedBindingBuilder<ProblemsHandler> builder) {
        if (problemsHandler != null) {
            bindToInstance(builder, problemsHandler);
        } else {
            bindToMockInstance(builder, ProblemsHandler.class);
        }
    }

    @Override
    protected void bindMessenger(AnnotatedBindingBuilder<Messenger> builder) {
        if (messenger != null) {
            bindToInstance(builder, messenger);
        } else {
            bindToMockInstance(builder, Messenger.class);
        }
    }

    class MockingCodeRunnerModule extends CodeRunnerModule {

        @Override
        protected void bindCodeRunnerFactory(AnnotatedBindingBuilder<CodeRunnerFactory> builder) {
            if (codeRunner != null) {
                builder.toInstance(new CodeRunnerInstanceFactory(codeRunner));
            } else {
                builder.to(CodeRunnerMockFactory.class);
            }
        }

        @Override
        protected void bindCodeRunner(AnnotatedBindingBuilder<CodeRunner> bindCodeRunner) {
            if (codeRunner != null) {
                bindToInstance(bindCodeRunner, codeRunner);
            } else {
                bindToMockInstance(bindCodeRunner, CodeRunner.class);
            }
        }
    }

    @Override
    protected CodeRunnerModule codeRunnerModule() {
        return new MockingCodeRunnerModule();
    }

    protected <T> void bindToMockInstance(AnnotatedBindingBuilder<T> builder, Class<T> theClass) {
        builder.toInstance(new ProxyMock<T>(theClass).getInstance());
    }

    protected <T> void bindToInstance(AnnotatedBindingBuilder<T> builder, T instance) {
        builder.toInstance(instance);
    }

    public static class ProxyException extends RuntimeException {

        private static final long serialVersionUID = -7983728368389046669L;

        private final Object proxy;

        private final Method method;

        public ProxyException(Object proxy, Method method, Object[] args) {
            this.proxy = proxy;
            this.method = method;
        }

        @Override
        public String toString() {
            return "Proxy exception: method " + method.toString() + " called on proxy " + proxy.toString();
        }
    }

    public static class ProxyMock<T> implements InvocationHandler {

        private final Class<T> theClass;

        public ProxyMock(Class<T> theClass) {
            this.theClass = theClass;
        }

        @SuppressWarnings({ "unchecked" })
        public T getInstance() {
            Class<?>[] interfaces;
            if (theClass.isInterface()) {
                interfaces = new Class<?>[theClass.getInterfaces().length + 1];
                interfaces[0] = theClass;
                for (int i = 0; i < theClass.getInterfaces().length; i++) {
                    interfaces[i + 1] = theClass.getInterfaces()[i];
                }
            } else {
                interfaces = theClass.getInterfaces();
            }
            return (T) Proxy.newProxyInstance(theClass.getClassLoader(), interfaces, this);
        }

        public Object invoke(Object proxy, Method method, Object[] args) {
            try {
                return method.getReturnType().newInstance();
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class CodeRunnerInstanceFactory implements CodeRunnerFactory {

        private final CodeRunner instance;

        public CodeRunnerInstanceFactory(CodeRunner instance) {
            this.instance = instance;
        }

        public CodeRunner create(JavaManager project) {
            return instance;
        }

        public CodeRunner get() {
            return instance;
        }
    }

    public static class CodeRunnerMockFactory extends CodeRunnerInstanceFactory {

        @Inject
        public CodeRunnerMockFactory() {
            super(new ProxyMock<CodeRunner>(CodeRunner.class).getInstance());
        }
    }

    public static class ModuleManagerInstanceFactory implements ModuleManagerFactory {

        private final ModuleManager instance;

        public ModuleManagerInstanceFactory(ModuleManager instance) {
            this.instance = instance;
        }

        public ModuleManager create(JavaManager project, Settings setting) {
            return instance;
        }

        public ModuleManager create(JavaManager project) {
            return instance;
        }

        public ModuleManager get() {
            return instance;
        }
    }

    public static class ModuleManagerMockFactory extends ModuleManagerInstanceFactory {

        @Inject
        public ModuleManagerMockFactory() {
            super(new ProxyMock<ModuleManager>(ModuleManager.class).getInstance());
        }
    }
}
