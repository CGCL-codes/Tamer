package de.mpiwg.vspace.diagram.modulecategories;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.transaction.NotificationFilter;
import de.mpiwg.vspace.common.constants.VSpaceExtensions;
import de.mpiwg.vspace.common.constants.VSpaceFilenames;
import de.mpiwg.vspace.common.project.ProjectObservable;
import de.mpiwg.vspace.diagram.part.ExhibitionDiagramEditorUtil;
import de.mpiwg.vspace.extension.ExceptionHandlingService;
import de.mpiwg.vspace.metamodel.Exhibition;
import de.mpiwg.vspace.metamodel.ExhibitionFactory;
import de.mpiwg.vspace.metamodel.ExhibitionModule;
import de.mpiwg.vspace.metamodel.ExhibitionPackage;
import de.mpiwg.vspace.metamodel.LinkedModuleCategoryContainer;
import de.mpiwg.vspace.metamodel.ModuleCategory;
import de.mpiwg.vspace.metamodel.ModuleCategoryContainer;
import de.mpiwg.vspace.util.domain.VSpaceDomainHelper;

public class ModuleCategoryManager extends Observable implements Observer {

    public static final ModuleCategoryManager INSTANCE = new ModuleCategoryManager();

    private List<ModuleCategory> categories;

    private CategoryListener listener;

    private ModuleCategoryManager() {
        categories = new ArrayList<ModuleCategory>();
        ProjectObservable.INSTANCE.addObserver(this);
        if (ProjectObservable.INSTANCE.isProjectOpen()) init();
    }

    public boolean createModuleCategoryContainer(Exhibition vspace, EditingDomain domain, IProject iproject) {
        ModuleCategoryContainer container = vspace.getModuleCategoryContainer();
        if (container != null && container.getLinkedContainer() != null) return false;
        if (container == null) {
            container = ExhibitionFactory.eINSTANCE.createModuleCategoryContainer();
            Command cmd = SetCommand.create(domain, vspace, ExhibitionPackage.Literals.EXHIBITION__MODULE_CATEGORY_CONTAINER, container);
            domain.getCommandStack().execute(cmd);
        }
        if (iproject == null) return false;
        LinkedModuleCategoryContainer linkedContainer = ExhibitionFactory.eINSTANCE.createLinkedModuleCategoryContainer();
        File categoryFile = new File(iproject.getLocation().toOSString() + File.separator + VSpaceFilenames.CATEGORIES_FILENAME + "." + VSpaceExtensions.MODULE_CATEGORIES_EXTENSION);
        if (!categoryFile.exists()) {
            try {
                categoryFile.createNewFile();
            } catch (IOException e1) {
                ExceptionHandlingService.INSTANCE.handleException(e1);
                return false;
            }
        }
        IFile categoryIFile = iproject.getFile(categoryFile.getName());
        URI categoryUri = URI.createPlatformResourceURI(categoryIFile.getFullPath().toOSString(), false);
        Factory factory = Resource.Factory.Registry.INSTANCE.getFactory(categoryUri);
        Resource categoryResource = factory.createResource(categoryUri);
        categoryResource.getContents().add(linkedContainer);
        domain.getResourceSet().getResources().add(categoryResource);
        try {
            categoryResource.save(ExhibitionDiagramEditorUtil.getSaveOptions());
        } catch (IOException e) {
            e.printStackTrace();
        }
        {
            Command cmd = SetCommand.create(domain, container, ExhibitionPackage.Literals.MODULE_CATEGORY_CONTAINER__LINKED_CONTAINER, linkedContainer);
            domain.getCommandStack().execute(cmd);
        }
        return true;
    }

    public List<ModuleCategory> getCategoriesForModule(ExhibitionModule module) {
        List<ModuleCategory> foundCats = new ArrayList<ModuleCategory>();
        for (ModuleCategory cat : categories) {
            if (cat.getModules().contains(module)) foundCats.add(cat);
        }
        return foundCats;
    }

    public ModuleCategory[] getCategories() {
        return categories.toArray(new ModuleCategory[categories.size()]);
    }

    public void update(Observable arg0, Object arg1) {
        if (arg0 instanceof ProjectObservable && arg1 instanceof Integer) {
            if (((Integer) arg1) == ProjectObservable.PROJECT_OPENED) {
                init();
            } else {
                for (ModuleCategory cat : categories) {
                    cat.eAdapters().remove(listener);
                }
                categories = new ArrayList<ModuleCategory>();
                listener = null;
            }
        }
    }

    protected void init() {
        EditingDomain domain = ProjectObservable.INSTANCE.getEditingDomain();
        Exhibition vspace = VSpaceDomainHelper.getVSpace(domain);
        categories = new ArrayList<ModuleCategory>();
        listener = new CategoryListener();
        ModuleCategoryContainer container = vspace.getModuleCategoryContainer();
        if (container != null) {
            LinkedModuleCategoryContainer linkedcontainer = container.getLinkedContainer();
            linkedcontainer.eAdapters().add(listener);
            for (ModuleCategory cate : linkedcontainer.getModuleCategories()) {
                buildCategories(cate, categories);
            }
        }
        for (ModuleCategory c : categories) {
            c.eAdapters().add(listener);
        }
    }

    protected void buildCategories(ModuleCategory category, List<ModuleCategory> categoryList) {
        categoryList.add(category);
        for (ModuleCategory subCat : category.getSubcategories()) {
            buildCategories(subCat, categoryList);
        }
    }

    class CategoryListener extends EContentAdapter {

        NotificationFilter filter = NotificationFilter.createFeatureFilter(ExhibitionPackage.Literals.LINKED_MODULE_CATEGORY_CONTAINER__MODULE_CATEGORIES).or(NotificationFilter.createFeatureFilter(ExhibitionPackage.Literals.MODULE_CATEGORY__SUBCATEGORIES));

        NotificationFilter moduleFilter = NotificationFilter.createFeatureFilter(ExhibitionPackage.Literals.MODULE_CATEGORY__MODULES);

        NotificationFilter editFilter = NotificationFilter.createNotifierTypeFilter(ExhibitionPackage.Literals.MODULE_CATEGORY).and(NotificationFilter.createEventTypeFilter(Notification.SET));

        @Override
        public void notifyChanged(Notification notification) {
            if (filter.matches(notification)) {
                Object newValue = notification.getNewValue();
                if (newValue instanceof ModuleCategory) {
                    if (notification.getEventType() == Notification.ADD) {
                        ((ModuleCategory) newValue).eAdapters().add(listener);
                        categories.add((ModuleCategory) newValue);
                    } else if (notification.getEventType() == Notification.REMOVE) {
                        ((ModuleCategory) newValue).eAdapters().remove(listener);
                        categories.remove(newValue);
                    }
                }
                setChanged();
                notifyObservers(notification);
                return;
            }
            if (moduleFilter.matches(notification) || editFilter.matches(notification)) {
                setChanged();
                notifyObservers(notification);
            }
        }
    }
}
