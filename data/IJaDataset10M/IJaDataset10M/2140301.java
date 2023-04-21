package net.sf.smbt.scripts.thingm;

import org.eclipse.xtext.Constants;
import org.eclipse.xtext.service.DefaultRuntimeModule;
import com.google.inject.Binder;
import com.google.inject.name.Names;

/**
 * Manual modifications go to {net.sf.smbt.scripts.thingm.dslRuntimeModule}
 */
public abstract class AbstractdslRuntimeModule extends DefaultRuntimeModule {

    @Override
    public void configure(Binder binder) {
        super.configure(binder);
        binder.bind(String.class).annotatedWith(Names.named(Constants.LANGUAGE_NAME)).toInstance("net.sf.smbt.scripts.thingm.dsl");
    }

    public Class<? extends org.eclipse.xtext.IGrammarAccess> bindIGrammarAccess() {
        return net.sf.smbt.scripts.thingm.services.dslGrammarAccess.class;
    }

    public Class<? extends org.eclipse.xtext.parsetree.reconstr.IParseTreeConstructor> bindIParseTreeConstructor() {
        return net.sf.smbt.scripts.thingm.parseTreeConstruction.dslParsetreeConstructor.class;
    }

    public Class<? extends org.eclipse.xtext.parser.antlr.IAntlrParser> bindIAntlrParser() {
        return net.sf.smbt.scripts.thingm.parser.antlr.dslParser.class;
    }

    public Class<? extends org.eclipse.xtext.parser.ITokenToStringConverter> bindITokenToStringConverter() {
        return org.eclipse.xtext.parser.antlr.AntlrTokenToStringConverter.class;
    }

    public Class<? extends org.eclipse.xtext.parser.antlr.IAntlrTokenFileProvider> bindIAntlrTokenFileProvider() {
        return net.sf.smbt.scripts.thingm.parser.antlr.dslAntlrTokenFileProvider.class;
    }

    public Class<? extends org.eclipse.xtext.parser.antlr.Lexer> bindLexer() {
        return net.sf.smbt.scripts.thingm.parser.antlr.internal.InternaldslLexer.class;
    }

    public Class<? extends org.eclipse.xtext.parser.antlr.ITokenDefProvider> bindITokenDefProvider() {
        return org.eclipse.xtext.parser.antlr.AntlrTokenDefProvider.class;
    }

    @org.eclipse.xtext.service.SingletonBinding(eager = true)
    public Class<? extends net.sf.smbt.scripts.thingm.validation.dslJavaValidator> bind() {
        return net.sf.smbt.scripts.thingm.validation.dslJavaValidator.class;
    }

    public Class<? extends org.eclipse.xtext.scoping.IScopeProvider> bindIScopeProvider() {
        return net.sf.smbt.scripts.thingm.scoping.dslScopeProvider.class;
    }

    public Class<? extends org.eclipse.xtext.formatting.IFormatter> bindIFormatter() {
        return net.sf.smbt.scripts.thingm.formatting.dslFormatter.class;
    }
}
