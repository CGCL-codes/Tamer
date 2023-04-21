package org.rubypeople.rdt.refactoring.core.mergeclasspartsinfile;

import java.util.ArrayList;
import java.util.Collection;
import org.rubypeople.rdt.refactoring.classnodeprovider.IncludedClassesProvider;
import org.rubypeople.rdt.refactoring.core.IRefactoringConfig;
import org.rubypeople.rdt.refactoring.documentprovider.IDocumentProvider;
import org.rubypeople.rdt.refactoring.nodewrapper.ClassNodeWrapper;
import org.rubypeople.rdt.refactoring.nodewrapper.PartialClassNodeWrapper;

public class MergeClassPartInFileConfig implements IRefactoringConfig {

    private IDocumentProvider documentProvider;

    private Collection<ClassNodeWrapper> selectableClasses;

    private PartialClassNodeWrapper selectedClassPart;

    private Collection<PartialClassNodeWrapper> checkedClassParts;

    private IncludedClassesProvider classNodeProvider;

    public MergeClassPartInFileConfig(IDocumentProvider documentProvider) {
        this.documentProvider = documentProvider;
        checkedClassParts = new ArrayList<PartialClassNodeWrapper>();
    }

    public IDocumentProvider getDocumentProvider() {
        return documentProvider;
    }

    public boolean hasSelectableClasses() {
        return selectableClasses != null && !selectableClasses.isEmpty();
    }

    public PartialClassNodeWrapper getSelectedClassPart() {
        return selectedClassPart;
    }

    public Collection<PartialClassNodeWrapper> getCheckedClassParts() {
        return checkedClassParts;
    }

    public void setSelectableClasses(Collection<ClassNodeWrapper> selectableClasses) {
        this.selectableClasses = selectableClasses;
    }

    public Collection<ClassNodeWrapper> getSelectableClasses() {
        return selectableClasses;
    }

    public void setCheckedClassParts(Collection<PartialClassNodeWrapper> checkedClassParts) {
        this.checkedClassParts = checkedClassParts;
    }

    public void setSelectedClassPart(PartialClassNodeWrapper selectedClassPart) {
        this.selectedClassPart = selectedClassPart;
    }

    public ClassNodeWrapper getClassNode(String className) {
        return classNodeProvider.getClassNode(className);
    }

    public Collection<ClassNodeWrapper> getAllClassNodes() {
        return classNodeProvider.getAllClassNodes();
    }

    public void setClassNodeProvider(IncludedClassesProvider classNodeProvider) {
        this.classNodeProvider = classNodeProvider;
    }

    public void setDocumentProvider(IDocumentProvider doc) {
        this.documentProvider = doc;
    }
}
