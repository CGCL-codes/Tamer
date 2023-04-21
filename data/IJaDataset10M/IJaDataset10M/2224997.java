package orcajo.azada.browser.wizard2;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class FormatStringPage extends AbstractWizardPage {

    private TableViewer viewer;

    private Text text;

    private Button bPersonalized;

    private boolean initialized;

    FormatStringPage() {
        super("FormatStringPage");
        setTitle(Messages.FormatStringPage_Title);
        setDescription(Messages.FormatStringPage_Description);
    }

    @Override
    public boolean isPageComplete() {
        return true;
    }

    @Override
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NONE);
        container.setLayout(new GridLayout());
        container.setLayoutData(new GridData(GridData.FILL_BOTH));
        Composite cTable = new Composite(container, SWT.NONE);
        createTable(cTable);
        bPersonalized = new Button(container, SWT.CHECK);
        bPersonalized.setText(Messages.FormatStringPage_Customize);
        bPersonalized.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        bPersonalized.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                boolean isSelected = ((Button) e.widget).getSelection();
                viewer.getTable().setEnabled(!isSelected);
                text.setEnabled(isSelected);
                if (isSelected) {
                    getModel().formatString = text.getText();
                } else {
                    if (viewer.getSelection().isEmpty()) {
                        getModel().formatString = "";
                    } else {
                        getModel().formatString = viewer.getSelection().toString();
                    }
                }
            }
        });
        text = new Text(container, SWT.BORDER | SWT.READ_ONLY);
        text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        text.setEnabled(false);
        bExpert = new Button(container, SWT.CHECK);
        bExpert.setText(Messages.FormatStringPage_ExpertMode);
        bExpert.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                if (((Button) e.widget).getSelection()) {
                    getModel().srcToExpertPage = FormatStringPage.this;
                } else {
                    getModel().srcToExpertPage = null;
                    ((WhithMembersWizard) getWizard()).expertPage.initialized = false;
                }
                updateButtons();
            }
        });
        setControl(container);
    }

    private void createTable(Composite cTable) {
        cTable.setLayout(new TableColumnLayout());
        cTable.setLayoutData(new GridData(GridData.FILL_BOTH));
        viewer = new TableViewer(cTable, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
        viewer.setLabelProvider(new LabelProvider());
        viewer.setContentProvider(new FSContentProvider());
        viewer.getTable().setHeaderVisible(false);
        viewer.getTable().addMouseListener(new MouseAdapter() {

            public void mouseUp(MouseEvent e) {
                ViewerCell cell = viewer.getCell(new Point(e.x, e.y));
                if (cell != null) {
                    Object element = cell.getElement();
                    if (element instanceof String) {
                        getModel().formatString = element.toString();
                    }
                }
            }
        });
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible && !initialized) {
            initialized = true;
            viewer.setInput(Model.format_string_samples);
            viewer.setSelection(new StructuredSelection(Model.format_string_samples[0]));
            getModel().formatString = Model.format_string_samples[0];
        }
    }

    class FSContentProvider implements IStructuredContentProvider {

        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

        public Object[] getElements(Object inputElement) {
            if (inputElement instanceof Object[]) {
                return (Object[]) inputElement;
            }
            return new String[0];
        }

        public void dispose() {
        }
    }
}
