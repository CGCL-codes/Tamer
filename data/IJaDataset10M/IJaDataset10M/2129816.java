package de.mpiwg.vspace.diagram.modulecategories.ui;

import java.util.List;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import de.mpiwg.vspace.diagram.providers.EditingDomainManager;
import de.mpiwg.vspace.metamodel.Exhibition;
import de.mpiwg.vspace.metamodel.ExhibitionFactory;
import de.mpiwg.vspace.metamodel.ExhibitionPackage;
import de.mpiwg.vspace.metamodel.LinkedModuleCategoryContainer;
import de.mpiwg.vspace.metamodel.ModuleCategory;
import de.mpiwg.vspace.util.domain.VSpaceDomainHelper;

public class AddCategoryActionDelegate implements IEditorActionDelegate {

    private IEditorPart part;

    public void run(IAction action) {
        ModuleCategory category = ExhibitionFactory.eINSTANCE.createModuleCategory();
        EditingDomain domain = EditingDomainManager.INSTANCE.getEditingDomain();
        Exhibition vspace = VSpaceDomainHelper.getVSpace(domain);
        if (part instanceof ModuleCategoriesView) {
            List<EObject> selected = ((ModuleCategoriesView) part).getSelectedObjects();
            if (selected == null || selected.size() != 1) {
                LinkedModuleCategoryContainer container = vspace.getModuleCategoryContainer().getLinkedContainer();
                Command cmd = AddCommand.create(domain, container, ExhibitionPackage.Literals.LINKED_MODULE_CATEGORY_CONTAINER__MODULE_CATEGORIES, category);
                domain.getCommandStack().execute(cmd);
            } else {
                if (selected.get(0) instanceof ModuleCategory) {
                    Command cmd = AddCommand.create(domain, selected.get(0), ExhibitionPackage.Literals.MODULE_CATEGORY__SUBCATEGORIES, category);
                    domain.getCommandStack().execute(cmd);
                }
            }
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
        this.part = targetEditor;
    }
}
