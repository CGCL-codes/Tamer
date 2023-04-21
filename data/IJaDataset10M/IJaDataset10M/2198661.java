package com.dsb.bar.barkas.admin;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import com.dsb.bar.barkas.admin.components.DataTable;

public class WijzigBonDialog extends org.eclipse.swt.widgets.Dialog {

    private DataTable samenvattingDataTable;

    private Table samenvattingTable;

    private Composite composite5;

    private DataTable bestellingDataTable;

    private String[][] prijslijst;

    private String[][] schenkCategorie;

    private Button VerwijderBestellingButton;

    private Button VoegToeButton;

    private Combo categorieCombo;

    private Text aantalText;

    private Combo bestellingWatCombo;

    private Label label1;

    private Table bestellingTable;

    private Composite composite4;

    private Composite composite3;

    private Composite composite2;

    private Composite composite1;

    private BarkasAdmin bkAdmin;

    private CTabItem bewerkingenTab;

    private CTabItem bestellingTab;

    private CTabFolder wijzigBonTab;

    private String bonID;

    private Button OKButton;

    private Shell dialogShell;

    private TableListener listener;

    public WijzigBonDialog(Shell parent, int style, String bon_ID, BarkasAdmin admin, TableListener listener) {
        super(parent, style);
        this.bonID = bon_ID;
        this.bkAdmin = admin;
        this.listener = listener;
    }

