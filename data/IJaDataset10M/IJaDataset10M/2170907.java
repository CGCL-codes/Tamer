package ar.com.fluxit.packageExplorerAddOns.dialogs;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jdt.internal.ui.filters.ClosedProjectFilter;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerContentProvider;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerLabelProvider;
import org.eclipse.jdt.internal.ui.packageview.WorkingSetAwareContentProvider;
import org.eclipse.jdt.internal.ui.packageview.WorkingSetAwareJavaElementSorter;
import org.eclipse.jdt.internal.ui.workingsets.WorkingSetModel;
import org.eclipse.jdt.ui.JavaElementComparator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.progress.WorkbenchJob;
import ar.com.fluxit.packageExplorerAddOns.filters.ProjectsFilter;
import ar.com.fluxit.packageExplorerAddOns.filters.ProjectsPatternFilter;
import ar.com.fluxit.packageExplorerAddOns.helpers.ProjectRevealer;
import ar.com.fluxit.packageExplorerAddOns.listeners.CheckSelectionListener;
import ar.com.fluxit.packageExplorerAddOns.listeners.TreeListener;

/**
 * Dialogo de busqueda en package explorer
 * 
 * @author Juan Barisich (<a href="mailto:juan.barisich@gmail.com">juan.barisich@gmail.com</a>)
 */
@SuppressWarnings("restriction")
public class PackageExplorerFinderDialog extends Dialog {

    private static final String DIALOG_SETTING_SECTION_NAME = "dialogPackageExplorerFinder";

    private static final String ONLY_JAVA_PROJECTS_ID = "onlyJavaProjects";

    public static final String PACKAGE_EXPLORER_VIEW_ID = "org.eclipse.jdt.ui.PackageExplorer";

    private Object model;

    private TreeViewer treeViewer;

    private ProjectsFilter projectsFilter;

    private Button onlyJavaProjectsButton;

    public PackageExplorerFinderDialog(Shell parentShell) {
        super(parentShell);
        setShellStyle(getShellStyle() | 0x10000000 | SWT.RESIZE);
    }

    @Override
    public boolean close() {
        saveWidgetValues();
        return super.close();
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Find Proyect");
    }

    private ViewerComparator createComparator() {
        if (model instanceof WorkingSetModel) {
            return new WorkingSetAwareJavaElementSorter();
        } else {
            return new JavaElementComparator();
        }
    }

    private PackageExplorerContentProvider createContentProvider() {
        if (model instanceof WorkingSetModel) {
            return new WorkingSetAwareContentProvider(true, (WorkingSetModel) model);
        } else {
            return new PackageExplorerContentProvider(true) {

                @Override
                public boolean hasChildren(Object element) {
                    return false;
                }
            };
        }
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        final Composite composite = (Composite) super.createDialogArea(parent);
        composite.setLayout(new GridLayout());
        projectsFilter = new ProjectsFilter();
        final PatternFilter filter = new ProjectsPatternFilter(projectsFilter);
        final int styleBits = SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER;
        final FilteredTree filteredTree = new FilteredTree(composite, styleBits, filter, false) {

            @Override
            protected void createFilterText(Composite parent) {
                super.createFilterText(parent);
                filterText.addKeyListener(new KeyAdapter() {

                    @Override
                    public void keyPressed(KeyEvent e) {
                        if (e.keyCode == SWT.CR) {
                            okPressed();
                        }
                    }
                });
            }

            @Override
            protected WorkbenchJob doCreateRefreshJob() {
                final WorkbenchJob refreshJob = super.doCreateRefreshJob();
                refreshJob.addJobChangeListener(new JobChangeAdapter() {

                    @Override
                    public void done(IJobChangeEvent event) {
                        final boolean hasItems = treeViewer.getTree().getItems().length > 0;
                        treeViewer.expandAll();
                        setOkButtonEnable(hasItems);
                    }
                });
                return refreshJob;
            }
        };
        filteredTree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        treeViewer = filteredTree.getViewer();
        onlyJavaProjectsButton = new Button(composite, SWT.CHECK);
        onlyJavaProjectsButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        onlyJavaProjectsButton.setText("Only Java Projects");
        onlyJavaProjectsButton.addSelectionListener(new CheckSelectionListener(projectsFilter, filteredTree));
        final PackageExplorerContentProvider contentProvider = createContentProvider();
        treeViewer.setContentProvider(contentProvider);
        treeViewer.setLabelProvider(createLabelProvider(contentProvider));
        treeViewer.addFilter(new ClosedProjectFilter());
        treeViewer.addFilter(projectsFilter);
        treeViewer.setInput(model);
        final TreeListener treeListener = new TreeListener(this);
        treeViewer.addDoubleClickListener(treeListener);
        treeViewer.addSelectionChangedListener(treeListener);
        treeViewer.setComparator(createComparator());
        restoreWidgetValues();
        return composite;
    }

    private IBaseLabelProvider createLabelProvider(PackageExplorerContentProvider contentProvider) {
        return new PackageExplorerLabelProvider(contentProvider);
    }

    private IDialogSettings getDialogSettings() {
        final IDialogSettings workbenchSettings = WorkbenchPlugin.getDefault().getDialogSettings();
        IDialogSettings section = workbenchSettings.getSection(DIALOG_SETTING_SECTION_NAME);
        if (section == null) {
            section = workbenchSettings.addNewSection(DIALOG_SETTING_SECTION_NAME);
        }
        return section;
    }

    @Override
    protected Point getInitialSize() {
        return new Point(480, 350);
    }

    public Object getSelectedProject() {
        final Object selected = ((TreeSelection) treeViewer.getSelection()).getFirstElement();
        if (projectsFilter.isProject(selected)) {
            return selected;
        } else {
            return null;
        }
    }

    @Override
    protected void okPressed() {
        final Object selectedProject = getSelectedProject();
        if (selectedProject != null) {
            ProjectRevealer.getInstance().selectAndReveal(selectedProject);
            super.okPressed();
        }
    }

    public int open(Object model) {
        this.model = model;
        return super.open();
    }

    private void restoreWidgetValues() {
        final IDialogSettings settings = getDialogSettings();
        final Boolean onlyJavaProjects = settings.getBoolean(ONLY_JAVA_PROJECTS_ID);
        projectsFilter.setOnlyJavaProjects(onlyJavaProjects);
        onlyJavaProjectsButton.setSelection(onlyJavaProjects);
    }

    public void saveWidgetValues() {
        final IDialogSettings settings = getDialogSettings();
        settings.put(ONLY_JAVA_PROJECTS_ID, onlyJavaProjectsButton.getSelection());
    }

    public void setOkButtonEnable(final boolean enabled) {
        final Button button = getButton(IDialogConstants.OK_ID);
        if (button != null) {
            button.setEnabled(enabled);
        }
    }
}
