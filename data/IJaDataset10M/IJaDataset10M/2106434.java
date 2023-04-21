package edu.cmu.cs.planno;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.rewrite.ImportRewrite;
import org.eclipse.jdt.ui.CodeStyleConfiguration;
import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.text.java.ClasspathFixProcessor;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickFixProcessor;
import org.eclipse.jdt.ui.text.java.ClasspathFixProcessor.ClasspathFixProposal;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.CompositeChange;
import org.eclipse.ltk.core.refactoring.PerformChangeOperation;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.TextFileChange;
import org.eclipse.ltk.ui.refactoring.RefactoringUI;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.PlatformUI;

/**
 * This is the code that actually adds a line to the quickfix menu asking you if you
 * want to add Plaid Annotations to the build path. It partially uses PlaidClasspathFix
 * in order to perform the change, but it doesn't totally... which is odd. If you are
 * adding a new annotation, you should definitely see that class, since it has to keep
 * a set of all the things it knows about.
 * 
 * @author Nels E. Beckman
 * @date Dec 13, 2008
 * @see {@link edu.cmu.cs.planno.PlaidClasspathFix}
 */
public class PlaidQuickFixProcessor implements IQuickFixProcessor {

    public IJavaCompletionProposal[] getCorrections(IInvocationContext context, IProblemLocation[] locations) throws CoreException {
        List<IJavaCompletionProposal> result = new ArrayList<IJavaCompletionProposal>();
        ICompilationUnit unit = context.getCompilationUnit();
        for (IProblemLocation problem_loc : locations) {
            String s = unit.getBuffer().getText(problem_loc.getOffset(), problem_loc.getLength());
            if (!PlaidClasspathFix.isProbablyPlaidAnno(s)) continue;
            String qualified = PlaidClasspathFix.bestGuessQualifiedName(s);
            final IJavaProject java_project = unit.getJavaProject();
            if (java_project.findType(qualified) != null) return new IJavaCompletionProposal[] {};
            ClasspathFixProposal[] class_path_fixes = ClasspathFixProcessor.getContributedFixImportProposals(java_project, s, null);
            for (ClasspathFixProposal class_path_fix : class_path_fixes) {
                ImportRewrite imports = getImportRewrite(context.getASTRoot(), qualified);
                result.add(new AddPlaidToPathProposal(class_path_fix, java_project, imports));
            }
        }
        return result.toArray(new IJavaCompletionProposal[result.size()]);
    }

    private ImportRewrite getImportRewrite(CompilationUnit astRoot, String typeToImport) {
        if (typeToImport != null) {
            ImportRewrite importRewrite = CodeStyleConfiguration.createImportRewrite(astRoot, true);
            importRewrite.addImport(typeToImport);
            return importRewrite;
        }
        return null;
    }

    public boolean hasCorrections(ICompilationUnit unit, int problemId) {
        return problemId == IProblem.UndefinedType;
    }

    private class AddPlaidToPathProposal implements IJavaCompletionProposal {

        private final ClasspathFixProposal classPathFix;

        private final IJavaProject project;

        private final ImportRewrite importRewrite;

        public AddPlaidToPathProposal(ClasspathFixProposal class_path_fix, IJavaProject javaProject, ImportRewrite imports) {
            this.classPathFix = class_path_fix;
            this.project = javaProject;
            this.importRewrite = imports;
        }

        public int getRelevance() {
            return classPathFix.getRelevance();
        }

        private Change createChange() throws CoreException {
            Change change = classPathFix.createChange(null);
            if (importRewrite != null) {
                TextFileChange cuChange = new TextFileChange("Add import", (IFile) importRewrite.getCompilationUnit().getResource());
                cuChange.setEdit(importRewrite.rewriteImports(null));
                CompositeChange composite = new CompositeChange(getDisplayString());
                composite.add(change);
                composite.add(cuChange);
                return composite;
            }
            return change;
        }

        public void apply(IDocument document) {
            try {
                PlatformUI.getWorkbench().getActiveWorkbenchWindow().run(false, true, new IRunnableWithProgress() {

                    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                        try {
                            Change change = createChange();
                            change.initializeValidationData(new NullProgressMonitor());
                            PerformChangeOperation op = RefactoringUI.createUIAwareChangeOperation(change);
                            op.setUndoManager(RefactoringCore.getUndoManager(), getDisplayString());
                            op.setSchedulingRule(project.getProject().getWorkspace().getRoot());
                            op.run(monitor);
                        } catch (CoreException e) {
                            throw new InvocationTargetException(e);
                        } catch (OperationCanceledException e) {
                            throw new InterruptedException();
                        }
                    }
                });
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public String getAdditionalProposalInfo() {
            return "This QuickFix will add the Plaid annotations library to your build path, which " + "is important if you are using a Crystal analysis that uses annotations.";
        }

        public IContextInformation getContextInformation() {
            return null;
        }

        public String getDisplayString() {
            return "Add Plaid Annotations to Buildpath";
        }

        public Image getImage() {
            return JavaUI.getSharedImages().getImage(ISharedImages.IMG_OBJS_LIBRARY);
        }

        public Point getSelection(IDocument document) {
            return null;
        }
    }
}
