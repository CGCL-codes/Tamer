package org.regilo.modules.editor.page;

import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.regilo.core.ui.editors.pages.BasePage;
import org.regilo.core.xmlrpc.Method;
import org.regilo.core.xmlrpc.Methods;
import org.regilo.modules.RegiloModulesImages;
import org.regilo.modules.dnd.ModelDataTransfer;
import org.regilo.modules.dnd.ModuleDragAdapter;
import org.regilo.modules.dnd.ModuleDropAdapter;
import org.regilo.modules.jobs.GetModuleListJob;
import org.regilo.modules.model.Module;
import org.regilo.modules.model.Project;
import org.regilo.modules.model.parsers.ModulesParser;
import org.regilo.modules.model.parsers.ProjectsParser;

public class ModulesPage extends BasePage {

    private ProjectsPageMaster masterPage;

    public ModulesPage() {
        super("org.regilo.modules.editor.page.ModulesPage", "Modules");
    }

    protected void createFormContent(IManagedForm managedForm) {
        super.createFormContent(managedForm);
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        SashForm sashForm = new SashForm(form.getBody(), SWT.VERTICAL);
        sashForm.setLayoutData(gd);
        createModulesSection(managedForm, sashForm);
        createProjectSection(managedForm, sashForm);
    }

    private void createProjectSection(IManagedForm managedForm, SashForm sashForm) {
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.grabExcessHorizontalSpace = true;
        Composite composite = new Composite(sashForm, SWT.NONE);
        composite.setLayoutData(gd);
        masterPage.createContent(managedForm, composite);
        TableViewer viewer = masterPage.getviewer();
        ObservableListContentProvider tableViewerContentProvider = new ObservableListContentProvider();
        viewer.setContentProvider(tableViewerContentProvider);
        IObservableMap[] attributeMaps = BeansObservables.observeMaps(tableViewerContentProvider.getKnownElements(), Project.class, new String[] { "title" });
        viewer.setLabelProvider(new ObservableMapLabelProvider(attributeMaps) {

            @Override
            public Image getImage(Object element) {
                Project project = (Project) element;
                if (project.getStatus() == 5) {
                    return RegiloModulesImages.getInstance().get(RegiloModulesImages.IMG_PROJECT);
                } else {
                    return RegiloModulesImages.getInstance().get(RegiloModulesImages.IMG_PROJECT_ALERT);
                }
            }

            @Override
            public Image getColumnImage(Object element, int columnIndex) {
                Project project = (Project) element;
                if (project.getStatus() == 5) {
                    return RegiloModulesImages.getInstance().get(RegiloModulesImages.IMG_PROJECT);
                } else {
                    return RegiloModulesImages.getInstance().get(RegiloModulesImages.IMG_PROJECT_ALERT);
                }
            }
        });
        viewer.setSorter(new ViewerSorter() {

            @Override
            public int compare(Viewer viewer, Object e1, Object e2) {
                Project project1 = (Project) e1;
                Project project2 = (Project) e2;
                return project1.getInfo().getName().compareTo(project2.getInfo().getName());
            }
        });
        ProjectsParser parser = new ProjectsParser(getDrupalSite());
        Method method = Methods.getInstance().getMethod("org.regilo.methods.modules.getEnabledProjects");
        try {
            getBrowser().execute(method, null, parser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        viewer.setInput(parser.getProjects());
        GetModuleListJob getModuleListJob = new GetModuleListJob("Update projects list from drupal.org");
        getModuleListJob.schedule();
    }

    private void createModulesSection(IManagedForm managedForm, SashForm sashForm) {
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.grabExcessHorizontalSpace = true;
        gd.grabExcessVerticalSpace = true;
        FormToolkit toolkit = managedForm.getToolkit();
        Section section = toolkit.createSection(sashForm, Section.TITLE_BAR | Section.DESCRIPTION | Section.EXPANDED);
        section.setText("Installed modules");
        section.setDescription("List of all installed modules.");
        section.setLayoutData(gd);
        section.marginWidth = 10;
        section.marginHeight = 5;
        Composite client = toolkit.createComposite(section, SWT.WRAP);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        client.setLayout(layout);
        ModulesParser parser = new ModulesParser(getDrupalSite());
        createTable(client, parser.getAvailableModules(), false);
        createTable(client, parser.getEnabledModules(), true);
        section.setClient(client);
        Method method = Methods.getInstance().getMethod("org.regilo.methods.modules.getInstalledModules");
        try {
            getBrowser().execute(method, null, parser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable(Composite client, WritableList list, boolean enabled) {
        TableViewer viewer = new TableViewer(client, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        GridData gd = new GridData(GridData.FILL_BOTH);
        viewer.getControl().setLayoutData(gd);
        ObservableListContentProvider availableViewerContentProvider = new ObservableListContentProvider();
        viewer.setContentProvider(availableViewerContentProvider);
        IObservableMap[] availableAttributeMaps = BeansObservables.observeMaps(availableViewerContentProvider.getKnownElements(), Module.class, new String[] { "infoName" });
        viewer.setLabelProvider(new ObservableMapLabelProvider(availableAttributeMaps) {

            @Override
            public Image getImage(Object element) {
                Module module = (Module) element;
                if (module.getStatus() == 1) {
                    return RegiloModulesImages.getInstance().get(RegiloModulesImages.IMG_MODULE_ENABLED);
                } else {
                    return RegiloModulesImages.getInstance().get(RegiloModulesImages.IMG_MODULE_AVAL);
                }
            }

            @Override
            public Image getColumnImage(Object element, int columnIndex) {
                Module module = (Module) element;
                if (module.getStatus() == 1) {
                    return RegiloModulesImages.getInstance().get(RegiloModulesImages.IMG_MODULE_ENABLED);
                } else {
                    return RegiloModulesImages.getInstance().get(RegiloModulesImages.IMG_MODULE_AVAL);
                }
            }

            @Override
            public String getText(Object element) {
                Module module = (Module) element;
                return module.getInfoName();
            }

            @Override
            public String getColumnText(Object element, int columnIndex) {
                Module module = (Module) element;
                return module.getInfoName();
            }
        });
        viewer.setSorter(new ViewerSorter() {

            @Override
            public int compare(Viewer viewer, Object e1, Object e2) {
                Module module1 = (Module) e1;
                Module module2 = (Module) e2;
                return module1.getInfoName().compareTo(module2.getInfoName());
            }
        });
        viewer.setInput(list);
        int dragOperations = DND.DROP_MOVE;
        Transfer[] dragTransferTypes = new Transfer[] { ModelDataTransfer.getInstance() };
        viewer.addDragSupport(dragOperations, dragTransferTypes, new ModuleDragAdapter(viewer));
        int dropOperations = DND.DROP_MOVE;
        Transfer[] dropTransferTypes = new Transfer[] { ModelDataTransfer.getInstance() };
        viewer.addDropSupport(dropOperations, dropTransferTypes, new ModuleDropAdapter(viewer, getBrowser(), enabled));
    }

    @Override
    public void initialize(FormEditor editor) {
        super.initialize(editor);
        masterPage = new ProjectsPageMaster(getEditor());
    }
}
