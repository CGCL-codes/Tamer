package org.deved.antlride.core.model;

import org.eclipse.core.runtime.IPath;
import org.eclipse.dltk.compiler.problem.IProblemReporter;

public interface IGrammarBuilder {

    IGrammar process(String content);

    void setFile(IPath file);

    void setProblemReporter(IProblemReporter problemReporter);

    void setProject(String project);
}
