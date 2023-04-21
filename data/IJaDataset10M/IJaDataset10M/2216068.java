package com.byterefinery.rmbench.dialogs;

import java.sql.Driver;
import java.util.Iterator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import com.byterefinery.rmbench.RMBenchPlugin;
import com.byterefinery.rmbench.exceptions.SystemException;
import com.byterefinery.rmbench.extension.DatabaseExtension;
import com.byterefinery.rmbench.jdbc.DriverUtil;
import com.byterefinery.rmbench.jdbc.IDriverInfo;
import com.byterefinery.rmbench.util.ImageConstants;
import com.byterefinery.rmbench.util.UIRunnableWithProgress;

/**
 * Wizard page that allows configuration of connection name, driver jar files and class name.
 * @author cse
 */
public class JdbcConnectionWizardPage1 extends WizardPage {

    private static final String[] FILTER_EXTENSIONS = new String[] { "*.jar", "*.zip", "*.*" };

    private final JdbcConnectionWizard wizard;

    private Text nameText;

    private ComboViewer classesViewer;

    private ComboViewer infosViewer;

    private TableViewer driverFilesViewer;

    private Button removeJarButton;

    public JdbcConnectionWizardPage1(JdbcConnectionWizard wizard) {
        super(JdbcConnectionWizardPage1.class.getName());
        this.wizard = wizard;
        setTitle(wizard.getPageTitle());
        setDescription(Messages.JdbcConnectionWizardPage1_description);
    }

