package edu.ucdavis.genomics.metabolomics.binbase.gui.swt.dialog;

import java.util.Vector;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import edu.ucdavis.genomics.metabolomics.binbase.bci.server.types.ExperimentClass;
import edu.ucdavis.genomics.metabolomics.binbase.bdi.table.TableResizer;

/**
 * creates the import dialog
 * 
 * @author nase
 * 
 */
public class CreateImportDialog extends TitleAreaDialog {

    public CreateImportDialog(Shell parentShell) {
        super(parentShell);
    }

    private boolean experiment;

    private CheckboxTableViewer viewer;

    private Vector<ExperimentClass> classes = new Vector<ExperimentClass>();

    @Override
    protected Control createDialogArea(Composite parent) {
        parent.setLayout(new GridLayout(4, true));
        Table table = new Table(parent, SWT.BORDER | SWT.CHECK | SWT.V_SCROLL);
        table.setHeaderVisible(true);
        table.addControlListener(new TableResizer(table));
        table.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 4, 1));
        table.addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent e) {
                Table table = (Table) e.getSource();
                if (table.getSelectionIndex() > -1) {
                    ExperimentClass clazz = classes.get(table.getSelectionIndex());
                    CreateClassDialog dialog = new CreateClassDialog(getShell(), clazz);
                    if (dialog.open() == Dialog.OK) {
                        classes.set(table.getSelectionIndex(), dialog.getClazz());
                        viewer.setInput(classes);
                    }
                }
            }

            public void mouseDown(MouseEvent e) {
            }

            public void mouseUp(MouseEvent e) {
            }
        });
        ((GridData) table.getLayoutData()).minimumHeight = 200;
        ((GridData) table.getLayoutData()).heightHint = 200;
        TableColumn samples = new TableColumn(table, SWT.LEFT);
        samples.setWidth(200);
        table.setLinesVisible(true);
        samples.setText("defined classes");
        viewer = new CheckboxTableViewer(table);
        viewer.setColumnProperties(new String[] { "defined classes" });
        viewer.setContentProvider(new IStructuredContentProvider() {

            public Object[] getElements(Object inputElement) {
                return classes.toArray();
            }

            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
        });
        viewer.setLabelProvider(new ITableLabelProvider() {

            public Image getColumnImage(Object element, int columnIndex) {
                return null;
            }

            public String getColumnText(Object element, int columnIndex) {
                return ((ExperimentClass) element).getId();
            }

            public void addListener(ILabelProviderListener listener) {
            }

            public void dispose() {
            }

            public boolean isLabelProperty(Object element, String property) {
                return false;
            }

            public void removeListener(ILabelProviderListener listener) {
            }
        });
        viewer.setInput(classes);
        final Button check = new Button(parent, SWT.CHECK);
        check.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 1, 1));
        check.setText("also schedule as experimnt");
        check.setSelection(false);
        check.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent arg0) {
                setExperiment(check.getSelection());
            }

            public void widgetSelected(SelectionEvent arg0) {
                setExperiment(check.getSelection());
            }
        });
        Button add = new Button(parent, SWT.PUSH);
        add.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 1, 1));
        add.setText("add class");
        add.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                CreateClassDialog dialog = new CreateClassDialog(getShell());
                if (dialog.open() == Dialog.OK) {
                    classes.add(dialog.getClazz());
                    viewer.setInput(classes);
                }
            }
        });
        Button rm = new Button(parent, SWT.PUSH);
        rm.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 1, 1));
        rm.setText("remove class");
        rm.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                for (Object o : viewer.getCheckedElements()) {
                    classes.remove(o);
                }
                viewer.setInput(classes);
            }
        });
        Button load = new Button(parent, SWT.PUSH);
        load.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false, 1, 1));
        load.setText("load");
        load.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {
            }

            public void widgetSelected(SelectionEvent e) {
                LoadClassesFromFileDialog dialog = new LoadClassesFromFileDialog(getShell());
                if (dialog.open() == Dialog.OK) {
                    for (ExperimentClass clazz : dialog.getClasses()) {
                        classes.add(clazz);
                    }
                    viewer.setInput(classes);
                }
            }
        });
        this.setTitle("define import dialog");
        this.setMessage("please add imports to this experiment");
        return super.createDialogArea(parent);
    }

    public Vector<ExperimentClass> getClasses() {
        return this.classes;
    }

    @Override
    protected void okPressed() {
        if (this.classes.isEmpty()) {
            new MessageDialog(getShell(), "error", null, "please add atleast one class", MessageDialog.ERROR, new String[] { "ok" }, 0).open();
        } else {
            ValidateSamplesDialog dialog = new ValidateSamplesDialog(getShell(), this.classes);
            if (Dialog.OK == dialog.open()) {
                super.okPressed();
            } else {
                this.setMessage("validation process was cancelled...");
            }
        }
    }

    public boolean isExperiment() {
        return experiment;
    }

    public void setExperiment(boolean experiment) {
        this.experiment = experiment;
    }
}
