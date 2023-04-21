package com.stateofflow.eclipse.tane.hidedelegate.model.rewrite;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTRequestor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import com.stateofflow.eclipse.tane.Activator;
import com.stateofflow.eclipse.tane.rewrite.visibility.VisibilityChangeException;
import com.stateofflow.eclipse.tane.util.ast.Parser;

public class RewriteMapBuilder {

    private final RefactoringStatus status;

    public RewriteMapBuilder(final RefactoringStatus status) {
        this.status = status;
    }

    public RewriteMap build(final Map<ICompilationUnit, Set<SearchMatch>> chainOriginReferencesByCompilationUnit, final Map<ICompilationUnit, Set<SearchMatch>> potentialVisibilityUpdatesByCompilationUnit, final Refactor refactor, final SubMonitor progressMonitor) throws CoreException {
        final RewriteMap rewrites = new RewriteMap();
        final CompilationUnitRewriteBuilder compilationUnitRewriteBuilder = new CompilationUnitRewriteBuilder(chainOriginReferencesByCompilationUnit, potentialVisibilityUpdatesByCompilationUnit, refactor);
        new Parser().parse(getAllCompilationUnits(chainOriginReferencesByCompilationUnit, potentialVisibilityUpdatesByCompilationUnit), new ASTRequestor() {

            @Override
            public void acceptAST(final ICompilationUnit source, final CompilationUnit ast) {
                addRewriteForCompilationUnit(rewrites, source, ast, compilationUnitRewriteBuilder);
            }
        }, progressMonitor);
        refactor.writeNewMethod(rewrites);
        return rewrites;
    }

    private Set<ICompilationUnit> getAllCompilationUnits(final Map<ICompilationUnit, Set<SearchMatch>> chainOriginReferencesByCompilationUnit, final Map<ICompilationUnit, Set<SearchMatch>> potentialVisibilityUpdatesByCompilationUnit) {
        final Set<ICompilationUnit> compilationUnits = new HashSet<ICompilationUnit>();
        compilationUnits.addAll(chainOriginReferencesByCompilationUnit.keySet());
        compilationUnits.addAll(potentialVisibilityUpdatesByCompilationUnit.keySet());
        return compilationUnits;
    }

    private void addRewriteForCompilationUnit(final RewriteMap rewrites, final ICompilationUnit source, final CompilationUnit ast, final CompilationUnitRewriteBuilder compilationUnitRewriteBuilder) {
        try {
            rewrites.put(source, compilationUnitRewriteBuilder.createRewrite(source, ast));
        } catch (final CoreException e) {
            status.addFatalError("A problem occurred. Please examine the logs and report a bug if necessary." + e);
            Activator.getDefault().getLog().log(e.getStatus());
        } catch (final VisibilityChangeException e) {
            status.addFatalError("Cannot change the visibility of one or more members of types in " + source.getElementName());
        }
    }
}
