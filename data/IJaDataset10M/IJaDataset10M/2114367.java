package org.jmlspecs.eclipse.refactor.action;

import java.util.Set;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.jmlspecs.eclipse.refactor.dom.JmlNode;
import org.jmlspecs.eclipse.refactor.util.Filter;

public class PullUpInputPage extends AbstractInputPage {

    public PullUpInputPage(String name) {
        super(name);
    }

    private AbstractMethodRefactoring getPullUpRefactoring() {
        return (AbstractMethodRefactoring) getRefactoring();
    }

    @Override
    protected IType[] getDestinationTypes() {
        return getPullUpRefactoring().getDestinationTypes();
    }

    @Override
    protected Set<JmlNode> getTreeViewerInitialSelection() {
        return getPullUpRefactoring().getSelectedNodes();
    }

    @Override
    protected JmlNode getTreeViewerInput() {
        return getPullUpRefactoring().getSourceTypeDeclaration();
    }

    @Override
    protected MethodRefactoringContentProvider createTreeViewerContentProvider() {
        return new MethodRefactoringContentProvider(new Filter<JmlNode>() {

            public boolean filter(JmlNode node) {
                return getPullUpRefactoring().canRefactor(node);
            }
        });
    }

    @Override
    protected void treeViewerCheckStateChanged(CheckStateChangedEvent event) {
        if (event.getElement() instanceof JmlNode) {
            if (event.getChecked()) {
                treeViewerItemChecked((JmlNode) event.getElement());
            } else {
                treeViewerItemUnchecked((JmlNode) event.getElement());
            }
            handleInputChanged();
        }
    }

    @Override
    protected void handleInputChanged() {
        RefactoringStatus status = new RefactoringStatus();
        AbstractMethodRefactoring refactoring = getPullUpRefactoring();
        status.merge(refactoring.setDestinationType(destTypeCombo.getText()));
        status.merge(refactoring.checkSelectedNodes());
        updateStatus(status);
    }

    @Override
    protected void treeViewerItemUnchecked(JmlNode node) {
        getTreeViewerInitialSelection().remove(node);
    }

    @Override
    protected void treeViewerItemChecked(JmlNode node) {
        getTreeViewerInitialSelection().add(node);
    }
}
