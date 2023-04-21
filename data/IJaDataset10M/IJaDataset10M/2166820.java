package com.ilog.translator.java2cs.translation.astrewriter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RenameRefactoring;
import org.eclipse.text.edits.TextEditGroup;
import com.ilog.translator.java2cs.translation.ITranslationContext;
import com.ilog.translator.java2cs.translation.astrewriter.astchange.ASTChangeVisitor;

/**
 * 
 * @author afau
 * 
 */
public class VariableVisibilityVisitor extends ASTChangeVisitor {

    private final Map<ILocalVariable, String> localVariableToRename = new HashMap<ILocalVariable, String>();

    public VariableVisibilityVisitor(ITranslationContext context) {
        super(context);
        transformerName = "Variable Visibility";
        description = new TextEditGroup(transformerName);
    }

    @Override
    public boolean runOnce() {
        return true;
    }

    @Override
    public boolean runAgain(CompilationUnit unit) {
        return false;
    }

    @Override
    public boolean transform(IProgressMonitor pm, ASTNode cunit) {
        final CollectLocalVariableToRename visitor = new CollectLocalVariableToRename();
        ((CompilationUnit) cunit).accept(visitor);
        if (localVariableToRename.size() == 0) return true;
        final Set<java.util.Map.Entry<ILocalVariable, String>> entries = localVariableToRename.entrySet();
        final List<ILocalVariable> locals = new ArrayList<ILocalVariable>();
        final List<String> newNames = new ArrayList<String>();
        for (final java.util.Map.Entry<ILocalVariable, String> entry : entries) {
            locals.add(entry.getKey());
            newNames.add(entry.getValue());
        }
        try {
            change = createChange(new NullProgressMonitor(), locals, newNames);
        } catch (final Exception e) {
            e.printStackTrace();
        }
        localVariableToRename.clear();
        return true;
    }

    public Change createChange(IProgressMonitor pm, List<ILocalVariable> locals, List<String> names) throws JavaModelException, CoreException {
        final RenameLocalVariablesProcessor processor = new RenameLocalVariablesProcessor(locals);
        RenameRefactoring refactoring = new RenameRefactoring(processor);
        processor.setNewElementNames(names);
        final RefactoringStatus status = refactoring.checkAllConditions(pm);
        if (status.hasFatalError()) {
            System.err.println(status);
            return null;
        }
        return refactoring.createChange(pm);
    }

    public class CollectLocalVariableToRename extends ASTVisitor {

        private final VariableScope scope = new VariableScope();

        @SuppressWarnings("unchecked")
        @Override
        public boolean visit(FieldDeclaration node) {
            final List listFragments = node.fragments();
            for (int i = 0; i < listFragments.size(); i++) {
                final VariableDeclarationFragment fragment = (VariableDeclarationFragment) listFragments.get(i);
                scope.addGlobal(fragment);
            }
            return false;
        }

        @Override
        public boolean visit(MethodDeclaration node) {
            scope.push(node.getName().getIdentifier());
            return true;
        }

        @Override
        public void endVisit(MethodDeclaration node) {
            scope.pop();
        }

        @Override
        public void endVisit(VariableDeclarationFragment node) {
            if (scope.nameClash(node)) {
                final String newName = scope.buildNewName(node);
                renameLocalVariable(node, newName);
            }
            scope.addInCurrentScope(node);
        }

        @Override
        public void endVisit(SingleVariableDeclaration node) {
            if (scope.nameClash(node)) {
                final String newName = scope.buildNewName(node);
                renameLocalVariable(node, newName);
            }
            scope.addInCurrentScope(node);
        }

        private void renameLocalVariable(VariableDeclaration node, String newName) {
            final IBinding binding = node.getName().resolveBinding();
            final IJavaElement e = binding.getJavaElement();
            localVariableToRename.put((ILocalVariable) e, newName);
        }

        public class VariableScope {

            class MethodScope {

                List<String> localVariables = new ArrayList<String>();

                String methodName;

                public MethodScope(String methodName) {
                    this.methodName = methodName;
                }
            }

            List<String> globaleVariables = new ArrayList<String>();

            Stack<MethodScope> stack = new Stack<MethodScope>();

            int i = 0;

            public void addGlobal(VariableDeclarationFragment fragment) {
                globaleVariables.add(fragment.getName().getIdentifier());
            }

            public String buildNewName(VariableDeclaration fragment) {
                return fragment.getName().getIdentifier() + "_" + i++;
            }

            public void addInCurrentScope(VariableDeclaration fragment) {
                if (!stack.isEmpty()) stack.peek().localVariables.add(fragment.getName().getIdentifier());
            }

            public MethodScope peek() {
                return stack.peek();
            }

            public void pop() {
                stack.pop();
            }

            public void push(String methodName) {
                stack.push(new MethodScope(methodName));
                i = 0;
            }

            public boolean nameClash(VariableDeclaration fragment) {
                return (globaleVariables.contains(fragment.getName().getIdentifier())) || (!stack.isEmpty() && stack.peek().localVariables.contains(fragment.getName().getIdentifier()));
            }
        }
    }
}
