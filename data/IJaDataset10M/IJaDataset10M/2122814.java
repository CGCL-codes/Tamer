package org.xtext.example;

import org.eclipse.xtext.Constants;
import org.eclipse.xtext.service.DefaultRuntimeModule;
import com.google.inject.Binder;
import com.google.inject.name.Names;

/**
 * Manual modifications go to {org.xtext.example.EntitiesRuntimeModule}
 */
public abstract class AbstractEntitiesRuntimeModule extends DefaultRuntimeModule {

    @Override
    public void configure(Binder binder) {
        super.configure(binder);
        binder.bind(String.class).annotatedWith(Names.named(Constants.LANGUAGE_NAME)).toInstance("org.xtext.example.Entities");
        bindProperties(binder);
    }

    protected void bindProperties(Binder binder) {
        bindProperties(binder, "/org/xtext/example/Entities.properties");
    }

    public Class<? extends org.eclipse.xtext.IGrammarAccess> bindIGrammarAccess() {
        return org.xtext.example.services.EntitiesGrammarAccess.class;
    }

    public Class<? extends org.eclipse.xtext.parsetree.reconstr.IParseTreeConstructor> bindIParseTreeConstructor() {
        return org.xtext.example.parseTreeConstruction.EntitiesParsetreeConstructor.class;
    }

    public Class<? extends org.eclipse.xtext.parser.antlr.IAntlrParser> bindIAntlrParser() {
        return org.xtext.example.parser.antlr.EntitiesParser.class;
    }

    public Class<? extends org.eclipse.xtext.parser.ITokenToStringConverter> bindITokenToStringConverter() {
        return org.eclipse.xtext.parser.antlr.AntlrTokenToStringConverter.class;
    }

    public Class<? extends org.eclipse.xtext.parser.antlr.IAntlrTokenFileProvider> bindIAntlrTokenFileProvider() {
        return org.xtext.example.parser.antlr.EntitiesAntlrTokenFileProvider.class;
    }

    public Class<? extends org.eclipse.xtext.parser.antlr.Lexer> bindLexer() {
        return org.xtext.example.parser.antlr.internal.InternalEntitiesLexer.class;
    }

    public com.google.inject.Provider<org.xtext.example.parser.antlr.internal.InternalEntitiesLexer> provideInternalEntitiesLexer() {
        return org.eclipse.xtext.parser.antlr.LexerProvider.create(org.xtext.example.parser.antlr.internal.InternalEntitiesLexer.class);
    }

    public void configureRuntimeLexer(com.google.inject.Binder binder) {
        binder.bind(org.eclipse.xtext.parser.antlr.Lexer.class).annotatedWith(com.google.inject.name.Names.named(org.eclipse.xtext.parser.antlr.LexerBindings.RUNTIME)).to(org.xtext.example.parser.antlr.internal.InternalEntitiesLexer.class);
    }

    public Class<? extends org.eclipse.xtext.parser.antlr.ITokenDefProvider> bindITokenDefProvider() {
        return org.eclipse.xtext.parser.antlr.AntlrTokenDefProvider.class;
    }

    @org.eclipse.xtext.service.SingletonBinding(eager = true)
    public Class<? extends org.xtext.example.validation.EntitiesJavaValidator> bindEntitiesJavaValidator() {
        return org.xtext.example.validation.EntitiesJavaValidator.class;
    }

    public Class<? extends org.eclipse.xtext.scoping.IScopeProvider> bindIScopeProvider() {
        return org.xtext.example.scoping.EntitiesScopeProvider.class;
    }

    public void configureIScopeProviderDelegate(com.google.inject.Binder binder) {
        binder.bind(org.eclipse.xtext.scoping.IScopeProvider.class).annotatedWith(com.google.inject.name.Names.named("org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider.delegate")).to(org.eclipse.xtext.scoping.impl.SimpleNameScopeProvider.class);
    }

    public Class<? extends org.eclipse.xtext.formatting.IFormatter> bindIFormatter() {
        return org.xtext.example.formatting.EntitiesFormatter.class;
    }
}
