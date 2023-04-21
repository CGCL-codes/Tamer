package hu.gbalage.dml;

import org.eclipse.xtext.Constants;
import org.eclipse.xtext.service.DefaultRuntimeModule;
import com.google.inject.Binder;
import com.google.inject.name.Names;

/**
 * Manual modifications go to {hu.gbalage.dml.DMLRuntimeModule}
 */
public abstract class AbstractDMLRuntimeModule extends DefaultRuntimeModule {

    @Override
    public void configure(Binder binder) {
        super.configure(binder);
        binder.bind(String.class).annotatedWith(Names.named(Constants.LANGUAGE_NAME)).toInstance("hu.gbalage.dml.DML");
    }

    public Class<? extends org.eclipse.xtext.IGrammarAccess> bindIGrammarAccess() {
        return hu.gbalage.dml.services.DMLGrammarAccess.class;
    }

    public Class<? extends org.eclipse.xtext.parsetree.reconstr.IParseTreeConstructor> bindIParseTreeConstructor() {
        return hu.gbalage.dml.parseTreeConstruction.DMLParsetreeConstructor.class;
    }

    public Class<? extends org.eclipse.xtext.parser.antlr.IAntlrParser> bindIAntlrParser() {
        return hu.gbalage.dml.parser.antlr.DMLParser.class;
    }

    public Class<? extends org.eclipse.xtext.parser.ITokenToStringConverter> bindITokenToStringConverter() {
        return org.eclipse.xtext.parser.antlr.AntlrTokenToStringConverter.class;
    }

    public Class<? extends org.eclipse.xtext.parser.antlr.IAntlrTokenFileProvider> bindIAntlrTokenFileProvider() {
        return hu.gbalage.dml.parser.antlr.DMLAntlrTokenFileProvider.class;
    }

    public Class<? extends org.eclipse.xtext.parser.antlr.Lexer> bindLexer() {
        return hu.gbalage.dml.parser.antlr.internal.InternalDMLLexer.class;
    }

    public Class<? extends org.eclipse.xtext.parser.antlr.ITokenDefProvider> bindITokenDefProvider() {
        return org.eclipse.xtext.parser.antlr.AntlrTokenDefProvider.class;
    }

    @org.eclipse.xtext.service.SingletonBinding(eager = true)
    public Class<? extends hu.gbalage.dml.validation.DMLJavaValidator> bindDMLJavaValidator() {
        return hu.gbalage.dml.validation.DMLJavaValidator.class;
    }

    public Class<? extends org.eclipse.xtext.scoping.IScopeProvider> bindIScopeProvider() {
        return hu.gbalage.dml.scoping.DMLScopeProvider.class;
    }

    public Class<? extends org.eclipse.xtext.formatting.IFormatter> bindIFormatter() {
        return hu.gbalage.dml.formatting.DMLFormatter.class;
    }
}
