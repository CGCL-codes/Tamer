package org.abettor.leaf4e.popup.actions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.abettor.leaf4e.Activator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 增加Action类的动作
 * @author shawn
 *
 */
public class AddActionBean implements IObjectActionDelegate {

    private Shell shell;

    private ISelection selection;

    private ILog logger = Activator.getDefault().getLog();

    @Override
    public void run(IAction action) {
        new ActionDialog(shell).open();
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        this.selection = selection;
    }

    @Override
    public void setActivePart(IAction action, IWorkbenchPart workbenchPart) {
        shell = workbenchPart.getSite().getShell();
    }

    private class ActionDialog extends Dialog {

        private boolean success = false;

        private Text txtPackage;

        private Text txtClass;

        private Text txtBean;

        private CheckboxTableViewer tableViewer;

        private List<FieldInfo> fieldList = new ArrayList<FieldInfo>();

        private String packageName;

        private String className;

        private String beanName;

        protected ActionDialog(Shell parentShell) {
            super(parentShell);
        }

        @Override
        protected Control createDialogArea(Composite parent) {
            GridLayout layout = new GridLayout();
            layout.makeColumnsEqualWidth = false;
            layout.numColumns = 3;
            Composite composite = (Composite) super.createDialogArea(parent);
            composite.getShell().setText("Add action bean");
            composite.setLayout(layout);
            GridData gridData = new GridData();
            gridData.grabExcessHorizontalSpace = true;
            gridData.horizontalAlignment = GridData.FILL;
            Label label = new Label(composite, SWT.NONE);
            label.setText("Package:");
            txtPackage = new Text(composite, SWT.BORDER);
            txtPackage.setLayoutData(gridData);
            Button btn = new Button(composite, SWT.PUSH);
            btn.setText("Browse");
            btn.addMouseListener(new MouseAdapter() {

                private List<String> packages;

                @Override
                public void mouseUp(MouseEvent e) {
                    TreeSelection ts = (TreeSelection) selection;
                    IJavaProject project = (IJavaProject) ts.getFirstElement();
                    IProject proj = project.getProject();
                    InputStream is = null;
                    try {
                        IFile conf = proj.getFile(Activator.PLUGIN_CONF);
                        is = conf.getContents();
                        Properties prop = new Properties();
                        prop.load(is);
                        IFolder src = proj.getFolder(prop.getProperty("SRC"));
                        packages = new ArrayList<String>();
                        travelForlder(src, "");
                        new PackageDialog(shell).open();
                    } catch (Exception ex) {
                        Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Show package list error", ex);
                        logger.log(status);
                        MessageDialog.openError(shell, "Error", "Show package list error");
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException ioe) {
                                Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "", ioe);
                                logger.log(status);
                            }
                        }
                    }
                }

                private void travelForlder(IFolder parentFolder, String base) {
                    try {
                        IResource[] resArray = parentFolder.members();
                        for (IResource res : resArray) {
                            if (res instanceof IFolder) {
                                IFolder folder = (IFolder) res;
                                String name = folder.getName();
                                if (base != null && !base.isEmpty()) {
                                    name = base + "." + name;
                                }
                                packages.add(name);
                                travelForlder(folder, name);
                            }
                        }
                    } catch (CoreException e) {
                        Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Traveling packages error", e);
                        logger.log(status);
                    }
                }

                class PackageDialog extends Dialog {

                    private Text pack;

                    private org.eclipse.swt.widgets.List list;

                    protected PackageDialog(Shell parentShell) {
                        super(parentShell);
                    }

                    @Override
                    protected Control createDialogArea(Composite parent) {
                        Composite composite = (Composite) super.createDialogArea(parent);
                        composite.getShell().setText("Browse package");
                        GridLayout layout = new GridLayout();
                        layout.makeColumnsEqualWidth = false;
                        layout.numColumns = 2;
                        composite.setLayout(layout);
                        GridData gridData = new GridData();
                        gridData.grabExcessHorizontalSpace = true;
                        gridData.horizontalAlignment = GridData.FILL;
                        Label label = new Label(composite, SWT.NONE);
                        label.setText("Package:");
                        pack = new Text(composite, SWT.BORDER);
                        pack.setLayoutData(gridData);
                        pack.addModifyListener(new ModifyListener() {

                            @Override
                            public void modifyText(ModifyEvent e) {
                                String filter = pack.getText();
                                List<String> filted = null;
                                if (filter == null || filter.isEmpty()) {
                                    filted = packages;
                                } else {
                                    filted = new ArrayList<String>();
                                    for (String item : packages) {
                                        if (item.startsWith(filter)) {
                                            filted.add(item);
                                        }
                                    }
                                }
                                String[] listItems = new String[filted.size()];
                                filted.toArray(listItems);
                                list.setItems(listItems);
                                list.setSelection(new int[0]);
                            }
                        });
                        gridData = new GridData();
                        gridData.grabExcessHorizontalSpace = true;
                        gridData.horizontalAlignment = GridData.FILL;
                        gridData.horizontalSpan = 2;
                        gridData.widthHint = 400;
                        gridData.heightHint = 300;
                        list = new org.eclipse.swt.widgets.List(composite, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE);
                        list.setLayoutData(gridData);
                        String[] listItems = new String[packages.size()];
                        packages.toArray(listItems);
                        list.setItems(listItems);
                        list.addSelectionListener(new SelectionListener() {

                            @Override
                            public void widgetSelected(SelectionEvent e) {
                                int idx = list.getSelectionIndex();
                                if (idx >= 0) {
                                    pack.setText(list.getItem(idx));
                                }
                            }

                            @Override
                            public void widgetDefaultSelected(SelectionEvent e) {
                            }
                        });
                        return composite;
                    }

