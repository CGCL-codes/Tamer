package org.deved.antlride.internal.core.model;

import org.deved.antlride.core.model.IGrammarBuilder;
import org.deved.antlride.internal.core.parser.AntlrGrammarBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.dltk.compiler.problem.IProblemReporter;

public final class GrammarRepository {

    private static GrammarRepository instance;

    public IGrammarBuilder createGrammarBuilder(IPath file, IProblemReporter reporter) throws CoreException {
        String projectName = file.segment(0);
        IGrammarBuilder grammarBuilder = new AntlrGrammarBuilder();
        grammarBuilder.setProblemReporter(reporter);
        grammarBuilder.setFile(file);
        grammarBuilder.setProject(projectName);
        return grammarBuilder;
    }

    public IGrammarBuilder createGrammarBuilder() throws CoreException {
        IGrammarBuilder grammarBuilder = new AntlrGrammarBuilder();
        return grammarBuilder;
    }

    private GrammarRepository() {
    }

    public static GrammarRepository getInstance() {
        if (instance == null) {
            instance = new GrammarRepository();
        }
        return instance;
    }
}
