package ru.adv.mozart.controller;

import groovy.lang.GroovyClassLoader;
import java.util.Arrays;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import ru.adv.mozart.controller.cache.InstanceCreator;
import ru.adv.mozart.controller.cache.MemoCache;

/**
 * 
 * @author vic
 */
public class ControllerFactoryImpl implements InitializingBean, ControllerFactory {

    private static final String DOT = ".";

    private SourceFactory sourceFactory;

    private MemoCache<ScriptExecutor> scriptCache = new MemoCache<ScriptExecutor>();

    private MemoCache<FunctionExecutor> functionCache = new MemoCache<FunctionExecutor>();

    private MemoCache<MethodExecutor> methodCache = new MemoCache<MethodExecutor>();

    public SourceFactory getSourceFactory() {
        return sourceFactory;
    }

    public void setSourceFactory(SourceFactory sourceFactory) {
        this.sourceFactory = sourceFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(getSourceFactory(), "Property sourceFactory is null");
    }

    /**
	 * Create GroovyClassLoader that compiles Groovy classes
	 * in 'classes' directory
	 */
    @Override
    public ClassLoader create(ClassLoader parentClassLoader) {
        if (getSourceFactory() instanceof FileSourceFactory) {
            CompilerConfiguration cfg = new CompilerConfiguration();
            final String classpath = ((FileSourceFactory) getSourceFactory()).getSourceDir() + "/classes";
            cfg.setClasspath(classpath);
            cfg.setSourceEncoding("UTF-8");
            cfg.setRecompileGroovySource(true);
            return new GroovyClassLoader(parentClassLoader, cfg, true);
        }
        return parentClassLoader;
    }

    public ExecutorContext getScriptExecutorContext(final String name) throws ControllerNotFoundException {
        ScriptExecutor scriptExecutor = scriptCache.get(name, new InstanceCreator<ScriptExecutor>() {

            @Override
            public ScriptExecutor create() {
                Source source = getSourceFactory().lookUp(name);
                return new ScriptExecutor(source, ControllerFactoryImpl.this);
            }
        });
        return new ExecutorContext(scriptExecutor);
    }

    public ExecutorContext getFunctionExecutorContext(String name) {
        String[] tokenz = StringUtils.tokenizeToStringArray(name, DOT);
        if (tokenz.length < 2) {
            throw new ControllerInitializationException("Bad function type cotroller name: " + name);
        }
        String funcName = tokenz[tokenz.length - 1];
        final String srcName = StringUtils.arrayToDelimitedString(Arrays.copyOfRange(tokenz, 0, tokenz.length - 1), DOT);
        FunctionExecutor functionExecutor = functionCache.get(srcName, new InstanceCreator<FunctionExecutor>() {

            @Override
            public FunctionExecutor create() {
                Source source = getSourceFactory().lookUp(srcName);
                return new FunctionExecutor(source, ControllerFactoryImpl.this);
            }
        });
        return new ExecutorContext(functionExecutor, funcName);
    }

    public ExecutorContext getMethodExecutorContext(String name) {
        String[] tokenz = StringUtils.tokenizeToStringArray(name, DOT);
        if (tokenz.length < 3) {
            throw new ControllerInitializationException("Bad method type controller name: " + name);
        }
        String methodName = tokenz[tokenz.length - 1];
        String instanceName = tokenz[tokenz.length - 2];
        final String srcName = StringUtils.arrayToDelimitedString(Arrays.copyOfRange(tokenz, 0, tokenz.length - 2), DOT);
        MethodExecutor methodExecutor = methodCache.get(srcName, new InstanceCreator<MethodExecutor>() {

            @Override
            public MethodExecutor create() {
                Source source = getSourceFactory().lookUp(srcName);
                return new MethodExecutor(source, ControllerFactoryImpl.this);
            }
        });
        return new ExecutorContext(methodExecutor, instanceName, methodName);
    }
}
