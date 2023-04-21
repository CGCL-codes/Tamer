package ch.hsr.orm.codegen.ui.debug.launching;

import java.util.Properties;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import ch.hsr.orm.codegen.ui.Messages;
import ch.hsr.orm.preferences.PreferenceConstants;
import ch.hsr.orm.preferences.PreferencesActivator;

public class MappingDialog extends Dialog {

    private Shell top;

    private TableColumn column1;

    private TableColumn column2;

    private Button btnEdit;

    private Table table;

    private Button btnOK;

    private Button btnCancel;

    private Group group;

    private CCombo profileCombo;

    private Properties mappings;

    private int lastSelection;

    private static final String[] comboContent = new String[] { Messages.MappingDialog_0, Messages.MappingDialog_1, Messages.MappingDialog_2 };

    public MappingDialog(Shell parent, Properties mappings) {
        this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        this.mappings = mappings;
    }

    public MappingDialog(Shell parent, int style) {
        super(parent, style);
        this.setText(Messages.MappingDialog_3);
    }

    public Properties open() {
        Shell shell = new Shell(getParent(), getStyle());
        shell.setText(getText());
        createContents(shell);
        shell.pack();
        shell.open();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        return mappings;
    }

    private void createContents(Shell shell) {
        top = shell;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        top.setText(Messages.MappingDialog_4);
        top.setLayout(gridLayout);
        createGroup();
        createTable();
        createTableColumns();
        createEditButton();
        GridData fillerGrid = new GridData();
        fillerGrid.grabExcessVerticalSpace = true;
        fillerGrid.horizontalSpan = 2;
        Label filler1 = new Label(shell, SWT.NONE);
        filler1.setLayoutData(fillerGrid);
        createButtonBar();
        doLoad();
        top.setDefaultButton(btnOK);
    }

    private void createButtonBar() {
        GridData okGrid = new GridData(GridData.END, GridData.CENTER, true, false);
        okGrid.widthHint = 80;
        btnOK = new Button(top, SWT.NONE);
        btnOK.setText(Messages.MappingDialog_5);
        btnOK.setLayoutData(okGrid);
        btnOK.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                mappings = generateProperties();
                top.close();
            }
        });
        GridData cancelGrid = new GridData();
        cancelGrid.widthHint = 80;
        btnCancel = new Button(top, SWT.NONE);
        btnCancel.setText(Messages.MappingDialog_6);
        btnCancel.setLayoutData(cancelGrid);
        btnCancel.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                top.close();
            }
        });
    }

    private void createTable() {
        GridData tableGrid = new GridData(GridData.FILL, GridData.CENTER, false, false);
        tableGrid.heightHint = 330;
        tableGrid.widthHint = 300;
        table = new Table(top, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION | SWT.V_SCROLL);
        table.setLinesVisible(true);
        table.setLayoutData(tableGrid);
        table.setHeaderVisible(true);
        table.addMouseListener(new MouseAdapter() {

            public void mouseDoubleClick(MouseEvent e) {
                editMapping();
            }
        });
        table.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(ControlEvent e) {
                int tableWidth = table.getSize().x - (2 * table.getBorderWidth()) - table.getVerticalBar().getSize().x;
                column1.setWidth(tableWidth / 2);
                column2.setWidth(tableWidth - column1.getWidth());
            }
        });
    }

    private void createTableColumns() {
        column1 = new TableColumn(table, SWT.NONE);
        column1.setText(Messages.MappingDialog_7);
        column2 = new TableColumn(table, SWT.NONE);
        column2.setText(Messages.MappingDialog_8);
    }

    private void createEditButton() {
        GridData buttonGrid = new GridData(GridData.CENTER, GridData.BEGINNING, false, false);
        buttonGrid.widthHint = 80;
        btnEdit = new Button(top, SWT.PUSH);
        btnEdit.setText(Messages.MappingDialog_9);
        btnEdit.setLayoutData(buttonGrid);
        btnEdit.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                editMapping();
            }
        });
    }

    private void createGroup() {
        GridData comboGrid = new GridData(GridData.FILL, GridData.CENTER, true, false);
        GridLayout profileLayout = new GridLayout();
        profileLayout.numColumns = 1;
        GridData groupGrid = new GridData(GridData.FILL, GridData.CENTER, true, false);
        groupGrid.horizontalSpan = 2;
        group = new Group(top, SWT.NONE);
        group.setText(Messages.MappingDialog_10);
        group.setLayout(profileLayout);
        group.setLayoutData(groupGrid);
        profileCombo = new CCombo(group, SWT.NONE);
        profileCombo.setLayoutData(comboGrid);
        for (String elem : comboContent) {
            profileCombo.add(elem);
        }
        profileCombo.select(0);
        lastSelection = profileCombo.getSelectionIndex();
        profileCombo.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                switch(profileCombo.getSelectionIndex()) {
                    case 0:
                        doLoad();
                        lastSelection = 0;
                        break;
                    case 1:
                        doLoadDefaults(PreferenceConstants.GORM_PREFIX);
                        lastSelection = 1;
                        break;
                    case 2:
                        doLoadDefaults(PreferenceConstants.JPA_PREFIX);
                        lastSelection = 2;
                        break;
                }
            }
        });
    }

    private void doLoadDefaults(String prefix) {
        if (lastSelection == 0) {
            mappings = generateProperties();
        }
        IPreferenceStore store = PreferencesActivator.getDefault().getPreferenceStore();
        table.removeAll();
        for (String datatype : PreferenceConstants.getDatatypes()) {
            new TableItem(table, SWT.NONE).setText(new String[] { datatype, store.getDefaultString(prefix + datatype) });
        }
    }

    private void doLoad() {
        table.removeAll();
        for (String datatype : PreferenceConstants.getDatatypes()) {
            new TableItem(table, SWT.NONE).setText(new String[] { datatype, mappings.getProperty(datatype) });
        }
    }

    private Properties generateProperties() {
        Properties props = new Properties();
        for (TableItem item : table.getItems()) {
            props.setProperty(item.getText(0), item.getText(1));
        }
        return props;
    }

    private void editMapping() {
        TableItem item = table.getItem(table.getSelectionIndex());
        MappingInputDialog dialog = new MappingInputDialog(top.getShell(), item.getText(0), item.getText(1));
        String newMapping = dialog.open();
        item.setText(new String[] { item.getText(0), newMapping });
        mappings = generateProperties();
        profileCombo.select(0);
    }
}