    /**
	* Opens the Dialog Shell.
	* Auto-generated code - any changes you make will disappear.
	*/
    public void open() {
        try {
            preInitGUI();
            Shell parent = getParent();
            dialogShell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
            dialogShell.setText(getText());
            wijzigBonTab = new CTabFolder(dialogShell, SWT.TOP | SWT.BORDER);
            bestellingTab = new CTabItem(wijzigBonTab, SWT.NULL);
            composite1 = new Composite(wijzigBonTab, SWT.NULL);
            composite2 = new Composite(composite1, SWT.NULL);
            bestellingTable = new Table(composite2, SWT.CHECK | SWT.FULL_SELECTION);
            composite3 = new Composite(composite1, SWT.BORDER);
            label1 = new Label(composite3, SWT.NULL);
            bestellingWatCombo = new Combo(composite3, SWT.DROP_DOWN | SWT.READ_ONLY);
            aantalText = new Text(composite3, SWT.BORDER);
            categorieCombo = new Combo(composite3, SWT.DROP_DOWN | SWT.READ_ONLY);
            VoegToeButton = new Button(composite3, SWT.PUSH | SWT.CENTER);
            composite4 = new Composite(composite1, SWT.NULL);
            VerwijderBestellingButton = new Button(composite4, SWT.PUSH | SWT.CENTER);
            bewerkingenTab = new CTabItem(wijzigBonTab, SWT.NULL);
            composite5 = new Composite(wijzigBonTab, SWT.NULL);
            samenvattingTable = new Table(composite5, SWT.SINGLE | SWT.FULL_SELECTION);
            OKButton = new Button(dialogShell, SWT.PUSH | SWT.CENTER);
            dialogShell.setText("Wijzig bon");
            dialogShell.setSize(new org.eclipse.swt.graphics.Point(619, 472));
            FormData wijzigBonTabLData = new FormData();
            wijzigBonTabLData.height = 378;
            wijzigBonTabLData.width = 602;
            wijzigBonTabLData.left = new FormAttachment(0, 1000, 6);
            wijzigBonTabLData.right = new FormAttachment(0, 1000, 612);
            wijzigBonTabLData.top = new FormAttachment(0, 1000, 7);
            wijzigBonTabLData.bottom = new FormAttachment(0, 1000, 409);
            wijzigBonTab.setLayoutData(wijzigBonTabLData);
            wijzigBonTab.setSize(new org.eclipse.swt.graphics.Point(602, 378));
            bestellingTab.setControl(composite1);
            bestellingTab.setText("Bestellingen");
            GridData composite2LData = new GridData();
            composite2LData.verticalAlignment = GridData.FILL;
            composite2LData.horizontalAlignment = GridData.FILL;
            composite2LData.widthHint = -1;
            composite2LData.heightHint = -1;
            composite2LData.horizontalIndent = 0;
            composite2LData.horizontalSpan = 1;
            composite2LData.verticalSpan = 1;
            composite2LData.grabExcessHorizontalSpace = true;
            composite2LData.grabExcessVerticalSpace = true;
            composite2.setLayoutData(composite2LData);
            composite2.setSize(new org.eclipse.swt.graphics.Point(592, 279));
            bestellingTable.setHeaderVisible(true);
            bestellingTable.setLinesVisible(true);
            bestellingTable.setSize(new org.eclipse.swt.graphics.Point(576, 267));
            bestellingTable.setLayout(null);
            bestellingTable.layout();
            FillLayout composite2Layout = new FillLayout(256);
            composite2.setLayout(composite2Layout);
            composite2Layout.type = SWT.HORIZONTAL;
            composite2Layout.marginWidth = 0;
            composite2Layout.marginHeight = 0;
            composite2Layout.spacing = 0;
            composite2.layout();
            GridData composite3LData = new GridData();
            composite3LData.verticalAlignment = GridData.END;
            composite3LData.horizontalAlignment = GridData.FILL;
            composite3LData.widthHint = -1;
            composite3LData.heightHint = 28;
            composite3LData.horizontalIndent = 0;
            composite3LData.horizontalSpan = 1;
            composite3LData.verticalSpan = 1;
            composite3LData.grabExcessHorizontalSpace = false;
            composite3LData.grabExcessVerticalSpace = false;
            composite3.setLayoutData(composite3LData);
            composite3.setSize(new org.eclipse.swt.graphics.Point(589, 28));
            RowData label1LData = new RowData(97, 22);
            label1.setLayoutData(label1LData);
            label1.setText("Voeg bestelling toe  ");
            label1.setSize(new org.eclipse.swt.graphics.Point(97, 22));
            RowData bestellingWatComboLData = new RowData(155, 21);
            bestellingWatCombo.setLayoutData(bestellingWatComboLData);
            bestellingWatCombo.setSize(new org.eclipse.swt.graphics.Point(155, 21));
            bestellingWatCombo.setLayout(null);
            bestellingWatCombo.layout();
            RowData aantalTextLData = new RowData(23, 13);
            aantalText.setLayoutData(aantalTextLData);
            aantalText.setText("0");
            aantalText.setSize(new org.eclipse.swt.graphics.Point(23, 13));
            RowData categorieComboLData = new RowData(77, 21);
            categorieCombo.setLayoutData(categorieComboLData);
            categorieCombo.setText("Schenk");
            categorieCombo.setSize(new org.eclipse.swt.graphics.Point(77, 21));
            categorieCombo.setLayout(null);
            categorieCombo.layout();
            RowData VoegToeButtonLData = new RowData(70, 23);
            VoegToeButton.setLayoutData(VoegToeButtonLData);
            VoegToeButton.setText("Voeg toe");
            VoegToeButton.setSize(new org.eclipse.swt.graphics.Point(70, 23));
            VoegToeButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent evt) {
                    VoegToeButtonWidgetSelected(evt);
                }
            });
            RowLayout composite3Layout = new RowLayout(256);
            composite3.setLayout(composite3Layout);
            composite3Layout.type = SWT.HORIZONTAL;
            composite3Layout.wrap = true;
            composite3Layout.pack = true;
            composite3Layout.fill = false;
            composite3Layout.justify = false;
            composite3Layout.spacing = 3;
            composite3Layout.marginLeft = 3;
            composite3Layout.marginTop = 3;
            composite3Layout.marginRight = 3;
            composite3Layout.marginBottom = 3;
            composite3Layout.marginWidth = 0;
            composite3Layout.marginHeight = 0;
            composite3.layout();
            GridData composite4LData = new GridData();
            composite4LData.verticalAlignment = GridData.END;
            composite4LData.horizontalAlignment = GridData.FILL;
            composite4LData.widthHint = -1;
            composite4LData.heightHint = 36;
            composite4LData.horizontalIndent = 0;
            composite4LData.horizontalSpan = 1;
            composite4LData.verticalSpan = 1;
            composite4LData.grabExcessHorizontalSpace = false;
            composite4LData.grabExcessVerticalSpace = false;
            composite4.setLayoutData(composite4LData);
            composite4.setSize(new org.eclipse.swt.graphics.Point(591, 36));
            RowData VerwijderBestellingButtonLData = new RowData(200, 30);
            VerwijderBestellingButton.setLayoutData(VerwijderBestellingButtonLData);
            VerwijderBestellingButton.setText("Verwijder gevinkte bestellingen");
            VerwijderBestellingButton.setSize(new org.eclipse.swt.graphics.Point(200, 30));
            VerwijderBestellingButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent evt) {
                    VerwijderBestellingButtonWidgetSelected(evt);
                }
            });
            RowLayout composite4Layout = new RowLayout(256);
            composite4.setLayout(composite4Layout);
            composite4Layout.type = SWT.HORIZONTAL;
            composite4Layout.wrap = true;
            composite4Layout.pack = true;
            composite4Layout.fill = false;
            composite4Layout.justify = false;
            composite4Layout.spacing = 3;
            composite4Layout.marginLeft = 3;
            composite4Layout.marginTop = 3;
            composite4Layout.marginRight = 3;
            composite4Layout.marginBottom = 3;
            composite4Layout.marginWidth = 0;
            composite4Layout.marginHeight = 0;
            composite4.layout();
            GridLayout composite1Layout = new GridLayout(1, true);
            composite1.setLayout(composite1Layout);
            composite1Layout.marginWidth = 5;
            composite1Layout.marginHeight = 5;
            composite1Layout.numColumns = 1;
            composite1Layout.makeColumnsEqualWidth = true;
            composite1Layout.horizontalSpacing = 5;
            composite1Layout.verticalSpacing = 5;
            composite1.layout();
            bewerkingenTab.setControl(composite5);
            bewerkingenTab.setText("Samenvatting");
            samenvattingTable.setHeaderVisible(true);
            samenvattingTable.setLinesVisible(true);
            samenvattingTable.setLayout(null);
            samenvattingTable.layout();
            FillLayout composite5Layout = new FillLayout(256);
            composite5.setLayout(composite5Layout);
            composite5Layout.type = SWT.HORIZONTAL;
            composite5Layout.marginWidth = 0;
            composite5Layout.marginHeight = 0;
            composite5Layout.spacing = 0;
            composite5.layout();
            wijzigBonTab.setLayout(null);
            wijzigBonTab.layout();
            wijzigBonTab.setSelection(0);
            FormData OKButtonLData = new FormData();
            OKButtonLData.height = 40;
            OKButtonLData.width = 60;
            OKButtonLData.left = new FormAttachment(0, 1000, 545);
            OKButtonLData.right = new FormAttachment(0, 1000, 605);
            OKButtonLData.top = new FormAttachment(0, 1000, 420);
            OKButtonLData.bottom = new FormAttachment(0, 1000, 460);
            OKButton.setLayoutData(OKButtonLData);
            OKButton.setText("Sluiten");
            OKButton.setSize(new org.eclipse.swt.graphics.Point(60, 40));
            OKButton.addSelectionListener(new SelectionAdapter() {

                public void widgetSelected(SelectionEvent evt) {
                    OKButtonWidgetSelected(evt);
                }
            });
            FormLayout dialogShellLayout = new FormLayout();
            dialogShell.setLayout(dialogShellLayout);
            dialogShellLayout.marginWidth = 0;
            dialogShellLayout.marginHeight = 0;
            dialogShell.layout();
            Rectangle bounds = dialogShell.computeTrim(0, 0, 619, 472);
            dialogShell.setSize(bounds.width, bounds.height);
            postInitGUI();
            dialogShell.open();
            Display display = dialogShell.getDisplay();
            while (!dialogShell.isDisposed()) {
                if (!display.readAndDispatch()) display.sleep();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
				* Add your pre-init code in here
				*/
    public void preInitGUI() {
    }

    /**
			* Add your post-init code in here
			*/
    public void postInitGUI() {
        dialogShell.setText("Wijzig bon (" + bonID + ")");
        bestellingDataTable = new DataTable(bestellingTable, TableListener.BESTELLINGEN_TABLE, listener);
        samenvattingDataTable = new DataTable(samenvattingTable, TableListener.BON_SAMENVATTING_TABLE, listener);
        bestellingDataTable.setOtherID(bonID);
        samenvattingDataTable.setOtherID(bonID);
        fillVoegBestellingToeCombos();
        fillBestellingenTable();
        fillSamenvattingTable();
    }

    protected void fillVoegBestellingToeCombos() {
        prijslijst = bkAdmin.getPrijslijst(bonID);
        if (prijslijst != null && prijslijst.length == 2) {
            bestellingWatCombo.removeAll();
            for (int i = 0; i < prijslijst[0].length; i++) {
                bestellingWatCombo.add(prijslijst[1][i], i);
            }
            if (bestellingWatCombo.getItemCount() > 0) bestellingWatCombo.select(0);
        }
        schenkCategorie = new String[2][];
        schenkCategorie[0] = new String[6];
        schenkCategorie[1] = new String[6];
        schenkCategorie[0][0] = "Bestelling_AantalS";
        schenkCategorie[1][0] = "Schenk";
        schenkCategorie[0][1] = "Bestelling_AantalSI";
        schenkCategorie[1][1] = "SI";
        schenkCategorie[0][2] = "Bestelling_AantalS225";
        schenkCategorie[1][2] = "S225";
        schenkCategorie[0][3] = "Bestelling_AantalS50";
        schenkCategorie[1][3] = "S50";
        schenkCategorie[0][4] = "Bestelling_AantalS80";
        schenkCategorie[1][4] = "S80";
        schenkCategorie[0][5] = "Bestelling_AantalEUR";
        schenkCategorie[1][5] = "EUR";
        categorieCombo.removeAll();
        for (int i = 0; i < 6; i++) categorieCombo.add(schenkCategorie[1][i], i);
        if (categorieCombo.getItemCount() > 0) categorieCombo.select(0);
    }

    /** Auto-generated event handler method */
    protected void OKButtonWidgetSelected(SelectionEvent evt) {
        dialogShell.close();
    }

    public void fillBestellingenTable() {
        bestellingDataTable.forceReloadData();
    }

    public void fillSamenvattingTable() {
        samenvattingDataTable.forceReloadData();
    }

    /** Auto-generated event handler method */
    protected void VerwijderBestellingButtonWidgetSelected(SelectionEvent evt) {
        int idColumn = -1;
        for (int i = 0; i < bestellingTable.getColumnCount(); i++) {
            if (bestellingTable.getColumn(i).getText().equalsIgnoreCase("Bestelling_ID")) {
                idColumn = i;
                break;
            }
        }
        if (idColumn == -1) return;
        for (int i = 0; i < bestellingTable.getItemCount(); i++) {
            TableItem ti = bestellingTable.getItem(i);
            if (ti.getChecked()) bkAdmin.removeBestelling(ti.getText(idColumn));
        }
        fillBestellingenTable();
    }

    /** Auto-generated event handler method */
    protected void VoegToeButtonWidgetSelected(SelectionEvent evt) {
        String wat = prijslijst[0][bestellingWatCombo.getSelectionIndex()];
        String hoeveel = aantalText.getText();
        String categorie = schenkCategorie[0][categorieCombo.getSelectionIndex()];
        try {
            Integer.parseInt(hoeveel);
        } catch (NumberFormatException e) {
            aantalText.setText("0");
            return;
        }
        if (bkAdmin.voegBestellingToe(bonID, wat, hoeveel, categorie)) {
            fillBestellingenTable();
            bestellingWatCombo.select(0);
            aantalText.setText("0");
            categorieCombo.select(0);
        }
    }

    /** Auto-generated event handler method */
    protected void bestellingTableWidgetSelected(SelectionEvent evt) {
    }
}