                    protected void okPressed() {
                        String p = pack.getText();
                        if (p == null || p.trim().isEmpty()) {
                            MessageDialog.openError(shell, "Error", "You must fill in the package name.");
                            return;
                        }
                        if (!packages.contains(p)) {
                            MessageDialog.openError(shell, "Error", "The package you entered dose not exist.");
                            return;
                        }
                        txtPackage.setText(p);
                        super.okPressed();
                    }
                }
            });
            gridData = new GridData();
            gridData.grabExcessHorizontalSpace = true;
            gridData.horizontalAlignment = GridData.FILL;
            gridData.horizontalSpan = 2;
            label = new Label(composite, SWT.NONE);
            label.setText("Class name:");
            txtClass = new Text(composite, SWT.BORDER);
            txtClass.setLayoutData(gridData);
            gridData = new GridData();
            gridData.grabExcessHorizontalSpace = true;
            gridData.horizontalAlignment = GridData.FILL;
            gridData.horizontalSpan = 2;
            label = new Label(composite, SWT.NONE);
            label.setText("Bean name:");
            txtBean = new Text(composite, SWT.BORDER);
            txtBean.setLayoutData(gridData);
            gridData = new GridData();
            gridData.grabExcessHorizontalSpace = true;
            gridData.horizontalAlignment = GridData.FILL;
            gridData.horizontalSpan = 3;
            layout = new GridLayout();
            layout.makeColumnsEqualWidth = false;
            layout.numColumns = 2;
            Group group = new Group(composite, SWT.NONE);
            group.setText("Injections");
            group.setLayoutData(gridData);
            group.setLayout(layout);
            gridData = new GridData();
            gridData.grabExcessHorizontalSpace = true;
            gridData.horizontalAlignment = GridData.FILL;
            gridData.heightHint = 300;
            TableViewer tv = new TableViewer(group, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.CHECK);
            tableViewer = new CheckboxTableViewer(tv.getTable());
            tableViewer.setContentProvider(new ContentProvider());
            tableViewer.setLabelProvider(new TableLabelProvider());
            Table dataTable = tableViewer.getTable();
            dataTable.setLayoutData(gridData);
            dataTable.setLinesVisible(true);
            dataTable.setHeaderVisible(true);
            TableColumn tc = new TableColumn(dataTable, SWT.NONE);
            tc.setText("");
            tc.setWidth(20);
            tc = new TableColumn(dataTable, SWT.NONE);
            tc.setText("Field");
            tc.setWidth(100);
            tc = new TableColumn(dataTable, SWT.NONE);
            tc.setText("Is reference");
            tc.setWidth(100);
            tc = new TableColumn(dataTable, SWT.NONE);
            tc.setText("Reference/Value");
            tc.setWidth(150);
            tc = new TableColumn(dataTable, SWT.NONE);
            tc.setText("Class");
            tc.setWidth(300);
            layout = new GridLayout();
            layout.numColumns = 1;
            gridData = new GridData();
            gridData.grabExcessHorizontalSpace = true;
            gridData.horizontalAlignment = GridData.END;
            gridData.grabExcessVerticalSpace = true;
            gridData.verticalAlignment = GridData.CENTER;
            Composite comp = new Composite(group, SWT.NONE);
            comp.setLayout(layout);
            comp.setLayoutData(gridData);
            gridData = new GridData();
            gridData.grabExcessHorizontalSpace = true;
            gridData.horizontalAlignment = GridData.FILL;
            btn = new Button(comp, SWT.PUSH);
            btn.setText("Add reference");
            btn.setLayoutData(gridData);
            btn.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseUp(MouseEvent e) {
                    ReferenceDialog rd = new ReferenceDialog(shell);
                    if (rd.open() == Dialog.OK) {
                        FieldInfo fieldInfo = rd.getFieldInfo();
                        fieldList.add(fieldInfo);
                        tableViewer.add(fieldInfo);
                    }
                }

                class ReferenceDialog extends Dialog {

                    private FieldInfo fieldInfo = new FieldInfo();

                    private Text txtPackage;

                    private Text txtClass;

                    private Text txtField;

                    private Text txtValue;

                    protected ReferenceDialog(Shell parentShell) {
                        super(parentShell);
                        fieldInfo.isRef = true;
                    }

                    @Override
                    protected Control createDialogArea(Composite parent) {
                        GridLayout layout = new GridLayout();
                        layout.makeColumnsEqualWidth = false;
                        layout.numColumns = 3;
                        Composite composite = (Composite) super.createDialogArea(parent);
                        composite.getShell().setText("Add reference");
                        composite.setLayout(layout);
                        GridData gridData = new GridData();
                        gridData.grabExcessHorizontalSpace = true;
                        gridData.horizontalAlignment = GridData.FILL;
                        gridData.widthHint = 250;
                        Label label = new Label(composite, SWT.NONE);
                        label.setText("Package:");
                        txtPackage = new Text(composite, SWT.BORDER);
                        txtPackage.setLayoutData(gridData);
                        Button btn = new Button(composite, SWT.PUSH);
                        btn.setText("Browse");
                        btn.addMouseListener(new MouseAdapter() {

                            private List<String> packages;

                            @Override
                            public void mouseUp(MouseEvent e) {
                                TreeSelection ts = (TreeSelection) selection;
                                IJavaProject project = (IJavaProject) ts.getFirstElement();
                                IProject proj = project.getProject();
                                InputStream is = null;
                                try {
                                    IFile conf = proj.getFile(Activator.PLUGIN_CONF);
                                    is = conf.getContents();
                                    Properties prop = new Properties();
                                    prop.load(is);
                                    IFolder src = proj.getFolder(prop.getProperty("SRC"));
                                    packages = new ArrayList<String>();
                                    travelForlder(src, "");
                                    new PackageDialog(shell).open();
                                } catch (Exception ex) {
                                    Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Show package list error", ex);
                                    logger.log(status);
                                    MessageDialog.openError(shell, "Error", "Show package list error");
                                } finally {
                                    if (is != null) {
                                        try {
                                            is.close();
                                        } catch (IOException ioe) {
                                            Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "", ioe);
                                            logger.log(status);
                                        }
                                    }
                                }
                            }

                            private void travelForlder(IFolder parentFolder, String base) {
                                try {
                                    IResource[] resArray = parentFolder.members();
                                    for (IResource res : resArray) {
                                        if (res instanceof IFolder) {
                                            IFolder folder = (IFolder) res;
                                            String name = folder.getName();
                                            if (base != null && !base.isEmpty()) {
                                                name = base + "." + name;
                                            }
                                            packages.add(name);
                                            travelForlder(folder, name);
                                        }
                                    }
                                } catch (CoreException e) {
                                    Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Traveling packages error", e);
                                    logger.log(status);
                                }
                            }

                            class PackageDialog extends Dialog {

                                private Text pack;

                                private org.eclipse.swt.widgets.List list;

                                protected PackageDialog(Shell parentShell) {
                                    super(parentShell);
                                }

                                @Override
                                protected Control createDialogArea(Composite parent) {
                                    Composite composite = (Composite) super.createDialogArea(parent);
                                    composite.getShell().setText("Browse package");
                                    GridLayout layout = new GridLayout();
                                    layout.makeColumnsEqualWidth = false;
                                    layout.numColumns = 2;
                                    composite.setLayout(layout);
                                    GridData gridData = new GridData();
                                    gridData.grabExcessHorizontalSpace = true;
                                    gridData.horizontalAlignment = GridData.FILL;
                                    Label label = new Label(composite, SWT.NONE);
                                    label.setText("Package:");
                                    pack = new Text(composite, SWT.BORDER);
                                    pack.setLayoutData(gridData);
                                    pack.addModifyListener(new ModifyListener() {

                                        @Override
                                        public void modifyText(ModifyEvent e) {
                                            String filter = pack.getText();
                                            List<String> filted = null;
                                            if (filter == null || filter.isEmpty()) {
                                                filted = packages;
                                            } else {
                                                filted = new ArrayList<String>();
                                                for (String item : packages) {
                                                    if (item.startsWith(filter)) {
                                                        filted.add(item);
                                                    }
                                                }
                                            }
                                            String[] listItems = new String[filted.size()];
                                            filted.toArray(listItems);
                                            list.setItems(listItems);
                                            list.setSelection(new int[0]);
                                        }
                                    });
                                    gridData = new GridData();
                                    gridData.grabExcessHorizontalSpace = true;
                                    gridData.horizontalAlignment = GridData.FILL;
                                    gridData.horizontalSpan = 2;
                                    gridData.widthHint = 400;
                                    gridData.heightHint = 300;
                                    list = new org.eclipse.swt.widgets.List(composite, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE);
                                    list.setLayoutData(gridData);
                                    String[] listItems = new String[packages.size()];
                                    packages.toArray(listItems);
                                    list.setItems(listItems);
                                    list.addSelectionListener(new SelectionListener() {

                                        @Override
                                        public void widgetSelected(SelectionEvent e) {
                                            int idx = list.getSelectionIndex();
                                            if (idx >= 0) {
                                                pack.setText(list.getItem(idx));
                                            }
                                        }

                                        @Override
                                        public void widgetDefaultSelected(SelectionEvent e) {
                                        }
                                    });
                                    return composite;
                                }

                                protected void okPressed() {
                                    String p = pack.getText();
                                    if (p == null || p.trim().isEmpty()) {
                                        MessageDialog.openError(shell, "Error", "You must fill in the package name.");
                                        return;
                                    }
                                    if (!packages.contains(p)) {
                                        MessageDialog.openError(shell, "Error", "The package you entered dose not exist.");
                                        return;
                                    }
                                    txtPackage.setText(p);
                                    super.okPressed();
                                }
                            }
                        });
                        gridData = new GridData();
                        gridData.grabExcessHorizontalSpace = true;
                        gridData.horizontalAlignment = GridData.FILL;
                        label = new Label(composite, SWT.NONE);
                        label.setText("Interface:");
                        txtClass = new Text(composite, SWT.BORDER);
                        txtClass.setLayoutData(gridData);
                        btn = new Button(composite, SWT.PUSH);
                        btn.setText("Browse");
                        btn.addMouseListener(new MouseAdapter() {

                            @Override
                            public void mouseUp(MouseEvent e) {
                                ChooseInterfaceDialog cid = new ChooseInterfaceDialog(shell);
                                if (cid.open() == Dialog.OK) {
                                    String inter = cid.getInterfaceName();
                                    txtClass.setText(inter);
                                }
                            }

                            class ChooseInterfaceDialog extends Dialog {

                                private String interfaceName;

                                private List<String> interfaces = new ArrayList<String>();

                                private Text txtInterface;

                                private org.eclipse.swt.widgets.List list;

                                protected ChooseInterfaceDialog(Shell parentShell) {
                                    super(parentShell);
                                    InputStream is = null;
                                    try {
                                        TreeSelection ts = (TreeSelection) selection;
                                        IJavaProject project = (IJavaProject) ts.getFirstElement();
                                        IProject proj = project.getProject();
                                        IFile conf = proj.getFile(Activator.PLUGIN_CONF);
                                        is = conf.getContents();
                                        Properties prop = new Properties();
                                        prop.load(is);
                                        IPackageFragmentRoot root = project.findPackageFragmentRoot(project.getPath().append(prop.getProperty("SRC")));
                                        IPackageFragment packageFrag = root.createPackageFragment(ReferenceDialog.this.txtPackage.getText(), false, null);
                                        for (ICompilationUnit compilation : packageFrag.getCompilationUnits()) {
                                            IType beanClass = compilation.findPrimaryType();
                                            if (beanClass.isInterface()) {
                                                interfaces.add(beanClass.getElementName());
                                            }
                                        }
                                    } catch (Exception e) {
                                        Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Lookup for interfaces error", e);
                                        logger.log(status);
                                    } finally {
                                        if (is != null) {
                                            try {
                                                is.close();
                                            } catch (IOException e) {
                                                Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "", e);
                                                logger.log(status);
                                            }
                                        }
                                    }
                                }

                                @Override
                                protected Control createDialogArea(Composite parent) {
                                    Composite composite = (Composite) super.createDialogArea(parent);
                                    composite.getShell().setText("Browse interfaces");
                                    GridLayout layout = new GridLayout();
                                    layout.makeColumnsEqualWidth = false;
                                    layout.numColumns = 2;
                                    composite.setLayout(layout);
                                    GridData gridData = new GridData();
                                    gridData.grabExcessHorizontalSpace = true;
                                    gridData.horizontalAlignment = GridData.FILL;
                                    Label label = new Label(composite, SWT.NONE);
                                    label.setText("Interface:");
                                    txtInterface = new Text(composite, SWT.BORDER);
                                    txtInterface.setLayoutData(gridData);
                                    txtInterface.addModifyListener(new ModifyListener() {

                                        @Override
                                        public void modifyText(ModifyEvent e) {
                                            String filter = txtInterface.getText();
                                            List<String> filted = null;
                                            if (filter == null || filter.isEmpty()) {
                                                filted = interfaces;
                                            } else {
                                                filted = new ArrayList<String>();
                                                for (String item : interfaces) {
                                                    if (item.startsWith(filter)) {
                                                        filted.add(item);
                                                    }
                                                }
                                            }
                                            String[] listItems = new String[filted.size()];
                                            filted.toArray(listItems);
                                            list.setItems(listItems);
                                            list.setSelection(new int[0]);
                                        }
                                    });
                                    gridData = new GridData();
                                    gridData.grabExcessHorizontalSpace = true;
                                    gridData.horizontalAlignment = GridData.FILL;
                                    gridData.horizontalSpan = 2;
                                    gridData.widthHint = 400;
                                    gridData.heightHint = 300;
                                    list = new org.eclipse.swt.widgets.List(composite, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE);
                                    list.setLayoutData(gridData);
                                    String[] listItems = new String[interfaces.size()];
                                    interfaces.toArray(listItems);
                                    list.setItems(listItems);
                                    list.addSelectionListener(new SelectionListener() {

                                        @Override
                                        public void widgetSelected(SelectionEvent e) {
                                            int idx = list.getSelectionIndex();
                                            if (idx >= 0) {
                                                txtInterface.setText(list.getItem(idx));
                                            }
                                        }

                                        @Override
                                        public void widgetDefaultSelected(SelectionEvent e) {
                                        }
                                    });
                                    return composite;
                                }

                                @Override
                                protected void okPressed() {
                                    String interf = txtInterface.getText();
                                    if (interf == null || interf.isEmpty()) {
                                        MessageDialog.openError(shell, "Error", "You must fill in the interface name.");
                                        return;
                                    }
                                    if (!interfaces.contains(interf)) {
                                        MessageDialog.openError(shell, "Error", "The interface you entered dose not exist.");
                                        return;
                                    }
                                    interfaceName = interf;
                                    super.okPressed();
                                }

                                public String getInterfaceName() {
                                    return interfaceName;
                                }
                            }
                        });
                        gridData = new GridData();
                        gridData.grabExcessHorizontalSpace = true;
                        gridData.horizontalAlignment = GridData.FILL;
                        gridData.horizontalSpan = 2;
                        label = new Label(composite, SWT.NONE);
                        label.setText("Field name:");
                        txtField = new Text(composite, SWT.BORDER);
                        txtField.setLayoutData(gridData);
                        gridData = new GridData();
                        gridData.grabExcessHorizontalSpace = true;
                        gridData.horizontalAlignment = GridData.FILL;
                        label = new Label(composite, SWT.NONE);
                        label.setText("Reference:");
                        txtValue = new Text(composite, SWT.BORDER);
                        txtValue.setLayoutData(gridData);
                        btn = new Button(composite, SWT.PUSH);
                        btn.setText("Browse");
                        btn.addMouseListener(new MouseAdapter() {

                            @Override
                            public void mouseUp(MouseEvent e) {
                                ChooseReferenceDialog crd = new ChooseReferenceDialog(shell);
                                if (crd.open() == Dialog.OK) {
                                    String ref = crd.getReferenceName();
                                    txtValue.setText(ref);
                                }
                            }

                            class ChooseReferenceDialog extends Dialog {

                                private String referenceName;

                                private List<String> references = new ArrayList<String>();

                                private Text txtReference;

                                private org.eclipse.swt.widgets.List list;

                                protected ChooseReferenceDialog(Shell parentShell) {
                                    super(parentShell);
                                    InputStream isFile = null;
                                    InputStream is = null;
                                    try {
                                        String interfacePackageName = ReferenceDialog.this.txtPackage.getText();
                                        String interfaceName = ReferenceDialog.this.txtClass.getText();
                                        TreeSelection ts = (TreeSelection) selection;
                                        IJavaProject project = (IJavaProject) ts.getFirstElement();
                                        IProject proj = project.getProject();
                                        IFile conf = proj.getFile(Activator.PLUGIN_CONF);
                                        is = conf.getContents();
                                        Properties prop = new Properties();
                                        prop.load(is);
                                        IPackageFragmentRoot root = project.findPackageFragmentRoot(project.getPath().append(prop.getProperty("SRC")));
                                        String path = prop.getProperty("WEB_ROOT") + "/" + prop.getProperty("CONTEXT_FOLDER") + "/" + prop.getProperty("APPLICATION_CONTEXT");
                                        IFile file = proj.getFile(path);
                                        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                                        dbf.setValidating(false);
                                        dbf.setFeature("http://xml.org/sax/features/namespaces", false);
                                        dbf.setFeature("http://xml.org/sax/features/validation", false);
                                        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
                                        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                                        DocumentBuilder db = dbf.newDocumentBuilder();
                                        isFile = file.getContents();
                                        Document parentDoc = db.parse(isFile);
                                        Element rootNode = parentDoc.getDocumentElement();
                                        NodeList beanNodeList = rootNode.getElementsByTagName("bean");
                                        int len = beanNodeList.getLength();
                                        for (int i = 0; i < len; i++) {
                                            Element beanNode = (Element) beanNodeList.item(i);
                                            String classFullName = beanNode.getAttribute("class");
                                            int idx = classFullName.lastIndexOf(".");
                                            if (idx < 0) {
                                                continue;
                                            }
                                            String packageName = classFullName.substring(0, idx);
                                            String className = classFullName.substring(idx + 1);
                                            IPackageFragment packageFrag = root.createPackageFragment(packageName, false, null);
                                            ICompilationUnit compilation = packageFrag.getCompilationUnit(className + ".java");
                                            if (compilation == null || !compilation.exists()) {
                                                continue;
                                            }
                                            IType beanClass = compilation.findPrimaryType();
                                            if (beanClass == null || !beanClass.isClass()) {
                                                continue;
                                            }
                                            boolean flag = false;
                                            IImportDeclaration imp = compilation.getImport(interfacePackageName + "." + interfaceName);
                                            String inters[] = beanClass.getSuperInterfaceNames();
                                            for (String inter : inters) {
                                                if ((imp != null && inter.equals(interfaceName)) || inter.equals(interfacePackageName + "." + interfaceName)) {
                                                    flag = true;
                                                }
                                            }
                                            if (flag) {
                                                String beanName = beanNode.getAttribute("id");
                                                references.add(beanName);
                                            }
                                        }
                                    } catch (Exception e) {
                                        Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Lookup for references error", e);
                                        logger.log(status);
                                    } finally {
                                        if (is != null) {
                                            try {
                                                is.close();
                                            } catch (IOException e) {
                                                Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "", e);
                                                logger.log(status);
                                            }
                                        }
                                        if (isFile != null) {
                                            try {
                                                isFile.close();
                                            } catch (IOException e) {
                                                Status status = new Status(IStatus.WARNING, Activator.PLUGIN_ID, "", e);
                                                logger.log(status);
                                            }
                                        }
                                    }
                                }

                                @Override
                                protected Control createDialogArea(Composite parent) {
                                    Composite composite = (Composite) super.createDialogArea(parent);
                                    composite.getShell().setText("Browse references");
                                    GridLayout layout = new GridLayout();
                                    layout.makeColumnsEqualWidth = false;
                                    layout.numColumns = 2;
                                    composite.setLayout(layout);
                                    GridData gridData = new GridData();
                                    gridData.grabExcessHorizontalSpace = true;
                                    gridData.horizontalAlignment = GridData.FILL;
                                    Label label = new Label(composite, SWT.NONE);
                                    label.setText("Reference:");
                                    txtReference = new Text(composite, SWT.BORDER);
                                    txtReference.setLayoutData(gridData);
                                    txtReference.addModifyListener(new ModifyListener() {

                                        @Override
                                        public void modifyText(ModifyEvent e) {
                                            String filter = txtReference.getText();
                                            List<String> filted = null;
                                            if (filter == null || filter.isEmpty()) {
                                                filted = references;
                                            } else {
                                                filted = new ArrayList<String>();
                                                for (String item : references) {
                                                    if (item.startsWith(filter)) {
                                                        filted.add(item);
                                                    }
                                                }
                                            }
                                            String[] listItems = new String[filted.size()];
                                            filted.toArray(listItems);
                                            list.setItems(listItems);
                                            list.setSelection(new int[0]);
                                        }
                                    });
                                    gridData = new GridData();
                                    gridData.grabExcessHorizontalSpace = true;
                                    gridData.horizontalAlignment = GridData.FILL;
                                    gridData.horizontalSpan = 2;
                                    gridData.widthHint = 400;
                                    gridData.heightHint = 300;
                                    list = new org.eclipse.swt.widgets.List(composite, SWT.V_SCROLL | SWT.BORDER | SWT.SINGLE);
                                    list.setLayoutData(gridData);
                                    String[] listItems = new String[references.size()];
                                    references.toArray(listItems);
                                    list.setItems(listItems);
                                    list.addSelectionListener(new SelectionListener() {

                                        @Override
                                        public void widgetSelected(SelectionEvent e) {
                                            int idx = list.getSelectionIndex();
                                            if (idx >= 0) {
                                                txtReference.setText(list.getItem(idx));
                                            }
                                        }

                                        @Override
                                        public void widgetDefaultSelected(SelectionEvent e) {
                                        }
                                    });
                                    return composite;
                                }

                                @Override
                                protected void okPressed() {
                                    String ref = txtReference.getText();
                                    if (ref == null || ref.isEmpty()) {
                                        MessageDialog.openError(shell, "Error", "You must fill in the reference name.");
                                        return;
                                    }
                                    if (!references.contains(ref)) {
                                        MessageDialog.openError(shell, "Error", "The reference you entered dose not exist.");
                                        return;
                                    }
                                    referenceName = ref;
                                    super.okPressed();
                                }

                                public String getReferenceName() {
                                    return referenceName;
                                }
                            }
                        });
                        return composite;
                    }

                    @Override
                    protected void okPressed() {
                        String pack = txtPackage.getText();
                        if (pack == null || pack.trim().isEmpty()) {
                            pack = "";
                        }
                        String clazz = txtClass.getText();
                        if (clazz == null || clazz.trim().isEmpty()) {
                            MessageDialog.openError(shell, "Error", "You must choose a interface.");
                            return;
                        }
                        if (!pack.isEmpty()) {
                            clazz = pack + "." + clazz;
                        }
                        fieldInfo.clazz = clazz.trim();
                        String field = txtField.getText();
                        if (field == null || field.trim().isEmpty()) {
                            MessageDialog.openError(shell, "Error", "You must fill in the field name.");
                            return;
                        }
                        fieldInfo.field = field.trim();
                        String value = txtValue.getText();
                        if (value == null || value.trim().isEmpty()) {
                            MessageDialog.openError(shell, "Error", "You must fill in the reference.");
                            return;
                        }
                        fieldInfo.value = value.trim();
                        super.okPressed();
                    }

                    public FieldInfo getFieldInfo() {
                        return fieldInfo;
                    }
                }
            });
            gridData = new GridData();
            gridData.grabExcessHorizontalSpace = true;
            gridData.horizontalAlignment = GridData.FILL;
            btn = new Button(comp, SWT.PUSH);
            btn.setText("Add value");
            btn.setLayoutData(gridData);
            btn.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseUp(MouseEvent e) {
                    ValueDialog vd = new ValueDialog(shell);
                    if (vd.open() == Dialog.OK) {
                        FieldInfo fieldInfo = vd.getFieldInfo();
                        fieldList.add(fieldInfo);
                        tableViewer.add(fieldInfo);
                    }
                }

                class ValueDialog extends Dialog {

                    private FieldInfo fieldInfo = new FieldInfo();

                    private Combo cmbClass;

                    private Text txtField;

                    private Text txtValue;

                    protected ValueDialog(Shell parentShell) {
                        super(parentShell);
                        fieldInfo.isRef = false;
                    }

                    @Override
                    protected Control createDialogArea(Composite parent) {
                        GridLayout layout = new GridLayout();
                        layout.makeColumnsEqualWidth = false;
                        layout.numColumns = 2;
                        Composite composite = (Composite) super.createDialogArea(parent);
                        composite.getShell().setText("Add value");
                        composite.setLayout(layout);
                        GridData gridData = new GridData();
                        gridData.grabExcessHorizontalSpace = true;
                        gridData.horizontalAlignment = GridData.FILL;
                        gridData.widthHint = 250;
                        Label label = new Label(composite, SWT.NONE);
                        label.setText("Class:");
                        String[] items = { "int", "short", "long", "float", "double", "boolean", "byte", "char", "Integer", "Short", "Long", "Float", "Double", "Boolean", "Byte", "Character", "String" };
                        cmbClass = new Combo(composite, SWT.NONE);
                        cmbClass.setItems(items);
                        cmbClass.setLayoutData(gridData);
                        gridData = new GridData();
                        gridData.grabExcessHorizontalSpace = true;
                        gridData.horizontalAlignment = GridData.FILL;
                        label = new Label(composite, SWT.NONE);
                        label.setText("Field name:");
                        txtField = new Text(composite, SWT.BORDER);
                        txtField.setLayoutData(gridData);
                        gridData = new GridData();
                        gridData.grabExcessHorizontalSpace = true;
                        gridData.horizontalAlignment = GridData.FILL;
                        label = new Label(composite, SWT.NONE);
                        label.setText("Value:");
                        txtValue = new Text(composite, SWT.BORDER);
                        txtValue.setLayoutData(gridData);
                        return composite;
                    }

                    @Override
                    protected void okPressed() {
                        String clazz = cmbClass.getText();
                        if (clazz == null || clazz.trim().isEmpty()) {
                            MessageDialog.openError(shell, "Error", "You must choose a class.");
                            return;
                        }
                        fieldInfo.clazz = clazz.trim();
                        String field = txtField.getText();
                        if (field == null || field.trim().isEmpty()) {
                            MessageDialog.openError(shell, "Error", "You must fill in the field name.");
                            return;
                        }
                        fieldInfo.field = field.trim();
                        String value = txtValue.getText();
                        if (value == null || value.trim().isEmpty()) {
                            MessageDialog.openError(shell, "Error", "You must fill in the value.");
                            return;
                        }
                        fieldInfo.value = value.trim();
                        super.okPressed();
                    }

                    public FieldInfo getFieldInfo() {
                        return fieldInfo;
                    }
                }
            });
            gridData = new GridData();
            gridData.grabExcessHorizontalSpace = true;
            gridData.horizontalAlignment = GridData.FILL;
            btn = new Button(comp, SWT.PUSH);
            btn.setText("Remove");
            btn.setLayoutData(gridData);
            btn.addMouseListener(new MouseAdapter() {

                public void mouseUp(MouseEvent e) {
                    Object[] array = tableViewer.getCheckedElements();
                    for (Object o : array) {
                        fieldList.remove(o);
                        tableViewer.remove(o);
                    }
                }
            });
            return composite;
        }

        @Override
        protected void okPressed() {
            packageName = txtPackage.getText();
            if (packageName == null || packageName.trim().isEmpty()) {
                MessageDialog.openError(shell, "Error", "You must enter the package name.");
                return;
            }
            packageName = packageName.trim();
            className = txtClass.getText();
            if (className == null || className.trim().isEmpty()) {
                MessageDialog.openError(shell, "Error", "You must enter the action bean class name.");
                return;
            }
            className = className.trim();
            beanName = txtBean.getText();
            if (beanName == null | beanName.trim().isEmpty()) {
                MessageDialog.openError(shell, "Error", "You must enter the action bean name.");
                return;
            }
            beanName = beanName.trim();
            IRunnableWithProgress runnable = new IRunnableWithProgress() {

                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    try {
                        doIt(monitor);
                    } catch (Exception e) {
                        throw new InvocationTargetException(e);
                    }
                }
            };
            try {
                new ProgressMonitorDialog(shell).run(true, false, runnable);
            } catch (Exception e) {
                Status status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Create action bean error", e);
                logger.log(status);
                MessageDialog.openError(shell, "Error", "Create action bean error.");
                return;
            }
            if (success) {
                super.okPressed();
            }
        }

        private void doIt(IProgressMonitor monitor) throws Exception {
            TreeSelection ts = (TreeSelection) selection;
            IJavaProject project = (IJavaProject) ts.getFirstElement();
            IProject proj = project.getProject();
            InputStream is = null;
            Properties prop = new Properties();
            try {
                IFile conf = proj.getFile(Activator.PLUGIN_CONF);
                is = conf.getContents();
                prop.load(is);
            } catch (Exception e) {
                throw e;
            } finally {
                if (is != null) {
                    is.close();
                }
            }
            IPackageFragmentRoot root = project.findPackageFragmentRoot(project.getPath().append(prop.getProperty("SRC")));
            IPackageFragment packageFrag = root.createPackageFragment(packageName, false, monitor);
            String content = "package " + packageName + ";\n";
            content += "public class " + className + " extends ActionSupport {\n";
            content += "}\n";
            ICompilationUnit compilation = packageFrag.createCompilationUnit(className + ".java", content, false, monitor);
            compilation.createImport("com.opensymphony.xwork2.ActionSupport", null, monitor);
            IType beanClass = compilation.findPrimaryType();
            for (FieldInfo info : fieldList) {
                String clazz = info.clazz;
                int idx = clazz.lastIndexOf(".");
                if (idx >= 0) {
                    compilation.createImport(clazz, null, monitor);
                    clazz = clazz.substring(idx + 1);
                }
                String field = "private " + clazz + " " + info.field + ";";
                beanClass.createField(field, null, false, monitor);
                String first = info.field.substring(0, 1);
                String others = info.field.substring(1);
                String name = first.toUpperCase() + others;
                String getter = null;
                if (clazz.equalsIgnoreCase("boolean")) {
                    getter = "public " + clazz + " is" + name + "(){\n";
                } else {
                    getter = "public " + clazz + " get" + name + "(){\n";
                }
                getter += "\treturn " + info.field + ";\n";
                getter += "}\n";
                String setter = "public void set" + name + "(" + clazz + " " + info.field + "){\n";
                setter += "\tthis." + info.field + " = " + info.field + ";\n";
                setter += "}\n";
                beanClass.createMethod(getter, null, false, monitor);
                beanClass.createMethod(setter, null, false, monitor);
            }
            InputStream isFile = null;
            ByteArrayOutputStream baos = null;
            ByteArrayInputStream baisXml = null;
            try {
                String path = prop.getProperty("WEB_ROOT") + "/" + prop.getProperty("CONTEXT_FOLDER") + "/" + prop.getProperty("STRUTS_CONTEXT");
                IFile file = proj.getFile(path);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                dbf.setValidating(false);
                dbf.setFeature("http://xml.org/sax/features/namespaces", false);
                dbf.setFeature("http://xml.org/sax/features/validation", false);
                dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
                dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
                DocumentBuilder db = dbf.newDocumentBuilder();
                isFile = file.getContents();
                Document parentDoc = db.parse(isFile);
                Element rootNode = parentDoc.getDocumentElement();
                Element beanElement = parentDoc.createElement("bean");
                beanElement.setAttribute("id", beanName);
                beanElement.setAttribute("class", packageName + "." + className);
                beanElement.setAttribute("scope", "prototype");
                rootNode.appendChild(beanElement);
                for (FieldInfo info : fieldList) {
                    Element property = parentDoc.createElement("property");
                    property.setAttribute("name", info.field);
                    if (info.isRef) {
                        property.setAttribute("ref", info.value);
                    } else {
                        property.setAttribute("value", info.value);
                    }
                    beanElement.appendChild(property);
                }
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
                DOMSource source = new DOMSource(parentDoc);
                baos = new ByteArrayOutputStream();
                StreamResult result = new StreamResult(baos);
                transformer.transform(source, result);
                baisXml = new ByteArrayInputStream(baos.toByteArray());
                file.setContents(baisXml, true, false, monitor);
            } catch (Exception e) {
                throw e;
            } finally {
                if (baisXml != null) {
                    baisXml.close();
                }
                if (baos != null) {
                    baos.close();
                }
                if (isFile != null) {
                    isFile.close();
                }
            }
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            dbf.setFeature("http://xml.org/sax/features/namespaces", false);
            dbf.setFeature("http://xml.org/sax/features/validation", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            ByteArrayInputStream bais = null;
            baisXml = null;
            baos = null;
            try {
                String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
                xmlContent += "<struts>\n";
                xmlContent += "</struts>";
                bais = new ByteArrayInputStream(xmlContent.getBytes("UTF-8"));
                DocumentBuilder builder = dbf.newDocumentBuilder();
                Document doc = builder.parse(bais);
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
                transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//Apache Software Foundation//DTD Struts Configuration 2.0//EN");
                transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://struts.apache.org/dtds/struts-2.0.dtd");
                DOMSource source = new DOMSource(doc);
                baos = new ByteArrayOutputStream();
                StreamResult result = new StreamResult(baos);
                transformer.transform(source, result);
                baisXml = new ByteArrayInputStream(baos.toByteArray());
                IFolder folder = proj.getFolder(prop.getProperty("SRC") + "/" + prop.getProperty("ACTION_MAPPING_PACKAGE"));
                if (!folder.exists()) {
                    folder.create(true, true, monitor);
                }
                IFile file = proj.getFile(prop.getProperty("SRC") + "/" + prop.getProperty("ACTION_MAPPING_PACKAGE") + "/" + className + ".xml");
                if (!file.exists()) {
                    file.create(baisXml, false, monitor);
                }
            } catch (Exception e) {
                throw e;
            } finally {
                if (bais != null) {
                    bais.close();
                }
                if (baisXml != null) {
                    baisXml.close();
                }
                if (baos != null) {
                    baos.close();
                }
            }
            isFile = null;
            baisXml = null;
            baos = null;
            try {
                IFile struts = proj.getFile(prop.getProperty("SRC") + "/struts.xml");
                isFile = struts.getContents();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document parentDoc = db.parse(isFile);
                Element rootNode = parentDoc.getDocumentElement();
                Element e = parentDoc.createElement("include");
                e.setAttribute("file", prop.getProperty("ACTION_MAPPING_PACKAGE") + "/" + className + ".xml");
                rootNode.appendChild(e);
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
                transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//Apache Software Foundation//DTD Struts Configuration 2.0//EN");
                transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://struts.apache.org/dtds/struts-2.0.dtd");
                DOMSource source = new DOMSource(parentDoc);
                baos = new ByteArrayOutputStream();
                StreamResult result = new StreamResult(baos);
                transformer.transform(source, result);
                baisXml = new ByteArrayInputStream(baos.toByteArray());
                struts.setContents(baisXml, true, false, monitor);
            } catch (Exception e) {
                throw e;
            } finally {
                if (isFile != null) {
                    isFile.close();
                }
                if (baisXml != null) {
                    baisXml.close();
                }
                if (baos != null) {
                    baos.close();
                }
            }
            success = true;
        }
    }

    /**
	 * 注射的成员信息
	 * @author shawn
	 *
	 */
    public class FieldInfo {

        public String field;

        public boolean isRef;

        public String value;

        public String clazz;

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((field == null) ? 0 : field.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            FieldInfo other = (FieldInfo) obj;
            if (!getOuterType().equals(other.getOuterType())) return false;
            if (field == null) {
                if (other.field != null) return false;
            } else if (!field.equals(other.field)) return false;
            return true;
        }

        private AddActionBean getOuterType() {
            return AddActionBean.this;
        }
    }

    private class ContentProvider implements IStructuredContentProvider {

        @Override
        public void dispose() {
        }

        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

        @SuppressWarnings("unchecked")
        @Override
        public Object[] getElements(Object inputElement) {
            if (inputElement instanceof List) {
                List<Object> list = (List<Object>) inputElement;
                Object[] val = new Object[list.size()];
                list.toArray(val);
                return val;
            } else {
                return new Object[0];
            }
        }
    }

    private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {

        @Override
        public Image getColumnImage(Object element, int index) {
            return null;
        }

        @Override
        public String getColumnText(Object element, int index) {
            if (element instanceof FieldInfo) {
                FieldInfo info = (FieldInfo) element;
                switch(index) {
                    case 0:
                        return "";
                    case 1:
                        return info.field;
                    case 2:
                        if (info.isRef) {
                            return "Y";
                        }
                        return "N";
                    case 3:
                        return info.value;
                    case 4:
                        return info.clazz;
                }
            }
            return null;
        }
    }
}
