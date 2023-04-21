package com.google.inject.tools.suite;

import com.google.inject.AbstractModule;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.tools.suite.code.CodeRunnerModule;
import com.google.inject.tools.suite.module.ModuleManagerModule;

/**
 * The guice module controlling the tools suite.
 * 
 * The general pattern is:
 * 
 * <code>protected abstract void bindFoo(AnnotatedBindingBuilder<Foo> bindFoo)</code>
 * 
 * should be implemented as:
 * 
 * <code>void bindFoo(AnnotatedBindingBuilder<Foo> bindFoo) {
 *   bindFoo.to(FooImpl.class);
 * }</code>
 * 
 * @author Darren Creutz (dcreutz@gmail.com)
 */
public abstract class GuiceToolsModule extends AbstractModule {

    @Override
    protected void configure() {
        install(codeRunnerModule());
        install(moduleManagerModule());
        bindProblemsHandler(bind(ProblemsHandler.class));
        bindMessenger(bind(Messenger.class));
        bindJavaManager(bind(JavaManager.class));
        bindProgressHandler(bind(ProgressHandler.class));
        bindSettings(bind(Settings.class));
    }

    protected CodeRunnerModule codeRunnerModule() {
        return new CodeRunnerModule();
    }

    protected ModuleManagerModule moduleManagerModule() {
        return new ModuleManagerModule();
    }

    protected void bindProblemsHandler(AnnotatedBindingBuilder<ProblemsHandler> bindProblemsHandler) {
        bindProblemsHandler.to(DefaultProblemsHandler.class);
    }

    protected void bindMessenger(AnnotatedBindingBuilder<Messenger> bindMessenger) {
        bindMessenger.to(DefaultMessenger.class);
    }

    protected void bindJavaManager(AnnotatedBindingBuilder<JavaManager> bindJavaManager) {
        bindJavaManager.to(DefaultJavaManager.class);
    }

    protected void bindProgressHandler(AnnotatedBindingBuilder<ProgressHandler> bindProgressHandler) {
        bindProgressHandler.to(BlockingProgressHandler.class);
    }

    protected void bindSettings(AnnotatedBindingBuilder<Settings> bindSettings) {
        bindSettings.to(DefaultSettings.class);
    }
}
