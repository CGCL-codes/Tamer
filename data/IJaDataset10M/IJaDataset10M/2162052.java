package org.antlride.internal.core.dltk.parser;

import org.antlride.core.CorePlugin;
import org.antlride.core.model.Grammar;
import org.antlride.core.model.GrammarFactory;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.parser.AbstractSourceParser;
import org.eclipse.dltk.ast.parser.IModuleDeclaration;
import org.eclipse.dltk.ast.parser.ISourceParser;
import org.eclipse.dltk.compiler.env.IModuleSource;
import org.eclipse.dltk.compiler.problem.IProblemReporter;

/**
 * The {@link ISourceParser ANTLR implementation}. Required by DLTK.
 * 
 * @author Edgar Espina
 * @since 2.1.0
 */
public class PrivateSourceParser extends AbstractSourceParser {

    PrivateSourceParser() {
    }

    /**
   * {@inheritDoc}
   */
    @Override
    public IModuleDeclaration parse(IModuleSource moduleSource, IProblemReporter reporter) {
        try {
            String source = moduleSource.getSourceContents();
            Grammar grammar = GrammarFactory.newGrammar(source);
            return new ASTFactory(source.length()).accept(grammar);
        } catch (Exception ex) {
            CorePlugin.getInstance().error(ex.getCause(), "Invalid source: %s", moduleSource.getFileName());
            return new ModuleDeclaration(0);
        }
    }
}