    public void createControl(Composite parent) {
        GridData gridData;
        GridLayout layout;
        final Composite mainComposite = new Composite(parent, SWT.NONE);
        mainComposite.setLayout(new GridLayout(1, false));
        Composite nameComposite = new Composite(mainComposite, SWT.NONE);
        nameComposite.setLayout(new GridLayout(2, false));
        nameComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Label label = new Label(nameComposite, SWT.NONE);
        label.setText(Messages.JdbcConnectionWizardPage1_name);
        nameText = new Text(nameComposite, SWT.BORDER | SWT.SINGLE);
        nameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                wizard.setConnectionName(nameText.getText());
                updatePageComplete();
            }
        });
        nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        if (wizard.getConnectionName() != null) nameText.setText(wizard.getConnectionName());
        Composite driverGroup = new Composite(mainComposite, SWT.BORDER);
        driverGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        layout = new GridLayout(3, false);
        layout.marginHeight = 10;
        layout.marginWidth = 10;
        driverGroup.setLayout(layout);
        label = new Label(driverGroup, SWT.NULL);
        label.setText(Messages.JdbcConnectionWizardPage1_DriverJars);
        gridData = new GridData(SWT.BEGINNING, SWT.DEFAULT, false, false);
        gridData.horizontalSpan = 3;
        label.setLayoutData(gridData);
        Table table = new Table(driverGroup, SWT.BORDER);
        gridData = new GridData(GridData.FILL_BOTH);
        gridData.horizontalSpan = 2;
        table.setLayoutData(gridData);
        driverFilesViewer = new TableViewer(table);
        driverFilesViewer.setLabelProvider(new DriverFilesLabelProvider());
        driverFilesViewer.setContentProvider(new ArrayContentProvider());
        driverFilesViewer.setInput(wizard.getDriverFiles());
        driverFilesViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                removeJarButton.setEnabled(!event.getSelection().isEmpty());
            }
        });
        Composite buttonArea = new Composite(driverGroup, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 0;
        buttonArea.setLayout(gridLayout);
        buttonArea.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
        Button addExternalJar = new Button(buttonArea, SWT.PUSH);
        addExternalJar.setText(Messages.JdbcConnectionWizardPage1_addExternalJar);
        addExternalJar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
        addExternalJar.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                addDriverFiles(mainComposite);
            }
        });
        removeJarButton = new Button(buttonArea, SWT.PUSH);
        removeJarButton.setText(Messages.JdbcConnectionWizardPage1_removeJar);
        removeJarButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_BEGINNING));
        removeJarButton.setEnabled(false);
        removeJarButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                IStructuredSelection selection = (IStructuredSelection) driverFilesViewer.getSelection();
                for (Iterator<?> i = selection.iterator(); i.hasNext(); ) {
                    String fileName = (String) i.next();
                    wizard.removeDriverFile(fileName);
                    driverFilesViewer.remove(fileName);
                }
                updateDrivers(true);
            }
        });
        label = new Label(driverGroup, SWT.NULL);
        label.setText(Messages.JdbcConnectionWizardPage1_driverClassName);
        final Combo classCombo = new Combo(driverGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        classCombo.setLayoutData(gridData);
        classesViewer = new ComboViewer(classCombo);
        classesViewer.setLabelProvider(new ClassNameLabelProvider());
        classesViewer.setContentProvider(new ArrayContentProvider());
        classesViewer.setInput(new Class[0]);
        classesViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                int index = classCombo.getSelectionIndex();
                String selectedClassName = classCombo.getItem(index);
                wizard.setDriverName(selectedClassName);
                updateDatabaseInfos();
            }
        });
        new Label(driverGroup, SWT.NULL);
        label = new Label(driverGroup, SWT.NULL);
        label.setText(Messages.JdbcConnectionWizardPage1_DriverType);
        Combo infoCombo = new Combo(driverGroup, SWT.DROP_DOWN | SWT.READ_ONLY);
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        infoCombo.setLayoutData(gridData);
        infosViewer = new ComboViewer(infoCombo);
        infosViewer.setLabelProvider(new ExtensionLabelProvider());
        infosViewer.setContentProvider(new ArrayContentProvider());
        infosViewer.setInput(RMBenchPlugin.getExtensionManager().getDatabaseExtensions());
        infosViewer.addSelectionChangedListener(new ISelectionChangedListener() {

            public void selectionChanged(SelectionChangedEvent event) {
                IStructuredSelection selection = (IStructuredSelection) infosViewer.getSelection();
                wizard.setDatabaseExtension((DatabaseExtension) selection.getFirstElement());
                updatePageComplete();
            }
        });
        setControl(mainComposite);
        updateDrivers(false);
        updateDatabaseInfos();
        updatePageComplete();
    }

    private void addDriverFiles(Composite parent) {
        FileDialog fileDialog = new FileDialog(parent.getShell(), SWT.OPEN);
        fileDialog.setFilterExtensions(FILTER_EXTENSIONS);
        fileDialog.setFilterNames(new String[] { Messages.JdbcConnectionWizardPage1_JarFiles, Messages.JdbcConnectionWizardPage1_ZipFiles, Messages.JdbcConnectionWizardPage1_AllFiles });
        fileDialog.setFilterPath(null);
        String filename = fileDialog.open();
        if (filename != null) {
            wizard.addDriverFile(filename);
            driverFilesViewer.add(filename);
            updateDrivers(true);
        }
    }

    private void updatePageComplete() {
        setPageComplete(wizard.getConnectionName() != null && wizard.getDriverName() != null && wizard.getDatabaseExtension() != null);
    }

    private void updateDrivers(boolean showProgressDialog) {
        UIRunnableWithProgress run = new UIRunnableWithProgress(showProgressDialog) {

            public void run(IProgressMonitor monitor) {
                monitor.beginTask(Messages.JdbcConnectionWizardPage1_loading_drivers, IProgressMonitor.UNKNOWN);
                try {
                    loadDriversFomFiles();
                } catch (SystemException e) {
                    RMBenchPlugin.logError(e);
                } finally {
                    monitor.done();
                }
            }
        };
        run.start();
    }

    private void loadDriversFomFiles() throws SystemException {
        String[] fileNames = wizard.getDriverFiles();
        if (fileNames.length > 0) {
            Class<Driver>[] drivers = DriverUtil.getAllDrivers(fileNames);
            classesViewer.setInput(drivers);
            if (drivers.length > 0) {
                Class<Driver> driver = findDeclaredDriver(drivers);
                if (driver == null) driver = drivers[0];
                classesViewer.setSelection(new StructuredSelection(driver));
            }
        }
    }

    private Class<Driver> findDeclaredDriver(Class<Driver>[] drivers) {
        IDriverInfo[] allInfos = RMBenchPlugin.getExtensionManager().getJdbcDriverExtensions();
        for (int i = 0; i < drivers.length; i++) {
            for (int j = 0; j < allInfos.length; j++) {
                if (allInfos[j].getClassName().equals(drivers[i].getName())) {
                    return drivers[i];
                }
            }
        }
        return null;
    }

    private void updateDatabaseInfos() {
        DatabaseExtension dbExt = wizard.getDatabaseExtension();
        if (dbExt != null) {
            infosViewer.setInput(new Object[] { dbExt });
            infosViewer.setSelection(new StructuredSelection(dbExt));
        } else {
            infosViewer.setInput(RMBenchPlugin.getExtensionManager().getDatabaseExtensions());
        }
    }

    public class DriverFilesLabelProvider extends LabelProvider {

        public Image getImage(Object element) {
            return RMBenchPlugin.getImage(ImageConstants.EXTERNAL_JAR);
        }
    }

    private class ClassNameLabelProvider extends LabelProvider {

        public String getText(Object element) {
            return ((Class<?>) element).getName();
        }
    }

    private class ExtensionLabelProvider extends LabelProvider {

        public String getText(Object element) {
            return ((DatabaseExtension) element).getName();
        }
    }
}
